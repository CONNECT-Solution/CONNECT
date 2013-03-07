package gov.hhs.fha.nhinc.transform.subdisc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.hl7.v3.ADExplicit;
import org.hl7.v3.CE;
import org.hl7.v3.II;
import org.hl7.v3.PNExplicit;
import org.hl7.v3.PRPAMT201301UV02Patient;
import org.hl7.v3.PRPAMT201301UV02Person;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectName;
import org.hl7.v3.PRPAMT201306UV02ParameterList;
import org.hl7.v3.PRPAMT201306UV02PatientAddress;
import org.hl7.v3.PRPAMT201306UV02PatientTelecom;
import org.hl7.v3.TELExplicit;
import org.hl7.v3.TSExplicit;
import org.junit.Test;

/**
 * @author achidambaram
 *
 */
public class HL7QueryParamsTransformsTest {
    
    @Test
    public void  createTelecom() {
        PRPAMT201306UV02PatientTelecom telecom = null;
        HL7QueryParamsTransforms queryTransforms = new HL7QueryParamsTransforms();
        telecom = queryTransforms.createTelecom(createTELList());
        assertEquals(telecom.getValue().get(0).getValue(), "CONNECT TEL");
    }
    
    @Test
    public void  createTelecomTELExplicitListNull() {
        PRPAMT201306UV02PatientTelecom telecom = null;
        HL7QueryParamsTransforms queryTransforms = new HL7QueryParamsTransforms();
        List<TELExplicit> telList = null;
        telecom = queryTransforms.createTelecom(telList);
        assertNull(telecom);
    }
    
    @Test
    public void  createAddressNull() {
        PRPAMT201306UV02PatientAddress patientAddress = new PRPAMT201306UV02PatientAddress();
        HL7QueryParamsTransforms queryTransforms = new HL7QueryParamsTransforms();
        List<ADExplicit> patientAddressList = null;
        patientAddress = queryTransforms.createAddress(patientAddressList);
        assertNull(patientAddress);
    }
    
    @Test
    public void  createAddress() {
        PRPAMT201306UV02PatientAddress patientAddress = new PRPAMT201306UV02PatientAddress();
        HL7QueryParamsTransforms queryTransforms = new HL7QueryParamsTransforms();
        patientAddress = queryTransforms.createAddress(patientAddressList());
        assertEquals(patientAddress.getValue().get(0).getUse().get(0), "12601, FairLakes");
    }
    
    @Test
    public void createParamList() {
        PRPAMT201306UV02ParameterList parameterList = null;
        HL7QueryParamsTransforms queryTransforms = new HL7QueryParamsTransforms();
        parameterList = queryTransforms.createParamList(createPRPAMT201301UV02Patient());
        assertEquals(parameterList.getLivingSubjectAdministrativeGender().get(0).getValue().get(0).getCode(), "CONNECT");
        assertEquals(parameterList.getLivingSubjectBirthTime().get(0).getValue().get(0).getValue(), "12-10-1955");
    }
    
    public void  createName() {
        PRPAMT201306UV02LivingSubjectName subjectName = null;
        List<PNExplicit> patientNames = null;
        HL7QueryParamsTransforms queryTransforms = new HL7QueryParamsTransforms();
        subjectName = queryTransforms.createName(patientNames);
        assertNull(subjectName);
    }
    
    private PRPAMT201301UV02Patient createPRPAMT201301UV02Patient() {
        org.hl7.v3.PRPAMT201301UV02Patient patient = new PRPAMT201301UV02Patient();
        PRPAMT201301UV02Person patientPerson = new PRPAMT201301UV02Person();
        javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:hl7-org:v3", "patientPerson");
        JAXBElement<PRPAMT201301UV02Person> patientPersonElement = new JAXBElement<PRPAMT201301UV02Person>(xmlqname,
                PRPAMT201301UV02Person.class, patientPerson);
        patient.setPatientPerson(patientPersonElement);
        patient.getId().add(createII());
        patientPerson.getClassCode().add("ClassCode");
        patientPerson.setAdministrativeGenderCode(createCE());
        patientPerson.setBirthTime(createTSExplicit());
        PNExplicit patientName = new PNExplicit();
        patientName.getContent().add(getFirstName());
        patientName.getContent().add(getLastName());
        patientName.getNullFlavor().add("NA");
        patientPerson.getName().add(patientName);
        patientPerson.getTelecom().add(createTELExplicit());
        patientPerson.getAddr().add(createADExplicit());
        patientPerson.setDeterminerCode("INSTANCE");
        patientPerson.setAdministrativeGenderCode(createCE());
        
        
        return patient;
    }
    
    private TSExplicit createTSExplicit() {
        TSExplicit ts = new TSExplicit();
        ts.setValue("12-10-1955");
        return ts;
    }
    
    private CE createCE(){
        CE ce = new CE();
        ce.setCode("CONNECT");
        return ce;
    }
    
    private II createII() {
        II ii = new II();
        ii.setAssigningAuthorityName("CONNECT");
        ii.setExtension("1.16.17.19");
        ii.setRoot("1.1");
        return ii;
    }
    
    private static String getFirstName() {
        String firstName = "Gallow";
        return firstName;
    }
    
    private static String getLastName() {
        String lastName = "Younger";
        return lastName;
    }
    
    private List<ADExplicit> patientAddressList() {
        List<ADExplicit> patientAddressList = new ArrayList<ADExplicit>();
        patientAddressList.add(createADExplicit());
        return patientAddressList;
    }
    
    private ADExplicit createADExplicit() {
        ADExplicit ad = new ADExplicit();
        ad.getUse().add("12601, FairLakes");
        return ad;
    }
    
    private List<TELExplicit> createTELList() {
        List<TELExplicit> telList = new ArrayList<TELExplicit>();
        telList.add(createTELExplicit());
        return telList;
    }
    
    private TELExplicit createTELExplicit() {
        TELExplicit tel = new TELExplicit();
        tel.setValue("CONNECT TEL");
        return tel;
    }

}
