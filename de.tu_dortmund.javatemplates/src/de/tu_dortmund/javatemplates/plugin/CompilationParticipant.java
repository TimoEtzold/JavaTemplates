package de.tu_dortmund.javatemplates.plugin;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;

import de.tu_dortmund.javatemplates.template.ITemplate;
import de.tu_dortmund.javatemplates.template.ITemplateHelper;
import de.tu_dortmund.javatemplates.template.TemplateFactory;
import de.tu_dortmund.javatemplates.template.TemplateHelper;
import de.tu_dortmund.javatemplates.template.TemplateUtils;
import de.tu_dortmund.javatemplates.template.TemplateVariable;

/**
 * This class is used to hook into the build mechanism of eclipse.<br>
 * Java Code Templates will be determined and the code will be replaced so that the project can compile successfully, <br>
 * if no other errors persist.
 * 
 * @author Timo Etzold
 *
 */
public class CompilationParticipant extends org.eclipse.jdt.core.compiler.CompilationParticipant {
  final String templateInfoMarkerID = "de.tu_dortmund.javatemplates.templateinfo";
  final String templateErrorMarkerID = "de.tu_dortmund.javatemplates.templateerror";
  Map<IFile,String> filesToBackup = new HashMap<>();

  public CompilationParticipant() {
  }
  
  /** 
   * Finds Templates in the project, replaces usages of template variables and saves the original file contents.
   * 
   * @see org.eclipse.jdt.core.compiler.CompilationParticipant#aboutToBuild(org.eclipse.jdt.core.IJavaProject)
   */
  public int aboutToBuild(IJavaProject project) {
    removeTemplateMarker(project);
    List<IFile> files = findTemplateFiles(project);
    ITemplateHelper templateHelper = TemplateUtils.getInstance().getTemplateHelper();
    for(IFile file : files){
      try {
        String content = inputStreamToString(file.getContents());
        String newContent = templateHelper.fixErrors(content);
        if(content != newContent){ 
          filesToBackup.put(file, content);
          file.setContents(new ByteArrayInputStream(newContent.getBytes()), true, true, null);
        }
      } catch (CoreException e) {
        System.out.println("Error reading the File: " + e.getMessage());
        e.printStackTrace();
      }
    }
    return READY_FOR_BUILD;
  }
  
  /**
   * Removes template markers from the project.
   * 
   * @param project the project whose template markers should be deleted
   */
  private void removeTemplateMarker(IJavaProject project){
    try{
      IMarker[] markers = project.getProject().findMarkers(templateInfoMarkerID, false, IResource.DEPTH_INFINITE);
      for(IMarker marker : markers){
        if(marker.exists()) marker.delete();
      }
      markers = project.getProject().findMarkers(templateErrorMarkerID, false, IResource.DEPTH_INFINITE);
      for(IMarker marker : markers){
        if(marker.exists()) marker.delete();
      }
    } catch(CoreException e) {
      System.out.println("Error deleting a marker: " + e.getMessage());
      e.printStackTrace();
    }
  }
  
  /**
   * Converts an {@link InputStream} to a String.
   * 
   * @param is the {@link InputStream} to convert
   * @return the {@link InputStream} as String
   */
  private String inputStreamToString(InputStream is){
    String content = "";
    try {
      int iReadByte;
      char cReadChar;
      while((iReadByte = is.read()) != -1){
        cReadChar = (char) iReadByte;
        content = content + cReadChar;
      }
    } catch (IOException e) {
      System.out.println("Failure converting InputStream to String: " + e.getMessage());
      e.printStackTrace();
    }
    return content;
  }
  
  /**
   * Gathers all files which contain java template code in a given {@link IContainer}.
   * 
   * @param container the {@link IContainer} in which java template code files are searched
   * @return a list of all files containing java template code
   */
  private List<IFile> findTemplateFiles(IContainer container){
    List<IFile> files = new ArrayList<>();
    try {
      IResource[] members = container.members();

      for (IResource member : members){
        if (member instanceof IContainer){
          files.addAll(findTemplateFiles((IContainer) member));
        }
        else if (member instanceof IFile){
          if(member.getName().endsWith(".javat")){ 
            files.add((IFile) member);
          }
        }
      }
    } catch (CoreException e) {
      System.out.println("Failure processing Container " + container.getName());
      e.printStackTrace();
    }
    return files;
  }
  
  /**
   * Gathers all source-files which contain java template code in a given {@link IJavaProject}.
   * 
   * @param container the {@link IJavaProject} in which java template code files are searched
   * @return a list of all files containing java template code
   */
  private List<IFile> findTemplateFiles(IJavaProject project){
    List<IFile> files = new ArrayList<>();
	  try {
      IPackageFragmentRoot[] roots = project.getAllPackageFragmentRoots();
      for(IPackageFragmentRoot root : roots){
        if(root.getKind() == IPackageFragmentRoot.K_SOURCE){
          for(IJavaElement javaElement : root.getChildren()){
            if (javaElement.getElementType() == IJavaElement.PACKAGE_FRAGMENT) {
              IPackageFragment fragment = (IPackageFragment) javaElement;
              if(fragment.containsJavaResources()){
                ICompilationUnit[] units = fragment.getCompilationUnits();
                for(ICompilationUnit unit : units){
                  IResource resource = unit.getUnderlyingResource();
                  if("javat".equals(resource.getFileExtension())){
                    if(resource instanceof IFile){
                      files.add((IFile) resource);
                    }
                  }
                }
              }
            }
          }
        }
      }
    } catch (JavaModelException e) {
		System.err.println("An error occured while getting all java template code files.");
		e.printStackTrace();
	}
    return files;
  }
  
  /** 
   * Restores the original content of the changed files, adjusts all markers (since they may be at a wrong position) and adds template markers.
   * 
   * @see org.eclipse.jdt.core.compiler.CompilationParticipant#buildFinished(org.eclipse.jdt.core.IJavaProject)
   */
  public void buildFinished(IJavaProject project) {
    Set<IFile> processedFiles = new HashSet<>();
    for(Map.Entry<IFile, String> entry : filesToBackup.entrySet()){
      try {
        entry.getKey().setContents(new ByteArrayInputStream(entry.getValue().getBytes()), true, true, null);
        adjustMarkerPositions(entry.getKey());
        addTemplateMarkersToResource(entry.getKey());
        processedFiles.add(entry.getKey());
      } catch (CoreException e) {
        System.out.println("Error restoring the File: " + e.getMessage());
        e.printStackTrace();
      }
    }
    for(IFile processedFile : processedFiles){
      filesToBackup.remove(processedFile);
    }
  }
  
  /**
   * Adds template markers to the resource.
   * 
   * @param resource the resource to which template markers should be added
   */
  private void addTemplateMarkersToResource(IResource resource){
    String content = null;
    try {
      content = inputStreamToString(((IFile) resource).getContents());
    } catch (CoreException e) {
      System.out.println("Could not get contents of the given resource: " + e.getMessage());
      e.printStackTrace();
    }
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
  
  /**
   * Adjusts the position of markers, which may be wrong because the content of the file changed after they were added.
   * 
   * @param file the file whose markers should be adjusted
   */
  private void adjustMarkerPositions(IFile file){
    try {
      IMarker[] problems = file.findMarkers(IMarker.PROBLEM, true, IResource.DEPTH_ZERO);
      
      String content = inputStreamToString(file.getContents());
      
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
        
        for(IMarker problem : problems){
          if(problem.exists()){
            if(isCreatedByReplacement(template, problem)){
              problem.delete();
            }
          }
        }
      }
    } catch (CoreException e) {
      System.out.println("Could not adjust Markerpositions: " + e.getMessage());
      e.printStackTrace();
    }
  }
  
  /**
   * Checks if the error message is created by the replacement String.
   * 
   * @param message the String to be checked.
   * @return {@code true} if it was created by the replacement, else {@code false}
   * @throws CoreException 
   */
  private boolean isCreatedByReplacement(ITemplate template, IMarker problem) throws CoreException{
    if(problem == null || template == null) return false;
    
    Matcher matcher = Pattern.compile(template.getRegex()).matcher(template.getCode());
    Integer iProblemStart = (Integer) problem.getAttribute(IMarker.CHAR_START);
    while(matcher.find()){
      if(matcher.start() == iProblemStart)
        return true;
    }
    return false;
  }
  
  /** 
   * This CompilationParticipant should be active for every project, so this method returns {@code project.isOpen()}
   * 
   * @see org.eclipse.jdt.core.compiler.CompilationParticipant#isActive(org.eclipse.jdt.core.IJavaProject)
   */
  public boolean isActive(IJavaProject project) {
    return project.isOpen();
  }
}
