/*
 * Copyright (c) 2009-2019, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.patientdiscovery.outbound;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import gov.hhs.fha.nhinc.aspect.OutboundProcessingEvent;
import gov.hhs.fha.nhinc.audit.ejb.AuditEJBLogger;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.connectmgr.UrlInfo;
import gov.hhs.fha.nhinc.event.error.ErrorEventException;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.patientdiscovery.PDMessageGeneratorUtils;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryPolicyChecker;
import gov.hhs.fha.nhinc.patientdiscovery.aspect.PRPAIN201305UV02ArgTransformer;
import gov.hhs.fha.nhinc.patientdiscovery.aspect.RespondingGatewayPRPAIN201306UV02Builder;
import gov.hhs.fha.nhinc.patientdiscovery.audit.PatientDiscoveryAuditLogger;
import gov.hhs.fha.nhinc.patientdiscovery.audit.transform.PatientDiscoveryAuditTransforms;
import gov.hhs.fha.nhinc.patientdiscovery.entity.OutboundPatientDiscoveryDelegate;
import gov.hhs.fha.nhinc.patientdiscovery.entity.OutboundPatientDiscoveryOrchestratable;
import gov.hhs.fha.nhinc.policyengine.adapter.proxy.PolicyEngineProxy;
import gov.hhs.fha.nhinc.policyengine.adapter.proxy.PolicyEngineProxyObjectFactory;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.junit.Test;

/**
 * @author akong
 *
 */
public class StandardOutboundPatientDiscoveryTest {

    private final AuditEJBLogger mockEJBLogger = mock(AuditEJBLogger.class);
    private final OutboundPatientDiscoveryDelegate delegate = mock(OutboundPatientDiscoveryDelegate.class);
    private final String TARGET_HCID = "1.1";
    private final String PD_URL = "https://localhost:8181/Gateway/PatientDiscovery/1_0/NhinService/NhinPatientDiscovery";
    private final OutboundPatientDiscoveryOrchestratable mockOrchestratable
        = mock(OutboundPatientDiscoveryOrchestratable.class);
    private final PatientDiscoveryPolicyChecker pdPolicyChecker = mock(PatientDiscoveryPolicyChecker.class);

    @Test
    public void hasOutboundProcessingEvent() throws Exception {
        Class<StandardOutboundPatientDiscovery> clazz = StandardOutboundPatientDiscovery.class;
        Method method = clazz.getMethod("respondingGatewayPRPAIN201305UV02",
            RespondingGatewayPRPAIN201305UV02RequestType.class, AssertionType.class);
        OutboundProcessingEvent annotation = method.getAnnotation(OutboundProcessingEvent.class);
        assertNotNull(annotation);
        assertEquals(PRPAIN201305UV02ArgTransformer.class, annotation.beforeBuilder());
        assertEquals(RespondingGatewayPRPAIN201306UV02Builder.class, annotation.afterReturningBuilder());
        assertEquals("Patient Discovery", annotation.serviceType());
        assertEquals("1.0", annotation.version());
    }

    @Test
    public void auditLoggingTurnedOnForPD() {
        RespondingGatewayPRPAIN201305UV02RequestType request = new RespondingGatewayPRPAIN201305UV02RequestType();
        request.setPRPAIN201305UV02(new PRPAIN201305UV02());
        NhinTargetCommunitiesType targets = createNhinTargetCommunitiesType(TARGET_HCID);
        request.setNhinTargetCommunities(createNhinTargetCommunitiesType(TARGET_HCID));
        NhinTargetSystemType target = PDMessageGeneratorUtils.getInstance().convertFirstToNhinTargetSystemType(targets);
        AssertionType assertion = createAssertion();
        request.setAssertion(assertion);

        PolicyEngineProxyObjectFactory policyFactory = mock(PolicyEngineProxyObjectFactory.class);
        PolicyEngineProxy policyEngine = mock(PolicyEngineProxy.class);

        when(pdPolicyChecker.checkOutgoingPolicy(any(RespondingGatewayPRPAIN201305UV02RequestType.class))).thenReturn(Boolean.TRUE);
        when(policyFactory.create()).thenReturn(policyEngine);

        StandardOutboundPatientDiscovery standardPD = createStandardPDObj(getAuditLogger(true));
        when(mockOrchestratable.getAssertion()).thenReturn(assertion);
        when(mockOrchestratable.getRequest()).thenReturn(request.getPRPAIN201305UV02());
        when(mockOrchestratable.getTarget()).thenReturn(target);
        try {
            standardPD.respondingGatewayPRPAIN201305UV02(request, assertion);
        } catch (Exception e) {
            assertTrue(e instanceof ErrorEventException);
        }

        assertNotNull("Assertion MessageId is null", assertion.getMessageId());
        verify(mockEJBLogger).auditRequestMessage(eq(request.getPRPAIN201305UV02()), any(AssertionType.class), any(
            NhinTargetSystemType.class), eq(NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION),
            eq(NhincConstants.AUDIT_LOG_NHIN_INTERFACE), eq(Boolean.TRUE), isNull(Properties.class),
            eq(NhincConstants.PATIENT_DISCOVERY_SERVICE_NAME), any(PatientDiscoveryAuditTransforms.class));
    }

    @Test
    public void auditLoggingTurnedOffForPD() {
        RespondingGatewayPRPAIN201305UV02RequestType request = new RespondingGatewayPRPAIN201305UV02RequestType();
        request.setPRPAIN201305UV02(new PRPAIN201305UV02());
        request.setNhinTargetCommunities(createNhinTargetCommunitiesType(TARGET_HCID));
        AssertionType assertion = new AssertionType();
        request.setAssertion(assertion);
        OutboundPatientDiscoveryOrchestratable outOrchestratable = new OutboundPatientDiscoveryOrchestratable();
        outOrchestratable.setResponse(new PRPAIN201306UV02());

        when(delegate.process(any(OutboundPatientDiscoveryOrchestratable.class))).thenReturn(outOrchestratable);
        StandardOutboundPatientDiscovery standardPatientDiscovery = createStandardPDObj(getAuditLogger(false));
        try {
            standardPatientDiscovery.respondingGatewayPRPAIN201305UV02(request, assertion);
        } catch (Exception e) {
            assertTrue(e instanceof ErrorEventException);
        }
        assertNotNull("Assertion MessageId is null", assertion.getMessageId());
        verify(mockEJBLogger, never()).auditRequestMessage(any(PRPAIN201305UV02.class), any(AssertionType.class),
            any(NhinTargetSystemType.class), eq(NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION),
            eq(NhincConstants.AUDIT_LOG_NHIN_INTERFACE), eq(Boolean.TRUE), isNull(Properties.class),
            eq(NhincConstants.PATIENT_DISCOVERY_SERVICE_NAME), any(PatientDiscoveryAuditTransforms.class));
    }

    private NhinTargetCommunitiesType createNhinTargetCommunitiesType(String hcid) {
        NhinTargetCommunitiesType targetCommunities = new NhinTargetCommunitiesType();
        NhinTargetCommunityType targetCommunity = new NhinTargetCommunityType();
        targetCommunity.setHomeCommunity(createHomeCommunity(hcid));
        targetCommunities.getNhinTargetCommunity().add(targetCommunity);
        return targetCommunities;
    }

    private HomeCommunityType createHomeCommunity(String hcid) {
        HomeCommunityType homeCommunity = new HomeCommunityType();
        homeCommunity.setHomeCommunityId(hcid);
        return homeCommunity;
    }

    private PatientDiscoveryAuditLogger getAuditLogger(final boolean isLoggingOn) {
        return new PatientDiscoveryAuditLogger() {
            @Override
            protected AuditEJBLogger getAuditLogger() {
                return mockEJBLogger;
            }

            @Override
            protected boolean isAuditLoggingOn(String serviceName) {
                return isLoggingOn;
            }
        };
    }

    private List<UrlInfo> getEndPoints(NhinTargetSystemType target) {
        List<UrlInfo> endpoints = new ArrayList<>();
        UrlInfo entry = new UrlInfo();
        entry.setHcid(target.getHomeCommunity().getHomeCommunityId());
        entry.setUrl(PD_URL);
        endpoints.add(entry);
        return endpoints;
    }

    private AssertionType createAssertion() {
        AssertionType assertion = new AssertionType();
        assertion.setMessageId(TARGET_HCID);
        return assertion;
    }

    private StandardOutboundPatientDiscovery createStandardPDObj(final PatientDiscoveryAuditLogger auditLogger) {
        return new StandardOutboundPatientDiscovery() {
            @Override
            protected PatientDiscoveryAuditLogger getAuditLogger() {
                return auditLogger;
            }

            @Override
            protected List<UrlInfo> getEndpoints(NhinTargetCommunitiesType targetCommunities) {
                return getEndPoints(PDMessageGeneratorUtils.getInstance().convertFirstToNhinTargetSystemType(
                    targetCommunities));
            }

            @Override
            protected boolean checkPolicy(RespondingGatewayPRPAIN201305UV02RequestType request) {
                return true;
            }

            @Override
            protected OutboundPatientDiscoveryOrchestratable createOrchestratable(PRPAIN201305UV02 message,
                AssertionType assertion, NhinTargetSystemType target, NhincConstants.GATEWAY_API_LEVEL gatewayLevel) {
                return mockOrchestratable;
            }
        };

    }
}
