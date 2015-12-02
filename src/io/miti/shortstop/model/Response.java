package io.miti.shortstop.model;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TimeZone;

import io.miti.shortstop.util.ContentTypeCache;
import io.miti.shortstop.util.HeaderField;
import io.miti.shortstop.util.ResponseCodeCache;
import io.miti.shortstop.util.Utility;

public final class Response
{
  /** The status code. */
  private int code = 200;
  
  /** The status message. */
  private String msg = "OK";
  
  /** The header map. */
  private Map<String, String> header = null;
  
  /** The response body. */
  private byte[] body = null;
  
  /**
   * Default constructor.
   */
  public Response() {
    setDefaults();
  }
  
  /**
   * Constructor taking the response code.
   * 
   * @param nCode the response code
   */
  public Response(final int nCode) {
    setDefaults();
    setCode(nCode);
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
   * @return this
   */
  public Response setCode(final int nCode) {
    code = nCode;
    
    // Look up the standard description of the code
    setMessage(ResponseCodeCache.getCache().getResponseMessage(code));
    
    return this;
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
   * @return this
   */
  public Response setMessage(final String sMsg) {
    if ((sMsg == null) || sMsg.trim().isEmpty()) {
      return this;
    }
    
    msg = sMsg.trim();
    
    return this;
  }
  
  /**
   * Set the response body as a string.
   * 
   * @param sBody the body string
   * @return this
   */
  public Response setBody(final String sBody) {
    // Check for null
    if (sBody == null) {
      return setBodyAsBytes(null);
    }
    
    // Cast the string to a byte array
    return setBodyAsBytes(sBody.getBytes(StandardCharsets.UTF_8));
  }
  
  /**
   * Set the response body.
   * 
   * @param sBody the new body
   * @return this
   */
  public Response setBodyAsBytes(final byte[] sBody) {
    
    // Save the new body
    body = (sBody == null) ? null : sBody;
    
    // Update the content length
    final int size = (body == null) ? 0 : body.length;
    addToHeader(HeaderField.RES_CONTENT_LENGTH, size);
    
    return this;
  }
  
  /**
   * Return the body.
   * 
   * @return the body
   */
  public byte[] getBody() {
    // Check for null
    if (body == null) {
      return null;
    }
    
    return java.util.Arrays.copyOf(body, body.length);
  }
  
  /**
   * Add a key/value pair to the header.
   * 
   * @param key the key
   * @param value the value
   * @return this
   */
  public Response addToHeader(final String key, final String value) {
    
    // Skip null/empty keys
    if ((key == null) || key.isEmpty() || (value == null) || value.isEmpty()) {
      return this;
    }
    
    // If the header hasn't been allocated yet, do so now
    if (header == null) {
      header = new HashMap<String, String>(5);
    }
    
    // Store the key/value pair
    header.put(key, value);
    
    return this;
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
   * Set the content type in the response header to JSON.
   * 
   * @return this
   */
  public Response setJsonContentType() {
    setContentTypeByFileExt("json");
    return this;
  }
  
  /**
   * Set the content type in the response header to plain text.
   * 
   * @return this
   */
  public Response setPlainTextContentType() {
    setContentTypeByFileExt("txt");
    return this;
  }
  
  /**
   * Set the content type in the response header to HTML.
   * 
   * @return this
   */
  public Response setHTMLContentType() {
    setContentTypeByFileExt("html");
    return this;
  }
  
  /**
   * Set the content type based on the file extension (e.g., html, txt, xls).
   * 
   * @param ext the file extension
   * @return this
   */
  public Response setContentTypeByFileExt(final String ext) {
    addToHeader(HeaderField.RES_CONTENT_TYPE, ContentTypeCache.getCache().getContentTypeMIMEType(ext));
    return this;
  }
  
  /**
   * Set the default values.
   * 
   * @return this
   */
  public Response setDefaults() {
    setPlainTextContentType();
    addToHeader(HeaderField.RES_SERVER, "Shortstop Web Server 0.1");
    addToHeader(HeaderField.RES_CONTENT_LENGTH, 0);
    addToHeader(HeaderField.RES_CONNECTION, "close");
    addToHeader(HeaderField.RES_CACHE_CONTROL, "no-cache, no-store, max-age=0, must-revalidate");
    addToHeader(HeaderField.RES_PRAGMA, "no-cache");
    addToHeader(HeaderField.RES_EXPIRES, 0);
    addToHeader("X-Content-Type-Options", "nosniff");
    addToHeader("X-XSS-Protection", "1; mode=block");
    // addToHeader(HeaderField.RES_CONTENT_MD5, <compute MD5 after content encoding?>);
    
    // Set date fields
    final SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z");
    sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
    final Date date = new Date();
    String dateStr = sdf.format(date);
    addToHeader(HeaderField.RES_DATE, dateStr);
    addToHeader(HeaderField.RES_LAST_MODIFIED, dateStr);
    
    return this;
  }
  
  
  /**
   * Set the code to 404 for this response.
   * 
   * @return this
   */
  public Response setAs404() {
    setCode(404);
    setBody(null);
    
    return this;
  }
  
  
  /**
   * Return if there is a body.
   * 
   * @return if the response has a body
   */
  public boolean hasBody() {
    return ((body != null) && (body.length > 0));
  }
  
  /**
   * Add the MD5 hash to the header.
   * 
   * @return this
   */
  public Response addMD5() {
    if (body != null) {
      final String md5 = Utility.getMD5(body);
      header.put(HeaderField.RES_CONTENT_MD5, md5);
    }
    
    return this;
  }
}
