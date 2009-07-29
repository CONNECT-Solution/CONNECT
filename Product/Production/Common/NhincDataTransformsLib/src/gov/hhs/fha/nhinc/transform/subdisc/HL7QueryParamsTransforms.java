/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.transform.subdisc;

import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import java.util.List;
import javax.xml.bind.JAXBElement;
import org.hl7.v3.CE;
import org.hl7.v3.II;
import org.hl7.v3.IVLTSExplicit;
import org.hl7.v3.PNExplicit;
import org.hl7.v3.PRPAMT201301UVPatient;
import org.hl7.v3.PRPAMT201301UVPerson;
import org.hl7.v3.PRPAMT201306UVLivingSubjectAdministrativeGender;
import org.hl7.v3.PRPAMT201306UVLivingSubjectBirthTime;
import org.hl7.v3.PRPAMT201306UVLivingSubjectId;
import org.hl7.v3.PRPAMT201306UVLivingSubjectName;
import org.hl7.v3.PRPAMT201306UVParameterList;
import org.hl7.v3.PRPAMT201306UVQueryByParameter;
import org.hl7.v3.ST;
import org.hl7.v3.TEL;

/**
 *
 * @author Jon Hoppesch
 */
public class HL7QueryParamsTransforms {

    public static JAXBElement<PRPAMT201306UVQueryByParameter> createQueryParams(PRPAMT201301UVPatient patient, String localDeviceId) {
        PRPAMT201306UVQueryByParameter params = new PRPAMT201306UVQueryByParameter();

        params.setQueryId(HL7MessageIdGenerator.GenerateHL7MessageId(localDeviceId));
        params.setStatusCode(HL7DataTransformHelper.CSFactory("new"));

        params.setParameterList(createParamList(patient));

        javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:hl7-org:v3", "queryByParameter");
        JAXBElement<PRPAMT201306UVQueryByParameter> queryParams = new JAXBElement<PRPAMT201306UVQueryByParameter>(xmlqname, PRPAMT201306UVQueryByParameter.class, params);

        return queryParams;
    }

    public static PRPAMT201306UVParameterList createParamList(PRPAMT201301UVPatient patient) {
        PRPAMT201306UVParameterList paramList = new PRPAMT201306UVParameterList();
        JAXBElement<PRPAMT201301UVPerson> jaxbPerson = patient.getPatientPerson();
        PRPAMT201301UVPerson person = jaxbPerson.getValue();

        // Set the Subject Gender Code  
        if (person.getAdministrativeGenderCode() != null &&
                NullChecker.isNotNullish(person.getAdministrativeGenderCode().getCode())) {
            paramList.getLivingSubjectAdministrativeGender().add(createGender(person.getAdministrativeGenderCode().getCode()));
        }

        // Set the Subject Birth Time
        if (person.getBirthTime() != null &&
                NullChecker.isNotNullish(person.getBirthTime().getValue())) {
           paramList.getLivingSubjectBirthTime().add(createBirthTime(person.getBirthTime().getValue()));
        }

        // Set the Subject Name
        if (person.getName() != null &&
                person.getName().size() > 0) {
           paramList.getLivingSubjectName().add(createName(person.getName()));
        }

        // Set the subject Id
        if (patient != null &&
                patient.getId() != null &&
                patient.getId().size() > 0 &&
                patient.getId().get(0) != null) {
            paramList.getLivingSubjectId().add(createSubjectId(patient.getId().get(0)));
        }

        return paramList;
    }

    public static PRPAMT201306UVLivingSubjectId createSubjectId(II subjectId) {
        PRPAMT201306UVLivingSubjectId id = new PRPAMT201306UVLivingSubjectId();

        if (subjectId != null) {
            id.getValue().add(subjectId);
            ST text = new ST();
            id.setSemanticsText(text);
        }

        return id;
    }

    public static PRPAMT201306UVLivingSubjectName createName(List<PNExplicit> patientNames) {
        if (patientNames.size() == 0) {
            return null;
        }
        
        PRPAMT201306UVLivingSubjectName subjectName = new PRPAMT201306UVLivingSubjectName();
        
        for (PNExplicit name : patientNames) {
           subjectName.getValue().add(HL7DataTransformHelper.ConvertPNToEN(name));
           ST text = new ST();
           subjectName.setSemanticsText(text);
        }      

        return subjectName;
    }

    public static PRPAMT201306UVLivingSubjectBirthTime createBirthTime(String birthTime) {
        PRPAMT201306UVLivingSubjectBirthTime subjectBirthTime = new PRPAMT201306UVLivingSubjectBirthTime();
        IVLTSExplicit bday = new IVLTSExplicit();

        if (birthTime != null &&
                birthTime.length() > 0) {
            bday.setValue(birthTime);
            subjectBirthTime.getValue().add(bday);
            ST text = new ST();
            subjectBirthTime.setSemanticsText(text);
        }

        return subjectBirthTime;
    }

    public static PRPAMT201306UVLivingSubjectAdministrativeGender createGender(String gender) {
        PRPAMT201306UVLivingSubjectAdministrativeGender adminGender = new PRPAMT201306UVLivingSubjectAdministrativeGender();
        CE genderCode = new CE();

        if (gender != null &&
                gender.length() > 0) {
            genderCode.setCode(gender);
            adminGender.getValue().add(genderCode);
            
            ST text = new ST();
            adminGender.setSemanticsText(text);
        }

        return adminGender;
    }
}
