package hu.bme.mit.inf.validator.gui;

import hu.bme.mit.inf.validator.gui.views.MDValidationView;

import java.io.File;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
/**
 * Command Handler classresponsible for opening the containing file of the selected patterns
 * @author Lunk Péter
 *
 */
public class OpenFileHandler extends AbstractHandler implements IHandler{

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		String path = "";
		
		IViewPart view = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage()
				.findView("hu.bme.mit.inf.validator.gui.mdview");
		
		if(view instanceof MDValidationView){
			path = ((MDValidationView) view).getSelected().getFilename();
		}
		
		File fileToOpen = new File(path);

		if (fileToOpen.exists() && fileToOpen.isFile()) {
			IFileStore fileStore = EFS.getLocalFileSystem()
					.getStore(fileToOpen.toURI());
			IWorkbenchPage page = PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getActivePage();

			try {
				IDE.openEditorOnFileStore(page, fileStore);
			} catch (PartInitException e) {
				// Put your exception handler here if you wish to
			}
		} else {
			// Do something if the file does not exist
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
				((MDValidationView) view).getSelected().getFilename();
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
