package io.miti.shortstop.util;

import java.util.HashMap;
import java.util.Map;

public final class ContentTypeCache {
  
  /** The single instance of this class. */
  private static final ContentTypeCache cache;
  
  /** The map of file extension to MIME type. */
  private Map<String, String> map = null;
  
  static {
    /** Instantiate the class and populate the map. */
    cache = new ContentTypeCache();
  }
  
  /**
   * Private default constructor.
   */
  private ContentTypeCache() {
    populateMap();
  }
  
  /**
   * Populate with some common file content types and their MIME types.
   */
  private void populateMap() {
    // Create the map
    map = new HashMap<String, String>(10);
    
    // Populate with some values
    map.put("7z", "application/x-7z-compressed");
    map.put("avi", "video/x-msvideo");
    map.put("azw", "application/vnd.amazon.ebook");
    map.put("bin", "application/octet-stream");
    map.put("bmp", "image/bmp");
    map.put("cer", "application/pkix-cert");
    map.put("css", "text/css");
    map.put("csv", "text/csv");
    map.put("doc", "application/msword");
    map.put("epub", "application/epub+zip");
    map.put("gif", "image/gif");
    map.put("html", "text/html");
    map.put("ico", "image/x-icon");
    map.put("ics", "text/calendar");
    map.put("jnlp", "application/x-java-jnlp-file");
    map.put("jpeg", "image/jpeg");
    map.put("jpg", "image/jpeg");
    map.put("jpgv", "video/jpeg");
    map.put("js", "application/javascript");
    map.put("json", "application/json");
    map.put("latex", "application/x-latex");
    map.put("mpeg", "video/mpeg");
    map.put("pdf", "application/pdf");
    map.put("pki", "application/pkixcmp");
    map.put("rar", "application/x-rar-compressed");
    map.put("rtf", "application/rtf");
    map.put("swf", "application/x-shockwave-flash");
    map.put("tar", "application/x-tar");
    map.put("tiff", "image/tiff");
    map.put("torrent", "application/x-bittorrent");
    map.put("tsv", "text/tab-separated-values");
    map.put("txt", "text/plain");
    map.put("uu", "text/x-uuencode");
    map.put("wmv", "video/x-ms-wmv");
    map.put("xls", "application/vnd.ms-excel");
    map.put("xml", "application/xml");
    map.put("yaml", "text/yaml");
    map.put("zip", "application/zip");
  }
  
  /**
   * Return whether we contain the specified file extension in our map.
   * A sample value for key is "html".
   * 
   * @param key the file extension (no leading period)
   * @return whether we have the key in our map
   */
  public boolean hasContentType(final String key) {
    if (key == null) {
      return false;
    }
    
    return map.containsKey(key.toLowerCase());
  }
  
  /**
   * Return the MIME type for a file extension.
   * 
   * @param key the file extension (e.g., "css")
   * @return the corresponding MIME type, or null if not found
   */
  public String getContentTypeMIMEType(final String key) {
    final String str = ((key == null) ? null : map.get(key.toLowerCase()));
    return str;
  }
  
  /**
   * Get the single instance of this class.
   * 
   * @return the single instance of this class
   */
  public static ContentTypeCache getCache() {
    return cache;
  }
}
