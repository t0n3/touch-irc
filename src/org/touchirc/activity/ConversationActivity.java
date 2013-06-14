package org.touchirc.activity;

import java.util.Arrays;

import org.touchirc.R;
import org.touchirc.adapter.ConversationPagerAdapter;
import org.touchirc.fragments.ConnectedServersFragment;
import org.touchirc.fragments.ConnectedUsersFragment;
import org.touchirc.fragments.ConversationFragment;
import org.touchirc.irc.IrcBinder;
import org.touchirc.irc.IrcBot;
import org.touchirc.irc.IrcCommands;
import org.touchirc.irc.IrcService;
import org.touchirc.model.Conversation;
import org.touchirc.model.Message;
import org.touchirc.model.Server;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.method.TextKeyListener;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.slidingmenu.lib.SlidingMenu;

public class ConversationActivity extends SherlockFragmentActivity implements ServiceConnection {

    private IrcService ircService;
    private Server currentServer;
    private ViewPager vp;
    private SlidingMenu menu;
    private EditText inputMessage;
    private ConversationPagerAdapter cPagerAdapter;
    private ConnectedUsersFragment connectedUserFragment;
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setTitle(R.string.app_name);
        
        // set the content view
        setContentView(R.layout.conversation_display);
           
        // configure the SlidingMenu
        menu = new SlidingMenu(this);
        menu.setMode(SlidingMenu.LEFT_RIGHT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        menu.setShadowWidthRes(R.dimen.sliding_shadow_width);
        menu.setShadowDrawable(R.drawable.shadow);
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        menu.setFadeDegree(0.35f);
        menu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
        menu.setMenu(R.layout.connected_servers);
        menu.setSecondaryMenu(R.layout.connected_users);
        menu.setSecondaryShadowDrawable(R.drawable.shadow_right);

        // Show "home" button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Bind the service
        ircService = null;
        Intent intent = new Intent(this, IrcService.class);
        getApplicationContext().bindService(intent, this, Context.BIND_AUTO_CREATE);

        // Set the viewpager
        vp = (ViewPager) findViewById(R.id.vp);
        vp.setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int arg0) { }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) { }

            @Override
            public void onPageSelected(int position) {
                ircService.setCurrentChannel(ircService.getBot(currentServer).getChannel(currentServer.getAllConversations().get(position)));
                // Refresh right menu when changing channel
                connectedUserFragment.getAdapter().notifyDataSetChanged();
            }
        });
        vp.setCurrentItem(0); // Default to 0
                
        // Set the EditText
        inputMessage = (EditText) findViewById(R.id.input);
        inputMessage.setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    sendMessage();
                    handled = true;
                }
                return handled;
            }
        });
        
    }
    
    /**
     * sendMessage
     * send the text in the EditText. If the first character is a '/'  the text is passed to sendCommand
     */
    public void sendMessage() {
        String mMessage = inputMessage.getText().toString();
        Conversation mCurrentConversation = currentServer.getConversation(ircService.getCurrentChannel().getName());
        String mAuthor = ircService.getBot(currentServer).getNick();
        
        if(mMessage.charAt(0) == '/') {
            sendCommand(mMessage);
        } else {
            // First, add message to the app
            mCurrentConversation.addMessage(new Message(mMessage, mAuthor, Message.TYPE_MESSAGE));
            // Second, send the message to the network
            ircService.getBot(currentServer).sendMessage(ircService.getCurrentChannel().getName(), inputMessage.getText().toString());
        }
        
        // Clean the edit text, important !
        TextKeyListener.clear(inputMessage.getText());
        
        // Refresh all these things
        ircService.sendBroadcast(new Intent("org.touchirc.irc.newMessage"));
    }
    
    /**
     * sendCommand
     * detect if the string is a command and send it.
     */
    public void sendCommand(String command){
        String[] args = command.split(" ");
        String cmd = args[0].substring(1, args[0].length()).toLowerCase();
        IrcBot bot = ircService.getBot(currentServer);
        args = Arrays.copyOfRange(args, 1, args.length);
        
        for(String c : IrcCommands.ALL_COMMANDS){
            if(c.equals(cmd)){
                // Join Channel
                if(cmd.equals(IrcCommands.JOIN_CHANNEL)) {
                    if(args.length < 2){
                        bot.joinChannel(args[0]);
                    } else {
                        bot.joinChannel(args[0], args[1]);
                    }
                // Action (/me)
                } else if(cmd.equals(IrcCommands.ACTION)){
                	bot.sendAction(ircService.getCurrentChannel(), Arrays.toString(args).replaceAll("(\\[)|(,)|(\\])", ""));
                }
            }
        }
    }
    

    @Override
    public void onServiceDisconnected(ComponentName name) {
        ircService = null;        
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder binder) {
        ircService = ((IrcBinder) binder).getService();
        
        // Retrieve the currently connected server
        currentServer = ircService.getCurrentServer();
        
        // Add the pager adapter
        cPagerAdapter = new ConversationPagerAdapter(getSupportFragmentManager(), this.currentServer);
        vp.setAdapter(cPagerAdapter);
        
        // Set the Activity title
        setTitle(currentServer.getName());
        
        // Set the current channel (by default, when launching it's 0
        ircService.setCurrentChannel(ircService.getBot(currentServer).getChannel(currentServer.getAllConversations().get(0)));
        
        final ConnectedServersFragment connectedServerFragment = new ConnectedServersFragment(ircService);
        connectedUserFragment = new ConnectedUsersFragment(ircService);
        
        // Register a new Broadcast Receiver to update the list of Fragments when channels states change
        BroadcastReceiver channelReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String lastConv = currentServer.getLastConversationName();
                cPagerAdapter.addFragment(new ConversationFragment(currentServer.getConversation(lastConv)));
                connectedServerFragment.getAdapter().notifyDataSetChanged();
                connectedUserFragment.getAdapter().notifyDataSetChanged();
                ircService.setCurrentChannel(ircService.getBot(currentServer).getChannel(lastConv));
            }    
        };
        registerReceiver(channelReceiver , new IntentFilter("org.touchirc.irc.channellistUpdated"));
        
        // Register a new Broadcast Receiver to update the list of Fragments when userList states change
        BroadcastReceiver userReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
            	((TextView)findViewById(R.id.connectedUserCount)).setText(getResources().getString(R.string.users) + " (" + ircService.getCurrentChannel().getUsers().size() + ")");
                connectedUserFragment.getAdapter().notifyDataSetChanged();
            }    
        };
        registerReceiver(userReceiver , new IntentFilter("org.touchirc.irc.userlistUpdated"));
        
        // Replace the left and right menu of slidingMenu by fragments
        getSupportFragmentManager().beginTransaction().replace(R.id.connectedServerLayout, connectedServerFragment).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.connectedUserLayout, connectedUserFragment).commit();
    }  
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Enable the back button
        // In the future, I think MenuActivity will be removed :)
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Intent intent = new Intent(this, MenuActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {
        // Toggle slidingMenu left if you click on the "home" icon
        switch (item.getItemId()) {
        case android.R.id.home:
            menu.toggle();
            return true;
        }
        return super.onOptionsItemSelected((android.view.MenuItem) item);
    }
    
    /**
     * Show to fragment with the position given to the user
     * Used by ConnectedServersFragment
     */
    public void setCurrentConversation(int positon){
        this.vp.setCurrentItem(positon);
        menu.showContent();
    }

}