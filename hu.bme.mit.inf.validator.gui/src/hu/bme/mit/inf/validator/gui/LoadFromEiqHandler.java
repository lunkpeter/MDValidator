package hu.bme.mit.inf.validator.gui;

import hu.bme.mit.inf.mdvalidator.patternloader.PatternLoader;
import hu.bme.mit.inf.validator.gui.views.MDValidationView;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;
/**
 * Handler class responsible for loading patterns from a selected file
 * @author Lunk Péter
 *
 */
public class LoadFromEiqHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		try {
			Display display = Display.getDefault();
			final Shell shell = display.getActiveShell();

			FileDialog filediag = new FileDialog(shell);
			filediag.setText("Select the .EIQ file to be transformed");
			filediag.setFilterExtensions(new String[] { "*.eiq" });
			filediag.setFilterNames(new String[] { "EMF IncQuery pattern definition files(*.eiq)" });
			final String modelpath = filediag.open();
			
			
			// PatternLoader.loadPatterns(path.toString());
			RegisterPatternsJob job = new RegisterPatternsJob(
					"MD Validation Pattern Registration", modelpath);
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

}
