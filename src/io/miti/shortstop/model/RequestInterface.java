package io.miti.shortstop.model;

@FunctionalInterface
public interface RequestInterface {
  Response process(Request req);
}
