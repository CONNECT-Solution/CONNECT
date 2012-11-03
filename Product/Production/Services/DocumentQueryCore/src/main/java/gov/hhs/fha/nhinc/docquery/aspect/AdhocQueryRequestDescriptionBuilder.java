/**
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
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
package gov.hhs.fha.nhinc.docquery.aspect;

import gov.hhs.fha.nhinc.event.BaseEventDescriptionBuilder;

import java.util.Arrays;

import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.common.collect.ImmutableList;

public class AdhocQueryRequestDescriptionBuilder extends BaseEventDescriptionBuilder {

    private static final Log LOG = LogFactory.getLog(AdhocQueryRequestDescriptionBuilder.class);
    private AdhocQueryRequest request;

    public AdhocQueryRequestDescriptionBuilder(AdhocQueryRequest request) {
        this.request = request;
    }

    /**
     * Constructor for aspects. Response should be set via <code>setArguments</code>.
     * 
     * @see #setArguments(Object...)
     */
    public AdhocQueryRequestDescriptionBuilder() {
    }

    @Override
    public void buildTimeStamp() {
        // time stamp not available from request
    }

    @Override
    public void buildStatuses() {
        // status not a relevant field for requests

    }

    @Override
    public void buildRespondingHCIDs() {
        // responding HCID not relevant for request object
    }

    @Override
    public void buildPayloadTypes() {
        if (request != null) {
            setPayLoadTypes(ImmutableList.of(request.getClass().getSimpleName()));
        }
    }

    @Override
    public void buildPayloadSize() {
        // payload size not available in request

    }

    @Override
    public void buildNPI() {
        // NPI not available in request

    }

    @Override
    public void buildInitiatingHCID() {
        // initiating HCID not available in request

    }

    @Override
    public void buildErrorCodes() {
        // error codes not available in request
    }

    @Override
    public void setArguments(Object... arguments) {
        if (arguments.length != 1 || !(arguments[0] instanceof AdhocQueryRequest)) {
            LOG.warn("Unexpected argument list: " + Arrays.toString(arguments));
        } else {
            request = (AdhocQueryRequest) arguments[0];
        }
    }

    /* (non-Javadoc)
     * @see gov.hhs.fha.nhinc.event.BaseEventDescriptionBuilder#setReturnValue(java.lang.Object)
     */
    @Override
    public void setReturnValue(Object returnValue) {
        // TODO Auto-generated method stub
        
    }
}
