package gov.hhs.fha.nhinc.adapter.xdr.async.request.error.proxy;

import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class AdapterXDRRequestErrorProxyObjectFactory
{
    private static final String CONFIG_FILE_NAME = "AdapterXDRRequestErrorProxyConfig.xml";
    private static final String BEAN_NAME_PROXY = "adapterxdrrequesterror";
    private static ApplicationContext context = null;

    static
    {
        context = new FileSystemXmlApplicationContext(PropertyAccessor.getPropertyFileURL() + CONFIG_FILE_NAME);
    }

    /**
     *
     * @return
     */
    public AdapterXDRRequestErrorProxy getAdapterXDRRequestErrorProxy()
    {
        AdapterXDRRequestErrorProxy proxy = null;
        if (context != null)
        {
            proxy = (AdapterXDRRequestErrorProxy) context.getBean(BEAN_NAME_PROXY);
        }
        return proxy;
    }
}
