# ScrapBook
ScrapBook is a library for scraping Facebook. It is meant to be used as a normal Facebook API without access tokens nor App Secrets. The data it accesses is not limited by any API.

It uses my library 'Universal Web Scraper', including all of the libraries inside it. (https://github.com/miguelangelo78/Universal-Web-Scraper)

Example of how it works:

``` Java
FacebookClient fb = new FacebookClient(); // A new Firefox window will appear for you to login to your Facebook account

User me = fb.getObject("me", User.class); // Fetch my own user data

String myName = me.getName();

System.out.println(me.getBirthday());

System.out.println(me); // Print all data

fb.end(); // Must finish the program
```
