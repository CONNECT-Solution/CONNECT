/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
 * All rights reserved. 
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met: 
 *     * Redistributions of source code must retain the above 
 *       copyright notice, this list of conditions and the following disclaimer. 
 *     * Redistributions in binary form must reproduce the above copyright 
 *       notice, this list of conditions and the following disclaimer in the documentation 
 *       and/or other materials provided with the distribution. 
 *     * Neither the name of the United States Government nor the 
 *       names of its contributors may be used to endorse or promote products 
 *       derived from this software without specific prior written permission. 
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY 
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS 
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
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
        PatientDemographicsPRPAIN201307UV02RequestType request = PatientInfoRequestHelper
                .createPatientDemographicsRequest(subjectId);
        return port.getPatienInfo(request);
    }

    public CareRecordQUPCIN043200UV01ResponseType getMedications(II subjectId, String careProvisionCode,
            String dataStartDate, String dataEndDate, String serviceEndpoint) {
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

            CareRecordQUPCIN043100UV01RequestType request = CareRecordRequestHelper.createCareRecordRequest(subjectId,
                    careProvisionCode, dataStartDate, dataEndDate);

            return port.getMedications(request);
        }
    }

    public CareRecordQUPCIN043200UV01ResponseType getMedications(II subjectId, String careProvisionCode,
            String serviceEndpoint) {
        if (subjectId == null || subjectId.getExtension() == null || subjectId.getExtension().length() < 1) {
            return null;
        }

        if (service == null) {
            initService(serviceEndpoint);
        }

        CommonDataLayerPortType port = service.getCommonDataLayerPort();
        CareRecordQUPCIN043100UV01RequestType request = CareRecordRequestHelper.createCareRecordRequest(subjectId,
                careProvisionCode);

        return port.getMedications(request);
    }

    public CareRecordQUPCIN043200UV01ResponseType getAllergies(II subjectId, String careProvisionCode,
            String dataStartDate, String dataEndDate, String serviceEndpoint) {
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

            CareRecordQUPCIN043100UV01RequestType request = CareRecordRequestHelper.createCareRecordRequest(subjectId,
                    careProvisionCode, dataStartDate, dataEndDate);

            return port.getAllergies(request);
        }
    }

    public CareRecordQUPCIN043200UV01ResponseType getAllergies(II subjectId, String careProvisionCode,
            String serviceEndpoint) {
        if (subjectId == null || subjectId.getExtension() == null || subjectId.getExtension().length() < 1) {
            return null;
        }

        if (service == null) {
            initService(serviceEndpoint);
        }

        CommonDataLayerPortType port = service.getCommonDataLayerPort();
        CareRecordQUPCIN043100UV01RequestType request = CareRecordRequestHelper.createCareRecordRequest(subjectId,
                careProvisionCode);

        return port.getAllergies(request);
    }

    public CareRecordQUPCIN043200UV01ResponseType getProblems(II subjectId, String careProvisionCode,
            String dataStartDate, String dataEndDate, String serviceEndpoint) {
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

            CareRecordQUPCIN043100UV01RequestType request = CareRecordRequestHelper.createCareRecordRequest(subjectId,
                    careProvisionCode, dataStartDate, dataEndDate);

            return port.getProblems(request);
        }
    }

    public CareRecordQUPCIN043200UV01ResponseType getProblems(II subjectId, String careProvisionCode,
            String serviceEndpoint) {
        if (subjectId == null || subjectId.getExtension() == null || subjectId.getExtension().length() < 1) {
            return null;
        }

        if (service == null) {
            initService(serviceEndpoint);
        }

        CommonDataLayerPortType port = service.getCommonDataLayerPort();
        CareRecordQUPCIN043100UV01RequestType request = CareRecordRequestHelper.createCareRecordRequest(subjectId,
                careProvisionCode);

        return port.getProblems(request);
    }
}
