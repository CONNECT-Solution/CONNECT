/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.transform.policy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.transform.subdisc.HL7PRPA201305Transforms;
import gov.hhs.fha.nhinc.transform.subdisc.HL7PatientTransforms;
import javax.xml.bind.JAXBElement;
import oasis.names.tc.xacml._2_0.context.schema.os.AttributeType;
import oasis.names.tc.xacml._2_0.context.schema.os.RequestType;
import org.apache.commons.logging.Log;
import org.hl7.v3.II;
import org.hl7.v3.MCCIMT000100UV01Agent;
import org.hl7.v3.MCCIMT000100UV01Device;
import org.hl7.v3.MCCIMT000100UV01Organization;
import org.hl7.v3.MCCIMT000100UV01Sender;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAMT201301UV02Patient;
import org.hl7.v3.PRPAMT201301UV02Person;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
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
        context = new Mockery() {

            {
                setImposteriser(ClassImposteriser.INSTANCE);
            }
        };
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

    @Test
    public void testTransformPatientDiscoveryEntityToCheckPolicyWillPass() {
        final Log mockLogger = context.mock(Log.class);

        RespondingGatewayPRPAIN201305UV02RequestType event = new RespondingGatewayPRPAIN201305UV02RequestType();
        PRPAMT201301UV02Patient patient = new PRPAMT201301UV02Patient();
        II patId = new II();
        patId.setExtension("1234");
        patId.setRoot("1.1.1");
        JAXBElement<PRPAMT201301UV02Person> person = HL7PatientTransforms.create201301PatientPerson("Joe", "Smith", "M", "0101195", "123456789");
        patient = HL7PatientTransforms.create201301Patient(person, patId);
        PRPAIN201305UV02 msg = HL7PRPA201305Transforms.createPRPA201305(patient, "1.1", "2.2", "1.1.1");
        event.setPRPAIN201305UV02(msg);
        event.setAssertion(new AssertionType());

        PatientDiscoveryPolicyTransformHelper testSubject = new PatientDiscoveryPolicyTransformHelper() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }
        };
        context.checking(new Expectations() {

            {
                allowing(mockLogger).debug(with(any(String.class)));
                will(returnValue(with(any(CheckPolicyRequestType.class))));
            }
        });
        CheckPolicyRequestType result = testSubject.transformPatientDiscoveryEntityToCheckPolicy(event);
        context.assertIsSatisfied();
        assertNotNull(result);
        assertNotNull(result.getRequest());
        assertNotNull(result.getRequest().getResource());
        assertEquals(1, result.getRequest().getResource().size());
        assertNotNull(result.getRequest().getSubject());
        assertEquals(1, result.getRequest().getSubject().size());
        verifyHomeCommunityId(result, "2.2");
        verifyPatientId(result, patId);
    }

    @Test
    public void testTransformPatientDiscoveryEntityToCheckPolicyNoPatId() {
        final Log mockLogger = context.mock(Log.class);

        RespondingGatewayPRPAIN201305UV02RequestType event = new RespondingGatewayPRPAIN201305UV02RequestType();
        PRPAMT201301UV02Patient patient = new PRPAMT201301UV02Patient();
        II patId = null;
        JAXBElement<PRPAMT201301UV02Person> person = HL7PatientTransforms.create201301PatientPerson("Joe", "Smith", "M", "0101195", null);
        patient = HL7PatientTransforms.create201301Patient(person, patId);
        PRPAIN201305UV02 msg = HL7PRPA201305Transforms.createPRPA201305(patient, "1.1", "2.2", "1.1.1");
        event.setPRPAIN201305UV02(msg);
        event.setAssertion(new AssertionType());

        PatientDiscoveryPolicyTransformHelper testSubject = new PatientDiscoveryPolicyTransformHelper() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }
        };
        context.checking(new Expectations() {

            {
                allowing(mockLogger).debug(with(any(String.class)));
                will(returnValue(with(any(CheckPolicyRequestType.class))));
            }
        });
        CheckPolicyRequestType result = testSubject.transformPatientDiscoveryEntityToCheckPolicy(event);
        context.assertIsSatisfied();
        assertNotNull(result);
        verifyNoPatientId(result);
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
        context.checking(new Expectations() {

            {
                exactly(2).of(mockLogger).debug(with(any(String.class)));
                will(returnValue(with(any(RequestType.class))));
            }
        });
        testSubject.getRequestType(event);
        context.assertIsSatisfied();
    }

    @Test
    public void testGetHomeCommunityFrom201305() {
        RespondingGatewayPRPAIN201305UV02RequestType event = new RespondingGatewayPRPAIN201305UV02RequestType();
        HomeCommunityType expectedReturnvalue = getHomeCommunityType(event);
        PatientDiscoveryPolicyTransformHelper testSubject = new PatientDiscoveryPolicyTransformHelper();
        assertEquals(expectedReturnvalue.getHomeCommunityId(), "1.1");
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

    private void verifyHomeCommunityId(CheckPolicyRequestType result, String hcid) {
        assertNotNull(result.getRequest().getResource().get(0).getAttribute());

        boolean match = false;

        for (AttributeType attr : result.getRequest().getResource().get(0).getAttribute()) {
            if (attr.getAttributeId().equalsIgnoreCase(Constants.HomeCommunityAttributeId)) {
                if (((String) attr.getAttributeValue().get(0).getContent().get(0)).equalsIgnoreCase(hcid)) {
                    match = true;
                    break;
                }
            }
        }

        assertEquals(true, match);
    }

    private void verifyPatientId(CheckPolicyRequestType result, II patId) {
        assertNotNull(result.getRequest().getResource().get(0).getAttribute());

        boolean aaMatch = false;
        boolean patIdMatch = false;

        for (AttributeType attr : result.getRequest().getResource().get(0).getAttribute()) {
            if (attr.getAttributeId().equalsIgnoreCase(Constants.AssigningAuthorityAttributeId)) {
                if (((String) attr.getAttributeValue().get(0).getContent().get(0)).equalsIgnoreCase(patId.getRoot())) {
                    aaMatch = true;
                    break;
                }
            }
        }

        for (AttributeType attr : result.getRequest().getResource().get(0).getAttribute()) {
            if (attr.getAttributeId().equalsIgnoreCase(Constants.ResourceIdAttributeId)) {
                if (((String) attr.getAttributeValue().get(0).getContent().get(0)).equalsIgnoreCase(patId.getExtension())) {
                    patIdMatch = true;
                    break;
                }
            }
        }

        assertEquals(true, aaMatch);
        assertEquals(true, patIdMatch);
    }

    private void verifyNoPatientId(CheckPolicyRequestType result) {
        assertNotNull(result.getRequest().getResource().get(0).getAttribute());

        boolean aaMatch = false;
        boolean patIdMatch = false;

        for (AttributeType attr : result.getRequest().getResource().get(0).getAttribute()) {
            if (attr.getAttributeId().equalsIgnoreCase(Constants.AssigningAuthorityAttributeId)) {
                aaMatch = true;
                break;
            }
        }

        for (AttributeType attr : result.getRequest().getResource().get(0).getAttribute()) {
            if (attr.getAttributeId().equalsIgnoreCase(Constants.ResourceIdAttributeId)) {
                patIdMatch = true;
                break;
            }
        }

        assertEquals(false, aaMatch);
        assertEquals(false, patIdMatch);
    }
}
