package de.tu_dortmund.javatemplates.types;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ArrayType;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.SimpleType;

import de.tu_dortmund.javatemplates.template.TemplateVariable;
import de.tu_dortmund.javatemplates.types.value.Array;
import de.tu_dortmund.javatemplates.types.value.Boolean;
import de.tu_dortmund.javatemplates.types.value.Byte;
import de.tu_dortmund.javatemplates.types.value.Char;
import de.tu_dortmund.javatemplates.types.value.Double;
import de.tu_dortmund.javatemplates.types.value.Float;
import de.tu_dortmund.javatemplates.types.value.Int;
import de.tu_dortmund.javatemplates.types.value.Long;
import de.tu_dortmund.javatemplates.types.value.NonArray;
import de.tu_dortmund.javatemplates.types.value.Short;
import de.tu_dortmund.javatemplates.types.value.Simple;

/**
 * This class is used to determine the exact type of a variable definition. <br>
 * Its main purpose is to get the exact type of a {@link TemplateVariable} whose {@link TemplateVariable.TYPE} is VALUE.
 * 
 * @author Timo Etzold
 *
 */
public class TypeFinder extends ASTVisitor {
  private Type type = null;
  
  /**
   * Returns the type found by this visitor.
   * 
   * @return the {@link Type} found by the visitor or {@code null} if none could be found.
   */
  public Type getType() {
    return type;
  }
  
  /**
   * Resets the {@link Type} found by the visitor, so the visitor can be used again.
   */
  public void reset(){
    type = null;
  }

  /**
   * Visits the ArrayType node of an AST and creates an instance of {@link Array}.
   */
  public boolean visit(ArrayType node){
    type = new Array(matchType(node.getElementType()), node.getDimensions());
    return false;
  }

  /**
   * Visits the PrimitiveType node of an AST and creates an instance of {@link PrimitiveType} by calling {@link #matchType(org.eclipse.jdt.core.dom.Type) matchType}.
   */
  public boolean visit(PrimitiveType node){
    type = matchType(node);
    return false;
  }

  /**
   * Visits the SimpleType node of an AST and creates an instance of {@link SimpleType} by calling {@link #matchType(org.eclipse.jdt.core.dom.Type) matchType}.
   */
  public boolean visit(SimpleType node){
    type = matchType(node);
    return false;
  }
  
  /**
   * Matches the given {@link org.eclipse.jdt.core.dom.Type} to a type of the class hierarchy of {@link Type}.
   * 
   * @param type the type to be matched.
   * @return an instance of the matched type (a subclass of {@link Type})
   */
  private NonArray matchType(org.eclipse.jdt.core.dom.Type type){
    if(type instanceof org.eclipse.jdt.core.dom.SimpleType){
      return new Simple(type.getClass());
    }
    else if(type instanceof org.eclipse.jdt.core.dom.PrimitiveType){
      org.eclipse.jdt.core.dom.PrimitiveType.Code code = ((org.eclipse.jdt.core.dom.PrimitiveType) type).getPrimitiveTypeCode();
      switch(code.toString()){
        case "boolean":
          return new Boolean();
        case "byte":
          return new Byte();
        case "char":
          return new Char();      
        case "double":
          return new Double();
        case "float":
          return new Float(); 
        case "int":
          return new Int();
        case "long":
          return new Long();
        case "short":
          return new Short();
      }
    }
    return null;
  }
}
