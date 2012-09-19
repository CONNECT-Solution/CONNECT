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

package gov.hhs.fha.nhinc.docsubmission.nhin;

import static org.junit.Assert.*;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.docsubmission.XDRAuditLogger;
import gov.hhs.fha.nhinc.docsubmission.XDRPolicyChecker;
import gov.hhs.fha.nhinc.docsubmission.adapter.proxy.AdapterDocSubmissionProxy;
import gov.hhs.fha.nhinc.gateway.aggregator.document.DocumentConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;

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
public class NhinDocSubmissionOrchImplTest {

    protected Mockery context = new JUnit4Mockery() {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };
    final Log mockLog = context.mock(Log.class);
    final XDRAuditLogger mockXDRLog = context.mock(XDRAuditLogger.class);
    final XDRPolicyChecker mockPolicyCheck = context.mock(XDRPolicyChecker.class);
    final PropertyAccessor mockPropertyAccessor = context.mock(PropertyAccessor.class);
    final AdapterDocSubmissionProxy mockProxy = context.mock(AdapterDocSubmissionProxy.class);
    
    @Test
    public void testProvideAndRegisterDocumentSetB() throws PropertyAccessException{
        expect4MockAudits();
        setMockPropertyAccessorToReturnValidHcid();
        allowAnyMockLogging();
        setMockPolicyCheck(true);
        expectProxyInvocationAndReturnValidResponse();
        
        RegistryResponseType response = runProvideAndRegisterDocumentSetB();

        context.assertIsSatisfied();
        assertNotNull(response);
        assertEquals(DocumentConstants.XDS_SUBMISSION_RESPONSE_STATUS_SUCCESS, response.getStatus());
    }
    
    @Test
    public void testProvideAndRegisterDocumentSetB_policyFailure() throws PropertyAccessException{
        expect2MockAudits();
        setMockPropertyAccessorToReturnValidHcid();
        allowAnyMockLogging();
        setMockPolicyCheck(false);
        
        RegistryResponseType response = runProvideAndRegisterDocumentSetB();

        context.assertIsSatisfied();
        assertNotNull(response);
        assertTrue(response.getRegistryErrorList().getRegistryError().size() > 0);
        assertEquals(DocumentConstants.XDS_SUBMISSION_RESPONSE_STATUS_FAILURE, response.getStatus());
    }
    
    @Test
    public void testProvideAndRegisterDocumentSetB_emptyAssertion() throws PropertyAccessException{
        expect2MockAudits();
        setMockPropertyAccessorToReturnValidHcid();
        allowAnyMockLogging();
       
        RegistryResponseType response = runProvideAndRegisterDocumentSetB_emptyAssertion();

        context.assertIsSatisfied();
        assertNotNull(response);
        assertTrue(response.getRegistryErrorList().getRegistryError().size() > 0);
        assertEquals(DocumentConstants.XDS_SUBMISSION_RESPONSE_STATUS_FAILURE, response.getStatus());
    }
    
    @Test
    public void testHasHomeCommunityId() {
        NhinDocSubmissionOrchImpl nhinOrchImpl = new NhinDocSubmissionOrchImpl();
        
        boolean hasHcid = nhinOrchImpl.hasHomeCommunityId(null);
        assertFalse(hasHcid);
        
        AssertionType assertion = new AssertionType();
        hasHcid = nhinOrchImpl.hasHomeCommunityId(assertion);
        assertFalse(hasHcid);
        
        HomeCommunityType homeCommunity = new HomeCommunityType();
        assertion.setHomeCommunity(homeCommunity);
        hasHcid = nhinOrchImpl.hasHomeCommunityId(assertion);
        assertFalse(hasHcid);
        
        homeCommunity.setHomeCommunityId("1.1");
        hasHcid = nhinOrchImpl.hasHomeCommunityId(assertion);
        assertTrue(hasHcid);
    }
    
    @Test
    public void testGetters() {
        NhinDocSubmissionOrchImpl nhinOrchImpl = new NhinDocSubmissionOrchImpl();
        
        assertNotNull(nhinOrchImpl.getPropertyAccessor());
        assertNotNull(nhinOrchImpl.getXDRAuditLogger());
        assertNotNull(nhinOrchImpl.getXDRPolicyChecker());
    }
    
    private RegistryResponseType runProvideAndRegisterDocumentSetB() {
        ProvideAndRegisterDocumentSetRequestType request = new ProvideAndRegisterDocumentSetRequestType();
        AssertionType assertion = new AssertionType();
        HomeCommunityType homeCommunity = new HomeCommunityType();
        homeCommunity.setHomeCommunityId("1.1");
        assertion.setHomeCommunity(homeCommunity);
        
        NhinDocSubmissionOrchImpl nhinOrch = createNhinDocSubmissionOrchImpl();
        return nhinOrch.documentRepositoryProvideAndRegisterDocumentSetB(request, assertion);
    }
    
    private RegistryResponseType runProvideAndRegisterDocumentSetB_emptyAssertion() {
        ProvideAndRegisterDocumentSetRequestType request = new ProvideAndRegisterDocumentSetRequestType();
        AssertionType assertion = new AssertionType();
        
        NhinDocSubmissionOrchImpl nhinOrch = createNhinDocSubmissionOrchImpl();
        return nhinOrch.documentRepositoryProvideAndRegisterDocumentSetB(request, assertion);
    }
    
    private void expect4MockAudits() {
        context.checking(new Expectations() {
            {
                oneOf(mockXDRLog).auditNhinXDR(
                        with(any(ProvideAndRegisterDocumentSetRequestType.class)),
                        with(any(AssertionType.class)), with(any(String.class)));

                oneOf(mockXDRLog).auditAdapterXDR(
                        with(any(ProvideAndRegisterDocumentSetRequestType.class)),
                        with(any(AssertionType.class)), with(any(String.class)));

                oneOf(mockXDRLog).auditNhinXDRResponse(with(any(RegistryResponseType.class)),
                        with(any(AssertionType.class)), with(any(String.class)));

                oneOf(mockXDRLog).auditAdapterXDRResponse(with(any(RegistryResponseType.class)),
                        with(any(AssertionType.class)), with(any(String.class)));

            }
        });
    }
    
    private void expect2MockAudits() {
        context.checking(new Expectations() {
            {
                oneOf(mockXDRLog).auditNhinXDR(
                        with(any(ProvideAndRegisterDocumentSetRequestType.class)),
                        with(any(AssertionType.class)), with(any(String.class)));

                oneOf(mockXDRLog).auditNhinXDRResponse(with(any(RegistryResponseType.class)),
                        with(any(AssertionType.class)), with(any(String.class)));
            }
        });
    }

    private void setMockPropertyAccessorToReturnValidHcid() throws PropertyAccessException{
        context.checking(new Expectations() {
            {
                oneOf(mockPropertyAccessor).getProperty(NhincConstants.GATEWAY_PROPERTY_FILE,
                        NhincConstants.HOME_COMMUNITY_ID_PROPERTY);
                will(returnValue("1.1"));
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

    private void setMockPolicyCheck(final boolean allow) {
        context.checking(new Expectations() {
            {
                oneOf(mockPolicyCheck).checkXDRRequestPolicy(with(any(ProvideAndRegisterDocumentSetRequestType.class)),
                        with(any(AssertionType.class)), with(any(String.class)), with(any(String.class)),
                        with(equal(NhincConstants.POLICYENGINE_INBOUND_DIRECTION)));
                will(returnValue(allow));
            }
        });
    }
        
    private void expectProxyInvocationAndReturnValidResponse() {
        context.checking(new Expectations() {
            {
                oneOf(mockProxy).provideAndRegisterDocumentSetB(with(any(ProvideAndRegisterDocumentSetRequestType.class)), with(any(AssertionType.class)));
                will(returnValue(createRegistryResponseType()));
            }
        });
    }
    
    private RegistryResponseType createRegistryResponseType() {
        RegistryResponseType response = new RegistryResponseType();       
        response.setStatus(DocumentConstants.XDS_SUBMISSION_RESPONSE_STATUS_SUCCESS);
        
        return response;
    }

    private NhinDocSubmissionOrchImpl createNhinDocSubmissionOrchImpl() {
        return new NhinDocSubmissionOrchImpl() {
            protected Log getLogger() {
                return mockLog;
            }

            protected XDRAuditLogger getXDRAuditLogger() {
                return mockXDRLog;
            }

            protected XDRPolicyChecker getXDRPolicyChecker() {
                return mockPolicyCheck;
            }

            protected PropertyAccessor getPropertyAccessor() {
                return mockPropertyAccessor;
            }
            
            protected AdapterDocSubmissionProxy getAdapterDocSubmissionProxy() {
                return mockProxy;
            }

        };
    }
}
