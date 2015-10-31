package io.miti.shortstop.server;

import java.util.HashMap;
import java.util.Map;

public final class Response
{
  /** The status code. */
  private int code = 200;
  
  /** The status message. */
  private String msg = "OK";
  
  /** The header map. */
  private Map<String, String> header = null;
  
  /** The response body. */
  private String body = null;
  
  /**
   * Default constructor.
   */
  public Response() {
    super();
  }
  
  /**
   * Get the status code.
   * 
   * @return the status code
   */
  public int getCode() {
    return code;
  }
  
  /**
   * Set the status code.
   * 
   * @param nCode the status code
   */
  public void setCode(final int nCode) {
    code = nCode;
  }
  
  /**
   * Get the message text.
   * 
   * @return the message text
   */
  public String getMessage() {
    return msg;
  }
  
  /**
   * Set the message text.
   * 
   * @param sMsg the message text
   */
  public void setMessage(final String sMsg) {
    msg = sMsg;
  }
  
  /**
   * Set the response body.
   * 
   * @param sBody the new body
   */
  public void setBody(final String sBody) {
    body = sBody;
  }
  
  /**
   * Return the body.
   * 
   * @return the body
   */
  public String getBody() {
    return body;
  }
  
  /**
   * Add a key/value pair to the header.
   * 
   * @param key the key
   * @param value the value
   */
  public void addToHeader(final String key, final String value) {
    
    // Skip null/empty keys
    if ((key == null) || key.isEmpty()) {
      return;
    }
    
    // If the header hasn't been allocated yet, do so now
    if (header == null) {
      header = new HashMap<String, String>(5);
    }
    
    // Store the key/value pair
    header.put(key, value);
  }
  
  /**
   * Set the default values.
   */
  public void setDefaults() {
    // TODO Auto-generated method stub
  }
}
