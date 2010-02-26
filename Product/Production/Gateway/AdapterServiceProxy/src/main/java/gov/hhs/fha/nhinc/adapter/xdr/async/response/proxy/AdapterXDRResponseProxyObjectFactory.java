package gov.hhs.fha.nhinc.adapter.xdr.async.response.proxy;

import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class AdapterXDRResponseProxyObjectFactory
{
    private static final String CONFIG_FILE_NAME = "AdapterXDRResponseProxyConfig.xml";
    private static final String BEAN_NAME_ADAPTER_DOCQUERY = "adapterxdrresponse";
    private static ApplicationContext context = null;

    static
    {
        context = new FileSystemXmlApplicationContext(PropertyAccessor.getPropertyFileLocation() + CONFIG_FILE_NAME);
    }

    /**
     * 
     * @return
     */
    public AdapterXDRResponseProxy getAdapterXDRResponseProxy()
    {
        AdapterXDRResponseProxy adapterDocQuery = null;
        if (context != null)
        {
            adapterDocQuery = (AdapterXDRResponseProxy) context.getBean(BEAN_NAME_ADAPTER_DOCQUERY);
        }
        return adapterDocQuery;
    }
}
