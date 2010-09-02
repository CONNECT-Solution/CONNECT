/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.mpi.adapter.proxy;

import gov.hhs.fha.nhinc.proxy.ComponentProxyObjectFactory;

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
 * <bean id="mpi" class="gov.hhs.fha.nhinc.mpi.proxy.AdapterMpiProxyNoOpImpl"/>
 * the bean id is "mpi" and an object of this type can be retrieved from
 * the application context by calling the getBean method like:
 * context.getBean("mpi");. This returns an object that can be casted to
 * the appropriate interface and then used in the application code. See the
 * getAdapterMpiProxy() method in this class.
 *
 * @author Les Westberg
 */
public class AdapterMpiProxyObjectFactory extends ComponentProxyObjectFactory
{
    private static final String CONFIG_FILE_NAME = "AdapterMpiProxyConfig.xml";
    private static final String BEAN_NAME_MPI = "adaptermpi";

    /**
     * Returns the name of the config file.
     *
     * @return The name of the config file.
     */
    protected String getConfigFileName()
    {
        return CONFIG_FILE_NAME;
    }

    /**
     * Return an instance of the AdapterComponentMpiProxy class.
     *
     * @return An instance of the AdapterComponentMpiProxy class.
     */
    public AdapterMpiProxy getAdapterMpiProxy()
    {
        return getBean(BEAN_NAME_MPI, AdapterMpiProxy.class);
    }

}
