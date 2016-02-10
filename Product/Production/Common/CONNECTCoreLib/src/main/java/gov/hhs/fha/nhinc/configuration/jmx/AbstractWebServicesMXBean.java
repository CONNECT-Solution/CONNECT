/*
 * Copyright (c) 2009-2016, United States Government, as represented by the Secretary of Health and Human Services.
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
import org.apache.commons.lang.StringUtils;
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
     * Gets the Standard Outbound OrchImpl bean name.
     *
     * @return the Standard Outbound OrchImpl interface bean name
     */
    protected abstract String getStandardOutboundBeanName();

    /**
     * Gets the Passthrough Outbound OrchImpl bean name.
     *
     * @return the Passthrough Outbound OrchImpl interface bean name
     */
    protected abstract String getPassthroughOutboundBeanName();

    /**
     * Gets the Standard Inbound OrchImpl bean name.
     *
     * @return the Standard Outbound OrchImpl interface bean name
     */
    protected abstract String getStandardInboundBeanName();

    /**
     * Gets the Passthrough Inbound OrchImpl bean name.
     *
     * @return the Passthrough Inbound OrchImpl interface bean name
     */
    protected abstract String getPassthroughInboundBeanName();

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

        return beanType.cast(webApplicationContext.getBean(beanName));
    }

    /**
     * Configure inbound Standard implementation. This method is abstract because subclass implementations must use
     * actual types as opposed to the type parameters use in {@link #retrieveBean(Class, String)} and
     *
     * @param className the class name
     * @throws InstantiationException the instantiation exception
     * @throws IllegalAccessException the illegal access exception
     * @throws ClassNotFoundException the class not found exception {@link #retrieveDependency(Class, String)}.
     */
    @Override
    public abstract void configureInboundStdImpl()
            throws InstantiationException, IllegalAccessException, ClassNotFoundException;

    /**
     * Configure inbound Passthrough implementation. This method is abstract because subclass implementations must use
     * actual types as opposed to the type parameters use in {@link #retrieveBean(Class, String)} and
     *
     * @param className the class name
     * @throws InstantiationException the instantiation exception
     * @throws IllegalAccessException the illegal access exception
     * @throws ClassNotFoundException the class not found exception {@link #retrieveDependency(Class, String)}.
     */
    @Override
    public abstract void configureInboundPtImpl()
            throws InstantiationException, IllegalAccessException, ClassNotFoundException;

    /**
     * Configure outbound Standard implementation. This method is abstract because subclass implementations must use
     * actual types as opposed to the type parameters use in {@link #retrieveBean(Class, String)} and
     *
     * @param className the class name
     * @throws InstantiationException the instantiation exception
     * @throws IllegalAccessException the illegal access exception
     * @throws ClassNotFoundException the class not found exception {@link #retrieveDependency(Class, String)}.
     */
    @Override
    public abstract void configureOutboundStdImpl()
            throws InstantiationException, IllegalAccessException, ClassNotFoundException;

    /**
     * Configure outbound Passthrough implementation. This method is abstract because subclass implementations must use
     * actual types as opposed to the type parameters use in {@link #retrieveBean(Class, String)} and
     *
     * @param className the class name
     * @throws InstantiationException the instantiation exception
     * @throws IllegalAccessException the illegal access exception
     * @throws ClassNotFoundException the class not found exception {@link #retrieveDependency(Class, String)}.
     */
    @Override
    public abstract void configureOutboundPtImpl()
            throws InstantiationException, IllegalAccessException, ClassNotFoundException;

    /**
     * Compares the class name of an object vs the class name passed in.
     *
     * @param clazz the Object
     * @param className the class name
     *
     * @return true if the clazz object class name and className match.
     */
    protected boolean compareClassName(Object clazz, String className) {
        boolean matches = false;
        if (clazz != null && clazz.getClass() != null
                && StringUtils.startsWith(clazz.getClass().getName(), className)) {
            matches = true;
        }
        return matches;
    }

}