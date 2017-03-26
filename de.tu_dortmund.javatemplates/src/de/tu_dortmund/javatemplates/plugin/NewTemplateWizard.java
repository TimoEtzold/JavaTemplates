package de.tu_dortmund.javatemplates.plugin;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IDE;

public class NewTemplateWizard extends Wizard implements INewWizard {

  private NewTemplateWizardPage wizPage;
  
  private IStructuredSelection selection;
  
  private IWorkbench workbench;

  public NewTemplateWizard(NewTemplateWizardPage page, boolean openEditorOnFinish) {
    wizPage = page;
    setWindowTitle("New Java Template Code");
  }

  public NewTemplateWizard() {
    this(null, true);
  }

  /*
   * @see Wizard#createPages
   */
  @Override
  public void addPages() {
    super.addPages();
    if (wizPage == null) {
      wizPage= new NewTemplateWizardPage(selection);
      wizPage.setWizard(this);
    }
    addPage(wizPage);
  }
  
  /**
   * Creates the new file and opens it with the appropriate editor.
   * 
   * @see org.eclipse.jface.wizard.Wizard#performFinish()
   */
  @Override
  public boolean performFinish() {
      boolean result = false;
   
      IFile file = wizPage.createNewFile();
      result = file != null;
   
      if (result) {
          try {
              IDE.openEditor(workbench.getActiveWorkbenchWindow().getActivePage(), file);
          } catch (PartInitException e) {
              e.printStackTrace();
          }
      }
   
      return result;
  }

  /* (non-Javadoc)
   * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench, org.eclipse.jface.viewers.IStructuredSelection)
   */
  @Override
  public void init(IWorkbench workbench, IStructuredSelection selection) {
    this.workbench = workbench;
    this.selection = selection;
  }

}
