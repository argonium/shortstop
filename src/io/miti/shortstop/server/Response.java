package io.miti.shortstop.server;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TimeZone;

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
    setDefaults();
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
   * Get the status message text.
   * 
   * @return the message text
   */
  public String getMessage() {
    return msg;
  }
  
  /**
   * Set the status message text.
   * 
   * @param sMsg the message text
   */
  public void setMessage(final String sMsg) {
    if ((sMsg == null) || sMsg.trim().isEmpty()) {
      return;
    }
    
    msg = sMsg.trim();
  }
  
  /**
   * Set the response body.
   * 
   * @param sBody the new body
   */
  public void setBody(final String sBody) {
    
    // Save the new body
    body = (sBody == null) ? null : sBody;
    
    // Update the content length
    final int size = (body == null) ? 0 : body.length();
    addToHeader("Content-Length", size);
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
    if ((key == null) || key.isEmpty() || (value == null) || value.isEmpty()) {
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
   * Helper method for adding a key/value pair for an int value.
   * 
   * @param key the key
   * @param value the value as an integer
   */
  public void addToHeader(final String key, final int value) {
    addToHeader(key, Integer.toString(value));
  }
  
  /**
   * Get the keyset of header pairs.
   * 
   * @return the header entries
   */
  public Set<Entry<String, String>> getHeaderIterator() {
    if (header == null) {
      return null;
    }
    
    return header.entrySet();
  }
  
  /**
   * Set the default values.
   */
  public void setDefaults() {
    // TODO Use enums for key names and some values (JSON)
    addToHeader("Content-Type", "text/html");
    addToHeader("Server", "Shortstop Web Server 0.1");
    addToHeader("Content-Length", 0);
    addToHeader("Cache-Control", "no-cache, no-store, max-age=0, must-revalidate");
    addToHeader("Pragma", "no-cache");
    addToHeader("Expires", 0);
    addToHeader("X-Content-Type-Options", "nosniff");
    addToHeader("X-XSS-Protection", "1; mode=block");
    // addToHeader("Content-MD5", <compute MD5 after content encoding?>);
    
    // Set date fields
    final SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z");
    sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
    final Date date = new Date();
    String dateStr = sdf.format(date);
    addToHeader("Date", dateStr);
    addToHeader("Last-Modified", dateStr);
  }
  
  
  /**
   * Set the 404 for this response.
   */
  public void setAs404() {
    code = 404;
    msg = "File not found";
    setBody(null);
  }
  
  
  /**
   * Return if there is a body.
   * 
   * @return if the response has a body
   */
  public boolean hasBody() {
    return ((body != null) && !body.isEmpty());
  }
}
