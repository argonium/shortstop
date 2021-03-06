package io.miti.shortstop.handler;

import io.miti.shortstop.model.Request;
import io.miti.shortstop.model.Response;

public final class MyHandler {

  public MyHandler() {
    super();
  }
  
  public static Response simpleHandler(final Request req) {
    final Response resp = new Response(200);
    String msg = "{\"sum\": \"unknown\"}";
    resp.setJsonContentType().setBody(msg);
    return resp;
  }
  
  public static Response addStrings(final Request req) {
    final Response resp = new Response(200);
    String sum = req.getTemplateVariable("val1") + req.getTemplateVariable("val2");
    String msg = "{\"sum\": \"" + sum + "\"}";
    resp.setJsonContentType().setBody(msg);
    return resp;
  }
}
