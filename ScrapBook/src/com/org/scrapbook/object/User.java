package com.org.scrapbook.object;

import com.org.scrapbook.global.FaceGlobal;
import com.org.uniscraper.misc.Util;
import com.org.uniscraper.scraper.WebScraper;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class User implements FaceGlobal {
	private boolean isPrivate = true;
	private String firstName; // DONE
	private String lastName; // DONE
	private String fullName; // DONE
	private String id; // DONE
	private String username; // DONE
	private String url; // DONE
	private String birthday; // DONE
	private String email; // DONE
	private String gender; // DONE
	private String pictureURL; // DONE
	
	private String location; // another page
	private String relationshipStatus; // pending (another page)
	
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
	private String about;	// NO
	private String rawUpdatedTime;
	private List<String> interestedIn = new ArrayList();
	private List<String> meetingFor = new ArrayList();
	private List<String> work = new ArrayList();
	private List<String> sports = new ArrayList();
	private List<String> favoriteTeams = new ArrayList();
	private List<String> favoriteAthletes = new ArrayList();
	private List<String> languages = new ArrayList();
	private boolean verified;
  
	public void construct(WebScraper scraper, String user_page_url, String username) {
		scraper.scrape("https://www.facebook.com/login.php?login_attempt=1", user_page_url, null, T_ME_ID_USR);
		scraper.export("C:\\Users\\Miguel\\Desktop\\out.json", false, false);
		
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
		birthday = ((Element)scraper.elem("personal", 0).select("._50f4").get(1)).text();
		try {
			gender = scraper.elem("personal", 1).select("._50f4").get(1).text();
		
			if (Util.containsInt(gender))
				gender = null;
		} catch (Exception e) {
			// Fix bug (gender being on birthday's field)
			gender = scraper.elem("personal", 0).select("._50f4").get(1).text();
			if(gender!=null)
				if(Util.containsInt(gender)) gender = null;
				else birthday = null;
		}
		
		// Email:
		email = id+"@facebook.com";
		
		// Picture URL:
		pictureURL = scraper.elem("profile_pic", 0).attr("src");
		
		
		
		isPrivate  = false;
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
	    
	  return out + "]";
  }
}
