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

import com.slidingmenu.lib.SlidingMenu;

public class ConversationActivity extends BaseSlidingActivity implements ServiceConnection {

    private IrcService ircService;
    private Server currentServer;
    private ViewPager vp;
    
    public ConversationActivity() {
		super(R.string.titleConversationDefault);
	}

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.ircService = null;
        Intent intent = new Intent(this, IrcService.class);
        //  getApplicationContext().startService(intent);
        if(getApplicationContext().bindService(intent, this, Context.BIND_AUTO_CREATE)){
            System.out.println("OK SERVICE BINDÉ");
        }

        
        setBehindContentView(R.layout.conversation_display);
        // set the viewpager
        this.vp = new ViewPager(this);
        this.vp.setId("VP".hashCode());
        setContentView(this.vp);
        this.vp.setOnPageChangeListener(new OnPageChangeListener() {
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
        this.vp.setCurrentItem(0);

    }
    
    // TODO
    public void updateConversation(){
    	
    }
    
    // TODO
    public void sendMessage(){
        
    }

    
    @Override
    public void onServiceDisconnected(ComponentName name) {
        this.ircService = null;        
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder binder) {
        this.ircService = ((IrcBinder) binder).getService();
        System.out.println("T'AS LE SERVICE MOFO");

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

        public ConversationPagerAdapter(FragmentManager fm, Set<String> conv) {
            super(fm);
            mFragments = new ArrayList<Fragment>();
            for(String c : conv)
            	mFragments.add(new ConversationFragment(ConversationActivity.this.currentServer.getConversation(c)));
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