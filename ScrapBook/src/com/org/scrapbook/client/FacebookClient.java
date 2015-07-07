package com.org.scrapbook.client;

import com.org.scrapbook.global.FaceGlobal;
import com.org.scrapbook.object.User;
import com.org.uniscraper.misc.Util;
import com.org.uniscraper.scraper.WebScraper;

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
	public <T> T getObject(String objectName, Class<T> classtype, Object... params) {
		String[] content_split = objectName.split("/");
    
		String who_what = content_split[0];
		
		if (content_split.length == 1) {
			String link_user = L_HOME;
			
			if (who_what.toLowerCase().equals("me")) 
				link_user = link_user + myID;
			else{
				boolean is_ID = Util.isNumeric(who_what);
				link_user = link_user + (is_ID?"profile.php?id=" : "") + who_what + (is_ID ? "%2Fabout&sk=about&section=contact-info&pnref=about" : "/about?section=contact-info&pnref=about" );
			}
			User new_user = new User();
			System.out.println(link_user);
			new_user.construct(facecraper, link_user, who_what);
			return (T) new_user;
		}else{
			
		}
    return null;
  }
  
  	public void publish(String content, Object parameters) {}
  
  	public void unpublish(String obj_id) {}
  
	public void end() {
		facecraper.end();
	}
  
	private void getMyUsername() {
		facecraper.scrape(L_LOGIN, L_HOME, null, "{'my_username': '._2dpe' }");
	  
		Matcher match = Pattern.compile("\\/.+?\\/(.+?)$").matcher(facecraper.elem("my_username", 0).attr("href"));
		if (match.find()) 
			myID = match.group(1)+"/about?section=contact-info&pnref=about";
		
		// If the username doesn't exist prepend : "profile.php?id="
	}
}
