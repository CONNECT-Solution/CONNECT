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
package gov.hhs.fha.nhinc.docretrieve.nhin;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import gov.hhs.fha.nhinc.auditrepository.AuditRepositoryDocumentRetrieveLogger;
import gov.hhs.fha.nhinc.common.auditlog.DocRetrieveMessageType;
import gov.hhs.fha.nhinc.common.auditlog.DocRetrieveResponseMessageType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.docretrieve.adapter.proxy.AdapterDocRetrieveProxy;
import gov.hhs.fha.nhinc.docretrieve.audit.DocRetrieveAuditLogger;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.util.MessageGeneratorUtils;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import java.util.Properties;
import org.junit.Before;

import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;

/**
 *
 * @author mweaver
 */
public class AdapterDocRetrieveStrategyImpl_a0Test {

    InboundDocRetrieveOrchestratable message;

    AdapterDocRetrieveProxy adapterProxy;
    DocRetrieveAuditLogger logger;
    InboundDocRetrieveStrategyImpl instance;
    RetrieveDocumentSetRequestType retrieveDocumentSetRequestType;
    RetrieveDocumentSetResponseType retrieveDocumentSetResponseType;
    NhinTargetSystemType nhinTargetSystemType = null;
    AssertionType assertionType = null;

    @Before
    public void Setup() {

        // creating mocks for necessary arguments
        assertionType = mock(AssertionType.class);
        message = mock(InboundDocRetrieveOrchestratable.class);
        adapterProxy = mock(AdapterDocRetrieveProxy.class);
        logger = mock(DocRetrieveAuditLogger.class);
        retrieveDocumentSetRequestType = mock(RetrieveDocumentSetRequestType.class);
        retrieveDocumentSetResponseType = mock(RetrieveDocumentSetResponseType.class);

        instance = new InboundDocRetrieveStrategyImpl(adapterProxy, logger) {
            @Override
            protected NhinTargetSystemType getTargetNhinTargetSystemType(InboundDocRetrieveOrchestratable message) {
                return nhinTargetSystemType;
            }

        };

    }

    /**
     * Test of execute method, of class AdapterDocRetrieveStrategyImpl_a0.
     */
    @Test
    public void testExecute() {

        Properties webContextProp = null;
        when(message.getRequest()).thenReturn(retrieveDocumentSetRequestType);
        when(message.getAssertion()).thenReturn(assertionType);
        when(message.getResponse()).thenReturn(retrieveDocumentSetResponseType);

        instance.execute(message);

        verify(logger).auditRequestMessage(eq(retrieveDocumentSetRequestType), eq(assertionType), eq(nhinTargetSystemType), eq(NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION), eq(NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE), eq(Boolean.FALSE), eq(webContextProp), eq("RetrieveDocuments"));

        verify(logger).auditResponseMessage(eq(retrieveDocumentSetRequestType), eq(retrieveDocumentSetResponseType), eq(assertionType), eq(nhinTargetSystemType), eq(NhincConstants.AUDIT_LOG_INBOUND_DIRECTION), eq(NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE), eq(Boolean.FALSE), eq(webContextProp), eq("RetrieveDocuments"));

        verify(adapterProxy).retrieveDocumentSet(any(RetrieveDocumentSetRequestType.class), any(AssertionType.class));

    }

    @Test
    public void testExecuteNull() {
        message = null;
        adapterProxy = mock(AdapterDocRetrieveProxy.class);
        logger = mock(DocRetrieveAuditLogger.class);
        instance = new InboundDocRetrieveStrategyImpl(adapterProxy, logger);
        instance.execute(message);
        verifyZeroInteractions(adapterProxy, logger);

    }

}
