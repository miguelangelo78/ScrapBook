package com.org.scrapbook.object;

import com.org.scrapbook.global.FaceGlobal;
import com.org.uniscraper.misc.Util;
import com.org.uniscraper.scraper.WebScraper;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
 
	public void construct(WebScraper scraper, String user_page_url, String username) {
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
	}
	
	public static ArrayList<User> constructFriends(WebScraper scraper, String user_page_url, String username, int start, int length){
		ArrayList<User> friends = new ArrayList<User>();
		
		// put a get callback here:
		scraper.scrape(L_LOGIN, user_page_url, null, T_FRIENDS); // Use the start variable here to affect what it is scraping
		
		int limit = (length<=0)? MAX_FRIENDS : start+length;
		
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
				
			}catch(Exception e){break;}
		}
		
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
