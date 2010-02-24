package gov.hhs.fha.nhinc.adapter.xdr.async.request.proxy;

import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class AdapterXDRRequestProxyObjectFactory
{
    private static final String CONFIG_FILE_NAME = "AdapterXDRRequestProxyConfig.xml";
    private static final String BEAN_NAME_ADAPTER_DOCQUERY = "adapterxdrrequest";
    private static ApplicationContext context = null;

    static
    {
        context = new FileSystemXmlApplicationContext(PropertyAccessor.getPropertyFileLocation() + CONFIG_FILE_NAME);
    }

    public AdapterXDRRequestProxy getAdapterDocQueryProxy()
    {
        AdapterXDRRequestProxy adapterDocQuery = null;
        if (context != null)
        {
            adapterDocQuery = (AdapterXDRRequestProxy) context.getBean(BEAN_NAME_ADAPTER_DOCQUERY);
        }
        return adapterDocQuery;
    }
}
