package io.miti.shortstop.util;

public final class HeaderField
{
  public static final String HK_CONTENT_LENGTH = "Content-Length";
  public static final String HK_ACCEPT_ENCODING = "Accept-Encoding";
  public static final String HK_TRANSFER_ENCODING = "Transfer-Encoding";
  public static final String HK_CONTENT_ENCODING = "Content-Encoding";
  
  /** Make the default constructor private. */
  private HeaderField() {
    super();
  }
}
