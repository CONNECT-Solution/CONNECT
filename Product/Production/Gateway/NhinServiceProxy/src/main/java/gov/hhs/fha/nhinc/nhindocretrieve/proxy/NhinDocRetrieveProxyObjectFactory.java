package gov.hhs.fha.nhinc.nhindocretrieve.proxy;

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
 * for creation. This configuration file must be located in the gateway property
 * file directory.
 *
 * To retrieve an object that is created by the framework, the
 * "getBean(String beanId)" method is called on the application context passing
 * in the beanId that is specified in the config file. Considering the default
 * correlation definition in the config file for this component:
 * <bean id="nhindocretrieve" class="gov.hhs.fha.nhinc.nhindocretrieve.proxy.NhinDocRetrieveNoOpImpl"/>
 * the bean id is "nhindocretrieve" and an object of this type can be retrieved from
 * the application context by calling the getBean method like:
 * context.getBean("nhindocretrieve");. This returns an object that can be casted to
 * the appropriate interface and then used in the application code. See the
 * getNhinDocRetrieveProxy() method in this class.
 *
 * @author Neil Webb
 */
public class NhinDocRetrieveProxyObjectFactory
{
    private static final String CONFIG_FILE_NAME = "NhinDocRetrieveProxyConfig.xml";
    private static final String BEAN_NAME_NHIN_DOC_RETRIEVE = "nhindocretrieve";
    private static ApplicationContext context = null;


    static
    {
        context = new FileSystemXmlApplicationContext(PropertyAccessor.getPropertyFileLocation() + CONFIG_FILE_NAME);
    }

    /**
     * Retrieve a nhin document retrieve implementation using the IOC framework.
     * This method retrieves the object from the framework that has an
     * identifier of "nhindocretrieve."
     *
     * @return NhinDocRetrieveProxy instance
     */
    public NhinDocRetrieveProxy getNhinDocRetrieveProxy()
    {
        NhinDocRetrieveProxy nhinDocRetrieve = null;
        if (context != null)
        {
            nhinDocRetrieve = (NhinDocRetrieveProxy) context.getBean(BEAN_NAME_NHIN_DOC_RETRIEVE);
        }
        return nhinDocRetrieve;
    }
}
