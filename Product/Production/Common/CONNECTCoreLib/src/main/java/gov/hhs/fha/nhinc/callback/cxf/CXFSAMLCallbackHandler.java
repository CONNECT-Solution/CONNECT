/**
 *
 */
package gov.hhs.fha.nhinc.callback.cxf;

import gov.hhs.fha.nhinc.callback.openSAML.CallbackMapProperties;
import gov.hhs.fha.nhinc.callback.openSAML.CallbackProperties;
import gov.hhs.fha.nhinc.callback.openSAML.HOKSAMLAssertionBuilder;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;

import java.io.IOException;
import java.util.Map;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.apache.log4j.Logger;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.PhaseInterceptorChain;
import org.apache.ws.security.saml.ext.SAMLCallback;
import org.opensaml.common.SAMLVersion;

/**
 * @author mweaver
 *
 */
public class CXFSAMLCallbackHandler implements CallbackHandler {

    private static final Logger LOG = Logger.getLogger(CXFSAMLCallbackHandler.class);

    public static final String HOK_CONFIRM = "urn:oasis:names:tc:SAML:2.0:cm:holder-of-key";
    private HOKSAMLAssertionBuilder builder = new HOKSAMLAssertionBuilder();
    
    public CXFSAMLCallbackHandler() {
    }
    
    public CXFSAMLCallbackHandler(HOKSAMLAssertionBuilder builder){
    	this.builder = builder;
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.security.auth.callback.CallbackHandler#handle(javax.security.auth.callback.Callback[])
     */
    @Override
    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        LOG.trace("CXFSAMLCallbackHandler.handle begin");
        for (Callback callback : callbacks) {
            if (callback instanceof SAMLCallback) {

                try {

                    Message message = getCurrentMessage();

                    Object obj = message.get("assertion");

                    AssertionType custAssertion = null;
                    if (obj != null) {
                        custAssertion = (AssertionType) obj;
                    }

                    SAMLCallback oSAMLCallback = (SAMLCallback) callback;

                    oSAMLCallback.setSamlVersion(SAMLVersion.VERSION_20);

                    SamlTokenCreator creator = new SamlTokenCreator();

                    CallbackProperties properties = new CallbackMapProperties(addMessageProperties(
                            creator.createRequestContext(custAssertion, null, null), message));

                    oSAMLCallback.setAssertionElement(builder.build(properties));
                } catch (Exception e) {
                    LOG.error("failed to create saml", e);
                }
            }
        }
        LOG.trace("CXFSAMLCallbackHandler.handle end");
    }
    
    /**
     * Populate Callback Properties with additional properties set on the message.
     * 
     * @param propertiesMap to be appended.
     * @param message source of additional properties.
     * @return map containing assertion data and additional properties.
     */
    private Map<String, Object> addMessageProperties(Map<String, Object> propertiesMap, Message message) {
        
        addPropertyFromMessage(propertiesMap, message, NhincConstants.WS_SOAP_TARGET_HOME_COMMUNITY_ID);
        addPropertyFromMessage(propertiesMap, message, NhincConstants.TARGET_API_LEVEL);
        addPropertyFromMessage(propertiesMap, message, NhincConstants.ACTION_PROP);
        
        return propertiesMap;                
    }
    
    private void addPropertyFromMessage(Map<String, Object> propertiesMap, Message message, String key) {
        propertiesMap.put(key, message.get(key));
    }
    
    protected Message getCurrentMessage(){
    	return PhaseInterceptorChain.getCurrentMessage();
    }
}

