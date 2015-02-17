package hu.bme.mit.inf.mdvalidator.core;

import org.eclipse.incquery.runtime.api.IPatternMatch;
import org.eclipse.incquery.runtime.api.IQuerySpecification;
import org.eclipse.incquery.runtime.api.IncQueryMatcher;
/**
 * Class that represents a validation constraint, a record in the QuerySpecificationRegistry
 * @author Lunk Péter
 *
 */
public class Record{
	private IQuerySpecification<? extends IncQueryMatcher<? extends IPatternMatch>> spec = null;
	public IQuerySpecification<? extends IncQueryMatcher<? extends IPatternMatch>> getSpec() {
		return spec;
	}
	public void setSpec(
			IQuerySpecification<? extends IncQueryMatcher<? extends IPatternMatch>> spec) {
		this.spec = spec;
	}
	public String getFile() {
		return file;
	}
	public void setFile(String file) {
		this.file = file;
	}

	private String file = null;
	
	
	public Record(IQuerySpecification<? extends IncQueryMatcher<? extends IPatternMatch>> spec, String path){
		this.spec = spec;
		this.file = path;
		
	}
	
};
