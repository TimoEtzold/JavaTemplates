package de.tu_dortmund.javatemplates.template;

import java.util.Map;

import org.eclipse.jdt.core.dom.CompilationUnit;

/**
 * Interface for helping methods used by {@link de.tu_dortmund.javatemplates.plugin.CompilationParticipant} and {@link Template}.
 * 
 * @author Timo Etzold
 *
 */
public interface ITemplateHelper {

  /**
   * Fixes errors in the source code which occur because of the usage of template variables.
   * 
   * @param sSource the source code of the template (including template variables)
   * @return the source code of the template containing no errors created by template variables
   */
  public String fixErrors(String sSource);
  
  /**
   * Returns the replacement String for a given {@link TemplateVariable} in an {@link ITemplate}.
   * 
   * @param template the template which contains the {@link TemplateVariable}
   * @param templateVariable the template variable inside the {@link ITemplate}
   * @return the replacement String for the template variable in the template
   */
  public String getReplacementForTemplateVariable(ITemplate template, TemplateVariable templateVariable);
  
  /**
   * Fills the {@link ITemplate} with the contents of the {@link Map} and returns the compiled {@link CompilationUnit}.
   * 
   * @param template the template containing variables
   * @param attributes a map with content for each template variable
   * @return the compiled CompilationUnit
   */
  public CompilationUnit fillTemplate(ITemplate template, Map<String, String> attributes);
}
