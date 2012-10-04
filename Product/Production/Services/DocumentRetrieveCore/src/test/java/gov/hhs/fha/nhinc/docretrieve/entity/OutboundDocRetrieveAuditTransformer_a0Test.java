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
package gov.hhs.fha.nhinc.docretrieve.entity;

import gov.hhs.fha.nhinc.auditrepository.AuditRepositoryLogger;
import gov.hhs.fha.nhinc.common.auditlog.DocRetrieveMessageType;
import gov.hhs.fha.nhinc.common.auditlog.DocRetrieveResponseMessageType;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
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

/**
 * 
 * @author mweaver
 */
public class OutboundDocRetrieveAuditTransformer_a0Test {

    private Mockery mockingContext;
    private AuditRepositoryLogger mockedDependency;

    public OutboundDocRetrieveAuditTransformer_a0Test() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        mockingContext = new JUnit4Mockery() {
            {
                setImposteriser(ClassImposteriser.INSTANCE);
            }
        };
        mockedDependency = mockingContext.mock(AuditRepositoryLogger.class);
    }

    @After
    public void tearDown() {
    }

    private OutboundDocRetrieveAuditTransformer_a0 createOutboundDocRetrieveAuditTransformer_a0() {
        return new OutboundDocRetrieveAuditTransformer_a0() {
            @Override
            protected AuditRepositoryLogger getAuditRepositoryLogger() {
                return mockedDependency;
            }
            
            @Override
            protected String getLocalHomeCommunityId() {
            	return "hcid";
            }
        };
    }

    private LogEventRequestType mockLogEventRequestType() {
        LogEventRequestType req = new LogEventRequestType();
        req.setDirection("Outbound");
        req.setInterface("Nhin");

        return req;
    }

    /**
     * Test of transformRequest method, of class OutboundDocRetrieveAuditTransformer_a0.
     */
    @Test
    public void testTransformRequest() {
        OutboundDocRetrieveOrchestratableFactory factory = new OutboundDocRetrieveOrchestratableFactory();
        Orchestratable message = factory.getEntityDocRetrieveOrchestratableImpl_a0();
        OutboundDocRetrieveAuditTransformer_a0 instance = createOutboundDocRetrieveAuditTransformer_a0();

        mockingContext.checking(new Expectations() {
            {
                one(mockedDependency).logDocRetrieve(with(any(DocRetrieveMessageType.class)), with(any(String.class)),
                        with(any(String.class)), with("hcid"));
                will(returnValue(mockLogEventRequestType()));
            }
        });
        LogEventRequestType result = instance.transformRequest(message);
        assertEquals("Outbound", result.getDirection());
        assertEquals("Nhin", result.getInterface());
    }

    /**
     * Test of transformResponse method, of class OutboundDocRetrieveAuditTransformer_a0.
     */
    @Test
    public void testTransformResponse() {
        OutboundDocRetrieveOrchestratableFactory factory = new OutboundDocRetrieveOrchestratableFactory();
        Orchestratable message = factory.getEntityDocRetrieveOrchestratableImpl_a0();
        OutboundDocRetrieveAuditTransformer_a0 instance = createOutboundDocRetrieveAuditTransformer_a0();

        mockingContext.checking(new Expectations() {
            {
                one(mockedDependency).logDocRetrieveResult(with(any(DocRetrieveResponseMessageType.class)),
                        with(any(String.class)), with(any(String.class)), with("hcid"));
                will(returnValue(mockLogEventRequestType()));
            }
        });
        LogEventRequestType result = instance.transformResponse(message);
        assertEquals("Outbound", result.getDirection());
        assertEquals("Nhin", result.getInterface());
    }
}
