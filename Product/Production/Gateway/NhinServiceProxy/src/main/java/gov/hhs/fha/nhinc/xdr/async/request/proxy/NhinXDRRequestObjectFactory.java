package gov.hhs.fha.nhinc.xdr.async.request.proxy;

import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 *
 * @author Neil Webb
 */
public class NhinXDRRequestObjectFactory
{
    private static final String CONFIG_FILE_NAME = "NhinXDRRequestProxyConfig.xml";
    private static final String BEAN_NAME_XDR = "nhinxdrrequest";
    private static ApplicationContext context = null;

    static
    {
        context = new FileSystemXmlApplicationContext(PropertyAccessor.getPropertyFileLocation() + CONFIG_FILE_NAME);
    }

    public NhinXDRRequestProxy getNhinXDRRequestProxy()
    {
        NhinXDRRequestProxy proxy = null;
        if (context != null)
        {
            proxy = (NhinXDRRequestProxy) context.getBean(BEAN_NAME_XDR);
        }
        return proxy;
    }

}
