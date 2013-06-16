package org.touchirc.fragments;

import org.touchirc.R;
import org.touchirc.adapter.ExpandableUserAdapter;
import org.touchirc.irc.IrcService;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

@SuppressLint("ValidFragment")
public class ConnectedUsersFragment extends Fragment {

	private static IrcService ircService;
	private ExpandableUserAdapter userAdapter;
	private ExpandableListView usersLV;

	public ConnectedUsersFragment(){}

	public ConnectedUsersFragment(IrcService service){
		ircService = service;
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		// Set initial connectedUserCount Textview
		((TextView)getActivity().findViewById(R.id.connectedUserCount)).setText(getResources().getString(R.string.users) + " (" + ircService.getCurrentChannel().getUsers().size() + ")");

		// construct the RelativeLayout
		RelativeLayout v = new RelativeLayout(getActivity());

		// Construct the ExpandableListView
		this.usersLV = (ExpandableListView) container.findViewById(R.id.connectedUserExpandableListView);

		usersLV.setVerticalFadingEdgeEnabled(false);

		// No child Divider
		usersLV.setChildDivider(getResources().getDrawable(android.R.color.transparent));

		userAdapter = new ExpandableUserAdapter(usersLV, ircService, getActivity(), (TextView)getActivity().findViewById(R.id.connectedUserCount));
		usersLV.setAdapter(userAdapter);

		usersLV.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				switch(childPosition){

				case 0 :

					// Instantiate an AlertDialog.Builder with its constructor
					AlertDialog.Builder builderOPC = new AlertDialog.Builder(getActivity());

					// Chain together various setter methods to set the dialog characteristics
					builderOPC.setTitle(R.string.OPsCommands)
					.setItems(R.array.OPs_Commands, new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO handle Commands
						}
					});

					// Get the AlertDialog from create()
					AlertDialog dialogOPC = builderOPC.create();
					dialogOPC.show();

					break;

				case 1 :

					// Instantiate an AlertDialog.Builder with its constructor
					AlertDialog.Builder builderWhoIs = new AlertDialog.Builder(getActivity());

					String about = getResources().getString(R.string.about);
					// Chain together various setter methods to set the dialog characteristics
					builderWhoIs.setTitle(about + " " + userAdapter.getGroup(groupPosition).getNick())
					.setMessage("\nPiece of information coming soon ...\n");
					// TODO Manage whois command

					// Get the AlertDialog from create()
					AlertDialog dialogWhoIs = builderWhoIs.create();
					dialogWhoIs.show();

					break;

				case 2 :

					// TODO Switch on good fragment

					break;

				default :
					return false;

				}
				return true;
			}
		});

		usersLV.setGroupIndicator(new ColorDrawable(android.R.color.transparent));

		return v;
	}
	
	public ExpandableUserAdapter getAdapter() {
		return userAdapter;
	}

}
