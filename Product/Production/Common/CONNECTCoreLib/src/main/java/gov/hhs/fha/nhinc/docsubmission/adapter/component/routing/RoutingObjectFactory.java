/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docsubmission.adapter.component.routing;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
/**
 *
 * @author dunnek
 */
public class RoutingObjectFactory {
    private static final String CONFIG_FILE_NAME = "NhinXDRRoutingProxyConfig.xml";

    public static final String BEAN_REFERENCE_IMPLEMENTATION = "reference";

    private static ApplicationContext context = null;

    static {
        context = new FileSystemXmlApplicationContext(PropertyAccessor.getPropertyFileURL() + CONFIG_FILE_NAME);
    }
    public XDRRouting getNhinXDRRouting(String beanName) {
        XDRRouting proxy = null;
        if (context != null) {
            proxy = (XDRRouting) context.getBean(beanName);
        }
        return proxy;
    }
}
