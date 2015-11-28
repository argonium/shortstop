package io.miti.shortstop.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import io.miti.shortstop.handler.MyHandler;
import io.miti.shortstop.model.Config;
import io.miti.shortstop.model.HttpOperation;
import io.miti.shortstop.model.Request;
import io.miti.shortstop.model.RequestInterface;
import io.miti.shortstop.model.Response;
import io.miti.shortstop.model.TemplateField;
import io.miti.shortstop.model.UrlHandler;
import io.miti.shortstop.model.UrlTemplate;
import io.miti.shortstop.util.HeaderField;

public final class Registrar {
  
  /** Create our map of handlers. */
  private static Map<UrlTemplate, List<UrlHandler>> map = new HashMap<UrlTemplate, List<UrlHandler>>(10);

  /**
   * Default constructor.
   */
  private Registrar() {
    super();
  }
  
  
  /**
   * Register all handlers.
   */
  public static void registerHandlers() {
    // Add handlers here
    register(HttpOperation.GET, "/api/sum", MyHandler::simpleHandler);
  }
  
  
  /**
   * Register a handler.
   * 
   * @param verb the HTTP operation
   * @param url the URL template
   * @param handler the handler implementation
   */
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
      // See if an entry already exists for the verb
      for (UrlHandler uh : list) {
        if (uh.getVerb().equals(verb)) {
          System.out.println("Warning: There is already a handler for " + verb.toString() + " " + url);
        }
      }
      
      // Add this entry to the existing list
      list.add(new UrlHandler(verb, handler));
    }
  }
  
  public static Response process(final Request request, final Config cfg) {
    
    // Check if we support TRACE, if this is a trace call
    if (request.getOperation().equals(HttpOperation.TRACE)) {
      if (cfg.traceEnabled()) {
        return handleTraceRequest(request);
      } else {
        // Trace is not supported (not enabled in the properties file)
        return new Response().setCode(405);
      }
    }
    
    // Check if we have any endpoints registered
    if (map.isEmpty()) {
      return null;
    }
    
    // See if the map contains any URLs matching the request's URL (template or not)
    final UrlTemplate urlImpl = new UrlTemplate(request.getURL());
    final UrlTemplate urlTemplate = findMatchingUrlTemplate(urlImpl);
    if (urlTemplate == null) {
      return null;
    }
    
    // Find the map entry by the URL template
    final List<UrlHandler> list = map.get(urlTemplate);
    if (list == null) {
      return null;
    }
    
    // If we find a match, see if there's an entry for the HTTP operation
    final UrlHandler handler = getUrlHandler(request.getOperation(), list);
    
    // If there's no verb match, return 4xx, with a list of the supported operations
    if (handler == null) {
      final Response resp = new Response().setCode(405);
      
      // Return 4xx with a list of supported operations
      final StringBuilder sb = new StringBuilder(30);
      for (UrlHandler uh : list) {
        if (sb.length() > 0) {
          sb.append(", ");
        }
        sb.append(uh.getVerb().toString());
      }
      
      // Add the field to the header and return the response
      resp.addToHeader(HeaderField.RES_ALLOW, sb.toString());
      return resp;
    }
    
    // We have a match, so add the values matching the URL template to the request
    updateRequest(request, urlImpl, urlTemplate);
    
    // Call the handler and return the response
    final Response response = handler.getHandler().process(request);
    return response;
  }
  
  
  private static Response handleTraceRequest(final Request request) {
    // TODO Auto-generated method stub
    return null;
  }


  private static UrlTemplate findMatchingUrlTemplate(UrlTemplate urlImpl) {
    // Verify we have entries in the map
    if ((map == null) || map.isEmpty()) {
      return null;
    }
    
    // Iterate over each template and look for a match
    for (UrlTemplate key : map.keySet()) {
      if (key.matchesImpl(urlImpl)) {
        return key;
      }
    }
    
    return null;
  }
  
  
  private static void updateRequest(final Request request, final UrlTemplate urlImpl,
      final UrlTemplate urlTemp) {
    
    // Get the fields from the template
    Iterator<TemplateField> fields = urlTemp.getFields();
    if (fields == null) {
      return;
    } else if (!urlTemp.hasVariables()) {
      // No variables, so no processing to perform
      return;
    }
    
    // Iterate over each field and provide its value
    int i = -1;
    while (fields.hasNext()) {
      // Increment our counter
      ++i;
      
      // Get the current field
      final TemplateField field = fields.next();
      
      // Check if we're on a variable
      if (!field.isVariable()) {
        continue;
      }
      
      // Get the value from the URL implementation
      final String value = urlImpl.getFieldByPosition(i);
      
      // Save the variable name and value
      request.addTemplateVariable(field.getField(), value);
    }
  }


  private static UrlHandler getUrlHandler(final HttpOperation verb, List<UrlHandler> list) {
    // Find a match on the verb
    for (UrlHandler uh : list) {
      if (verb.equals(uh.getVerb())) {
        return uh;
      }
    }
    
    // No match found
    return null;
  }
}
