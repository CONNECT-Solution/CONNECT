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
package gov.hhs.fha.nhinc.docsubmission._20.gateway;

import gov.hhs.fha.nhinc.configuration.jmx.WebServicesMXBean;
import gov.hhs.fha.nhinc.docsubmission.configuration.jmx.DocumentSubmission20WebServices;
import gov.hhs.fha.nhinc.docsubmission.configuration.jmx.DocumentSubmissionDefRequest20WebServices;
import gov.hhs.fha.nhinc.docsubmission.configuration.jmx.DocumentSubmissionDefResponse20WebServices;
import gov.hhs.fha.nhinc.registrar.AbstractMXBeanRegistrar;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ImportResource;
import org.springframework.stereotype.Component;

/**
 * The Class InitServlet.
 *
 * @author msw
 *
 */
@Component
@ImportResource({ "classpath:/docsubmission/_20/applicationContext.xml" })
public class InitServlet  extends AbstractMXBeanRegistrar {

    @Autowired
    DocumentSubmission20WebServices docSubmission20;

    @Autowired
    DocumentSubmissionDefRequest20WebServices docSubmission20Request;

    @Autowired
    DocumentSubmissionDefResponse20WebServices docSubmission20Response;

    @Override
    @PostConstruct
    public void init() {
        super.init();
    }

    @Override
    @PreDestroy
    public void destroy() {
        super.destroy();
    }

    @Override
    public Set<WebServicesMXBean> getWebServiceMXBean() {
        Set<WebServicesMXBean> newBeans = new HashSet<>();
        newBeans.add(docSubmission20);
        newBeans.add(docSubmission20Request);
        newBeans.add(docSubmission20Response);
        return newBeans;
    }
}
