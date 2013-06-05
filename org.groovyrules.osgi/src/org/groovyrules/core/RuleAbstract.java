package org.groovyrules.core;

import java.util.Map;

import javax.rules.admin.Rule;

/**
 * Interface for the rule over and above the standard JSR rule
 * 
 * @author Steve Jones
 * @since 15-Jan-2006
 * @version $Revision: 1.0 $
 */
public abstract class RuleAbstract<T, E> implements Rule {

  /**
	 * 
	 */
  private static final long serialVersionUID = -1278430818157009911L;

  /**
   * Execute the rule against the data
   * 
   * @param data The data to process with the rule
   */
  public abstract void execute(T data);

  /**
   * Set the properties for the rule in bulk
   * 
   * @param properties Map<E,E>
   */
  public abstract void setProperties(Map<E, E> properties);

}
