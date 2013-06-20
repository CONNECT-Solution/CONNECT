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
package gov.hhs.fha.nhinc.configuration;

import gov.hhs.fha.nhinc.configuration.jmx.Configuration;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;

import java.lang.management.ManagementFactory;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/**
 * The Class InitServlet.
 * 
 * @author msw
 */
public class InitServlet extends HttpServlet {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 7738036165392946446L;

    /**
     * Init method for starting up the Configuration MBean.
     * 
     * @see javax.servlet.GenericServlet#init()
     */
    @Override
    public void init() throws ServletException {
        super.init();

        String enableJMX = System.getProperty(NhincConstants.JMX_ENABLED_SYSTEM_PROPERTY);
        if ("true".equalsIgnoreCase(enableJMX)) {
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
            ObjectName name = null;
            try {
                name = new ObjectName(NhincConstants.JMX_CONFIGURATION_BEAN_NAME);
                Configuration mbean = new Configuration();
                mbs.registerMBean(mbean, name);
            } catch (MalformedObjectNameException e) {
                e.printStackTrace();
                throw new ServletException(e);
            } catch (InstanceAlreadyExistsException e) {
                e.printStackTrace();
                throw new ServletException(e);
            } catch (MBeanRegistrationException e) {
                e.printStackTrace();
                throw new ServletException(e);
            } catch (NotCompliantMBeanException e) {
                e.printStackTrace();
                throw new ServletException(e);
            }
        }
    }
}
