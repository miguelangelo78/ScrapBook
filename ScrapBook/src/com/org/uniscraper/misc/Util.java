package com.org.uniscraper.misc;

import com.org.uniscraper.jsoniterator.JSONIterator;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.json.simple.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class Util
{
  private static JSONObject innerJSON = new JSONObject();
  public static JSONObject outerJSON;
  public static boolean json_with_slashes = false;
  private static boolean useInnerJSON = false;
  
  public static String[] jsonStringToArray(String jsonString)
  {
    jsonString = jsonString.trim().replaceAll("\\{|\\}", "").replaceAll("(?: )+?,(?: ?)+", ",").replaceAll("(?: ?)+:(?: ?)+", ":");
    String[] res = jsonString.split(":|,");
    for (int i = 0; i < res.length; i++) {
      res[i] = res[i].trim();
    }
    return res;
  }
  
  public static String toJSON(JSONObject inner)
  {
    useInnerJSON = true;
    innerJSON = inner;
    return toJSON();
  }
  
  public static String toJSON()
  {
    String json = "{";
    JSONObject json_to_iterate = useInnerJSON ? innerJSON : outerJSON;
    int last = 0;
    for (Object key : json_to_iterate.keySet())
    {
      Object val = json_to_iterate.get(key);
      
      String val_str = "";
      if ((val instanceof Object[])) {
        val_str = JSONIterator.simpleArray((Object[])val, json_with_slashes);
      } else {
        val_str = toJSON((JSONObject)val);
      }
      json = json + "\"" + key + "\":" + val_str + (last < json_to_iterate.size() - 1 ? ", " : "");
      last++;
    }
    useInnerJSON = false;
    return json + "}";
  }
  
  public String toJSON(boolean addslashes)
  {
    json_with_slashes = true;
    String slashed_json = toJSON();
    json_with_slashes = false;
    return slashed_json;
  }
  
  public static Map<String, String> strArray_to_map(String[] arr)
  {
    Map<String, String> m = new HashMap();
    for (int i = 0; i < arr.length - 1; i += 2) {
      m.put(arr[i], arr[(i + 1)]);
    }
    return m;
  }
  
  public static boolean isNumeric(String str)
  {
    try
    {
      double d = Double.parseDouble(str);
    }
    catch (NumberFormatException nfe)
    {
      return false;
    }
    return true;
  }
  
  public static boolean containsInt(String str){
	  return str.matches(".*\\d+.*");
  }
  
  public static WebElement select(WebDriver client, String cssSelector)
  {
    return client.findElement(By.cssSelector(cssSelector));
  }
  
  public static int extractInt(String str){
	  int n = -1;
	  try{
		  n = Integer.parseInt(str.replaceAll("\\D", ""));
	  }catch(Exception e){}
	  
	  return n;
  }
 
}
