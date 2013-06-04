package org.touchirc.activity;

import java.util.ArrayList;
import java.util.Set;

import org.touchirc.R;
import org.touchirc.fragments.ConversationFragment;
import org.touchirc.irc.IrcBinder;
import org.touchirc.irc.IrcService;
import org.touchirc.model.Server;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.slidingmenu.lib.SlidingMenu;

public class ConversationActivity extends SherlockFragmentActivity implements ServiceConnection {

    private IrcService ircService;
    private Server currentServer;
    private ViewPager vp;
    private SlidingMenu menu;
    
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
            }

        });
        this.vp.setCurrentItem(0);
        
        // Set the EditText
        EditText e = (EditText) findViewById(R.id.input);

        
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
        this.vp.setAdapter(new ConversationPagerAdapter(getSupportFragmentManager(), this.currentServer.getAllConversations()));
    }

    /**
     * ConversationPagerAdapter
     * In charge to handle conversation fragment for swiping in ConversationActivity
     */
    public class ConversationPagerAdapter extends FragmentPagerAdapter {
        
        private ArrayList<Fragment> mFragments;

        public ConversationPagerAdapter(FragmentManager fm, ArrayList<String> conv) {
            super(fm);
            mFragments = new ArrayList<Fragment>();
            for(String c : conv) {
                System.out.println(ConversationActivity.this.currentServer.getConversation(c).getTitle());
            	mFragments.add(new ConversationFragment(ConversationActivity.this.currentServer.getConversation(c)));
            }
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

    }

}