package com.org.app;

import java.util.ArrayList;

import com.org.scrapbook.client.FacebookClient;
import com.org.scrapbook.object.User;

public class app {
  public static void main(String[] args) {
	FacebookClient fb = new FacebookClient();
    
	ArrayList<User> all_friends = fb.getObject("me/friends", ArrayList.class);
	User first_friend = fb.updateFriend( fb.getObject("me/friends/0", User.class) );
	
    System.out.println(first_friend.getFullName()+" lives in: " + first_friend.getLocation());
    
    fb.end();
  }
}
