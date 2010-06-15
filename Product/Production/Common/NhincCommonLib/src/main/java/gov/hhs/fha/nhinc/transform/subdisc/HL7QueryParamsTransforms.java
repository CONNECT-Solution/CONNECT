/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.transform.subdisc;

import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import java.util.List;
import javax.xml.bind.JAXBElement;
import org.hl7.v3.ADExplicit;
import org.hl7.v3.CE;
import org.hl7.v3.II;
import org.hl7.v3.IVLTSExplicit;
import org.hl7.v3.PNExplicit;
import org.hl7.v3.PRPAMT201301UV02OtherIDs;
import org.hl7.v3.PRPAMT201301UV02Patient;
import org.hl7.v3.PRPAMT201301UV02Person;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectAdministrativeGender;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectBirthTime;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectId;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectName;
import org.hl7.v3.PRPAMT201306UV02ParameterList;
import org.hl7.v3.PRPAMT201306UV02PatientAddress;
import org.hl7.v3.PRPAMT201306UV02PatientTelecom;
import org.hl7.v3.PRPAMT201306UV02QueryByParameter;
import org.hl7.v3.ST;
import org.hl7.v3.TELExplicit;

/**
 *
 * @author Jon Hoppesch
 */
public class HL7QueryParamsTransforms {

    public static JAXBElement<PRPAMT201306UV02QueryByParameter> createQueryParams(PRPAMT201301UV02Patient patient, String localDeviceId) {
        PRPAMT201306UV02QueryByParameter params = new PRPAMT201306UV02QueryByParameter();

        params.setQueryId(HL7MessageIdGenerator.GenerateHL7MessageId(localDeviceId));
        params.setStatusCode(HL7DataTransformHelper.CSFactory("new"));

        params.setParameterList(createParamList(patient));

        javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:hl7-org:v3", "queryByParameter");
        JAXBElement<PRPAMT201306UV02QueryByParameter> queryParams = new JAXBElement<PRPAMT201306UV02QueryByParameter>(xmlqname, PRPAMT201306UV02QueryByParameter.class, params);

        return queryParams;
    }

    public static PRPAMT201306UV02ParameterList createParamList(PRPAMT201301UV02Patient patient) {
        PRPAMT201306UV02ParameterList paramList = new PRPAMT201306UV02ParameterList();
        JAXBElement<PRPAMT201301UV02Person> jaxbPerson = patient.getPatientPerson();
        PRPAMT201301UV02Person person = jaxbPerson.getValue();

        // Set the Subject Gender Code  
        if (person != null &&
                person.getAdministrativeGenderCode() != null &&
                NullChecker.isNotNullish(person.getAdministrativeGenderCode().getCode())) {
            paramList.getLivingSubjectAdministrativeGender().add(createGender(person.getAdministrativeGenderCode().getCode()));
        }

        // Set the Subject Birth Time
        if (person != null &&
                person.getBirthTime() != null &&
                NullChecker.isNotNullish(person.getBirthTime().getValue())) {
           paramList.getLivingSubjectBirthTime().add(createBirthTime(person.getBirthTime().getValue()));
        }
        
        // Set the address
        if(person != null &&
                person.getAddr() != null &&
                person.getAddr().size() > 0)
        {
            paramList.getPatientAddress().add(createAddress(person.getAddr()));
        }

        // Set telephone number
        if(person != null &&
                person.getTelecom()!=null &&
                person.getTelecom().size() > 0)
        {
            paramList.getPatientTelecom().add(createTelecom(person.getTelecom()));
        }
        // Set the Subject Name
        if (person != null &&
                person.getName() != null &&
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

        // Set the other ids
        if (person != null &&
                NullChecker.isNotNullish(person.getAsOtherIDs())) {
            for (PRPAMT201301UV02OtherIDs otherId : person.getAsOtherIDs()) {
                for (II id : otherId.getId()) {
                    paramList.getLivingSubjectId().add(createSubjectId(id));
                }
            }
        }

        return paramList;
    }

    public static PRPAMT201306UV02LivingSubjectId createSubjectId(II subjectId) {
        PRPAMT201306UV02LivingSubjectId id = new PRPAMT201306UV02LivingSubjectId();

        if (subjectId != null) {
            id.getValue().add(subjectId);
            ST text = new ST();
            id.setSemanticsText(text);
        }

        return id;
    }

    public static PRPAMT201306UV02LivingSubjectName createName(List<PNExplicit> patientNames) {
        if (patientNames == null || patientNames.size() == 0) {
            return null;
        }
        
        PRPAMT201306UV02LivingSubjectName subjectName = new PRPAMT201306UV02LivingSubjectName();
        
        for (PNExplicit name : patientNames) {
           subjectName.getValue().add(HL7DataTransformHelper.ConvertPNToEN(name));
           ST text = new ST();
           subjectName.setSemanticsText(text);
        }      

        return subjectName;
    }

    public static PRPAMT201306UV02LivingSubjectBirthTime createBirthTime(String birthTime) {
        PRPAMT201306UV02LivingSubjectBirthTime subjectBirthTime = new PRPAMT201306UV02LivingSubjectBirthTime();
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

    public static PRPAMT201306UV02LivingSubjectAdministrativeGender createGender(String gender) {
        PRPAMT201306UV02LivingSubjectAdministrativeGender adminGender = new PRPAMT201306UV02LivingSubjectAdministrativeGender();
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

    public static PRPAMT201306UV02PatientAddress createAddress(List<ADExplicit> patientAddress)
    {
        PRPAMT201306UV02PatientAddress subjectAddress = null;
        ST text = null;

        if(patientAddress != null)
        {
            subjectAddress = new PRPAMT201306UV02PatientAddress();
            for (ADExplicit address : patientAddress) {
               subjectAddress.getValue().add(address);
               text = new ST();
               subjectAddress.setSemanticsText(text);
            }
        }

        return subjectAddress;
    }

    public static PRPAMT201306UV02PatientTelecom createTelecom(List<TELExplicit> patientTelecom)
    {
        PRPAMT201306UV02PatientTelecom subjectTele = null;
        ST text = null;

        if (patientTelecom != null)
        {
            subjectTele = new PRPAMT201306UV02PatientTelecom();
            for(TELExplicit tele : patientTelecom)
            {
                subjectTele.getValue().add(tele);
                text = new ST();
                subjectTele.setSemanticsText(text);
            }
        }

        return subjectTele;
    }
}
