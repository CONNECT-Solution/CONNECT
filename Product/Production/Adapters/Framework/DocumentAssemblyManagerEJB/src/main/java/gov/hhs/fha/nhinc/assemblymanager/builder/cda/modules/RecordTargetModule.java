/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
/*
 * Copyright (c) 2011, Conemaugh Valley Memorial Hospital
 *
 * This source is subject to the Conemaugh public license.  Please see the
 * license.txt file for more information.
 *
 * All other rights reserved.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND
 * CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT
 * SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.assemblymanager.builder.cda.modules;

import gov.hhs.fha.nhinc.assemblymanager.AssemblyConstants;
import gov.hhs.fha.nhinc.assemblymanager.builder.DocumentBuilder;
import gov.hhs.fha.nhinc.assemblymanager.builder.DocumentBuilderException;
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
import org.hl7.v3.POCDMT000040Participant1;

/**
 *
 * @author kim
 */
public class RecordTargetModule extends DocumentBuilder {

   private static Log log = LogFactory.getLog(RecordTargetModule.class);
   private II subjectId = null;
   private String MaritalStatusCodeSystem = "2.16.840.1.113883.5.2";
   private String MaritalStatusCodeSystemName = "MaritalStatusCode";

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

      String serviceEndpoint = AssemblyConstants.DAS_DATASERVICE_ENDPOINT;
      DataService dataService = new DataService(serviceEndpoint);

      // query patient registry for demographics information (including contact info)
      PatientDemographicsPRPAMT201303UV02ResponseType response = dataService.getPatientDemographics(subjectId, serviceEndpoint);

      if (response.getSubject() != null) {
         recordTarget.setPatientRole(createPatient(response.getSubject()));
      } 
      else
      {
         log.error("response.getSubject() = null");
         recordTarget = null;
      }

      return recordTarget;
   }

      public POCDMT000040RecordTarget build(POCDMT000040Participant1 participant) throws DocumentBuilderException {
      POCDMT000040RecordTarget recordTarget = objectFactory.createPOCDMT000040RecordTarget();

      String serviceEndpoint = AssemblyConstants.DAS_DATASERVICE_ENDPOINT;
      DataService dataService = new DataService(serviceEndpoint);

      // query patient registry for demographics information (including contact info)
      PatientDemographicsPRPAMT201303UV02ResponseType response = dataService.getPatientDemographics(subjectId, serviceEndpoint);

      if (response.getSubject() != null) {
         recordTarget.setPatientRole(createPatient(response.getSubject()));
         //since the Emergency contact info is in the patient info response, create the participant module as well
         ParticipantModule participantModule = new ParticipantModule();
         participantModule.build(response, participant);
      } else {
         log.debug("response.getSubject() = null");
         recordTarget = null;
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
               
             //add use attribute to address that is returned from EHR
             address.getUse().add("HP");
             
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
         log.debug("patient.getPatientPerson() = null");
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
         org.hl7.v3.CE patientPersonMaritalStatusCode = patientPerson.getMaritalStatusCode();

         //added for NIST compliance
         patientPersonMaritalStatusCode.setCodeSystem(MaritalStatusCodeSystem);
         patientPersonMaritalStatusCode.setCodeSystemName(MaritalStatusCodeSystemName);

         cdaPatient.setMaritalStatusCode(patientPersonMaritalStatusCode);

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
