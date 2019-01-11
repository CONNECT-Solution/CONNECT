/*
 * Copyright (c) 2009-2019, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.admingui.loadtestdata;

import static gov.hhs.fha.nhinc.util.CoreHelpUtils.isId;

import gov.hhs.fha.nhinc.common.loadtestdatamanagement.AddressType;
import gov.hhs.fha.nhinc.common.loadtestdatamanagement.DeleteAddressRequestMessageType;
import gov.hhs.fha.nhinc.common.loadtestdatamanagement.DeleteDocumentRequestMessageType;
import gov.hhs.fha.nhinc.common.loadtestdatamanagement.DeleteEventCodeRequestMessageType;
import gov.hhs.fha.nhinc.common.loadtestdatamanagement.DeleteIdentifierRequestMessageType;
import gov.hhs.fha.nhinc.common.loadtestdatamanagement.DeletePatientRequestMessageType;
import gov.hhs.fha.nhinc.common.loadtestdatamanagement.DeletePersonNameRequestMessageType;
import gov.hhs.fha.nhinc.common.loadtestdatamanagement.DeletePhoneNumberRequestMessageType;
import gov.hhs.fha.nhinc.common.loadtestdatamanagement.DocumentMetadataType;
import gov.hhs.fha.nhinc.common.loadtestdatamanagement.DuplicateDocumentRequestMessageType;
import gov.hhs.fha.nhinc.common.loadtestdatamanagement.DuplicatePatientRequestMessageType;
import gov.hhs.fha.nhinc.common.loadtestdatamanagement.EventCodeType;
import gov.hhs.fha.nhinc.common.loadtestdatamanagement.GetAddressRequestMessageType;
import gov.hhs.fha.nhinc.common.loadtestdatamanagement.GetDocumentRequestMessageType;
import gov.hhs.fha.nhinc.common.loadtestdatamanagement.GetEventCodeRequestMessageType;
import gov.hhs.fha.nhinc.common.loadtestdatamanagement.GetIdentifierRequestMessageType;
import gov.hhs.fha.nhinc.common.loadtestdatamanagement.GetPatientByIdentifierRequestMessageType;
import gov.hhs.fha.nhinc.common.loadtestdatamanagement.GetPatientRequestMessageType;
import gov.hhs.fha.nhinc.common.loadtestdatamanagement.GetPersonNameRequestMessageType;
import gov.hhs.fha.nhinc.common.loadtestdatamanagement.GetPhoneNumberRequestMessageType;
import gov.hhs.fha.nhinc.common.loadtestdatamanagement.IdentifierType;
import gov.hhs.fha.nhinc.common.loadtestdatamanagement.ListAllAddressRequestMessageType;
import gov.hhs.fha.nhinc.common.loadtestdatamanagement.ListAllDocumentRequestMessageType;
import gov.hhs.fha.nhinc.common.loadtestdatamanagement.ListAllEventCodeRequestMessageType;
import gov.hhs.fha.nhinc.common.loadtestdatamanagement.ListAllIdentierRequestMessageType;
import gov.hhs.fha.nhinc.common.loadtestdatamanagement.ListAllPatientRequestMessageType;
import gov.hhs.fha.nhinc.common.loadtestdatamanagement.ListAllPersonNameRequestMessageType;
import gov.hhs.fha.nhinc.common.loadtestdatamanagement.ListAllPhoneNumberRequestMessageType;
import gov.hhs.fha.nhinc.common.loadtestdatamanagement.LoadTestDataSimpleResponseMessageType;
import gov.hhs.fha.nhinc.common.loadtestdatamanagement.PatientType;
import gov.hhs.fha.nhinc.common.loadtestdatamanagement.PersonNameType;
import gov.hhs.fha.nhinc.common.loadtestdatamanagement.PhoneNumberType;
import gov.hhs.fha.nhinc.common.loadtestdatamanagement.SaveAddressRequestMessageType;
import gov.hhs.fha.nhinc.common.loadtestdatamanagement.SaveDocumentRequestMessageType;
import gov.hhs.fha.nhinc.common.loadtestdatamanagement.SaveEventCodeRequestMessageType;
import gov.hhs.fha.nhinc.common.loadtestdatamanagement.SaveIdentifierRequestMessageType;
import gov.hhs.fha.nhinc.common.loadtestdatamanagement.SavePatientRequestMessageType;
import gov.hhs.fha.nhinc.common.loadtestdatamanagement.SavePersonNameRequestMessageType;
import gov.hhs.fha.nhinc.common.loadtestdatamanagement.SavePhoneNumberRequestMessageType;
import gov.hhs.fha.nhinc.docrepository.adapter.model.DocumentMetadata;
import gov.hhs.fha.nhinc.docrepository.adapter.model.EventCode;
import gov.hhs.fha.nhinc.loadtestdata.LoadTestDataException;
import gov.hhs.fha.nhinc.loadtestdata.LoadTestDataService;
import gov.hhs.fha.nhinc.loadtestdatamanagement.EntityLoadTestDataManagementPortType;
import gov.hhs.fha.nhinc.patientdb.model.Address;
import gov.hhs.fha.nhinc.patientdb.model.Identifier;
import gov.hhs.fha.nhinc.patientdb.model.Patient;
import gov.hhs.fha.nhinc.patientdb.model.Personname;
import gov.hhs.fha.nhinc.patientdb.model.Phonenumber;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ImportResource;
import org.springframework.stereotype.Component;

/**
 * @author ttang
 *
 */
@ImportResource("file:${nhinc.properties.dir}/LoadTestDataConfig.xml")
@Component
public class LoadTestDataManagement implements EntityLoadTestDataManagementPortType {
    private static final Logger LOG = LoggerFactory.getLogger(LoadTestDataManagement.class);
    private static final String ACT_SUCCESSFUL = "successful";
    private static final String ACT_FAIL = "fail";

    @Autowired
    private LoadTestDataService loadTestDataService;

    @Override
    public LoadTestDataSimpleResponseMessageType deleteAddress(DeleteAddressRequestMessageType request) {
        Address record = new Address(request.getAddress(), loadTestDataService.getPatientBy(request.getAddress().getPatientId()));
        boolean result = loadTestDataService.deleteAddress(record);
        if (result) {
            return buildSimpleResponse(Boolean.TRUE, ACT_SUCCESSFUL);
        }
        return buildSimpleResponse(Boolean.FALSE, ACT_FAIL);
    }

    @Override
    public LoadTestDataSimpleResponseMessageType deleteIdentifier(DeleteIdentifierRequestMessageType request) {
        Identifier record = new Identifier(request.getIdentifier(),
            loadTestDataService.getPatientBy(request.getIdentifier().getPatientId()));
        boolean result = loadTestDataService.deleteIdentifier(record);
        if (result) {
            return buildSimpleResponse(Boolean.TRUE, ACT_SUCCESSFUL);
        }
        return buildSimpleResponse(Boolean.FALSE, ACT_FAIL);
    }

    @Override
    public LoadTestDataSimpleResponseMessageType deletePatient(DeletePatientRequestMessageType request) {
        Patient record = new Patient(request.getPatient());
        boolean result = loadTestDataService.deletePatient(record);
        if (result) {
            return buildSimpleResponse(Boolean.TRUE, ACT_SUCCESSFUL);
        }
        return buildSimpleResponse(Boolean.FALSE, ACT_FAIL);
    }

    @Override
    public LoadTestDataSimpleResponseMessageType deletePersonName(DeletePersonNameRequestMessageType request) {
        Personname record = new Personname(request.getPersonName(),
            loadTestDataService.getPatientBy(request.getPersonName().getPatientId()));
        boolean result = loadTestDataService.deletePersonname(record);
        if (result) {
            return buildSimpleResponse(Boolean.TRUE, ACT_SUCCESSFUL);
        }
        return buildSimpleResponse(Boolean.FALSE, ACT_FAIL);
    }

    @Override
    public LoadTestDataSimpleResponseMessageType deletePhoneNumber(DeletePhoneNumberRequestMessageType request) {
        Phonenumber record = new Phonenumber(request.getPhoneNumber(),
            loadTestDataService.getPatientBy(request.getPhoneNumber().getPatientId()));
        boolean result = loadTestDataService.deletePhonenumber(record);
        if (result) {
            return buildSimpleResponse(Boolean.TRUE, ACT_SUCCESSFUL);
        }
        return buildSimpleResponse(Boolean.FALSE, ACT_FAIL);
    }


    @Override
    public LoadTestDataSimpleResponseMessageType deleteDocument(DeleteDocumentRequestMessageType request) {
        DocumentMetadata record = new DocumentMetadata(request.getDocument());
        boolean result = loadTestDataService.deleteDocument(record);
        if (result) {
            return buildSimpleResponse(Boolean.TRUE, ACT_SUCCESSFUL);
        }
        return buildSimpleResponse(Boolean.FALSE, ACT_FAIL);
    }

    @Override
    public LoadTestDataSimpleResponseMessageType deleteEventCode(DeleteEventCodeRequestMessageType request) {
        EventCode record = new EventCode(request.getEventCode(),
            loadTestDataService.getDocumentBy(request.getEventCode().getDocumentid()));
        boolean result = loadTestDataService.deleteEventCode(record);
        if (result) {
            return buildSimpleResponse(Boolean.TRUE, ACT_SUCCESSFUL);
        }
        return buildSimpleResponse(Boolean.FALSE, ACT_FAIL);
    }

    @Override
    public LoadTestDataSimpleResponseMessageType duplicateDocument(DuplicateDocumentRequestMessageType request) {
        if (!isId(request.getDocumentId())) {
            return buildSimpleResponse(Boolean.FALSE, "DocumentId is required to duplicate document.");
        }
        DocumentMetadataType document = loadTestDataService.duplicateDocument(request.getDocumentId())
            .getDocumentMetadataType();
        if (null != document && isId(document.getDocumentId())) {
            LoadTestDataSimpleResponseMessageType response = buildSimpleResponse(Boolean.TRUE, ACT_SUCCESSFUL);
            response.getDocumentMetadataList().add(document);
            return response;
        }
        return buildSimpleResponse(Boolean.FALSE, ACT_FAIL);
    }

    @Override
    public LoadTestDataSimpleResponseMessageType duplicatePatient(DuplicatePatientRequestMessageType request) {
        if (!isId(request.getPatientId())) {
            return buildSimpleResponse(Boolean.FALSE, ACT_FAIL);
        }
        PatientType record = loadTestDataService.duplicatePatient(request.getPatientId()).getPatientType();
        LoadTestDataSimpleResponseMessageType response =  buildSimpleResponse(Boolean.TRUE, ACT_SUCCESSFUL);
        response.getPatientList().add(record);
        return response;
    }

    @Override
    public LoadTestDataSimpleResponseMessageType getAddress(GetAddressRequestMessageType request) {
        if (!isId(request.getAddressId())) {
            return buildSimpleResponse(Boolean.FALSE, ACT_FAIL);
        }
        LoadTestDataSimpleResponseMessageType response = new LoadTestDataSimpleResponseMessageType();
        AddressType record = loadTestDataService.getAddressBy(request.getAddressId()).getAddessType();
        response.getAddressList().add(record);
        return response;
    }

    @Override
    public LoadTestDataSimpleResponseMessageType getDocument(GetDocumentRequestMessageType request) {
        if (!isId(request.getDocumentId())) {
            return buildSimpleResponse(Boolean.FALSE, ACT_FAIL);
        }
        LoadTestDataSimpleResponseMessageType response = new LoadTestDataSimpleResponseMessageType();
        DocumentMetadataType record = loadTestDataService.getDocumentBy(request.getDocumentId()).getDocumentMetadataType();
        response.getDocumentMetadataList().add(record);

        return response;
    }

    @Override
    public LoadTestDataSimpleResponseMessageType getEventCode(GetEventCodeRequestMessageType request) {
        if (!isId(request.getEventCodeId())) {
            return buildSimpleResponse(Boolean.FALSE, ACT_FAIL);
        }
        LoadTestDataSimpleResponseMessageType response = new LoadTestDataSimpleResponseMessageType();
        EventCodeType record = loadTestDataService.getEventCodeBy(request.getEventCodeId()).getEventCodeType();
        response.getEventCodeList().add(record);

        return response;
    }

    @Override
    public LoadTestDataSimpleResponseMessageType getIdentifier(GetIdentifierRequestMessageType request) {
        if (!isId(request.getIdentifierId())) {
            return buildSimpleResponse(Boolean.FALSE, ACT_FAIL);
        }
        LoadTestDataSimpleResponseMessageType response = new LoadTestDataSimpleResponseMessageType();
        IdentifierType record = loadTestDataService.getIdentifierBy(request.getIdentifierId()).getIdentifierType();
        response.getIdentifierList().add(record);
        return response;
    }

    @Override
    public LoadTestDataSimpleResponseMessageType getPatient(GetPatientRequestMessageType request) {
        if (!isId(request.getPatientId())) {
            return buildSimpleResponse(Boolean.FALSE, ACT_FAIL);
        }
        LoadTestDataSimpleResponseMessageType response = new LoadTestDataSimpleResponseMessageType();
        PatientType record = loadTestDataService.getPatientBy(request.getPatientId()).getPatientType();
        response.getPatientList().add(record);
        return response;
    }

    @Override
    public LoadTestDataSimpleResponseMessageType getPatientByIdentifier(
        GetPatientByIdentifierRequestMessageType request) {
        String identifierId = request.getIdentifierId();
        String identifierOrg = request.getIdentifierOrg();

        if (StringUtils.isBlank(identifierId) || StringUtils.isBlank(identifierOrg)) {
            return buildSimpleResponse(Boolean.FALSE, "Identifier: Id and Org are required.");
        }
        LoadTestDataSimpleResponseMessageType response = new LoadTestDataSimpleResponseMessageType();
        PatientType record = loadTestDataService.getPatientBy(identifierId, identifierOrg).getPatientType();
        response.getPatientList().add(record);
        return response;
    }

    @Override
    public LoadTestDataSimpleResponseMessageType getPersonName(GetPersonNameRequestMessageType request) {
        if (!isId(request.getPersonNameId())) {
            return buildSimpleResponse(Boolean.FALSE, ACT_FAIL);
        }
        LoadTestDataSimpleResponseMessageType response = new LoadTestDataSimpleResponseMessageType();
        PersonNameType record = loadTestDataService.getPersonnameBy(request.getPersonNameId()).getPersonNameType();
        response.getPersonNameList().add(record);
        return response;
    }

    @Override
    public LoadTestDataSimpleResponseMessageType getPhoneNumber(GetPhoneNumberRequestMessageType request) {
        if (!isId(request.getPhoneNumberId())) {
            return buildSimpleResponse(Boolean.FALSE, ACT_FAIL);
        }
        LoadTestDataSimpleResponseMessageType response = new LoadTestDataSimpleResponseMessageType();
        PhoneNumberType record = loadTestDataService.getPhonenumberBy(request.getPhoneNumberId()).getPhoneNumberType();
        response.getPhoneNumberList().add(record);
        return response;
    }

    @Override
    public LoadTestDataSimpleResponseMessageType listAllAddress(ListAllAddressRequestMessageType request) {
        if (!isId(request.getPatientId())) {
            return buildSimpleResponse(Boolean.FALSE, ACT_FAIL);
        }
        LoadTestDataSimpleResponseMessageType response = new LoadTestDataSimpleResponseMessageType();
        for (Address item : loadTestDataService.getAllAddressesBy(request.getPatientId())) {
            response.getAddressList().add(item.getAddessType());
        }
        return response;
    }
    @Override
    public LoadTestDataSimpleResponseMessageType listAllDocument(ListAllDocumentRequestMessageType request) {
        if (null == request) {
            return buildSimpleResponse(Boolean.FALSE, ACT_FAIL);
        }
        LoadTestDataSimpleResponseMessageType response = new LoadTestDataSimpleResponseMessageType();
        for (DocumentMetadata item : loadTestDataService.getAllDocuments()) {
            response.getDocumentMetadataList().add(item.getDocumentMetadataType(DocumentMetadata.METADATA_ONLY));
        }
        return response;
    }

    @Override
    public LoadTestDataSimpleResponseMessageType listAllEventCode(ListAllEventCodeRequestMessageType request) {
        if (!isId(request.getDocumentId())) {
            return buildSimpleResponse(Boolean.FALSE, ACT_FAIL);
        }
        LoadTestDataSimpleResponseMessageType response = new LoadTestDataSimpleResponseMessageType();
        for (EventCode item : loadTestDataService.getAllEventCodesBy(request.getDocumentId())) {
            response.getEventCodeList().add(item.getEventCodeType());
        }
        return response;
    }

    @Override
    public LoadTestDataSimpleResponseMessageType listAllIdentier(ListAllIdentierRequestMessageType request) {
        if (!isId(request.getPatientId())) {
            return buildSimpleResponse(Boolean.FALSE, ACT_FAIL);
        }
        LoadTestDataSimpleResponseMessageType response = new LoadTestDataSimpleResponseMessageType();
        for (Identifier item : loadTestDataService.getAllIdentiersBy(request.getPatientId())) {
            response.getIdentifierList().add(item.getIdentifierType());
        }
        return response;
    }

    @Override
    public LoadTestDataSimpleResponseMessageType listAllPatient(ListAllPatientRequestMessageType request) {
        LoadTestDataSimpleResponseMessageType response = new LoadTestDataSimpleResponseMessageType();
        for (Patient item : loadTestDataService.getAllPatients()) {
            response.getPatientList().add(item.getPatientType());
        }
        return response;
    }

    @Override
    public LoadTestDataSimpleResponseMessageType listAllPersonName(ListAllPersonNameRequestMessageType request) {
        if (!isId(request.getPatientId())) {
            return buildSimpleResponse(Boolean.FALSE, ACT_FAIL);
        }
        LoadTestDataSimpleResponseMessageType response = new LoadTestDataSimpleResponseMessageType();
        for (Personname item : loadTestDataService.getAllPersonnamesBy(request.getPatientId())) {
            response.getPersonNameList().add(item.getPersonNameType());
        }
        return response;
    }

    @Override
    public LoadTestDataSimpleResponseMessageType listAllPhoneNumber(ListAllPhoneNumberRequestMessageType request) {
        if (!isId(request.getPatientId())) {
            return buildSimpleResponse(Boolean.FALSE, ACT_FAIL);
        }
        LoadTestDataSimpleResponseMessageType response = new LoadTestDataSimpleResponseMessageType();
        for (Phonenumber item : loadTestDataService.getAllPhonenumbersBy(request.getPatientId())) {
            response.getPhoneNumberList().add(item.getPhoneNumberType());
        }
        return response;
    }

    @Override
    public LoadTestDataSimpleResponseMessageType saveAddress(SaveAddressRequestMessageType request) {
        Patient patient = loadTestDataService.getPatientBy(request.getAddress().getPatientId());
        if(null != patient){
            try{
                Address record = new Address(request.getAddress(), patient);
                loadTestDataService.saveAddress(record);
                if (isId(record.getAddressId())) {
                    return buildSimpleResponse(Boolean.TRUE, ACT_SUCCESSFUL, record.getAddressId());
                }
            } catch (LoadTestDataException ex) {
                LOG.error("error while saving patient address: {}", ex.getLocalizedMessage(), ex);
            }
        }
        return buildSimpleResponse(Boolean.FALSE, ACT_FAIL);
    }

    @Override
    public LoadTestDataSimpleResponseMessageType saveDocument(SaveDocumentRequestMessageType request) {
        if (null != request) {
            try {
                DocumentMetadata record = new DocumentMetadata(request.getDocument());
                loadTestDataService.saveDocument(record);
                if (isId(record.getDocumentid())) {
                    return buildSimpleResponse(Boolean.TRUE, ACT_SUCCESSFUL, record.getDocumentid());
                }
            } catch (LoadTestDataException ex) {
                LOG.error("error while saving document: {}", ex.getLocalizedMessage(), ex);
            }
        }
        return buildSimpleResponse(Boolean.FALSE, ACT_FAIL);
    }

    @Override
    public LoadTestDataSimpleResponseMessageType saveEventCode(SaveEventCodeRequestMessageType request) {
        DocumentMetadata meta = loadTestDataService.getDocumentBy(request.getEventCode().getDocumentid());
        if (null != meta) {
            try {
                EventCode record = new EventCode(request.getEventCode(), meta);
                loadTestDataService.saveEventCode(record);
                if (isId(record.getEventCodeId())) {
                    return buildSimpleResponse(Boolean.TRUE, ACT_SUCCESSFUL, record.getEventCodeId());
                }
            } catch (LoadTestDataException ex) {
                LOG.error("error while saving document eventCode: {}", ex.getLocalizedMessage(), ex);
            }
        }
        return buildSimpleResponse(Boolean.FALSE, ACT_FAIL);
    }

    @Override
    public LoadTestDataSimpleResponseMessageType saveIdentifier(SaveIdentifierRequestMessageType request) {
        Patient patient = loadTestDataService.getPatientBy(request.getIdentifier().getPatientId());
        if (null != patient) {
            try {
                Identifier record = new Identifier(request.getIdentifier(), patient);
                loadTestDataService.saveIdentifier(record);
                if (isId(record.getIdentifierId())) {
                    return buildSimpleResponse(Boolean.TRUE, ACT_SUCCESSFUL, record.getIdentifierId());
                }
            } catch (LoadTestDataException ex) {
                LOG.error("error while saving patient identifier: {}", ex.getLocalizedMessage(), ex);
            }
        }
        return buildSimpleResponse(Boolean.FALSE, ACT_FAIL);
    }

    @Override
    public LoadTestDataSimpleResponseMessageType savePatient(SavePatientRequestMessageType request) {
        if (null != request.getPatient()) {
            try {
                Patient record = new Patient(request.getPatient());
                loadTestDataService.savePatient(record);
                if (isId(record.getPatientId())) {
                    return buildSimpleResponse(Boolean.TRUE, ACT_SUCCESSFUL, record.getPatientId());
                }
            } catch (LoadTestDataException ex) {
                LOG.error("error while saving patient: {}", ex.getLocalizedMessage(), ex);
            }
        }
        return buildSimpleResponse(Boolean.FALSE, ACT_FAIL);
    }

    @Override
    public LoadTestDataSimpleResponseMessageType savePersonName(SavePersonNameRequestMessageType request) {
        Patient patient = loadTestDataService.getPatientBy(request.getPersonName().getPatientId());
        if (null != patient) {
            try {
                Personname record = new Personname(request.getPersonName(), patient);
                loadTestDataService.savePersonname(record);
                if (isId(record.getPersonnameId())) {
                    return buildSimpleResponse(Boolean.TRUE, ACT_SUCCESSFUL, record.getPersonnameId());
                }
            } catch (LoadTestDataException ex) {
                LOG.error("error while saving patient personname: {}", ex.getLocalizedMessage(), ex);
            }
        }
        return buildSimpleResponse(Boolean.FALSE, ACT_FAIL);
    }

    @Override
    public LoadTestDataSimpleResponseMessageType savePhoneNumber(SavePhoneNumberRequestMessageType request) {
        Patient patient = loadTestDataService.getPatientBy(request.getPhoneNumber().getPatientId());
        if (null != patient) {
            try {
                Phonenumber record = new Phonenumber(request.getPhoneNumber(), patient);
                loadTestDataService.savePhonenumber(record);
                if (isId(record.getPhonenumberId())) {
                    return buildSimpleResponse(Boolean.TRUE, ACT_SUCCESSFUL, record.getPhonenumberId());
                }
            } catch (LoadTestDataException ex) {
                LOG.error("error while saving patient phonenumber: {}", ex.getLocalizedMessage(), ex);
            }
        }
        return buildSimpleResponse(Boolean.FALSE, ACT_FAIL);
    }

    private static LoadTestDataSimpleResponseMessageType buildSimpleResponse(Boolean status, String message) {
        return buildSimpleResponse(status, message, null);
    }
    private static LoadTestDataSimpleResponseMessageType buildSimpleResponse(Boolean status, String message,
        Long saveRecordId) {
        LoadTestDataSimpleResponseMessageType build = new LoadTestDataSimpleResponseMessageType();
        build.setStatus(status);
        build.setMessage(message);
        if (isId(saveRecordId)) {
            build.setSaveRecordId(saveRecordId);
        }
        return build;
    }

}
