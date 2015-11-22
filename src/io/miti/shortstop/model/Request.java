package io.miti.shortstop.model;

import io.miti.shortstop.util.Utility;

import java.util.HashMap;
import java.util.Map;

public final class Request
{
  /** The HTTP operation (PUT, POST, GET, etc.) */
  private HttpOperation operation = null;
  
  /** The original endpoint URL passed to the server. */
  private String endpoint = null;
  
  /** The protocol (e.g., "HTTP/1.0"). */
  private String protocol = null;
  
  /** Query string parameters (e.g., "?k1=v1&k2=v2..."). */
  private Map<String, String> parameters = null;
  
  /** Any payload in the request. */
  private char[] buffer = null;
  
  /** The parsed URL (from the endpoint, minus parameters and fragment). */
  private String url = null;
  
  /** Any fragment from the URL (e.g., "#top"). */
  private String fragment = null;
  
  /** Map of key/value pairs from the header. */
  private Map<String, String> header = null; 
  
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
    endpoint = decode(line.substring(index0 + 1, index1).trim());
    protocol = line.substring(index1 + 1).trim();
  }
  
  /**
   * Decode hex strings in the input.
   * 
   * @param input the input (endpoint)
   * @return the input with hex encodings converted
   */
  private static String decode(final String input) {
    
    // Check the input
    if ((input == null) || input.isEmpty() || (input.indexOf('%') < 0)) {
      return input;
    }
    
    // Check for hex-encoded strings
    final int len = input.length();
    StringBuilder sb = new StringBuilder(len);
    for (int i = 0; i < len; ++i) {
      final char ch = input.charAt(i);
      if (ch == '%') {
        if (i < (len - 2)) {
          final String hexVal = input.substring(i + 1, i + 3);
          final char hexNum = (char) Utility.parseStringAsInt(hexVal, ((int) '_'), 16);
          sb.append(Character.valueOf(hexNum));
          i += 2;
        }
      } else {
        sb.append(ch);
      }
    }
    
    return sb.toString();
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
    
    // Parse any fragment
    final int fragmentIndex = endpoint.indexOf('#');
    if (fragmentIndex > 0) {
      fragment = endpoint.substring(fragmentIndex + 1);
    }
    
    // Get the endpoint minus any fragment
    final String tempEnd = ((fragmentIndex < 0) ? endpoint : endpoint.substring(0, fragmentIndex));
    
    // Populate the url and parameters fields based on the endpoint value
    final int questionMark = tempEnd.indexOf('?');
    if (questionMark < 0) {
      url = tempEnd;
    } else if (questionMark == (tempEnd.length() - 1)) {
      url = tempEnd.substring(0, questionMark);
    } else {
      url = tempEnd.substring(0, questionMark);
      
      // Set any query string parameters
      final String params = tempEnd.substring(questionMark + 1);
      String[] fields = params.split("&");
      final int len = fields.length;
      for (int i = 0; i < len; ++i) {
        final String field = fields[i];
        final int equalsIndex = field.indexOf('=');
        if (equalsIndex > 0)  {
          final String key = field.substring(0, equalsIndex);
          final String value = (equalsIndex < (field.length() - 1)) ? field.substring(equalsIndex + 1) : "";
          addParameter(key, value);
        }
      }
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
    return ((protocol != null) && (protocol.equals("HTTP/1.0") ||
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
  
  public boolean hasBody() {
    return (buffer != null);
  }
  
  /**
   * Return any fragment from the endpoint, or null if none found.
   * 
   * @return fragment from the endpoint
   */
  public String getFragment() {
    return fragment;
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
   * Return the endpoint.
   * 
   * @return the endpoint
   */
  public String getEndpoint() {
    return endpoint;
  }
  
  /**
   * Return the protocol string.
   * 
   * @return the protocol
   */
  public String getProtocol() {
    return protocol;
  }
  
  /**
   * Return the operation (verb).
   * 
   * @return the operation (verb)
   */
  public HttpOperation getOperation() {
    return operation;
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
   * Add a key/value pair to the header.
   * 
   * @param key the key
   * @param value the value
   */
  public void addHeaderRow(final String key, final String value) {
    
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
   * Generate a string representation of this object.
   */
  @Override
  public String toString() {
    return "Request [operation=" + operation + ", endpoint=" + endpoint
        + ", protocol=" + protocol + "]";
  }
  
  /**
   * Returns the value matching the key from the header.
   * 
   * @param key the key
   * @return the value for the key in the header
   */
  public String headerGetKey(final String key) {
    if ((header == null) || (key == null)) {
      return null;
    }
    
    return header.get(key);
  }
  
  /**
   * Return whether the header contains the key.
   * 
   * @param key the key (must not be null)
   * @return if header contains the key
   */
  public boolean headerContainsKey(final String key) {
    return ((header != null) && (key != null) && (header.containsKey(key)));
  }
}
