/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.adapterauthentication.proxy;

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
 * <bean id="adapterauthentication" class="gov.hhs.fha.nhinc.adapterauthentication.proxy.AdapterAuthenticationNoOpImpl"/>
 * the bean id is "adapterauthentication" and an object of this type can be retrieved from
 * the application context by calling the getBean method like:
 * context.getBean("adapterauthentication");. This returns an object that can be casted to
 * the appropriate interface and then used in the application code.
 *
 */
public class AdapterAuthenticationProxyObjectFactory
{
    private static final String SPRING_CONFIG_FILE = "AdapterAuthenticationProxyConfig.xml";
    private static final String BEAN_NAME_ADAPTER_AUTH = "adapterauthentication";

    private static ApplicationContext context = null;

    static
    {
        String configFile = PropertyAccessor.getPropertyFileURL();

        context = new FileSystemXmlApplicationContext(configFile + SPRING_CONFIG_FILE);
    }

    /**
     * Retrieve an adapter authentication implementation using the IOC framework.
     * This method retrieves the object from the framework that has an
     * identifier of "adapterauthentication."
     *
     * @return AdapterAuthenticationProxy instance
     */
    public AdapterAuthenticationProxy getAdapterAuthenticationProxy()
    {
        AdapterAuthenticationProxy adapterProxy = null;
        if(context != null)
        {
            adapterProxy = (AdapterAuthenticationProxy)context.getBean(BEAN_NAME_ADAPTER_AUTH);
        }
        return adapterProxy;
    }
}
