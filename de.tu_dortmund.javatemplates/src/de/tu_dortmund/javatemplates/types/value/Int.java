package de.tu_dortmund.javatemplates.types.value;

import de.tu_dortmund.javatemplates.template.TemplateVariable;
import de.tu_dortmund.javatemplates.types.ITypeVisitor;

/**
 * This class represents the exact type of a {@link TemplateVariable} with the {@link TemplateVariable.TYPE} VALUE 
 * used for the definition of an int.
 * 
 * @author Timo Etzold
 *
 */
public class Int extends Primitive {

  /* (non-Javadoc)
   * @see de.tu_dortmund.javatemplates.types.Type#accept(de.tu_dortmund.javatemplates.types.ITypeVisitor)
   */
  @Override
  public String accept(ITypeVisitor v) {
    return v.visit(this);
  }
}
