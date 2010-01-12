/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.adapter.commondatalayer;

import gov.hhs.fha.nhinc.adapter.commondatalayer.mappers.*;
import org.hl7.v3.PatientDemographicsPRPAIN201307UV02RequestType;
import org.hl7.v3.PatientDemographicsPRPAMT201303UV02ResponseType;

/**
 *
 * @author kim
 */
public class AdapterCommonDataLayerImpl {

   private static AdapterCommonDataLayerImpl instance = null;

   public static AdapterCommonDataLayerImpl getInstance() {
      synchronized (AdapterCommonDataLayerImpl.class) {
         if (instance == null) {
            instance = new AdapterCommonDataLayerImpl();
         }
      }

      return instance;
   }

   public PatientDemographicsPRPAMT201303UV02ResponseType getPatienInfo(PatientDemographicsPRPAIN201307UV02RequestType request) {
      return StaticPatientDemographicsQuery.createPatientDemographicsResponse(request);
   }

   public org.hl7.v3.CareRecordQUPCIN043200UV01ResponseType getMedications(org.hl7.v3.CareRecordQUPCIN043100UV01RequestType param0) {
      return StaticMedicationsQuery.createMedicationsResponse(param0);
   }

   public org.hl7.v3.CareRecordQUPCIN043200UV01ResponseType getTestResults(org.hl7.v3.CareRecordQUPCIN043100UV01RequestType param0) {
      return StaticTestResultsQuery.createTestResultsResponse(param0);
   }

   public org.hl7.v3.CareRecordQUPCIN043200UV01ResponseType getAllergies(org.hl7.v3.CareRecordQUPCIN043100UV01RequestType param0) {
      return StaticAllergiesQuery.createAllergiesResponse(param0);
   }

   public org.hl7.v3.CareRecordQUPCIN043200UV01ResponseType getProblems(org.hl7.v3.CareRecordQUPCIN043100UV01RequestType param0) {
      return StaticProblemsQuery.createProblemsResponse(param0);
   }

   public org.hl7.v3.FindPatientsPRPAMT201310UV02ResponseType findPatients(org.hl7.v3.FindPatientsPRPAIN201305UV02RequestType param0) {
      return StaticFindPatientsQuery.createFindPatientsResponse(param0);
   }
}
