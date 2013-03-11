package gov.hhs.fha.nhinc.transform.subdisc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.xml.bind.JAXBElement;

import org.hl7.v3.II;
import org.hl7.v3.PRPAIN201310UV02;
import org.hl7.v3.PRPAMT201307UV02ParameterList;
import org.hl7.v3.PRPAMT201307UV02PatientIdentifier;
import org.hl7.v3.PRPAMT201307UV02QueryByParameter;
import org.junit.Test;

/**
 * @author achidambaram
 * 
 */
public class HL7PRPA201310TransformsTest {
    @Test
    public void createPRPA201310() {
        PRPAIN201310UV02 result = new PRPAIN201310UV02();
        String patientId = "D123401";
        String assigningAuthorityId = "1.1";
        String localDeviceId = "1.1";
        String senderOID = "1.1";
        String receiverOID = "2.2";
        JAXBElement<PRPAMT201307UV02QueryByParameter> queryParam = createQueryByParameter();
        HL7PRPA201310Transforms transforms = new HL7PRPA201310Transforms();
        result = transforms.createPRPA201310(patientId, assigningAuthorityId, localDeviceId, senderOID, receiverOID,
                queryParam);
        assertEquals(result.getId().getRoot(), "1.1");
        assertEquals(result.getSender().getDevice().getId().get(0).getRoot(), "1.1");
        assertEquals(result.getReceiver().get(0).getDevice().getId().get(0).getRoot(), "2.2");
        assertEquals(result.getControlActProcess().getQueryByParameter().getValue().getParameterList()
                .getPatientIdentifier().get(0).getValue().get(0).getExtension(), "D123401");
        assertEquals(result.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1()
                .getPatient().getId().get(0).getRoot(), "1.1");
    }

    @Test
    public void createPRPA201310WhenPatIdandAAIDNull() {
        PRPAIN201310UV02 result = new PRPAIN201310UV02();
        String patientId = null;
        String assigningAuthorityId = null;
        String localDeviceId = "1.1";
        String senderOID = "1.1";
        String receiverOID = "2.2";
        JAXBElement<PRPAMT201307UV02QueryByParameter> queryParam = createQueryByParameter();
        HL7PRPA201310Transforms transforms = new HL7PRPA201310Transforms();
        result = transforms.createPRPA201310(patientId, assigningAuthorityId, localDeviceId, senderOID, receiverOID,
                queryParam);
        assertEquals(result.getId().getRoot(), "1.1");
        assertEquals(result.getSender().getDevice().getId().get(0).getRoot(), "1.1");
        assertEquals(result.getReceiver().get(0).getDevice().getId().get(0).getRoot(), "2.2");
        assertEquals(result.getControlActProcess().getQueryByParameter().getValue().getParameterList()
                .getPatientIdentifier().get(0).getValue().get(0).getExtension(), "D123401");
        assertNull(result.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient()
                .getId().get(0).getRoot());
    }

    @Test
    public void createFaultPRPA201310() {
        PRPAIN201310UV02 result = new PRPAIN201310UV02();
        HL7PRPA201310Transforms transforms = new HL7PRPA201310Transforms();
        result = transforms.createFaultPRPA201310();
        assertNull(result.getSender().getDevice().getId().get(0).getRoot());
        assertNull(result.getReceiver().get(0).getDevice().getId().get(0).getRoot());
        assertTrue(result.getControlActProcess().getQueryByParameter().getValue().getParameterList()
                .getPatientIdentifier().isEmpty());
        assertNull(result.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient()
                .getId().get(0).getRoot());
    }

    @Test
    public void createFaultPRPA201310WithSenderAndReceiver() {
        PRPAIN201310UV02 result = new PRPAIN201310UV02();
        String senderOID = "1.1";
        String receiverOID = "2.2";
        HL7PRPA201310Transforms transforms = new HL7PRPA201310Transforms();
        result = transforms.createFaultPRPA201310(senderOID, receiverOID);
        assertEquals(result.getSender().getDevice().getId().get(0).getRoot(), "1.1");
        assertEquals(result.getReceiver().get(0).getDevice().getId().get(0).getRoot(), "2.2");
        assertTrue(result.getControlActProcess().getQueryByParameter().getValue().getParameterList()
                .getPatientIdentifier().isEmpty());
        assertNull(result.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient()
                .getId().get(0).getRoot());
    }

    private JAXBElement<PRPAMT201307UV02QueryByParameter> createQueryByParameter() {
        PRPAMT201307UV02QueryByParameter parameter = new PRPAMT201307UV02QueryByParameter();
        parameter.setQueryId(createII());
        parameter.setParameterList(createPRPAMT201307UV02ParameterList());
        javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:hl7-org:v3", "parameter");
        JAXBElement<PRPAMT201307UV02QueryByParameter> queryByParameter = new JAXBElement<PRPAMT201307UV02QueryByParameter>(
                xmlqname, PRPAMT201307UV02QueryByParameter.class, parameter);
        return queryByParameter;
    }

    /**
     * @return
     */
    private II createII() {
        II ii = new II();
        ii.setAssigningAuthorityName("CONNECT");
        ii.setExtension("D123401");
        ii.setRoot("1.1");
        return ii;
    }

    private PRPAMT201307UV02ParameterList createPRPAMT201307UV02ParameterList() {
        PRPAMT201307UV02ParameterList parameterList = new PRPAMT201307UV02ParameterList();
        parameterList.setId(createII());
        parameterList.getPatientIdentifier().add(createPRPAMT201307UV02PatientIdentifier());
        return parameterList;
    }

    private PRPAMT201307UV02PatientIdentifier createPRPAMT201307UV02PatientIdentifier() {
        PRPAMT201307UV02PatientIdentifier identifier = new PRPAMT201307UV02PatientIdentifier();
        identifier.getValue().add(createII());
        return identifier;
    }

}
