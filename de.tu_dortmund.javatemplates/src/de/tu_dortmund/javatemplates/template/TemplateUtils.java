package de.tu_dortmund.javatemplates.template;

/**
 * Singleton to provide access to a globally used implementation of {@link ITemplateHelper}
 * 
 * @author Timo Etzold
 *
 */
public class TemplateUtils {
  private ITemplateHelper templateHelper = null;
  private static TemplateUtils instance = null;
  
  private TemplateUtils(){
    //protect from instantiation
  }
  
  /**
   * Returns the instance of this Singleton.
   * 
   * @return the instance
   */
  public static TemplateUtils getInstance(){
    if(instance == null){
      instance = new TemplateUtils();
      instance.setTemplateHelper(new TemplateHelper());
    }
    return instance;
  }

  /**
   * Sets the globally used implementation of {@link ITemplateHelper}.
   * 
   * @param templateHelper the {@link ITemplateHelper} which is to be used globally
   */
  public void setTemplateHelper(ITemplateHelper templateHelper){
    this.templateHelper = templateHelper;
  }
  
  /**
   * Returns the globally used implementation of {@link ITemplateHelper}.
   * 
   * @return the instance of {@link ITemplateHelper}
   */
  public ITemplateHelper getTemplateHelper(){
    return templateHelper;
  }
}
