package gov.hhs.fha.nhinc.patientcorrelationfacade.proxy;

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
 * <bean id="patientcorrelation" class="gov.hhs.fha.nhinc.patientcorrelation.PatientCorrelationBaseImpl"/>
 * the bean id is "patientcorrelation" and an object of this type can be retrieved from
 * the application context by calling the getBean method like:
 * context.getBean("patientcorrelation");. This returns an object that can be casted to
 * the appropriate interface and then used in the application code. See the
 * getPatientCorrelation() method in this class.
 *
 * @author Neil Webb
 */
public class PatientCorrelationFacadeProxyObjectFactory
{
    private static final String SPRING_CONFIG_FILE = "PatientCorrelationFacadeConfig.xml";
    private static final String BEAN_NAME_PATIENT_CORRELATION = "patientcorrelationfacade";

    private static ApplicationContext context = null;

    static
    {
        context = new FileSystemXmlApplicationContext(PropertyAccessor.getPropertyFileLocation() + SPRING_CONFIG_FILE);
    }

    /**
     * Retrieve a patient correlation implementation using the IOC framework.
     * This method retrieves the object from the framework that has an
     * identifier of "patientcorrelation."
     *
     * @return PatientCorrelationFacadeProxy instance
     */
    public PatientCorrelationFacadeProxy getPatientCorrelationFacadeProxy()
    {
        PatientCorrelationFacadeProxy correlation = null;
        if(context != null)
        {
            correlation = (PatientCorrelationFacadeProxy)context.getBean(BEAN_NAME_PATIENT_CORRELATION);
        }
        return correlation;
    }
}
