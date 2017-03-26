package de.tu_dortmund.javatemplates.types;

import de.tu_dortmund.javatemplates.types.other.Args;
import de.tu_dortmund.javatemplates.types.other.ArgsDef;
import de.tu_dortmund.javatemplates.types.other.Expr;
import de.tu_dortmund.javatemplates.types.other.Name;
import de.tu_dortmund.javatemplates.types.other.ReturnType;
import de.tu_dortmund.javatemplates.types.value.Array;
import de.tu_dortmund.javatemplates.types.value.Boolean;
import de.tu_dortmund.javatemplates.types.value.Byte;
import de.tu_dortmund.javatemplates.types.value.Char;
import de.tu_dortmund.javatemplates.types.value.Double;
import de.tu_dortmund.javatemplates.types.value.Float;
import de.tu_dortmund.javatemplates.types.value.Int;
import de.tu_dortmund.javatemplates.types.value.Long;
import de.tu_dortmund.javatemplates.types.value.Short;
import de.tu_dortmund.javatemplates.types.value.Simple;

/**
 * Default implementation of {@link ITypeVisitor}
 * 
 * @author Timo Etzold
 *
 */
public class TypeVisitor implements ITypeVisitor{

  /* (non-Javadoc)
   * @see de.tu_dortmund.javatemplates.types.ITypeVisitor#visit(de.tu_dortmund.javatemplates.types.value.Boolean)
   */
  @Override
  public String visit(Boolean type) {
    return "true";
  }

  /* (non-Javadoc)
   * @see de.tu_dortmund.javatemplates.types.ITypeVisitor#visit(de.tu_dortmund.javatemplates.types.value.Byte)
   */
  @Override
  public String visit(Byte type) {
    return "0";
  }

  /* (non-Javadoc)
   * @see de.tu_dortmund.javatemplates.types.ITypeVisitor#visit(de.tu_dortmund.javatemplates.types.value.Char)
   */
  @Override
  public String visit(Char type) {
    return "'c'";
  }

  /* (non-Javadoc)
   * @see de.tu_dortmund.javatemplates.types.ITypeVisitor#visit(de.tu_dortmund.javatemplates.types.value.Double)
   */
  @Override
  public String visit(Double type) {
    return "0";
  }

  /* (non-Javadoc)
   * @see de.tu_dortmund.javatemplates.types.ITypeVisitor#visit(de.tu_dortmund.javatemplates.types.value.Float)
   */
  @Override
  public String visit(Float type) {
    return "0";
  }

  /* (non-Javadoc)
   * @see de.tu_dortmund.javatemplates.types.ITypeVisitor#visit(de.tu_dortmund.javatemplates.types.value.Int)
   */
  @Override
  public String visit(Int type) {
    return "0";
  }

  /* (non-Javadoc)
   * @see de.tu_dortmund.javatemplates.types.ITypeVisitor#visit(de.tu_dortmund.javatemplates.types.value.Long)
   */
  @Override
  public String visit(Long type) {
    return "0";
  }

  /* (non-Javadoc)
   * @see de.tu_dortmund.javatemplates.types.ITypeVisitor#visit(de.tu_dortmund.javatemplates.types.value.Short)
   */
  @Override
  public String visit(Short type) {
    return "0";
  }

  /* (non-Javadoc)
   * @see de.tu_dortmund.javatemplates.types.ITypeVisitor#visit(de.tu_dortmund.javatemplates.types.value.Simple)
   */
  @Override
  public String visit(Simple type) {
    return "null";
  }

  /* (non-Javadoc)
   * @see de.tu_dortmund.javatemplates.types.ITypeVisitor#visit(de.tu_dortmund.javatemplates.types.value.Array)
   */
  @Override
  public String visit(Array type) {
    return "null";
  }

  /* (non-Javadoc)
   * @see de.tu_dortmund.javatemplates.types.ITypeVisitor#visit(de.tu_dortmund.javatemplates.types.other.Args)
   */
  @Override
  public String visit(Args args) {
    return "";
  }

  /* (non-Javadoc)
   * @see de.tu_dortmund.javatemplates.types.ITypeVisitor#visit(de.tu_dortmund.javatemplates.types.other.ArgsDef)
   */
  @Override
  public String visit(ArgsDef argsDef) {
    return "";
  }

  /* (non-Javadoc)
   * @see de.tu_dortmund.javatemplates.types.ITypeVisitor#visit(de.tu_dortmund.javatemplates.types.other.Expr)
   */
  @Override
  public String visit(Expr expr) {
    return "";
  }
  
  /* (non-Javadoc)
   * @see de.tu_dortmund.javatemplates.types.ITypeVisitor#visit(de.tu_dortmund.javatemplates.types.other.Name)
   */
  @Override
  public String visit(Name name) {
    return "TemplateStubClass";
  }

  /* (non-Javadoc)
   * @see de.tu_dortmund.javatemplates.types.ITypeVisitor#visit(de.tu_dortmund.javatemplates.types.other.ReturnType)
   */
  @Override
  public String visit(ReturnType returnType) {
    return "void";
  }

}
