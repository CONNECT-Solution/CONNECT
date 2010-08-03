package gov.hhs.fha.nhinc.proxy;

import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import java.io.File;
import java.net.URI;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * Base class for all component proxy object factories. Performs context loading
 * and refresh management. The application context will be refreshed if the configuraiton
 * file is modified.
 * 
 * @author Neil Webb
 */
public abstract class ComponentProxyObjectFactory
{
    protected Log log = null;
    private static ApplicationContext context = null;
    private static long configLastModified = -1L;

    public ComponentProxyObjectFactory()
    {
        log = createLogger();
    }

    /**
     * Create the logger.
     *
     * @return Logger
     */
    protected Log createLogger()
    {
        return LogFactory.getLog(getClass());
    }

    /**
     * Get the URL to properties files.
     *
     * @return Property file URL
     */
    protected String getPropertyFileURL()
    {
        return PropertyAccessor.getPropertyFileURL();
    }

    /**
     * Create a bean given the bean name of the interface type specified.
     * 
     * @param beanName The value of the "id" attribute of a bean element in the configuration file.
     * @param type Type of the bean created. The bean instance created will be cast to this type.
     * @return Bean instance of the type specified.
     */
    protected <T extends Object> T getBean(String beanName, Class<T> type)
    {
        Object proxyObject = null;
        ApplicationContext workingContext = getContext();
        if (workingContext != null)
        {
            proxyObject = workingContext.getBean(beanName);
        }
        else
        {
            log.warn("ApplicationContext was null - not retrieving bean.");
        }
        //TODO: Check type before returning.
        return type.cast(proxyObject);
    }

    /**
     * Get the application context. Create or refresh if necessary.
     *
     * @return ApplicationContext
     */
    protected ApplicationContext getContext()
    {
        String configFilePath = getPropertyFileURL() + getConfigFileName();

        if(getApplicationContext() == null)
        {
            log.debug("ApplicationContext was null - creating.");
            setApplicationContext(createApplicationContext(configFilePath));
            setConfigLastModifed(getLastModified(configFilePath));
        }
        else
        {
            log.debug("ApplicationContext was not null.");
            long lastModified = getLastModified(configFilePath);
            if(getConfigLastModified() != lastModified)
            {
                log.debug("Refreshing the Spring application context.");
                refreshConfigurationContext(getApplicationContext());
                setConfigLastModifed(lastModified);
            }
        }
        return getApplicationContext();
    }

    /**
     * Get the static application context
     * 
     * @return ApplicationContext value
     */
    protected ApplicationContext getApplicationContext()
    {
        return context;
    }

    /**
     * Set the static application context value.
     *
     * @param appContext ApplicationContext
     */
    protected void setApplicationContext(ApplicationContext appContext)
    {
        context = appContext;
    }

    /**
     * Retrieve the timestamp of the last time the config file was reloaded in MS
     *
     * @return Last config file modified timestamp
     */
    protected long getConfigLastModified()
    {
        return configLastModified;
    }

    /**
     * Set the last modified timestamp of the config file in MS.
     *
     * @param lastModified Last modified timestamp in MS.
     */
    protected void setConfigLastModifed(long lastModified)
    {
        configLastModified = lastModified;
    }

    /**
     * Create a configuration context
     *
     * @param configFilePath Path to the configuration file.
     * @return Created ApplicationContext
     */
    protected ApplicationContext createApplicationContext(String configFilePath)
    {
        return new FileSystemXmlApplicationContext(configFilePath);
    }

    /**
     * Refresh the ApplicationContext to reload the contents.
     *
     * @param appContext ApplicationContext to refresh.
     */
    protected void refreshConfigurationContext(ApplicationContext appContext)
    {
        ((ConfigurableApplicationContext)appContext).refresh();
    }

    /**
     * Get the timestamp in MS of the last time the configuration file was modified.
     *
     * @param filePath Path to the configuration file.
     * @return Last modified timestamp in MS
     */
    protected long getLastModified(String filePath)
    {
        long lastModified = 0L;
        try
        {
            String tmpFilePath = filePath.replace('\\', '/');
            URI fileURI = new URI(tmpFilePath);
            File configFile = new File(fileURI);
            if(configFile.exists())
            {
                lastModified = configFile.lastModified();
            }
            else
            {
                log.error(filePath + " does not exist.");
            }
        }
        catch(Throwable t)
        {
            log.error("Error getting last modified: " + t.getMessage(), t);
        }
        return lastModified;
    }

    /**
     * Get the name of the configuration file.
     *
     * @return Configuration file name
     */
    protected abstract String getConfigFileName();

}
