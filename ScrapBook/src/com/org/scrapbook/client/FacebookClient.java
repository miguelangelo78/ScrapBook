package com.org.scrapbook.client;

import com.org.cache.JCache;
import com.org.scrapbook.global.FaceGlobal;
import com.org.scrapbook.object.User;
import com.org.uniscraper.misc.Util;
import com.org.uniscraper.scraper.WebScraper;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FacebookClient implements FaceGlobal {
	 private WebScraper facecraper;
	 private String myID;
	  
	 public FacebookClient() {
		 WebScraper.manual_auth = true;
		 facecraper = new WebScraper(L_LOGIN, null, true);
	   
		 getMyUsername();
	 }
  
	@SuppressWarnings("unchecked")
	public <T> T get(String path, Class<T> classtype, Object... params) {
		String[] path_split = path.split("/");
    
		String who_what = path_split[0];
		
		// Grab user/page:
		String link_user = L_HOME;
		
		boolean is_ID = Util.isNumeric(who_what);
		if (who_what.toLowerCase().equals("me")) 
			link_user += myID;
		else
			link_user += (is_ID?"profile.php?id=" : "") + who_what;
		
		if (path_split.length == 1) {
			User new_user = new User();
			new_user.construct(facecraper, link_user + (is_ID?"%2Fabout&sk=about&section=contact-info&pnref=about" : "/about?section=contact-info&pnref=about"), who_what);
			return (T) new_user;
		}else{
			switch(path_split[1].toLowerCase()){
				case S_FRIENDS:
					int start = 0;
					int length = -1;
					try{ start = (int) params[0]; } catch(Exception e){}
					try{ length = (int) params[1]; } catch(Exception e){}
					
					if(path_split.length==3)
						return (T) User.constructFriends(facecraper, link_user + (is_ID?"&sk=friends":"/friends"), who_what, Integer.parseInt(path_split[2]), 1).get(0);
					else
						return (T) User.constructFriends(facecraper, link_user + (is_ID?"&sk=friends":"/friends"), who_what, start, length);
				case S_FEED: break;
				case S_HOME: break;
				case S_PAGE: break;
				case S_PUBLISH: break;
				case S_SEARCH: break;
				case S_UNPUBLISH: break;
			}
		}
		return null;
  }
  
  	public void publish(String content, Object parameters) {}
  
  	public void unpublish(String obj_id) {}
  
	public void end() {
		facecraper.end();
	}
  
	private void getMyUsername() {
		// Use cache to load my username:
		if(JCache.isInCache(JCache.cache_auth_name, "me")){
			myID = (String) JCache.get(JCache.cache_auth_name, "me");
			return;
		}
		
		facecraper.scrape(L_LOGIN, L_HOME, null, "{'my_username': '._2dpe' }");
	  
		Matcher match = Pattern.compile("\\/.+?\\/(.+?)$").matcher(facecraper.elem("my_username", 0).attr("href"));
		if (match.find()) 
			myID = match.group(1);
		
		// If the username doesn't exist prepend : "profile.php?id="
		
		JCache.put(JCache.cache_auth_name, "me", myID);
	}
	
	// Friends functions:
	public ArrayList<User> updateAllFriends(ArrayList<User> friendsList){
		for(int i=0;i<friendsList.size();i++)
			friendsList.set(i, get(friendsList.get(i).getUsername(), User.class));
		return friendsList;
	}
	
	public User updateFriend(User friend){
		return (friend = get(friend.getUsername(), User.class));
	}
}
