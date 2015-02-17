package hu.bme.mit.inf.validator.gui;

import hu.bme.mit.inf.mdvalidator.patternloader.PatternLoader;
import hu.bme.mit.inf.validator.gui.views.MDValidationView;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

/**
 * Registers the constraints in the selected Eclipse resource (only active if an .eiq file is selected)
 * @author Lunk Péter
 *
 */
public class RegisterPatternsHandler extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IPath path = null;
		try {
			IWorkbenchWindow window = PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow();
			if (window != null) {
				IStructuredSelection selection = (IStructuredSelection) window
						.getSelectionService().getSelection();
				Object firstElement = selection.getFirstElement();
				if (firstElement instanceof IAdaptable) {
					IFile file = (IFile) ((IAdaptable) firstElement)
							.getAdapter(IFile.class);
					path = file.getLocation();
					
				}
			}
			// PatternLoader.loadPatterns(path.toString());
			RegisterPatternsJob job = new RegisterPatternsJob(
					"MD Validation Pattern Registration", path.toString());
			job.schedule();
		} catch (Exception e) {
			// TODO: handle exception
		}

		return null;
	}

	private void syncWithUi(final Shell shell, final String path) {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				MessageDialog.openInformation(shell, "Message",
						"Validation patterns imported from " + path);
				
			
				IViewPart view = PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getActivePage()
						.findView("hu.bme.mit.inf.validator.gui.mdview");
				
				if(view instanceof MDValidationView){
					((MDValidationView) view).refreshTree();
				}

			}
		});

	}

	class RegisterPatternsJob extends Job {
		final Shell shell = Display.getDefault().getActiveShell();
		String path;

		public RegisterPatternsJob(String name, String path) {
			super(name);
			this.path = path;
		}

		@Override
		protected IStatus run(IProgressMonitor monitor) {

			monitor.beginTask("MD Validation Pattern Registration", 100);

			try {

				PatternLoader.loadPatterns(path);

			} catch (Exception e) {
				e.printStackTrace();
				return Status.CANCEL_STATUS;
			}
			monitor.worked(100);

			syncWithUi(shell, path);
			return Status.OK_STATUS;
		}
	}
	
	@Override
	public boolean isEnabled(){
		IPath path = null;
		IWorkbenchWindow window = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow();
		if (window != null) {
			IStructuredSelection selection = (IStructuredSelection) window
					.getSelectionService().getSelection();
			Object firstElement = selection.getFirstElement();
			if (firstElement instanceof IAdaptable) {
				IFile file = (IFile) ((IAdaptable) firstElement)
						.getAdapter(IFile.class);
				path = file.getLocation();
				
			}
			if(path.toString().contains(".eiq"))
				return true;
		}
		return false;
	}
}
