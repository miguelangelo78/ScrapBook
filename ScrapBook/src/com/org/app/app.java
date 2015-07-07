package com.org.app;

import com.org.scrapbook.client.FacebookClient;
import com.org.scrapbook.object.User;
import java.io.PrintStream;

public class app
{
  public static void main(String[] args)
  {
    FacebookClient fb = new FacebookClient();
    
    User me = (User)fb.getObject("migmacs", User.class, new Object[0]);
    
    System.out.println(me);
    
    fb.end();
  }
}
