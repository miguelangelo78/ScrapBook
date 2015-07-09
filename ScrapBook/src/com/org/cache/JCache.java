package com.org.cache;

import java.util.Set;

import org.openqa.selenium.Cookie;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

public class JCache {
	  public static CacheManager cacheman;
	  public static String cache_cookiename = "facebook_auth_cookies";
	  public static String cache_auth_name = "facebook_auth_cache";
	  
	  public static boolean isInCache(String cacheName, String key){
		  return cacheman.getCache(cacheName).isKeyInCache(key);
	  }
	  
	  public static void put(String cacheName, String key, Object obj){
		  cacheman.getCache(cacheName).put(new Element(key, obj));
	  }
	  
	  public static Object get(String cacheName, String key){
		  Element el = cacheman.getCache(cacheName).get(key);
		  return (el == null ? null : el.getObjectValue());
	  }
	  
	  public static void save_cookies_to_cache(Set<Cookie> cookies){
		  cacheman.getCache(cache_auth_name).put(new Element(cache_cookiename, cookies));
	  }
	  
	  @SuppressWarnings("unchecked")
	  public static Set<Cookie> load_cookies_from_cache(){
		  return (Set<Cookie>) cacheman.getCache(cache_auth_name).get(cache_cookiename).getObjectValue();
	  }
	  
	  public static void initCache(){
	  	cacheman = CacheManager.getInstance();
	  }
	  
}
