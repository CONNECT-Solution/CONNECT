package gov.hhs.fha.nhinc.redaction.proxy;

import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 *
 * @author Neil Webb
 */
public class RedactionEngineProxyFactory
{
    private static final String CONFIG_FILE_NAME = "AdapterRedactionEngineProxyConfig.xml";
    private static final String BEAN_NAME_ADAPTER_REDACTION_ENGINE = "adapterredactionengine";
    private static ApplicationContext context = null;

    public RedactionEngineProxyFactory()
    {
        if(context == null)
        {
            context = createApplicationContext();
        }
    }

    protected ApplicationContext createApplicationContext()
    {
        return new FileSystemXmlApplicationContext(PropertyAccessor.getPropertyFileLocation() + CONFIG_FILE_NAME);
    }

    public RedactionEngineProxy getRedactionEngineProxy()
    {
        RedactionEngineProxy redactionEngineProxy = null;
        ApplicationContext workingContext = getContext();
        if (workingContext != null)
        {
            redactionEngineProxy = (RedactionEngineProxy) workingContext.getBean(BEAN_NAME_ADAPTER_REDACTION_ENGINE);
        }
        return redactionEngineProxy;
    }

    protected ApplicationContext getContext()
    {
        return context;
    }

}
