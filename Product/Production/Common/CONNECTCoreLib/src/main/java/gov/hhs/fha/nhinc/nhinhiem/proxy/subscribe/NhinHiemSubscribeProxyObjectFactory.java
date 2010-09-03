/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.nhinhiem.proxy.subscribe;

import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

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
 * <bean id="nhinhiemsubscribe" class="gov.hhs.fha.nhinc.nhinhiem.proxy.subscribe.NhinHiemSubscribeNoOpImpl"/>
 * the bean id is "nhinhiemsubscribe" and an object of this type can be retrieved from
 * the application context by calling the getBean method like:
 * context.getBean("nhinhiemsubscribe");. This returns an object that can be casted to
 * the appropriate interface and then used in the application code. See the
 * getNhinAuditQueryProxy() method in this class.
 *
 * @author Jon Hoppesch
 */
public class NhinHiemSubscribeProxyObjectFactory {

    private static final String SPRING_BEAN_NAME = "nhinhiemsubscribe";
    private static final String CONFIG_FILE_NAME = "NhinHiemProxyConfig.xml";

    private static ApplicationContext context = null;

    static {
        context = new FileSystemXmlApplicationContext(PropertyAccessor.getPropertyFileURL() + CONFIG_FILE_NAME);
    }

    /**
     * Retrieve a nhin audit query implementation using the IOC framework.
     * This method retrieves the object from the framework that has an
     * identifier of "nhinauditquery."
     *
     * @return NhinAuditQueryProxy instance
     */
    public NhinHiemSubscribeProxy getNhinHiemSubscribeProxy() {
        NhinHiemSubscribeProxy nhinHiemSubscribe = null;
        if (context != null) {
            nhinHiemSubscribe = (NhinHiemSubscribeProxy) context.getBean(SPRING_BEAN_NAME);
        }
        return nhinHiemSubscribe;
    }

}
