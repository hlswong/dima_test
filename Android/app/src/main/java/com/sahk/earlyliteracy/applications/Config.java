package com.sahk.earlyliteracy.applications;

public class Config {

//        public static final String SERVER_BASE_URL = "http://sahk.uat01.kanhan-uat.com/";  //SIT
    public static final String SERVER_BASE_URL = "http://sahk.kanhan.com/";  //Production
//        public static final String GAME_JSON = "/json/game_n.json";   // SIT
//    public static final String GAME_JSON = "/json/game_n_rex.json";
//    public static final String GAME_JSON = "/json/game.json";      //Old Production
    public static final String GAME_JSON = "/json/game_n.json";      //New Production
    public static final String ADV_JSON = "/json/adv.json";
    public static final String VIDEO_JSON = "/json/video.json"; // NOT USED

    public static String AD_SHOW = "N";
    public static String AD_IMG = "";
    public static String AD_URL = "";  // Edit by Rex   Remove final + String

    public static final int TOOLBAR_STATUS_HIDE = 0;
    public static final int TOOLBAR_STATUS_THEME = 2;
    public static final int TOOLBAR_STATUS_VIDEO = 3;
    public static final int TOOLBAR_STATUS_MENU = 4;
    public static final int TOOLBAR_STATUS_AWARD = 5;
    public static final int TOOLBAR_STATUS_GAMEINTRO = 6;
    public static final int TOOLBAR_STATUS_GAME = 7;
    public static final int TOOLBAR_STATUS_GAMEEND = 8;
    public static final int TOOLBAR_STATUS_SETTINGS = 99;
    public static final int TOOLBAR_STATUS_AWARD_FROM_GAME = 9; //Added By Rex

    public static final int VIDEO_THEME = 0;
    public static final int VIDEO_SONG = 1;
    public static final int VIDEO_JQANDA = 2;
    public static final int VIDEO_SQANDAA = 3;

    public static final int NEWCOMER_SCORE = 10;

    public static final String THEME1_NAME = "我的身體";
    public static final String THEME2_NAME = "我的家人";
    public static final String THEME3_NAME = "我愛閱讀";
    public static final String THEME4_NAME = "我的學校";
    public static final String THEME5_NAME = "大自然真美麗";
    public static final String THEME6_NAME = "環保";




}
