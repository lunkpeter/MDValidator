package hu.bme.mit.inf.validator.gui.views;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
/**
 * class that represents a QuerySpecification object in the UI
 * @author Lunk Péter
 *
 */
public class SpecificationItem {
	private String name;
	private String filename;
	private String annot;
	private String message;
	private String severity;

	
	private PropertyChangeSupport changeSupport = new PropertyChangeSupport(
			this);

	public void addPropertyChangeListener(String propertyName,
			PropertyChangeListener listener) {
		changeSupport.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(String propertyName,
			PropertyChangeListener listener) {
		changeSupport.removePropertyChangeListener(listener);
	}
	
	public SpecificationItem() {
		super();
	}
	
	public SpecificationItem(String name, String filename, String annot, String message, String severity) {
		this.filename = filename;
		this.annot = annot;
		this.name = name;
		this.message = message;
		this.severity = severity;
	}

	public void setFileName(String filename) {
		String oldfileName = this.filename;
		this.filename = filename;
		changeSupport.firePropertyChange("filename", oldfileName, filename);
	}

	public void setAnnot(String annot) {
		String oldAnnot = this.annot;
		this.annot = annot;
		changeSupport.firePropertyChange("annot", oldAnnot, annot);
	}

	public void setName(String  name) {
		String oldname = this.name;
		this.name = name;
		changeSupport.firePropertyChange("name", oldname, name);
	}

	public void setChangeSupport(PropertyChangeSupport changeSupport) {
		this.changeSupport = changeSupport;
	}

	public String getFilename() {
		return filename;
	}
	public String getAnnot() {
		return annot;
	}
	public String getName() {
		return name;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		String oldmessage = this.message;
		this.message = message;
		changeSupport.firePropertyChange("message", oldmessage, message);
	}

	public String getSeverity() {
		return severity;
	}

	public void setSeverity(String severity) {
		String oldseverity = this.severity;
		this.severity = severity;
		changeSupport.firePropertyChange("message", oldseverity, severity);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((filename == null) ? 0 : filename.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((annot == null) ? 0 : annot.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SpecificationItem other = (SpecificationItem) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
