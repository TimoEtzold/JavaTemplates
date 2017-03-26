package de.tu_dortmund.javatemplates.plugin;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;

public class NewTemplateWizardPage extends WizardNewFileCreationPage {
  
  boolean isClass = true;
  
  private IStructuredSelection selection;

  /**
   * Creates the NewTemplateWizardPage with the given {@link IStructuredSelection}.
   * 
   * @param selection
   */
  public NewTemplateWizardPage(IStructuredSelection selection) {
    super("New Java Template Code", selection);
    setSelection(selection);
    setTitle("New Java Template Code");
    setDescription("Create a new Java Template Code file.");
    setFileExtension("javat");
  }

  /**
   * Returns the {@link IStructuredSelection}.
   * 
   * @return the {@link IStructuredSelection}
   */
  private IStructuredSelection getSelection() {
    return selection;
  }

  /**
   * Sets the {@link IStructuredSelection}.
   * 
   * @param selection the {@link IStructuredSelection}
   */
  private void setSelection(IStructuredSelection selection) {
    this.selection = selection;
  }

  /* (non-Javadoc)
   * @see org.eclipse.ui.dialogs.WizardNewFileCreationPage#createControl(org.eclipse.swt.widgets.Composite)
   */
  @SuppressWarnings("deprecation")
  @Override
  public void createControl(Composite parent) {
    Composite topLevel = new Composite(parent, SWT.NONE);
    topLevel.setLayout(new GridLayout());
    topLevel.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_FILL
        | GridData.HORIZONTAL_ALIGN_FILL));
    
    Composite container = new Composite(topLevel, SWT.NONE);
    container.setLayout(new GridLayout(2, false));
    container.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    
    Label lType = new Label(container, SWT.NONE);
    lType.setText("Kind:");
    
    Composite subContainer = new Composite(container, SWT.NONE);
    subContainer.setLayout(new RowLayout());
    
    Button[] rType = new Button[2];
    rType[0] = new Button(subContainer, SWT.RADIO);
    rType[0].setSelection(true);
    rType[0].setText("Class Template");
    rType[0].addSelectionListener(new SelectionListener() {
      
      @Override
      public void widgetSelected(SelectionEvent e) {
        isClass = true;
      }
      
      @Override
      public void widgetDefaultSelected(SelectionEvent e) {
      }
    });
    rType[1] = new Button(subContainer, SWT.RADIO);
    rType[1].setText("Interface Template");
    rType[1].addSelectionListener(new SelectionListener() {
      
      @Override
      public void widgetSelected(SelectionEvent e) {
        isClass = false;
      }
      
      @Override
      public void widgetDefaultSelected(SelectionEvent e) {
      }
    });
    
    ResourcesPlugin.getPlugin().getPluginPreferences().setValue(ResourcesPlugin.PREF_DISABLE_LINKING, true);
    
    super.createControl(topLevel);
    
    setControl(topLevel);
  }
  
  /** 
   * Returns the initial contents of the file which is to be created by this wizard. <br>
   * The package of the new file is determined by the {@link IStructuredSelection} <br>
   * and the kind of the java element by selection of the radio buttons on the page.
   * 
   * @return
   */
  @Override
  protected InputStream getInitialContents() {
    String pkg = "";
    Object firstElement = getSelection().getFirstElement();
    if(firstElement instanceof IJavaElement){
      IJavaElement jelem = (IJavaElement) firstElement;
      try{
        IResource res = jelem.getCorrespondingResource();
        if(res != null){
          IPath path = res.getFullPath();
          IPackageFragment frag = jelem.getJavaProject().findPackageFragment(path);
          if(frag == null){
            frag = jelem.getJavaProject().findPackageFragment(path.removeLastSegments(1));
          }
          if(frag != null){
            pkg = frag.getElementName();
          }
        }
      }
      catch(JavaModelException e){
        e.printStackTrace();
      }
    }
    String initialContents = (pkg != "" ? ("package " + pkg + ";\n\n") : "") + "public " + (isClass ? "class" : "interface") + " ${Template:NAME} {\n\n}";
    return new ByteArrayInputStream(initialContents.getBytes());
  }
}