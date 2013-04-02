package org.touchirc.irc;

public class IrcCommands {
	
	// Display a message in action mode on all channels in which I'm connected
	public String ACTION_MODE_MSG = "/ame ";
	// Display a message on all channels in which I'm connected
	public String MSG_ALL_CHANNEL = "/amsg ";
	// Ban an user
	public String BAN = "/ban ";
	
	public String CTCP = "/ctcp ";
	
	public String ENABLE = "/enable";
	// Quit IRC by closing each connection correctly
	public String QUIT = "/quit";
	// the same but leave a message before closing
	public String QUIT_WITH_MSG = "/quit ["; // message + ] after the message
	// like QUIT
	public String EXIT = "/exit";
	
	public String HELP = "/help";
	// Invite an user on the channel . OP status (Operator) compulsory on the channel from which you invit
	public String INVIT = "/invite <"; // pseudo>
	// Ignore totally an user, you can't see him talking
	public String IGNORE = "/ignore <"; // pseudo>
	// Disable IGNORE
	public String UNIGNORE = "/unignore <"; // pseudo>
	
	public String JOIN_CHANNEL = "/join <"; // channel>
	
	public String KICK_FROM_CHANNEL = "/kick <"; // pseudo> [reason compulsory]
	// Ban and throw away an user from a channel
	public String KICK_BAN = "/kickban <"; // pseudo> [reason compulsory]
	// Leave a channel
	public String LEAVE_FROM_THE_CHANNEL = "/leave";
	
	public String LEAVE_FROM_ANOTHER_CHANNEL = "/leave <#"; // channel>
	
	public String LIST_CHANNEL_ON_CURRENT_SERVER = "/list";
	
	public String LOG = "/log";
	// Private Message to <pseudo>
	public String QUERY = "/msg <"; // pseudo> [message]
	
	public String CHANGE_NICK = "/nick <"; // newNick>
	// Warn when the specific user is connected
	public String NOTIFY_CONNEXION = "/notify <"; // pseudo>
	// Send a notice to <pseudo> which will be read by <pseudo> only
	public String NOTICE = "/notice <"; // pseudo> [message]
	
	public String OP_RIGHTS_TO_USER = "/op";
	
	public String CONNECT_TO_SERVER = "/server "; // server[:port]
	
	public String CHANGE_TOPIC_ON_CURRENT_CHANNEL = "/topic <"; // message>
	
	public String CHANGE_TOPIC_ON_ANOTHER_CHANNEL = "/topic <"; // #channel><message>
	// Starting private messaging with <pseudo>
	public String STARTING_QUERY = "/query <"; // pseudo> [message]
	
	public String WHO = "/who";
	// Give informations about <pseudo>
	public String WHOIS = "/whois <"; // pseudo>
	// Same but when <pseudo> is disconnected
	public String WHOWAS = "/whowas <"; // pseudo>
	
}
