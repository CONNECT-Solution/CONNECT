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
package gov.hhs.fha.nhinc.docretrieve.entity;

import gov.hhs.fha.nhinc.auditrepository.AuditRepositoryLogger;
import gov.hhs.fha.nhinc.common.auditlog.DocRetrieveMessageType;
import gov.hhs.fha.nhinc.common.auditlog.DocRetrieveResponseMessageType;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.docretrieve.adapter.proxy.AdapterDocRetrieveProxy;
import gov.hhs.fha.nhinc.docretrieve.audit.DocRetrieveAuditLogger;
import gov.hhs.fha.nhinc.docretrieve.nhin.InboundDocRetrieveOrchestratable;
import gov.hhs.fha.nhinc.docretrieve.nhin.InboundDocRetrieveStrategyImpl;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.orchestration.Orchestratable;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Matchers.eq;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import java.util.Properties;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;

/**
 *
 * @author mweaver
 */
public class OutboundDocRetrieveAuditTransformer_a0Test {
//
//    private DocRetrieveAuditLogger docRetrieveLogger = mock(DocRetrieveAuditLogger.class);
//    private OutboundDocRetrieveAuditTransformer_a0 instance = null;
//    private Orchestratable message = null;
//    private OutboundDocRetrieveOrchestratableFactory factory = null;
//    private final Properties properties = null;
//
//    public OutboundDocRetrieveAuditTransformer_a0Test() {
//    }
//
//    @Before
//    public void setup() {
//        factory = new OutboundDocRetrieveOrchestratableFactory();
//        message = factory.createOutboundStandardDocRetrieveOrchestratable();
//        instance = new OutboundDocRetrieveAuditTransformer_a0() {
//            @Override
//            protected DocRetrieveAuditLogger getAuditRepositoryLogger() {
//                return docRetrieveLogger;
//            }
//        };
//
//    }
//
//    /**
//     * Test of transformRequest method, of class OutboundDocRetrieveAuditTransformer_a0.
//     */
//    @Test
//    public void testTransformRequest() {
//        instance.transformRequest(message);
//        verify(docRetrieveLogger, never()).auditRequestMessage(Mockito.any(RetrieveDocumentSetRequestType.class),
//            Mockito.any(AssertionType.class), Mockito.any(NhinTargetSystemType.class),
//            eq(NhincConstants.AUDIT_LOG_INBOUND_DIRECTION), eq(NhincConstants.AUDIT_LOG_ENTITY_INTERFACE),
//            eq(Boolean.TRUE), eq(properties), eq(NhincConstants.DOC_RETRIEVE_SERVICE_NAME));
//
//    }
//
//    /**
//     * Test of transformResponse method, of class OutboundDocRetrieveAuditTransformer_a0.
//     */
//    @Test
//    public void testTransformResponse() {
//        instance.transformResponse(message);
//        verify(docRetrieveLogger, never()).auditResponseMessage(Mockito.any(RetrieveDocumentSetRequestType.class),
//            Mockito.any(RetrieveDocumentSetResponseType.class), Mockito.any(AssertionType.class),
//            Mockito.any(NhinTargetSystemType.class), eq(NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION),
//            eq(NhincConstants.AUDIT_LOG_NHIN_INTERFACE), eq(Boolean.TRUE), eq(properties),
//            eq(NhincConstants.DOC_RETRIEVE_SERVICE_NAME));
//
//    }
}
