/**
 * 
 */
package gov.hhs.fha.nhinc.callback.openSAML;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.joda.time.DateTime;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opensaml.saml2.core.AuthnStatement;
import org.w3c.dom.Element;

/**
 * @author bhumphrey
 * 
 */
public class HOKSAMLAssertionBuilderTest {

	@BeforeClass
	static public void setUp() {
		// WORKAROUND NEEDED IN METRO1.4. TO BE REMOVED LATER.
		javax.net.ssl.HttpsURLConnection
				.setDefaultHostnameVerifier(new javax.net.ssl.HostnameVerifier() {

					public boolean verify(String hostname,
							javax.net.ssl.SSLSession sslSession) {
						return true;
					}
				});
		
		    Logger rootLogger = Logger.getRootLogger();
		    rootLogger.setLevel(Level.INFO);
		    rootLogger.addAppender(new ConsoleAppender(
		               new PatternLayout("%-6r [%p] %c - %m%n")));
	}

	@Test
	public void testBuild() throws Exception {
		HOKSAMLAssertionBuilder builder = new HOKSAMLAssertionBuilder(
				getProperties());
		Element assertion = builder.build();
		assertNotNull(assertion);
	}
	
	
	@Test
	public void testCreateAuthenicationStatement() {
		List<AuthnStatement> authnStatement = HOKSAMLAssertionBuilder.createAuthenicationStatements(getProperties());
		assertNotNull(authnStatement);
		
		assertFalse(authnStatement.isEmpty());
	}
	

	CallbackProperties getProperties() {
		return new CallbackProperties() {

			@Override
			public String getUsername() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String getUserSystemName() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String getUserSystem() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String getUserOrganization() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String getUserFullName() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String getUserDisplay() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String getUserCode() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String getSubjectLocality() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String getSubjectDNS() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String getPurposeSystemName() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String getPurposeSystem() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String getPurposeDisplay() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String getPurposeCode() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String getPatientID() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String getIssuer() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String getHomeCommunity() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String getEvidenceIssuerFormat() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String getEvidenceIssuer() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public DateTime getEvidenceInstant() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public List getEvidenceInstanctAccessConsent() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String getEvidenceID() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public DateTime getEvidenceConditionNotBefore() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public DateTime getEvidenceConditionNotAfter() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public List getEvidenceAccessConstent() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String getAuthnicationResource() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Boolean getAuthenicationStatementExists() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String getAuthenicationSessionIndex() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public DateTime getAuthenicationInstant() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String getAuthenicationDecision() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String getAuthenicationContextClass() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String getAssertionIssuerFormat() {
				// TODO Auto-generated method stub
				return null;
			}
		};
	}

}
