package io.miti.shortstop.util;

public final class HeaderField
{
  /** Request header fields. */
  public static final String REQ_ACCEPT = "Accept";
  public static final String REQ_ACCEPT_CHARSET = "Accept-Charset";
  public static final String REQ_ACCEPT_ENCODING = "Accept-Encoding";
  public static final String REQ_ACCEPT_LANGUAGE = "Accept-Language";
  public static final String REQ_ACCEPT_DATETIME = "Accept-Datetime";
  public static final String REQ_AUTHORIZATION = "Authorization";
  public static final String REQ_CACHE_CONTROL = "Cache-Control";
  public static final String REQ_CONNECTION = "Connection";
  public static final String REQ_COOKIE = "Cookie";
  public static final String REQ_CONTENT_LENGTH = "Content-Length";
  public static final String REQ_CONTENT_MD5 = "Content-MD5";
  public static final String REQ_CONTENT_TYPE = "Content-Type";
  public static final String REQ_DATE = "Date";
  public static final String REQ_EXPECT = "Expect";
  public static final String REQ_FROM = "From";
  public static final String REQ_HOST = "Host";
  public static final String REQ_IF_MATCH = "If-Match";
  public static final String REQ_IF_MODIFIED_SINCE = "If-Modified-Since";
  public static final String REQ_IF_NONE_MATCH = "If-None-Match";
  public static final String REQ_IF_RANGE = "If-Range";
  public static final String REQ_IF_UNMODIFIED_SINCE = "If-Unmodified-Since";
  public static final String REQ_MAX_FORWARDS = "Max-Forwards";
  public static final String REQ_ORIGIN = "Origin";
  public static final String REQ_PRAGMA = "Pragma";
  public static final String REQ_PROXY_AUTHORIZATION = "Proxy-Authorization";
  public static final String REQ_RANGE = "Range";
  public static final String REQ_REFERER = "Referer";
  public static final String REQ_TE = "TE";
  public static final String REQ_USER_AGENT = "User-Agent";
  public static final String REQ_UPGRADE = "Upgrade";
  public static final String REQ_VIA = "Via";
  public static final String REQ_WARNING = "Warning";
  
  /** Response header fields. */
  public static final String RES_ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
  public static final String RES_ACCEPT_PATCH = "Accept-Patch";
  public static final String RES_ACCEPT_RANGES = "Accept-Ranges";
  public static final String RES_AGE = "Age";
  public static final String RES_ALLOW = "Allow";
  public static final String RES_CACHE_CONTROL = "Cache-Control";
  public static final String RES_CONNECTION = "Connection";
  public static final String RES_CONTENT_DISPOSITION = "Content-Disposition";
  public static final String RES_CONTENT_ENCODING = "Content-Encoding";
  public static final String RES_CONTENT_LANGUAGE = "Content-Language";
  public static final String RES_CONTENT_LENGTH = "Content-Length";
  public static final String RES_CONTENT_LOCATION = "Content-Location";
  public static final String RES_CONTENT_MD5 = "Content-MD5";
  public static final String RES_CONTENT_RANGE = "Content-Range";
  public static final String RES_CONTENT_TYPE = "Content-Type";
  public static final String RES_DATE = "Date";
  public static final String RES_ETAG = "ETag";
  public static final String RES_EXPIRES = "Expires";
  public static final String RES_LAST_MODIFIED = "Last-Modified";
  public static final String RES_LINK = "Link";
  public static final String RES_LOCATION = "Location";
  public static final String RES_P3P = "P3P";
  public static final String RES_PRAGMA = "Pragma";
  public static final String RES_PROXY_AUTHENTICATE = "Proxy-Authenticate";
  public static final String RES_PUBLIC_KEY_PINS = "Public-Key-Pins";
  public static final String RES_REFRESH = "Refresh";
  public static final String RES_RETRY_AFTER = "Retry-After";
  public static final String RES_SERVER = "Server";
  public static final String RES_SET_COOKIE = "Set-Cookie";
  public static final String RES_STATUS = "Status";
  public static final String RES_STRICT_TRANSPORT_SECURITY = "Strict-Transport-Security";
  public static final String RES_TRAILER = "Trailer";
  public static final String RES_TRANSFER_ENCODING = "Transfer-Encoding";
  public static final String RES_UPGRADE = "Upgrade";
  public static final String RES_VARY = "Vary";
  public static final String RES_VIA = "Via";
  public static final String RES_WARNING = "Warning";
  public static final String RES_WWW_AUTHENTICATE = "WWW-Authenticate";
  public static final String RES_X_FRAME_OPTIONS = "X-Frame-Options";
  
  /** Make the default constructor private. */
  private HeaderField() {
    super();
  }
}
