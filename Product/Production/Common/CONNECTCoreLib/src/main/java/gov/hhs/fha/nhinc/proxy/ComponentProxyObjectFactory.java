/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.proxy;

import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import java.io.File;
import java.net.URI;
import java.util.HashMap;
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
 * @author Neil Webb, Les Westberg
 */
public abstract class ComponentProxyObjectFactory
{
    protected Log log = null;
    
    // Getting a context is very expensive.  We want to keep them around when we get them.  Since
    // the context is specific to each of the derived classes, we need to keep a map for all of them.
    // We have synchronized the method that sets and retrieves this to make it thread safe.  
    //------------------------------------------------------------------------------------------------
    private static HashMap<String, LocalApplicationContextInfo> contextMap = new HashMap<String, LocalApplicationContextInfo>();

    /**
     * Default constructor.
     */
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
        
        // VERY BIG NOTE: THIS IS A SYNCHRONIZED METHOD SO THAT IT IS THREAD
        // SAFE. CONTEXT SHOULD ONLY BE SET OR RETRIEVED THROUGH THIS METHOD ONLY.
        //-------------------------------------------------------------------------
        ApplicationContext workingContext = getContext();
        if (workingContext != null)
        {
            proxyObject = workingContext.getBean(beanName);
        }
        else
        {
            log.warn("ApplicationContext was null - not retrieving bean.");
        }
        return type.cast(proxyObject);
    }

    /**
     * This is a helper class for unit test purposes that will return the
     * requested LocalApplicationContextInfo from the hash map.
     *
     * @param sKey The key to the file.  The is the name of the config file.
     * @return The LocalApplicationContextInfo
     */
    protected LocalApplicationContextInfo getAppContextInfo(String sKey)
    {
        return contextMap.get(sKey);
    }

    /**
     * Get the application context. Create or refresh if necessary.
     * VERY BIG NOTE: THIS IS A SYNCHRONIZED METHOD SO THAT IT IS THREAD
     * SAFE. CONTEXT SHOULD ONLY BE SET OR RETRIEVED THROUGH THIS METHOD ONLY.
     *
     * @return ApplicationContext
     */
    protected synchronized ApplicationContext getContext()
    {
        ApplicationContext appContext = null;

        String configFilePath = getPropertyFileURL() + getConfigFileName();
        LocalApplicationContextInfo appContextInfo = getAppContextInfo(getConfigFileName());

        if(appContextInfo == null)
        {
            log.debug("ApplicationContext for: " + getConfigFileName() + ".xml was null - creating.");
            appContextInfo = new LocalApplicationContextInfo();
            appContextInfo.setApplicationContext(createApplicationContext(configFilePath));
            appContextInfo.setConfigLastModified(getLastModified(configFilePath));
            contextMap.put(getConfigFileName(), appContextInfo);
            appContext = appContextInfo.getApplicationContext();
        }
        else
        {
            log.debug("ApplicationContext for: " + getConfigFileName() + ".xml was not null - checking to see if it is stale.");
            long lastModified = getLastModified(configFilePath);
            if(appContextInfo.getConfigLastModified() != lastModified)
            {
                log.debug("Refreshing the Spring application context for: " + getConfigFileName() + ".xml");
                refreshConfigurationContext(appContextInfo.getApplicationContext());
                appContextInfo.setConfigLastModified(lastModified);
            }
            appContext = appContextInfo.getApplicationContext();
        }
        return appContext;
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

    /**
     * This class is an inner class to put the ApplicationContext, and last
     * modified time together so that they can be put in the map.
     */
    protected class LocalApplicationContextInfo
    {
        private ApplicationContext applicationContext = null;
        private long configLastModified = -1L;

        /**
         * Return the last modified time for this config file.
         *
         * @return The last modified time for this config file.
         */
        public long getConfigLastModified()
        {
            return configLastModified;
        }

        /**
         * Set the last modified time for this config file.
         *
         * @param configLastModified The last modified time for this config file.
         */
        public void setConfigLastModified(long configLastModified)
        {
            this.configLastModified = configLastModified;
        }

        /**
         * Return the context.
         *
         * @return The context.
         */
        public ApplicationContext getApplicationContext()
        {
            return applicationContext;
        }

        /**
         * Set the context.
         *
         * @param applicationContext The context.
         */
        public void setApplicationContext(ApplicationContext applicationContext)
        {
            this.applicationContext = applicationContext;
        }
    }
}
