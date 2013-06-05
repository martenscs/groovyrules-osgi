/**
 * 
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.rules.ConfigurationException;
import javax.rules.InvalidRuleSessionException;
import javax.rules.ObjectFilter;
import javax.rules.RuleExecutionSetMetadata;
import javax.rules.RuleExecutionSetNotFoundException;
import javax.rules.RuleRuntime;
import javax.rules.RuleServiceProvider;
import javax.rules.RuleServiceProviderManager;
import javax.rules.RuleSessionCreateException;
import javax.rules.RuleSessionTypeUnsupportedException;
import javax.rules.StatelessRuleSession;
import javax.rules.admin.LocalRuleExecutionSetProvider;
import javax.rules.admin.RuleAdministrator;
import javax.rules.admin.RuleExecutionSet;
import javax.rules.admin.RuleExecutionSetCreateException;
import javax.rules.admin.RuleExecutionSetRegisterException;

import org.groovyrules.core.RuleServiceProviderImpl;
import org.osgi.framework.Bundle;

/**
 * @author cmartens
 * 
 */
public class GroovyStatelessRuleSession implements StatelessRuleSession {

	private String rulesFile;
	private StatelessRuleSession statelessRuleSession;
	private Bundle bundle;

	/**
	 * 
	 */
	public GroovyStatelessRuleSession(String fileName, Bundle bundle) {
		this.rulesFile = fileName;
		this.bundle = bundle;
	}

	@SuppressWarnings("unchecked")
	public void init() {
		// Load the rule service provider of the implementation.
		// Loading this class will automatically register this
		// provider with the provider manager.
		try {
			Class.forName("org.groovyrules.core.RuleServiceProviderImpl");

			// Get the rule service provider from the provider manager.
			RuleServiceProvider serviceProvider = RuleServiceProviderManager
					.getRuleServiceProvider("org.groovyrules.core.RuleServiceProviderImpl");

			// Get the RuleAdministrator
			RuleAdministrator ruleAdministrator = serviceProvider
					.getRuleAdministrator();

			// assertNotNull(ruleAdministrator);

			// Get an input stream to a test XML ruleset.
			// This rule execution set was modified from the TCK.
			ClassLoader cl = Thread.currentThread().getContextClassLoader();
			InputStream url = bundle.getEntry(rulesFile).openStream();

			// InputStream inStream = new FileInputStream(url);

			Map mappedClassloader = new HashMap();
			mappedClassloader.put(
					RuleServiceProviderImpl.RULE_IMPL_CLASSLOADER, Thread
							.currentThread().getContextClassLoader());
			mappedClassloader.put(RuleServiceProviderImpl.RULE_IMPL_BUNDLE,
					bundle);
			// Parse the ruleset from the config file
			LocalRuleExecutionSetProvider provider = ruleAdministrator
					.getLocalRuleExecutionSetProvider(null);

			RuleExecutionSet res1 = provider.createRuleExecutionSet(url,
					mappedClassloader);
			url.close();

			// Register the RuleExecutionSet
			String uri = res1.getName();
			ruleAdministrator.registerRuleExecutionSet(uri, res1, null);

			RuleRuntime ruleRuntime = serviceProvider.getRuleRuntime();
			// assertNotNull(ruleRuntime);

			List<?> registrations = ruleRuntime.getRegistrations();
			// assertTrue("No RuleSets registered", registrations.size()>=1);
			// assertEquals(String.class, registrations.get(0).getClass());

			// Create a StatelessRuleSession
			statelessRuleSession = (StatelessRuleSession) ruleRuntime
					.createRuleSession(uri, new HashMap<Object, Object>(),
							RuleRuntime.STATELESS_SESSION_TYPE);
		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		} catch (ConfigurationException e) {

			e.printStackTrace();
		} catch (RemoteException e) {

			e.printStackTrace();
		} catch (RuleExecutionSetCreateException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		} catch (RuleSessionTypeUnsupportedException e) {

			e.printStackTrace();
		} catch (RuleSessionCreateException e) {

			e.printStackTrace();
		} catch (RuleExecutionSetNotFoundException e) {

			e.printStackTrace();
		} catch (RuleExecutionSetRegisterException e) {

			e.printStackTrace();
		}

	}

	@Override
	public RuleExecutionSetMetadata getRuleExecutionSetMetadata()
			throws InvalidRuleSessionException, RemoteException {

		return statelessRuleSession.getRuleExecutionSetMetadata();
	}

	@Override
	public int getType() throws RemoteException, InvalidRuleSessionException {

		return statelessRuleSession.getType();
	}

	@Override
	public void release() throws RemoteException, InvalidRuleSessionException {
		statelessRuleSession.release();
	}

	@Override
	public List executeRules(List arg0) throws InvalidRuleSessionException,
			RemoteException {

		return statelessRuleSession.executeRules(arg0);
	}

	@Override
	public List executeRules(List arg0, ObjectFilter arg1)
			throws InvalidRuleSessionException, RemoteException {
		return statelessRuleSession.executeRules(arg0, arg1);
	}

	public String getRulesFile() {
		return rulesFile;
	}

	public void setRulesFile(String rulesFile) {
		this.rulesFile = rulesFile;
	}

}
