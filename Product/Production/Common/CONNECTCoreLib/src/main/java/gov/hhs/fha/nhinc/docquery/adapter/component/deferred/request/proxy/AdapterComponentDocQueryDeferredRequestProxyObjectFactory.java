/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docquery.adapter.component.deferred.request.proxy;

import gov.hhs.fha.nhinc.proxy.ComponentProxyObjectFactory;

/**
 *
 * @author jhoppesc
 */
public class AdapterComponentDocQueryDeferredRequestProxyObjectFactory extends ComponentProxyObjectFactory {
    private static final String CONFIG_FILE_NAME = "DocumentQueryDeferredProxyConfig.xml";
    private static final String BEAN_NAME = "adaptercomponentdocquerydeferredrequest";

    protected String getConfigFileName() {
        return CONFIG_FILE_NAME;
    }

    public AdapterComponentDocQueryDeferredRequestProxy getAdapterDocQueryDeferredRequestProxy() {
        return getBean(BEAN_NAME, AdapterComponentDocQueryDeferredRequestProxy.class);
    }

}
