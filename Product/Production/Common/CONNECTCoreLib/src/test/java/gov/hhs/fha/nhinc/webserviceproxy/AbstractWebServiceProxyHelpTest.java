package gov.hhs.fha.nhinc.webserviceproxy;

import gov.hhs.fha.nhinc.properties.IPropertyAcessor;

import org.apache.commons.logging.Log;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Ignore;

@Ignore
public abstract class AbstractWebServiceProxyHelpTest {
	Mockery context = new JUnit4Mockery() {

		{
			setImposteriser(ClassImposteriser.INSTANCE);
		}
	};
	final protected Log mockLog = context.mock(Log.class);
	final protected IPropertyAcessor mockPropertyAccessor = context
			.mock(IPropertyAcessor.class);
	final protected WebServiceProxyHelper oHelper = new WebServiceProxyHelper(mockLog,
			mockPropertyAccessor);
}
