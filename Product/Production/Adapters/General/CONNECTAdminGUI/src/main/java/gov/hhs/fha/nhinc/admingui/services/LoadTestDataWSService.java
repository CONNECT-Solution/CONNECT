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
package gov.hhs.fha.nhinc.admingui.services;

import gov.hhs.fha.nhinc.common.loadtestdatamanagement.AddressType;
import gov.hhs.fha.nhinc.common.loadtestdatamanagement.DocumentMetadataType;
import gov.hhs.fha.nhinc.common.loadtestdatamanagement.EventCodeType;
import gov.hhs.fha.nhinc.common.loadtestdatamanagement.IdentifierType;
import gov.hhs.fha.nhinc.common.loadtestdatamanagement.PatientType;
import gov.hhs.fha.nhinc.common.loadtestdatamanagement.PersonNameType;
import gov.hhs.fha.nhinc.common.loadtestdatamanagement.PhoneNumberType;
import gov.hhs.fha.nhinc.loadtestdata.LoadTestDataException;
import java.util.List;

/**
 * @author Tran Tang
 *
 *         used by the admingui
 */
public interface LoadTestDataWSService {

    public List<PatientType> getAllPatients();

    public List<PersonNameType> getAllPersonNamesBy(Long patientId);

    public List<AddressType> getAllAddressesBy(Long patientId);

    public List<IdentifierType> getAllIdentiersBy(Long patientId);

    public List<PhoneNumberType> getAllPhoneNumbersBy(Long patientId);

    public boolean deletePatient(PatientType patient);

    public boolean deletePersonName(PersonNameType personname);

    public boolean deleteAddress(AddressType address);

    public boolean deleteIdentifier(IdentifierType identifier);

    public boolean deletePhoneNumber(PhoneNumberType phonenumber);

    public PatientType getPatientBy(Long patientId);

    public PatientType duplicatePatient(Long patientId);

    public PersonNameType getPersonNameBy(Long personnameId);

    public AddressType getAddressBy(Long addressId);

    public IdentifierType getIdentifierBy(Long identifierId);

    public PhoneNumberType getPhoneNumberBy(Long phonenumberId);

    public boolean savePatient(PatientType patient) throws LoadTestDataException;

    public boolean savePersonName(PersonNameType personname) throws LoadTestDataException;

    public boolean saveAddress(AddressType address) throws LoadTestDataException;

    public boolean saveIdentifier(IdentifierType identifier) throws LoadTestDataException;

    public boolean savePhoneNumber(PhoneNumberType phonenumber) throws LoadTestDataException;

    public List<DocumentMetadataType> getAllDocuments();

    public List<EventCodeType> getAllEventCodesBy(Long documentId);

    public DocumentMetadataType duplicateDocument(Long documentId);

    public boolean deleteDocument(DocumentMetadataType document);

    public boolean deleteEventCode(EventCodeType eventCode);

    public DocumentMetadataType getDocumentBy(Long documentId);

    public EventCodeType getEventCodeBy(Long eventCodeId);

    public boolean saveDocument(DocumentMetadataType document) throws LoadTestDataException;

    public boolean saveEventCode(EventCodeType eventCode) throws LoadTestDataException;

    public PatientType getPatientBy(String identifierId, String identifierOrg);

}
