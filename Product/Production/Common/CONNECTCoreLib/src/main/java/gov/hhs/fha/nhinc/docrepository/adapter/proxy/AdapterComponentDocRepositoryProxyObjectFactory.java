/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docrepository.adapter.proxy;

import gov.hhs.fha.nhinc.proxy.ComponentProxyObjectFactory;

/**
 *
 * @author rayj
 */
public class AdapterComponentDocRepositoryProxyObjectFactory extends ComponentProxyObjectFactory {
    private static final String CONFIG_FILE_NAME = "AdapterDocumentRepositoryProxyConfig.xml";
    private static final String BEAN_NAME = "adapterdocumentrepository";

    protected String getConfigFileName() {
        return CONFIG_FILE_NAME;
    }

    public AdapterComponentDocRepositoryProxy getAdapterDocumentRepositoryProxy() {
        return getBean(BEAN_NAME, AdapterComponentDocRepositoryProxy.class);
    }
    
}
