package hu.bme.mit.inf.validator.gui.views;

import java.net.URL;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
/**
 * labelprovider class that acts as the label provider of the MD Query Specification Treeviewer
 * @author Lunk Péter
 *
 */
public class SpecificationLabelProvider extends LabelProvider {

	private static final Image Pattern = getImage("show_categories.gif");

	@Override
	public String getText(Object element) {
		if (element instanceof SpecificationItem) {
			return ((SpecificationItem) element).getName();
		}
		return null;
	}

	/**
	 * @param element
	 * @return
	 */
	@Override
	public Image getImage(Object element) {
		if (element instanceof SpecificationItem) {
			return Pattern;
		}
		return null;
	}

	/**
	 * @param file
	 * @return
	 */
	private static Image getImage(String file) {
		Bundle bundle = FrameworkUtil
				.getBundle(SpecificationLabelProvider.class);
		URL url = FileLocator.find(bundle, new Path("icons/" + file), null);
		ImageDescriptor image = ImageDescriptor.createFromURL(url);
		return image.createImage();

	}
}
