package io.miti.shortstop.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.miti.shortstop.model.HttpOperation;
import io.miti.shortstop.model.Request;
import io.miti.shortstop.model.RequestInterface;
import io.miti.shortstop.model.Response;
import io.miti.shortstop.model.UrlHandler;
import io.miti.shortstop.model.UrlTemplate;

public final class Registrar {
  
  /** Create our map of handlers. */
  private static Map<UrlTemplate, List<UrlHandler>> map = new HashMap<UrlTemplate, List<UrlHandler>>(10);

  private Registrar() {
    super();
  }
  
  public static void register(final HttpOperation verb, final String url, final RequestInterface handler) {
    // Generate the URL template object
    final UrlTemplate key = new UrlTemplate(url);
    
    // See if it's in the map
    List<UrlHandler> list = map.get(key);
    if (list == null) {
      // Add a new entry to the map
      list = new ArrayList<UrlHandler>(3);
      list.add(new UrlHandler(verb, handler));
      map.put(key, list);
    } else {
      // Add this entry to the existing list
      list.add(new UrlHandler(verb, handler));
    }
  }
  
  public static Response process(final Request request) {
    
    // Check if we have any endpoints registered
    if (map.isEmpty()) {
      Response resp = new Response().setAs404();
      return resp;
    }
    
    // TODO See if the map contains any URLs matching the request's URL (template or not)
    
    // TODO If we find a match, see if there's an entry for the HTTP oepration
    
    // TODO If there's no verb match, return 4xx, with a list of the supported operations
    
    // TODO We have a match, so add the values matching the URL template to the request
    
    // TODO Call the handler and return the response
    
    return null;
  }
}
