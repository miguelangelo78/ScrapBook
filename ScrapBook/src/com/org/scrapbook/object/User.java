package com.org.scrapbook.object;

import com.org.cache.JCache;
import com.org.scrapbook.global.FaceGlobal;
import com.org.uniscraper.jsengine.EngineCallback;
import com.org.uniscraper.jsengine.PhantomJS;
import com.org.uniscraper.misc.Util;
import com.org.uniscraper.scraper.WebScraper;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.JavascriptExecutor;

public class User implements FaceGlobal {
	public boolean isPrivate() {
		return isPrivate;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getFullName() {
		return fullName;
	}

	public String getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public String getUrl() {
		return url;
	}

	public String getBirthday() {
		return birthday;
	}

	public String getEmail() {
		return email;
	}

	public String getGender() {
		return gender;
	}

	public String getPictureURL() {
		return pictureURL;
	}

	public String getLocation() {
		return location;
	}

	public String getRelationshipStatus(){
		return relationshipStatus;
	}
	
	public void setPrivate(boolean isPrivate) {
		this.isPrivate = isPrivate;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public void setPictureURL(String pictureURL) {
		this.pictureURL = pictureURL;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void setRelationshipStatus(String relationshipStatus) {
		this.relationshipStatus = relationshipStatus;
	}
	
	public String getFriendCount() {
		return friendCount;
	}

	public void setFriendCount(String friendCount) {
		this.friendCount = friendCount;
	}

	private boolean isPrivate = true;
	private String firstName;
	private String lastName;
	private String fullName;
	private String id;
	private String username;
	private String url;
	private String birthday;
	private String email;
	private String gender;
	private String pictureURL;
	private String location;
	private String relationshipStatus;
	private String friendCount;
	
	/*
	// Irrelevant:
	private String employer;
	private String position;
	private String jobStartDate;
	private String jobEndDate;
	private String description;
	
	private String religion; // NO
	private String website;	// NO
	private String SO;
	private String bio;	// NO
	private String quotes;	// NO
	private List<String> interestedIn = new ArrayList();
	private List<String> meetingFor = new ArrayList();
	private List<String> work = new ArrayList();
	private List<String> sports = new ArrayList();
	private List<String> favoriteTeams = new ArrayList();
	private List<String> favoriteAthletes = new ArrayList();
	private List<String> languages = new ArrayList();
	private boolean verified;
  */
	
	private String cacheUser = "facebook_object_user";
	private static String cacheFriends = "facebook_list_friends";
	private static String cacheUserPrefix ="usr_";
	public static int global_start = 0; // Because the get callback is out of scope
	
	public void load_usr_from_cache(String key){
		key+="_";
		fullName = (String) JCache.get(cacheUser, key+"fullName");
		firstName = (String) JCache.get(cacheUser, key+"firstName");
		lastName = (String) JCache.get(cacheUser, key+"lastName");
		
		id = (String) JCache.get(cacheUser, key+"id");
		username = (String) JCache.get(cacheUser, key+"username");
		
		url = (String) JCache.get(cacheUser, key+"url");
		
		birthday = (String) JCache.get(cacheUser, key+"birthday");
		
		gender = (String) JCache.get(cacheUser, key+"gender");
		
		email = (String) JCache.get(cacheUser, key+"email");
		
		pictureURL = (String) JCache.get(cacheUser, key+"pictureURL");
		
		location = (String) JCache.get(cacheUser, key+"location");
		
		relationshipStatus = (String) JCache.get(cacheUser, key+"relationshipStatus");
		
		isPrivate = (boolean) JCache.get(cacheUser, key+"isPrivate");
	}
	
	private void cache_user(String cache_usr_name){
		JCache.put(cacheUser, cache_usr_name, "alive_and_updated");
		cache_usr_name += "_";
		JCache.put(cacheUser, cache_usr_name+"fullName", fullName);
		JCache.put(cacheUser, cache_usr_name+"firstName", firstName);
		JCache.put(cacheUser, cache_usr_name+"lastName", lastName);
		JCache.put(cacheUser, cache_usr_name+"id", id);
		JCache.put(cacheUser, cache_usr_name+"username", username);
		JCache.put(cacheUser, cache_usr_name+"url", url);
		JCache.put(cacheUser, cache_usr_name+"birthday", birthday);
		JCache.put(cacheUser, cache_usr_name+"gender", gender);
		JCache.put(cacheUser, cache_usr_name+"email", email);
		JCache.put(cacheUser, cache_usr_name+"pictureURL", pictureURL);
		JCache.put(cacheUser, cache_usr_name+"location", location);
		JCache.put(cacheUser, cache_usr_name+"relationshipStatus", relationshipStatus);
		JCache.put(cacheUser, cache_usr_name+"isPrivate", isPrivate);
	}
	
	public void construct(WebScraper scraper, String user_page_url, String username) {
		
		// Check if user lives in cache
		String cache_usr_name = cacheUserPrefix+username;
		if(JCache.isInCache(cacheUser, cache_usr_name))
			if(((String)JCache.get(cacheUser, cache_usr_name)).equals("alive_and_updated")){
				// If he is there AND updated, load his data instead of scraping from facebook
				load_usr_from_cache(cache_usr_name);
				return;
			}
	
		// He's not there/he's just been id'd, fetch the data from face
		scraper.scrape(L_LOGIN, user_page_url, null, T_ME_ID_USR);
		
		// Name:
		fullName = scraper.elem("fullName", 0).text().replaceAll("\\(.+?\\)", "").trim();
		String[] fullNameSplit = this.fullName.split(" ");
		firstName = fullNameSplit[0];
		lastName = fullNameSplit[(fullNameSplit.length - 1)];
		
		// Identification:
		Matcher match = Pattern.compile("\"profile_owner\":\"(.+?)\",").matcher(scraper.elem("id", 0).attr("data-gt"));
		if (match.find())
			id = match.group(1);
		
		this.username = username;
		
		url = user_page_url.replaceAll("about\\?.+$", "");
		
		// Birthday:
		try{
			birthday = ((Element)scraper.elem("personal", 0).select("._50f4").get(1)).text();
		}catch(Exception e){}
		
		try {
			gender = scraper.elem("personal", 1).select("._50f4").get(1).text();
		
			if (Util.containsInt(gender))
				gender = null;
		} catch (Exception e) {
			// Fix bug (gender being on birthday's field)
			try{
				gender = scraper.elem("personal", 0).select("._50f4").get(1).text();
				if(gender!=null)
					if(Util.containsInt(gender)) gender = null;
					else birthday = null;
			}catch(Exception e2){}
		}
		
		// Email:
		email = id+"@facebook.com";
		
		// Picture URL:
		pictureURL = scraper.elem("profile_pic", 0).attr("src");
		
		// Now go to another page (location):
		scraper.scrape(L_LOGIN, user_page_url.replaceAll("=contact-info","=living"), null, T_ME_ID_USR_LIVING);
		
		location = ""; // Initialize location
		for(int i=0;i<2;i++)
			try{
				if(i==1) location+=" | ";
				location+=scraper.elem("location", i).select("._50f5 > a").text();
			}catch(Exception e){}
		
		// Now scrape on the relationships page:
		scraper.scrape(L_LOGIN, user_page_url.replaceAll("=contact-info","=relationship"), null, T_ME_ID_USR_RELATIONSHIP);
		
		relationshipStatus = "";
		try{
			relationshipStatus += scraper.elem("relations", 0).select("._42ef").text();
			relationshipStatus += " ("+scraper.elem("relations", 0).select("._3-91").attr("href")+")";
		}catch(Exception e){}
	
		try{
			Elements relationship_list = scraper.elem("relations", 1).select("._43c8");
			for(int i=0;;i++)
				try{
					Element tmp;
					if((tmp=relationship_list.get(i))!=null){
						relationshipStatus+=" | "+tmp.text();
						relationshipStatus+=" ("+tmp.select("._3-91").attr("href")+")";
					}
				}catch(Exception e){break;}
		}catch(Exception e){}
		
		isPrivate = false;
	
		// Cache this user!
		cache_user(cache_usr_name);
	}
	
	public static ArrayList<User> load_friendlist_from_cache(String cache_usr_name, int start, int end){
		ArrayList<User> friends = new ArrayList<User>();
		
		// Load all friends:
		try{
			for(int i=start;i<end;i++){
				User tmpFriend = new User();
				String fullNameTmp = (String) JCache.get(cacheFriends, cache_usr_name+"_friend_"+i+"_fullName");
				if(fullNameTmp==null) continue;
				
				tmpFriend.setFullName(fullNameTmp);
				tmpFriend.setFirstName((String) JCache.get(cacheFriends, cache_usr_name+"_friend_"+i+"_firstName"));
				tmpFriend.setLastName((String) JCache.get(cacheFriends, cache_usr_name+"_friend_"+i+"_lastName"));
				tmpFriend.setUrl((String) JCache.get(cacheFriends, cache_usr_name+"_friend_"+i+"_url"));
				tmpFriend.setFriendCount((String) JCache.get(cacheFriends, cache_usr_name+"_friend_"+i+"_friendCount"));
				tmpFriend.setPictureURL((String) JCache.get(cacheFriends, cache_usr_name+"_friend_"+i+"_pictureURL"));
				tmpFriend.setId((String) JCache.get(cacheFriends, cache_usr_name+"_friend_"+i+"_id"));
				tmpFriend.setUsername((String) JCache.get(cacheFriends, cache_usr_name+"_friend_"+i+"_username"));
				
				// All set, add this user:
				friends.add(tmpFriend);
			}	
		}catch(Exception e){return null;}
		
		if(friends.size()==0) return null;
		else return friends;
	}
	
	public static void cache_friendlist(String cache_usr_name, ArrayList<User> friends, int start, int end){
		JCache.put(cacheFriends, cache_usr_name, "alive_and_identified");
		
		for(int i=start,fr_ctr = 0;i<end;i++,fr_ctr++){
			JCache.put(cacheFriends, cache_usr_name+"_friend_"+i+"_fullName", friends.get(fr_ctr).getFullName());
			JCache.put(cacheFriends, cache_usr_name+"_friend_"+i+"_firstName", friends.get(fr_ctr).getFirstName());
			JCache.put(cacheFriends, cache_usr_name+"_friend_"+i+"_lastName", friends.get(fr_ctr).getLastName());
			JCache.put(cacheFriends, cache_usr_name+"_friend_"+i+"_url", friends.get(fr_ctr).getUrl());
			JCache.put(cacheFriends, cache_usr_name+"_friend_"+i+"_friendCount", friends.get(fr_ctr).getFriendCount());
			JCache.put(cacheFriends, cache_usr_name+"_friend_"+i+"_pictureURL", friends.get(fr_ctr).getPictureURL());
			JCache.put(cacheFriends, cache_usr_name+"_friend_"+i+"_id", friends.get(fr_ctr).getId());
			JCache.put(cacheFriends, cache_usr_name+"_friend_"+i+"_username", friends.get(fr_ctr).getUsername());
		}
	}
	
	public static ArrayList<User> constructFriends(WebScraper scraper, String user_page_url, String username, int start, int length){
		ArrayList<User> friends = null;
		
		int limit = (length<=0)? MAX_FRIENDS : start+length;
		
		// Check if user lives in cache
		String cache_usr_name = cacheUserPrefix+username;
		boolean construct = true;
				
		if(JCache.isInCache(cacheFriends, cache_usr_name)){
			// He is, load all data necessary for this friend's friendlist:
			friends = load_friendlist_from_cache(cache_usr_name, start, limit);
			if(friends!=null) construct = false;
		}
		
		if(!construct) return friends;
		
		// He's not in the cache, scrape his friend list:
		friends = new ArrayList<User>();
		
		/*// put a get callback here:
		if(start>MAX_FRIENDS)
		{
			global_start = start;
			scraper.setProperty(WebScraper.Props.ENGINE_GET_CALLBACK, new EngineCallback() {
				public void after_get(PhantomJS ctx) {
					ctx.waitForLoad(ctx.getClient());
					int start = (User.global_start - MAX_FRIENDS); // Grab the start variable from the class out of scope
					int base = 2000;
					
					int scrollTo = base + (5000);
					System.out.println("Start: "+start+" Scrollto: "+scrollTo);
					
					for(int i=0;i<start;i++){
						((JavascriptExecutor)ctx.getClient()).executeScript("scrollBy(0, "+scrollTo+")");
						   
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					ctx.waitForLoad(ctx.getClient());
					ctx.take_screenshot("C:\\Users\\Miguel\\Desktop\\screen.jpg", true);
				}
	
				public void before_get(PhantomJS ctx) {}
				
			});
		}*/
		
		scraper.scrape(L_LOGIN, user_page_url, null, T_FRIENDS); // Use the start variable here to affect what it is scraping
		
		int how_manyfriends_found = scraper.count("friends");
		System.out.println("FOUND: "+how_manyfriends_found);
		if(start >= how_manyfriends_found && how_manyfriends_found>0){
			start = how_manyfriends_found-1;
			limit=start+1;
		}
		
		for(int i=start;i<limit;i++){
			try{
				User friend = new User();
				Element friend_element = scraper.elem("friends",i);
				
				// Set friend's name:
				friend.setFullName(friend_element.select(".fsl > a").text());
				String tmp []= friend.getFullName().replaceAll("\\(.+?\\)", "").trim().split(" ");
				friend.setFirstName(tmp[0]);
				friend.setLastName(tmp[tmp.length-1]);
				
				// Friend's url:
				friend.setUrl(friend_element.select(".fsl > a").attr("href"));
				
				// Friend's friend count:
				int tmp_friendcount = Util.extractInt(friend_element.select(".uiProfileBlockContent > div > ._6a").get(1).select("a.uiLinkSubtle").text());
				friend.setFriendCount(tmp_friendcount==-1 ? "unknown": tmp_friendcount+"");
				
				// Friend's image:
				friend.setPictureURL(friend_element.select("._s0").attr("src"));
				
				// Friend's ID:
				Matcher m = Pattern.compile("\\/.+?\\/(.+?)(?:\\?|&|$)").matcher(friend.getUrl().replaceAll("profile.php?id=", ""));
				if(m.find())
					friend.setId(m.group(1));
				
				friend.setUsername(friend.getId());
				
				friends.add(friend);
				
			}catch(Exception e){System.err.println("("+user_page_url+") > Found "+how_manyfriends_found+" friends"); e.printStackTrace(); break;}
		}

		// Add this list of friends to the cache!
		if(friends.size()!=0)
			cache_friendlist(cache_usr_name, friends, start, limit);
		return friends;
	}
  
	public String toString() {
		String out = "[";
	    
		out = out + "firstName: " + firstName + ", ";
		out = out + "lastName: " + lastName + ", ";
		out = out + "fullName: " + fullName + ", ";
		out = out + "id: " + id + ", ";
		out = out + "username: " + username + ", ";
		out = out + "url: " + url + ", ";
		out = out + "birthday: " + birthday + ", ";
		out = out + "gender: " + gender + ", ";
		out = out + "email: " + email + ", ";
		out = out + "pictureURL: " + pictureURL + ", ";
		out = out + "relationship: " + relationshipStatus + ", ";
		out = out + "location: " + location;
		  
		return out + "]";
	}
}
