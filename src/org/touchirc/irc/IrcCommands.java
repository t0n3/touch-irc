package org.touchirc.irc;

import java.util.ArrayList;

public class IrcCommands {

	// Display a message in action mode on all channels in which I'm connected
	public static String ACTION_MODE_MSG = "/ame message";
	// Display a message on all channels in which I'm connected
	public static String MSG_ALL_CHANNEL = "/amsg message";
	// Ban an user
	public static String BAN = "/ban user";

	public static String CTCP = "/ctcp ";

	public static String ENABLE = "/enable";
	// Quit IRC by closing each connection correctly
	public static String QUIT = "/quit";
	// the same but leave a message before closing
	public static String QUIT_WITH_MSG = "/quit message"; // message]
	// like QUIT
	public static String EXIT = "/exit";

	public static String HELP = "/help";
	// Invite an user on the channel . OP status (Operator) compulsory on the channel from which you invit
	public static String INVIT = "/invite pseudo"; // pseudo>
	// Ignore totally an user, you can't see him talking
	public static String IGNORE = "/ignore pseudo"; // pseudo>
	// Disable IGNORE
	public static String UNIGNORE = "/unignore pseudo"; // pseudo>

	public static String JOIN_CHANNEL = "/join channel"; // channel>

	public static String KICK_FROM_CHANNEL = "/kick pseudo reason"; // pseudo> [reason compulsory]
	// Ban and throw away an user from a channel
	public static String KICK_BAN = "/kickban pseudo reason"; // pseudo> [reason compulsory]
	// Leave a channel
	public static String LEAVE_FROM_THE_CHANNEL = "/leave";

	public static String LEAVE_FROM_ANOTHER_CHANNEL = "/leave #channel"; // channel>

	public static String LIST_CHANNEL_ON_CURRENT_SERVER = "/list";

	public static String LOG = "/log";

	// Private Message to <pseudo>	
	public static String QUERY = "/msg pseudo message"; // pseudo> [message]

	public static String CHANGE_NICK = "/nick newNick"; // newNick>
	// Warn when the specific user is connected
	public static String NOTIFY_CONNEXION = "/notify pseudo"; // pseudo>
	// Send a notice to <pseudo> which will be read by <pseudo> only
	public static String NOTICE = "/notice pseudo message"; // pseudo> [message]

	public static String OP_RIGHTS_TO_USER = "/op ";

	public static String CONNECT_TO_SERVER = "/server server[port]"; // server[:port]

	public static String CHANGE_TOPIC_ON_CURRENT_CHANNEL = "/topic message"; // message>

	public static String CHANGE_TOPIC_ON_ANOTHER_CHANNEL = "/topic #channel message"; // #channel><message>
	// Starting private messaging with <pseudo>
	public static String STARTING_QUERY = "/query pseudo message"; // pseudo> [message]

	public static String WHO = "/who";
	// Give informations about <pseudo>
	public static String WHOIS = "/whois pseudo"; // pseudo>
	// Same but when <pseudo> is disconnected
	public static String WHOWAS = "/whowas pseudo"; // pseudo>

	// MODES AVAILABLES FOR CHANNELS

	// give/take channel operator rights
	public static String GIVE_OP_RIGHTS = "/MODE #channel +o user";
	public static String TAKE_OP_RIGHTS = "/MODE #channel -o user";

	// flag for a private channel
	public static String FLAG_PRIVATE_CHANNEL = "/MODE #channel +p";
	public static String UNFLAG_PRIVATE_CHANNEL = "/MODE #channel -p";

	// flag for a secret channel
	public static String FLAG_SECRET_CHANNEL = "/MODE #channel +s";
	public static String UNFLAG_SECRET_CHANNEL = "/MODE #channel -s";

	// flag for a channel with access by invitation only
	public static String FLAG_RESTRICTED_ACCESS_CHANNEL = "/MODE #channel +i";
	public static String UNFLAG_RESTRICTED_ACCESS_CHANNEL = "/MODE #channel -i";

	// flag for a channel subject alterable only by operators
	public static String FLAG_CHAN_SUBJECT_ALTER = "/MODE #channel +t";
	public static String UNFLAG_CHAN_SUBJECT_ALTER = "/MODE #channel -t";

	// no messages in a channel from clients outside the channel
	public static String NO_MSG_FROM_UNKNOWN_PERS_IN_THE_CHAN = "/MODE #channel +n";

	// moderated channel
	public static String MODERATED_CHANNEL = "/MODE #channel +m";
	public static String UNMODERATED_CHANNEL = "/MODE #channel -m";

	// defines the max number of person in a channel
	public static String MAX_PERS_IN_THE_CHANNEL = "/MODE #channel +l number";

	// list ban mask to restrict access to users
	public static String BAN_MASK_ON_CHANNEL = "/MODE &channel +b";

	// Nobody can join the channel
	public static String NOBODY_JOIN_CHAN = "/MODE &channel +b *!*@*";

	// Forbid the access to channel to a specific person
	public static String FORBID_CHAN_ACCESS_TO = "/MODE &channel +b *!*@ user";

	// give/take the opportunity to speak on a moderated channel
	public static String GIVE_SPEAK_RIGHT_ON_MOD_CHAN = "/MODE #channel +v user";
	public static String TAKE_SPEAK_RIGHT_ON_MOD_CHAN = "/MODE #channel -v user";

	// defines the key (password) of a channel
	public static String SET_KEY_CHANNEL = "/MODE #channel +k password";

	// MODES AVAILABLES FOR USERS

	// Become invisible
	public static String SET_INVISIBLE = "/MODE user +i";
	public static String SET_VISIBLE = "/MODE user -i";

	// Mark the person as receiving notifications on the server
	public static String SET_RECEIV_NOTIF_ON_SERV = "/MODE user +s";
	public static String SET_NOT_RECEIV_NOTIF_ON_SERV = "/MODE user -s";

	// allow/delete the WALLOPS messages reception for a specific person
	public static String SET_RECEIV_WALLOPS_MESSAGES = ":MODE user +w";
	public static String SET_UNRECEIV_WALLOPS_MESSAGES = ":MODE user -w";

	// Delete operator rights
	public static String DEL_OP_RIGHTS = "MODE user -o";

	private ArrayList<String> commandsList;

	public IrcCommands(){
		this.commandsList = new ArrayList<String>();
		// Currents commands
		commandsList.add(ACTION_MODE_MSG);
		commandsList.add(BAN);
		commandsList.add(CHANGE_NICK);
		commandsList.add(CHANGE_TOPIC_ON_ANOTHER_CHANNEL);
		commandsList.add(CHANGE_TOPIC_ON_CURRENT_CHANNEL);
		commandsList.add(CONNECT_TO_SERVER);
		commandsList.add(CTCP);
		commandsList.add(ENABLE);
		commandsList.add(EXIT);
		commandsList.add(HELP);
		commandsList.add(IGNORE);
		commandsList.add(INVIT);
		commandsList.add(JOIN_CHANNEL);
		commandsList.add(KICK_BAN);
		commandsList.add(KICK_FROM_CHANNEL);
		commandsList.add(LEAVE_FROM_ANOTHER_CHANNEL);
		commandsList.add(LEAVE_FROM_THE_CHANNEL);
		commandsList.add(LIST_CHANNEL_ON_CURRENT_SERVER);
		commandsList.add(LOG);
		commandsList.add(MSG_ALL_CHANNEL);
		commandsList.add(NOTICE);
		commandsList.add(NOTIFY_CONNEXION);
		commandsList.add(OP_RIGHTS_TO_USER);
		commandsList.add(QUERY);
		commandsList.add(QUIT);
		commandsList.add(QUIT_WITH_MSG);
		commandsList.add(STARTING_QUERY);
		commandsList.add(UNIGNORE);
		commandsList.add(WHO);
		commandsList.add(WHOIS);
		commandsList.add(WHOWAS);

		// Modes for Channels
		commandsList.add(GIVE_OP_RIGHTS);
		commandsList.add(TAKE_OP_RIGHTS);
		commandsList.add(FLAG_PRIVATE_CHANNEL);
		commandsList.add(UNFLAG_PRIVATE_CHANNEL);
		commandsList.add(FLAG_SECRET_CHANNEL);
		commandsList.add(UNFLAG_SECRET_CHANNEL);
		commandsList.add(FLAG_RESTRICTED_ACCESS_CHANNEL);
		commandsList.add(UNFLAG_RESTRICTED_ACCESS_CHANNEL);
		commandsList.add(FLAG_CHAN_SUBJECT_ALTER);
		commandsList.add(UNFLAG_CHAN_SUBJECT_ALTER);
		commandsList.add(NO_MSG_FROM_UNKNOWN_PERS_IN_THE_CHAN);
		commandsList.add(MODERATED_CHANNEL);
		commandsList.add(UNMODERATED_CHANNEL);
		commandsList.add(MAX_PERS_IN_THE_CHANNEL);
		commandsList.add(BAN_MASK_ON_CHANNEL);
		commandsList.add(NOBODY_JOIN_CHAN); 
		commandsList.add(FORBID_CHAN_ACCESS_TO);
		commandsList.add(GIVE_SPEAK_RIGHT_ON_MOD_CHAN);
		commandsList.add(TAKE_SPEAK_RIGHT_ON_MOD_CHAN); 
		commandsList.add(SET_KEY_CHANNEL);
		
		// Modes for users
		commandsList.add(SET_INVISIBLE);
		commandsList.add(SET_VISIBLE);
		commandsList.add(SET_RECEIV_NOTIF_ON_SERV);
		commandsList.add(SET_NOT_RECEIV_NOTIF_ON_SERV);
		commandsList.add(SET_RECEIV_WALLOPS_MESSAGES);
		commandsList.add(SET_UNRECEIV_WALLOPS_MESSAGES);
		commandsList.add(DEL_OP_RIGHTS);
	}

	// Use to take the syntax of the command
	public String getCommand(int number){
		return commandsList.get(number);
	}

}
