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

public class User
  implements FaceGlobal
{
  private String firstName;
  private String lastName;
  private String fullName;
  private String id;
  private String url;
  private String bio;
  private String quotes;
  private String about;
  private String relationshipStatus;
  private String religion;
  private String website;
  private String birthday;
  private String email;
  private String timezone;
  private String gender;
  private String political;
  private String locale;
  private String username;
  private String pictureURL;
  private String hometown;
  private String employer;
  private String position;
  private String jobStartDate;
  private String jobEndDate;
  private String description;
  private String location;
  private String SO;
  private String rawUpdatedTime;
  private List<String> interestedIn = new ArrayList();
  private List<String> meetingFor = new ArrayList();
  private List<String> work = new ArrayList();
  private List<String> sports = new ArrayList();
  private List<String> favoriteTeams = new ArrayList();
  private List<String> favoriteAthletes = new ArrayList();
  private List<String> languages = new ArrayList();
  private boolean verified;
  
  public void construct(WebScraper scraper, String who)
  {
    scraper.scrape("https://www.facebook.com/login.php?login_attempt=1", who, null, "{'html':'html','fullName':'#fb-timeline-cover-name','id':'#pagelet_timeline_main_column','personal':'#pagelet_basic > div > .uiList > ._3pw9'}");
    scraper.export("C:\\Users\\Miguel\\Desktop\\out.json", false, false);
    
    this.fullName = scraper.elem("fullName", 0).text();
    String[] fullNameSplit = this.fullName.split(" ");
    this.firstName = fullNameSplit[0];
    this.lastName = fullNameSplit[(fullNameSplit.length - 1)];
    
    Matcher match = Pattern.compile("\"profile_owner\":\"(.+?)\",").matcher(scraper.elem("id", 0).attr("data-gt"));
    if (match.find()) {
      this.id = match.group(1);
    }
    this.url = who.replaceAll("about\\?.+$", "");
    
    this.birthday = ((Element)scraper.elem("personal", 0).select("._50f4").get(1)).text();
    try
    {
      this.gender = ((Element)scraper.elem("personal", 1).select("._50f4").get(1)).text();
      if (Util.isNumeric(this.gender)) {
        this.gender = null;
      }
    }
    catch (Exception localException) {}
  }
  
  public String toString()
  {
    String out = "[";
    
    out = out + "firstName: " + this.firstName + ", ";
    out = out + "lastName: " + this.lastName + ", ";
    out = out + "fullName: " + this.fullName + ", ";
    out = out + "id: " + this.id + ", ";
    out = out + "url: " + this.url + ", ";
    out = out + "birthday: " + this.birthday + ", ";
    out = out + "gender: " + this.gender + ", ";
    
    return out + "]";
  }
}
