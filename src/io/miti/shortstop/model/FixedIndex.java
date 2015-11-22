package io.miti.shortstop.model;

public final class FixedIndex {

  private int index = 0;
  
  private String fixed = null;
  
  public FixedIndex() {
    super();
  }
  
  public FixedIndex(final int nIndex, final String sFixed) {
    index = nIndex;
    fixed = sFixed;
  }
  
  public int getIndex() {
    return index;
  }
  
  public String getFixed() {
    return fixed;
  }
}
