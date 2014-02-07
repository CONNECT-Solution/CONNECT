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
package gov.hhs.fha.nhinc.docretrieve.nhin;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import gov.hhs.fha.nhinc.auditrepository.AuditRepositoryDocumentRetrieveLogger;
import gov.hhs.fha.nhinc.common.auditlog.DocRetrieveMessageType;
import gov.hhs.fha.nhinc.common.auditlog.DocRetrieveResponseMessageType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;

import org.junit.Test;
import org.mockito.Mockito;

/**
 * 
 * @author mweaver
 */
public class NhinDocRetrieveAuditTransformer_g0Test {

    private PropertyAccessor accessor;

    /**
     * Test of transformRequest method, of class NhinDocRetrieveAuditTransformer_g0.
     * 
     * @throws PropertyAccessException
     */
    @Test
    public void testTransformRequest() throws PropertyAccessException {
        AuditRepositoryDocumentRetrieveLogger mockLogger = mock(AuditRepositoryDocumentRetrieveLogger.class);
        InboundDocRetrieveAuditTransformer_g0 transform = new InboundDocRetrieveAuditTransformer_g0(mockLogger);
        InboundDocRetrieveOrchestratable mockMessage = mock(InboundDocRetrieveOrchestratable.class);
        String localCommunityId = "1.1";
        accessor = mock(PropertyAccessor.class);
        HomeCommunityMap.setPropertyAccessor(accessor);

        when(accessor.getProperty(Mockito.anyString(), Mockito.anyString())).thenReturn(localCommunityId);

        transform.transformRequest(mockMessage);

        verify(mockMessage).getAssertion();
        verify(mockMessage).getRequest();
        verify(mockLogger).logDocRetrieve(any(DocRetrieveMessageType.class),
                eq(NhincConstants.AUDIT_LOG_INBOUND_DIRECTION), eq(NhincConstants.AUDIT_LOG_NHIN_INTERFACE),
                any(String.class));

    }

    /**
     * Test of transformResponse method, of class NhinDocRetrieveAuditTransformer_g0.
     * 
     * @throws PropertyAccessException
     */
    @Test
    public void testTransformResponse() throws PropertyAccessException {
        AssertionType assertion = mock(AssertionType.class);
        AuditRepositoryDocumentRetrieveLogger mockLogger = mock(AuditRepositoryDocumentRetrieveLogger.class);
        InboundDocRetrieveAuditTransformer_g0 transform = new InboundDocRetrieveAuditTransformer_g0(mockLogger);
        InboundDocRetrieveOrchestratable mockMessage = mock(InboundDocRetrieveOrchestratable.class);

        String localCommunityId = "1.1";
        HomeCommunityType homeCommunity = new HomeCommunityType();
        homeCommunity.setHomeCommunityId(localCommunityId);

        when(assertion.getHomeCommunity()).thenReturn(homeCommunity);

        transform.transformResponse(mockMessage);

        verify(mockMessage).getAssertion();
        verify(mockMessage).getResponse();
        verify(mockLogger).logDocRetrieveResult(any(DocRetrieveResponseMessageType.class),
                eq(NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION), eq(NhincConstants.AUDIT_LOG_NHIN_INTERFACE),
                any(String.class));

    }

}