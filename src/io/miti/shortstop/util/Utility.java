package io.miti.shortstop.util;

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
}
