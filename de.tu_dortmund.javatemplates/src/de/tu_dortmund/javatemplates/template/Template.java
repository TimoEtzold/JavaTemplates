package de.tu_dortmund.javatemplates.template;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.CompilationUnit;

/**
 * Default implementation of {@link ITemplate}. <br>
 * A {@link TemplateVariable} is defined using {@code ${NAME:TYPE}, where <br>
 *   {@code NAME} consists of at least one word character and <br>
 *   {@code TYPE} is any of {@code [ARGS, ARGSDEF, EXPR, NAME, RETURNTYPE, VALUE]} <br>
 * 
 * @author Timo Etzold
 *
 */
public class Template implements ITemplate {
  private final String sRegex = "\\$\\{(?<name>\\w+):(?<type>\\w+)\\}";
  
  private String sCode = null;
  
  private List<TemplateVariable> templateVariables = new ArrayList<>();
  
  /* (non-Javadoc)
   * @see de.tu_dortmund.javatemplates.template.ITemplate#getRegex()
   */
  @Override
  public String getRegex() {
    return sRegex;
  }

  /* (non-Javadoc)
   * @see de.tu_dortmund.javatemplates.template.ITemplate#setCode(java.lang.String)
   */
  @Override
  public void setCode(String sCode) {
    this.sCode = sCode;
  }
  
  /* (non-Javadoc)
   * @see de.tu_dortmund.javatemplates.template.ITemplate#getCode()
   */
  @Override
  public String getCode() {
    return sCode;
  }
  
  /* (non-Javadoc)
   * @see de.tu_dortmund.javatemplates.template.ITemplate#setTemplateVariables(java.util.List)
   */
  @Override
  public void setTemplateVariables(List<TemplateVariable> templateVariables) {
    this.templateVariables = templateVariables;
  }

  /* (non-Javadoc)
   * @see de.tu_dortmund.javatemplates.template.ITemplate#getTemplateVariables()
   */
  @Override
  public List<TemplateVariable> getTemplateVariables() {
    return templateVariables;
  }

  /* (non-Javadoc)
   * @see de.tu_dortmund.javatemplates.template.ITemplate#addTemplateVariable(de.tu_dortmund.javatemplates.template.TemplateVariable)
   */
  @Override
  public void addTemplateVariable(TemplateVariable templateVariable) {
    this.templateVariables.add(templateVariable);
  }

  /* (non-Javadoc)
   * @see de.tu_dortmund.javatemplates.template.ITemplate#removeTemplateVariable(de.tu_dortmund.javatemplates.template.TemplateVariable)
   */
  @Override
  public void removeTemplateVariable(TemplateVariable templateVariable) {
    this.templateVariables.remove(templateVariable);
  }

  /**
   * Inserts the code from the {@link Map} into each {@link TemplateVariable} and parses the resulting Code into a {@link CompilationUnit}. <br>
   * {@link IllegalArgumentException} will be thrown if: <br>
   * - a {@link TemplateVariable} of this template has no name or type<br>
   * - the {@link Map} is missing code for a variable<br>
   * - the type of the code which is to be inserted into a variable does not match the variable's type<br>
   * - no type can be determined for the code, which happens if there are errors in the code.
   * 
   * @param attributes the {@link Map} containing the code for each {@link TemplateVariable}
   * @return the {@link CompilationUnit} that is parsed after inserting the {@code TemplateVariables}
   * @throws IllegalArgumentException if<br> 
   * - a {@link TemplateVariable} has errors or <br>
   * - the {@link Map} has errors
   */
  @Override
  public CompilationUnit fillTemplate(Map<String, String> attributes) throws IllegalArgumentException {
    return TemplateUtils.getInstance().getTemplateHelper().fillTemplate(this, attributes);
  }
}
