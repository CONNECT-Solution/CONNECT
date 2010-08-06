/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.admindistribution.entity;

import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewaySendAlertMessageType;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
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
public class EntityAdministrativeDistributionTest {

    private Mockery context;
    public EntityAdministrativeDistributionTest() {
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
     * Test of sendAlertMessage method, of class EntityAdministrativeDistribution.
     */
    @Test
    public void testSendAlertMessage() {
        System.out.println("sendAlertMessage");
        final RespondingGatewaySendAlertMessageType body = null;


        final EntityAdminDistributionOrchImpl mockImpl = context.mock(EntityAdminDistributionOrchImpl.class);



        EntityAdministrativeDistribution instance = new EntityAdministrativeDistribution(){
        @Override

            protected EntityAdminDistributionOrchImpl getEntityImpl()
            {
                return mockImpl;
            }
        };


        context.checking(new Expectations() {

          {
                allowing(mockImpl).sendAlertMessage(body, body.getAssertion(), body.getNhinTargetCommunities());
                will(returnValue(null));
          }
        });

        instance.sendAlertMessage(body);
        context.assertIsSatisfied();
    }



}