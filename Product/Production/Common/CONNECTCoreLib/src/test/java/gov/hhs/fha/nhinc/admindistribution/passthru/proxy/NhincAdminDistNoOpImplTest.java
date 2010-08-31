/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.admindistribution.passthru.proxy;

import gov.hhs.fha.nhinc.admindistribution.passthru.proxy.PassthruAdminDistributionProxyNoOpImpl;
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
 *
 * @author dunnek
 */
public class NhincAdminDistNoOpImplTest {
    private Mockery context;
    public NhincAdminDistNoOpImplTest() {
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
        EDXLDistribution body = null;
        AssertionType assertion = null;
        NhinTargetSystemType target = null;
        PassthruAdminDistributionProxyNoOpImpl instance = new PassthruAdminDistributionProxyNoOpImpl()
{

            @Override
            protected Log createLogger() {
                return mockLogger;
            }
        };
        context.checking(new Expectations() {

            {
                allowing(mockLogger).info(with(any(String.class)));
                allowing(mockLogger).debug(with(any(String.class)));
                will(returnValue(null));
            }
        });

        instance.sendAlertMessage(body, assertion, target);
        context.assertIsSatisfied();
    }

}