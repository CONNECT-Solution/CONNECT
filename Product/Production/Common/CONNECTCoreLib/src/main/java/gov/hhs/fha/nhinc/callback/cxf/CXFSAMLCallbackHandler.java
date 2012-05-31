/**
 * 
 */
package gov.hhs.fha.nhinc.callback.cxf;

import gov.hhs.fha.nhinc.callback.openSAML.CallbackMapProperties;
import gov.hhs.fha.nhinc.callback.openSAML.OpenSAMLCallbackHandler;
import gov.hhs.fha.nhinc.callback.openSAML.SAMLAssertionBuilder;
import gov.hhs.fha.nhinc.callback.openSAML.SAMLAssertionBuilderFactory;
import gov.hhs.fha.nhinc.callback.openSAML.SAMLAssertionBuilderFactoryImpl;

import java.io.IOException;
import java.util.HashMap;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ws.security.saml.ext.SAMLCallback;
import org.opensaml.common.SAMLVersion;
import org.w3c.dom.Element;

/**
 * @author mweaver
 * 
 */
public class CXFSAMLCallbackHandler implements CallbackHandler {

    private static Log log = LogFactory.getLog(CXFSAMLCallbackHandler.class);
    
    public static final String HOK_CONFIRM = "urn:oasis:names:tc:SAML:2.0:cm:holder-of-key";
    private SAMLAssertionBuilderFactory assertionBuilderFactory;

    public CXFSAMLCallbackHandler() {
        assertionBuilderFactory = new SAMLAssertionBuilderFactoryImpl();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.security.auth.callback.CallbackHandler#handle(javax.security.auth.callback.Callback[])
     */
    @Override
    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        log.debug("CXFSAMLCallbackHandler.handle begin");
        for (Callback callback : callbacks) {
            if (callback instanceof SAMLCallback) {

                Element assertionElement;
                try {
                    SAMLCallback oSAMLCallback = (SAMLCallback) callback;

                    oSAMLCallback.setSamlVersion(SAMLVersion.VERSION_20);

                    SAMLAssertionBuilder builder = assertionBuilderFactory
                            .getBuilder(SAMLAssertionBuilderFactory.HOK_ASSERTION_TYPE);
                    assertionElement = builder.build(new CallbackMapProperties(new HashMap<Object, Object>()));
                    log.debug("Element info: " + assertionElement.getClass().getPackage() + assertionElement.getClass().getName());
                    oSAMLCallback.setAssertionElement(assertionElement);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        log.debug("CXFSAMLCallbackHandler.handle end");
    }
}
