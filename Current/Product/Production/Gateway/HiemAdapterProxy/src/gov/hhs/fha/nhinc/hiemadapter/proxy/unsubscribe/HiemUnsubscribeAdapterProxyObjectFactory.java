/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.hiemadapter.proxy.unsubscribe;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author rayj
 */
public class HiemUnsubscribeAdapterProxyObjectFactory {

    private static final String SPRING_CONFIG_FILE = "hiemAdapterConfig.xml";
    private static final String BEAN_NAME_HIEM_SUBSCRIBE_ADAPTER = "hiemunsubscribeadapter";
    private static ApplicationContext context = null;


    static {
        context = new ClassPathXmlApplicationContext(SPRING_CONFIG_FILE);
    }

    public HiemUnsubscribeAdapterProxy getHiemSubscribeAdapterProxy() {
        HiemUnsubscribeAdapterProxy proxy = null;
        if (context != null) {
            proxy = (HiemUnsubscribeAdapterProxy) context.getBean(BEAN_NAME_HIEM_SUBSCRIBE_ADAPTER);
        }
        return proxy;
    }
}

