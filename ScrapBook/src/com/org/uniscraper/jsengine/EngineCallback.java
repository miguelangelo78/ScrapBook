package com.org.uniscraper.jsengine;

public abstract interface EngineCallback
{
  public abstract void after_get(PhantomJS paramPhantomJS);
  
  public abstract void before_get(PhantomJS paramPhantomJS);
}
