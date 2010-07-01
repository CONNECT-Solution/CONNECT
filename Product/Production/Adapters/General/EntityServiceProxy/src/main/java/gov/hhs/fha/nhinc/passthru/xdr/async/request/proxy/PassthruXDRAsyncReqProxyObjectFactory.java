/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.passthru.xdr.async.request.proxy;

import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * An object factory that uses the Spring Framework to create service
 * implementation objects. The configuration file is referenced in the
 * common properties file location to assist in installation and configuration
 * simplicity.
 *
 * The Spring configuration file is defined by the constant "SPRING_CONFIG_FILE".
 * This file is loaded into an application context in the static initializer
 * and then the objects defined in the config file are available to the framework
 * for creation. This configuration file can be located anywhere in the
 * classpath.
 *
 * To retrieve an object that is created by the framework, the
 * "getBean(String beanId)" method is called on the application context passing
 * in the beanId that is specified in the config file. Considering the default
 * correlation definition in the config file for this component:
 * <bean id="passthruxdrasyncreq" class="gov.hhs.fha.nhinc.passthru.xdr.async.request.proxy.PassthruXDRAsyncReqNoOpImpl"/>
 * the bean id is "passthruxdrasyncreq" and an object of this type can be retrieved from
 * the application context by calling the getBean method like:
 * context.getBean("passthruxdrasyncreq");. This returns an object that can be casted to
 * the appropriate interface and then used in the application code. See the
 * getPassthruXDRAsyncReqProxy() method in this class.
 *
 * @author Jon Hoppesch
 */
public class PassthruXDRAsyncReqProxyObjectFactory {

    private static final String CONFIG_FILE_NAME = "PassthruXDRAsyncReqProxyConfig.xml";
    private static final String BEAN_NAME = "passthruxdrasyncreq";
    private static ApplicationContext context = null;

    static {
        context = new FileSystemXmlApplicationContext(PropertyAccessor.getPropertyFileURL() + CONFIG_FILE_NAME);
    }

    /**
     * Retrieve a entity patient discovery implementation using the IOC framework.
     * This method retrieves the object from the framework that has an
     * identifier of "passthruxdrasyncreq."
     *
     * @return PassthruXDRAsyncReqProxy instance
     */
    public PassthruXDRAsyncReqProxy getPassthruXDRAsyncReqProxy() {
        PassthruXDRAsyncReqProxy proxy = null;
        if (context != null) {
            proxy = (PassthruXDRAsyncReqProxy) context.getBean(BEAN_NAME);
        }
        return proxy;
    }

}
