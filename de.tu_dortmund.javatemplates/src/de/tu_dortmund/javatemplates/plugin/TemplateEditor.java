package de.tu_dortmund.javatemplates.plugin;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Message;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.ui.text.JavaSourceViewerConfiguration;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.ide.ResourceUtil;

import de.tu_dortmund.javatemplates.template.ITemplate;
import de.tu_dortmund.javatemplates.template.ITemplateHelper;
import de.tu_dortmund.javatemplates.template.TemplateFactory;
import de.tu_dortmund.javatemplates.template.TemplateHelper;
import de.tu_dortmund.javatemplates.template.TemplateUtils;
import de.tu_dortmund.javatemplates.template.TemplateVariable;

@SuppressWarnings("restriction")
public class TemplateEditor extends TextEditor {
  final String templateInfoMarkerID = "de.tu_dortmund.javatemplates.templateinfo";
  final String templateErrorMarkerID = "de.tu_dortmund.javatemplates.templateerror";
  
  protected void initializeEditor() {
    super.initializeEditor();
    setSourceViewerConfiguration(new JavaSourceViewerConfiguration(JavaPlugin.getDefault().getJavaTextTools().getColorManager(),
        JavaPlugin.getDefault().getCombinedPreferenceStore(),null,null));
  }
  
  @Override
  protected void editorSaved() {
    super.editorSaved();
    
    removeMarkers();
    
    ITemplateHelper templateHelper = TemplateUtils.getInstance().getTemplateHelper();
    CompilationUnit cu = parseContent(templateHelper.fixErrors(getContent()));
    
    if(cu != null){     
      addMessagesToResource(cu.getMessages());
      addProblemsToResource(cu.getProblems());
      
      adjustMarkerPositions();
    }
    
    addTemplateMarkers();
  }
  
  protected CompilationUnit parseContent(String codeToCompile){
    Map<String, String> options = JavaCore.getOptions();
    JavaCore.setComplianceOptions(JavaCore.VERSION_1_8, options);
    
    ASTParser parser = ASTParser.newParser(AST.JLS8);
    parser.setSource(codeToCompile.toCharArray());
    parser.setCompilerOptions(options);   
    parser.setKind(ASTParser.K_COMPILATION_UNIT);
 
    return (CompilationUnit) parser.createAST(null);
  }
  
  protected String getContent(){
    IDocument doc = getDocumentProvider().getDocument(getEditorInput());
    return doc.get();
  }
  
  protected IResource getResource(){
    return ResourceUtil.getResource(getEditorInput());
  }
  
  private void addMessagesToResource(Message[] messages){
    for(Message message : messages){
      try {
        IMarker m = getResource().createMarker(IMarker.MESSAGE);
        m.setAttribute(IMarker.CHAR_START, message.getStartPosition());
        m.setAttribute(IMarker.CHAR_END, message.getStartPosition() + message.getLength());
        m.setAttribute(IMarker.MESSAGE, message.getMessage());
        m.setAttribute(IMarker.PRIORITY, IMarker.PRIORITY_HIGH);
        m.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
      } catch (CoreException e) {
        e.printStackTrace();
      }
    }
  }
  
  private void addProblemsToResource(IProblem[] problems){
    for(IProblem problem : problems){
      try {
        IMarker m = getResource().createMarker(IMarker.PROBLEM);
        m.setAttribute(IMarker.CHAR_START, problem.getSourceStart());
        m.setAttribute(IMarker.CHAR_END, problem.getSourceStart());
        m.setAttribute(IMarker.MESSAGE, problem.getMessage());
        m.setAttribute(IMarker.PRIORITY, IMarker.PRIORITY_HIGH);
        m.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
      } catch (CoreException e) {
        e.printStackTrace();
      }
    }
  }
  
  private void adjustMarkerPositions(){
    IResource res = getResource();
    try {
      IMarker[] problems = res.findMarkers(IMarker.PROBLEM, true, IResource.DEPTH_ZERO);
      
      for(IMarker problem : problems){
        if(problem.exists()){
          if(isMessageCreatedByReplacement((String) problem.getAttribute(IMarker.MESSAGE))){
            problem.delete();
          }
        }
      }
      
      String content = getContent();
      ITemplate template = TemplateFactory.createTemplateForSource(content);
      if(template != null){
        Matcher matcher = Pattern.compile(template.getRegex()).matcher(template.getCode());
        
        while(matcher.find()){
          int iLengthDiff = (matcher.end() - matcher.start()) - TemplateUtils.getInstance().getTemplateHelper().getReplacementForTemplateVariable(template, TemplateHelper.getTemplateVariableByName(template.getTemplateVariables(), matcher.group("name"))).length();
          for(IMarker problem : problems){
            if(problem.exists()){
              Integer iProblemStart = (Integer) problem.getAttribute(IMarker.CHAR_START);
              Integer iProblemEnd = (Integer) problem.getAttribute(IMarker.CHAR_END);
              if(iProblemStart != null && iProblemEnd != null && (iProblemStart + iLengthDiff) > matcher.end()){
                problem.setAttribute(IMarker.CHAR_START, iProblemStart + iLengthDiff);
                problem.setAttribute(IMarker.CHAR_END, iProblemEnd + iLengthDiff);
              }
            }
          }
        }
      }
    } catch (CoreException e) {
      System.out.println("Could not adjust Markerpositions: " + e.getMessage());
      e.printStackTrace();
    }
  }
  
  private void addTemplateMarkers(){
    IResource resource = getResource();
    String content = getContent();
    
    try {
      if(content != null){
        ITemplate template = TemplateFactory.createTemplateForSource(content);
        if(template != null){
          Matcher matcher = Pattern.compile(template.getRegex()).matcher(template.getCode());
          
          while(matcher.find()){
            IMarker marker = resource.createMarker(templateInfoMarkerID);
            marker.setAttribute(IMarker.MESSAGE, "Usage of Template.");
            marker.setAttribute(IMarker.CHAR_START, matcher.start());
            marker.setAttribute(IMarker.CHAR_END, matcher.end());
            
            TemplateVariable var = TemplateHelper.getTemplateVariableByName(template.getTemplateVariables(), matcher.group("name"));
            
            if(var != null){
              if(var.getError() != null){
                IMarker m = resource.createMarker(templateErrorMarkerID);
                m.setAttribute(IMarker.CHAR_START, matcher.start());
                m.setAttribute(IMarker.CHAR_END, matcher.end());
                m.setAttribute(IMarker.MESSAGE, var.getError());
                m.setAttribute(IMarker.PRIORITY, IMarker.PRIORITY_HIGH);
                m.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
              }
            }
          }
        }
      }
    } catch (CoreException e) {
      System.out.println("Could not create Marker: " + e.getMessage());
      e.printStackTrace();
    }
  }
  
  private void removeMarkers(){
    IResource res = getResource();
    try{
      IMarker[] markers = res.findMarkers(templateInfoMarkerID, false, IResource.DEPTH_INFINITE);
      for(IMarker marker : markers){
        if(marker.exists()) marker.delete();
      }
      markers = res.findMarkers(templateErrorMarkerID, false, IResource.DEPTH_INFINITE);
      for(IMarker marker : markers){
        if(marker.exists()) marker.delete();
      }
      markers = res.findMarkers(IMarker.PROBLEM, false, IResource.DEPTH_INFINITE);
      for(IMarker marker : markers){
        if(marker.exists()) marker.delete();
      }
      markers = res.findMarkers(IMarker.MESSAGE, false, IResource.DEPTH_INFINITE);
      for(IMarker marker : markers){
        if(marker.exists()) marker.delete();
      }
    } catch(CoreException e) {
      System.out.println("Error deleting a marker: " + e.getMessage());
      e.printStackTrace();
    }
  }
  
  private boolean isMessageCreatedByReplacement(String message){
    if(message == null) return false;
    return message.contains("TemplateStubClass") || message.contains("This method has a constructor name");
  }
}