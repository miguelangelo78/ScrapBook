package com.org.app;

import com.org.scrapbook.client.FacebookClient;
import com.org.scrapbook.object.User;

public class app
{
  public static void main(String[] args)
  {
	FacebookClient fb = new FacebookClient();
    
    User me = fb.getObject("me", User.class);
    
    System.out.println(me.getBirthday());
    
    fb.end();
  }
}
