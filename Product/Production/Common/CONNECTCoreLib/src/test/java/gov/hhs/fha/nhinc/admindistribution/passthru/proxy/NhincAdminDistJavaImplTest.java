/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.admindistribution.passthru.proxy;

import gov.hhs.fha.nhinc.admindistribution.passthru.proxy.PassthruAdminDistributionProxyJavaImpl;
import gov.hhs.fha.nhinc.admindistribution.passthru.PassthruAdminDistributionOrchImpl;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
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
/**
 *
 * @author dunnek
 */
public class NhincAdminDistJavaImplTest {
    private Mockery context;
    public NhincAdminDistJavaImplTest() {
    }

    @Before
    public void setup() {
        context = new Mockery() {

            {
                setImposteriser(ClassImposteriser.INSTANCE);
            }
        };
    }


    /**
     * Test of sendAlertMessage method, of class NhincAdminDistNoOpImpl.
     */
    @Test
    public void testSendAlertMessage() {
        System.out.println("sendAlertMessage");
        final Log mockLogger = context.mock(Log.class);
        final PassthruAdminDistributionOrchImpl mockImpl = context.mock(PassthruAdminDistributionOrchImpl.class);
        final EDXLDistribution body = null;
        final AssertionType assertion = null;
        final NhinTargetSystemType target = null;
        
        PassthruAdminDistributionProxyJavaImpl instance = new PassthruAdminDistributionProxyJavaImpl()
{

            @Override
            protected Log createLogger() {
                return mockLogger;
            }
            @Override
            protected PassthruAdminDistributionOrchImpl getNhincAdminDistImpl()
            {
                return mockImpl;
            }
        };
        context.checking(new Expectations() {

            {
                allowing(mockLogger).info(with(any(String.class)));
                allowing(mockLogger).debug(with(any(String.class)));
                allowing(mockImpl).sendAlertMessage(body, assertion, target);
                will(returnValue(null));
            }
        });

        instance.sendAlertMessage(body, assertion, target);
        context.assertIsSatisfied();
    }

}