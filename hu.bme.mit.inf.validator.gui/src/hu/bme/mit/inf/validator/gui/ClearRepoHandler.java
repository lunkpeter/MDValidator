package hu.bme.mit.inf.validator.gui;
import hu.bme.mit.inf.mdvalidator.core.MDQuerySpecificationRegistry;
import hu.bme.mit.inf.validator.gui.views.MDValidationView;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;


/**
 * handler class responsible for clearing the pattern repository
 * @author Lunk Péter
 *
 */
public class ClearRepoHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		MDQuerySpecificationRegistry.wipeRepository();
		
		IViewPart view = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage()
				.findView("hu.bme.mit.inf.validator.gui.mdview");
		
		if(view instanceof MDValidationView){
			((MDValidationView) view).refreshTree();
		}
		
		return null;
	}

	

}
