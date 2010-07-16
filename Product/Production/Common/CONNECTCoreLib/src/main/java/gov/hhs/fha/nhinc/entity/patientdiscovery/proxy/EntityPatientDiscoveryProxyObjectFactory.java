package gov.hhs.fha.nhinc.entity.patientdiscovery.proxy;

import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 *
 * @author Neil Webb
 */
public class EntityPatientDiscoveryProxyObjectFactory
{
    private static final String CONFIG_FILE_NAME = "EntityPatientDiscoveryProxyConfig.xml";
    private static final String BEAN_NAME_ENTITY_PATIENT_DISCOVERY = "entitypatientdiscovery";
    private static ApplicationContext context = null;

    public EntityPatientDiscoveryProxyObjectFactory()
    {
        if(context == null)
        {
            context = createApplicationContext();
        }
    }

    protected ApplicationContext createApplicationContext()
    {
        return new FileSystemXmlApplicationContext(PropertyAccessor.getPropertyFileURL() + CONFIG_FILE_NAME);
    }

    public EntityPatientDiscoveryProxy getEntityPatientDiscoveryProxy()
    {
        EntityPatientDiscoveryProxy entityPatientDiscoveryProxy = null;
        ApplicationContext workingContext = getContext();
        if (workingContext != null)
        {
            entityPatientDiscoveryProxy = (EntityPatientDiscoveryProxy) workingContext.getBean(BEAN_NAME_ENTITY_PATIENT_DISCOVERY);
        }
        return entityPatientDiscoveryProxy;
    }

    protected ApplicationContext getContext()
    {
        return context;
    }

}
