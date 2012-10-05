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
package gov.hhs.fha.nhinc.admindistribution.nhin;

import static org.junit.Assert.assertNull;
import oasis.names.tc.emergency.edxl.de._1.EDXLDistribution;

import org.apache.commons.logging.Log;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Test;

import gov.hhs.fha.nhinc.admindistribution.AdminDistributionAuditLogger;
import gov.hhs.fha.nhinc.admindistribution.AdminDistributionPolicyChecker;
import gov.hhs.fha.nhinc.admindistribution.adapter.proxy.AdapterAdminDistributionProxy;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;

/**
 *
 * @author dunnek
 */
public class NhinAdminDistOrchImplTest {

    private Mockery context;

    public NhinAdminDistOrchImplTest() {
    }

    @Before
    public void setup() {
        context = new Mockery() {

            {
                setImposteriser(ClassImposteriser.INSTANCE);
            }
        };
    }

    @Test
    public void testSendAlertMessage_ServiceEnabled() {

        System.out.println("testSendAlertMessage_ServiceEnabled");
        final Log mockLogger = context.mock(Log.class);
        final AdapterAdminDistributionProxy mockAdapter = context.mock(AdapterAdminDistributionProxy.class);
        final AdminDistributionAuditLogger mockAuditLogger = context.mock(AdminDistributionAuditLogger.class);

        final EDXLDistribution body = new EDXLDistribution();
        final AssertionType assertion = new AssertionType();

        Exception unsupported = null;

        final AdminDistributionPolicyChecker policyChecker = new AdminDistributionPolicyChecker() {
            @Override
            public boolean checkIncomingPolicy(EDXLDistribution request, AssertionType assertion) {
                {
                    return true;
                }
            }
        };

        NhinAdminDistributionOrchImpl instance = new NhinAdminDistributionOrchImpl() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }

            @Override
            protected AdminDistributionAuditLogger getLogger() {
                return mockAuditLogger;
            }

            @Override
            protected AdapterAdminDistributionProxy getAdapterAdminDistProxy() {
                return mockAdapter;
            }

            @Override
            protected AdminDistributionPolicyChecker getPolicyChecker() {
                return policyChecker;
            }

            @Override
            protected long getSleepPeriod() {
                return 1;
            }
        };
        context.checking(new Expectations() {

            {

                allowing(mockLogger).info(with(any(String.class)));
                allowing(mockLogger).debug(with(any(String.class)));
                never(mockLogger).warn(with(any(String.class)));
                never(mockLogger).error(with(any(String.class)));

                allowing(mockAuditLogger).auditNhinAdminDist(body, assertion,
                        NhincConstants.AUDIT_LOG_INBOUND_DIRECTION,
                        NhincConstants.AUDIT_LOG_NHIN_INTERFACE);
                allowing(mockAuditLogger).auditNhinAdminDist(body, assertion,
                        NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION,
                        NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE);
                allowing(mockAdapter).sendAlertMessage(body, assertion);
                will(returnValue(null));
            }
        });

        try {
            instance.sendAlertMessage(body, assertion);
        } catch (Exception ex) {
            unsupported = ex;
        }

        context.assertIsSatisfied();
        assertNull(unsupported);
    }

    @Test
    public void testSendAlertMessage_NoSleep() {

        final Log mockLogger = context.mock(Log.class);
        final AdapterAdminDistributionProxy mockAdapter = context.mock(AdapterAdminDistributionProxy.class);
        final AdminDistributionAuditLogger mockAuditLogger = context.mock(AdminDistributionAuditLogger.class);

        final EDXLDistribution body = new EDXLDistribution();
        final AssertionType assertion = new AssertionType();

        Exception unsupported = null;

        final AdminDistributionPolicyChecker policyChecker = new AdminDistributionPolicyChecker() {
            @Override
            public boolean checkIncomingPolicy(EDXLDistribution request, AssertionType assertion) {
                {
                    return true;
                }
            }
        };

        NhinAdminDistributionOrchImpl instance = new NhinAdminDistributionOrchImpl() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }

            @Override
            protected AdminDistributionAuditLogger getLogger() {
                return mockAuditLogger;
            }

            @Override
            protected AdapterAdminDistributionProxy getAdapterAdminDistProxy() {
                return mockAdapter;
            }

            @Override
            protected AdminDistributionPolicyChecker getPolicyChecker() {
                return policyChecker;
            }

            @Override
            protected long getSleepPeriod() {
                return -1;
            }
        };
        context.checking(new Expectations() {

            {
                allowing(mockLogger).info(with(any(String.class)));
                allowing(mockLogger).debug(with(any(String.class)));
                never(mockLogger).warn(with(any(String.class)));
                never(mockLogger).error(with(any(String.class)));
                allowing(mockAuditLogger).auditNhinAdminDist(body, assertion,
                        NhincConstants.AUDIT_LOG_INBOUND_DIRECTION,
                        NhincConstants.AUDIT_LOG_NHIN_INTERFACE);
                allowing(mockAuditLogger).auditNhinAdminDist(body, assertion,
                        NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION,
                        NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE);
                allowing(mockAdapter).sendAlertMessage(body, assertion);
                will(returnValue(null));
            }
        });

        try {
            instance.sendAlertMessage(body, assertion);
        } catch (Exception ex) {
            unsupported = ex;
        }

        context.assertIsSatisfied();
        assertNull(unsupported);
    }

    @Test
    public void testSendAlertMessage_Null() {

        System.out.println("testSendAlertMessage_Null");
        final Log mockLogger = context.mock(Log.class);
        final AdapterAdminDistributionProxy mockAdapter = context.mock(AdapterAdminDistributionProxy.class);
        final AdminDistributionAuditLogger mockAuditLogger = context.mock(AdminDistributionAuditLogger.class);

        final EDXLDistribution body = new EDXLDistribution();
        final AssertionType assertion = new AssertionType();

        Exception unsupported = null;

        final AdminDistributionPolicyChecker policyChecker = new AdminDistributionPolicyChecker() {
            @Override
            public boolean checkIncomingPolicy(EDXLDistribution request, AssertionType assertion) {
                {
                    return true;
                }
            }
        };

        NhinAdminDistributionOrchImpl instance = new NhinAdminDistributionOrchImpl() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }

            @Override
            protected AdminDistributionAuditLogger getLogger() {
                return mockAuditLogger;
            }

            @Override
            protected AdapterAdminDistributionProxy getAdapterAdminDistProxy() {
                return mockAdapter;
            }

            @Override
            protected AdminDistributionPolicyChecker getPolicyChecker() {
                return policyChecker;
            }

            @Override
            protected long getSleepPeriod() {
                return -1;
            }
        };
        context.checking(new Expectations() {

            {
                allowing(mockLogger).info(with(any(String.class)));
                allowing(mockLogger).debug(with(any(String.class)));
                oneOf(mockLogger).warn(with(any(String.class)));
                never(mockLogger).error(with(any(String.class)));
                allowing(mockAuditLogger).auditNhinAdminDist(null, null, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION,
                        NhincConstants.AUDIT_LOG_NHIN_INTERFACE);
                allowing(mockAdapter).sendAlertMessage(body, assertion);
                will(returnValue(null));
            }
        });

        try {
            instance.sendAlertMessage(null, null);
        } catch (Exception ex) {
            unsupported = ex;
        }

        context.assertIsSatisfied();
        assertNull(unsupported);
    }

    @Test
    public void testSendAlertMessage_PolicyFail() {

        System.out.println("testSendAlertMessage_PolicyFail");
        final Log mockLogger = context.mock(Log.class);
        final AdapterAdminDistributionProxy mockAdapter = context.mock(AdapterAdminDistributionProxy.class);
        final AdminDistributionAuditLogger mockAuditLogger = context.mock(AdminDistributionAuditLogger.class);

        final EDXLDistribution body = new EDXLDistribution();
        final AssertionType assertion = new AssertionType();

        Exception unsupported = null;

        final AdminDistributionPolicyChecker policyChecker = new AdminDistributionPolicyChecker() {
            @Override
            public boolean checkIncomingPolicy(EDXLDistribution request, AssertionType assertion) {
                {
                    return false;
                }
            }
        };

        NhinAdminDistributionOrchImpl instance = new NhinAdminDistributionOrchImpl() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }

            @Override
            protected AdminDistributionAuditLogger getLogger() {
                return mockAuditLogger;
            }

            @Override
            protected AdapterAdminDistributionProxy getAdapterAdminDistProxy() {
                return mockAdapter;
            }

            @Override
            protected AdminDistributionPolicyChecker getPolicyChecker() {
                return policyChecker;
            }

            @Override
            protected long getSleepPeriod() {
                return 1;
            }
        };
        context.checking(new Expectations() {

            {

                allowing(mockLogger).info(with(any(String.class)));
                allowing(mockLogger).debug(with(any(String.class)));
                never(mockLogger).warn(with(any(String.class)));
                never(mockLogger).error(with(any(String.class)));

                allowing(mockAuditLogger).auditNhinAdminDist(body, assertion,
                        NhincConstants.AUDIT_LOG_INBOUND_DIRECTION,
                        NhincConstants.AUDIT_LOG_NHIN_INTERFACE);
                never(mockAdapter).sendAlertMessage(body, assertion);
                will(returnValue(null));
            }
        });

        try {
            instance.sendAlertMessage(body, assertion);
        } catch (Exception ex) {
            unsupported = ex;
        }

        context.assertIsSatisfied();
        assertNull(unsupported);
    }
}