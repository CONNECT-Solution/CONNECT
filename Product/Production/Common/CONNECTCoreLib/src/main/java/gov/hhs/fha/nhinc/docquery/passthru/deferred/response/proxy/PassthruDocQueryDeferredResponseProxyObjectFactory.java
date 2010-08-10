/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docquery.passthru.deferred.response.proxy;

import gov.hhs.fha.nhinc.proxy.ComponentProxyObjectFactory;

/**
 *
 * @author jhoppesc
 */
public class PassthruDocQueryDeferredResponseProxyObjectFactory extends ComponentProxyObjectFactory {
    private static final String CONFIG_FILE_NAME = "DocumentQueryDeferredProxyConfig.xml";
    private static final String BEAN_NAME = "passthrudocquerydeferredresponse";

    protected String getConfigFileName()
    {
        return CONFIG_FILE_NAME;
    }

    public PassthruDocQueryDeferredResponseProxy getPassthruDocQueryDeferredResponseProxy()
    {
        return getBean(BEAN_NAME, PassthruDocQueryDeferredResponseProxy.class);
   }

}
