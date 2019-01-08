/*
 * Copyright (c) 2009-2019, United States Government, as represented by the Secretary of Health and Human Services.
 * All rights reserved.
 *  
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above
 *       copyright notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the documentation
 *       and/or other materials provided with the distribution.
 *     * Neither the name of the United States Government nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package gov.hhs.fha.nhinc.proxy;

import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * Base class for all component proxy object factories. Performs context loading and refresh management. The application
 * context will be refreshed if the configuraiton file is modified.
 *
 * @author Neil Webb, Les Westberg
 */
public abstract class ComponentProxyObjectFactory {

    private static final Logger LOG = LoggerFactory.getLogger(ComponentProxyObjectFactory.class);

    /**
     * As the contextMap cache is static, the lock for this class MUST be at the object level and not at the instance
     * level.
     */
    private static final Object CACHE_LOCK = new Object();

    // Getting a context is very expensive. We want to keep them around when we get them. Since
    // the context is specific to each of the derived classes, we need to keep a map for all of them.
    // We have synchronized the method that sets and retrieves this to make it thread safe.
    // ------------------------------------------------------------------------------------------------
    private static final HashMap<String, LocalApplicationContextInfo> contextMap = new HashMap<>();

    /**
     * Get the URL to properties files.
     *
     * @return Property file URL
     */
    protected String getPropertyFileURL() {
        return PropertyAccessor.getInstance().getPropertyFileURL();
    }

    /**
     * Create a bean given the bean name of the interface type specified.
     *
     * @param <T>
     * @param beanName The value of the "id" attribute of a bean element in the configuration file.
     * @param type Type of the bean created. The bean instance created will be cast to this type.
     * @return Bean instance of the type specified.
     */
    protected <T extends Object> T getBean(String beanName, Class<T> type) {
        Object proxyObject = null;

        // VERY BIG NOTE: THIS IS A SYNCHRONIZED METHOD SO THAT IT IS THREAD
        // SAFE. CONTEXT SHOULD ONLY BE SET OR RETRIEVED THROUGH THIS METHOD ONLY.
        // -------------------------------------------------------------------------
        ApplicationContext workingContext = getContext();
        if (workingContext != null) {
            proxyObject = workingContext.getBean(beanName);
        } else {
            LOG.warn("ApplicationContext was null - not retrieving bean.");
        }
        return type.cast(proxyObject);
    }

    /**
     * This is a helper class for unit test purposes that will return the requested LocalApplicationContextInfo from the
     * hash map.
     *
     * @param sKey The key to the file. The is the name of the config file.
     * @return The LocalApplicationContextInfo
     */
    protected LocalApplicationContextInfo getAppContextInfo(String sKey) {
        return contextMap.get(sKey);
    }

    /**
     * Get the application context. Create or refresh if necessary. VERY BIG NOTE: THIS IS A SYNCHRONIZED METHOD SO THAT
     * IT IS THREAD SAFE. CONTEXT SHOULD ONLY BE SET OR RETRIEVED THROUGH THIS METHOD ONLY.
     *
     * @return ApplicationContext
     */
    protected ApplicationContext getContext() {
        ApplicationContext appContext;

        String configFilePath = getPropertyFileURL() + getConfigFileName();
        if (LOG.isDebugEnabled()) {
            LOG.debug("Trying to get lock for ApplicationContext " + configFilePath);
        }
        synchronized (CACHE_LOCK) {
            LocalApplicationContextInfo appContextInfo = getAppContextInfo(getConfigFileName());
            if (appContextInfo == null) {
                LOG.debug("ApplicationContext for: " + getConfigFileName() + " was null - creating.");
                appContextInfo = new LocalApplicationContextInfo();
                appContextInfo.setApplicationContext(createApplicationContext(configFilePath));
                appContextInfo.setConfigLastModified(getLastModified(configFilePath));
                contextMap.put(getConfigFileName(), appContextInfo);
                appContext = appContextInfo.getApplicationContext();
            } else {
                LOG.debug("ApplicationContext for: " + getConfigFileName()
                        + " was not null - checking to see if it is stale.");
                long lastModified = getLastModified(configFilePath);
                if (appContextInfo.getConfigLastModified() != lastModified) {
                    LOG.debug("Refreshing the Spring application context for: " + getConfigFileName());
                    refreshConfigurationContext(appContextInfo.getApplicationContext());
                    appContextInfo.setConfigLastModified(lastModified);
                }
                appContext = appContextInfo.getApplicationContext();
            }
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Releasing lock for ApplicationContext" + configFilePath);
        }

        return appContext;
    }

    /**
     * Create a configuration context
     *
     * @param configFilePath Path to the configuration file.
     * @return Created ApplicationContext
     */
    protected ApplicationContext createApplicationContext(String configFilePath) {
        return new FileSystemXmlApplicationContext(configFilePath);
    }

    /**
     * Refresh the ApplicationContext to reload the contents.
     *
     * @param appContext ApplicationContext to refresh.
     */
    protected void refreshConfigurationContext(ApplicationContext appContext) {
        ((ConfigurableApplicationContext) appContext).refresh();

    }

    /**
     * Get the timestamp in MS of the last time the configuration file was modified.
     *
     * @param filePath Path to the configuration file.
     * @return Last modified timestamp in MS
     */
    protected long getLastModified(String filePath) {
        long lastModified = 0L;
        try {
            String tmpFilePath = filePath.replace('\\', '/').replaceAll(" ", "%20");
            URI fileURI = new URI(tmpFilePath);
            File configFile = new File(fileURI);
            if (configFile.exists()) {
                lastModified = configFile.lastModified();
            } else {
                LOG.error(filePath + " does not exist.");
            }
        } catch (URISyntaxException use) {
            LOG.error("Error getting last modified: " + use.getMessage(), use);
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
     * This class is an inner class to put the ApplicationContext, and last modified time together so that they can be
     * put in the map.
     */
    protected static class LocalApplicationContextInfo {

        private ApplicationContext applicationContext = null;
        private long configLastModified = -1L;

        /**
         * Return the last modified time for this config file.
         *
         * @return The last modified time for this config file.
         */
        public long getConfigLastModified() {
            return configLastModified;
        }

        /**
         * Set the last modified time for this config file.
         *
         * @param configLastModified The last modified time for this config file.
         */
        public void setConfigLastModified(long configLastModified) {
            this.configLastModified = configLastModified;
        }

        /**
         * Return the context.
         *
         * @return The context.
         */
        public ApplicationContext getApplicationContext() {
            return applicationContext;
        }

        /**
         * Set the context.
         *
         * @param applicationContext The context.
         */
        public void setApplicationContext(ApplicationContext applicationContext) {
            this.applicationContext = applicationContext;
        }
    }
}
