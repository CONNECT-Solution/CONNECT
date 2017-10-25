/*
 * Copyright (c) 2009-2017, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.admingui.services;

import gov.hhs.fha.nhinc.admingui.services.exception.LoadTestDataException;
import gov.hhs.fha.nhinc.docrepository.adapter.dao.DocumentDao;
import gov.hhs.fha.nhinc.docrepository.adapter.dao.EventCodeDao;
import gov.hhs.fha.nhinc.docrepository.adapter.model.Document;
import gov.hhs.fha.nhinc.docrepository.adapter.model.EventCode;
import gov.hhs.fha.nhinc.patientdb.dao.AddressDAO;
import gov.hhs.fha.nhinc.patientdb.dao.IdentifierDAO;
import gov.hhs.fha.nhinc.patientdb.dao.PatientDAO;
import gov.hhs.fha.nhinc.patientdb.dao.PersonnameDAO;
import gov.hhs.fha.nhinc.patientdb.dao.PhonenumberDAO;
import gov.hhs.fha.nhinc.patientdb.model.Address;
import gov.hhs.fha.nhinc.patientdb.model.Identifier;
import gov.hhs.fha.nhinc.patientdb.model.Patient;
import gov.hhs.fha.nhinc.patientdb.model.Personname;
import gov.hhs.fha.nhinc.patientdb.model.Phonenumber;
import java.text.MessageFormat;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Tran Tang
 */

public class LoadTestDataDBServiceImpl implements LoadTestDataService {
    private static final Logger LOG = LoggerFactory.getLogger(LoadTestDataDBServiceImpl.class);
    private PatientDAO patientDAO = PatientDAO.getPatientDAOInstance();
    private PersonnameDAO personnameDAO = PersonnameDAO.getPersonnameDAOInstance();
    private IdentifierDAO identifierDAO = IdentifierDAO.getIdentifierDAOInstance();
    private AddressDAO addressDAO = AddressDAO.getAddressDAOInstance();
    private PhonenumberDAO phonenumberDAO = PhonenumberDAO.getPhonenumberDAOInstance();

    private DocumentDao documentDAO = new DocumentDao();
    private EventCodeDao eventCodeDAO = new EventCodeDao();

    private List<Patient> cachePatients;

    @Override
    public List<Patient> getAllPatients() {
        cachePatients = patientDAO.getAll();
        return cachePatients;
    }

    @Override
    public boolean deletePatient(Patient patient) {
        return patientDAO.deleteTransaction(patient);
    }

    @Override
    public Patient getPatientBy(Long id) {
        return patientDAO.readTransaction(id, false);
    }

    @Override
    public boolean savePatient(Patient patient) throws LoadTestDataException {
        boolean actionResult;
        if (CollectionUtils.isNotEmpty(patient.getPersonnames())) {
            actionResult = patientDAO.saveTransaction(patient);

            if (!actionResult) {
                logDaoError("Patient basic-info");
            }
        }
        else {
            LOG.info("Patient basic-info is required");
            throw new LoadTestDataException("Patient basic-info is required when trying to save-patient.");
        }
        return actionResult;
    }

    @Override
    public List<Personname> getAllPersonnamesBy(Long patientId) {
        return personnameDAO.findRecords(patientId);
    }

    @Override
    public List<Address> getAllAddressesBy(Long patientId) {
        return addressDAO.findRecords(patientId);
    }

    @Override
    public List<Identifier> getAllIdentiersBy(Long patientId) {
        return identifierDAO.findRecords(patientId);
    }

    @Override
    public List<Phonenumber> getAllPhonenumbersBy(Long patientId) {
        return phonenumberDAO.findRecords(patientId);
    }

    @Override
    public boolean deletePersonname(Personname personname) {
        return personnameDAO.delete(personname);
    }

    @Override
    public boolean deleteAddress(Address address) {
        return addressDAO.delete(address);
    }

    @Override
    public boolean deleteIdentifier(Identifier identifier) {
        return identifierDAO.delete(identifier);
    }

    @Override
    public boolean deletePhonenumber(Phonenumber phonenumber) {
        return phonenumberDAO.delete(phonenumber);
    }

    @Override
    public Personname getPersonnameBy(Long personnameId) {
        return personnameDAO.read(personnameId);
    }

    @Override
    public Address getAddressBy(Long addressId) {
        return addressDAO.read(addressId);
    }

    @Override
    public Identifier getIdentifierBy(Long identifierId) {
        return identifierDAO.read(identifierId);
    }

    @Override
    public Phonenumber getPhonenumberBy(Long phonenumberId) {
        return phonenumberDAO.read(phonenumberId);
    }

    @Override
    public boolean savePersonname(Personname personname) throws LoadTestDataException {
        boolean actionResult = false;
        if (personname.getPatient() != null) {
            actionResult = personnameDAO.save(personname);
            if (!actionResult) {
                logDaoError("Personname");
            }
        }
        return actionResult;
    }

    @Override
    public boolean saveAddress(Address address) throws LoadTestDataException {
        boolean actionResult = false;
        if (address.getPatient() != null) {
            actionResult = addressDAO.save(address);
            if (!actionResult) {
                logDaoError("Address");
            }
        }
        return actionResult;
    }

    @Override
    public boolean saveIdentifier(Identifier identifier) throws LoadTestDataException {
        boolean actionResult = false;
        if (identifier.getPatient() != null) {
            actionResult = identifierDAO.save(identifier);
            if (!actionResult) {
                logDaoError("Identifier");
            }
        }
        return actionResult;
    }

    @Override
    public boolean savePhonenumber(Phonenumber phonenumber) throws LoadTestDataException {
        boolean actionResult = false;
        if (phonenumber.getPatient() != null) {
            actionResult = phonenumberDAO.save(phonenumber);
            if (!actionResult) {
                logDaoError("Phonenumber");
            }
        }
        return actionResult;
    }

    // DocumentRepository
    @Override
    public List<Document> getAllDocuments() {
        return documentDAO.findAll();
    }

    @Override
    public List<EventCode> getAllEventCodesBy(Long documentId) {
        return eventCodeDAO.findAllBy(documentId);
    }

    @Override
    public boolean deleteDocument(Document document) {
        return documentDAO.delete(document);
    }

    @Override
    public boolean deleteEventCode(EventCode eventCode) {
        return eventCodeDAO.delete(eventCode);
    }

    @Override
    public Document getDocumentBy(Long documentId) {
        return documentDAO.findById(documentId);
    }

    @Override
    public EventCode getEventCodeBy(Long eventCodeId) {
        return eventCodeDAO.findById(eventCodeId);
    }

    @Override
    public boolean saveDocument(Document document) throws LoadTestDataException {
        return documentDAO.save(document);
    }

    @Override
    public boolean saveEventCode(EventCode eventCode) throws LoadTestDataException {
        boolean actionResult = false;
        if (eventCode.getDocument() != null) {
            actionResult = eventCodeDAO.save(eventCode);
            if (!actionResult) {
                logDaoError("EventCode");
            }
        }
        return actionResult;
    }

    @Override
    public Patient getPatientBy(String identifierId, String identifierOrg){
        return patientDAO.readTransaction(identifierId, identifierOrg);
    }

    @Override
    public List<Patient> getCachePatients() {
        if (null == cachePatients) {
            getAllPatients();
        }
        return cachePatients;
    }

    private static void logDaoError(String logOf) throws LoadTestDataException {
        LOG.error("DAO fail to save {}.", logOf);
        throw new LoadTestDataException(MessageFormat.format("{0} fail to save to database.", logOf));
    }
}
