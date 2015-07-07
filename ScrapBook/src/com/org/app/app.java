package com.org.app;

import com.org.scrapbook.client.FacebookClient;
import com.org.scrapbook.object.User;
import com.org.uniscraper.misc.Util;

import java.io.PrintStream;

public class app
{
  public static void main(String[] args)
  {
	FacebookClient fb = new FacebookClient();
    
    User me = fb.getObject("me", User.class);
    
    System.out.println(me);
    
    fb.end();
  }
}
