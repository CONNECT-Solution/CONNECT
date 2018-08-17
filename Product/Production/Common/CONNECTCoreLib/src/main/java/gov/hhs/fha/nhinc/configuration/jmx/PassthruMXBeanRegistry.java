/*
 * Copyright (c) 2009-2018, United States Government, as represented by the Secretary of Health and Human Services.
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

import gov.hhs.fha.nhinc.configuration.IConfiguration.directionEnum;
import gov.hhs.fha.nhinc.configuration.IConfiguration.serviceEnum;
import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

/**
 * The Singleton Class PassthruMXBeanRegistry.
 *
 * @author msw
 */
public class PassthruMXBeanRegistry {

    Logger LOG = LoggerFactory.getLogger(PassthruMXBeanRegistry.class);

    /** The instance. */
    private static PassthruMXBeanRegistry instance;

    /**
     * Package level constructor instantiates a new passthru mx bean registry.
     */
    PassthruMXBeanRegistry() {
        registeredBeans = new HashSet<>();
    }

    /** The registered beans. */
    protected Set<WebServicesMXBean> registeredBeans = null;

    /**
     * Gets the single instance of PassthruMXBeanRegistry.
     *
     * @return single instance of PassthruMXBeanRegistry
     */
    public static PassthruMXBeanRegistry getInstance() {
        if (instance == null) {
            instance = new PassthruMXBeanRegistry();
        }
        return instance;
    }

    /**
     * Register web service mx bean.
     *
     * @param bean the bean
     */
    public void registerWebServiceMXBean(WebServicesMXBean bean) {
        registeredBeans.add(bean);
    }

    public boolean unregisterBean(WebServicesMXBean bean) {
        return registeredBeans.remove(bean);
    }


    /**
     * Sets the passthru mode.
     *
     * @throws InstantiationException the instantiation exception
     * @throws IllegalAccessException the illegal access exception
     * @throws ClassNotFoundException the class not found exception
     */
    public void setPassthruMode() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        for (WebServicesMXBean b : registeredBeans) {
            LOG.debug("Configuring Bean Registry to Passthrough.");
            try {
                b.configureInboundPtImpl();
                b.configureOutboundPtImpl();
            } catch (NoSuchBeanDefinitionException e) {
                LOG.debug("Couldn't configure bean {}", b.getClass(), e);
            }

        }
    }

    /**
     * Sets the standard mode.
     *
     * @throws InstantiationException the instantiation exception
     * @throws IllegalAccessException the illegal access exception
     * @throws ClassNotFoundException the class not found exception
     */
    public void setStandardMode() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        LOG.debug("Configuring Bean Registry to Standard.");
        for (WebServicesMXBean b : registeredBeans) {
            try {
                b.configureInboundStdImpl();
                b.configureOutboundStdImpl();
            } catch (NoSuchBeanDefinitionException e) {
                LOG.debug("Couldn't configure bean {}", b.getClass(), e);
            }
        }
    }

    /**
     * @param serviceName
     * @param direction
     */
    public void setPassthruMode(serviceEnum serviceName, directionEnum direction)
            throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        for (WebServicesMXBean b : registeredBeans) {
            if (direction.toString().equals("Inbound") && b.getServiceName().equals(serviceName)) {
                b.configureInboundPtImpl();
            }
            if (direction.toString().equals("Outbound") && b.getServiceName().equals(serviceName)) {
                b.configureOutboundPtImpl();
            }
        }
    }

    /**
     * @param serviceName
     * @param direction
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public void setStandardMode(serviceEnum serviceName, directionEnum direction)
            throws InstantiationException, IllegalAccessException, ClassNotFoundException {

        for (WebServicesMXBean b : registeredBeans) {
            if (direction.toString().equals("Inbound") && b.getServiceName().equals(serviceName)) {
                b.configureInboundStdImpl();
            }
            if (direction.toString().equals("Outbound") && b.getServiceName().equals(serviceName)) {
                b.configureOutboundStdImpl();
            }
        }
    }

    /**
     * @param serviceName
     * @param direction
     */
    public boolean isPassthru(serviceEnum serviceName, directionEnum direction) {
        boolean passthruMode = false;
        for (WebServicesMXBean b : registeredBeans) {
            if (direction.toString().equals("Inbound") && b.getServiceName().equals(serviceName)
                    && b.isInboundPassthru()) {
                passthruMode = true;
            }
            if (direction.toString().equals("Outbound") && b.getServiceName().equals(serviceName)
                    && b.isOutboundPassthru()) {
                passthruMode = true;
            }
        }
        return passthruMode;
    }

    public boolean isStandard(serviceEnum serviceName, directionEnum direction) {
        boolean standardMode = false;
        for (WebServicesMXBean b : registeredBeans) {
            if (direction.toString().equals("Outbound") && b.getServiceName().equals(serviceName)
                    && b.isOutboundStandard()) {
                standardMode = true;
            }
            if (direction.toString().equals("Inbound") && b.getServiceName().equals(serviceName)
                    && b.isInboundStandard()) {
                standardMode = true;
            }
        }
        return standardMode;
    }



}
