package gov.hhs.fha.nhinc.webserviceproxy;

import gov.hhs.fha.nhinc.properties.IPropertyAcessor;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.api.Action;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Ignore;
import org.jmock.lib.legacy.ClassImposteriser;

@Ignore
public abstract class AbstractWebServiceProxyHelpTest {
	
	protected Mockery context = new JUnit4Mockery() {{
		setImposteriser(ClassImposteriser.INSTANCE);
	}};
	
	final protected IPropertyAcessor mockPropertyAccessor = context
			.mock(IPropertyAcessor.class);
	
	
	protected void initializationExpectations() throws PropertyAccessException {
		retryDelayExpectation(mockPropertyAccessor, Expectations.returnValue("1"));

		retryAttemptsExpectation(mockPropertyAccessor, Expectations.returnValue("5"));
		
		timeoutExpectation(mockPropertyAccessor, Expectations.returnValue("300"));
			
		
		exceptionExpectation(mockPropertyAccessor, Expectations.returnValue("PropertyAccessException"));
	}
	
	
	protected void exceptionExpectation(IPropertyAcessor mock,
			Action action) throws PropertyAccessException {
		propertyExpectation(mock, action, WebServiceProxyHelperProperties.CONFIG_KEY_EXCEPTION);
	}

	protected void timeoutExpectation(IPropertyAcessor mockPropertyAccessor,
			Action action) throws PropertyAccessException {
		propertyExpectation(mockPropertyAccessor, action, WebServiceProxyHelperProperties.CONFIG_KEY_TIMEOUT);
		
	}

	protected void retryAttemptsExpectation(final IPropertyAcessor mock,
			final Action action) throws PropertyAccessException {
		propertyExpectation(mock, action, WebServiceProxyHelperProperties.CONFIG_KEY_RETRYATTEMPTS);
	}

	protected void retryDelayExpectation(final IPropertyAcessor mock,
			final Action action) throws PropertyAccessException {
		propertyExpectation(mock, action,
				WebServiceProxyHelperProperties.CONFIG_KEY_RETRYDELAY);
	}

	protected void propertyExpectation(final IPropertyAcessor mock,
			final Action action, final String property)
			throws PropertyAccessException {
		context.checking(new Expectations() {

			{
				oneOf(mock).getProperty(property);
				will(action);
			}
		});
	}

	
}
