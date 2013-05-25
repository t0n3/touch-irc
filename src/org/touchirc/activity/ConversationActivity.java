package org.touchirc.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.touchirc.R;
import org.touchirc.fragments.ConversationFragment;
import org.touchirc.irc.IrcBinder;
import org.touchirc.model.Conversation;
import org.touchirc.model.Server;

import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingFragmentActivity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;

import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

public class ConversationActivity extends SlidingFragmentActivity implements ServiceConnection {

    private IrcBinder ircServiceBind;
    private Set<Server> connectedServers;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retrieve currently connected servers
        this.connectedServers = this.ircServiceBind.getService().getConnectedServers();
        
        // set the content view
        setContentView(R.layout.conversation_display);
        
        // set the viewpager
        ViewPager vp = new ViewPager(this);
		vp.setId("VP".hashCode());
		vp.setAdapter(new ConversationPagerAdapter(getSupportFragmentManager()));
		setContentView(vp);

		vp.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageScrollStateChanged(int arg0) { }

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) { }

			@Override
			public void onPageSelected(int position) {
				switch (position) {
				case 0:
					getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
					break;
				default:
					getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
					break;
				}
			}

		});
		
		vp.setCurrentItem(0);
        
        // configure the SlidingMenu
        getSlidingMenu().setMode(SlidingMenu.LEFT);
        getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        getSlidingMenu().setShadowWidthRes(R.dimen.sliding_shadow_width);
        getSlidingMenu().setShadowDrawable(R.drawable.shadow);
        getSlidingMenu().setBehindOffsetRes(R.dimen.slidingmenu_offset);
        getSlidingMenu().setFadeDegree(0.35f);
        getSlidingMenu().attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		getSlidingMenu().setMenu(R.layout.connected_servers);

    }
    
    /**
     * Initialize TabView for a specified server
     */
    public void initConversation(Server s){
    	if(s.hasConversation()){
    		
    	}
    }
    
    public void updateConversation(){
    	
    }
    
    public void sendMessage(){
        
    }

    
    @Override
    public void onServiceDisconnected(ComponentName name) {
        this.ircServiceBind = null;
    }


    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        this.ircServiceBind = (IrcBinder) service;
    }

    /**
     * ConversationPagerAdapter
     * In charge to handle conversation fragment for swiping in ConversationActivity
     * @author tone
     */
    public class ConversationPagerAdapter extends FragmentPagerAdapter {
        
        private ArrayList<Fragment> mFragments;

        public ConversationPagerAdapter(FragmentManager fm) {
            super(fm);
            mFragments = new ArrayList<Fragment>();
            mFragments.add(new ConversationFragment());
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