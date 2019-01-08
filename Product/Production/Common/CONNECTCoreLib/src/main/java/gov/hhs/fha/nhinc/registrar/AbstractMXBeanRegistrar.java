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
package gov.hhs.fha.nhinc.registrar;

import gov.hhs.fha.nhinc.configuration.jmx.PassthruMXBeanRegistry;
import gov.hhs.fha.nhinc.configuration.jmx.WebServicesMXBean;
import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractMXBeanRegistrar {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractMXBeanRegistrar.class);

    protected  Set<WebServicesMXBean> beans = new HashSet<WebServicesMXBean>();

    /**
     * This method should be annotated with @PostConstruct in the concrete class as annotations are not inherited
     *
     * This method will register the beans from the PassthruBeanMXRegistry
     */
    public void init() {
        LOG.info("Constuction of {} service beans has begun...", this.getClass());
        beans = getWebServiceMXBean();
        for(WebServicesMXBean bean : beans) {
            LOG.info("Registering {} from {}", bean.getClass().getName(), this.getClass().getName());
            PassthruMXBeanRegistry.getInstance().registerWebServiceMXBean(bean);
        }

    }

    /**
     * This method should be annotated with @PreDestroy in the concrete class as annotations are not inherited
     *
     * This method will unregister the beans from the PassthruBeanMXRegistry
     */
    public void destroy() {
        LOG.info("Destruction of {} has begun...", this.getClass());
        for(WebServicesMXBean bean : beans) {
            if (PassthruMXBeanRegistry.getInstance().unregisterBean(bean)) {
                LOG.info("Unregistering {} from destruction of {} bean", bean.getClass().getName(), this.getClass().getName());
            } else {
                LOG.error("Could not remove MX Bean {}! Was it not registered in the creation of {}?" ,bean.getClass().getName(), this.getClass().getName());
            }

        }

    }

    public abstract Set<WebServicesMXBean> getWebServiceMXBean();
}
