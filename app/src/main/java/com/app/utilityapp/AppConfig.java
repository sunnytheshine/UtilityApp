package com.app.utilityapp;

/**
 * This is the config file for all global and static paths, filename and URL's
 * Created by sukhvindersingh on 2017-09-16.
 */

import com.raizlabs.android.dbflow.annotation.Database;

@Database(name = AppConfig.NAME, version = AppConfig.VERSION)

public class AppConfig {
    public static String GLOBAL_PATH            = "com.app.utilityapp"; //Main Default directory of app
    public static String CONFIG_FILE            = "com.config"; //Main config file to manage UI x&y axis


    public static String TORCH_STATUS           = "TORCH";
    public static String GLOBAL_FILE_USERDETAIL = "comuserdetail.json"; // USer details - IP address, name etc
    public static String GLOBAL_FILE_PLAYSTORE  = "playstore.json"; // Playstore config file

    static final String DB_NAME                 = "utilityBD.db"; //Database Name
    static final String DB_TABLE                = "size"; //Table Name

    public static final String NAME             = "RestClientDatabase";
    public static final int VERSION             = 1;

    public static int PERMISSION_REQUEST_CODE   = 5469; //Permission request code
}
