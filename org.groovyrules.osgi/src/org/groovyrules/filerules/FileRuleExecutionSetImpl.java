package org.groovyrules.filerules;

import groovy.util.GroovyScriptEngine;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.rules.admin.RuleExecutionSet;

import org.groovyrules.core.RuleExecutionSetAbstract;
import org.groovyrules.core.RuleServiceProviderImpl;
import org.osgi.framework.Bundle;

/**
 * Implementation of the <tt>RuleExecutionSet</tt>; this contains a list of
 * <tt>Rule</tt> instances, which share a <tt>GroovyScriptEngine</tt> to run
 * within.
 * 
 * @author Rob Newsome
 * @version $Revision: 1.0 $
 */
public class FileRuleExecutionSetImpl extends RuleExecutionSetAbstract
		implements RuleExecutionSet {

	/**
   * 
   */
	private static final long serialVersionUID = -7020914667807463392L;
	private String description;
	private String name;

	// TODO: Support properties properly - various scopes for set, rule, etc
	@SuppressWarnings("rawtypes")
	private Map properties;

	/**
	 * Constructor for FileRuleExecutionSetImpl.
	 * 
	 * @param config
	 *            XMLRuleExecutionSetConfiguration
	 * @param creationProperties
	 *            Map
	 * @throws IOException
	 */
	public FileRuleExecutionSetImpl(XMLRuleExecutionSetConfiguration config,
			@SuppressWarnings("rawtypes") Map creationProperties)
			throws IOException {

		this.name = config.getName();
		this.description = config.getDescription();

		if (creationProperties != null) {
			this.properties = creationProperties;
		} else {
			this.properties = new HashMap<Object, Object>();
		}
		URL url = null;
		if (creationProperties
				.containsKey(RuleServiceProviderImpl.RULE_IMPL_CLASSLOADER)) {
			ClassLoader cls = (ClassLoader) creationProperties
					.get(RuleServiceProviderImpl.RULE_IMPL_CLASSLOADER);
			url = cls.getResource(config.getRuleRoot());
		}
		if (creationProperties
				.containsKey(RuleServiceProviderImpl.RULE_IMPL_BUNDLE)
				&& url == null) {
			Bundle bundle = (Bundle) creationProperties
					.get(RuleServiceProviderImpl.RULE_IMPL_BUNDLE);
			url = bundle.getResource(config.getRuleRoot());
		}
		String[] roots = new String[] { url.toString() };
		GroovyScriptEngine scriptEngine = new GroovyScriptEngine(roots);

		for (int i = 0; i < config.getRuleFiles().size(); i++) {

			FileRuleImpl rule = new FileRuleImpl(scriptEngine, (String) config
					.getRuleFiles().get(i));
			rules.add(rule);

		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.rules.admin.RuleExecutionSet#getDescription()
	 */
	public String getDescription() {
		return description;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.rules.admin.RuleExecutionSet#getName()
	 */
	public String getName() {
		return name;
	}

	// /* (non-Javadoc)
	// * @see javax.rules.admin.RuleExecutionSet#getDefaultObjectFilter()
	// */
	// public String getDefaultObjectFilter() {
	// return this.defaultFilterClass;
	// }
	//
	// /* (non-Javadoc)
	// * @see
	// javax.rules.admin.RuleExecutionSet#setDefaultObjectFilter(java.lang.String)
	// */
	// public void setDefaultObjectFilter(String filterClass) {
	// this.defaultFilterClass = filterClass;
	// }

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.rules.admin.RuleExecutionSet#getProperty(java.lang.Object)
	 */
	/**
	 * Method getProperty.
	 * 
	 * @param key
	 *            Object
	 * @return Object
	 * @see javax.rules.admin.RuleExecutionSet#getProperty(Object)
	 */
	public Object getProperty(Object key) {
		return this.properties.get(key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.rules.admin.RuleExecutionSet#setProperty(java.lang.Object,
	 * java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	public void setProperty(Object key, Object value) {
		this.properties.put(key, value);
	}

}
