# ScrapBook
ScrapBook is a library for scraping Facebook. It is meant to be used as a normal Facebook API without access tokens nor App Secrets. The data it accesses is not limited by any API.

It uses my library 'Universal Web Scraper', including all of the libraries inside it. (https://github.com/miguelangelo78/Universal-Web-Scraper)

**- Example: Grabbing a Facebook user**
=======
Example of how it works:

``` Java
FacebookClient fb = new FacebookClient(); // A new Firefox window will appear for you to login to your Facebook account

User me = fb.get("me", User.class); // Fetch my own user data

String myName = me.getName(); // What's my name?

System.out.println(me.getBirthday()); // And my birthday?

System.out.println(me); // Print all data

fb.end(); // Must finish the program
```
You can also specify the username or ID of someone's page:
``` Java
FacebookClient fb = new FacebookClient(); // A new Firefox window will appear for you to login to your Facebook account

User him = fb.get("lucas.example123", User.class); // Fetch lucas' data

String hisName = him.getName(); // What's his name?

System.out.println(him.getBirthday()); // And his birthday?

System.out.println(him); // Print all data

fb.end(); // Must finish the program
```

**- Friends**
=======
In order to get your friend's data you can do this:
``` Java
FacebookClient fb = new FacebookClient(); // Authenticate
    
ArrayList<User> all_friends = fb.get("me/friends", ArrayList.class); // Fetch all my friends (limit is 40 per fetch, parameters can be added)
User first_friend = fb.updateFriend( fb.get("me/friends/0", User.class) ); // Grab my first friend on the list and update his data once fetched
	
System.out.println(first_friend.getFullName()+" lives in: " + first_friend.getLocation()); // Print his info
    
fb.end(); // Finish it
```

Grab any friend from any person:
``` Java
User someoneUnknown = fb.get("lucas.example123/friends/5", User.class); // Grab the 6th friend from lucas

System.out.println("Name: " + someoneUnknown.getFullName());
System.out.println("Lives in: " + someoneUnknown.getLocation());
```
You can also grab a friend of your friend!

``` Java
User first_friend = fb.updateFriend( fb.get("me/friends/0", User.class) ); // My first friend
	
// First friend of my friend:
User friends_friend = fb.updateFriend(fb.get(first_friend.getUsername()+"/friends/0", User.class));

// Print his data:
System.out.println(friends_friend.getFullName()+" lives in: " + friends_friend.getLocation());
    
```

**Note:**
The great advantage in this library is that every action you do on Facebook gets cached, which means next time you fetch lucas' 6th friend (or all his friends, or himself!) the library won't need to go to Facebook again anytime soon (until the cache expires obviously).

**- Feeds**
=======
In order to read your own feed you need to write the following:
``` Java
// Grab most recent feeds:
Elements feeds = fb.get("me/feed", Elements.class);
			
// Fetch 1st entry from your feed:
String feed_html = feeds.get(0).html();
String feed_text = feeds.get(0).text(); 

// Show the data!
System.out.print(feed_html);
System.out.print(feed_text);
			
```
Or grab a friend's feed! (or even a friend's friend feed)
``` Java
// Grab most recent feeds:
Elements feeds = fb.get("lucas.192/feed", Elements.class);
			
// Fetch 3rd entry:
String feed_html = feeds.get(2).html();
String feed_text = feeds.get(2).text(); 

// Show the data!
System.out.print(feed_html);
System.out.print(feed_text);
``` 

**- Home**
=======

The home behaves very similarly to the feeds, except you can only fetch your OWN data from your own Facebook homepage (obviously, this isn't an Hack of any sort!). Here's how it's done:
``` Java
// Fetch most recent entries:
Elements homefeed = fb.get("me/home", Elements.class);

// Simple as this:
for(Element entry: homefeed)
	System.out.println(entry.text());

``` 

**Note:** Since this library doesn't use Facebook's API, it must use a headless browser for scraping the data. It could use JSoup but Facebook is mostly made out of Javascript code and I needed a headless browser to execute it. Because of this, it might take some time to fetch a single user. This means this library would be ideal for a bot to continuously crawl Facebook as time goes on, and not to fetch 100 users extremely fast in less than 30 seconds.

