/**
 * 
 */
package gov.hhs.fha.nhinc.callback.openSAML;

import com.sun.xml.wss.impl.callback.SAMLCallback;

/**
 * @author bhumphrey
 *
 */
public class SAMLAssertionBuilderFactoryImpl implements SAMLAssertionBuilderFactory {
	
	
	@Override
	public SAMLAssertionBuilder getBuilder(final String confirmationMethod, CallbackProperties properties) {
		SAMLAssertionBuilder builder = null;
		if ( confirmationMethod.equals(SAMLCallback.HOK_ASSERTION_TYPE) ) {
			builder = new HOKSAMLAssertionBuilder(properties);
		} else if (confirmationMethod.equals(SAMLCallback.SV_ASSERTION_TYPE)) {
			   builder = new SVSAMLAssertionBuilder(properties);
		}
		return builder;
	}
	
	

}
