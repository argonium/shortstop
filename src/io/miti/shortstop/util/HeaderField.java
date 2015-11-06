package io.miti.shortstop.util;

public final class HeaderField
{
  /** Request header fields. */
  public static String REQ_ACCEPT = "Accept";
  public static String REQ_ACCEPT_CHARSET = "Accept-Charset";
  public static String REQ_ACCEPT_ENCODING = "Accept-Encoding";
  public static String REQ_ACCEPT_LANGUAGE = "Accept-Language";
  public static String REQ_ACCEPT_DATETIME = "Accept-Datetime";
  public static String REQ_AUTHORIZATION = "Authorization";
  public static String REQ_CACHE_CONTROL = "Cache-Control";
  public static String REQ_CONNECTION = "Connection";
  public static String REQ_COOKIE = "Cookie";
  public static String REQ_CONTENT_LENGTH = "Content-Length";
  public static String REQ_CONTENT_MD5 = "Content-MD5";
  public static String REQ_CONTENT_TYPE = "Content-Type";
  public static String REQ_DATE = "Date";
  public static String REQ_EXPECT = "Expect";
  public static String REQ_FROM = "From";
  public static String REQ_HOST = "Host";
  public static String REQ_IF_MATCH = "If-Match";
  public static String REQ_IF_MODIFIED_SINCE = "If-Modified-Since";
  public static String REQ_IF_NONE_MATCH = "If-None-Match";
  public static String REQ_IF_RANGE = "If-Range";
  public static String REQ_IF_UNMODIFIED_SINCE = "If-Unmodified-Since";
  public static String REQ_MAX_FORWARDS = "Max-Forwards";
  public static String REQ_ORIGIN = "Origin";
  public static String REQ_PRAGMA = "Pragma";
  public static String REQ_PROXY_AUTHORIZATION = "Proxy-Authorization";
  public static String REQ_RANGE = "Range";
  public static String REQ_REFERER = "Referer";
  public static String REQ_TE = "TE";
  public static String REQ_USER_AGENT = "User-Agent";
  public static String REQ_UPGRADE = "Upgrade";
  public static String REQ_VIA = "Via";
  public static String REQ_WARNING = "Warning";
  
  /** Response header fields. */
  public static String RES_ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
  public static String RES_ACCEPT_PATCH = "Accept-Patch";
  public static String RES_ACCEPT_RANGES = "Accept-Ranges";
  public static String RES_AGE = "Age";
  public static String RES_ALLOW = "Allow";
  public static String RES_CACHE_CONTROL = "Cache-Control";
  public static String RES_CONNECTION = "Connection";
  public static String RES_CONTENT_DISPOSITION = "Content-Disposition";
  public static String RES_CONTENT_ENCODING = "Content-Encoding";
  public static String RES_CONTENT_LANGUAGE = "Content-Language";
  public static String RES_CONTENT_LENGTH = "Content-Length";
  public static String RES_CONTENT_LOCATION = "Content-Location";
  public static String RES_CONTENT_MD5 = "Content-MD5";
  public static String RES_CONTENT_RANGE = "Content-Range";
  public static String RES_CONTENT_TYPE = "Content-Type";
  public static String RES_DATE = "Date";
  public static String RES_ETAG = "ETag";
  public static String RES_EXPIRES = "Expires";
  public static String RES_LAST_MODIFIED = "Last-Modified";
  public static String RES_LINK = "Link";
  public static String RES_LOCATION = "Location";
  public static String RES_P3P = "P3P";
  public static String RES_PRAGMA = "Pragma";
  public static String RES_PROXY_AUTHENTICATE = "Proxy-Authenticate";
  public static String RES_PUBLIC_KEY_PINS = "Public-Key-Pins";
  public static String RES_REFRESH = "Refresh";
  public static String RES_RETRY_AFTER = "Retry-After";
  public static String RES_SERVER = "Server";
  public static String RES_SET_COOKIE = "Set-Cookie";
  public static String RES_STATUS = "Status";
  public static String RES_STRICT_TRANSPORT_SECURITY = "Strict-Transport-Security";
  public static String RES_TRAILER = "Trailer";
  public static String RES_TRANSFER_ENCODING = "Transfer-Encoding";
  public static String RES_UPGRADE = "Upgrade";
  public static String RES_VARY = "Vary";
  public static String RES_VIA = "Via";
  public static String RES_WARNING = "Warning";
  public static String RES_WWW_AUTHENTICATE = "WWW-Authenticate";
  public static String RES_X_FRAME_OPTIONS = "X-Frame-Options";
  
  /** Make the default constructor private. */
  private HeaderField() {
    super();
  }
}
