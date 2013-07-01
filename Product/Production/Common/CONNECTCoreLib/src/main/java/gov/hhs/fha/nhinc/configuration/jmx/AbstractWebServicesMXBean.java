/*
 * Copyright (c) 2009-2013, United States Government, as represented by the Secretary of Health and Human Services. 
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
package gov.hhs.fha.nhinc.configuration.jmx;

import javax.servlet.ServletContext;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * The Class AbstractWebServicesMXBean. This abstract class provides some common methods for retrieving beans and
 * dependencies as well as some methods which must be overriden to provide bean names.
 * 
 * @author msw
 */
public abstract class AbstractWebServicesMXBean implements WebServicesMXBean {

    /** The ServletContext. */
    private ServletContext sc;

    /**
     * Instantiates a new abstract web services mx bean.
     * 
     * @param sc the ServletContext
     */
    public AbstractWebServicesMXBean(ServletContext sc) {
        this.sc = sc;
    }

    /**
     * Gets the Nhin interface bean name.
     * 
     * @return the Nhin interface bean name
     */
    protected abstract String getNhinBeanName();

    /**
     * Gets the entity unsecured interface bean name.
     * 
     * @return the entity unsecured interface bean name
     */
    protected abstract String getEntityUnsecuredBeanName();

    /**
     * Gets the entity secured interface bean name.
     * 
     * @return the entity secured interface bean name
     */
    protected abstract String getEntitySecuredBeanName();

    /**
     * Gets the inbound standard class name.
     * 
     * @return the inbound standard class name
     */
    protected abstract String getInboundStandardClassName();

    /**
     * Gets the inbound passthru class name.
     * 
     * @return the inbound passthru class name
     */
    protected abstract String getInboundPassthruClassName();

    /**
     * Gets the outbound standard class name.
     * 
     * @return the outbound standard class name
     */
    protected abstract String getOutboundStandardClassName();

    /**
     * Gets the outbound passthru class name.
     * 
     * @return the outbound passthru class name
     */
    protected abstract String getOutboundPassthruClassName();

    /**
     * Parameterized method for retrieving a bean based on the type and name. The bean is retrieved from the Spring
     * application context.
     * 
     * @param <T> the generic type
     * @param beanType the generic bean type
     * @param beanName the bean name
     * @return the t
     */
    protected <T> T retrieveBean(final Class<T> beanType, String beanName) {
        WebApplicationContext webApplicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(sc);

        T bean = beanType.cast(webApplicationContext.getBean(beanName));
        return bean;
    }

    /**
     * Parameterized method for retrieving a dependency on the type and name. The dependency class is instanciated from
     * the class loader.
     * 
     * @param <T> the generic type
     * @param dependencyType the generic dependency type
     * @param className the class name
     * @return the t
     * @throws InstantiationException the instantiation exception
     * @throws IllegalAccessException the illegal access exception
     * @throws ClassNotFoundException the class not found exception
     */
    protected <T> T retrieveDependency(final Class<T> dependencyType, String className) throws InstantiationException,
            IllegalAccessException, ClassNotFoundException {
        T dependency = dependencyType.cast(Class.forName(className).newInstance());
        return dependency;
    }

    /**
     * Configure inbound implementation. This method is abstract because subclass implementations must use actual types
     * as opposed to the type parameters use in {@link #retrieveBean(Class, String)} and
     * 
     * @param className the class name
     * @throws InstantiationException the instantiation exception
     * @throws IllegalAccessException the illegal access exception
     * @throws ClassNotFoundException the class not found exception {@link #retrieveDependency(Class, String)}.
     */
    public abstract void configureInboundImpl(String className) throws InstantiationException, IllegalAccessException,
            ClassNotFoundException;

    /**
     * Configure the inbound dependency with the inbound passthru implementation.
     *
     * @throws InstantiationException the instantiation exception
     * @throws IllegalAccessException the illegal access exception
     * @throws ClassNotFoundException the class not found exception
     * @see gov.hhs.fha.nhinc.configuration.jmx.WebServicesMXBean#configureInboundPassthru()
     */
    @Override
    public void configureInboundPassthru() throws InstantiationException, IllegalAccessException,
            ClassNotFoundException {
        configureInboundImpl(getInboundPassthruClassName());
    }

    /**
     * Configure the inbound dependency with the inbound standard implementation.
     *
     * @throws InstantiationException the instantiation exception
     * @throws IllegalAccessException the illegal access exception
     * @throws ClassNotFoundException the class not found exception
     * @see gov.hhs.fha.nhinc.configuration.jmx.WebServicesMXBean#configureInboundStd()
     */
    @Override
    public void configureInboundStd() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        configureInboundImpl(getInboundStandardClassName());
    }

    /**
     * Configure outbound implementation. This method is abstract because subclass implementations must use actual types
     * as opposed to the type parameters use in {@link #retrieveBean(Class, String)} and
     * 
     * @param className the class name
     * @throws InstantiationException the instantiation exception
     * @throws IllegalAccessException the illegal access exception
     * @throws ClassNotFoundException the class not found exception {@link #retrieveDependency(Class, String)}.
     */
    public abstract void configureOutboundImpl(String className) throws InstantiationException, IllegalAccessException,
            ClassNotFoundException;

    /**
     * Configure the outbound dependency with the outbound passthru implementation.
     * 
     * @throws InstantiationException the instantiation exception
     * @throws IllegalAccessException the illegal access exception
     * @throws ClassNotFoundException the class not found exception
     * @see gov.hhs.fha.nhinc.configuration.jmx.WebServicesMXBean#configureOutboundPassthru()
     */
    @Override
    public void configureOutboundPassthru() throws InstantiationException, IllegalAccessException,
            ClassNotFoundException {
        configureOutboundImpl(getOutboundPassthruClassName());
    }

    /**
     * Configure the outbound dependency with with outbound standard implementation.
     * 
     * @throws InstantiationException the instantiation exception
     * @throws IllegalAccessException the illegal access exception
     * @throws ClassNotFoundException the class not found exception
     * @see gov.hhs.fha.nhinc.configuration.jmx.WebServicesMXBean#configureOutboundStd()
     */
    @Override
    public void configureOutboundStd() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        configureOutboundImpl(getOutboundStandardClassName());
    }

}