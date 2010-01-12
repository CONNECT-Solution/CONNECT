/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.assemblymanager.service;

import gov.hhs.fha.nhinc.adapter.commondatalayer.CommonDataLayerPortType;
import gov.hhs.fha.nhinc.adapter.commondatalayer.CommonDataLayerService;
import gov.hhs.fha.nhinc.template.model.CdaTemplate;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import org.hl7.v3.CareRecordQUPCIN043100UV01RequestType;
import org.hl7.v3.CareRecordQUPCIN043200UV01ResponseType;
import org.hl7.v3.II;
import org.hl7.v3.PatientDemographicsPRPAIN201307UV02RequestType;
import org.hl7.v3.PatientDemographicsPRPAMT201303UV02ResponseType;

/**
 *
 * @author kim
 */
public class DataService {

   private CommonDataLayerService service = null;
   public static final String COMMON_DATA_LAYER_QNAME = "urn:gov:hhs:fha:nhinc:adapter:commondatalayer";

   public DataService() {
   }

   public DataService(String endpoint) {
      initService(endpoint);
   }

   private void initService(String serviceEndpoint) {
      URL baseUrl;

      baseUrl = gov.hhs.fha.nhinc.adapter.commondatalayer.CommonDataLayerService.class.getResource(".");
      try {
         URL url = new URL(baseUrl, serviceEndpoint);
         service = new CommonDataLayerService(url, new QName(COMMON_DATA_LAYER_QNAME, "CommonDataLayerService"));
      } catch (MalformedURLException e) {
         System.err.println("Failed to create URL for the wsdl Location: " + serviceEndpoint);
      }
   }

   public PatientDemographicsPRPAMT201303UV02ResponseType getPatientDemographics(II subjectId, String serviceEndpoint) {
      if (subjectId == null || subjectId.getExtension() == null || subjectId.getExtension().length() < 1) {
         return null;
      }

      if (service == null) {
         initService(serviceEndpoint);
      }

      CommonDataLayerPortType port = service.getCommonDataLayerPort();
      PatientDemographicsPRPAIN201307UV02RequestType request = PatientInfoRequestHelper.createPatientDemographicsRequest(subjectId);
      return port.getPatienInfo(request);
   }

   public CareRecordQUPCIN043200UV01ResponseType getMedications(II subjectId, String careProvisionCode, String dataStartDate, String dataEndDate, String serviceEndpoint) {
      if (subjectId == null || subjectId.getExtension() == null || subjectId.getExtension().length() < 1) {
         return null;
      }

      if (service == null) {
         initService(serviceEndpoint);
      }

      if (dataStartDate == null) {
         return getMedications(subjectId, careProvisionCode, serviceEndpoint);
      } else {
         CommonDataLayerPortType port = service.getCommonDataLayerPort();

         CareRecordQUPCIN043100UV01RequestType request = CareRecordRequestHelper.createCareRecordRequest(subjectId, careProvisionCode, dataStartDate, dataEndDate);

         return port.getMedications(request);
      }
   }

   public CareRecordQUPCIN043200UV01ResponseType getMedications(II subjectId, String careProvisionCode, String serviceEndpoint) {
      if (subjectId == null || subjectId.getExtension() == null || subjectId.getExtension().length() < 1) {
         return null;
      }

      if (service == null) {
         initService(serviceEndpoint);
      }

      CommonDataLayerPortType port = service.getCommonDataLayerPort();
      CareRecordQUPCIN043100UV01RequestType request = CareRecordRequestHelper.createCareRecordRequest(subjectId, careProvisionCode);

      return port.getMedications(request);
   }

   public CareRecordQUPCIN043200UV01ResponseType getAllergies(II subjectId, String careProvisionCode, String dataStartDate, String dataEndDate, String serviceEndpoint) {
      if (subjectId == null || subjectId.getExtension() == null || subjectId.getExtension().length() < 1) {
         return null;
      }

      if (service == null) {
         initService(serviceEndpoint);
      }

      if (dataStartDate == null) {
         return getAllergies(subjectId, careProvisionCode, serviceEndpoint);
      } else {
         CommonDataLayerPortType port = service.getCommonDataLayerPort();

         CareRecordQUPCIN043100UV01RequestType request = CareRecordRequestHelper.createCareRecordRequest(subjectId, careProvisionCode, dataStartDate, dataEndDate);

         return port.getAllergies(request);
      }
   }

   public CareRecordQUPCIN043200UV01ResponseType getAllergies(II subjectId, String careProvisionCode, String serviceEndpoint) {
      if (subjectId == null || subjectId.getExtension() == null || subjectId.getExtension().length() < 1) {
         return null;
      }

      if (service == null) {
         initService(serviceEndpoint);
      }

      CommonDataLayerPortType port = service.getCommonDataLayerPort();
      CareRecordQUPCIN043100UV01RequestType request = CareRecordRequestHelper.createCareRecordRequest(subjectId, careProvisionCode);

      return port.getAllergies(request);
   }

   public CareRecordQUPCIN043200UV01ResponseType getProblems(II subjectId, String careProvisionCode, String dataStartDate, String dataEndDate, String serviceEndpoint) {
      if (subjectId == null || subjectId.getExtension() == null || subjectId.getExtension().length() < 1) {
         return null;
      }

      if (service == null) {
         initService(serviceEndpoint);
      }

      if (dataStartDate == null) {
         return getProblems(subjectId, careProvisionCode, serviceEndpoint);
      } else {
         CommonDataLayerPortType port = service.getCommonDataLayerPort();

         CareRecordQUPCIN043100UV01RequestType request = CareRecordRequestHelper.createCareRecordRequest(subjectId, careProvisionCode, dataStartDate, dataEndDate);

         return port.getProblems(request);
      }
   }

   public CareRecordQUPCIN043200UV01ResponseType getProblems(II subjectId, String careProvisionCode, String serviceEndpoint) {
      if (subjectId == null || subjectId.getExtension() == null || subjectId.getExtension().length() < 1) {
         return null;
      }

      if (service == null) {
         initService(serviceEndpoint);
      }

      CommonDataLayerPortType port = service.getCommonDataLayerPort();
      CareRecordQUPCIN043100UV01RequestType request = CareRecordRequestHelper.createCareRecordRequest(subjectId, careProvisionCode);

      return port.getProblems(request);
   }
}
