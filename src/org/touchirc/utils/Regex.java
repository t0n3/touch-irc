package org.touchirc.utils;

import java.util.regex.Pattern;

public class Regex {
	private final static String special = "\\[\\]`^{}|_\\\\";

	public static boolean isAValidNickName(String nickname){
		Pattern pattern = Pattern.compile("^[a-zA-Z0-9"+special+"][a-zA-Z0-9"+special+"-]*");
		return pattern.matcher(nickname).matches();
	}
}
