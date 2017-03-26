package de.tu_dortmund.javatemplates.plugin;

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jdt.ui.PreferenceConstants;
import org.osgi.framework.BundleContext;
import org.osgi.service.prefs.Preferences;

public class Plugin extends org.eclipse.core.runtime.Plugin {
  boolean savedValue = true;
  boolean createTestProject = true;
  
  /** 
   * Disables the reconciling of the editor.
   * 
   * @see org.eclipse.core.runtime.Plugin#start(org.osgi.framework.BundleContext)
   */
  @Override
  public void start(BundleContext context) throws Exception {
    super.start(context);
    
    Preferences prefs = Platform.getPreferencesService().getRootNode().node(InstanceScope.SCOPE).node("org.eclipse.jdt.ui");
    savedValue = prefs.getBoolean(PreferenceConstants.EDITOR_EVALUTE_TEMPORARY_PROBLEMS, true);
    prefs.putBoolean(PreferenceConstants.EDITOR_EVALUTE_TEMPORARY_PROBLEMS, false);
  }

  /** 
   * Restores the reconciling of the editor.
   * 
   * @see org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
   */
  @Override
  public void stop(BundleContext context) throws Exception {
    super.stop(context);
    
    Preferences prefs = Platform.getPreferencesService().getRootNode().node(InstanceScope.SCOPE).node("org.eclipse.jdt.ui");
    prefs.putBoolean(PreferenceConstants.EDITOR_EVALUTE_TEMPORARY_PROBLEMS, savedValue);
  }
}
