/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.admindistribution_g1.nhin;

import gov.hhs.fha.nhinc.admindistribution.nhin.*;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import javax.xml.ws.WebServiceContext;
import oasis.names.tc.emergency.edxl.de._1.EDXLDistribution;
import org.junit.Before;
import org.junit.Test;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;

/**
 *
 * @author dunnek
 */
public class NhinAdministrativeDistributionTest_g1 {

    private Mockery context;

    public NhinAdministrativeDistributionTest_g1() {
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
     * Test of sendAlertMessage method, of class NhinAdministrativeDistribution.
     */
    @Test
    public void testSendAlertMessage() {
        System.out.println("sendAlertMessage");
        final EDXLDistribution body = new EDXLDistribution();
        final NhinAdminDistributionOrchImpl mockImpl = context.mock(NhinAdminDistributionOrchImpl.class);
        final AssertionType assertion = new AssertionType();

        body.setSenderID("test");

        NhinAdministrativeDistribution_g1 instance = new NhinAdministrativeDistribution_g1() {

            protected AssertionType extractAssertion(WebServiceContext context) {
                return assertion;
            }

            protected NhinAdminDistributionOrchImpl getNhinImpl() {
                return mockImpl;
            }
        };
        context.checking(new Expectations() {

            {
                allowing(mockImpl).sendAlertMessage(body, assertion);
                will(returnValue(null));
            }
        });

        instance.sendAlertMessage(body);
        context.assertIsSatisfied();
    }
}