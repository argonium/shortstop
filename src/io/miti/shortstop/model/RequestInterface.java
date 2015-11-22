package io.miti.shortstop.model;

@FunctionalInterface
public interface RequestInterface {
  String handler(String stringValue, int intValue);
}
