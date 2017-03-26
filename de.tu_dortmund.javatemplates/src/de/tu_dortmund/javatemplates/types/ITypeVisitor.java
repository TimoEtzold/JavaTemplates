package de.tu_dortmund.javatemplates.types;

import de.tu_dortmund.javatemplates.template.TemplateVariable;
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
 * Interface for a Visitor to determine a replacement String for a {@link TemplateVariable}
 * 
 * @author Timo Etzold
 *
 */
public interface ITypeVisitor {
  /**
   * Returns the replacement String for a {@link TemplateVariable} with the {@link TemplateVariable.TYPE} VALUE and the value type {@link Boolean}.
   * 
   * @param type the type whose replacement String is to be determined
   * @return the replacement String
   */
  String visit(Boolean type);
  
  /**
   * Returns the replacement String for a {@link TemplateVariable} with the {@link TemplateVariable.TYPE} VALUE and the value type {@link Byte}.
   * 
   * @param type the type whose replacement String is to be determined
   * @return the replacement String
   */
  String visit(Byte type);
  
  /**
   * Returns the replacement String for a {@link TemplateVariable} with the {@link TemplateVariable.TYPE} VALUE and the value type {@link Char}.
   * 
   * @param type the type whose replacement String is to be determined
   * @return the replacement String
   */
  String visit(Char type);
  
  /**
   * Returns the replacement String for a {@link TemplateVariable} with the {@link TemplateVariable.TYPE} VALUE and the value type {@link Double}.
   * 
   * @param type the type whose replacement String is to be determined
   * @return the replacement String
   */
  String visit(Double type);
  
  /**
   * Returns the replacement String for a {@link TemplateVariable} with the {@link TemplateVariable.TYPE} VALUE and the value type {@link Float}.
   * 
   * @param type the type whose replacement String is to be determined
   * @return the replacement String
   */
  String visit(Float type);
  
  /**
   * Returns the replacement String for a {@link TemplateVariable} with the {@link TemplateVariable.TYPE} VALUE and the value type {@link Int}.
   * 
   * @param type the type whose replacement String is to be determined
   * @return the replacement String
   */
  String visit(Int type);
  
  /**
   * Returns the replacement String for a {@link TemplateVariable} with the {@link TemplateVariable.TYPE} VALUE and the value type {@link Long}.
   * 
   * @param type the type whose replacement String is to be determined
   * @return the replacement String
   */
  String visit(Long type);
  
  /**
   * Returns the replacement String for a {@link TemplateVariable} with the {@link TemplateVariable.TYPE} VALUE and the value type {@link Short}.
   * 
   * @param type the type whose replacement String is to be determined
   * @return the replacement String
   */
  String visit(Short type);
  
  /**
   * Returns the replacement String for a {@link TemplateVariable} with the {@link TemplateVariable.TYPE} VALUE and the value type {@link Simple}.
   * 
   * @param type the type whose replacement String is to be determined
   * @return the replacement String
   */
  String visit(Simple type);
  
  /**
   * Returns the replacement String for a {@link TemplateVariable} with the {@link TemplateVariable.TYPE} VALUE and the value type {@link Array}.
   * 
   * @param type the type whose replacement String is to be determined
   * @return the replacement String
   */
  String visit(Array type);

  /**
   * Returns the replacement String for a {@link TemplateVariable} with the {@link TemplateVariable.TYPE} ARGS.
   * 
   * @param type the type whose replacement String is to be determined
   * @return the replacement String
   */
  String visit(Args args);

  /**
   * Returns the replacement String for a {@link TemplateVariable} with the {@link TemplateVariable.TYPE} ARGSDEF.
   * 
   * @param type the type whose replacement String is to be determined
   * @return the replacement String
   */
  String visit(ArgsDef argsDef);

  /**
   * Returns the replacement String for a {@link TemplateVariable} with the {@link TemplateVariable.TYPE} EXPR.
   * 
   * @param type the type whose replacement String is to be determined
   * @return the replacement String
   */
  String visit(Expr expr);

  /**
   * Returns the replacement String for a {@link TemplateVariable} with the {@link TemplateVariable.TYPE} NAME.
   * 
   * @param type the type whose replacement String is to be determined
   * @return the replacement String
   */
  String visit(Name name);

  /**
   * Returns the replacement String for a {@link TemplateVariable} with the {@link TemplateVariable.TYPE} RETURNTYPE.
   * 
   * @param type the type whose replacement String is to be determined
   * @return the replacement String
   */
  String visit(ReturnType returnType);
}
