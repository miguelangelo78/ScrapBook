package com.org.uniscraper.jsengine;

import com.org.cache.JCache;
import com.org.uniscraper.misc.Util;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Set;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PhantomJS
{
  private WebDriver client;
  private DesiredCapabilities capabilities;
  private final int CLIENT_WIDTH = 1920;
  private final int CLIENT_HEIGHT = 1080;
  private final String PHANTOM_EXECUTABLE = "lib/phantomjs/phantomjs.exe";

  boolean cookies_loaded_from_cache = false;
  
  public WebDriver getClient()
  {
    return this.client;
  }
  
  private void setCapabilities()
  {
    this.capabilities = new DesiredCapabilities();
    this.capabilities.setJavascriptEnabled(true);
    this.capabilities.setCapability("phantomjs.binary.path", PHANTOM_EXECUTABLE);
    this.capabilities.setCapability("databaseEnabled", true);
    this.capabilities.setCapability("locationContextEnabled", true);
    this.capabilities.setCapability("applicationCacheEnabled", true);
    this.capabilities.setCapability("browserConnectionEnabled", true);
    this.capabilities.setCapability("webStorageEnabled", true);
    this.capabilities.setCapability("acceptSslCerts", true);
  }
  
  private void setProperties()
  {
    this.client.manage().window().setSize(new Dimension(CLIENT_WIDTH, CLIENT_HEIGHT));
  }
  
  public Document getDocument()
  {
    return Jsoup.parse(this.client.getPageSource());
  }
  
  private String getDomainFromUrl(String url)
  {
    URI uri = null;
    try
    {
      uri = new URI(url);
    }
    catch (URISyntaxException e)
    {
      e.printStackTrace();
    }
    String domain = uri.getHost();
    return domain.startsWith("www.") ? domain.substring(4) : domain;
  }
  
  private void setCookies(String domain, Map<String, String> cookies)
  {
    for (Map.Entry<String, String> entry : cookies.entrySet()) {
      this.client.manage().addCookie(new Cookie((String)entry.getKey(), (String)entry.getValue(), domain, "/", null));
    }
  }
  
  public PhantomJS()
  {
	plz_killme();
    setCapabilities();
    this.client = new PhantomJSDriver(this.capabilities);
    setProperties();
  }
  
  void waitForLoad(WebDriver driver) {
	    ExpectedCondition<Boolean> pageLoadCondition = new
	        ExpectedCondition<Boolean>() {
	            public Boolean apply(WebDriver driver) {
	                return ((JavascriptExecutor)driver).executeScript("return document.readyState").equals("complete");
	            }
	        };
	    WebDriverWait wait = new WebDriverWait(driver, 30);
	    wait.until(pageLoadCondition);
	}
  
  public void run(String base_url, String domain_url, Map<String, String> cookies, EngineCallback callback)
  {
    if (cookies != null) {
      setCookies(getDomainFromUrl(domain_url), cookies);
    }
    if (callback != null) {
      callback.before_get(this);
    }
    
    this.client.get(base_url);
    
    waitForLoad(client);
    
    if (callback != null) {
      callback.after_get(this);
    }
  }
  
  public byte[] take_screenshot(String screensht_filepath, boolean as_file)
  {
    if (!as_file) {
      return (byte[])((TakesScreenshot)this.client).getScreenshotAs(OutputType.BYTES);
    }
    File scrFile = (File)((TakesScreenshot)this.client).getScreenshotAs(OutputType.FILE);
    try
    {
      FileUtils.copyFile(scrFile, new File(screensht_filepath));
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    return null;
  }
  
  public void quit()
  {
    this.client.close();
    this.client.quit();
    plz_killme();
    JCache.cacheman.shutdown();
  }
  
  private void plz_killme(){
	// Force close:
	String procName = "phantomjs.exe";
	try {
		if(Util.isProcessRunning(procName)) Util.killProcess(procName);
	} catch (Exception e) {
		e.printStackTrace();
	}
  }
  
  private void show_login_message(){
	  JOptionPane msg = new JOptionPane();
	  msg.setMessage("Press the OK button after you've logged in on Facebook");
	  JDialog diag = msg.createDialog("Facebook Login");
	  diag.setAlwaysOnTop(true);
	  diag.setVisible(true);
  }
  
  public Map<String, String> auth(EngineAuthCallback callback, String url_base, boolean manual)
  {
    Set<Cookie> cookies = null;
    if (manual)
    {
    	// load cookies from cache here
    	JCache.initCache();
    	try{
    		if(JCache.isInCache(JCache.cache_auth_name, JCache.cache_cookiename)){
	    		cookies = JCache.load_cookies_from_cache();
	    		cookies_loaded_from_cache = true;
	    	}
    	}catch(Exception e){}
    	
    	if(!cookies_loaded_from_cache){
    		WebDriver manual_auth_driver = new FirefoxDriver();
    		
    		manual_auth_driver.get(url_base);
	      
    		show_login_message();
    		
	    	cookies = manual_auth_driver.manage().getCookies();
	    	
	    	// Save cookies on cache here
	    	JCache.save_cookies_to_cache(cookies);
	    	manual_auth_driver.close();
    	}
    }
    else
    {
      this.client.get(url_base);
      callback.on_auth(this.client);
      cookies = this.client.manage().getCookies();
    }
    return EngineAuthCallback.toCookies(cookies);
  }
 
}
