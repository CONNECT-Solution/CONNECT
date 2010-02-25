/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.nhinc.xdr.routing;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
/**
 *
 * @author dunnek
 */
public class RoutingObjectFactory {
    private static final String CONFIG_FILE_NAME = "NhinXDRRoutingProxyConfig.xml";

    public static final String BEAN_REFERENCE_IMPLEMENTATION = "reference";

    private static ApplicationContext context = null;

    static {
        context = new FileSystemXmlApplicationContext(PropertyAccessor.getPropertyFileLocation() + CONFIG_FILE_NAME);
    }
    public XDRRouting getNhinXDRRouting(String beanName) {
        XDRRouting proxy = null;
        if (context != null) {
            proxy = (XDRRouting) context.getBean(beanName);
        }
        return proxy;
    }
}
