/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/common/CommonProperties.java,v 1.16 2006/02/08 18:38:52 farrukh_najmi Exp $
 * ====================================================================
 */
package org.freebxml.omar.common;

import java.io.File;
import java.util.Properties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * A common class for configuration properties. Can be extended by server
 * and client for more specific property loading.
 *
 * For now only loading from DEFAULT_PROPERTY_RESOURCE.
 * //TODO: load from other places (FS, command line, registry server)
 *
 * @author  Diego Ballve / Republica Corp
 */
public class CommonProperties extends AbstractProperties {

    private static Log log = LogFactory.getLog("org.freebxml.omar.common.CommonProperties");    
    private static final String PROPERTY_FILE_NAME = "omar-common.properties";
    private static final String DEFAULT_PROPERTY_RESOURCE = "org/freebxml/omar/common/omar-common-defaults.properties";

    private static CommonProperties instance = null;

    protected CommonProperties() {
        super();
        initProperties();
    }
        
    /**
     * Initializes the properties performing the full loading sequence. 
     */
    protected void initProperties() {
        props = loadProperties(new Properties());

        // initializes <omar.home>
        initOmarHomeDir(props);

        // Substitute variables
        substituteVariables(props);
        
        logProperties("Common properties", props);
    }
    
    /**
     * Implement Singleton class, this method is only way to get this object.
     */
    public synchronized static CommonProperties getInstance() {
        if (instance == null) {
            instance = new CommonProperties();
        }
        return instance;
    }

    protected Properties loadProperties(final Properties props) {
        return staticLoadProperties(getClass().getClassLoader(), props);
    }
    
    public static Properties staticLoadProperties(ClassLoader classLoader, final Properties props) {
        log.trace(CommonResourceBundle.getInstance().getString("message.LoadingOmar-commonProperties"));
        Properties properties = new Properties(props);
        
        // Start from default properties
        staticLoadDefaultProperties(classLoader, properties);
        
        // Props from resource at classpath root
        boolean resLoaded = loadResourceProperties(classLoader, properties, PROPERTY_FILE_NAME);
        
        if (!resLoaded) {
            // Load properties from file system
            loadFileProperties(properties, new File(getPropertyFileName(properties)));
        }
        
        //TODO: should it load all or subset
        loadSystemProperties(properties);

        return properties;
    }
    
    /** 
      * Check property omar.properties for a property file name.
      * Default property file name is <registry.home>/omar-common.properties.
      */
    private static String getPropertyFileName(Properties properties) {
        String propertyFileName = getOmarHome(properties) + "/" + PROPERTY_FILE_NAME;
        propertyFileName = System.getProperty(PROPERTY_FILE_NAME, propertyFileName);
        return propertyFileName;
    }
    
    /**
     * Replace pre-defined variables in property values with the variable value from the
     * corresponding property.
     */
    protected void substituteVariables(Properties properties) {
        staticSubstituteVariables(properties);
    }

    public static void staticSubstituteVariables(Properties properties) {
        //Iterate and replace allowed variables
        substituteVariables(properties, "$user.home", getUserHome());
        substituteVariables(properties, "$omar.home", getOmarHome(properties));
    }
    
    /**
     * Load default property settings for this property list using the 
     * DEFAULT_PROPERTY_RESOURCE.
     *
     * @return a Properties object loaded from a predefined resource
     */
    protected void loadDefaultProperties(Properties properties) {
        staticLoadDefaultProperties(getClass().getClassLoader(), properties);
    }

    private static void staticLoadDefaultProperties(ClassLoader classLoader, Properties properties) {
        loadResourceProperties(classLoader, properties, DEFAULT_PROPERTY_RESOURCE);
    }
}
