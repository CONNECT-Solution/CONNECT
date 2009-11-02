/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/common/RegistryProperties.java,v 1.15 2006/02/08 18:39:00 farrukh_najmi Exp $
 * ====================================================================
 */
package org.freebxml.omar.server.common;

import java.io.File;
import java.util.Properties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.freebxml.omar.common.AbstractProperties;
import org.freebxml.omar.common.CommonProperties;
import org.freebxml.omar.server.util.ServerResourceBundle;
/**
 * Abstraction for Registry Configuration properties.
 *
 * Initial implementation just uses Java Property file,
 * future implementation might acquire configuration info
 * from XML. Thus, all java property methods are called
 * via this abstraction.
 *
 * Registry Property File Search Order
 * 1. If Property "omar.properties", this value is used as the filename
 * to load the omar properties from. Skip to step 3 if this property is
 * set and the properties successfully loaded.
 * 2. If file Property{user.home}/omar.properties exists, the properties
 * are loaded from here.
 * 3. System default properties are read from the CLASSPATH. The first
 * file named org/freebxml/omar/server/common/omar.properties will be used to set
 * default Registry properties. These properties are overriden by the
 * same named property set by either steps 1 or 2.
 *
 * When adding a new property, be sure to set a default value for the
 * property in DEFAULT_PROPERTY_RESOURCE.
 *
 * Property Priority
 * 1. Highest priority: any java system properties including command line
 * properties set with -D
 * 2. Medium priority: any properties set in {user.home}/omar.properties
 * 3. Lowest priority: any properties set in the Registry default property file
 */
public class RegistryProperties extends AbstractProperties {
    private static Log log = LogFactory.getLog("org.freebxml.omar.server.common.RegistryProperties");
    private static final String PROPERTY_FILE_NAME = "omar.properties";
    private static final String DEFAULT_PROPERTY_RESOURCE = "org/freebxml/omar/server/common/omar-defaults.properties";
    private static RegistryProperties instance = null;
    
    /**
     * Cache RegistryProperties.
     */
    private RegistryProperties() {
        super();
        initProperties();
    }
        
    /**
     * Initializes the properties performing the full loading sequence. 
     */
    protected void initProperties() {
        // Complete omar-common loading sequence at first
        Properties commonProps = CommonProperties.staticLoadProperties(getClass().getClassLoader(), new Properties());

        // Load Provider Properties
        props = loadProperties(commonProps);

        // initializes <omar.home>
        initOmarHomeDir(props);

        // Substitute variables
        substituteVariables(props);
        
        logProperties("Registry properties", props);
    }

    /** This method is used to load properties. It returns a Properties
      * object that should be cached in memory.  It is called by
      * the RegistryProperties() constructor and by reloadProperties()
      *
      * @param defaultProps a default list of properties
      *
      * @return a Properties object loaded from a predefined resource
      */
    protected Properties loadProperties(final Properties defaultProps) {
        log.info(ServerResourceBundle.getInstance().getString("message.LoadingOmarProperties"));
        Properties properties = new Properties(defaultProps);
        
        // Start from default properties
        loadDefaultProperties(properties);
        
        // Props from resource at classpath root
        boolean resLoaded = loadResourceProperties(getClass().getClassLoader(),
            properties, PROPERTY_FILE_NAME);
        
        if (!resLoaded) {
            // Load properties from file system
            loadFileProperties(properties, new File(getPropertyFileName(properties)));
        }
        
        //TODO: should it load all or subset
        // we don't really want all the system props, should we weed them out?
        loadSystemProperties(properties);

        return properties;
    }

    /** 
      * Check property omar.properties for a property file name.
      * Default property file name is <omar.home>/omar.properties.
      */
    private String getPropertyFileName(Properties properties) {
        String propertyFileName = getOmarHome(properties) + "/" + PROPERTY_FILE_NAME;
        propertyFileName = System.getProperty(PROPERTY_FILE_NAME, propertyFileName);
        return propertyFileName;
    }

    /**
     * Replace pre-defined variables in property values with the variable value from the
     * corresponding property.
     */
    private void substituteVariables(Properties properties) {
        // at coding time this would replace $user.home and $omar.home
        CommonProperties.staticSubstituteVariables(properties);
        // add more substitutions here if required, like this
        //substituteVariables(properties, "$myVar", "newValue");
    }

    /**
     * Implement Singleton class, this method is only way to get this object.
     */
    public synchronized static RegistryProperties getInstance() {
        if (instance == null) {
            instance = new RegistryProperties();
        }
        return instance;
    }

    /**
     * Load default common properties, default properties from
     * DEFAULT_PROPERTY_RESOURCE and add them all to 'properties'.
     *
     * @param properties and existing property set to be updated.
     */
    public void loadDefaultProperties(Properties properties) {
        loadResourceProperties(getClass().getClassLoader(), properties
            , RegistryProperties.DEFAULT_PROPERTY_RESOURCE);
    }
    
}
