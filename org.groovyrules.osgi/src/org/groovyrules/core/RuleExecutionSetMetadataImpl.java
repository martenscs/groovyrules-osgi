package org.groovyrules.core;

import javax.rules.RuleExecutionSetMetadata;

/**
 * Implementation of metadata for a RuleExecutionSet. This is currently very limited.
 * 
 * @author Rob Newsome
 * @version $Revision: 1.0 $
 */
public class RuleExecutionSetMetadataImpl implements RuleExecutionSetMetadata {

  /**
   * 
   */
  private static final long serialVersionUID = 3047382091344835123L;
  private String name;
  private String description;
  private String uri;

  /**
   * Constructor for RuleExecutionSetMetadataImpl.
   * 
   * @param name String
   * @param description String
   * @param uri String
   */
  RuleExecutionSetMetadataImpl(String name, String description, String uri) {
    this.name = name;
    this.description = description;
    this.uri = uri;
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.rules.RuleExecutionSetMetadata#getDescription()
   */
  public String getDescription() {
    return description;
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.rules.RuleExecutionSetMetadata#getName()
   */
  public String getName() {
    return name;
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.rules.RuleExecutionSetMetadata#getUri()
   */
  public String getUri() {
    return uri;
  }

}
