package hu.bme.mit.inf.mdvalidator.patternloader;

import hu.bme.mit.inf.mdvalidator.core.MDQuerySpecificationRegistry;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.incquery.patternlanguage.emf.EMFPatternLanguageStandaloneSetup;
import org.eclipse.incquery.patternlanguage.emf.specification.SpecificationBuilder;
import org.eclipse.incquery.patternlanguage.patternLanguage.Annotation;
import org.eclipse.incquery.patternlanguage.patternLanguage.Pattern;
import org.eclipse.incquery.patternlanguage.patternLanguage.PatternModel;
import org.eclipse.incquery.runtime.exception.IncQueryException;
/**
 * Class tzhat loads EMF IncQuery patterns from a selected file
 * @author Lunk Péter
 *
 */
public class PatternLoader {
	/**
	 * loads patterns from the path specified
	 * @param patternpath - path of the .eiq file containing the patterns
	 */
	public static void loadPatterns(String patternpath) {

		List<Pattern> patterns = new ArrayList<>();
		// use a trick to load Pattern models from a file
		ResourceSet resourceSet = new ResourceSetImpl();
		
		MDQuerySpecificationRegistry.removeSpecificationsFromPath(patternpath);
		
		// Initializing Xtext-based resource parser
		// Do not use if EMF-IncQuery tooling is loaded!
		new EMFPatternLanguageStandaloneSetup()
				.createInjectorAndDoEMFRegistration();
		

		// Loading pattern resource from file
		URI fileURI = URI.createFileURI(patternpath);
		Resource patternResource = resourceSet.getResource(fileURI, true);

		// navigate to the pattern definition that we want
		if (patternResource != null) {
			if (patternResource.getErrors().size() == 0
					&& patternResource.getContents().size() >= 1) {
				EObject topElement = patternResource.getContents().get(0);
				if (topElement instanceof PatternModel) {
					for (Pattern _p : ((PatternModel) topElement).getPatterns()) {
						EList<Annotation> annotations = _p.getAnnotations();
						for (Annotation annotation : annotations) {
							if(annotation.getName().equals("Constraint")){
								patterns.add(_p);
							}
						}
					}
				}
			}
		}
		// A specification builder is used to translate patterns to query
		// specifications
		SpecificationBuilder builder = new SpecificationBuilder();

		for (Pattern pattern : patterns) {
			
			try {
				MDQuerySpecificationRegistry.registerQuerySpecification(builder
					.getOrCreateSpecification(pattern), patternpath);
			
				
			} catch (IncQueryException e) {
				e.printStackTrace();
				System.out
						.println("Error when adding queryspecification to repository");
			}
			
		}

	}
}
