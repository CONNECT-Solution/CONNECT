/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
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
package gov.hhs.fha.nhinc.notify.adapter.proxy;

import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * An object factory that uses the Spring Framework to create service implementation objects. The configuration file is
 * referenced in the common properties file location to assist in installation and configuration simplicity.
 * 
 * The Spring configuration file is defined by the constant "SPRING_CONFIG_FILE". This file is loaded into an
 * application context in the static initializer and then the objects defined in the config file are available to the
 * framework for creation. This configuration file can be located anywhere in the classpath.
 * 
 * To retrieve an object that is created by the framework, the "getBean(String beanId)" method is called on the
 * application context passing in the beanId that is specified in the config file. Considering the default correlation
 * definition in the config file for this component: <bean id="hiemnotifyadapter"
 * class="gov.hhs.fha.nhinc.hiemadapter.proxy.notify.HiemNotifyAdapterNoOpImpl"/> the bean id is "hiemnotifyadapter" and
 * an object of this type can be retrieved from the application context by calling the getBean method like:
 * context.getBean("hiemnotifyadapter");. This returns an object that can be casted to the appropriate interface and
 * then used in the application code. See the getHiemNotifyAdapterProxy() method in this class.
 * 
 * @author Jon Hoppesch
 */
public class HiemNotifyAdapterProxyObjectFactory {

    private static final String CONFIG_FILE_NAME = "hiemAdapterConfig.xml";
    private static final String BEAN_NAME_AUDIT_REPOSITORY = "hiemnotifyadapter";
    private static ApplicationContext context = null;

    static {
        context = new FileSystemXmlApplicationContext(PropertyAccessor.getInstance().getPropertyFileURL() + CONFIG_FILE_NAME);
    }

    /**
     * Retrieve an adapter audit query implementation using the IOC framework. This method retrieves the object from the
     * framework that has an identifier of "hiemsubscribeadapter."
     * 
     * @return HiemNotifyAdapterProxy instance
     */
    public HiemNotifyAdapterProxy getHiemNotifyAdapterProxy() {
        HiemNotifyAdapterProxy adapterHiemNotify = null;
        if (context != null) {
            adapterHiemNotify = (HiemNotifyAdapterProxy) context.getBean(BEAN_NAME_AUDIT_REPOSITORY);
        }
        return adapterHiemNotify;
    }

}
