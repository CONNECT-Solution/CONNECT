/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.admindistribution_g1.entity;

import gov.hhs.fha.nhinc.admindistribution.entity.*;
import javax.xml.ws.WebServiceContext;

import org.junit.Before;
import org.junit.Test;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;

import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewaySendAlertMessageSecuredType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
/**
 *
 * @author dunnek
 */
public class EntityAdministrativeDistributionSecuredTest_g1 {

    private Mockery context;
    public EntityAdministrativeDistributionSecuredTest_g1() {
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
     * Test of sendAlertMessage method, of class EntityAdministrativeDistributionSecured.
     */

    @Test
    public void testSendAlertMessage() {
        System.out.println("sendAlertMessage");
        final RespondingGatewaySendAlertMessageSecuredType body = new RespondingGatewaySendAlertMessageSecuredType();

        final AssertionType assertion = new AssertionType();
        final EntityAdminDistributionOrchImpl mockImpl = context.mock(EntityAdminDistributionOrchImpl.class);
        final NhinTargetCommunitiesType targets = new NhinTargetCommunitiesType();

       
        EntityAdministrativeDistributionSecured_g1 instance = new EntityAdministrativeDistributionSecured_g1(){
        @Override
            protected AssertionType extractAssertion(WebServiceContext context)
            {
                return assertion;
            }
            protected EntityAdminDistributionOrchImpl getEntityImpl()
            {
                return mockImpl;
            }
        };


        context.checking(new Expectations() {

          {
                allowing(mockImpl).sendAlertMessage(with(any(RespondingGatewaySendAlertMessageSecuredType.class)),with(any(AssertionType.class)),with(any(NhinTargetCommunitiesType.class)));
                will(returnValue(null));
          }
        });

        instance.sendAlertMessage(body);
        context.assertIsSatisfied();
    }


}