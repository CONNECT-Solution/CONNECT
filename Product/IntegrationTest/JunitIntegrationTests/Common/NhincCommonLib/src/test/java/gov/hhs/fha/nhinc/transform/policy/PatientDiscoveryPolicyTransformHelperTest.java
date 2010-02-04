/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.transform.policy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import javax.xml.bind.JAXBElement;
import oasis.names.tc.xacml._2_0.context.schema.os.RequestType;
import org.apache.commons.logging.Log;
import org.hl7.v3.II;
import org.hl7.v3.MCCIMT000100UV01Agent;
import org.hl7.v3.MCCIMT000100UV01Device;
import org.hl7.v3.MCCIMT000100UV01Organization;
import org.hl7.v3.MCCIMT000100UV01Sender;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author svalluripalli
 */
public class PatientDiscoveryPolicyTransformHelperTest {

    private Mockery context;

    public PatientDiscoveryPolicyTransformHelperTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        context = new Mockery(){{
            setImposteriser(ClassImposteriser.INSTANCE);
        }};
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testTransformPatientDiscoveryEntityToCheckPolicyWillFailForNullRequest() {
        final Log mockLogger = context.mock(Log.class);
        PatientDiscoveryPolicyTransformHelper testSubject = new PatientDiscoveryPolicyTransformHelper() {
            @Override
            protected Log createLogger() {
                return mockLogger;
            }
        };
        context.checking(new Expectations() {
            {
                exactly(1).of(mockLogger).debug(with(any(String.class)));
                //Input Request Object can not be null
                exactly(1).of(mockLogger).error("Request is null.");
                will(returnValue(with(any(CheckPolicyRequestType.class))));
            }
        });
        testSubject.transformPatientDiscoveryEntityToCheckPolicy(null);
        context.assertIsSatisfied();
    }

    //                Expected Response should be validated.
    //                At least one RequestType
    //                At least one SubjectType
    //                Set Action on RequestType
    //                Set SubjectType on RequestType
    //                Create Subject and Add to RequestType
    //                ResourceType add to RequestType

    @Ignore
    @Test
    public void testTransformPatientDiscoveryEntityToCheckPolicyWillPass() {
        final Log mockLogger = context.mock(Log.class);
        CheckPolicyRequestType expectedReturnValue = new CheckPolicyRequestType();
        expectedReturnValue.setAssertion(new AssertionType());
        expectedReturnValue.setRequest(new RequestType());
        RespondingGatewayPRPAIN201305UV02RequestType event = new RespondingGatewayPRPAIN201305UV02RequestType();
        getHomeCommunityType(event);
        PatientDiscoveryPolicyTransformHelper testSubject = new PatientDiscoveryPolicyTransformHelper() {
            @Override
            protected Log createLogger() {
                return mockLogger;
            }
        };
        context.checking(new Expectations() {
            {
                exactly(4).of(mockLogger).debug(with(any(String.class)));
                will(returnValue(with(any(CheckPolicyRequestType.class))));
            }
        });
        testSubject.transformPatientDiscoveryEntityToCheckPolicy(event);
        context.assertIsSatisfied();
    }

    @Test
    public void testGetRequestTypeWillPass() {
        final Log mockLogger = context.mock(Log.class);
        final RequestType request = new RequestType();
        RespondingGatewayPRPAIN201305UV02RequestType event = new RespondingGatewayPRPAIN201305UV02RequestType();
        getHomeCommunityType(event);
        PatientDiscoveryPolicyTransformHelper testSubject = new PatientDiscoveryPolicyTransformHelper() {
            @Override
            protected Log createLogger() {
                return mockLogger;
            }
        };
        context.checking(new Expectations(){{
            exactly(2).of(mockLogger).debug(with(any(String.class)));
            will(returnValue(with(any(RequestType.class))));
        }});
        testSubject.getRequestType(event);
        context.assertIsSatisfied();
    }

    @Test
    public void testGetHomeCommunityFrom201305()
    {
        RespondingGatewayPRPAIN201305UV02RequestType event = new RespondingGatewayPRPAIN201305UV02RequestType();
        HomeCommunityType expectedReturnvalue = getHomeCommunityType(event);
        PatientDiscoveryPolicyTransformHelper testSubject = new PatientDiscoveryPolicyTransformHelper();
        Assert.assertEquals(expectedReturnvalue.getHomeCommunityId(), "1.1");
        testSubject.getHomeCommunityFrom201305(event);
        context.assertIsSatisfied();
    }

    protected HomeCommunityType getHomeCommunityType(RespondingGatewayPRPAIN201305UV02RequestType event) {
        PRPAIN201305UV02 request201305 = new PRPAIN201305UV02();
        MCCIMT000100UV01Sender sender = new MCCIMT000100UV01Sender();
        MCCIMT000100UV01Device device = new MCCIMT000100UV01Device();
        II e = new II();
        e.setRoot("1.1");

        MCCIMT000100UV01Agent agentVal = new MCCIMT000100UV01Agent();

        MCCIMT000100UV01Organization repOrgVal = new MCCIMT000100UV01Organization();
        repOrgVal.getId().add(e);

        org.hl7.v3.ObjectFactory oJaxbObjectFactory = new org.hl7.v3.ObjectFactory();
        JAXBElement<MCCIMT000100UV01Organization> repOrg = oJaxbObjectFactory.createMCCIMT000100UV01AgentRepresentedOrganization(repOrgVal);
        repOrg.setValue(repOrgVal);
        agentVal.setRepresentedOrganization(repOrg);

        JAXBElement<MCCIMT000100UV01Agent> agent = oJaxbObjectFactory.createMCCIMT000100UV01DeviceAsAgent(agentVal);
        device.setAsAgent(agent);
        sender.setDevice(device);
        request201305.setSender(sender);
        event.setPRPAIN201305UV02(request201305);
        final HomeCommunityType expectedReturnvalue = new HomeCommunityType();
        expectedReturnvalue.setHomeCommunityId("1.1");
        return expectedReturnvalue;
    }
}
