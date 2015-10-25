package io.miti.shortstop.server;

public final class Request
{
  public HttpOperation operation = null;
  public String endpoint = null;
  public String protocol = null;
  
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
  
  public boolean isValidProtocol() {
    return ((protocol != null) && (protocol.equals("HTTP/1.1") ||
             protocol.equals("HTTP/1.1")));
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
