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
package gov.hhs.fha.nhinc.docsubmission.adapter.component.deferred.response.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.docsubmission.adapter.component.deferred.response.AdapterComponentDocSubmissionResponseOrchImpl;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;

import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

import org.apache.log4j.Logger;

/**
 * This class is the java implementation of the AdapterPatientDiscovery component proxy.
 * 
 * @author Les westberg
 */
public class AdapterComponentDocSubmissionResponseProxyJavaImpl implements AdapterComponentDocSubmissionResponseProxy {

    private static final Logger LOG = Logger.getLogger(AdapterComponentDocSubmissionResponseProxyJavaImpl.class);

    /**
     * Receive document deferred document submission response.
     * 
     * @param body The doc submission response message.
     * @param assertion The assertion information.
     * @return The ACK
     */
    public XDRAcknowledgementType provideAndRegisterDocumentSetBResponse(RegistryResponseType body,
            AssertionType assertion) {
        LOG.trace("Entering AdapterComponentDocSubmissionResponseProxyJavaImpl.provideAndRegisterDocumentSetBResponse");
        AdapterComponentDocSubmissionResponseOrchImpl oOrchestrator = new AdapterComponentDocSubmissionResponseOrchImpl();
        LOG.trace("Leaving AdapterComponentDocSubmissionResponseProxyJavaImpl.provideAndRegisterDocumentSetBResponse");
        return oOrchestrator.provideAndRegisterDocumentSetBResponse(body, assertion);

    }
}
