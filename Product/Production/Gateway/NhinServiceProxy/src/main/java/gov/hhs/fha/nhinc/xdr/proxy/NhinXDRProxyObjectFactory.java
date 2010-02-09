/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.xdr.proxy;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
/**
 *
 * @author dunnek
 */
public class NhinXDRProxyObjectFactory {
    private static final String CONFIG_FILE_NAME = "NhinXDRProxyConfig.xml";
    private static final String BEAN_NAME_XDR = "nhinxdr";
    private static ApplicationContext context = null;

    static {
        context = new FileSystemXmlApplicationContext(PropertyAccessor.getPropertyFileLocation() + CONFIG_FILE_NAME);
    }
    public NhinXDRProxy getNhinXDRProxy() {
        NhinXDRProxy proxy = null;
        if (context != null) {
            proxy = (NhinXDRProxy) context.getBean(BEAN_NAME_XDR);
        }
        return proxy;
    }
}
