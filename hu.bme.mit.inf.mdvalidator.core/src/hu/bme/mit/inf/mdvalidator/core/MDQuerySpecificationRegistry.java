package hu.bme.mit.inf.mdvalidator.core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.incquery.runtime.api.IPatternMatch;
import org.eclipse.incquery.runtime.api.IQuerySpecification;
import org.eclipse.incquery.runtime.api.IncQueryMatcher;
import org.eclipse.incquery.runtime.util.IncQueryLoggingUtil;
/**
 * Specificyation registry that contains the MagicDraw validation constraints
 * @author Lunk Péter
 *
 */
public final class MDQuerySpecificationRegistry {

	private static final Map<String, Record> QUERY_SPECIFICATIONS = createQuerySpecificationRegistry();

    /**
     * Utility class constructor hidden
     */
    private MDQuerySpecificationRegistry() {
    }

    private static Map<String, Record> createQuerySpecificationRegistry() {
        final Map<String, Record> specifications = new HashMap<String, Record>();
        return specifications;
    }


    /**
     * Puts the specification in the registry, unless it already contains a specification for the given pattern FQN
     * 
     * @param specification
     */
    public static void registerQuerySpecification(
            IQuerySpecification<? extends IncQueryMatcher<? extends IPatternMatch>> specification, String path) {
        
    	String qualifiedName = specification.getFullyQualifiedName();
        
        
        if (!QUERY_SPECIFICATIONS.containsKey(qualifiedName)) {
        	
        	final Record r = new Record(specification, path);
            QUERY_SPECIFICATIONS.put(qualifiedName, r);
        } else {
            IncQueryLoggingUtil
                    .getLogger(MDQuerySpecificationRegistry.class)
                    .warn(String
                            .format("[QuerySpecificationRegistry] Trying to register duplicate FQN (%s). Check your plug-in configuration!",
                                    qualifiedName));
        }
    }

    /**
     * Removes the query specification from the registry which belongs to the given fully qualified pattern name.
     * 
     * @param patternFQN
     *            the fully qualified name of the pattern
     */
    public static void unregisterQuerySpecification(String patternFQN) {
        QUERY_SPECIFICATIONS.remove(patternFQN);
    }

    /**
     * @return a copy of the set of contributed query specifications
     */
    public static Set<Record> getContributedQuerySpecifications() {
        return new HashSet<Record>(QUERY_SPECIFICATIONS.values());
    }

    /**
     * @param patternFQN
     *            the fully qualified name of a registered generated pattern
     * @return the generated query specification of the pattern with the given fully qualified name, if it is
     *         registered, or null if there is no such generated pattern
     */
    public static Record getQuerySpecification(
            String patternFQN) {
        if (QUERY_SPECIFICATIONS.containsKey(patternFQN)) {
            return QUERY_SPECIFICATIONS.get(patternFQN);
        }
        
        
        return null;
    }
    
    /**
     * Wipes the repository
     * 
     */
    public static void wipeRepository() {
    	Set<Record> records = getContributedQuerySpecifications();
    	for (Record record : records) {
			unregisterQuerySpecification(record.getSpec().getFullyQualifiedName());
		}
    }
    
    /**
     * Removes specifications originating from a given file
     * 
     */
    public static void removeSpecificationsFromPath(String path) {
    	Set<Record> records = getContributedQuerySpecifications();
    	for (Record record : records) {
    		if(record.getFile().equals(path)){
    			unregisterQuerySpecification(record.getSpec().getFullyQualifiedName());
    		}
		}
    }
}
