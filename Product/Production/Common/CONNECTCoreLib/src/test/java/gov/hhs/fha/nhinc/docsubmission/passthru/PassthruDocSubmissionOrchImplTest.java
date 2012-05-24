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

package gov.hhs.fha.nhinc.docsubmission.passthru;

import static org.junit.Assert.*;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.docsubmission.XDRAuditLogger;
import gov.hhs.fha.nhinc.docsubmission.entity.OutboundDocSubmissionDelegate;
import gov.hhs.fha.nhinc.docsubmission.entity.OutboundDocSubmissionOrchestratable;
import gov.hhs.fha.nhinc.gateway.aggregator.document.DocumentConstants;


import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

import org.apache.commons.logging.Log;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;

/**
 * @author akong
 *
 */
public class PassthruDocSubmissionOrchImplTest {
    protected Mockery context = new JUnit4Mockery() {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };
    final Log mockLog = context.mock(Log.class);
    final XDRAuditLogger mockXDRLog = context.mock(XDRAuditLogger.class);
    final OutboundDocSubmissionDelegate mockDelegate = context.mock(OutboundDocSubmissionDelegate.class);
    
    @Test
    public void testProvideAndRegisterDocumentSetB() {
        allowAnyMockLogging();
        expect2MockAudits();
        expectMockDelegateProcessAndReturnValidResponse();
        
        RegistryResponseType response = runProvideAndRegisterDocumentSetB();
        
        context.assertIsSatisfied();
        assertNotNull(response);
        assertEquals(DocumentConstants.XDS_SUBMISSION_RESPONSE_STATUS_SUCCESS, response.getStatus());
    }
    
    @Test
    public void testGetters() {
        PassthruDocSubmissionOrchImpl passthruOrch = new PassthruDocSubmissionOrchImpl();
        
        assertNotNull(passthruOrch.getLogger());
        assertNotNull(passthruOrch.getOutboundDocSubmissionDelegate());
        assertNotNull(passthruOrch.getXDRAuditLogger());
    }
    
    private RegistryResponseType runProvideAndRegisterDocumentSetB() {
        ProvideAndRegisterDocumentSetRequestType request = new ProvideAndRegisterDocumentSetRequestType();
        AssertionType assertion = new AssertionType();
        NhinTargetSystemType targets = new NhinTargetSystemType();
        
        PassthruDocSubmissionOrchImpl passthruOrch = createPassthruDocSubmissionOrchImpl();
        return passthruOrch.provideAndRegisterDocumentSetB(request, assertion, targets);       
    }
    
    private void expect2MockAudits() {
        context.checking(new Expectations() {
            {
                oneOf(mockXDRLog)
                        .auditXDR(
                                with(any(gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType.class)),
                                with(any(AssertionType.class)), with(any(String.class)));

                oneOf(mockXDRLog).auditNhinXDRResponse(with(any(RegistryResponseType.class)),
                        with(any(AssertionType.class)), with(any(String.class)));
            }
        });
    }

    private void allowAnyMockLogging() {
        context.checking(new Expectations() {
            {
                ignoring(mockLog);
            }
        });
    }
    
    private void expectMockDelegateProcessAndReturnValidResponse() {
        context.checking(new Expectations() {
            {
                oneOf(mockDelegate).process(with(any(OutboundDocSubmissionOrchestratable.class)));
                will(returnValue(createOutboundDocSubmissionOrchestratable()));
            }
        });
    }
    
    private OutboundDocSubmissionOrchestratable createOutboundDocSubmissionOrchestratable() {
        RegistryResponseType response = new RegistryResponseType();
        response.setStatus(DocumentConstants.XDS_SUBMISSION_RESPONSE_STATUS_SUCCESS);

        OutboundDocSubmissionOrchestratable orchestratable = new OutboundDocSubmissionOrchestratable(null);
        orchestratable.setResponse(response);

        return orchestratable;
    }
    
    private PassthruDocSubmissionOrchImpl createPassthruDocSubmissionOrchImpl() {
        return new PassthruDocSubmissionOrchImpl() {
            protected Log getLogger() {
                return mockLog;
            }

            protected XDRAuditLogger getXDRAuditLogger() {
                return mockXDRLog;
            }
            
            protected OutboundDocSubmissionDelegate getOutboundDocSubmissionDelegate() {
                return mockDelegate;
            }
        };
    }
}
