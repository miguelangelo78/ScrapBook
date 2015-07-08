package com.org.uniscraper.jsengine;

import com.org.uniscraper.misc.Util;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Options;
import org.openqa.selenium.WebDriver.Window;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

public class PhantomJS
{
  private WebDriver client;
  private DesiredCapabilities capabilities;
  private final int CLIENT_WIDTH = 1920;
  private final int CLIENT_HEIGHT = 1080;
  private final String PHANTOM_EXECUTABLE = "lib/phantomjs/phantomjs.exe";
  
  public WebDriver getClient()
  {
    return this.client;
  }
  
  private void setCapabilities()
  {
    this.capabilities = new DesiredCapabilities();
    this.capabilities.setJavascriptEnabled(true);
    this.capabilities.setCapability("phantomjs.binary.path", "lib/phantomjs/phantomjs.exe");
    this.capabilities.setCapability("databaseEnabled", true);
    this.capabilities.setCapability("locationContextEnabled", true);
    this.capabilities.setCapability("applicationCacheEnabled", true);
    this.capabilities.setCapability("browserConnectionEnabled", true);
    this.capabilities.setCapability("webStorageEnabled", true);
    this.capabilities.setCapability("acceptSslCerts", true);
  }
  
  private void setProperties()
  {
    this.client.manage().window().setSize(new Dimension(1920, 1080));
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
    setCapabilities();
    this.client = new PhantomJSDriver(this.capabilities);
    setProperties();
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
  }
  
  public Map<String, String> auth(EngineAuthCallback callback, String url_base, boolean manual)
  {
    Set<Cookie> cookies = null;
    if (manual)
    {
      WebDriver manual_auth_driver = new FirefoxDriver();
      manual_auth_driver.get(url_base);
      
      WebElement email = Util.select(manual_auth_driver, "input[name=email]");
      email.sendKeys("santosmiguel25@gmail.com");
      
      WebElement pass = Util.select(manual_auth_driver, "input[name=pass]");
      pass.sendKeys("nico21");
      pass.sendKeys(Keys.ENTER);
      
      cookies = manual_auth_driver.manage().getCookies();
      manual_auth_driver.close();
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
