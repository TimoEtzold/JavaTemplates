package de.tu_dortmund.javatemplates.types.value;

import de.tu_dortmund.javatemplates.template.TemplateVariable;
import de.tu_dortmund.javatemplates.types.ITypeVisitor;
import de.tu_dortmund.javatemplates.types.Type;

/**
 * This class represents the exact type of a {@link TemplateVariable} with the {@link TemplateVariable.TYPE} VALUE used for the definition of an array.
 * 
 * @author Timo Etzold
 *
 */
public class Array extends Value {
  
  private NonArray elementType;
  
  private int dimension;
  
  /**
   * Creates an instance of this class with a given element type and a dimension.
   * 
   * @param elementType the element type of the array
   * @param dimension the dimension of the array
   */
  public Array(NonArray elementType, int dimension){
    this.setElementType(elementType);
    this.setDimension(dimension);
  }

  /* (non-Javadoc)
   * @see de.tu_dortmund.javatemplates.types.Type#accept(de.tu_dortmund.javatemplates.types.ITypeVisitor)
   */
  @Override
  public String accept(ITypeVisitor v) {
    return v.visit(this);
  }

  /**
   * Returns the dimension of the array.
   * 
   * @return the dimension of the array
   */
  public int getDimension() {
    return dimension;
  }

  /**
   * Sets the dimension of the array.
   * 
   * @param dimension the dimension of the array
   */
  public void setDimension(int dimension) {
    this.dimension = dimension;
  }

  /**
   * Returns the element type of the array.
   * 
   * @return the element type of the array
   */
  public Type getElementType() {
    return elementType;
  }

  /**
   * Sets the element type of the array.
   * 
   * @param elementType the element type of the array
   */
  public void setElementType(NonArray elementType) {
    this.elementType = elementType;
  }

}
