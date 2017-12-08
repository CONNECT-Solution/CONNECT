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
        throw new UnsupportedOperationException("Not supported. ");
    }

    @Override
    public boolean deletePatient(Patient patient) {
        throw new UnsupportedOperationException("Not supported. ");
    }

    @Override
    public Patient getPatientBy(Long id) {
        throw new UnsupportedOperationException("Not supported. ");
    }

    @Override
    public boolean savePatient(Patient patient) throws LoadTestDataException {
        throw new UnsupportedOperationException("Not supported. ");
    }

    @Override
    public List<Personname> getAllPersonnamesBy(Long patientId) {
        throw new UnsupportedOperationException("Not supported. ");
    }

    @Override
    public List<Address> getAllAddressesBy(Long patientId) {
        throw new UnsupportedOperationException("Not supported. ");
    }

    @Override
    public List<Identifier> getAllIdentiersBy(Long patientId) {
        throw new UnsupportedOperationException("Not supported. ");
    }

    @Override
    public List<Phonenumber> getAllPhonenumbersBy(Long patientId) {
        throw new UnsupportedOperationException("Not supported. ");
    }

    @Override
    public boolean deletePersonname(Personname personname) {
        throw new UnsupportedOperationException("Not supported. ");
    }

    @Override
    public boolean deleteAddress(Address address) {
        throw new UnsupportedOperationException("Not supported. ");
    }

    @Override
    public boolean deleteIdentifier(Identifier identifier) {
        throw new UnsupportedOperationException("Not supported. ");
    }

    @Override
    public boolean deletePhonenumber(Phonenumber phonenumber) {
        throw new UnsupportedOperationException("Not supported. ");
    }

    @Override
    public Personname getPersonnameBy(Long personnameId) {
        throw new UnsupportedOperationException("Not supported. ");
    }

    @Override
    public Address getAddressBy(Long addressId) {
        throw new UnsupportedOperationException("Not supported. ");
    }

    @Override
    public Identifier getIdentifierBy(Long identifierId) {
        throw new UnsupportedOperationException("Not supported. ");
    }

    @Override
    public Phonenumber getPhonenumberBy(Long phonenumberId) {
        throw new UnsupportedOperationException("Not supported. ");
    }

    @Override
    public boolean savePersonname(Personname personname) throws LoadTestDataException {
        throw new UnsupportedOperationException("Not supported. ");
    }

    @Override
    public boolean saveAddress(Address address) throws LoadTestDataException {
        throw new UnsupportedOperationException("Not supported. ");
    }

    @Override
    public boolean saveIdentifier(Identifier identifier) throws LoadTestDataException {
        throw new UnsupportedOperationException("Not supported. ");
    }

    @Override
    public boolean savePhonenumber(Phonenumber phonenumber) throws LoadTestDataException {
        throw new UnsupportedOperationException("Not supported. ");
    }

    @Override
    public List<Document> getAllDocuments() {
        throw new UnsupportedOperationException("Not supported. ");
    }

    @Override
    public List<EventCode> getAllEventCodesBy(Long documentId) {
        throw new UnsupportedOperationException("Not supported. ");
    }

    @Override
    public boolean deleteDocument(Document document) {
        throw new UnsupportedOperationException("Not supported. ");
    }

    @Override
    public Document duplicateDocument(Long id) {
        throw new UnsupportedOperationException("Not supported. ");
    }

    @Override
    public boolean deleteEventCode(EventCode eventCode) {
        throw new UnsupportedOperationException("Not supported. ");
    }

    @Override
    public Document getDocumentBy(Long documentId) {
        throw new UnsupportedOperationException("Not supported. ");
    }

    @Override
    public EventCode getEventCodeBy(Long eventCodeId) {
        throw new UnsupportedOperationException("Not supported. ");
    }

    @Override
    public boolean saveDocument(Document document) throws LoadTestDataException {
        throw new UnsupportedOperationException("Not supported. ");
    }

    @Override
    public boolean saveEventCode(EventCode eventCode) throws LoadTestDataException {
        throw new UnsupportedOperationException("Not supported. ");
    }

    @Override
    public Patient getPatientBy(String identifierId, String identifierOrg) {
        throw new UnsupportedOperationException("Not supported. ");
    }

    @Override
    public Patient duplicatePatient(Long id) {
        throw new UnsupportedOperationException("Not supported. ");
    }

    @Override
    public List<Patient> getCachePatients() {
        throw new UnsupportedOperationException("Not supported. ");
    }

}
