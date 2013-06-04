package org.touchirc.adapter;

import java.util.ArrayList;

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

	public ConversationPagerAdapter(FragmentManager fm, Server s) {
	    super(fm);
	    mFragments = new ArrayList<Fragment>();
	    for(String c : s.getAllConversations()) {
	        System.out.println(s.getConversation(c).getTitle());
	        mFragments.add(new ConversationFragment(s.getConversation(c)));
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