package com.org.scrapbook.global;

public abstract interface FaceGlobal
{
  public static final String L_LOGIN = "https://www.facebook.com/login.php?login_attempt=1";
  public static final String L_HOME = "https://www.facebook.com/";
  
  public static final String ME = "me";
  
  public static final String T_ME_ID_USR = "{'fullName':'#fb-timeline-cover-name','id':'#pagelet_timeline_main_column','personal':'#pagelet_basic > div > .uiList > ._3pw9','profile_pic':'.profilePic'}";
  public static final String T_ME_ID_USR_LIVING = "{'location':'#current_city, #hometown'}";
  public static final String T_ME_ID_USR_RELATIONSHIP = "{'relations':'#pagelet_relationships > div'}";
  public static final String T_FRIENDS = "{'friends':'#collection_wrapper_2356318349 > div > ul > li'}";
  
  public static String S_FRIENDS = "friends";
  public static String S_PAGE = "page";
  public static String S_FEED = "feed";
  public static String S_HOME = "home";
  public static String S_SEARCH = "search";
  public static String S_PUBLISH = "publish";
  public static String S_UNPUBLISH = "unpublish";
  
  public static int MAX_FRIENDS = 40;
}
