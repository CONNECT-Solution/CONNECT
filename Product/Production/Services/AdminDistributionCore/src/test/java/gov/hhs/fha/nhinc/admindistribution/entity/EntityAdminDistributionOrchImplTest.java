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
package gov.hhs.fha.nhinc.admindistribution.entity;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;

import gov.hhs.fha.nhinc.admindistribution.AdminDistributionAuditLogger;
import gov.hhs.fha.nhinc.admindistribution.AdminDistributionPolicyChecker;
import gov.hhs.fha.nhinc.admindistribution.passthru.proxy.PassthruAdminDistributionProxy;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewaySendAlertMessageType;
import gov.hhs.fha.nhinc.connectmgr.UrlInfo;

/**
 * @author zmelnick
 *
 */
public class EntityAdminDistributionOrchImplTest {

    protected Mockery context = new JUnit4Mockery() {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };

    final Log mockLog = context.mock(Log.class);
    final AdminDistributionAuditLogger mockAuditLog = context.mock(AdminDistributionAuditLogger.class);
    final AdminDistributionPolicyChecker mockPolicyCheck = context.mock(AdminDistributionPolicyChecker.class);
    final OutboundAdminDistributionDelegate mockDelegate = context.mock(OutboundAdminDistributionDelegate.class);
    final PassthruAdminDistributionProxy mockPassthruProxy = context.mock(PassthruAdminDistributionProxy.class);

    final EntityAdminDistributionOrchImpl mockImpl = createMockEntityADOrchImpl();

    @Test
    public void testSendAlertMessage_PolicyOK() {
        expectMockAudits(2);
        setMockPolicyCheck(true);
        expectationsInAlertMessage();

        runSendAlertMessage();
        context.assertIsSatisfied();
    }

    @Test
    public void testSendAlertMessage_PolicyFail() {
        expectMockAudits(1);
        setMockPolicyCheck(false);
        setPolicyFailExpectations();

        runSendAlertMessage();
        context.assertIsSatisfied();
    }

    /**
     * Runs a standard SendAlertMessage() for AD.
     */
    private void runSendAlertMessage() {
        RespondingGatewaySendAlertMessageType message = new RespondingGatewaySendAlertMessageType();
        AssertionType assertion = new AssertionType();
        NhinTargetCommunitiesType target = new NhinTargetCommunitiesType();

        EntityAdminDistributionOrchImpl entityOrch = createMockEntityADOrchImpl();
        entityOrch.sendAlertMessage(message, assertion, target);
    }

    /*-----------------Expectation Methods---------------*/

    /**
     * Expectations for successful sendAlertMessage().
     */
    private void expectationsInAlertMessage() {
        allowAnyMockLogging();
        expectProcessOrchestratable();
    }

    /**
     * Expectations for sendAlertMessage() failed policy check.
     */
    private void setPolicyFailExpectations() {
        expectErrorLogInvocation();
        expectProcessOrchestratable_fail();
    }

    /**
     * Sets up expectations for Error Log being invoked.
     */
    private void expectErrorLogInvocation() {
        context.checking(new Expectations() {
            {
                atLeast(1).of(mockLog).error(with(any(String.class)));
                allowing(mockLog).debug(with(any(String.class)));
                allowing(mockLog).warn(with(any(String.class)));
            }
        });

    }

    private void expectMockAudits(final int n) {
        context.checking(new Expectations() {
            {
                exactly(n).of(mockAuditLog).auditEntityAdminDist(
                        with(any(RespondingGatewaySendAlertMessageType.class)), with(any(AssertionType.class)),
                        with(any(String.class)));
            }
        });
    }

    private void setMockPolicyCheck(final boolean allow) {
        context.checking(new Expectations() {
            {
                oneOf(mockPolicyCheck).checkOutgoingPolicy(with(any(RespondingGatewaySendAlertMessageType.class)),
                        with(any(String.class)));
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

    private void expectProcessOrchestratable() {
        context.checking(new Expectations() {
            {
                oneOf(mockDelegate).process(with(any(OutboundAdminDistributionOrchestratable.class)));
                will(returnValue(null));
            }
        });
    }

    private void expectProcessOrchestratable_fail() {
        context.checking(new Expectations() {
            {
                never(mockDelegate).process(with(any(OutboundAdminDistributionOrchestratable.class)));
                will(returnValue(null));
            }
        });
    }

    /**
     * Creates the Mock EntityAdminDistributionOrchImpl, with mock members.
     *
     * @return the mock EntityAdminDistributionOrchImpl
     */
    private EntityAdminDistributionOrchImpl createMockEntityADOrchImpl() {
        return new EntityAdminDistributionOrchImpl() {

            @Override
            protected Log getLog() {
                return mockLog;
            }

            @Override
            protected AdminDistributionAuditLogger getAuditLogger() {
                return mockAuditLog;
            }

            @Override
            protected PassthruAdminDistributionProxy getNhincAdminDist() {
                return mockPassthruProxy;
            }

            @Override
            protected List<UrlInfo> getEndpoints(NhinTargetCommunitiesType targetCommunities) {
                List<UrlInfo> endpoints = new ArrayList<UrlInfo>();
                endpoints.add(new UrlInfo());
                return endpoints;
            }

            @Override
            protected OutboundAdminDistributionDelegate getNewOutboundAdminDistributionDelegate() {
                return mockDelegate;
            }

            @Override
            protected boolean checkPolicy(RespondingGatewaySendAlertMessageType request, AssertionType assertion,
                    String target) {
                return mockPolicyCheck.checkOutgoingPolicy(request, target);
            }

        };

    }
}
