package de.tu_dortmund.javatemplates.types.value;

import de.tu_dortmund.javatemplates.template.TemplateVariable;
import de.tu_dortmund.javatemplates.types.ITypeVisitor;

/**
 * This class represents the exact type of a {@link TemplateVariable} with the {@link TemplateVariable.TYPE} VALUE 
 * used for the definition of any non-primitive, non-array variable.
 * 
 * @author Timo Etzold
 *
 */
public class Simple extends NonArray {
  
  private Class<?> clazz;
  
  /**
   * Creates a Simple representing the given class.
   * 
   * @param clazz the class which this Simple represents
   */
  public Simple(Class<?> clazz){
    this.setClazz(clazz);
  }

  /**
   * Returns the class represented by this Simple.
   * 
   * @return the class represented by this Simple
   */
  public Class<?> getClazz() {
    return clazz;
  }

  /**
   * Sets the class represented by this Simple.
   * 
   * @param clazz the class represented by this Simple
   */
  public void setClazz(Class<?> clazz) {
    this.clazz = clazz;
  }

  /* (non-Javadoc)
   * @see de.tu_dortmund.javatemplates.types.Type#accept(de.tu_dortmund.javatemplates.types.ITypeVisitor)
   */
  @Override
  public String accept(ITypeVisitor v) {
    return v.visit(this);
  }
}
