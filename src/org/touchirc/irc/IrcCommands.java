package org.touchirc.irc;

import java.util.ArrayList;

public class IrcCommands {

	// Display a message in action mode on all channels in which I'm connected
	public static String ACTION = "me";
	// Display a message on all channels in which I'm connected
	public static String MSG_ALL_CHANNEL = "amsg";
	// Ban an user
	public static String BAN = "ban";

	public static String CTCP = "ctcp";

	public static String ENABLE = "enable";
	// Quit IRC by closing each connection correctly
	public static String QUIT = "quit";

	// like QUIT
	public static String EXIT = "exit";

	public static String HELP = "help";
	// Invite an user on the channel . OP status (Operator) compulsory on the channel from which you invit
	public static String INVIT = "invite"; // pseudo>
	// Ignore totally an user, you can't see him talking
	public static String IGNORE = "ignore"; // pseudo>
	// Disable IGNORE
	public static String UNIGNORE = "unignore"; // pseudo>

	public static String JOIN_CHANNEL = "join"; // channel>

	public static String KICK_FROM_CHANNEL = "kick"; // pseudo> [reason compulsory]
	// Ban and throw away an user from a channel
	public static String KICK_BAN = "kickban"; // pseudo> [reason compulsory]

	public static String LIST_CHANNEL_ON_CURRENT_SERVER = "list";

	public static String LOG = "log";

	// Private Message to <pseudo>	
	public static String QUERY = "msg"; // pseudo> [message]

	public static String CHANGE_NICK = "nick"; // newNick>
	// Warn when the specific user is connected
	public static String NOTIFY_CONNEXION = "notify"; // pseudo>
	// Send a notice to <pseudo> which will be read by <pseudo> only
	public static String NOTICE = "notice"; // pseudo> [message]

	public static String OP_RIGHTS_TO_USER = "op";

	// Leave a channel
	public static String PART = "part";

	public static String CONNECT_TO_SERVER = "server"; // server[:port]

	public static String CHANGE_TOPIC = "topic"; // message>
	// Starting private messaging with <pseudo>
	public static String STARTING_QUERY = "query"; // pseudo> [message]

	public static String WHO = "who";
	// Give informations about <pseudo>
	public static String WHOIS = "whois"; // pseudo>
	// Same but when <pseudo> is disconnected
	public static String WHOWAS = "whowas"; // pseudo>

	public static final String[] ALL_COMMANDS = {		
		ACTION,
		BAN,
		CHANGE_NICK,
		CHANGE_TOPIC,
		CONNECT_TO_SERVER,
		CTCP,
		ENABLE,
		EXIT,
		HELP,
		IGNORE,
		INVIT,
		JOIN_CHANNEL,
		KICK_BAN,
		KICK_FROM_CHANNEL,
		PART,
		LIST_CHANNEL_ON_CURRENT_SERVER,
		LOG,
		MSG_ALL_CHANNEL,
		NOTICE,
		NOTIFY_CONNEXION,
		OP_RIGHTS_TO_USER,
		QUERY,
		QUIT,
		STARTING_QUERY,
		UNIGNORE,
		WHO,
		WHOIS,
		WHOWAS		
	};

}
