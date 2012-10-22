/**
 * 
 */
package gov.hhs.fha.nhinc.messaging.server;

import java.util.List;

import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.cxf.extraction.SAML2AssertionExtractor;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;

import javax.xml.ws.WebServiceContext;

/**
 * @author bhumphrey
 *
 */
public abstract class BaseService {
    
    protected AssertionType getAssertion(WebServiceContext context, AssertionType oAssertionIn) {
        AssertionType assertion = null;
        if (oAssertionIn == null) {
            assertion = SAML2AssertionExtractor.getInstance().extractSamlAssertion(context);
        } else {
            assertion = oAssertionIn;
        }

      // Extract the message id value from the WS-Addressing Header and place it in the Assertion Class
      if (assertion != null) {
          assertion.setMessageId(AsyncMessageIdExtractor.getOrCreateAsyncMessageId(context));
      }
        
        
      // Extract the relates-to value from the WS-Addressing Header and place it in the Assertion Class
      if (assertion != null) {
          List<String> relatesToList = AsyncMessageIdExtractor.getAsyncRelatesTo(context);
          if (NullChecker.isNotNullish(relatesToList)) {
              assertion.getRelatesToList().add(AsyncMessageIdExtractor.getAsyncRelatesTo(context).get(0));
          }
      }


        return assertion;
    }
    
    protected String getLocalHomeCommunityId() {
        return HomeCommunityMap.getLocalHomeCommunityId();
    }
    
 
    
    protected AssertionType extractAssertion(WebServiceContext context) {
        return SAML2AssertionExtractor.getInstance().extractSamlAssertion(context);
    }

}
