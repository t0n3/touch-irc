package org.touchirc.db;

import android.provider.BaseColumns;

public interface DBConstants extends BaseColumns {
	
	public static final String SERVER_TABLE_NAME = "servers";
	public static final String SERVER_ID		 = "id";
	public static final String SERVER_TITLE 	 = "title";
    public static final String SERVER_HOST 		 = "host";
    public static final String SERVER_PORT 		 = "port";
    public static final String SERVER_PASSWORD 	 = "password";
    public static final String SERVER_USE_SSL 	 = "useSSL";
    public static final String SERVER_CHARSET 	 = "charset";
	
    
    public static final String[] SERVER_ALL = {
        SERVER_ID,
        SERVER_TITLE,
        SERVER_HOST,
        SERVER_PORT,
        SERVER_PASSWORD,
        SERVER_USE_SSL,
        SERVER_CHARSET
    };
    
    
}
