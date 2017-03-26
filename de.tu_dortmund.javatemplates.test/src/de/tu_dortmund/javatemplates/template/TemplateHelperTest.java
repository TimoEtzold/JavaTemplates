package de.tu_dortmund.javatemplates.template;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.junit.Test;

public class TemplateHelperTest {
  
  @Test
  public void testFillTemplate(){
    //test data
    String sTemplate = "class ${DecoratorName:NAME} implements ${DecoratorInterface:NAME} {" + 
        "\n" +
        "  private ${DecoratorInterface:NAME} decoratedInstance;\n" + 
        "\n" + 
        "  public ${DecoratorName:NAME}(${DecoratorInterface:NAME} decoratedInstance) {\n" + 
        "    this.decoratedInstance = decoratedInstance;\n" + 
        "  }\n" + 
        "\n" + 
        "  public ${DecoratedMethodType:RETURNTYPE} ${DecoratedMethodName:NAME}(${DecoratedMethodParameters:ARGSDEF}) {\n" + 
        "    ${AdditionalCodeBefore:EXPR}\n" + 
        "    decoratedInstance.${DecoratedMethodName:NAME}(${ForwardedArguments:ARGS});\n" + 
        "    ${AdditionalCodeAfter:EXPR}\n" + 
        "  }\n" + 
      "}\n";
    
    ITemplate template = TemplateFactory.createSpecificTemplateForSource(new Template(), sTemplate);
    
    Map<String, String> attributes = new HashMap<>();
    attributes.put("DecoratorName","MyDecorator");
    attributes.put("DecoratorInterface","TestInterface");
    attributes.put("DecoratedMethodType", "void");
    attributes.put("DecoratedMethodName", "doNothing");
    attributes.put("DecoratedMethodParameters", "");
    attributes.put("AdditionalCodeBefore", "");
    attributes.put("ForwardedArguments", "");
    attributes.put("AdditionalCodeAfter", "");
    TemplateVariable var = new TemplateVariable(null, null);
    
    //test
    TemplateHelper templateHelper = (TemplateHelper) TemplateUtils.getInstance().getTemplateHelper();
    
    template.addTemplateVariable(var);
    
    try{
      templateHelper.fillTemplate(template, attributes);
      fail("Exception was expected");
    } catch(IllegalArgumentException e){
      assertTrue(e.getMessage().contains("has no name"));
    }
    
    var.setName("DecoratorName");
    
    try{
      templateHelper.fillTemplate(template, attributes);
      fail("Exception was expected");
    } catch(IllegalArgumentException e){
      assertTrue(e.getMessage().contains("untyped"));
    }
    
    attributes.remove("AdditionalCodeAfter");
    template.removeTemplateVariable(var);
    
    try{
      templateHelper.fillTemplate(template, attributes);
      fail("Exception was expected");
    } catch(IllegalArgumentException e){
      assertTrue(e.getMessage().contains("missing"));
    }
    
    attributes.put("AdditionalCodeAfter", "return true");
    
    try{
      templateHelper.fillTemplate(template, attributes);
      fail("Exception was expected");
    } catch(IllegalArgumentException e){
      assertTrue(e.getMessage().contains("error"));
    }
    
    attributes.put("AdditionalCodeAfter", "return true;");
    
    CompilationUnit cu = templateHelper.fillTemplate(template, attributes);
    
    assertNotNull(cu);
    assertTrue(cu.getProblems().length == 0);
    assertTrue(cu.getMessages().length == 0);
  }

  @Test
  public void testGetTypesForAttribute() {
    try{
      //get method under test through reflection (as it is private)
      Method method = TemplateHelper.class.getDeclaredMethod("getTypesForAttribute", String.class);
      method.setAccessible(true);
      
      //test data
      String sARGS1 = "new Object(){}, 3";
      String sARGS2 = "new Object(){ @Override public boolean equals(Object o){ return false;}}, 42";
      String sARGSDEF1 = "String sAttribute";
      String sARGSDEF2 = "Class< ? > clazz";
      String sEXPR1 = "int i = 3;\n\n\n\n";
      String sEXPR2 = "if(b){\nint i = 3;\n}";
      String sEXPR3 = "class A {public void doNothing(){}}";
      String sEXPR4 = "return true;\n int i = 3;";
      String sNAME1 = "SomeClass";
      String sNAME2 = "someMethod";
      String sRETURNTYPE1 = "void";
      String sRETURNTYPE2 = "Object[]";
      String sVALUE1 = "3";
      String sVALUE2 = "new Object(){}";

      List<TemplateVariable.TYPE> lARGS = new ArrayList<>(Arrays.asList(TemplateVariable.TYPE.ARGS));
      List<TemplateVariable.TYPE> lARGSDEF = new ArrayList<>(Arrays.asList(TemplateVariable.TYPE.ARGSDEF)); 
      List<TemplateVariable.TYPE> lEXPR = new ArrayList<>(Arrays.asList(TemplateVariable.TYPE.EXPR));
      List<TemplateVariable.TYPE> lNAME = new ArrayList<>(Arrays.asList(TemplateVariable.TYPE.ARGS,TemplateVariable.TYPE.NAME,TemplateVariable.TYPE.RETURNTYPE,TemplateVariable.TYPE.VALUE));
      List<TemplateVariable.TYPE> lRETURNTYPE = new ArrayList<>(Arrays.asList(TemplateVariable.TYPE.RETURNTYPE));
      List<TemplateVariable.TYPE> lVALUE = new ArrayList<>(Arrays.asList(TemplateVariable.TYPE.ARGS,TemplateVariable.TYPE.VALUE));
      
      TemplateHelper templateHelper = (TemplateHelper) TemplateUtils.getInstance().getTemplateHelper();
      
      //test
      assertTrue(method.invoke(templateHelper, sARGS1).equals(lARGS));
      assertTrue(method.invoke(templateHelper, sARGS2).equals(lARGS));
      assertTrue(method.invoke(templateHelper, sARGSDEF1).equals(lARGSDEF));
      assertTrue(method.invoke(templateHelper, sARGSDEF2).equals(lARGSDEF));
      assertTrue(method.invoke(templateHelper, sEXPR1).equals(lEXPR));
      assertTrue(method.invoke(templateHelper, sEXPR2).equals(lEXPR));
      assertTrue(method.invoke(templateHelper, sEXPR3).equals(lEXPR));
      assertTrue(method.invoke(templateHelper, sEXPR4).equals(lEXPR));
      assertTrue(method.invoke(templateHelper, sNAME1).equals(lNAME));
      assertTrue(method.invoke(templateHelper, sNAME2).equals(lNAME));
      assertTrue(method.invoke(templateHelper, sRETURNTYPE1).equals(lRETURNTYPE));
      assertTrue(method.invoke(templateHelper, sRETURNTYPE2).equals(lRETURNTYPE));
      assertTrue(method.invoke(templateHelper, sVALUE1).equals(lVALUE));
      assertTrue(method.invoke(templateHelper, sVALUE2).equals(lVALUE));
    }
    catch(NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e){
      fail("Exception occurred.");
    }
  }
}
