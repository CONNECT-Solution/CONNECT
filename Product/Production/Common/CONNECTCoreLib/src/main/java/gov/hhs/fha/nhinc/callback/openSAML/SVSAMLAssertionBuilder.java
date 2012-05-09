/**
 * 
 */
package gov.hhs.fha.nhinc.callback.openSAML;

import org.w3c.dom.Element;

/**
 * @author bhumphrey
 *
 */
public class SVSAMLAssertionBuilder extends SAMLAssertionBuilder {

	/**
	 * @param properties
	 */
	SVSAMLAssertionBuilder(CallbackProperties properties) {
		super(properties);
	}

	/* (non-Javadoc)
	 * @see gov.hhs.fha.nhinc.callback.openSAML.SAMLAssertionBuilder#build()
	 */
	@Override
	public Element build() throws Exception {
	    // log.debug("SamlCallbackHandler.createSVSAMLAssertion20() -- Begin");
	    // Assertion assertion = null;
	    // try {
	    // SAMLAssertionFactory factory = SAMLAssertionFactory.newInstance(SAMLAssertionFactory.SAML2_0);
	    //
	    // // create the assertion id
	    // // Per GATEWAY-847 the id attribute should not be allowed to start
	    // // with a number (UUIDs can). Direction
	    // // given from 2011 specification set was to prepend with and
	    // // underscore.
	    // String aID = ID_PREFIX.concat(String.valueOf(UUID.randomUUID()));
	    // log.debug("Assertion ID: " + aID);
	    //
	    // // name id of the issuer - For now just use default
	    // NameID issueId = create509NameID(factory, DEFAULT_NAME);
	    //
	    // // issue instant
	    // GregorianCalendar issueInstant = calendarFactory();
	    //
	    // // name id of the subject - user name
	    // String uname = "defUser";
	    // if (tokenVals.containsKey(SamlConstants.USER_NAME_PROP)
	    // && tokenVals.get(SamlConstants.USER_NAME_PROP) != null) {
	    // uname = tokenVals.get(SamlConstants.USER_NAME_PROP).toString();
	    // }
	    // NameID nmId = factory.createNameID(uname, null, X509_NAME_ID);
	    // Subject subj = factory.createSubject(nmId, null);
	    //
	    // // authentication statement
	    // List statements = createAuthnStatements(factory);
	    //
	    // assertion = factory.createAssertion(aID, issueId, issueInstant, null, null, subj, statements);
	    //
	    // assertion.setVersion("2.0");
	    //
	    // log.debug("createSVSAMLAssertion20 end ()");
	    // return assertion.toElement(null);
	    // } catch (Exception e) {
	    // e.printStackTrace();
	    // throw new RuntimeException(e);
	    // }
		return null;
	 }

}
