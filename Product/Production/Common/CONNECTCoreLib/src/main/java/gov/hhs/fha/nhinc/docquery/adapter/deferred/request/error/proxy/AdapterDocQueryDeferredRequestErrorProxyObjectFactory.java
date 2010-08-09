/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docquery.adapter.deferred.request.error.proxy;

import gov.hhs.fha.nhinc.proxy.ComponentProxyObjectFactory;

/**
 *
 * @author jhoppesc
 */
public class AdapterDocQueryDeferredRequestErrorProxyObjectFactory extends ComponentProxyObjectFactory {
    private static final String CONFIG_FILE_NAME = "DocumentQueryDeferredProxyConfig.xml";
    private static final String BEAN_NAME = "adapterdocquerydeferredrequesterror";

    protected String getConfigFileName() {
        return CONFIG_FILE_NAME;
    }

    public AdapterDocQueryDeferredRequestErrorProxy getAdapterDocQueryDeferredRequestErrorProxy() {
        return getBean(BEAN_NAME, AdapterDocQueryDeferredRequestErrorProxy.class);
    }

}
