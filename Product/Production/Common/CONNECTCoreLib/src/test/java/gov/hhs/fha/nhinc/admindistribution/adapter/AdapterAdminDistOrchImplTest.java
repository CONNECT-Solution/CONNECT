/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.admindistribution.adapter;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import oasis.names.tc.emergency.edxl.de._1.EDXLDistribution;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
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
public class AdapterAdminDistOrchImplTest {


    private Mockery context;
    public AdapterAdminDistOrchImplTest() {
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
       System.out.println("testSendAlertMessage_ServiceEnabled");
        final Log mockLogger = context.mock(Log.class);

        final EDXLDistribution body = new EDXLDistribution();
        final AssertionType assertion = new AssertionType();

        final String stringValue = "test";

        body.setDistributionID(stringValue);
        body.setSenderID(stringValue);
        
        AdapterAdminDistributionOrchImpl instance = new AdapterAdminDistributionOrchImpl()
        {
            @Override
            protected Log createLogger()
            {
                return mockLogger;
            }
        };
        context.checking(new Expectations() {

            {
                exactly(5).of(mockLogger).info(with(any(String.class)));
                exactly(1).of(mockLogger).debug(with(any(String.class)));
                never(mockLogger).warn(with(any(String.class)));
                never(mockLogger).error(with(any(String.class)));
            }
        });
        instance.sendAlertMessage(body, assertion);
    }

}