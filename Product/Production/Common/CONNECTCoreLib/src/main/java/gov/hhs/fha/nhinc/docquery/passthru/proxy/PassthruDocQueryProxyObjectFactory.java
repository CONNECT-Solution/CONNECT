/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docquery.passthru.proxy;

import gov.hhs.fha.nhinc.proxy.ComponentProxyObjectFactory;

/**
 *
 * @author JHOPPESC
 */
public class PassthruDocQueryProxyObjectFactory extends ComponentProxyObjectFactory {

    private static final String CONFIG_FILE_NAME = "DocumentQueryDeferredProxyConfig.xml";
    private static final String BEAN_NAME = "passthrudocquery";

    protected String getConfigFileName() {
        return CONFIG_FILE_NAME;
    }

    public PassthruDocQueryProxy getPassthruDocQueryProxy() {
        return getBean(BEAN_NAME, PassthruDocQueryProxy.class);
    }

}
