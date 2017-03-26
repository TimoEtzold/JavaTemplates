package de.tu_dortmund.javatemplates.template;

import org.apache.commons.lang3.StringUtils;

/**
 * Each template variable in an implementation of {@link ITemplate} is represented by an instance of this class.
 * 
 * @author Timo Etzold
 *
 */
public class TemplateVariable {
  private String sName;
  private TYPE type;
  private String error;
  
  public enum TYPE { ARGS, ARGSDEF, EXPR, NAME, RETURNTYPE, VALUE };
  
  /**
   * Creates a template variable with a name and a type.
   * 
   * @param sName the name of the new template variable
   * @param sType the type of the new template varaible
   */
  public TemplateVariable(String sName, String sType){
    setName(sName);
    setType(sType);
  }

  /**
   * Returns the name of this template variable.
   * 
   * @return the name of this template variable
   */
  public String getName() {
    return sName;
  }

  /**
   * Sets the name of this template variable.
   * 
   * @param sName the name of this template variable
   */
  public void setName(String sName){
    this.sName = sName;
  }

  /**
   * Returns the {@link TemplateVariable.TYPE} of this template variable.
   * 
   * @return the {@link TemplateVariable.TYPE} of this template variable
   */
  public TYPE getType() {
    return type;
  }
  
  /**
   * Tries to set the {@link TemplateVariable.TYPE} of this template variable using a String.
   * 
   * @param sType the type of this template variable as String
   */
  public void setType(String sType){
    if(StringUtils.equalsIgnoreCase(sType, TYPE.ARGS.toString())){
      setType(TYPE.ARGS);
    }
    else if(StringUtils.equalsIgnoreCase(sType, TYPE.ARGSDEF.toString())){
      setType(TYPE.ARGSDEF);
    }
    else if(StringUtils.equalsIgnoreCase(sType, TYPE.EXPR.toString())){
      setType(TYPE.EXPR);
    }
    else if (StringUtils.equalsIgnoreCase(sType, TYPE.NAME.toString())){
      setType(TYPE.NAME);
    }
    else if (StringUtils.equalsIgnoreCase(sType, TYPE.RETURNTYPE.toString())){
      setType(TYPE.RETURNTYPE);
    }
    else if(StringUtils.equalsIgnoreCase(sType, TYPE.VALUE.toString())){
      setType(TYPE.VALUE);
    }
    else 
      System.err.println("Unknown Type: " + sType);
  }

  /**
   * Sets the {@link TemplateVariable.TYPE} of this template variable.
   * 
   * @param type the {@link TemplateVariable.TYPE} of this template variable
   */
  public void setType(TYPE type) {
    this.type = type;
  }
  
  /**
   * Returns the error message of this template variable.
   * 
   * @return the error message of this template variable or {@code null}
   */
  public String getError() {
    return error;
  }

  /**
   * Sets the error message of this template variable.
   * 
   * @param error the error message of this template variable
   */
  public void setError(String error) {
    this.error = error;
  }
}
