/**
 * 
 */
package gov.hhs.fha.nhinc.jaxws;

import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.cxf.extraction.SAML2AssertionExtractor;

import javax.xml.ws.WebServiceContext;

/**
 * @author mweaver
 *
 */
public class WebServiceHeaderExtractor {
    
    public WebServiceHeaderExtractor() {
        
    }

    public AssertionType extractAssertionFromContext(WebServiceContext context) {
        AssertionType assertion = null;
                    
        SAML2AssertionExtractor extractor = new SAML2AssertionExtractor();
        assertion = extractor.extractSamlAssertion(context);

        // Extract the message id value from the WS-Addressing Header and place it in the Assertion Class
        if (assertion != null) {
            assertion.setMessageId(AsyncMessageIdExtractor.GetAsyncMessageId(context));
        }

        return assertion;
    }
}
