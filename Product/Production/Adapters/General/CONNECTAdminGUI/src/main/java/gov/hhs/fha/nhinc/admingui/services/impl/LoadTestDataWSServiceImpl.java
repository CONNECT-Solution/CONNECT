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
package gov.hhs.fha.nhinc.admingui.services.impl;

import static gov.hhs.fha.nhinc.admingui.util.HelperUtil.buildConfigAssertion;
import static gov.hhs.fha.nhinc.util.CoreHelpUtils.firstItem;

import gov.hhs.fha.nhinc.admingui.constant.AdminWSConstants;
import gov.hhs.fha.nhinc.admingui.services.LoadTestDataWSService;
import gov.hhs.fha.nhinc.admingui.util.HelperUtil;
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
import gov.hhs.fha.nhinc.exchangemgr.ExchangeManagerException;
import gov.hhs.fha.nhinc.loadtestdata.LoadTestDataException;
import gov.hhs.fha.nhinc.loadtestdatamanagement.EntityLoadTestDataManagementPortType;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClient;
import gov.hhs.fha.nhinc.util.CoreHelpUtils;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ttang
 *
 */
public class LoadTestDataWSServiceImpl implements LoadTestDataWSService {
    private static final String WS_ADDRESSING_ACTION = "urn:gov:hhs:fha:nhinc:loadtestdatamanagement:EntityLoadTestDataManagement";
    private static final Class<EntityLoadTestDataManagementPortType> PORT_TYPE = EntityLoadTestDataManagementPortType.class;
    private static final WebServiceProxyHelper proxyHelper = new WebServiceProxyHelper();
    private static final Logger LOG = LoggerFactory.getLogger(LoadTestDataWSServiceImpl.class);

    private static Object client = null;

    @Override
    public List<PatientType> getAllPatients() {
        ListAllPatientRequestMessageType request = new ListAllPatientRequestMessageType();
        request.setConfigAssertion(buildConfigAssertion());

        try {
            LoadTestDataSimpleResponseMessageType response = (LoadTestDataSimpleResponseMessageType) invokeClientPort(
                AdminWSConstants.ADMIN_LTD_LISTALLPATIENT, request);
            logDebug(AdminWSConstants.ADMIN_LTD_LISTALLPATIENT, response.getPatientList().size());
            return response.getPatientList();
        } catch (Exception e) {
            LOG.error("error during get-all patients: {}", e.getLocalizedMessage(), e);
        }
        return new ArrayList<>();
    }

    @Override
    public List<PersonNameType> getAllPersonNamesBy(Long patientId) {
        ListAllPersonNameRequestMessageType request = new ListAllPersonNameRequestMessageType();
        request.setConfigAssertion(buildConfigAssertion());
        request.setPatientId(patientId);

        try {
            LoadTestDataSimpleResponseMessageType response = (LoadTestDataSimpleResponseMessageType) invokeClientPort(
                AdminWSConstants.ADMIN_LTD_LISTALLPERSONNAME, request);
            logDebug(AdminWSConstants.ADMIN_LTD_LISTALLPERSONNAME, response.getPersonNameList().size());
            return response.getPersonNameList();
        } catch (Exception e) {
            LOG.error("error during get-all personnames: {}, {}", patientId, e.getLocalizedMessage(), e);
        }
        return new ArrayList<>();
    }

    @Override
    public List<AddressType> getAllAddressesBy(Long patientId) {
        ListAllAddressRequestMessageType request = new ListAllAddressRequestMessageType();
        request.setConfigAssertion(buildConfigAssertion());
        request.setPatientId(patientId);

        try {
            LoadTestDataSimpleResponseMessageType response = (LoadTestDataSimpleResponseMessageType) invokeClientPort(
                AdminWSConstants.ADMIN_LTD_LISTALLADDRESS, request);
            logDebug(AdminWSConstants.ADMIN_LTD_LISTALLADDRESS, response.getAddressList().size());
            return response.getAddressList();
        } catch (Exception e) {
            LOG.error("error during get-all addresses: {}, {}", patientId, e.getLocalizedMessage(), e);
        }
        return new ArrayList<>();
    }

    @Override
    public List<IdentifierType> getAllIdentiersBy(Long patientId) {
        ListAllIdentierRequestMessageType request = new ListAllIdentierRequestMessageType();
        request.setConfigAssertion(buildConfigAssertion());
        request.setPatientId(patientId);

        try {
            LoadTestDataSimpleResponseMessageType response = (LoadTestDataSimpleResponseMessageType) invokeClientPort(
                AdminWSConstants.ADMIN_LTD_LISTALLIDENTIER, request);
            logDebug(AdminWSConstants.ADMIN_LTD_LISTALLIDENTIER, response.getIdentifierList().size());
            return response.getIdentifierList();
        } catch (Exception e) {
            LOG.error("error during get-all identiers: {}, {}", patientId, e.getLocalizedMessage(), e);
        }
        return new ArrayList<>();
    }

    @Override
    public List<PhoneNumberType> getAllPhoneNumbersBy(Long patientId) {
        ListAllPhoneNumberRequestMessageType request = new ListAllPhoneNumberRequestMessageType();
        request.setConfigAssertion(buildConfigAssertion());
        request.setPatientId(patientId);

        try {
            LoadTestDataSimpleResponseMessageType response = (LoadTestDataSimpleResponseMessageType) invokeClientPort(
                AdminWSConstants.ADMIN_LTD_LISTALLPHONENUMBER, request);
            logDebug(AdminWSConstants.ADMIN_LTD_LISTALLPHONENUMBER, response.getPhoneNumberList().size());
            return response.getPhoneNumberList();
        } catch (Exception e) {
            LOG.error("error during get-all phonenumbers: {}, {}", patientId, e.getLocalizedMessage(), e);
        }
        return new ArrayList<>();
    }

    @Override
    public boolean deletePatient(PatientType patient) {
        DeletePatientRequestMessageType request = new DeletePatientRequestMessageType();
        request.setConfigAssertion(buildConfigAssertion());
        request.setPatient(patient);

        try {
            LoadTestDataSimpleResponseMessageType response = (LoadTestDataSimpleResponseMessageType) invokeClientPort(
                AdminWSConstants.ADMIN_LTD_DELETEPATIENT, request);
            logDebug(AdminWSConstants.ADMIN_LTD_DELETEPATIENT, response.isStatus(), response.getMessage());
            return response.isStatus();
        } catch (Exception e) {
            LOG.error("error during delete patient: {}, {}", patient.getPatientId(), e.getLocalizedMessage(), e);
        }
        return false;
    }

    @Override
    public boolean deletePersonName(PersonNameType personname) {
        DeletePersonNameRequestMessageType request = new DeletePersonNameRequestMessageType();
        request.setConfigAssertion(buildConfigAssertion());
        request.setPersonName(personname);

        try {
            LoadTestDataSimpleResponseMessageType response = (LoadTestDataSimpleResponseMessageType) invokeClientPort(
                AdminWSConstants.ADMIN_LTD_DELETEPERSONNAME, request);
            logDebug(AdminWSConstants.ADMIN_LTD_DELETEPERSONNAME, response.isStatus(), response.getMessage());
            return response.isStatus();
        } catch (Exception e) {
            LOG.error("error during delete personname: {}, {}", personname.getPersonNameId(), e.getLocalizedMessage(),
                e);
        }
        return false;
    }

    @Override
    public boolean deleteAddress(AddressType address) {
        DeleteAddressRequestMessageType request = new DeleteAddressRequestMessageType();
        request.setConfigAssertion(buildConfigAssertion());
        request.setAddress(address);

        try {
            LoadTestDataSimpleResponseMessageType response = (LoadTestDataSimpleResponseMessageType) invokeClientPort(
                AdminWSConstants.ADMIN_LTD_DELETEADDRESS, request);
            logDebug(AdminWSConstants.ADMIN_LTD_DELETEADDRESS, response.isStatus(), response.getMessage());
            return response.isStatus();
        } catch (Exception e) {
            LOG.error("error during delete address: {}, {}", address.getAddressId(), e.getLocalizedMessage(), e);
        }
        return false;
    }

    @Override
    public boolean deleteIdentifier(IdentifierType identifier) {
        DeleteIdentifierRequestMessageType request = new DeleteIdentifierRequestMessageType();
        request.setConfigAssertion(buildConfigAssertion());
        request.setIdentifier(identifier);

        try {
            LoadTestDataSimpleResponseMessageType response = (LoadTestDataSimpleResponseMessageType) invokeClientPort(
                AdminWSConstants.ADMIN_LTD_DELETEIDENTIFIER, request);
            logDebug(AdminWSConstants.ADMIN_LTD_DELETEIDENTIFIER, response.isStatus(), response.getMessage());
            return response.isStatus();
        } catch (Exception e) {
            LOG.error("error during delete identifier: {}, {}", identifier.getIdentifierId(), e.getLocalizedMessage(),
                e);
        }
        return false;
    }

    @Override
    public boolean deletePhoneNumber(PhoneNumberType phonenumber) {
        DeletePhoneNumberRequestMessageType request = new DeletePhoneNumberRequestMessageType();
        request.setConfigAssertion(buildConfigAssertion());
        request.setPhoneNumber(phonenumber);

        try {
            LoadTestDataSimpleResponseMessageType response = (LoadTestDataSimpleResponseMessageType) invokeClientPort(
                AdminWSConstants.ADMIN_LTD_DELETEPHONENUMBER, request);
            logDebug(AdminWSConstants.ADMIN_LTD_DELETEPHONENUMBER, response.isStatus(), response.getMessage());
            return response.isStatus();
        } catch (Exception e) {
            LOG.error("error during delete phonenumber: {}, {}", phonenumber.getPhoneNumberId(),
                e.getLocalizedMessage(), e);
        }
        return false;
    }

    @Override
    public PatientType getPatientBy(Long patientId) {
        GetPatientRequestMessageType request = new GetPatientRequestMessageType();
        request.setConfigAssertion(buildConfigAssertion());
        request.setPatientId(patientId);

        try {
            LoadTestDataSimpleResponseMessageType response = (LoadTestDataSimpleResponseMessageType) invokeClientPort(
                AdminWSConstants.ADMIN_LTD_GETPATIENT, request);
            logDebug(AdminWSConstants.ADMIN_LTD_GETPATIENT, response.getPatientList().size());
            return firstItem(response.getPatientList());
        } catch (Exception e) {
            LOG.error("error during get patient: {}, {}", patientId, e.getLocalizedMessage(), e);
        }
        return null;
    }

    @Override
    public PatientType duplicatePatient(Long patientId) {
        DuplicatePatientRequestMessageType request = new DuplicatePatientRequestMessageType();
        request.setConfigAssertion(buildConfigAssertion());
        request.setPatientId(patientId);

        try {
            LoadTestDataSimpleResponseMessageType response = (LoadTestDataSimpleResponseMessageType) invokeClientPort(
                AdminWSConstants.ADMIN_LTD_DUPLICATEPATIENT, request);
            logDebug(AdminWSConstants.ADMIN_LTD_DUPLICATEPATIENT, response.isStatus(), response.getMessage());
            return firstItem(response.getPatientList());
        } catch (Exception e) {
            LOG.error("error during duplicate patient: {}, {}", patientId, e.getLocalizedMessage(), e);
        }
        return null;
    }

    @Override
    public PersonNameType getPersonNameBy(Long personnameId) {
        GetPersonNameRequestMessageType request = new GetPersonNameRequestMessageType();
        request.setConfigAssertion(buildConfigAssertion());
        request.setPersonNameId(personnameId);

        try {
            LoadTestDataSimpleResponseMessageType response = (LoadTestDataSimpleResponseMessageType) invokeClientPort(
                AdminWSConstants.ADMIN_LTD_GETPERSONNAME, request);
            logDebug(AdminWSConstants.ADMIN_LTD_GETPERSONNAME, response.isStatus(), response.getMessage());
            return firstItem(response.getPersonNameList());
        } catch (Exception e) {
            LOG.error("error during retrieval personname: {}, {}", personnameId, e.getLocalizedMessage(), e);
        }
        return null;
    }

    @Override
    public AddressType getAddressBy(Long addressId) {
        GetAddressRequestMessageType request = new GetAddressRequestMessageType();
        request.setConfigAssertion(buildConfigAssertion());
        request.setAddressId(addressId);

        try {
            LoadTestDataSimpleResponseMessageType response = (LoadTestDataSimpleResponseMessageType) invokeClientPort(
                AdminWSConstants.ADMIN_LTD_GETADDRESS, request);
            logDebug(AdminWSConstants.ADMIN_LTD_GETADDRESS, response.isStatus(), response.getMessage());
            return firstItem(response.getAddressList());
        } catch (Exception e) {
            LOG.error("error during retrieval address: {}, {}", addressId, e.getLocalizedMessage(), e);
        }
        return null;
    }

    @Override
    public IdentifierType getIdentifierBy(Long identifierId) {
        GetIdentifierRequestMessageType request = new GetIdentifierRequestMessageType();
        request.setConfigAssertion(buildConfigAssertion());
        request.setIdentifierId(identifierId);

        try {
            LoadTestDataSimpleResponseMessageType response = (LoadTestDataSimpleResponseMessageType) invokeClientPort(
                AdminWSConstants.ADMIN_LTD_GETIDENTIFIER, request);
            logDebug(AdminWSConstants.ADMIN_LTD_GETIDENTIFIER, response.isStatus(), response.getMessage());
            return firstItem(response.getIdentifierList());
        } catch (Exception e) {
            LOG.error("error during retrieval identifier: {}, {}", identifierId, e.getLocalizedMessage(), e);
        }
        return null;
    }

    @Override
    public PhoneNumberType getPhoneNumberBy(Long phonenumberId) {
        GetPhoneNumberRequestMessageType request = new GetPhoneNumberRequestMessageType();
        request.setConfigAssertion(buildConfigAssertion());
        request.setPhoneNumberId(phonenumberId);

        try {
            LoadTestDataSimpleResponseMessageType response = (LoadTestDataSimpleResponseMessageType) invokeClientPort(
                AdminWSConstants.ADMIN_LTD_GETPHONENUMBER, request);
            logDebug(AdminWSConstants.ADMIN_LTD_GETPHONENUMBER, response.isStatus(), response.getMessage());
            return firstItem(response.getPhoneNumberList());
        } catch (Exception e) {
            LOG.error("error during retrieval identifier: {}, {}", phonenumberId, e.getLocalizedMessage(), e);
        }
        return null;
    }

    @Override
    public boolean savePatient(PatientType patient) throws LoadTestDataException {
        SavePatientRequestMessageType request = new SavePatientRequestMessageType();
        request.setConfigAssertion(buildConfigAssertion());
        request.setPatient(patient);

        try {
            LoadTestDataSimpleResponseMessageType response = (LoadTestDataSimpleResponseMessageType) invokeClientPort(
                AdminWSConstants.ADMIN_LTD_SAVEPATIENT, request);
            logDebug(AdminWSConstants.ADMIN_LTD_SAVEPATIENT, response.isStatus(), response.getMessage());
            if (response.isStatus()) {
                patient.setPatientId(response.getSaveRecordId());
            }
            return response.isStatus();
        } catch (Exception e) {
            LOG.error("error during save patient: {}", e.getLocalizedMessage(), e);
        }
        return false;
    }

    @Override
    public boolean savePersonName(PersonNameType personname) throws LoadTestDataException {
        SavePersonNameRequestMessageType request = new SavePersonNameRequestMessageType();
        request.setConfigAssertion(buildConfigAssertion());
        request.setPersonName(personname);

        try {
            LoadTestDataSimpleResponseMessageType response = (LoadTestDataSimpleResponseMessageType) invokeClientPort(
                AdminWSConstants.ADMIN_LTD_SAVEPERSONNAME, request);
            logDebug(AdminWSConstants.ADMIN_LTD_SAVEPERSONNAME, response.isStatus(), response.getMessage());
            return response.isStatus();
        } catch (Exception e) {
            LOG.error("error during save personname: {}", e.getLocalizedMessage(), e);
        }
        return false;
    }

    @Override
    public boolean saveAddress(AddressType address) throws LoadTestDataException {
        SaveAddressRequestMessageType request = new SaveAddressRequestMessageType();
        request.setConfigAssertion(buildConfigAssertion());
        request.setAddress(address);

        try {
            LoadTestDataSimpleResponseMessageType response = (LoadTestDataSimpleResponseMessageType) invokeClientPort(
                AdminWSConstants.ADMIN_LTD_SAVEADDRESS, request);
            logDebug(AdminWSConstants.ADMIN_LTD_SAVEADDRESS, response.isStatus(), response.getMessage());
            return response.isStatus();
        } catch (Exception e) {
            LOG.error("error during save address: {}", e.getLocalizedMessage(), e);
        }
        return false;
    }

    @Override
    public boolean saveIdentifier(IdentifierType identifier) throws LoadTestDataException {
        SaveIdentifierRequestMessageType request = new SaveIdentifierRequestMessageType();
        request.setConfigAssertion(buildConfigAssertion());
        request.setIdentifier(identifier);

        try {
            LoadTestDataSimpleResponseMessageType response = (LoadTestDataSimpleResponseMessageType) invokeClientPort(
                AdminWSConstants.ADMIN_LTD_SAVEIDENTIFIER, request);
            logDebug(AdminWSConstants.ADMIN_LTD_SAVEIDENTIFIER, response.isStatus(), response.getMessage());
            return response.isStatus();
        } catch (Exception e) {
            LOG.error("error during save identifier: {}", e.getLocalizedMessage(), e);
        }
        return false;
    }

    @Override
    public boolean savePhoneNumber(PhoneNumberType phonenumber) throws LoadTestDataException {
        SavePhoneNumberRequestMessageType request = new SavePhoneNumberRequestMessageType();
        request.setConfigAssertion(buildConfigAssertion());
        request.setPhoneNumber(phonenumber);

        try {
            LoadTestDataSimpleResponseMessageType response = (LoadTestDataSimpleResponseMessageType) invokeClientPort(
                AdminWSConstants.ADMIN_LTD_SAVEPHONENUMBER, request);
            logDebug(AdminWSConstants.ADMIN_LTD_SAVEPHONENUMBER, response.isStatus(), response.getMessage());
            return response.isStatus();
        } catch (Exception e) {
            LOG.error("error during save phonenumber: {}", e.getLocalizedMessage(), e);
        }
        return false;
    }

    @Override
    public List<DocumentMetadataType> getAllDocuments() {
        ListAllDocumentRequestMessageType request = new ListAllDocumentRequestMessageType();
        request.setConfigAssertion(buildConfigAssertion());

        try {
            LoadTestDataSimpleResponseMessageType response = (LoadTestDataSimpleResponseMessageType) invokeClientPort(
                AdminWSConstants.ADMIN_LTD_LISTALLDOCUMENT, request);
            logDebug(AdminWSConstants.ADMIN_LTD_LISTALLDOCUMENT, response.isStatus(), response.getMessage());
            return response.getDocumentMetadataList();
        } catch (Exception e) {
            LOG.error("error during get-all documents: {}", e.getLocalizedMessage(), e);
        }
        return new LinkedList<>();
    }


    @Override
    public List<EventCodeType> getAllEventCodesBy(Long documentId) {
        ListAllEventCodeRequestMessageType request = new ListAllEventCodeRequestMessageType();
        request.setConfigAssertion(buildConfigAssertion());
        request.setDocumentId(documentId);

        try {
            LoadTestDataSimpleResponseMessageType response = (LoadTestDataSimpleResponseMessageType) invokeClientPort(
                AdminWSConstants.ADMIN_LTD_LISTALLEVENTCODE, request);
            logDebug(AdminWSConstants.ADMIN_LTD_LISTALLEVENTCODE, response.isStatus(), response.getMessage());
            return response.getEventCodeList();
        } catch (Exception e) {
            LOG.error("error during get-all eventCodes: {}, {}", documentId, e.getLocalizedMessage(), e);
        }
        return new LinkedList<>();
    }

    @Override
    public DocumentMetadataType duplicateDocument(Long documentId) {
        DuplicateDocumentRequestMessageType request = new DuplicateDocumentRequestMessageType();
        request.setConfigAssertion(buildConfigAssertion());
        request.setDocumentId(documentId);

        try {
            LoadTestDataSimpleResponseMessageType response = (LoadTestDataSimpleResponseMessageType) invokeClientPort(
                AdminWSConstants.ADMIN_LTD_DUPLICATEDOCUMENT, request);
            logDebug(AdminWSConstants.ADMIN_LTD_DUPLICATEDOCUMENT, response.isStatus(), response.getMessage());
            return firstItem(response.getDocumentMetadataList());
        } catch (Exception e) {
            LOG.error("error during duplicate document: {}, {}", documentId, e.getLocalizedMessage(), e);
        }
        return null;
    }

    @Override
    public boolean deleteDocument(DocumentMetadataType document) {
        DeleteDocumentRequestMessageType request = new DeleteDocumentRequestMessageType();
        request.setConfigAssertion(buildConfigAssertion());
        request.setDocument(document);

        try {
            LoadTestDataSimpleResponseMessageType response = (LoadTestDataSimpleResponseMessageType) invokeClientPort(
                AdminWSConstants.ADMIN_LTD_DELETEDOCUMENT, request);
            logDebug(AdminWSConstants.ADMIN_LTD_DELETEDOCUMENT, response.isStatus(), response.getMessage());
            return response.isStatus();
        } catch (Exception e) {
            LOG.error("error during delete document: {}", e.getLocalizedMessage(), e);
        }
        return false;
    }

    @Override
    public boolean deleteEventCode(EventCodeType eventCode) {
        DeleteEventCodeRequestMessageType request = new DeleteEventCodeRequestMessageType();
        request.setConfigAssertion(buildConfigAssertion());
        request.setEventCode(eventCode);

        try {
            LoadTestDataSimpleResponseMessageType response = (LoadTestDataSimpleResponseMessageType) invokeClientPort(
                AdminWSConstants.ADMIN_LTD_DELETEEVENTCODE, request);
            logDebug(AdminWSConstants.ADMIN_LTD_DELETEEVENTCODE, response.isStatus(), response.getMessage());
            return response.isStatus();
        } catch (Exception e) {
            LOG.error("error during delete eventcode: {}", e.getLocalizedMessage(), e);
        }
        return false;
    }

    @Override
    public DocumentMetadataType getDocumentBy(Long documentId) {
        GetDocumentRequestMessageType request = new GetDocumentRequestMessageType();
        request.setConfigAssertion(buildConfigAssertion());
        request.setDocumentId(documentId);

        try {
            LoadTestDataSimpleResponseMessageType response = (LoadTestDataSimpleResponseMessageType) invokeClientPort(
                AdminWSConstants.ADMIN_LTD_GETDOCUMENT, request);
            logDebug(AdminWSConstants.ADMIN_LTD_GETDOCUMENT, response.isStatus(), response.getMessage());
            return firstItem(response.getDocumentMetadataList());
        } catch (Exception e) {
            LOG.error("error during get document: {}, {}", documentId, e.getLocalizedMessage(), e);
        }
        return null;
    }

    @Override
    public EventCodeType getEventCodeBy(Long eventCodeId) {
        GetEventCodeRequestMessageType request = new GetEventCodeRequestMessageType();
        request.setConfigAssertion(buildConfigAssertion());
        request.setEventCodeId(eventCodeId);

        try {
            LoadTestDataSimpleResponseMessageType response = (LoadTestDataSimpleResponseMessageType) invokeClientPort(
                AdminWSConstants.ADMIN_LTD_GETEVENTCODE, request);
            logDebug(AdminWSConstants.ADMIN_LTD_GETEVENTCODE, response.isStatus(), response.getMessage());
            return firstItem(response.getEventCodeList());
        } catch (Exception e) {
            LOG.error("error during get eventCode: {}, {}", eventCodeId, e.getLocalizedMessage(), e);
        }
        return null;
    }

    @Override
    public boolean saveDocument(DocumentMetadataType document) throws LoadTestDataException {
        SaveDocumentRequestMessageType request = new SaveDocumentRequestMessageType();
        request.setConfigAssertion(buildConfigAssertion());
        request.setDocument(document);

        try {
            LoadTestDataSimpleResponseMessageType response = (LoadTestDataSimpleResponseMessageType) invokeClientPort(
                AdminWSConstants.ADMIN_LTD_SAVEDOCUMENT, request);
            logDebug(AdminWSConstants.ADMIN_LTD_SAVEDOCUMENT, response.isStatus(), response.getMessage());
            if (CoreHelpUtils.isId(response.getSaveRecordId())) {
                document.setDocumentId(response.getSaveRecordId());
            }
            return response.isStatus();
        } catch (Exception e) {
            LOG.error("error during save document: {}", e.getLocalizedMessage(), e);
        }
        return false;
    }

    @Override
    public boolean saveEventCode(EventCodeType eventCode) throws LoadTestDataException {
        SaveEventCodeRequestMessageType request = new SaveEventCodeRequestMessageType();
        request.setConfigAssertion(buildConfigAssertion());
        request.setEventCode(eventCode);

        try {
            LoadTestDataSimpleResponseMessageType response = (LoadTestDataSimpleResponseMessageType) invokeClientPort(
                AdminWSConstants.ADMIN_LTD_SAVEEVENTCODE, request);
            logDebug(AdminWSConstants.ADMIN_LTD_SAVEEVENTCODE, response.isStatus(), response.getMessage());
            return response.isStatus();
        } catch (Exception e) {
            LOG.error("error during save eventCode: {}", e.getLocalizedMessage(), e);
        }
        return false;
    }

    @Override
    public PatientType getPatientBy(String identifierId, String identifierOrg) {
        GetPatientByIdentifierRequestMessageType request = new GetPatientByIdentifierRequestMessageType();
        request.setConfigAssertion(buildConfigAssertion());
        request.setIdentifierId(identifierId);
        request.setIdentifierOrg(identifierOrg);

        try {
            LoadTestDataSimpleResponseMessageType response = (LoadTestDataSimpleResponseMessageType) invokeClientPort(
                AdminWSConstants.ADMIN_LTD_GETPATIENT_BYIDENTIFIER, request);
            logDebug(AdminWSConstants.ADMIN_LTD_GETPATIENT_BYIDENTIFIER, response.isStatus(), response.getMessage());
            return firstItem(response.getPatientList());
        } catch (Exception e) {
            LOG.error("error during get patientByIdentifier: {}, {}, {}", identifierId, identifierOrg,
                e.getLocalizedMessage(), e);
        }
        return null;
    }

    private static <T> Object invokeClientPort(String serviceName, T request)
        throws Exception {
        String url = proxyHelper
            .getAdapterEndPointFromConnectionManager(AdminWSConstants.ENTITY_LOAD_TEST_DATA_SERVICE_NAME);
        return getClient(url, WS_ADDRESSING_ACTION, PORT_TYPE).invokePort(PORT_TYPE, serviceName, request);
    }

    private static <T> CONNECTClient<T> getClient(String serviceUrl, String wsAddressingAction, Class<T> portTypeClass)
        throws ExchangeManagerException {
        if (null == client) {
            client = HelperUtil.getClientUnsecure(serviceUrl, wsAddressingAction, portTypeClass);
        }
        return (CONNECTClient<T>) client;
    }

    private static void logDebug(String msg, Object... objects) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("{}: {}", msg, StringUtils.join(objects, ", "));
        }
    }
}
