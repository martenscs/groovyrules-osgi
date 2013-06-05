package org.groovyrules.filerules;

import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * Loads configuration for a <tt>RuleExecutionSet</tt>, from an XML configuration file. Each rule in
 * the set is defined in an individual <tt>.groovyrule</tt> file.
 * <p>
 * This uses a simple <tt>DocumentBuilder</tt> to parse the XML configuration.
 * 
 * @author Rob Newsome
 * @version $Revision: 1.0 $
 */
public class XMLRuleExecutionSetConfiguration {

  private String name;

  private String description;

  private String ruleRoot;

  // List of Strings
  private List<String> ruleFiles;

  /**
   * Constructor for XMLRuleExecutionSetConfiguration.
   * 
   * @param inStream InputStream
   */
  public XMLRuleExecutionSetConfiguration(InputStream inStream) {

    try {
 
      Document configurationDoc = null;
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      DocumentBuilder db = dbf.newDocumentBuilder();
      configurationDoc = db.parse(inStream);
      inStream.close();

      // This is the <ruleexecutionset> doc
      Element documentElement = configurationDoc.getDocumentElement();

      parseConfiguration(documentElement);

    } catch (Exception e) {
      throw new RuntimeException(
          "RuleExecutionSetConfiguration unable to parse input stream to XML document", e);
    }

  }

  /**
   * Constructor for XMLRuleExecutionSetConfiguration.
   * 
   * @param reader Reader
   */
  public XMLRuleExecutionSetConfiguration(Reader reader) {

    try {

      Document configurationDoc = null;

      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      DocumentBuilder db = dbf.newDocumentBuilder();
      configurationDoc = db.parse(new InputSource(reader));
      reader.close();

      // This is the <ruleexecutionset> doc
      Element documentElement = configurationDoc.getDocumentElement();

      parseConfiguration(documentElement);

    } catch (Exception e) {
      throw new RuntimeException(
          "RuleExecutionSetConfiguration unable to parse reader to XML document", e);
    }

  }


  /**
   * Constructor for XMLRuleExecutionSetConfiguration.
   * 
   * @param documentElement Element
   */
  public XMLRuleExecutionSetConfiguration(Element documentElement) {

    parseConfiguration(documentElement);

  }

  /**
   * Method getName.
   * 
   * @return String
   */
  public String getName() {
    return name;
  }

  /**
   * Method getDescription.
   * 
   * @return String
   */
  public String getDescription() {
    return description;
  }

  /**
   * Method getRuleRoot.
   * 
   * @return String
   */
  public String getRuleRoot() {
    return ruleRoot;
  }

  /**
   * Method getRuleFiles.
   * 
   * @return List<String>
   */
  public List<String> getRuleFiles() {
    return ruleFiles;
  }

  /**
   * Method parseConfiguration.
   * 
   * @param documentElement Element
   */
  private final void parseConfiguration(Element documentElement) {

    try {

      // Create a list for the rules
      ruleFiles = new ArrayList<String>();

      NodeList confNodeList = documentElement.getChildNodes();

      for (int i = 0; i < confNodeList.getLength(); i++) {

        Node childNode = confNodeList.item(i);
        if (childNode != null) {
          String nodeName = childNode.getNodeName();

          if (nodeName.equals("name")) {
            name = childNode.getFirstChild().getNodeValue();
          } else if (nodeName.equals("description")) {
            description = childNode.getFirstChild().getNodeValue();
          } else if (nodeName.equals("ruleroot")) {
            ruleRoot = childNode.getFirstChild().getNodeValue();
          } else if (nodeName.equals("rules")) {
            // Now get all the rules...
            NodeList ruleNodeList = childNode.getChildNodes();
            for (int j = 0; j < ruleNodeList.getLength(); j++) {
              Node ruleNode = ruleNodeList.item(j);
              if (ruleNode.getNodeName().equals("rule")) {
                String rule = ruleNode.getFirstChild().getNodeValue();
                ruleFiles.add(rule);
              }
            }
          } else {
            // Unknown config node - ignore
          }
        }
      }
      if(ruleRoot==null)
    	  ruleRoot="";
    } catch (Exception ex) {

      throw new RuntimeException(
          "Unable to parse configuration XML as a valid RuleExecutionSet configuration", ex);

    }
  }

}
