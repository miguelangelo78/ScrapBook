package com.org.app;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.org.cache.JCache;
import com.org.scrapbook.client.FacebookClient;

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
			
			// Fetch most recent entries:
			Elements homefeed = fb.get("me/home", Elements.class);

			// Simple as this:
			for(Element entry: homefeed)
				System.out.println(entry.text());
			
			/////
			float elapsedTime = cron()*1.0f/1000; 
		    
			System.out.println("! "+String.format("%s", elapsedTime)+ " seconds !");
			
		}catch(Exception e){e.printStackTrace(); JCache.cacheman.shutdown();}
		finally{
			fb.end();
		}
	}
}
