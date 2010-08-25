/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docsubmission.nhin.deferred.response.proxy;

import gov.hhs.fha.nhinc.proxy.ComponentProxyObjectFactory;

/**
 *
 * @author JHOPPESC
 */
public class NhinDocSubmissionDeferredResponseProxyObjectFactory extends ComponentProxyObjectFactory {
    private static final String CONFIG_FILE_NAME = "NhinXDRResponseProxyConfig.xml";
    private static final String BEAN_NAME = "nhinxdrresponse";

    protected String getConfigFileName() {
        return CONFIG_FILE_NAME;
    }

    public NhinDocSubmissionDeferredResponseProxy getNhinDocSubmissionDeferredResponseProxy() {
        return getBean(BEAN_NAME, NhinDocSubmissionDeferredResponseProxy.class);
    }

}
