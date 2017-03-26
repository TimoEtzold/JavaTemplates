package de.tu_dortmund.javatemplates.types;

/**
 * Abstract superclass of the class hierarchy.
 * 
 * @author Timo Etzold
 *
 */
public abstract class Type {
  
  /**
   * This methods accepts the {@link ITypeVisitor} and calls {@code v.visit(this)} to determine the replacement String.
   * 
   * @param v the visitor used to determine the replacement String
   * @return the replacement String
   */
  public abstract String accept(ITypeVisitor v);
  
}
