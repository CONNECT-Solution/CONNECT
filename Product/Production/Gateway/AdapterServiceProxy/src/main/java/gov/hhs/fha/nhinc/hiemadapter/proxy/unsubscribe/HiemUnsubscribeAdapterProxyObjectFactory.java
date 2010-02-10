package gov.hhs.fha.nhinc.hiemadapter.proxy.unsubscribe;

import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 *
 * @author rayj
 */
public class HiemUnsubscribeAdapterProxyObjectFactory {

    private static final String CONFIG_FILE_NAME = "hiemAdapterConfig.xml";
    private static final String BEAN_NAME_HIEM_SUBSCRIBE_ADAPTER = "hiemunsubscribeadapter";
    private static ApplicationContext context = null;


    static
    {
        context = new FileSystemXmlApplicationContext(PropertyAccessor.getPropertyFileLocation() + CONFIG_FILE_NAME);
    }

    public HiemUnsubscribeAdapterProxy getHiemSubscribeAdapterProxy() {
        HiemUnsubscribeAdapterProxy proxy = null;
        if (context != null) {
            proxy = (HiemUnsubscribeAdapterProxy) context.getBean(BEAN_NAME_HIEM_SUBSCRIBE_ADAPTER);
        }
        return proxy;
    }
}

