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
import gov.hhs.fha.nhinc.admingui.services.exception.UnsupportedMethodException;
import gov.hhs.fha.nhinc.docrepository.adapter.model.Document;
import gov.hhs.fha.nhinc.docrepository.adapter.model.EventCode;
import gov.hhs.fha.nhinc.patientdb.model.Address;
import gov.hhs.fha.nhinc.patientdb.model.Identifier;
import gov.hhs.fha.nhinc.patientdb.model.Patient;
import gov.hhs.fha.nhinc.patientdb.model.Personname;
import gov.hhs.fha.nhinc.patientdb.model.Phonenumber;
import java.util.List;

/**
 * @author Tran Tang
 *
 */
public interface LoadTestDataService {
    public List<Patient> getAllPatients();

    public List<Personname> getAllPersonnamesBy(Long patientId);

    public List<Address> getAllAddressesBy(Long patientId);

    public List<Identifier> getAllIdentiersBy(Long patientId);

    public List<Phonenumber> getAllPhonenumbersBy(Long patientId);

    public boolean deletePatient(Patient patient);

    public boolean deletePersonname(Personname personname);

    public boolean deleteAddress(Address address);

    public boolean deleteIdentifier(Identifier identifier);

    public boolean deletePhonenumber(Phonenumber phonenumber);

    public Patient getPatientBy(Long patientId);

    public Patient duplicatePatient(Long patientId);

    public Personname getPersonnameBy(Long personnameId);

    public Address getAddressBy(Long addressId);

    public Identifier getIdentifierBy(Long identifierId);

    public Phonenumber getPhonenumberBy(Long phonenumberId);

    public boolean savePatient(Patient patient) throws LoadTestDataException;

    public boolean savePersonname(Personname personname) throws LoadTestDataException;

    public boolean saveAddress(Address address) throws LoadTestDataException;

    public boolean saveIdentifier(Identifier identifier) throws LoadTestDataException;

    public boolean savePhonenumber(Phonenumber phonenumber) throws LoadTestDataException;

    public List<Document> getAllDocuments();

    public List<EventCode> getAllEventCodesBy(Long documentId);

    public Document duplicateDocument(Long documentId);

    public boolean deleteDocument(Document document);

    public boolean deleteEventCode(EventCode eventCode);

    public Document getDocumentBy(Long documentId);

    public EventCode getEventCodeBy(Long eventCodeId);

    public boolean saveDocument(Document document) throws LoadTestDataException;

    public boolean saveEventCode(EventCode eventCode) throws LoadTestDataException;

    public Patient getPatientBy(String identifierId, String identifierOrg);

    public List<Patient> getCachePatients();

}
