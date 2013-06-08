package org.touchirc.activity;

import org.touchirc.R;
import org.touchirc.adapter.ConversationPagerAdapter;
import org.touchirc.irc.IrcBinder;
import org.touchirc.irc.IrcService;
import org.touchirc.model.Server;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.method.TextKeyListener;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.slidingmenu.lib.SlidingMenu;

public class ConversationActivity extends SherlockFragmentActivity implements ServiceConnection {

    private IrcService ircService;
    private Server currentServer;
    private ViewPager vp;
    private SlidingMenu menu;
    private EditText inputMessage;
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setTitle(R.string.app_name);
		
		// set the content view
        setContentView(R.layout.conversation_display);
           
        // configure the SlidingMenu
        menu = new SlidingMenu(this);
        menu.setMode(SlidingMenu.LEFT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        menu.setShadowWidthRes(R.dimen.sliding_shadow_width);
        menu.setShadowDrawable(R.drawable.shadow);
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        menu.setFadeDegree(0.35f);
        menu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
		menu.setMenu(R.layout.connected_servers);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.ircService = null;
        Intent intent = new Intent(this, IrcService.class);
        //  getApplicationContext().startService(intent);
        if(getApplicationContext().bindService(intent, this, Context.BIND_AUTO_CREATE)){
            System.out.println("Bind to Service");
        }

        // set the viewpager
        this.vp = (ViewPager) findViewById(R.id.vp);
        this.vp.setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int arg0) { }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) { }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                case 0:
                    menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
                    break;
                default:
                    menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
                    break;
                }
  
                ircService.setCurrentChannel(ircService.getBot(currentServer).getChannel(currentServer.getAllConversations().get(position)));
            }

        });
        this.vp.setCurrentItem(0);
        
        // Clean if it's useless :)
        PagerTabStrip vpTabViewer = (PagerTabStrip) findViewById(R.id.vpTabViewer);
        
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
    
    public void sendMessage() {
    	ircService.getBot(currentServer).sendMessage(ircService.getCurrentChannel(), inputMessage.getText().toString());
    	TextKeyListener.clear(inputMessage.getText());
    }
    

    @Override
    public void onServiceDisconnected(ComponentName name) {
        this.ircService = null;        
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder binder) {
        this.ircService = ((IrcBinder) binder).getService();
        // Retrieve the currently connected server
        this.currentServer = this.ircService.getCurrentServer();
        this.vp.setAdapter(new ConversationPagerAdapter(getSupportFragmentManager(), this.currentServer));
        // Set the Activity title
        this.setTitle(currentServer.getName());
        // Set the current channel (by default, when launching it's 0
        ircService.setCurrentChannel(ircService.getBot(currentServer).getChannel(currentServer.getAllConversations().get(0)));
    }   

}