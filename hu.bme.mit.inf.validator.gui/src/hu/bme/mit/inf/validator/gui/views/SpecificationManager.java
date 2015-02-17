package hu.bme.mit.inf.validator.gui.views;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/**
 * class that contains the available Specification Items
 * @author Lunk Péter
 *
 */
public class SpecificationManager {
	private List<SpecificationItem> specs = new ArrayList<SpecificationItem>();
	
	
	public void addSpecificationItem(SpecificationItem item) {
		specs.add(item);
	}
	
	public void removeSpecificationItem(SpecificationItem item) {
		specs.remove(item);
	}
	
	public List<SpecificationItem> getAllSpecificationItems() {
		return Collections.unmodifiableList(specs);
	}
	
	public void clrAllSpecificationItems() {
		specs.clear();
	}
}
