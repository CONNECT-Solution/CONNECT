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
package gov.hhs.fha.nhinc.gateway.servlet;

import gov.hhs.fha.nhinc.configuration.jmx.AbstractPassthruRegistryEnabledServlet;
import gov.hhs.fha.nhinc.configuration.jmx.WebServicesMXBean;
import gov.hhs.fha.nhinc.docquery.configuration.jmx.DocumentQuery20WebServices;
import gov.hhs.fha.nhinc.gateway.executorservice.ExecutorServiceHelper;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.apache.log4j.Logger;

/**
 * Started on webapplication init, creates the main ExecutorService and CamelContext instances Note the following: 1.
 * Main ExecutorService creates a new thread pool of size specified on construction, independent/in addition to
 * glassfish thread pool(s) set in domain.xml. 2. ExecutorService automatically handles any thread death condition and
 * creates a new thread in this case
 * 
 * 3. Also creates a second largeJobExecutor with a fixed size thread pool (largeJobExecutor is used for TaskExecutors
 * that get a callable list of size comparable to the size of the main ExecutorService)
 * 
 * 4. See {@link gov.hhs.fha.nhinc.gateway.AbstractJMXEnabledServlet} for JMX related super class.
 * 
 * @author paul.eftis, msw
 */
public class InitServlet extends AbstractPassthruRegistryEnabledServlet {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -4229185731377926278L;

    /** The Constant LOG. */
    private static final Logger LOG = Logger.getLogger(InitServlet.class);

    /** The executor. */
    private static ExecutorService executor = null;

    /** The large job executor. */
    private static ExecutorService largeJobExecutor = null;

    /**
     * Inits the servlet, first initializes the parallel fanout executors, then calls super.init().
     * 
     * @param config the config
     * @throws ServletException the servlet exception
     * @see gov.hhs.fha.nhinc.gateway.AbstractJMXEnabledServlet#init(javax.servlet.ServletConfig)
     */
    @Override
    @SuppressWarnings("static-access")
    public void init(ServletConfig config) throws ServletException {
        LOG.debug("InitServlet start...");
        executor = Executors.newFixedThreadPool(ExecutorServiceHelper.getInstance().getExecutorPoolSize());
        largeJobExecutor = Executors.newFixedThreadPool(ExecutorServiceHelper.getInstance()
                .getLargeJobExecutorPoolSize());

        super.init(config);
    }

    /**
     * Gets the executor service.
     * 
     * @return the executor service
     */
    public static ExecutorService getExecutorService() {
        return executor;
    }

    /**
     * Gets the large job executor service.
     * 
     * @return the large job executor service
     */
    public static ExecutorService getLargeJobExecutorService() {
        return largeJobExecutor;
    }

    /**
     * Destroys the servlet. First destroys the parallel fanout executors, then calls super.destroy(). Since we don't
     * want to halt the servlet from destroying, no exceptions are propagated through this method.
     * 
     * @see gov.hhs.fha.nhinc.gateway.AbstractJMXEnabledServlet#destroy()
     */
    @Override
    public void destroy() {
        LOG.debug("InitServlet shutdown stopping executor(s)....");
        if (executor != null) {
            try {
                executor.shutdown();
            } catch (Exception e) {
                LOG.error("Error stopping executor.", e);
            }
        }
        if (largeJobExecutor != null) {
            try {
                largeJobExecutor.shutdown();
            } catch (Exception e) {
                LOG.error("Error stopping large job executor.", e);
            }
        }

        super.destroy();
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.configuration.jmx.AbstractPassthruRegistryEnabledServlet#getWebServiceMXBean()
     */
    @Override
    public Set<WebServicesMXBean> getWebServiceMXBean(ServletContext sc) {
        WebServicesMXBean bean = new DocumentQuery20WebServices(sc);
        return Collections.singleton(bean);
    }

}
