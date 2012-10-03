/**
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
package gov.hhs.fha.nhinc.patientdiscovery.entity;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.hl7.v3.CommunityPRPAIN201306UV02ResponseType;
import org.hl7.v3.MCCIMT000100UV01Receiver;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02ResponseType;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.connectmgr.UrlInfo;
import gov.hhs.fha.nhinc.gateway.executorservice.NhinTaskExecutor;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.GATEWAY_API_LEVEL;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditLogger;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryPolicyChecker;
import gov.hhs.fha.nhinc.patientdiscovery.utils.PDTestUtils;

/**
 * @author zmelnick
 *
 */
public class EntityPatientDiscoveryOrchImplTest {

    protected Mockery context = new JUnit4Mockery() {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };

    final Log mockLog = context.mock(Log.class);
    final PatientDiscoveryPolicyChecker mockPolicyCheck = context.mock(PatientDiscoveryPolicyChecker.class);
    final PatientDiscoveryAuditLogger mockAuditLog = context.mock(PatientDiscoveryAuditLogger.class);

    @Test
    public void testRespondingGatewayPRPAIN201305UV02_Success() {
        RespondingGatewayPRPAIN201305UV02RequestType request = new RespondingGatewayPRPAIN201305UV02RequestType();
        AssertionType assertion = new AssertionType();
        request.setPRPAIN201305UV02(context.mock(PRPAIN201305UV02.class));

        allowAnyMockLogging();
        expect2Audits();

        final RespondingGatewayPRPAIN201306UV02ResponseType response = new RespondingGatewayPRPAIN201306UV02ResponseType();

        final EntityPatientDiscoveryOrchImpl entityOrchImpl = new EntityPatientDiscoveryOrchImpl() {

            @Override
            protected boolean checkPolicy(RespondingGatewayPRPAIN201305UV02RequestType request, AssertionType assertion) {
                return mockPolicyCheck.checkOutgoingPolicy(request);
            }

            @Override
            protected PatientDiscoveryAuditLogger getNewPatientDiscoveryAuditLogger() {
                return mockAuditLog;
            }

            @Override
            protected RespondingGatewayPRPAIN201306UV02ResponseType getResponseFromCommunities(
                    RespondingGatewayPRPAIN201305UV02RequestType request, AssertionType assertion) {
                return response;
            }
        };

        // assert that response is the same object as is returned by getResponseFromCommunities
        assertTrue(response == entityOrchImpl.respondingGatewayPRPAIN201305UV02(request, assertion));
        context.assertIsSatisfied();
    }

    @Test
    public void testGetResponseFromCommunities_Success() {
        final RespondingGatewayPRPAIN201306UV02ResponseType response = new RespondingGatewayPRPAIN201306UV02ResponseType();
        final NhinTargetCommunitiesType mockTargetCommunity = context.mock(NhinTargetCommunitiesType.class);
        final RespondingGatewayPRPAIN201305UV02RequestType newRequest = context
                .mock(RespondingGatewayPRPAIN201305UV02RequestType.class);
        final PRPAIN201305UV02 new201305 = context.mock(PRPAIN201305UV02.class);
        final MCCIMT000100UV01Receiver chaos = context.mock(MCCIMT000100UV01Receiver.class);
        RespondingGatewayPRPAIN201305UV02RequestType request = new RespondingGatewayPRPAIN201305UV02RequestType();
        AssertionType assertion = new AssertionType();

        setMockPolicyCheck(true);
        request.setNhinTargetCommunities(mockTargetCommunity);

        final EntityPatientDiscoveryOrchImpl entityOrchImpl = new EntityPatientDiscoveryOrchImpl() {
            @Override
            protected RespondingGatewayPRPAIN201306UV02ResponseType getCumulativeResponse(
                    NhinTaskExecutor<OutboundPatientDiscoveryOrchestratable, OutboundPatientDiscoveryOrchestratable> dqexecutor) {
                return response;
            }

            @Override
            protected List<UrlInfo> getEndpoints(NhinTargetCommunitiesType targetCommunities) {
                List<UrlInfo> endpoints = new ArrayList<UrlInfo>();
                UrlInfo mockUrlInfo = new UrlInfo();
                mockUrlInfo.setHcid("1.1");
                endpoints.add(mockUrlInfo);
                return endpoints;
            }

            @Override
            protected RespondingGatewayPRPAIN201305UV02RequestType createNewRequest(
                    RespondingGatewayPRPAIN201305UV02RequestType request, AssertionType assertion, UrlInfo urlInfo) {
                new201305.getReceiver().set(0, chaos);
                newRequest.setAssertion(assertion);
                newRequest.setPRPAIN201305UV02(new201305);
                newRequest.setNhinTargetCommunities(request.getNhinTargetCommunities());
                return newRequest;
            }

            @Override
            protected void setHomeCommunityIdInRequest(RespondingGatewayPRPAIN201305UV02RequestType request, String hcid) {

            }

            @Override
            protected boolean checkPolicy(RespondingGatewayPRPAIN201305UV02RequestType request, AssertionType assertion) {
                return mockPolicyCheck.checkOutgoingPolicy(request);
            }

            @Override
            protected GATEWAY_API_LEVEL getGatewayVersion() {
                return GATEWAY_API_LEVEL.LEVEL_g1;
            }
        };

        context.checking(new Expectations() {
            {
                atLeast(1).of(newRequest).getPRPAIN201305UV02();
                atLeast(1).of(newRequest).setAssertion(with(any(AssertionType.class)));
                atLeast(1).of(newRequest).setPRPAIN201305UV02(with(any(PRPAIN201305UV02.class)));
                atLeast(1).of(newRequest).setNhinTargetCommunities(with(any(NhinTargetCommunitiesType.class)));
                atLeast(1).of(new201305).getReceiver();
            }
        });

        assertSame(response, entityOrchImpl.getResponseFromCommunities(request, assertion));
        context.assertIsSatisfied();
    }

    @Test
    public void testGetResponseFromCommunities_PolicyFail() {
        final RespondingGatewayPRPAIN201306UV02ResponseType response = new RespondingGatewayPRPAIN201306UV02ResponseType();
        PDTestUtils testUtils = new PDTestUtils();
        final RespondingGatewayPRPAIN201305UV02RequestType newRequest = testUtils.createValidEntityRequest();

        setMockPolicyCheck(false);

        final EntityPatientDiscoveryOrchImpl entityOrchImpl = new EntityPatientDiscoveryOrchImpl() {
            @Override
            protected RespondingGatewayPRPAIN201306UV02ResponseType getCumulativeResponse(
                    NhinTaskExecutor<OutboundPatientDiscoveryOrchestratable, OutboundPatientDiscoveryOrchestratable> dqexecutor) {
                return response;
            }

            @Override
            protected List<UrlInfo> getEndpoints(NhinTargetCommunitiesType targetCommunities) {
                List<UrlInfo> endpoints = new ArrayList<UrlInfo>();
                UrlInfo mockUrlInfo = new UrlInfo();
                mockUrlInfo.setHcid("1.1");
                endpoints.add(mockUrlInfo);
                return endpoints;
            }

            @Override
            protected RespondingGatewayPRPAIN201305UV02RequestType createNewRequest(
                    RespondingGatewayPRPAIN201305UV02RequestType request, AssertionType assertion, UrlInfo urlInfo) {
                PDTestUtils testUtils = new PDTestUtils();
                RespondingGatewayPRPAIN201305UV02RequestType request2 = testUtils.createValidEntityRequest();
                return request2;
            }

            @Override
            protected boolean checkPolicy(RespondingGatewayPRPAIN201305UV02RequestType request, AssertionType assertion) {
                return mockPolicyCheck.checkOutgoingPolicy(request);
            }

            @Override
            protected GATEWAY_API_LEVEL getGatewayVersion() {
                return GATEWAY_API_LEVEL.LEVEL_g1;
            }
        };

        RespondingGatewayPRPAIN201306UV02ResponseType testResponse = entityOrchImpl.getResponseFromCommunities(
                newRequest, new AssertionType());
        assertTrue(response != testResponse);

        String testErrorMessage = (String) testResponse.getCommunityResponse().get(0).getPRPAIN201306UV02()
                .getControlActProcess().getReasonOf().get(0).getDetectedIssueEvent().getMitigatedBy().get(0)
                .getDetectedIssueManagement().getText().getContent().get(0);

        assertTrue(testErrorMessage.contains("Policy Check Failed for homeId="));

        context.assertIsSatisfied();
    }

    @Test
    public void testRespondingGatewayPRPAIN201305UV02_NullRequest(){
        RespondingGatewayPRPAIN201305UV02RequestType request = null;
        AssertionType assertion = context.mock(AssertionType.class);
        final CommunityPRPAIN201306UV02ResponseType communityResponseError = new CommunityPRPAIN201306UV02ResponseType();



        EntityPatientDiscoveryOrchImpl entityOrchImpl= new EntityPatientDiscoveryOrchImpl(){
            @Override
            protected Log getLog(){
                return mockLog;
            }

            @Override
            protected void addErrorMessageToResponse(RespondingGatewayPRPAIN201305UV02RequestType request,
            RespondingGatewayPRPAIN201306UV02ResponseType response, Exception e){
                response.getCommunityResponse().add(0, communityResponseError);
            }
        };

        context.checking(new Expectations(){
            {
                atLeast(2).of(mockLog).debug(with(any(String.class)));
                atLeast(1).of(mockLog).error(with(any(String.class)), with(any(Exception.class)));
            }
        });

        assertSame(communityResponseError,entityOrchImpl.respondingGatewayPRPAIN201305UV02(request, assertion).getCommunityResponse().get(0));
        context.assertIsSatisfied();
    }


    @Test
    public void testRespondingGatewayPRPAIN201305UV02_NullAssertion(){
        final RespondingGatewayPRPAIN201305UV02RequestType mockRequest = context.mock(RespondingGatewayPRPAIN201305UV02RequestType.class);
        AssertionType assertion = null;
        final CommunityPRPAIN201306UV02ResponseType communityResponseError = new CommunityPRPAIN201306UV02ResponseType();

        EntityPatientDiscoveryOrchImpl entityOrchImpl= new EntityPatientDiscoveryOrchImpl(){
            @Override
            protected Log getLog(){
                return mockLog;
            }

            @Override
            protected void addErrorMessageToResponse(RespondingGatewayPRPAIN201305UV02RequestType request,
            RespondingGatewayPRPAIN201306UV02ResponseType response, Exception e){
                response.getCommunityResponse().add(0, communityResponseError);
            }
        };

        context.checking(new Expectations(){
            {
                ignoring(mockRequest);
                atLeast(2).of(mockLog).debug(with(any(String.class)));
                atLeast(1).of(mockLog).error(with(any(String.class)), with(any(Exception.class)));
            }
        });

        assertSame(communityResponseError,entityOrchImpl.respondingGatewayPRPAIN201305UV02(mockRequest, assertion).getCommunityResponse().get(0));
        context.assertIsSatisfied();
    }

    /*-------------------------------------Non Test Methods go below this line---------------------*/

    private void expect2Audits() {
        context.checking(new Expectations() {
            {
                oneOf(mockAuditLog).auditEntity201305(with(any(RespondingGatewayPRPAIN201305UV02RequestType.class)),
                        with(any(AssertionType.class)), with(any(String.class)));
                oneOf(mockAuditLog).auditEntity201306(with(any(RespondingGatewayPRPAIN201306UV02ResponseType.class)),
                        with(any(AssertionType.class)), with(any(String.class)));
            }
        });

    }

    private void setMockPolicyCheck(final boolean allow) {
        context.checking(new Expectations() {
            {
                atLeast(1).of(mockPolicyCheck).checkOutgoingPolicy(
                        with(any(RespondingGatewayPRPAIN201305UV02RequestType.class)));
                will(returnValue(allow));
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

    /**
     * Create the mock instance of the class under test.
     *
     * @return a mock instance of EntityPatientDiscoveryOrchImpl
     */
    public EntityPatientDiscoveryOrchImpl createEntityPDOrchImpl() {
        return new EntityPatientDiscoveryOrchImpl() {

            @Override
            protected boolean checkPolicy(RespondingGatewayPRPAIN201305UV02RequestType request, AssertionType assertion) {
                return mockPolicyCheck.checkOutgoingPolicy(request);
            }

            @Override
            protected PatientDiscoveryAuditLogger getNewPatientDiscoveryAuditLogger() {
                return mockAuditLog;
            }

        };
    }

}
