/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.admindistribution.nhin;

import gov.hhs.fha.nhinc.admindistribution.AdminDistributionAuditLogger;
import gov.hhs.fha.nhinc.admindistribution.nhin.proxy.NhinAdminDistObjectFactory;
import gov.hhs.fha.nhinc.admindistribution.nhin.proxy.NhinAdminDistProxy;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import javax.xml.ws.WebServiceContext;
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
public class NhinAdministrativeDistributionTest {

    private Mockery context;
    public NhinAdministrativeDistributionTest() {
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
        final NhinAdminDistOrchImpl mockImpl = context.mock(NhinAdminDistOrchImpl.class);
        final AssertionType assertion = new AssertionType();

        body.setSenderID("test");
        
        NhinAdministrativeDistribution instance = new NhinAdministrativeDistribution()
        {

            protected AssertionType extractAssertion(WebServiceContext context)
            {
                return  assertion;
            }
            protected NhinAdminDistOrchImpl getNhinImpl()
            {
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