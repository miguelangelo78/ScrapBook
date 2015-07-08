# ScrapBook
ScrapBook is a library for scraping Facebook. It is meant to be used as a normal Facebook API without access tokens nor App Secrets. The data it accesses is not limited by any API.

It uses my library 'Universal Web Scraper', including all of the libraries inside it. (https://github.com/miguelangelo78/Universal-Web-Scraper)

Example of how it works:

``` Java
FacebookClient fb = new FacebookClient(); // A new Firefox window will appear for you to login to your Facebook account

User me = fb.get("me", User.class); // Fetch my own user data

String myName = me.getName();

System.out.println(me.getBirthday());

System.out.println(me); // Print all data

fb.end(); // Must finish the program
```

In order to get your friend's data you can do this:
``` Java
FacebookClient fb = new FacebookClient(); // Authenticate
    
ArrayList<User> all_friends = fb.get("me/friends", ArrayList.class); // Fetch all my friends (limit is 40 per fetch, parameters can be added)
User first_friend = fb.updateFriend( fb.get("me/friends/0", User.class) ); // Grab my first friend on the list and update his data once fetched
	
System.out.println(first_friend.getFullName()+" lives in: " + first_friend.getLocation()); // Print his info
    
fb.end(); // Finish it
```

You can also grab a friend of your friend!

``` Java
User first_friend = fb.updateFriend( fb.get("me/friends/0", User.class) ); // My first friend
	
// First friend of my friend:
User friends_friend = fb.updateFriend(fb.get(first_friend.getUsername()+"/friends/0", User.class));

// Print his data:
System.out.println(friends_friend.getFullName()+" lives in: " + friends_friend.getLocation());
    
```

NOTE: Since this library doesn't use Facebook's API, it must use a headless browser for scraping the data. It could use JSoup but Facebook is mostly made out of Javascript code and I needed a headless browser to execute it. Because of this, it might take some time to fetch a single user. This means this library would be ideal for a bot to continuously crawl Facebook as time goes on, and not to fetch 100 users extremely fast in less than 30 seconds.
