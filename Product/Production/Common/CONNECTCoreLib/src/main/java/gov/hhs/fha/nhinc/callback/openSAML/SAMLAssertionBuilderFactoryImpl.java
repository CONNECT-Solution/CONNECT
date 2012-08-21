/**
 *
 */
package gov.hhs.fha.nhinc.callback.openSAML;

/**
 * @author bhumphrey
 *
 */
public class SAMLAssertionBuilderFactoryImpl implements SAMLAssertionBuilderFactory {

	/* (non-Javadoc)
	 * @see gov.hhs.fha.nhinc.callback.openSAML.SAMLAssertionBuilderFactory#getBuilder(java.lang.String)
	 */
	@Override
	public SAMLAssertionBuilder getBuilder(final String confirmationMethod) {
		SAMLAssertionBuilder builder = null;
		if ( confirmationMethod.equals(HOK_ASSERTION_TYPE)) {
			builder = new HOKSAMLAssertionBuilder();
		} else if (confirmationMethod.equals(SV_ASSERTION_TYPE)) {
			   builder = new SVSAMLAssertionBuilder();
		}
		return builder;
	}



}
