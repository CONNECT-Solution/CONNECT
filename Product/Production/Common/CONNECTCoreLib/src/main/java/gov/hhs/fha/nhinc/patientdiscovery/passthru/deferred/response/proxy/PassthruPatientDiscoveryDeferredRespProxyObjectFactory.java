/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.patientdiscovery.passthru.deferred.response.proxy;

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
 * <bean id="passthrupatientdiscoveryasyncresp" class="gov.hhs.fha.nhinc.passthru.patientdiscovery.async.response.proxy.PassthruPatientDiscoveryAsyncRespNoOpImpl"/>
 * the bean id is "passthrupatientdiscoveryasyncresp" and an object of this type can be retrieved from
 * the application context by calling the getBean method like:
 * context.getBean("passthrupatientdiscoveryasyncresp");. This returns an object that can be casted to
 * the appropriate interface and then used in the application code. See the
 * getPassthruPatientDisocoveryAsyncReapProxy() method in this class.
 *
 * @author Jon Hoppesch
 */
public class PassthruPatientDiscoveryDeferredRespProxyObjectFactory extends ComponentProxyObjectFactory
{

    private static final String CONFIG_FILE_NAME = "PassthruPatientDiscoveryDeferredRespProxyConfig.xml";
    private static final String BEAN_NAME = "passthrupatientdiscoverydeferredresp";

    protected String getConfigFileName()
    {
        return CONFIG_FILE_NAME;
    }

    /**
     * Retrieve a entity patient discovery implementation using the IOC framework.
     * This method retrieves the object from the framework that has an
     * identifier of "passthrupatientdiscoverydeferredresp."
     *
     * @return PassthruPatientDiscoveryAsyncRespProxy instance
     */
    public PassthruPatientDiscoveryDeferredRespProxy getPassthruPatientDiscoveryDeferredRespProxy()
    {
        return getBean(BEAN_NAME, PassthruPatientDiscoveryDeferredRespProxy.class);
    }
}
