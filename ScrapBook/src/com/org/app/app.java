package com.org.app;

import org.jsoup.select.Elements;

import com.org.cache.JCache;
import com.org.scrapbook.client.FacebookClient;
import com.org.scrapbook.object.User;

public class app {

	static long time = 0;
	
	public static long cron(){
		long new_time = System.currentTimeMillis();
		long delta = new_time - time;
		time = new_time;
		return delta;
	}
	
	public static void main(String[] args) {
		
		FacebookClient fb = new FacebookClient();
		
		try{
			cron(); 
			/////
			
			Elements feeds = fb.get("me/home", Elements.class);
			
			System.out.println(feeds.size());
			System.out.print(feeds.get(0).text());
			
			/////
			float elapsedTime = cron()*1.0f/1000; 
		    
			System.out.println("! "+String.format("%s", elapsedTime)+ " seconds !");
			
		}catch(Exception e){e.printStackTrace(); JCache.cacheman.shutdown();}
		finally{
			fb.end();
		}
	}
}
