package hu.bme.mit.inf.mdvalidator.core;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.incquery.runtime.api.AdvancedIncQueryEngine;
import org.eclipse.incquery.runtime.api.IPatternMatch;
import org.eclipse.incquery.runtime.api.IQuerySpecification;
import org.eclipse.incquery.runtime.api.IncQueryMatcher;
import org.eclipse.incquery.runtime.exception.IncQueryException;
/**
 * Class responsible for conducting the pattern matching
 * @author Lunk Péter
 *
 */
public class Matcher {
	public static Collection<? extends IPatternMatch> getMatches(
			IQuerySpecification<? extends IncQueryMatcher<? extends IPatternMatch>> iQuerySpecification,
			Resource model) {
		Collection<? extends IPatternMatch> matches = new ArrayList<>();
		try {

			AdvancedIncQueryEngine engine = AdvancedIncQueryEngine.createUnmanagedEngine(model);
			IncQueryMatcher<? extends IPatternMatch> matcher = engine
					.getMatcher(iQuerySpecification);

			if (matcher != null) {
				matches = matcher.getAllMatches();

			}
			
			engine.wipe();
		} catch (IncQueryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return matches;

	}
}
