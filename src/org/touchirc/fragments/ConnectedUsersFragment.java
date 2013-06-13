package org.touchirc.fragments;

import org.touchirc.R;
import org.touchirc.adapter.UsersListAdapter;
import org.touchirc.irc.IrcService;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

@SuppressLint("ValidFragment")
public class ConnectedUsersFragment extends Fragment {

	private static IrcService ircService;
	private UsersListAdapter userListAdapter;

	public ConnectedUsersFragment(){}
		
	public ConnectedUsersFragment(IrcService service){
		ircService = service;
	}
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		userListAdapter = new UsersListAdapter(ircService, getActivity());

		// construct the RelativeLayout
		RelativeLayout v = new RelativeLayout(getActivity());
		
		// Construct the ListView
		ListView usersLV = new ListView(getActivity());
		
		usersLV.setPadding(10, 10, 10, 10);
		usersLV.setVerticalFadingEdgeEnabled(false);
		
		usersLV.setAdapter(userListAdapter);
		usersLV.setBackgroundColor(getResources().getColor(R.color.red));
		v.addView(usersLV);
		return v;
	}

	public UsersListAdapter getAdapter() {
		return userListAdapter;
	}
	
}
