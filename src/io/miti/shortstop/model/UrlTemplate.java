package io.miti.shortstop.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

public final class UrlTemplate {
  
  /** The URL template. */
  private String template = null;
  
  /** The parsed version. */
  private List<TemplateField> fields = null;
  
  /** Whether there are variable fields in the URL. */
  private boolean hasVariables = false;
  
  /** The number of elements. */
  private int numFields = 0;
  
  /** The map of position to String for the non-variable fields. */
  private List<FixedIndex> fixedFields = null;
  
  /**
   * Default constructor.
   */
  @SuppressWarnings("unused")
  private UrlTemplate() {
    super();
  }
  
  /**
   * Constructor taking a template.
   * 
   * @param template the URL template
   */
  public UrlTemplate(final String template) {
    this.template = template;
    parseFields(template);
  }
  
  private void parseFields(final String url) {
    fields = new ArrayList<TemplateField>(3);
    fixedFields = new ArrayList<FixedIndex>(3);
    if ((url == null) || (url.isEmpty())) {
      return;
    }
    
    numFields = 0;
    StringTokenizer tokens = new StringTokenizer(url, "/");
    while (tokens.hasMoreTokens()) {
      final String token = tokens.nextToken();
      
      final TemplateField tf = new TemplateField(token);
      fields.add(tf);
      
      if (!tf.isVariable()) {
        fixedFields.add(new FixedIndex(numFields, tf.getField()));
      } else {
        hasVariables = true;
      }
      
      ++numFields;
    }
  }
  
  public boolean hasVariables() {
    return hasVariables;
  }
  
  /**
   * Return the template.
   * 
   * @return the URL template
   */
  public String getTemplate() {
    return template;
  }
  
  public Iterator<TemplateField> getFields() {
    if (fields == null) {
      return null;
    }
    
    return fields.iterator();
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((fixedFields == null) ? 0 : fixedFields.hashCode());
    result = prime * result + numFields;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    UrlTemplate other = (UrlTemplate) obj;
    if (fixedFields == null) {
      if (other.fixedFields != null)
        return false;
    } else if (!fixedFields.equals(other.fixedFields))
      return false;
    if (numFields != other.numFields)
      return false;
    return true;
  }

  public boolean matchesImpl(final UrlTemplate urlImpl) {
    if (numFields != urlImpl.numFields) {
      return false;
    }
    
    for (int i = 0; i < numFields; ++i) {
      final TemplateField tf = fields.get(i);
      
      // If this is a variable, don't compare
      if (tf.isVariable()) {
        continue;
      }
      
      if (!tf.getField().equals(urlImpl.fields.get(i))) {
        return false;
      }
    }
    
    // If we reach here, we have a match
    return true;
  }

  public String getFieldByPosition(int i) {
    if ((fields == null) || (i < 0) || (i >= fields.size())) {
      System.err.println("Warning: getFieldByPosition() has an error");
      return null;
    }
    
    return fields.get(i).getField();
  }
}
