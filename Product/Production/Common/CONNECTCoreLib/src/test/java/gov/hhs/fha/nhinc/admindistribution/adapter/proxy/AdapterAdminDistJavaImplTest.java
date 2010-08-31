/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.admindistribution.adapter.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import oasis.names.tc.emergency.edxl.de._1.EDXLDistribution;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.logging.Log;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
/**
 *
 * @author dunnek
 */
public class AdapterAdminDistJavaImplTest {

    private Mockery context;
    public AdapterAdminDistJavaImplTest() {
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

        final EDXLDistribution body = new EDXLDistribution();


        AdapterAdminDistributionProxyJavaImpl instance = new AdapterAdminDistributionProxyJavaImpl()
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

        instance.sendAlertMessage(body, new AssertionType());
        context.assertIsSatisfied();
    }
}