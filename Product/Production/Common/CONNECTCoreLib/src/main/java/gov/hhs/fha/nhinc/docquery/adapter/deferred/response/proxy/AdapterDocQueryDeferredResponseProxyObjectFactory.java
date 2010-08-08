/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docquery.adapter.deferred.response.proxy;

import gov.hhs.fha.nhinc.proxy.ComponentProxyObjectFactory;

/**
 *
 * @author JHOPPESC
 */
public class AdapterDocQueryDeferredResponseProxyObjectFactory extends ComponentProxyObjectFactory {
    private static final String CONFIG_FILE_NAME = "DocumentQueryDeferredProxyConfig.xml";
    private static final String BEAN_NAME = "adapterdocquerydeferredresponse";

    protected String getConfigFileName()
    {
        return CONFIG_FILE_NAME;
    }

    public AdapterDocQueryDeferredResponseProxy getAdapterDocQueryDeferredRequestProxy()
    {
        return getBean(BEAN_NAME, AdapterDocQueryDeferredResponseProxy.class);
   }

}
