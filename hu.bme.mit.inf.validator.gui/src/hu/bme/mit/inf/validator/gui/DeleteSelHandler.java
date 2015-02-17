package hu.bme.mit.inf.validator.gui;
import hu.bme.mit.inf.mdvalidator.core.MDQuerySpecificationRegistry;
import hu.bme.mit.inf.validator.gui.views.MDValidationView;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;

/**
 * Handler class responsible for deleting the selected query specifications
 * @author Lunk Péter
 *
 */
public class DeleteSelHandler extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		String FQN = "";
		
		IViewPart view = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage()
				.findView("hu.bme.mit.inf.validator.gui.mdview");
		
		if(view instanceof MDValidationView){
			FQN = ((MDValidationView) view).getSelected().getName();
		}
		
		MDQuerySpecificationRegistry.unregisterQuerySpecification(FQN);
		
	
		
		if(view instanceof MDValidationView){
			((MDValidationView) view).refreshTree();
		}


		return null;
	}
		
	@Override
	public boolean isEnabled(){
		try {
			IViewPart view = PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getActivePage()
					.findView("hu.bme.mit.inf.validator.gui.mdview");
			
			if(view instanceof MDValidationView){
				((MDValidationView) view).getSelected().getName();
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
