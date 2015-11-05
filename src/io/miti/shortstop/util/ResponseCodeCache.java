package io.miti.shortstop.util;

import java.util.HashMap;
import java.util.Map;

public final class ResponseCodeCache {
  
  /** The single instance of this class. */
  private static final ResponseCodeCache cache;
  
  /** The map of response code to description. */
  private Map<Integer, String> map = null;
  
  static {
    /** Instantiate the class and populate the map. */
    cache = new ResponseCodeCache();
  }
  
  /**
   * Private default constructor.
   */
  private ResponseCodeCache() {
    populateMap();
  }
  
  /**
   * Populate with some response codes and their description.
   */
  private void populateMap() {
    // Create the map
    map = new HashMap<Integer, String>(80);
    
    // Populate with some values
    map.put(Integer.valueOf(100), "Continue");
    map.put(Integer.valueOf(101), "Switching Protocols");
    map.put(Integer.valueOf(102), "Processing");
    map.put(Integer.valueOf(200), "OK");
    map.put(Integer.valueOf(201), "Created");
    map.put(Integer.valueOf(202), "Accepted");
    map.put(Integer.valueOf(203), "Non-Authoritative Information");
    map.put(Integer.valueOf(204), "No Content");
    map.put(Integer.valueOf(205), "Reset Content");
    map.put(Integer.valueOf(206), "Partial Content");
    map.put(Integer.valueOf(207), "Multi-Status");
    map.put(Integer.valueOf(208), "Already Reported");
    map.put(Integer.valueOf(226), "IM Used");
    map.put(Integer.valueOf(300), "Multiple Choices");
    map.put(Integer.valueOf(301), "Moved Permanently");
    map.put(Integer.valueOf(302), "Found");
    map.put(Integer.valueOf(303), "See Other");
    map.put(Integer.valueOf(304), "Not Modified");
    map.put(Integer.valueOf(305), "Use Proxy");
    map.put(Integer.valueOf(306), "Switch Proxy");
    map.put(Integer.valueOf(307), "Temporary Redirect");
    map.put(Integer.valueOf(308), "Permanent Redirect");
    map.put(Integer.valueOf(400), "Bad Request");
    map.put(Integer.valueOf(401), "Unauthorized");
    map.put(Integer.valueOf(402), "Payment Required");
    map.put(Integer.valueOf(403), "Forbidden");
    map.put(Integer.valueOf(404), "Not Found");
    map.put(Integer.valueOf(405), "Method Not Allowed");
    map.put(Integer.valueOf(406), "Not Acceptable");
    map.put(Integer.valueOf(407), "Proxy Authentication Required");
    map.put(Integer.valueOf(408), "Request Timeout");
    map.put(Integer.valueOf(409), "Conflict");
    map.put(Integer.valueOf(410), "Gone");
    map.put(Integer.valueOf(411), "Length Required");
    map.put(Integer.valueOf(412), "Precondition Failed");
    map.put(Integer.valueOf(413), "Payload Too Large");
    map.put(Integer.valueOf(414), "Request-URI Too Long");
    map.put(Integer.valueOf(415), "Unsupported Media Type");
    map.put(Integer.valueOf(416), "Requested Range Not Satisfiable");
    map.put(Integer.valueOf(417), "Expectation Failed");
    map.put(Integer.valueOf(418), "I'm a teapot");
    map.put(Integer.valueOf(419), "Authentication Timeout");
    map.put(Integer.valueOf(420), "Method Failure");
    map.put(Integer.valueOf(421), "Misdirected Request");
    map.put(Integer.valueOf(422), "Unprocessable Entity");
    map.put(Integer.valueOf(423), "Locked");
    map.put(Integer.valueOf(424), "Failed Dependency");
    map.put(Integer.valueOf(426), "Upgrade Required");
    map.put(Integer.valueOf(428), "Precondition Required");
    map.put(Integer.valueOf(429), "Too Many Requests");
    map.put(Integer.valueOf(431), "Request Header Fields Too Large");
    map.put(Integer.valueOf(440), "Login Timeout");
    map.put(Integer.valueOf(444), "No Response");
    map.put(Integer.valueOf(449), "Retry With");
    map.put(Integer.valueOf(450), "Blocked by Windows Parental Controls");
    map.put(Integer.valueOf(451), "Unavailable For Legal Reasons");
    map.put(Integer.valueOf(494), "Request Header Too Large");
    map.put(Integer.valueOf(495), "Cert Error");
    map.put(Integer.valueOf(496), "No Cert");
    map.put(Integer.valueOf(497), "HTTP to HTTPS");
    map.put(Integer.valueOf(498), "Token expired/invalid");
    map.put(Integer.valueOf(499), "Client Closed Request");
    map.put(Integer.valueOf(500), "Internal Server Error");
    map.put(Integer.valueOf(501), "Not Implemented");
    map.put(Integer.valueOf(502), "Bad Gateway");
    map.put(Integer.valueOf(503), "Service Unavailable");
    map.put(Integer.valueOf(504), "Gateway Timeout");
    map.put(Integer.valueOf(505), "HTTP Version Not Supported");
    map.put(Integer.valueOf(506), "Variant Also Negotiates");
    map.put(Integer.valueOf(507), "Insufficient Storage");
    map.put(Integer.valueOf(508), "Loop Detected");
    map.put(Integer.valueOf(509), "Bandwidth Limit Exceeded");
    map.put(Integer.valueOf(510), "Not Extended");
    map.put(Integer.valueOf(511), "Network Authentication Required");
    map.put(Integer.valueOf(520), "Unknown Error");
    map.put(Integer.valueOf(522), "Origin Connection Time-out");
    map.put(Integer.valueOf(598), "Network read timeout error");
    map.put(Integer.valueOf(599), "Network connect timeout error");
  }
  
  /**
   * Return whether we have a description for the code.
   * 
   * @param key the response code
   * @return whether we have the key in our map
   */
  public boolean hasResponseCode(final int key) {
    return map.containsKey(Integer.valueOf(key));
  }
  
  /**
   * Return the response description for a code.
   * 
   * @param key the response code
   * @return the corresponding response description
   */
  public String getResponseMessage(final int key) {
    final String str = map.get(Integer.valueOf(key));
    return str;
  }
  
  /**
   * Get the single instance of this class.
   * 
   * @return the single instance of this class
   */
  public static ResponseCodeCache getCache() {
    return cache;
  }
}
