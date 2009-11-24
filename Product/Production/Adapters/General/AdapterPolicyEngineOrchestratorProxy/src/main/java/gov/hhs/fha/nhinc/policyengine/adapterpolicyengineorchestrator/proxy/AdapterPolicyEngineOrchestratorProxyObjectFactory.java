package gov.hhs.fha.nhinc.policyengine.adapterpolicyengineorchestrator.proxy;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;

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
 * <bean id="adapterpolicyengineorchestrator" class="gov.hhs.fha.nhinc.policyengine.adapterpolicyengineorchestrator.AdapterPolicyEngineOrchestratorImpl"/>
 * the bean id is "adapterpolicyengineorchestrator" and an object of this type can be retrieved from
 * the application context by calling the getBean method like:
 * context.getBean("adapterpolicyengineorchestrator");. This returns an object that can be casted to
 * the appropriate interface and then used in the application code. See the
 * getAdapterPolicyEngineOrchestratorProxy() method in this class.
 *
 * @author Les westberg
 */
public class AdapterPolicyEngineOrchestratorProxyObjectFactory
{
    private static final String SPRING_CONFIG_FILE = "AdapterPolicyEngineOrchestratorProxyConfig.xml";
    private static final String BEAN_NAME_ADAPTER_POLICY_ENGINE_ORCHESTRATOR = "adapterpolicyengineorchestrator";

    private static ApplicationContext context = null;

    static
    {
        String sConfigFileLocation = PropertyAccessor.getPropertyFileLocation();

        context = new FileSystemXmlApplicationContext(sConfigFileLocation + SPRING_CONFIG_FILE);
    }

    /**
     * Retrieve an adapter policy engine orchestrator implementation using the IOC framework.
     * This method retrieves the object from the framework that has an
     * identifier of "adapterpolicyengineorchestrator."
     *
     * @return AdapterPolicyEngineOrchestratorProxy instance
     */
    public AdapterPolicyEngineOrchestratorProxy getAdapterPolicyEngineOrchestratorProxy()
    {
        AdapterPolicyEngineOrchestratorProxy oOrchestratorProxy = null;
        if(context != null)
        {
            oOrchestratorProxy = (AdapterPolicyEngineOrchestratorProxy)context.getBean(BEAN_NAME_ADAPTER_POLICY_ENGINE_ORCHESTRATOR);
        }
        return oOrchestratorProxy;
    }
}
