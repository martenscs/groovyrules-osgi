package org.groovyrules.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.rules.ObjectFilter;
import javax.rules.admin.RuleExecutionSet;

/**
 * Abstract class for all the rule execution sets. This mainly handles running
 * all the rules, which is common for all implementations, as all rules extend
 * from the {@link org.groovyrules.core.RuleAbstract} class.
 * 
 * @author Steve Jones
 * @since 15-Jan-2006
 * @version $Revision: 1.0 $
 */
public abstract class RuleExecutionSetAbstract implements RuleExecutionSet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -70952254766759028L;
	// The default filter class is null if not set
	String defaultFilterClass = null;
	// The list of rules
	protected final List<RuleAbstract<RuleData, Object>> rules = new ArrayList<RuleAbstract<RuleData, Object>>();
	// The properties, requires J2SE 5.0 if use generics
	// Map properties = new HashMap<Integer, String>();
	protected Map<Object, Object> properties = new HashMap<Object, Object>();
	/**
	 * Returns an instantiated default object filter, or null if none is
	 * specified.
	 * 
	 * 
	 * @return ObjectFilter
	 * @throws RuntimeException
	 *             If can't instantiate the filter.
	 */
	public ObjectFilter getDefaultObjectFilterInstance() {
		if (defaultFilterClass == null) {
			return null;
		} else {
			try {
				ObjectFilter defaultFilter = (ObjectFilter) Class.forName(
						defaultFilterClass).newInstance();
				return defaultFilter;
			} catch (Exception e) {
				if (e instanceof ClassNotFoundException) {
					ClassLoader hostClassLoader = (ClassLoader) getProperty(RuleServiceProviderImpl.RULE_IMPL_CLASSLOADER);
					try {
						
						return (ObjectFilter) hostClassLoader.loadClass(
								defaultFilterClass).newInstance();
					} catch (Exception e2) {
						throw new RuntimeException(
								"Unable to construct default object filter instance",
								e2);
					}
				}
				throw new RuntimeException(
						"Unable to construct default object filter instance", e);
			}
		}
	}

	/**
	 * Runs all rules in this execution set.
	 * 
	 * @param data
	 *            RuleData
	 */
	protected void runRules(RuleData data) {

		Iterator<RuleAbstract<RuleData, Object>> rules = getRules().iterator();

		while (rules.hasNext()) {
			RuleAbstract<RuleData, Object> rule = (RuleAbstract<RuleData, Object>) rules
					.next();
			rule.setProperties(this.properties);
			rule.execute(data);
		}

	}

	/**
	 * @see javax.rules.admin.RuleExecutionSet#getRules()
	 */
	public List<RuleAbstract<RuleData, Object>> getRules() {
		return rules;
	}

	/**
	 * @see javax.rules.admin.RuleExecutionSet#setDefaultObjectFilter(java.lang.String)
	 */
	public void setDefaultObjectFilter(String filterClass) {
		this.defaultFilterClass = filterClass;
	}

	/**
	 * @see javax.rules.admin.RuleExecutionSet#getDefaultObjectFilter()
	 */
	public String getDefaultObjectFilter() {
		return this.defaultFilterClass;
	}

	/**
	 * Properties are used to map from the list position to a given name, if
	 * there are no properties then the only mapping is the input list to "data"
	 * 
	 * @see javax.rules.admin.RuleExecutionSet#getProperty(java.lang.Object)
	 */
	public Object getProperty(Object arg0) {
		return this.properties.get(arg0);
	}

	/**
	 * @see javax.rules.admin.RuleExecutionSet#setProperty(java.lang.Object,
	 *      java.lang.Object)
	 */
	public void setProperty(Object arg0, Object arg1) {
		this.properties.put(arg0, arg1);
	}

}
