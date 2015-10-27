package io.miti.shortstop.server;

import java.util.HashMap;
import java.util.Map;

public final class Request
{
  public HttpOperation operation = null;
  public String endpoint = null;
  public String protocol = null;
  private Map<String, String> parameters = null;
  private char[] buffer = null;
  private String url = null;
  
  /**
   * Default constructor.
   */
  public Request() {
    super();
  }
  
  /**
   * Parse a request line.
   * 
   * @param line the request line from the client
   */
  public Request(final String line) {
    final int len = line.length();
    final int index0 = line.indexOf(' ');
    if ((index0 < 0) || (index0 == (len - 1))) {
      return;
    }
    final int index1 = line.indexOf(' ', index0 + 1);
    if ((index1 < 0) || (index1 == (len - 1))) {
      return;
    }
    
    final String verb = line.substring(0, index0).trim();
    operation = getOperation(verb);
    endpoint = line.substring(index0 + 1, index1).trim();
    protocol = line.substring(index1 + 1).trim();
  }
  
  /**
   * Add a parameter key-value pair.
   * 
   * @param key the key
   * @param value the value
   */
  public void addParameter(final String key, final String value) {
    if ((key == null) || key.isEmpty()) {
      return;
    }
    
    if (parameters == null) {
      parameters = new HashMap<String, String>(5);
    }
    
    parameters.put(key, value);
  }
  
  /**
   * Get a parameter value by key.
   * 
   * @param key the parameter key
   * @return the corresponding parameter value, or null if not found
   */
  public String getParameterByKey(final String key) {
    
    if ((key == null) || key.isEmpty() || (parameters == null) || parameters.isEmpty()) {
      return null;
    }
    
    return parameters.get(key);
  }
  
  /**
   * Populate the URL and parameters fields based on the endpoint value.
   */
  public void parseURLandParameters() {
    if ((endpoint == null) || endpoint.isEmpty()) {
      return;
    }
    
    // Populate the url and parameters fields based on the endpoint value
    final int questionMark = endpoint.indexOf('?');
    if (questionMark < 0) {
      url = endpoint;
    } else if (questionMark == (endpoint.length() - 1)) {
      url = endpoint.substring(0, questionMark);
    } else {
      url = endpoint.substring(0, questionMark);
      
      // TODO Set any query string parameters
    }
  }
  
  /**
   * Convert the verb string into an HttpOperation enum value.
   * 
   * @param verb the verb (PUT, DELETE, etc.)
   * @return the corresponding HTTP operation
   */
  public static HttpOperation getOperation(final String verb) {
    // Get the list of possible values
    final HttpOperation[] values = HttpOperation.values();
    
    // Find a match
    HttpOperation op = HttpOperation.UNKNOWN;
    for (HttpOperation value : values) {
      if (verb.equals(value.toString())) {
        op = value;
        break;
      }
    }
    
    return op;
  }
  
  /**
   * Check if this is a supported protocol.
   * 
   * @return if this is a supported protocol
   */
  public boolean isValidProtocol() {
    return ((protocol != null) && (protocol.equals("HTTP/1.1") ||
             protocol.equals("HTTP/1.1")));
  }
  
  /**
   * Get the message body.
   * 
   * @return the message body
   */
  public char[] getMessageBody() {
    return buffer;
  }
  
  /**
   * Set the message body.
   * 
   * @param caBuffer the message body
   */
  public void setMessageBody(final char[] caBuffer) {
    buffer = caBuffer;
  }
  
  /**
   * Get the base URL.
   * 
   * @return the base URL
   */
  public String getURL() {
    return url;
  }
  
  /**
   * Clear out the values.
   */
  public void cleanup() {
    operation = null;
    endpoint = null;
    protocol = null;
    if (parameters != null) {
      parameters.clear();
      parameters = null;
    }
    buffer = null;
  }
  
  /**
   * Generate a string representation of this object.
   */
  @Override
  public String toString() {
    return "Request [operation=" + operation + ", endpoint=" + endpoint
        + ", protocol=" + protocol + "]";
  }
}
