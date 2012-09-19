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

package gov.hhs.fha.nhinc.docsubmission.passthru.deferred.request;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.docsubmission.XDRAuditLogger;
import gov.hhs.fha.nhinc.docsubmission.entity.deferred.request.OutboundDocSubmissionDeferredRequestDelegate;
import gov.hhs.fha.nhinc.docsubmission.entity.deferred.request.OutboundDocSubmissionDeferredRequestOrchestratable;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
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
public class PassthruDocSubmissionDeferredRequestOrchImplTest {
    protected Mockery context = new JUnit4Mockery() {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };
    final Log mockLog = context.mock(Log.class);
    final XDRAuditLogger mockXDRLog = context.mock(XDRAuditLogger.class);
    final OutboundDocSubmissionDeferredRequestDelegate mockDelegate = context.mock(OutboundDocSubmissionDeferredRequestDelegate.class);
    
    @Test
    public void testProvideAndRegisterDocumentSetB() {
        allowAnyMockLogging();
        expect2MockAudits();
        expectMockDelegateProcessAndReturnValidResponse();
        
        XDRAcknowledgementType response = runProvideAndRegisterDocumentSetBRequest();
        
        assertNotNull(response);
        assertEquals(NhincConstants.XDR_ACK_STATUS_MSG, response.getMessage().getStatus());
    }
    
    @Test
    public void testGetters() {
        PassthruDocSubmissionDeferredRequestOrchImpl passthruOrch = new PassthruDocSubmissionDeferredRequestOrchImpl();
        
        assertNotNull(passthruOrch.getLogger());
        assertNotNull(passthruOrch.getOutboundDocSubmissionDeferredRequestDelegate());
        assertNotNull(passthruOrch.getXDRAuditLogger());
    }
    
    private XDRAcknowledgementType runProvideAndRegisterDocumentSetBRequest() {
        ProvideAndRegisterDocumentSetRequestType request = new ProvideAndRegisterDocumentSetRequestType();
        AssertionType assertion = new AssertionType();
        NhinTargetSystemType targets = new NhinTargetSystemType();
        
        PassthruDocSubmissionDeferredRequestOrchImpl passthruOrch = createPassthruDocSubmissionDeferredRequestOrchImpl();
        return passthruOrch.provideAndRegisterDocumentSetBRequest(request, assertion, targets);       
    }
    
    private void expect2MockAudits() {
        context.checking(new Expectations() {
            {
                oneOf(mockXDRLog)
                        .auditXDR(
                                with(any(gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType.class)),
                                with(any(AssertionType.class)), with(any(String.class)));

                oneOf(mockXDRLog).auditAcknowledgement(with(any(XDRAcknowledgementType.class)),
                        with(any(AssertionType.class)), with(any(String.class)), with(any(String.class)));
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
                oneOf(mockDelegate).process(with(any(OutboundDocSubmissionDeferredRequestOrchestratable.class)));
                will(returnValue(createOutboundDocSubmissionDeferredRequestOrchestratable()));
            }
        });
    }
    
    private OutboundDocSubmissionDeferredRequestOrchestratable createOutboundDocSubmissionDeferredRequestOrchestratable() {
        RegistryResponseType regResponse = new RegistryResponseType();
        regResponse.setStatus(NhincConstants.XDR_ACK_STATUS_MSG);

        XDRAcknowledgementType response = new XDRAcknowledgementType();
        response.setMessage(regResponse);
        
        OutboundDocSubmissionDeferredRequestOrchestratable orchestratable = new OutboundDocSubmissionDeferredRequestOrchestratable(null);
        orchestratable.setResponse(response);

        return orchestratable;
    }
    
    private PassthruDocSubmissionDeferredRequestOrchImpl createPassthruDocSubmissionDeferredRequestOrchImpl() {
        return new PassthruDocSubmissionDeferredRequestOrchImpl() {
            protected Log getLogger() {
                return mockLog;
            }

            protected XDRAuditLogger getXDRAuditLogger() {
                return mockXDRLog;
            }
            
            protected OutboundDocSubmissionDeferredRequestDelegate getOutboundDocSubmissionDeferredRequestDelegate() {
                return mockDelegate;
            }
        };
    }
}
