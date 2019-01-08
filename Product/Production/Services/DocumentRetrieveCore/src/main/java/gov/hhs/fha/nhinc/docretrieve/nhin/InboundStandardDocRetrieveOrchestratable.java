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
package gov.hhs.fha.nhinc.docretrieve.nhin;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.orchestration.AbstractStandardOrchestratable;
import gov.hhs.fha.nhinc.orchestration.Delegate;
import gov.hhs.fha.nhinc.orchestration.InboundDelegate;
import gov.hhs.fha.nhinc.orchestration.PolicyTransformer;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import java.util.Properties;

/**
 *
 * @author bhumphrey
 *
 */
public class InboundStandardDocRetrieveOrchestratable extends AbstractStandardOrchestratable implements
    InboundDocRetrieveOrchestratable {

    private InboundDelegate inboundDelegate;
    private final String serviceName = "NhinDocumentRetrieve_g0";
    private RetrieveDocumentSetRequestType request;
    private RetrieveDocumentSetResponseType response;
    private AssertionType assertion;
    private Properties webContextProperties;

    /**
     * default constructor.
     */
    public InboundStandardDocRetrieveOrchestratable() {
        super();
        inboundDelegate = null;
    }

    /**
     * Injectable constructor.
     *
     * @param pt policy transformer
     * @param id inbound delegate
     */
    public InboundStandardDocRetrieveOrchestratable(PolicyTransformer pt, InboundDelegate id) {
        super(pt);
        inboundDelegate = id;
    }

    /**
     * getter for delegate.
     */
    @Override
    public Delegate getDelegate() {
        return getAdapterDelegate();
    }

    /**
     * getter for service name.
     */
    @Override
    public String getServiceName() {
        return serviceName;
    }

    /**
     * getter for adapter delegate.
     */
    @Override
    public InboundDelegate getAdapterDelegate() {
        return inboundDelegate;
    }

    @Override
    public RetrieveDocumentSetRequestType getRequest() {

        return request;
    }

    @Override
    public RetrieveDocumentSetResponseType getResponse() {
        return response;
    }

    /**
     * @return the assertion
     */
    @Override
    public AssertionType getAssertion() {
        return assertion;
    }

    /**
     * @param assertion the assertion to set
     */
    @Override
    public void setAssertion(AssertionType assertion) {
        this.assertion = assertion;
    }

    /**
     * @param request the request to set
     */
    @Override
    public void setRequest(RetrieveDocumentSetRequestType request) {
        this.request = request;
    }

    /**
     * @param response the response to set
     */
    @Override
    public void setResponse(RetrieveDocumentSetResponseType response) {
        this.response = response;
    }

    @Override
    public Properties getWebContextProperties() {
        return webContextProperties;
    }

    @Override
    public void setWebContextProperties(Properties webContextProperties) {
        this.webContextProperties = webContextProperties;
    }

}
