/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docquery.adapter.component.deferred.response.proxy;

import gov.hhs.fha.nhinc.proxy.ComponentProxyObjectFactory;

/**
 *
 * @author jhoppesc
 */
public class AdapterComponentDocQueryDeferredResponseProxyObjectFactory extends ComponentProxyObjectFactory {
    private static final String CONFIG_FILE_NAME = "DocumentQueryDeferredProxyConfig.xml";
    private static final String BEAN_NAME = "adaptercomponentdocquerydeferredresponse";

    protected String getConfigFileName() {
        return CONFIG_FILE_NAME;
    }

    public AdapterComponentDocQueryDeferredResponseProxy getAdapterDocQueryDeferredResponseProxy() {
        return getBean(BEAN_NAME, AdapterComponentDocQueryDeferredResponseProxy.class);
    }

}
