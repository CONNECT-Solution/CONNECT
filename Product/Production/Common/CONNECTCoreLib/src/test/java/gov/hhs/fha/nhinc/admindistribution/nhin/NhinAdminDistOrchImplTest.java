/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.admindistribution.nhin;

import gov.hhs.fha.nhinc.admindistribution.AdminDistributionAuditLogger;
import gov.hhs.fha.nhinc.admindistribution.AdminDistributionPolicyChecker;
import gov.hhs.fha.nhinc.admindistribution.adapter.proxy.AdapterAdminDistributionProxy;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import oasis.names.tc.emergency.edxl.de._1.EDXLDistribution;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.junit.Assert.*;
import org.apache.commons.logging.Log;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;

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

        final AdminDistributionPolicyChecker policyChecker = new AdminDistributionPolicyChecker()
        {
            @Override
            public boolean checkIncomingPolicy (EDXLDistribution request, AssertionType assertion) {
                {
                    return true;
                }
            }
        };
        
        NhinAdminDistributionOrchImpl instance = new NhinAdminDistributionOrchImpl()
        {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }
            @Override
            protected AdminDistributionAuditLogger getLogger()
            {
                return mockAuditLogger;
            }
            @Override
            protected AdapterAdminDistributionProxy getAdapterAdminDistProxy()
            {
                return mockAdapter;
            }
            @Override
            protected boolean isServiceEnabled()
            {
                return true;
            }
            @Override
            protected AdminDistributionPolicyChecker getPolicyChecker()
            {
                return policyChecker;
            }
            @Override
            protected long getSleepPeriod()
            {
                return 1;
            }
         };
        context.checking(new Expectations() {

            {
                
                allowing(mockLogger).info(with(any(String.class)));
                allowing(mockLogger).debug(with(any(String.class)));
                never(mockLogger).warn(with(any(String.class)));
                never(mockLogger).error(with(any(String.class)));
                
                
                allowing(mockAuditLogger).auditNhinAdminDist(body, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION);
                allowing(mockAdapter).sendAlertMessage(body, assertion);
                will(returnValue(null));
            }
        });

        try
        {
            instance.sendAlertMessage(body, assertion);
        }
        catch(Exception ex)
        {
            unsupported = ex;
        }
        
        context.assertIsSatisfied();
        assertNull(unsupported);
    }
    @Test
    public void testSendAlertMessage_NoSleep() {

       System.out.println("testSendAlertMessage_NoSleep");
        final Log mockLogger = context.mock(Log.class);
        final AdapterAdminDistributionProxy mockAdapter = context.mock(AdapterAdminDistributionProxy.class);
        final AdminDistributionAuditLogger mockAuditLogger = context.mock(AdminDistributionAuditLogger.class);

        final EDXLDistribution body = new EDXLDistribution();
        final AssertionType assertion = new AssertionType();

        Exception unsupported = null;

        final AdminDistributionPolicyChecker policyChecker = new AdminDistributionPolicyChecker()
        {
            @Override
            public boolean checkIncomingPolicy (EDXLDistribution request, AssertionType assertion) {
                {
                    return true;
                }
            }
        };

        NhinAdminDistributionOrchImpl instance = new NhinAdminDistributionOrchImpl()
        {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }
            @Override
            protected AdminDistributionAuditLogger getLogger()
            {
                return mockAuditLogger;
            }
            @Override
            protected AdapterAdminDistributionProxy getAdapterAdminDistProxy()
            {
                return mockAdapter;
            }
            @Override
            protected boolean isServiceEnabled()
            {
                return true;
            }
            @Override
            protected AdminDistributionPolicyChecker getPolicyChecker()
            {
                return policyChecker;
            }
            @Override
            protected long getSleepPeriod()
            {
                return -1;
            }
         };
        context.checking(new Expectations() {

            {
                allowing(mockLogger).info(with(any(String.class)));
                allowing(mockLogger).debug(with(any(String.class)));
                never(mockLogger).warn(with(any(String.class)));
                never(mockLogger).error(with(any(String.class)));
                allowing(mockAuditLogger).auditNhinAdminDist(body, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION);
                allowing(mockAdapter).sendAlertMessage(body, assertion);
                will(returnValue(null));
            }
        });

        try
        {
            instance.sendAlertMessage(body, assertion);
        }
        catch(Exception ex)
        {
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

        final AdminDistributionPolicyChecker policyChecker = new AdminDistributionPolicyChecker()
        {
            @Override
            public boolean checkIncomingPolicy (EDXLDistribution request, AssertionType assertion) {
                {
                    return true;
                }
            }
        };

        NhinAdminDistributionOrchImpl instance = new NhinAdminDistributionOrchImpl()
        {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }
            @Override
            protected AdminDistributionAuditLogger getLogger()
            {
                return mockAuditLogger;
            }
            @Override
            protected AdapterAdminDistributionProxy getAdapterAdminDistProxy()
            {
                return mockAdapter;
            }
            @Override
            protected boolean isServiceEnabled()
            {
                return true;
            }
            @Override
            protected AdminDistributionPolicyChecker getPolicyChecker()
            {
                return policyChecker;
            }
            @Override
            protected long getSleepPeriod()
            {
                return -1;
            }
         };
        context.checking(new Expectations() {

            {
                allowing(mockLogger).info(with(any(String.class)));
                allowing(mockLogger).debug(with(any(String.class)));
                oneOf(mockLogger).warn(with(any(String.class)));
                never(mockLogger).error(with(any(String.class)));
                allowing(mockAuditLogger).auditNhinAdminDist(null, null, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION);
                allowing(mockAdapter).sendAlertMessage(body, assertion);
                will(returnValue(null));
            }
        });

        try
        {
            instance.sendAlertMessage(null, null);
        }
        catch(Exception ex)
        {
            unsupported = ex;
        }

        context.assertIsSatisfied();
        assertNull(unsupported);
    }

    @Test
    public void testSendAlertMessage_ServiceDisabled() {

       System.out.println("testSendAlertMessage_ServiceDisabled");
        final Log mockLogger = context.mock(Log.class);
        final AdapterAdminDistributionProxy mockAdapter = context.mock(AdapterAdminDistributionProxy.class);
        final AdminDistributionAuditLogger mockAuditLogger = context.mock(AdminDistributionAuditLogger.class);

        final EDXLDistribution body = new EDXLDistribution();
        final AssertionType assertion = new AssertionType();

        Exception unsupported = null;

        final AdminDistributionPolicyChecker policyChecker = new AdminDistributionPolicyChecker()
        {
            @Override
            public boolean checkIncomingPolicy (EDXLDistribution request, AssertionType assertion) {
                {
                    return true;
                }
            }
        };

        NhinAdminDistributionOrchImpl instance = new NhinAdminDistributionOrchImpl()
        {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }
            @Override
            protected AdminDistributionAuditLogger getLogger()
            {
                return mockAuditLogger;
            }
            @Override
            protected AdapterAdminDistributionProxy getAdapterAdminDistProxy()
            {
                return mockAdapter;
            }
            @Override
            protected boolean isServiceEnabled()
            {
                return false;
            }
            @Override
            protected AdminDistributionPolicyChecker getPolicyChecker()
            {
                return policyChecker;
            }
            @Override
            protected long getSleepPeriod()
            {
                return 1;
            }
         };
        context.checking(new Expectations() {

            {

                allowing(mockLogger).info(with(any(String.class)));
                allowing(mockLogger).debug(with(any(String.class)));
                oneOf(mockLogger).warn(with(any(String.class)));
                never(mockLogger).error(with(any(String.class)));


                allowing(mockAuditLogger).auditNhinAdminDist(body, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION);
                never(mockAdapter).sendAlertMessage(body, assertion);
                will(returnValue(null));
            }
        });

        try
        {
            instance.sendAlertMessage(body, assertion);
        }
        catch(Exception ex)
        {
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

        final AdminDistributionPolicyChecker policyChecker = new AdminDistributionPolicyChecker()
        {
            @Override
            public boolean checkIncomingPolicy (EDXLDistribution request, AssertionType assertion) {
                {
                    return false;
                }
            }
        };

        NhinAdminDistributionOrchImpl instance = new NhinAdminDistributionOrchImpl()
        {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }
            @Override
            protected AdminDistributionAuditLogger getLogger()
            {
                return mockAuditLogger;
            }
            @Override
            protected AdapterAdminDistributionProxy getAdapterAdminDistProxy()
            {
                return mockAdapter;
            }
            @Override
            protected boolean isServiceEnabled()
            {
                return true;
            }
            @Override
            protected AdminDistributionPolicyChecker getPolicyChecker()
            {
                return policyChecker;
            }
            @Override
            protected long getSleepPeriod()
            {
                return 1;
            }
         };
        context.checking(new Expectations() {

            {

                allowing(mockLogger).info(with(any(String.class)));
                allowing(mockLogger).debug(with(any(String.class)));
                never(mockLogger).warn(with(any(String.class)));
                never(mockLogger).error(with(any(String.class)));


                allowing(mockAuditLogger).auditNhinAdminDist(body, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION);
                never(mockAdapter).sendAlertMessage(body, assertion);
                will(returnValue(null));
            }
        });

        try
        {
            instance.sendAlertMessage(body, assertion);
        }
        catch(Exception ex)
        {
            unsupported = ex;
        }

        context.assertIsSatisfied();
        assertNull(unsupported);
    }
}