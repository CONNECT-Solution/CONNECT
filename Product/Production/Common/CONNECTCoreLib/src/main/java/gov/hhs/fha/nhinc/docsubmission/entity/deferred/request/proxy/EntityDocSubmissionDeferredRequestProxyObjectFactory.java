/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docsubmission.entity.deferred.request.proxy;

import gov.hhs.fha.nhinc.proxy.ComponentProxyObjectFactory;

/**
 * @author Jon Hoppesch
 */
public class EntityDocSubmissionDeferredRequestProxyObjectFactory extends ComponentProxyObjectFactory {

    private static final String CONFIG_FILE_NAME = "EntityXDRAsyncReqProxyConfig.xml";
    private static final String BEAN_NAME = "entityxdrasyncreq";

    protected String getConfigFileName() {
        return CONFIG_FILE_NAME;
    }

    public EntityDocSubmissionDeferredRequestProxy getEntityXDRDeferredReqProxy() {
        return getBean(BEAN_NAME, EntityDocSubmissionDeferredRequestProxy.class);
    }
}
