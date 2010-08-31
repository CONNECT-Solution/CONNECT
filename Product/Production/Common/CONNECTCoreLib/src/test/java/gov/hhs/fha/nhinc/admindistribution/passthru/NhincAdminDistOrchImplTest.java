/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.admindistribution.passthru;

import gov.hhs.fha.nhinc.admindistribution.passthru.PassthruAdminDistributionOrchImpl;
import gov.hhs.fha.nhinc.admindistribution.AdminDistributionAuditLogger;
import gov.hhs.fha.nhinc.admindistribution.nhin.proxy.NhinAdminDistributionProxyObjectFactory;
import gov.hhs.fha.nhinc.admindistribution.nhin.proxy.NhinAdminDistributionProxy;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import oasis.names.tc.emergency.edxl.de._1.EDXLDistribution;
import org.apache.commons.logging.Log;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.logging.Log;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;

/**
 *
 * @author dunnek
 */
public class NhincAdminDistOrchImplTest {

    private Mockery context;
    public NhincAdminDistOrchImplTest() {
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
    public void testSendAlertMessage() {
        System.out.println("sendAlertMessage");
        final Log mockLogger = context.mock(Log.class);
        final NhinAdminDistributionProxy mockNhin = context.mock(NhinAdminDistributionProxy.class);
        final AdminDistributionAuditLogger mockAuditLogger = context.mock(AdminDistributionAuditLogger.class);
        
        final EDXLDistribution body = null;
        final AssertionType assertion = null;
        final NhinTargetSystemType target = null;
        Exception unsupported = null;

        PassthruAdminDistributionOrchImpl instance = new PassthruAdminDistributionOrchImpl()
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
            protected NhinAdminDistributionProxy getNhinProxy()
            {
                return mockNhin;
            }
         };
        context.checking(new Expectations() {

            {
                allowing(mockLogger).info(with(any(String.class)));
                allowing(mockLogger).debug(with(any(String.class)));
                allowing(mockAuditLogger).auditNhincAdminDist(body, assertion, target, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION);
                allowing(mockNhin).sendAlertMessage(body, assertion, target);
                will(returnValue(null));
            }
        });

        try
        {
            instance.sendAlertMessage(body, assertion, target);
        }
        catch(Exception ex)
        {
            unsupported = ex;
        }

        context.assertIsSatisfied();
        assertNull(unsupported);
    }

}