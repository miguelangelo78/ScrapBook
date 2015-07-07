package com.org.scrapbook.client;

import com.org.scrapbook.global.FaceGlobal;
import com.org.scrapbook.object.User;
import com.org.uniscraper.scraper.WebScraper;
import java.io.PrintStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class FacebookClient
  implements FaceGlobal
{
  private WebScraper facecraper;
  private String myID;
  
  public FacebookClient()
  {
    WebScraper.manual_auth = true;
    this.facecraper = new WebScraper("https://www.facebook.com/login.php?login_attempt=1", null, true);
    
    getMyID();
  }
  
  public <T> T getObject(String objectName, Class<T> classtype, Object... params)
  {
    String[] content_split = objectName.split("/");
    
    String who_what = content_split[0];
    if (content_split.length == 1)
    {
      String link_user = "https://www.facebook.com/";
      if (who_what.toLowerCase().equals("me")) {
        link_user = link_user + "profile.php?id=" + this.myID;
      } else {
        link_user = link_user + who_what;
      }
      User new_user = new User();
      System.out.println(link_user + "/about");
      new_user.construct(this.facecraper, link_user + "/about?section=contact-info&pnref=about");
      return (T) new_user;
    }
    return null;
  }
  
  public void publish(String content, Object parameters) {}
  
  public void unpublish(String obj_id) {}
  
  public void end()
  {
    this.facecraper.end();
  }
  
  private void getMyID()
  {
    this.facecraper.scrape("https://www.facebook.com/login.php?login_attempt=1", "https://www.facebook.com/", null, "{'my_id': '.fbxWelcomeBoxBlock' }");
    
    Matcher match = Pattern.compile("\"bmid\":(.+?),").matcher(this.facecraper.getDocument().select(".fbxWelcomeBoxBlock").attr("data-gt"));
    if (match.find()) {
      this.myID = match.group(1);
    }
  }
}
