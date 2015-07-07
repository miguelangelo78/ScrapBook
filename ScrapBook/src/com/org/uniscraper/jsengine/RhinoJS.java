package com.org.uniscraper.jsengine;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.CookieManager;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.StringWebResponse;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebClientOptions;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.HTMLParser;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class RhinoJS
{
  private WebClient client;
  private HtmlPage page;
  
  public HtmlPage getPage()
  {
    return this.page;
  }
  
  public void setPage(HtmlPage page)
  {
    this.page = page;
  }
  
  public WebClient getClient()
  {
    return this.client;
  }
  
  public Document getDocument()
  {
    return Jsoup.parse(this.page.asXml());
  }
  
  private HtmlPage toHtml(String html, WebWindow window, String base_url)
  {
    HtmlPage page = null;
    try
    {
      page = HTMLParser.parseHtml(new StringWebResponse(html, new URL(base_url)), window);
    }
    catch (IOException e1)
    {
      e1.printStackTrace();
    }
    return page;
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
      this.client.getCookieManager().addCookie(new Cookie(domain, (String)entry.getKey(), (String)entry.getValue()));
    }
  }
  
  public RhinoJS(String raw_html, String base_url, Map<String, String> cookies)
  {
    warnings(true);
    
    this.client = new WebClient(BrowserVersion.FIREFOX_38);
    this.client.getOptions().setJavaScriptEnabled(true);
    this.client.getOptions().setActiveXNative(true);
    this.client.getOptions().setAppletEnabled(true);
    this.client.getOptions().setCssEnabled(true);
    this.client.getOptions().setDoNotTrackEnabled(true);
    this.client.getOptions().setPopupBlockerEnabled(true);
    this.client.getOptions().setRedirectEnabled(true);
    this.client.getOptions().setUseInsecureSSL(true);
    this.client.getOptions().setThrowExceptionOnFailingStatusCode(true);
    this.client.getOptions().setThrowExceptionOnScriptError(true);
    this.client.setAjaxController(new NicelyResynchronizingAjaxController());
    
    this.client.getCookieManager().setCookiesEnabled(true);
    setCookies(getDomainFromUrl(base_url), cookies);
    
    this.client.waitForBackgroundJavaScript(60000L);
    
    this.page = toHtml(raw_html, this.client.getCurrentWindow(), base_url);
  }
  
  private void warnings(boolean enable)
  {
    if (!enable) {
      Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF);
    } else {
      Logger.getLogger("com.gargoylesoftware").setLevel(Level.ALL);
    }
  }
}
