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
import gov.hhs.fha.nhinc.docrepository.adapter.model.Document;
import gov.hhs.fha.nhinc.docrepository.adapter.model.EventCode;
import gov.hhs.fha.nhinc.patientdb.model.Address;
import gov.hhs.fha.nhinc.patientdb.model.Identifier;
import gov.hhs.fha.nhinc.patientdb.model.Patient;
import gov.hhs.fha.nhinc.patientdb.model.Personname;
import gov.hhs.fha.nhinc.patientdb.model.Phonenumber;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author PVenkatakrishnan
 */

public class LoadTestDataFileServiceImpl implements LoadTestDataService {
    private static final Logger LOG = LoggerFactory.getLogger(LoadTestDataFileServiceImpl.class);
    private static final String LOG_UNSUPPORT_METHOD = "error: this method is currently unsupported";

    @Override
    public List<Patient> getAllPatients() {
        LOG.info(LOG_UNSUPPORT_METHOD);
        return new ArrayList<>();
    }

    @Override
    public boolean deletePatient(Patient patient) {
        LOG.info(LOG_UNSUPPORT_METHOD);
        return false;
    }

    @Override
    public Patient getPatientBy(Long id) {
        LOG.info(LOG_UNSUPPORT_METHOD);
        return new Patient();
    }

    @Override
    public boolean savePatient(Patient patient) throws LoadTestDataException {
        LOG.info(LOG_UNSUPPORT_METHOD);
        return false;
    }

    @Override
    public List<Personname> getAllPersonnamesBy(Long patientId) {
        LOG.info(LOG_UNSUPPORT_METHOD);
        return new ArrayList<>();
    }

    @Override
    public List<Address> getAllAddressesBy(Long patientId) {
        LOG.info(LOG_UNSUPPORT_METHOD);
        return new ArrayList<>();
    }

    @Override
    public List<Identifier> getAllIdentiersBy(Long patientId) {
        LOG.info(LOG_UNSUPPORT_METHOD);
        return new ArrayList<>();
    }

    @Override
    public List<Phonenumber> getAllPhonenumbersBy(Long patientId) {
        LOG.info(LOG_UNSUPPORT_METHOD);
        return new ArrayList<>();
    }

    @Override
    public boolean deletePersonname(Personname personname) {
        LOG.info(LOG_UNSUPPORT_METHOD);
        return false;
    }

    @Override
    public boolean deleteAddress(Address address) {
        LOG.info(LOG_UNSUPPORT_METHOD);
        return false;
    }

    @Override
    public boolean deleteIdentifier(Identifier identifier) {
        LOG.info(LOG_UNSUPPORT_METHOD);
        return false;
    }

    @Override
    public boolean deletePhonenumber(Phonenumber phonenumber) {
        LOG.info(LOG_UNSUPPORT_METHOD);
        return false;
    }

    @Override
    public Personname getPersonnameBy(Long personnameId) {
        LOG.info(LOG_UNSUPPORT_METHOD);
        return new Personname();
    }

    @Override
    public Address getAddressBy(Long addressId) {
        LOG.info(LOG_UNSUPPORT_METHOD);
        return new Address();
    }

    @Override
    public Identifier getIdentifierBy(Long identifierId) {
        LOG.info(LOG_UNSUPPORT_METHOD);
        return new Identifier();
    }

    @Override
    public Phonenumber getPhonenumberBy(Long phonenumberId) {
        LOG.info(LOG_UNSUPPORT_METHOD);
        return new Phonenumber();
    }

    @Override
    public boolean savePersonname(Personname personname) throws LoadTestDataException {
        LOG.info(LOG_UNSUPPORT_METHOD);
        return false;
    }

    @Override
    public boolean saveAddress(Address address) throws LoadTestDataException {
        LOG.info(LOG_UNSUPPORT_METHOD);
        return false;
    }

    @Override
    public boolean saveIdentifier(Identifier identifier) throws LoadTestDataException {
        LOG.info(LOG_UNSUPPORT_METHOD);
        return false;
    }

    @Override
    public boolean savePhonenumber(Phonenumber phonenumber) throws LoadTestDataException {
        LOG.info(LOG_UNSUPPORT_METHOD);
        return false;
    }

    @Override
    public List<Document> getAllDocuments() {
        LOG.info(LOG_UNSUPPORT_METHOD);
        return new ArrayList<>();
    }

    @Override
    public List<EventCode> getAllEventCodesBy(Long documentId) {
        LOG.info(LOG_UNSUPPORT_METHOD);
        return new ArrayList<>();
    }

    @Override
    public boolean deleteDocument(Document document) {
        LOG.info(LOG_UNSUPPORT_METHOD);
        return false;
    }

    @Override
    public Document duplicateDocument(Long id) {
        LOG.info(LOG_UNSUPPORT_METHOD);
        return new Document();
    }

    @Override
    public boolean deleteEventCode(EventCode eventCode) {
        LOG.info(LOG_UNSUPPORT_METHOD);
        return false;
    }

    @Override
    public Document getDocumentBy(Long documentId) {
        LOG.info(LOG_UNSUPPORT_METHOD);
        return new Document();
    }

    @Override
    public EventCode getEventCodeBy(Long eventCodeId) {
        LOG.info(LOG_UNSUPPORT_METHOD);
        return new EventCode();
    }

    @Override
    public boolean saveDocument(Document document) throws LoadTestDataException {
        LOG.info(LOG_UNSUPPORT_METHOD);
        return false;
    }

    @Override
    public boolean saveEventCode(EventCode eventCode) throws LoadTestDataException {
        LOG.info(LOG_UNSUPPORT_METHOD);
        return false;
    }

    @Override
    public Patient getPatientBy(String identifierId, String identifierOrg) {
        LOG.info(LOG_UNSUPPORT_METHOD);
        return new Patient();
    }

    @Override
    public Patient duplicatePatient(Long id) {
        LOG.info(LOG_UNSUPPORT_METHOD);
        return new Patient();
    }

    @Override
    public List<Patient> getCachePatients() {
        return getAllPatients();
    }

}
