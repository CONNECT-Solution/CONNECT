/*
 * Copyright (c) 2009-2019, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.gateway;

import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import java.lang.management.ManagementFactory;
import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class AbstractJMXEnabledServlet.
 *
 * @author msw
 */
public abstract class AbstractJMXEnabledServlet {

    /** The Constant LOG. */
    private static final Logger LOG = LoggerFactory.getLogger(AbstractJMXEnabledServlet.class);

    /** The Constant UNABLE_TO_REGISTER_MBEAN_MSG. */
    private static final String UNABLE_TO_REGISTER_MBEAN_MSG = "Unable to register MBean: ";

    /** The Constant UNABLE_TO_DESTORY_MBEAN_MSG. */
    private static final String UNABLE_TO_DESTORY_MBEAN_MSG = "Unable to unregister MBean: ";

    /**
     * Gets the m bean name.
     *
     * @return the m bean name
     */
    public abstract String getMBeanName();

    /**
     * Gets the m bean instance.
     *
     * @param sc the sc
     * @return the m bean instance
     */
    public abstract Object getMBeanInstance();

    /**
     * Registers the MBean with the MBeanServer if JMX is enabled for CONNECT
     */
    public void init() {
        String enableJMX = System.getProperty(NhincConstants.JMX_ENABLED_SYSTEM_PROPERTY);
        if ("true".equalsIgnoreCase(enableJMX)) {
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();

            ObjectName name;
            try {
                name = new ObjectName(getMBeanName());

                if (!mbs.isRegistered(name)) {
                    Object mbean = getMBeanInstance();
                    mbs.registerMBean(mbean, name);
                }
            } catch ( MBeanRegistrationException | NotCompliantMBeanException
                | MalformedObjectNameException e) {
                LOG.error(getRegistrationErrorMessage(), e);
            }
            catch (InstanceAlreadyExistsException e) {
                // This should never happen as we check if the bean is registered before we attempt to register it.
                LOG.warn("Tried to register an already existing MBean: {}", getMBeanName(), e);
            }
        }

    }

    /**
     * Unregisters previously registered MBeans if JMX is enabled for CONNECT
     */
    public void destroy() {
        String enableJMX = System.getProperty(NhincConstants.JMX_ENABLED_SYSTEM_PROPERTY);
        if ("true".equalsIgnoreCase(enableJMX)) {
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
            ObjectName name;
            try {
                name = new ObjectName(getMBeanName());
                mbs.unregisterMBean(name);
            } catch (InstanceNotFoundException | MalformedObjectNameException | MBeanRegistrationException e) {
                LOG.error(getDestroyErrorMessage(), e);
            }
        }

    }

    /**
     * Gets the error message.
     *
     * @return the error message
     */
    private String getRegistrationErrorMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append(UNABLE_TO_REGISTER_MBEAN_MSG);
        sb.append(getMBeanName());
        return sb.toString();
    }

    /**
     * Gets the error message.
     *
     * @return the error message
     */
    private String getDestroyErrorMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append(UNABLE_TO_DESTORY_MBEAN_MSG);
        sb.append(getMBeanName());
        return sb.toString();
    }
}
