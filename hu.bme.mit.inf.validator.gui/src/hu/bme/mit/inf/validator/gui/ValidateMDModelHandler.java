package hu.bme.mit.inf.validator.gui;


import hu.bme.mit.inf.mdvalidator.core.MDQuerySpecificationRegistry;
import hu.bme.mit.inf.mdvalidator.core.Matcher;
import hu.bme.mit.inf.mdvalidator.core.Record;
import hu.bme.mit.inf.mdvalidator.magicdrawmodelprovider.MagicDrawModelProvider;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;



import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.incquery.runtime.api.IPatternMatch;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import com.nomagic.magicdraw.uml.BaseElement;
/**
 * Command handler class responsible for validating the active MD model
 * @author Lunk Péter
 *
 */
public class ValidateMDModelHandler extends AbstractHandler {


	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		try {
			//PatternLoader.loadPatterns(path.toString());
			RegisterPatternsJob job = new RegisterPatternsJob(
					"MD Validation");
			job.schedule();
		} catch (Exception e) {
			// TODO: handle exception
		}

		return null;
	}

	private void syncWithUi(final Shell shell) {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				MessageDialog.openInformation(shell, "Message",
						"Validation Completed! ");

			}
		});

	}

	class RegisterPatternsJob extends Job {
		final Shell shell = Display.getDefault().getActiveShell();
		

		public RegisterPatternsJob(String name) {
			super(name);
		}

		@Override
		protected IStatus run(IProgressMonitor monitor) {

			

			try {
				Set<Record> spec = MDQuerySpecificationRegistry.getContributedQuerySpecifications();
				monitor.beginTask("MD Validation",spec.size()+1);
				
				Resource model = MagicDrawModelProvider.getInstance().GetModel();
				MagicDrawModelProvider.getInstance().removeAllAnnot();
				monitor.worked(1);
				
				//Set<IQuerySpecification<? extends IncQueryMatcher<? extends IPatternMatch>>> specifications = QuerySpecificationRegistry.getContributedQuerySpecifications();
				
				
				for (Record record : spec) {
					String text = "";
					String severityLiteral = "";
					
					
					
					List<BaseElement> elems = new ArrayList<>();
					text = (String) record.getSpec().getAllAnnotations().get(0).getAllValues("message").get(0);
					severityLiteral = (String) record.getSpec().getAllAnnotations().get(0).getAllValues("severity").get(0);
					//System.out.println(severityLiteral);
					
					
					//System.out.println(record.getSpec().getFullyQualifiedName()+" -- "+record.getFile());
					Collection<? extends IPatternMatch> matches = Matcher.getMatches(record.getSpec(), model);
					for (IPatternMatch iPatternMatch : matches) {
						elems.add((BaseElement) iPatternMatch.get(0));	
					}
					MagicDrawModelProvider.getInstance().addAnnotation(elems, text, severityLiteral);
					monitor.worked(1);

				}

			} catch (Exception e) {
				e.printStackTrace();
				return Status.CANCEL_STATUS;
			}
			

			syncWithUi(shell);
			return Status.OK_STATUS;
		}
	}
	
	
	

}
