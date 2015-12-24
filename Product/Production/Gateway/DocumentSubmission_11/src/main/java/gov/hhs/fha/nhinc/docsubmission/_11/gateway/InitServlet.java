/*
 * Copyright (c) 2009-2015, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.docsubmission._11.gateway;

import gov.hhs.fha.nhinc.configuration.jmx.AbstractPassthruRegistryEnabledServlet;
import gov.hhs.fha.nhinc.configuration.jmx.WebServicesMXBean;
import gov.hhs.fha.nhinc.docsubmission.configuration.jmx.DocumentSubmission11WebServices;
import gov.hhs.fha.nhinc.docsubmission.configuration.jmx.DocumentSubmissionDefRequest11WebServices;
import gov.hhs.fha.nhinc.docsubmission.configuration.jmx.DocumentSubmissionDefResponse11WebServices;
import java.util.HashSet;
import java.util.Set;
import javax.servlet.ServletContext;

/**
 * The Class InitServlet.
 *
 * @author msw
 *
 */
public class InitServlet extends AbstractPassthruRegistryEnabledServlet {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -331241203887741599L;

    /*
     * (non-Javadoc)
     *
     * @see
     * gov.hhs.fha.nhinc.configuration.jmx.AbstractPassthruRegistryEnabledServlet#getWebServiceMXBean(javax.servlet.
     * ServletContext)
     */
    @Override
    public Set<WebServicesMXBean> getWebServiceMXBean(ServletContext sc) {
        Set<WebServicesMXBean> beans = new HashSet<>();
        beans.add(new DocumentSubmission11WebServices(sc));
        beans.add(new DocumentSubmissionDefRequest11WebServices(sc));
        beans.add(new DocumentSubmissionDefResponse11WebServices(sc));
        return beans;
    }
}
