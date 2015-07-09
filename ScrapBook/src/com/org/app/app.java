package com.org.app;

import com.org.cache.JCache;
import com.org.scrapbook.client.FacebookClient;
import com.org.scrapbook.object.User;

public class app {
	
	public static void main(String[] args) {
		FacebookClient fb = new FacebookClient();
		
		try{
			int path [] = new int[]{5,0,0,0};
			
			long startTime = System.currentTimeMillis();
			
			User friends_friend = fb.get("me/friends/"+path[0], User.class);
			
			for(int i=1;i<path.length;i++){
				friends_friend = fb.get(friends_friend.getUsername()+"/friends/"+Integer.toString(path[i]), User.class);
				System.out.println("Result: "+friends_friend.getUsername());
			}
				
			friends_friend = fb.updateFriend(friends_friend);
			
			float elapsedTime = (System.currentTimeMillis() - startTime)*1.0f/1000;
		    
			System.out.println("! "+String.format("%s", elapsedTime)+ " seconds , "+String.format("%s", elapsedTime/path.length*1.0f)+"s per friend!");
			System.out.println(friends_friend);
		
		}catch(Exception e){e.printStackTrace(); JCache.cacheman.shutdown();}
		finally{
			fb.end();
		}
	}
}
