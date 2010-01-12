/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.assemblymanager.builder.cda.modules;

import gov.hhs.fha.nhinc.assemblymanager.AssemblyConstants;
import gov.hhs.fha.nhinc.assemblymanager.builder.DocumentBuilder;
import gov.hhs.fha.nhinc.assemblymanager.builder.DocumentBuilderException;
import gov.hhs.fha.nhinc.assemblymanager.dao.PropertiesDAO;
import gov.hhs.fha.nhinc.assemblymanager.service.DataService;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.ADExplicit;
import org.hl7.v3.COCTMT150003UV03Organization;
import org.hl7.v3.CS;
import org.hl7.v3.II;
import org.hl7.v3.ONExplicit;
import org.hl7.v3.POCDMT000040LanguageCommunication;
import org.hl7.v3.POCDMT000040Organization;
import org.hl7.v3.POCDMT000040Patient;
import org.hl7.v3.POCDMT000040PatientRole;
import org.hl7.v3.POCDMT000040RecordTarget;
import org.hl7.v3.PRPAMT201303UV02LanguageCommunication;
import org.hl7.v3.PRPAMT201303UV02Patient;
import org.hl7.v3.PRPAMT201303UV02Person;
import org.hl7.v3.PatientDemographicsPRPAMT201303UV02ResponseType;
import org.hl7.v3.TELExplicit;

/**
 *
 * @author kim
 */
public class RecordTargetModule extends DocumentBuilder {

   private static Log log = LogFactory.getLog(RecordTargetModule.class);
   private II subjectId = null;

   /**
    * Constructor
    * @param patientId  Unique identifer for patient in the EMR system.
    * @param rootId      Unique object identifier representing the EMR system.
    */
   public RecordTargetModule(II subjectId) {
      super();
      this.subjectId = subjectId;
   }

   public II getSubjectId() {
      return subjectId;
   }

   public void setSubjectId(II subjectId) {
      this.subjectId = subjectId;
   }

   public POCDMT000040RecordTarget build() throws DocumentBuilderException {
      POCDMT000040RecordTarget recordTarget = objectFactory.createPOCDMT000040RecordTarget();

      String serviceEndpoint = PropertiesDAO.getInstance().getAttributeValue(AssemblyConstants.DAS_DATASERVICE_ENDPOINT, true);
      DataService dataService = new DataService(serviceEndpoint);

      // query patient registry for demographics information (including contact info)
      PatientDemographicsPRPAMT201303UV02ResponseType response = dataService.getPatientDemographics(subjectId, serviceEndpoint);

      if (response.getSubject() != null) {
         recordTarget.setPatientRole(createPatient(response.getSubject()));
      } else {
         log.error("response.getSubject() = null");
      }

      return recordTarget;
   }

   private POCDMT000040PatientRole createPatient(PRPAMT201303UV02Patient subject) {
      POCDMT000040PatientRole patientRole = objectFactory.createPOCDMT000040PatientRole();

      // patient identifier (in EMR system)
      if (subject.getId() != null && subject.getId().size() > 0) {
         for (II id : subject.getId()) {
            patientRole.getId().add(id);
         }
      }

      // address(es)
      // set recordTarget->patientRole->addr element to convey patient's addresses info
      if (subject.getAddr() != null && subject.getAddr().size() > 0) {
         for (ADExplicit address : subject.getAddr()) {
            patientRole.getAddr().add(address);
         }
      }

      // telecom(s)
      // set recordTarget->patientRole->telecom element to convey patient's phone info
      if (subject.getTelecom() != null && subject.getTelecom().size() > 0) {
         for (TELExplicit telecom : subject.getTelecom()) {
            patientRole.getTelecom().add(telecom);
         }
      }




      // patient
      if (subject.getPatientPerson() != null) {
         patientRole.setPatient(createPOCDMT000040Patient(subject.getPatientPerson().getValue()));
      } else {
         log.error("patient.getPatientPerson() = null");
      }

      // provider organization
      if (subject.getProviderOrganization() != null) {
         patientRole.setProviderOrganization(createPOCDMT000040Organization(subject.getProviderOrganization().getValue()));
      }


      return patientRole;
   }

   private POCDMT000040Organization createPOCDMT000040Organization(COCTMT150003UV03Organization org) {
      POCDMT000040Organization providerOrg = objectFactory.createPOCDMT000040Organization();

      if (org != null) {
         // build POCDMT000040Organization if no info available
         if (org == null || org.getId().size() == 0 || org.getName().size() == 0) {
            // get organization OID
            II orgId = new II();
            orgId.setRoot(this.orgOID);
            providerOrg.getId().add(orgId);

            // get organization name
            ONExplicit providerOrgName = new ONExplicit();
            providerOrgName.getContent().add(this.orgName);
            providerOrg.getName().add(providerOrgName);
         } else {
            providerOrg.getId().add(org.getId().get(0));
            providerOrg.getName().add(org.getName().get(0));
         }
      }

      return providerOrg;
   }

   private POCDMT000040Patient createPOCDMT000040Patient(PRPAMT201303UV02Person patientPerson) {
      POCDMT000040Patient cdaPatient = objectFactory.createPOCDMT000040Patient();

      if (patientPerson != null) {
         // name(s)
         if (patientPerson.getName() != null) {
            for (int i = 0; i < patientPerson.getName().size(); i++) {
               cdaPatient.getName().add(patientPerson.getName().get(i));
            }
         }

         // birth date
         cdaPatient.setBirthTime(patientPerson.getBirthTime());

         // gender
         cdaPatient.setAdministrativeGenderCode(patientPerson.getAdministrativeGenderCode());

         // ethnicticity
         if (patientPerson.getEthnicGroupCode() != null && patientPerson.getEthnicGroupCode().size() > 0) {
            cdaPatient.setEthnicGroupCode(patientPerson.getEthnicGroupCode().get(0));
         }

         // race
         if (patientPerson.getRaceCode() != null && patientPerson.getRaceCode().size() > 0) {
            cdaPatient.setRaceCode(patientPerson.getRaceCode().get(0));
         }

         // marital status
         cdaPatient.setMaritalStatusCode(patientPerson.getMaritalStatusCode());

         // language
         List<PRPAMT201303UV02LanguageCommunication> languages = patientPerson.getLanguageCommunication();
         if (languages != null) {
            POCDMT000040LanguageCommunication language = null;
            for (int i = 0; i < languages.size(); i++) {
               if (languages.get(i).getLanguageCode() != null) {
                  language = objectFactory.createPOCDMT000040LanguageCommunication();
                  CS languageCode = new CS();
                  languageCode.setCode(languages.get(i).getLanguageCode().getCode());
                  language.setLanguageCode(languageCode);
                  cdaPatient.getLanguageCommunication().add(language);
               }
            }
         }
      }
      return cdaPatient;
   }
}
