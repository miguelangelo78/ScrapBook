package com.org.app;

import com.org.scrapbook.client.FacebookClient;
import com.org.scrapbook.object.User;

public class app {
	
	public static void main(String[] args) {
		FacebookClient fb = new FacebookClient();
		
		int path [] = new int[]{2,11,10,1};
		
		long startTime = System.currentTimeMillis();
		
		User friends_friend = null;
		User first_friend = fb.get("me/friends/"+path[0], User.class);
		
		for(int i=1;i<path.length;i++)
			friends_friend = fb.get(first_friend.getUsername()+"/friends/"+Integer.toString(path[i]), User.class);
		
		friends_friend = fb.updateFriend(friends_friend);
		
		float elapsedTime = (System.currentTimeMillis() - startTime)*1.0f/1000;
	    
		System.out.println("! "+String.format("%s", elapsedTime)+ " seconds , "+String.format("%s", elapsedTime/path.length*1.0f)+"s per friend!");
		
	    System.out.println(friends_friend);
	    
	    fb.end();
	}
}
