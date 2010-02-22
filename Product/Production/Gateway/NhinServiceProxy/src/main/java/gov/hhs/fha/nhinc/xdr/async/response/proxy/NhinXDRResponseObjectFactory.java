package gov.hhs.fha.nhinc.xdr.async.response.proxy;

import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 *
 * @author Neil Webb
 */
public class NhinXDRResponseObjectFactory
{
    private static final String CONFIG_FILE_NAME = "NhinXDRResponseProxyConfig.xml";
    private static final String BEAN_NAME_XDR = "nhinxdrresponse";
    private static ApplicationContext context = null;

    static
    {
        context = new FileSystemXmlApplicationContext(PropertyAccessor.getPropertyFileLocation() + CONFIG_FILE_NAME);
    }

    public NhinXDRResponseProxy getNhinXDRResponseProxy()
    {
        NhinXDRResponseProxy proxy = null;
        if (context != null)
        {
            proxy = (NhinXDRResponseProxy) context.getBean(BEAN_NAME_XDR);
        }
        return proxy;
    }

}
