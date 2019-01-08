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
package gov.hhs.fha.nhinc.patientdiscovery._10.servlet;

import gov.hhs.fha.nhinc.configuration.jmx.WebServicesMXBean;
import gov.hhs.fha.nhinc.gateway.executorservice.ExecutorServiceHelper;
import gov.hhs.fha.nhinc.patientdiscovery.configuration.jmx.PatientDiscovery10WebServices;
import gov.hhs.fha.nhinc.patientdiscovery.configuration.jmx.PatientDiscoveryDeferredReq10WebServices;
import gov.hhs.fha.nhinc.patientdiscovery.configuration.jmx.PatientDiscoveryDeferredResp10WebServices;
import gov.hhs.fha.nhinc.registrar.AbstractMXBeanRegistrar;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ImportResource;
import org.springframework.stereotype.Component;

/**
 * Started on spring init, creates the main ExecutorService and CamelContext instances Note the following: 1.
 * Main ExecutorService creates a new thread pool of size specified on construction, independent/in addition to
 * glassfish thread pool(s) set in domain.xml. 2. ExecutorService automatically handles any thread death condition and
 * creates a new thread in this case
 *
 * 3. Also creates a second largeJobExecutor with a fixed size thread pool (largeJobExecutor is used for TaskExecutors
 * that get a callable list of size comparable to the size of the main ExecutorService).
 *
 * 4. See {@link gov.fha.hhs.nhinc.gateway.AbstractJMXEnabledServlet} for JMX init and destroy functionality.
 *
 * @author paul.eftis, msw
 */

@Component
@ImportResource({ "classpath:/patientdiscovery/_10/applicationContext.xml" })
public class InitServlet extends AbstractMXBeanRegistrar {

    private static final Logger LOG = LoggerFactory.getLogger(InitServlet.class);
    private static ExecutorService executor = null;
    private static ExecutorService largeJobExecutor = null;

    @Autowired
    PatientDiscovery10WebServices patientDiscovery10;

    @Autowired
    PatientDiscoveryDeferredReq10WebServices patientDiscovery10Request;

    @Autowired
    PatientDiscoveryDeferredResp10WebServices patientDiscovery10Response;

    @PostConstruct
    @Override
    public void init() {
        LOG.info("PatientDiscovery InitServlet starting...");
        ExecutorService newExecutor = Executors.newFixedThreadPool(ExecutorServiceHelper.getInstance().getExecutorPoolSize());
        setExecutorService(newExecutor);
        ExecutorService newLargeJobExecutor = Executors.newFixedThreadPool(ExecutorServiceHelper.getInstance().getLargeJobExecutorPoolSize());
        setLargeJobExecutorService(newLargeJobExecutor);
        super.init();

    }

    public static ExecutorService getExecutorService() {
        return executor;
    }

    public static void setExecutorService(ExecutorService newExecutor) {
        executor = newExecutor;
    }

    public static ExecutorService getLargeJobExecutorService() {
        return largeJobExecutor;
    }

    public static void setLargeJobExecutorService(ExecutorService newLargeJobExecutor) {
        largeJobExecutor = newLargeJobExecutor;
    }

    @Override
    @PreDestroy
    public void destroy() {
        LOG.info("Shutting down executors...");
        if (executor != null) {
            try {
                executor.shutdown();
            } catch (Exception e) {
                LOG.error("Error shutting down executor.", e);
            }
        }
        if (largeJobExecutor != null) {
            try {
                largeJobExecutor.shutdown();
            } catch (Exception e) {
                LOG.error("Error shutting down large job executor.", e);
            }
        }

        super.destroy();

    }

    @Override
    public Set<WebServicesMXBean> getWebServiceMXBean() {
        Set<WebServicesMXBean> newBeans = new HashSet<>();
        newBeans.add(patientDiscovery10);
        newBeans.add(patientDiscovery10Request);
        newBeans.add(patientDiscovery10Response);
        return newBeans;
    }

}
