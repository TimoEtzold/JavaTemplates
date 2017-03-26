package de.tu_dortmund.javatemplates.types.other;

import de.tu_dortmund.javatemplates.template.TemplateVariable;
import de.tu_dortmund.javatemplates.types.ITypeVisitor;
import de.tu_dortmund.javatemplates.types.Type;

/**
 * This class represents the exact type of a {@link TemplateVariable} with the {@link TemplateVariable.TYPE} EXPR.
 * 
 * @author Timo Etzold
 *
 */
public class Expr extends Type {

  /* (non-Javadoc)
   * @see de.tu_dortmund.javatemplates.types.Type#accept(de.tu_dortmund.javatemplates.types.ITypeVisitor)
   */
  @Override
  public String accept(ITypeVisitor v) {
    return v.visit(this);
  }

}
