package com.org.uniscraper.scraper;

import com.org.uniscraper.file.ScraperFile;
import com.org.uniscraper.jsengine.EngineAuthCallback;
import com.org.uniscraper.jsengine.EngineCallback;
import com.org.uniscraper.jsengine.PhantomJS;
import com.org.uniscraper.jsoniterator.JSONIterator;
import com.org.uniscraper.misc.Util;
import java.io.IOException;
import java.util.Map;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WebScraper
{
  public WebScraper() {}
  
  public static enum Props
  {
    USER_AGENT,  TIMEOUT,  COOKIE,  HEADER,  PARAMS,  REFERRER,  METHOD,  IGNRCONTTYPE,  PROXY,  USING_HEADLESS,  ENGINE_GET_CALLBACK,  ENGINE_AUTH_CALLBACK,  MANUAL_AUTH;
  }
  
  private int timeout = 60000;
  private String USER_AGENT = "Mozilla/5.0";
  private JSONObject data;
  private Document document;
  private boolean is_connected = false;
  private boolean is_method_set = false;
  public static boolean manual_auth = false;
  public static boolean is_using_headless = false;
  public static PhantomJS engine;
  public static EngineCallback engine_get_callback = null;
  public static EngineAuthCallback engine_auth_callback = null;
  private Connection conn;
  private Connection.Response response_conn;
  private Map<String, String> cookies_auth;
  
  public WebScraper(String urlLogin, String urlDest, Connection.Method method, boolean using_headless, String params, String targets)
  {
    is_using_headless = using_headless;
    engine = new PhantomJS();
    auth(urlLogin, urlDest, Util.jsonStringToArray(params));
    scrape(urlDest, urlLogin, method, targets);
  }
  
  public WebScraper(String urlLogin, String urlDest, Connection.Method method, String params, String targets)
  {
    auth(urlLogin, urlDest, Util.jsonStringToArray(params));
    scrape(urlDest, urlLogin, method, targets);
  }
  
  public WebScraper(String urlLogin, String urlDest, Connection.Method method, boolean using_headless, String targets, String params, String bytokens)
  {
    is_using_headless = using_headless;
    engine = new PhantomJS();
    auth(urlLogin, urlDest, Util.jsonStringToArray(params), Util.jsonStringToArray(bytokens));
    scrape(urlDest, urlLogin, method, targets);
  }
  
  public WebScraper(String urlLogin, String urlDest, Connection.Method method, String targets, String params, String bytokens)
  {
    auth(urlLogin, urlDest, Util.jsonStringToArray(params), Util.jsonStringToArray(bytokens));
    scrape(urlDest, urlLogin, method, targets);
  }
  
  public WebScraper(String urlLogin, String urlDest, boolean using_headless, String params, String bytokens)
  {
    is_using_headless = using_headless;
    engine = new PhantomJS();
    auth(urlLogin, urlDest, Util.jsonStringToArray(params), Util.jsonStringToArray(bytokens));
  }
  
  public WebScraper(String urlLogin, String urlDest, Connection.Method method, String params, String bytokens, boolean usebytokens)
  {
    auth(urlLogin, urlDest, Util.jsonStringToArray(params), Util.jsonStringToArray(bytokens));
  }
  
  public WebScraper(String urlLogin, String urlDest, Connection.Method method, boolean using_headless, String params)
  {
    is_using_headless = using_headless;
    engine = new PhantomJS();
    auth(urlLogin, urlDest, Util.jsonStringToArray(params));
  }
  
  public WebScraper(String urlLogin, String urlDest, Connection.Method method, String params)
  {
    auth(urlLogin, urlDest, Util.jsonStringToArray(params));
  }
  
  public WebScraper(String urlLogin, String urlDest, boolean using_headless)
  {
    is_using_headless = using_headless;
    engine = new PhantomJS();
    auth(urlLogin, urlDest, null);
  }
  
  public WebScraper(String url)
  {
    this.conn = Jsoup.connect(url);
    this.is_connected = true;
  }
  
  public WebScraper(String url, boolean using_headless)
  {
    engine = new PhantomJS();
    is_using_headless = using_headless;
    this.conn = Jsoup.connect(url);
    this.is_connected = true;
  }
  
  public WebScraper(String url, Connection.Method method, boolean using_headless, String targets)
  {
    scrape(url, url, method, targets);
    is_using_headless = using_headless;
  }
  
  public WebScraper(String url, Connection.Method method, String targets)
  {
    scrape(url, url, method, targets);
  }
  
  public WebScraper(String url, Connection.Method method, String targets, boolean run_on_construct)
  {
    if (!run_on_construct)
    {
      this.conn = Jsoup.connect(url);
      this.is_connected = true;
    }
    else
    {
      scrape(url, url, method, targets);
    }
  }
  
  public Document getDocument()
  {
    return this.document;
  }
  
  public Connection getConn()
  {
    return this.conn;
  }
  
  public void setConn(Connection conn)
  {
    this.conn = conn;
  }
  
  public JSONObject getData()
  {
    return this.data;
  }
  
  public void setData(JSONObject data)
  {
    this.data = data;
  }
  
  public static void setEngineAuthentication(EngineAuthCallback callback)
  {
    engine_auth_callback = callback;
  }
  
  public void setProperty(Props property, Object value)
  {
    switch (property)
    {
    case ENGINE_AUTH_CALLBACK: 
      this.timeout = ((Integer)value).intValue(); break;
    case ENGINE_GET_CALLBACK: 
      if ((value instanceof String[])) {
        this.conn.cookie(((String[])value)[0], ((String[])value)[1]);
      } else {
        this.conn.cookies((Map)value);
      }
      break;
    case HEADER: 
      this.conn.header(((String[])value)[0], ((String[])value)[1]); break;
    case IGNRCONTTYPE: 
      this.conn.data((Map)value); break;
    case COOKIE: 
      this.USER_AGENT = ((String)value); break;
    case MANUAL_AUTH: 
      this.conn.referrer((String)value); break;
    case METHOD: 
      this.is_method_set = true;this.conn.method((Connection.Method)value); break;
    case PARAMS: 
      this.conn.ignoreContentType(((Boolean)value).booleanValue()); break;
    case REFERRER: 
      is_using_headless = ((Boolean)value).booleanValue(); break;
    case TIMEOUT: 
      engine_get_callback = (EngineCallback)value; break;
    case USER_AGENT: 
      engine_auth_callback = (EngineAuthCallback)value; break;
    case USING_HEADLESS: 
      manual_auth = ((Boolean)value).booleanValue(); break;
    case PROXY: 
      String[] keyvals = Util.jsonStringToArray((String)value);
      System.setProperty("http.proxyHost", keyvals[0]);
      System.setProperty("http.proxyPort", keyvals[1]);
      break;
    }
  }
  
  public Object getProperty(Props property, String value)
  {
    switch (property)
    {
    case ENGINE_AUTH_CALLBACK: 
      return Integer.valueOf(this.timeout);
    case ENGINE_GET_CALLBACK: 
      return this.conn.response().cookies();
    case HEADER: 
      return this.conn.response().header(value);
    case COOKIE: 
      return this.USER_AGENT;
    case METHOD: 
      return this.conn.response().method();
    case REFERRER: 
      return Boolean.valueOf(is_using_headless);
    }
    return null;
  }
  
  public Connection.Response getResponse()
  {
    return this.response_conn;
  }
  
  public String get(String target, int index)
  {
    return ((Element)((Object[])this.data.get(target))[index]).toString();
  }
  
  public Element elem(String target, int index)
  {
    return (Element)((Object[])this.data.get(target))[index];
  }
  
  public Elements elems(String target)
  {
    Elements el = new Elements();
    
    Object[] obj_el = (Object[])this.data.get(target);
    Object[] arrayOfObject1;
    int j = (arrayOfObject1 = obj_el).length;
    for (int i = 0; i < j; i++)
    {
      Object obj = arrayOfObject1[i];
      el.add((Element)obj);
    }
    return el;
  }
  
  public Elements[] allElems()
  {
    Elements[] all_elements = new Elements[this.data.size()];
    
    int i = 0;
    for (Object elems_targets : this.data.keySet()) {
      all_elements[(i++)] = elems((String)elems_targets);
    }
    return all_elements;
  }
  
  void updateCookies(Map<String, String> new_cookies)
  {
    this.cookies_auth = new_cookies;
  }
  
  public void print()
  {
    JSONIterator.print(this.data);
  }
  
  public String toJSON()
  {
    Util.outerJSON = this.data;
    return Util.toJSON();
  }
  
  public String toJSON(boolean addslashes)
  {
    Util.json_with_slashes = true;
    String slashed_json = toJSON();
    Util.json_with_slashes = false;
    return slashed_json;
  }
  
  public void connect(String url)
  {
    if (!this.is_connected)
    {
      this.conn = Jsoup.connect(url);
      this.is_connected = (!this.is_connected);
    }
  }
  
  public Connection execute()
  {
    try
    {
      this.response_conn = this.conn.execute();
      this.is_connected = false;
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    return this.conn;
  }
  
  public void auth(String urlLogin, String urlHome, String[] params, String[] by_tokens)
  {
    if ((engine_auth_callback == null) && (!manual_auth))
    {
      boolean bypass_token = by_tokens != null;
      
      Map<String, String> params_map = Util.strArray_to_map(params);
      if (bypass_token)
      {
        connect(urlLogin);this.is_connected = false;
        
        setProperty(Props.IGNRCONTTYPE, Boolean.valueOf(true));
        setProperty(Props.METHOD, Connection.Method.GET);
        execute();
        try
        {
          Document curld = this.response_conn.parse();
          String[] arrayOfString;
          int j = (arrayOfString = by_tokens).length;
          for (int i = 0; i < j; i++)
          {
            String field = arrayOfString[i];
            params_map.put(field, curld.select("input[name=" + field + "]").val());
          }
        }
        catch (IOException e)
        {
          e.printStackTrace();
        }
      }
      connect(urlLogin);this.is_connected = false;
      if (bypass_token) {
        setProperty(Props.COOKIE, this.response_conn.cookies());
      }
      setProperty(Props.PARAMS, params_map);
      setProperty(Props.METHOD, Connection.Method.POST);
      setProperty(Props.IGNRCONTTYPE, Boolean.valueOf(true));
      
      execute();
      
      connect(urlHome);
      updateCookies(this.response_conn.cookies());
    }
    else
    {
      updateCookies(engine.auth(engine_auth_callback, urlLogin, manual_auth));
    }
  }
  
  public void auth(String urlLogin, String urlHome, String[] params)
  {
    auth(urlLogin, urlHome, params, null);
  }
  
  public WebScraper scrape(String urlAuth, String url, Connection.Method method, String targets)
  {
    Document final_doc = null;
    
    JSONObject targetsObj = null;
    
    boolean targetless = targets == null;
    if (!targetless) {
      try
      {
        targetsObj = (JSONObject)new JSONParser().parse(targets.replaceAll("'", "\""));
      }
      catch (ParseException e1)
      {
        e1.printStackTrace();
      }
    }
    if (is_using_headless)
    {
      engine.run(url, urlAuth, this.cookies_auth, engine_get_callback);
      if (!targetless) {
        final_doc = engine.getDocument();
      }
    }
    else if (!targetless)
    {
      Connection conn = null;
      if (!this.is_connected) {
        conn = Jsoup.connect(url);
      } else {
        conn = this.conn;
      }
      this.is_connected = false;
      try
      {
        conn.userAgent(this.USER_AGENT).timeout(this.timeout);
        if (!this.is_method_set) {
          conn.method(method);
        }
        this.is_method_set = false;
        
        updateCookies(this.cookies_auth);
        execute();
        
        final_doc = this.response_conn.parse();
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
    }
    if (!targetless)
    {
      JSONIterator.update(targetsObj, final_doc);
      this.document = final_doc;
      this.data = targetsObj;
    }
    return this;
  }
  
  public WebScraper scrape(Connection.Method method, String targets)
  {
    return scrape("", "", method, targets);
  }
  
  public WebScraper scrape(String targets)
  {
    return scrape("", "", null, targets);
  }
  
  public void end()
  {
    engine.quit();
  }
  
  public void export(String filepath, boolean to_each_file, boolean end)
  {
    if (!to_each_file) {
      ScraperFile.write(filepath, toString());
    }
    if (end) {
      end();
    }
  }
  
  public String toString()
  {
    return toJSON(true);
  }
}
