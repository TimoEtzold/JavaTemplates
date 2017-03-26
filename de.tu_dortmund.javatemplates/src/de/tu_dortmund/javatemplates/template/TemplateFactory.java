package de.tu_dortmund.javatemplates.template;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

/**
 * The Factory which is to be used to create instances of {@link ITemplate}.
 * 
 * @author Timo Etzold
 *
 */
public class TemplateFactory{
  
  /**
   * Tries to create an instance of {@link ITemplate} for the given source code. <br>
   * The eclipse plugin registry is used to determine all implementations of {@link ITemplate}. <br>
   * The method tries to create an instance of {@link ITemplate} for each found implementation. <br>
   * If for any implementation an instance is returned by {@link #createSpecificTemplateForSource(ITemplate, String) createSpecificTemplateForSource},
   * the search ends and the instance is returned.
   * 
   * @param sSource the source code whose {@link ITemplate} is to be created
   * @return the {@link ITemplate} for the source code or {@code null} if none can be determined
   */
  public static ITemplate createTemplateForSource(String sSource){
    if(sSource == null){
      return null;
    }
    
    ITemplate template = null;
    
    for(ITemplate itemplate : getAllImplementations()){
      template = createSpecificTemplateForSource(itemplate, sSource);
      if(template != null){
        break;
      }
    }
    
    return template;
  }
  
  /**
   * Tries to create an instance of a specific implementation of {@link ITemplate} with the source code. <br>
   * If any parameter is {@code null}, {@code null} is returned. <br>
   * If any {@link TemplateVariable} can be found using {@link ITemplate#getRegex() the regular expression},
   * the instance is returned, else {@code null} will be returned.
   * 
   * @param itemplate the implementation of {@link ITemplate} with which the template should be created
   * @param sSource the source code of the template
   * @return the instance of {@link ITemplate} if it contains at least one {@link TemplateVariable}, else {@code null}
   */
  public static ITemplate createSpecificTemplateForSource(ITemplate itemplate, String sSource){
    if(itemplate != null){
      if(sSource != null){
        if(itemplate.getRegex() != null){
          Pattern pattern = Pattern.compile(itemplate.getRegex());
          Matcher matcher = pattern.matcher(sSource);
          
          itemplate.setCode(sSource);
          
          List<TemplateVariable> templateVariables = new ArrayList<>();
          while(matcher.find()){
            TemplateVariable templateVariable = new TemplateVariable(matcher.group("name"), matcher.group("type"));
            TemplateVariable otherVariable = TemplateHelper.getTemplateVariableByName(templateVariables, matcher.group("name"));
            if(otherVariable != null){
              if(otherVariable.getType() != templateVariable.getType()){
                otherVariable.setError("Multiple usage of variable " + matcher.group("name") + " with different types.");
              }
              continue;
            }
            templateVariables.add(templateVariable);
          }
          
          if(!templateVariables.isEmpty()){
            itemplate.setTemplateVariables(templateVariables);
            return itemplate;
          }
        }
        else{
          System.err.println("ITemplate ("+ itemplate.getClass() +") has no regex for detecting templateVariables -> returning null;");
        }
      }
      else{
        System.err.println("Source is null -> returning null;");
      }
    }
    else{
      System.err.println("ITemplate is null -> returning null;");
    }
    return null;
  }
  
  /**
   * Requests and returns all implementations of {@link ITemplate} from eclipse plugin registry 
   * using the extension point {@code de.tu_dortmund.javatemplates.template}.
   * 
   * @return a list of all implementations of {@link ITemplate}
   */
  private static ArrayList<ITemplate> getAllImplementations(){
    IExtensionRegistry reg = Platform.getExtensionRegistry();
    IExtensionPoint ep = reg.getExtensionPoint("de.tu_dortmund.javatemplates.template");
    IExtension[] extensions = ep.getExtensions();
    ArrayList<ITemplate> contributors = new ArrayList<>();
    for (int i = 0; i < extensions.length; i++) {
       IExtension ext = extensions[i];
       IConfigurationElement[] ce = ext.getConfigurationElements();
       for (int j = 0; j < ce.length; j++) {
          Object obj = null;
          try {
            obj = ce[j].createExecutableExtension("class");
          } catch (CoreException e) {
            e.printStackTrace();
          }
          contributors.add((ITemplate) obj);
       }
    }
    return contributors;
  }
}
