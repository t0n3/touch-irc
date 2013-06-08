package org.touchirc.adapter;

import java.util.ArrayList;

import org.touchirc.activity.ConversationActivity;
import org.touchirc.fragments.ConversationFragment;
import org.touchirc.model.Server;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
* ConversationPagerAdapter
* In charge to handle conversation fragment for swiping in ConversationActivity
*/
public class ConversationPagerAdapter extends FragmentPagerAdapter {

	private ArrayList<Fragment> mFragments;
	private Server server;

	public ConversationPagerAdapter(FragmentManager fm, Server s) {
	    super(fm);
	    server = s;
	    mFragments = new ArrayList<Fragment>();
	    for(String c : server.getAllConversations()) {
	        System.out.println(server.getConversation(c).getTitle());
	        mFragments.add(new ConversationFragment(server.getConversation(c)));
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
	

	@Override
	public CharSequence getPageTitle(int position){
		return server.getAllConversations().get(position);
	}

}