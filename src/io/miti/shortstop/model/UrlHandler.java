package io.miti.shortstop.model;

public class UrlHandler {
  
  /** The handler for the URL. */
  private RequestInterface handler = null;
  
  /** The verb. */
  private HttpOperation verb = null;
  
  /**
   * Default constructor.
   */
  @SuppressWarnings("unused")
  private UrlHandler() {
    super();
  }
  
  /**
   * Constructor.
   * 
   * @param verb the HTTP operation
   * @param handler the handler method reference
   */
  public UrlHandler(final HttpOperation verb, final RequestInterface handler) {
    this.verb = verb;
    this.handler = handler;
  }
  
  public HttpOperation getVerb() {
    return verb;
  }
  
  public RequestInterface getHandler() {
    return handler;
  }
}
