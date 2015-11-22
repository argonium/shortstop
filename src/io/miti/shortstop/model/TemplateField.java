package io.miti.shortstop.model;

public final class TemplateField {
  
  private String field = null;
  
  private boolean isVariable = false;
  
  /**
   * Default constructor.
   */
  @SuppressWarnings("unused")
  private TemplateField() {
    super();
  }
  
  /**
   * Constructor.
   * 
   * @param sField the field from a URL
   */
  public TemplateField(final String sField) {
    if (sField.startsWith(":")) {
      field = sField.substring(1);
      isVariable = true;
    } else {
      field = sField;
      isVariable = false;
    }
  }
  
  public String getField() {
    return field;
  }
  
  public boolean isVariable() {
    return isVariable;
  }
}
