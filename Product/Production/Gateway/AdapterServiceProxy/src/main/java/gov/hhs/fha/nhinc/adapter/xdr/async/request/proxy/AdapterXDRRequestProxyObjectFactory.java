/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.adapter.xdr.async.request.proxy;

import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 *
 * @author JHOPPESC
 */
public class AdapterXDRRequestProxyObjectFactory {

    private static final String CONFIG_FILE_NAME = "AdapterXDRRequestProxyConfig.xml";
    private static final String BEAN_NAME_ADAPTER_DOCQUERY = "adapterxdrrequest";
    private static ApplicationContext context = null;

    static
    {
        context = new FileSystemXmlApplicationContext(PropertyAccessor.getPropertyFileURL() + CONFIG_FILE_NAME);
    }

    /**
     *
     * @return
     */
    public AdapterXDRRequestProxy getAdapterXDRRequestProxy()
    {
        AdapterXDRRequestProxy adapterDocQuery = null;
        if (context != null)
        {
            adapterDocQuery = (AdapterXDRRequestProxy) context.getBean(BEAN_NAME_ADAPTER_DOCQUERY);
        }
        return adapterDocQuery;
    }

}


