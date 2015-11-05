package io.miti.shortstop.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

public final class Utility
{
  private Utility() {
    super();
  }
  
  /**
   * Parse a string as an integer.
   * 
   * @param str the input string
   * @param defaultValue the default value to return
   * @return the string as an int, or the default value
   */
  public static int parseStringAsInt(final String str, final int defaultValue) {
    return parseStringAsInt(str, defaultValue, 10);
  }
  
  /**
   * Parse a string as an integer.
   * 
   * @param str the input string
   * @param defaultValue the default value to return
   * param base the base of the numeric string
   * @return the string as an int, or the default value
   */
  public static int parseStringAsInt(final String str, final int defaultValue, final int base) {
    if ((str == null) || (str.trim().isEmpty())) {
      return defaultValue;
    }
    
    int val = 0;
    try {
      val = Integer.parseInt(str, base);
    } catch (NumberFormatException nfe) {
      System.err.println("Invalid numeric value: " + str);
      val = defaultValue;
    }
    
    return val;
  }
  
  /**
   * Parse a string as a boolean (true or false).
   * 
   * @param str the input string
   * @param defaultValue the default value
   * @return the str value as a boolean, or defaultValue
   */
  public static boolean parseStringAsBoolean(final String str, final boolean defaultValue) {
    if ((str == null) || (str.trim().isEmpty())) {
      return defaultValue;
    }
    
    final boolean val = ((str.equals("true")) ? true : defaultValue);
    return val;
  }
  
  /**
   * Compute the MD5 hash for an input string.
   * 
   * @param str the input string
   * @return the MD5 value for str
   */
  public static String getMD5(final byte[] str) {
    
    String md5 = null;
    try {
      MessageDigest md = MessageDigest.getInstance("MD5");
      byte[] digest = md.digest(str);
      md5 = (new HexBinaryAdapter()).marshal(digest);
    } catch (NoSuchAlgorithmException e) {
      System.out.println("Exception computing MD5: " + e.getMessage());
    }
    
    return md5;
  }
}
