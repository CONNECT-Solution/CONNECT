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
package gov.hhs.fha.nhinc.mpi.adapter.component.hl7parsers;

import gov.hhs.fha.nhinc.patientdb.model.Address;
import gov.hhs.fha.nhinc.patientdb.model.Identifier;
import gov.hhs.fha.nhinc.patientdb.model.Patient;
import gov.hhs.fha.nhinc.patientdb.model.Personname;
import gov.hhs.fha.nhinc.patientdb.model.Phonenumber;
import gov.hhs.fha.nhinc.util.format.UTCDateUtil;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.bind.JAXBElement;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hl7.v3.ADExplicit;
import org.hl7.v3.AdxpExplicitCity;
import org.hl7.v3.AdxpExplicitPostalCode;
import org.hl7.v3.AdxpExplicitState;
import org.hl7.v3.AdxpExplicitStreetAddressLine;
import org.hl7.v3.EnExplicitFamily;
import org.hl7.v3.EnExplicitGiven;
import org.hl7.v3.EnExplicitPrefix;
import org.hl7.v3.EnExplicitSuffix;
import org.hl7.v3.II;
import org.hl7.v3.IVLTSExplicit;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectBirthTime;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectId;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectName;
import org.hl7.v3.PRPAMT201306UV02ParameterList;
import org.hl7.v3.PRPAMT201306UV02PatientAddress;
import org.hl7.v3.PRPAMT201306UV02PatientTelecom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author richard.ettema
 */
public class HL7DbParser201305 {

    private static final Logger LOG = LoggerFactory.getLogger(HL7DbParser201305.class);
    private static HL7Parser201305Utils hl7ParserUtils = HL7Parser201305Utils.getInstance();
    /**
     * Social Security Number Root Identifier.
     */
    public static final String SSN_ROOT_IDENTIFIER = "2.16.840.1.113883.4.1";

    private HL7DbParser201305() {

    }

    /**
     * Method to extract Gender Code from a PRPAMT201306UV02ParameterList.
     *
     * @param params the Paramater list from which to extract a Gender Code
     * @return The Gender Code is returned
     */
    public static String extractGender(PRPAMT201306UV02ParameterList params) {
        LOG.trace("Entering HL7DbParser201305.ExtractGender method...");
        return hl7ParserUtils.extractGender(params);
    }

    /**
     * Method to extract birthdate from a PRPAMT201306UV02ParameterList.
     *
     * @param params the parameterList from which to extract the birthdate
     * @return a Timestamp object containing the birthdate.
     */
    public static Timestamp extractBirthdate(PRPAMT201306UV02ParameterList params) {
        LOG.trace("Entering HL7DbParser201305.ExtractBirthdate method...");

        Timestamp birthDate = null;
        final int dateOnlyLength = 8;

        try {
            // Extract the birth time from the query parameters - Assume only one was specified
            if (CollectionUtils.isNotEmpty(params.getLivingSubjectBirthTime())
                && params.getLivingSubjectBirthTime().get(0) != null) {
                PRPAMT201306UV02LivingSubjectBirthTime birthTime = params.getLivingSubjectBirthTime().get(0);

                if (CollectionUtils.isNotEmpty(birthTime.getValue())
                    && birthTime.getValue().get(0) != null) {
                    birthDate = formatBirthDate(birthDate, dateOnlyLength, birthTime);
                } else {
                    LOG.info("message does not contain a birthtime");
                }
            } else {
                LOG.info("message does not contain a birthtime");
            }
        } catch (Exception e) {
            LOG.error("Exception parsing birth date: {} ", e);
            return null;
        }

        LOG.trace("Exiting HL7DbParser201305.ExtractBirthdate method...");
        return birthDate;
    }

    /**
     * @param birthDate
     * @param dateOnlyLength
     * @param birthTime
     * @return
     */
    private static Timestamp formatBirthDate(Timestamp birthDate, final int dateOnlyLength,
        PRPAMT201306UV02LivingSubjectBirthTime birthTime) {
        Timestamp parseBirthDate = birthDate;
        IVLTSExplicit birthday = birthTime.getValue().get(0);
        LOG.info("Found birthTime in query parameters: {} ", birthday.getValue());
        UTCDateUtil utcDateUtil = new UTCDateUtil();
        // Check date string length
        if (birthday.getValue().length() == dateOnlyLength) {
            parseBirthDate = new Timestamp(utcDateUtil.parseDate(birthday.getValue(),
                UTCDateUtil.DATE_ONLY_FORMAT, null).getTime());
        } else if (birthday.getValue().length() > dateOnlyLength) {
            parseBirthDate = new Timestamp(
                utcDateUtil.parseDate(birthday.getValue(),
                    UTCDateUtil.DATE_FORMAT_UTC, null).getTime());
        } else {
            LOG.info("message does not contain a valid formatted birthtime");
        }
        return parseBirthDate;
    }

    /**
     * Method to extract a list of Person names from a PRPAMT201306UV02ParameterList.
     *
     * @param params the ParamaterList from which to extract names.
     * @return a list of names from the ParamaterList.
     */
    public static List<Personname> extractPersonnames(PRPAMT201306UV02ParameterList params) {
        LOG.trace("Entering HL7DbParser201305.ExtractPersonnames method...");

        List<Personname> personnames = new ArrayList<>();

        // Extract the person names from the query parameters
        if (CollectionUtils.isNotEmpty(params.getLivingSubjectName())
            && params.getLivingSubjectName().get(0) != null) {

            for (PRPAMT201306UV02LivingSubjectName name : params.getLivingSubjectName()) {
                if (CollectionUtils.isNotEmpty(name.getValue()) && name.getValue().get(0) != null) {
                    List<Serializable> choice = name.getValue().get(0).getContent();

                    LOG.info("choice.size() : {} ", choice.size());

                    Iterator<Serializable> iterSerialObjects = choice.iterator();

                    String nameString = "";
                    EnExplicitFamily lastname = null;
                    EnExplicitGiven firstname = null;
                    EnExplicitGiven middlename = null;
                    EnExplicitPrefix prefix = null;
                    EnExplicitSuffix suffix = null;

                    while (iterSerialObjects.hasNext()) {
                        LOG.info("in iterSerialObjects.hasNext() loop");

                        Serializable contentItem = iterSerialObjects.next();

                        if (contentItem instanceof String) {
                            nameString = hl7ParserUtils.formatNameString((String) contentItem, nameString);
                        } else if (contentItem instanceof JAXBElement) {
                            LOG.info("contentItem is JAXBElement");

                            JAXBElement oJAXBElement = (JAXBElement) contentItem;

                            if (oJAXBElement.getValue() instanceof EnExplicitFamily) {
                                lastname = (EnExplicitFamily) oJAXBElement.getValue();
                                LOG.info("found lastname element; content : {} ", lastname.getContent());
                            } else if (oJAXBElement.getValue() instanceof EnExplicitGiven) {
                                if (firstname == null) {
                                    firstname = (EnExplicitGiven) oJAXBElement.getValue();
                                    LOG.info("found firstname element; content: {} ", firstname.getContent());
                                } else if (middlename == null) {
                                    middlename = (EnExplicitGiven) oJAXBElement.getValue();
                                    LOG.info("found middlename element; content : {} ", middlename.getContent());
                                } else {
                                    // ignore all other given values
                                }
                            } else if (oJAXBElement.getValue() instanceof EnExplicitPrefix) {
                                prefix = (EnExplicitPrefix) oJAXBElement.getValue();
                                LOG.info("found prefix element; content : {} ", prefix.getContent());
                            } else if (oJAXBElement.getValue() instanceof EnExplicitSuffix) {
                                suffix = (EnExplicitSuffix) oJAXBElement.getValue();
                                LOG.info("found suffix element; content : {} ", suffix.getContent());
                            } else {
                                LOG.info("other name part : {} ", oJAXBElement.getValue());
                            }
                        } else {
                            LOG.info("contentItem is other");
                        }
                    }

                    // If text string in patient name, then set in name
                    // else set in element.
                    Personname personNameDBModel = formatPatientName(nameString, lastname, firstname, middlename,
                        prefix, suffix);

                    personnames.add(personNameDBModel);
                } else {
                    LOG.info("message does not contain a subject name");
                }
            }
        } else {
            LOG.info("message does not contain a subject name");
        }

        LOG.trace("Exiting HL7DbParser201305.ExtractPersonnames method...");
        return personnames;
    }

    /**
     * @param nameString
     * @param lastname
     * @param firstname
     * @param middlename
     * @param prefix
     * @param suffix
     * @return
     */
    private static Personname formatPatientName(String nameString, EnExplicitFamily lastname, EnExplicitGiven firstname,
        EnExplicitGiven middlename, EnExplicitPrefix prefix, EnExplicitSuffix suffix) {
        boolean namefound;
        Personname personNameDBModel = new Personname();
        namefound = checkNameContents(lastname, firstname, middlename, prefix, suffix, personNameDBModel);

        if (!namefound && StringUtils.isNotEmpty(nameString)) {
            LOG.info("setting name by nameString : {} ", nameString);
            personNameDBModel.setLastName(nameString);

        }
        return personNameDBModel;
    }

    /**
     * @param lastname
     * @param firstname
     * @param middlename
     * @param prefix
     * @param suffix
     * @param namefound
     * @param personNameDBModel
     * @return
     */
    private static boolean checkNameContents(EnExplicitFamily lastname, EnExplicitGiven firstname,
        EnExplicitGiven middlename, EnExplicitPrefix prefix, EnExplicitSuffix suffix,
        Personname personNameDBModel) {
        boolean namefound;
        namefound = checkFirstMiddleLastNameContents(lastname, firstname, middlename, personNameDBModel)
            || checkSuffixNameContents(prefix, suffix, personNameDBModel);
        return namefound;
    }

    /**
     * @param lastname
     * @param firstname
     * @param middlename
     * @param personNameDBModel
     */
    private static boolean checkFirstMiddleLastNameContents(EnExplicitFamily lastname, EnExplicitGiven firstname,
        EnExplicitGiven middlename, Personname personNameDBModel) {
        boolean namefound = false;
        if (lastname != null && lastname.getContent() != null) {
            personNameDBModel.setLastName(lastname.getContent());
            LOG.info("FamilyName : {} ", personNameDBModel.getLastName());
            namefound = true;
        }

        if (firstname != null && firstname.getContent() != null) {
            personNameDBModel.setFirstName(firstname.getContent());
            LOG.info("GivenName(first) : {} ", personNameDBModel.getFirstName());
            namefound = true;
        }

        if (middlename != null && middlename.getContent() != null) {
            personNameDBModel.setMiddleName(middlename.getContent());
            LOG.info("GivenName(middle) : {} ", personNameDBModel.getMiddleName());
            namefound = true;
        }
        return namefound;
    }

    /**
     * @param prefix
     * @param suffix
     * @param personNameDBModel
     * @param namefound
     * @return
     */
    private static boolean checkSuffixNameContents(EnExplicitPrefix prefix, EnExplicitSuffix suffix,
        Personname personNameDBModel) {
        boolean namefound = false;
        if (prefix != null && prefix.getContent() != null) {
            personNameDBModel.setPrefix(prefix.getContent());
            LOG.info("Prefix : {} ", personNameDBModel.getPrefix());
            namefound = true;
        }

        if (suffix != null && suffix.getContent() != null) {
            personNameDBModel.setSuffix(suffix.getContent());
            LOG.info("Suffix : {} ", personNameDBModel.getSuffix());
            namefound = true;
        }
        return namefound;
    }

    /**
     * Method to extract a list of Person Identifiers from a PRPAMT201306UV02ParameterList.
     *
     * @param params the PRPAMT201306UV02ParameterList from which to extract a list of Person Identifiers
     * @return a List of Identifiers.
     */
    public static List<Identifier> extractPersonIdentifiers(PRPAMT201306UV02ParameterList params) {
        LOG.trace("Entering HL7DbParser201305.ExtractPersonIdentifiers method...");

        List<Identifier> ids = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(params.getLivingSubjectId())
            && params.getLivingSubjectId().get(0) != null) {

            for (PRPAMT201306UV02LivingSubjectId livingSubjectId : params.getLivingSubjectId()) {
                if (CollectionUtils.isNotEmpty(livingSubjectId.getValue())
                    && livingSubjectId.getValue().get(0) != null) {
                    II subjectId = livingSubjectId.getValue().get(0);

                    if (StringUtils.isNotEmpty(subjectId.getExtension())
                        && StringUtils.isNotEmpty(subjectId.getRoot())) {
                        // Ignore SSN identifiers
                        if (!subjectId.getRoot().equals(SSN_ROOT_IDENTIFIER)) {
                            ids.add(getPatientDbIdentifier(subjectId));
                        }
                    } else {
                        LOG.info("message does not contain an id");
                    }
                } else {
                    LOG.info("message does not contain an id");
                }
            }
        } else {
            LOG.info("message does not contain an id");
        }

        LOG.trace("Exiting HL7DbParser201305.ExtractPersonIdentifiers method...");
        return ids;
    }

    /**
     * @param subjectId
     * @return
     */
    private static Identifier getPatientDbIdentifier(II subjectId) {
        Identifier id = new Identifier();
        id.setId(subjectId.getExtension());
        id.setOrganizationId(subjectId.getRoot());
        LOG.info("Created id from patient identifier [organization : {}][id : {}] ", id.getOrganizationId(),
            id.getId());
        return id;
    }

    /**
     * Method to extract a Social Security Number from a PRPAMT201306UV02ParameterList.
     *
     * @param params the PRPAMT201306UV02ParameterList from which to extract a SSN
     * @return a Social Security Number from the PRPAMT201306UV02ParameterList
     */
    public static String extractSSNIdentifier(PRPAMT201306UV02ParameterList params) {
        LOG.trace("Entering HL7DbParser201305.ExtractSSNIdentifier method...");

        String ssn = null;

        if (CollectionUtils.isNotEmpty(params.getLivingSubjectId())
            && params.getLivingSubjectId().get(0) != null) {

            for (PRPAMT201306UV02LivingSubjectId livingSubjectId : params.getLivingSubjectId()) {
                if (CollectionUtils.isNotEmpty(livingSubjectId.getValue())
                    && livingSubjectId.getValue().get(0) != null) {
                    II subjectId = livingSubjectId.getValue().get(0);

                    if (StringUtils.isNotEmpty(subjectId.getExtension())
                        && StringUtils.isNotEmpty(subjectId.getRoot())) {
                        // Look for first SSN identifier
                        if (subjectId.getRoot().equals(SSN_ROOT_IDENTIFIER)) {
                            Identifier ssnId = new Identifier();
                            ssnId.setId(subjectId.getExtension());
                            ssnId.setOrganizationId(subjectId.getRoot());
                            LOG.info("Created id from ssn identifier [organization : {}][id : {}] ",
                                ssnId.getOrganizationId(), ssnId.getId());
                            ssn = ssnId.getId();
                        }
                    } else {
                        LOG.info("message does not contain an id");
                    }
                } else {
                    LOG.info("message does not contain an id");
                }
            }
        } else {
            LOG.info("message does not contain an id");
        }

        LOG.trace("Exiting HL7DbParser201305.ExtractSSNIdentifier method...");
        return ssn;
    }

    /**
     * Method to extract a list of Addresses from a PRPAMT201306UV02ParameterList.
     *
     * @param params the PRPAMT201306UV02ParameterList from which to extract the Addresses
     * @return a list of Addresses from PRPAMT201306UV02ParameterList.
     */
    public static List<Address> extractPersonAddresses(PRPAMT201306UV02ParameterList params) {
        LOG.trace("Entering HL7DbParser201305.ExtractPersonAddress method...");

        List<Address> addresses = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(params.getPatientAddress())
            && params.getPatientAddress().get(0) != null) {

            for (PRPAMT201306UV02PatientAddress patientAddress : params.getPatientAddress()) {
                if (CollectionUtils.isNotEmpty(patientAddress.getValue())
                    && patientAddress.getValue().get(0) != null) {
                    ADExplicit adExplicit = patientAddress.getValue().get(0);

                    List<Serializable> choice = adExplicit.getContent();

                    LOG.info("choice.size() : {} ", choice.size());

                    Iterator<Serializable> iterSerialObjects = choice.iterator();

                    int addressLineCounter = 0;
                    AdxpExplicitStreetAddressLine addressLine1;
                    AdxpExplicitStreetAddressLine addressLine2;
                    AdxpExplicitCity city;
                    AdxpExplicitState state;
                    AdxpExplicitPostalCode postalCode;

                    Address address = null;

                    while (iterSerialObjects.hasNext()) {
                        LOG.info("in iterSerialObjects.hasNext() loop");

                        Serializable contentItem = iterSerialObjects.next();

                        if (contentItem instanceof JAXBElement) {
                            LOG.info("contentItem is JAXBElement");

                            JAXBElement oJAXBElement = (JAXBElement) contentItem;

                            if (oJAXBElement.getValue() instanceof AdxpExplicitStreetAddressLine) {
                                addressLineCounter++;
                                if (addressLineCounter == 1) {
                                    addressLine1 = (AdxpExplicitStreetAddressLine) oJAXBElement.getValue();
                                    LOG.info("found addressLine1 element; content : {} ", addressLine1.getContent());
                                    if (address == null) {
                                        address = new Address();
                                    }
                                    address.setStreet1(addressLine1.getContent());
                                }
                                if (addressLineCounter == 2) {
                                    addressLine2 = (AdxpExplicitStreetAddressLine) oJAXBElement.getValue();
                                    LOG.info("found addressLine2 element; content : {} ", addressLine2.getContent());
                                    if (address == null) {
                                        address = new Address();
                                    }
                                    address.setStreet2(addressLine2.getContent());
                                }
                            } else if (oJAXBElement.getValue() instanceof AdxpExplicitCity) {
                                city = (AdxpExplicitCity) oJAXBElement.getValue();
                                LOG.info("found city element; content : {} ", city.getContent());
                                if (address == null) {
                                    address = new Address();
                                }
                                address.setCity(city.getContent());
                            } else if (oJAXBElement.getValue() instanceof AdxpExplicitState) {
                                state = (AdxpExplicitState) oJAXBElement.getValue();
                                LOG.info("found state element; content : {} ", state.getContent());
                                if (address == null) {
                                    address = new Address();
                                }
                                address.setState(state.getContent());
                            } else if (oJAXBElement.getValue() instanceof AdxpExplicitPostalCode) {
                                postalCode = (AdxpExplicitPostalCode) oJAXBElement.getValue();
                                LOG.info("found postalCode element; content : {} ", postalCode.getContent());
                                if (address == null) {
                                    address = new Address();
                                }
                                address.setPostal(postalCode.getContent());
                            } else {
                                LOG.info("other address part : {} ", oJAXBElement.getValue());
                            }
                        } else {
                            LOG.info("contentItem is other");
                        }
                    }

                    if (address != null) {
                        addresses.add(address);
                    }
                }
            }
        }

        return addresses;
    }

    /**
     * Method to extract a list of Phone Numbers from a PRPAMT201306UV02ParameterList.
     *
     * @param params the PRPAMT201306UV02ParameterList from which to extract Phone Numbers.
     * @return a List of Phone Numbers from the PRPAMT201306UV02ParameterList
     */
    public static List<Phonenumber> extractTelecoms(PRPAMT201306UV02ParameterList params) {
        LOG.trace("Entering HL7DbParser201305.ExtractTelecoms method...");

        List<Phonenumber> phonenumbers = new ArrayList<>();

        Phonenumber phonenumber;

        // Extract the telecom (phone number) from the query parameters - Assume only one was specified
        if (CollectionUtils.isNotEmpty(params.getPatientTelecom())
            && params.getPatientTelecom().get(0) != null) {
            for (PRPAMT201306UV02PatientTelecom patientTelecom : params.getPatientTelecom()) {
                String telecom = hl7ParserUtils.extractTelecom(patientTelecom);
                if (StringUtils.isNotEmpty(telecom)) {
                    phonenumber = new Phonenumber();
                    phonenumber.setValue(telecom);
                    phonenumbers.add(phonenumber);
                }
            }
        } else {
            LOG.info("message does not contain a patientTelecom");
        }

        LOG.trace("Exiting HL7DbParser201305.ExtractTelecoms method...");
        return phonenumbers;
    }

    /**
     * Method to extract a PRPAMT201306UV02ParameterList object from a PRPAIN201305UV02 message.
     *
     * @param message the PRPAIN201305UV02 message from which to extract the PRPAMT201306UV02ParameterList
     * @return PRPAMT201306UV02ParameterList
     */
    public static PRPAMT201306UV02ParameterList extractHL7QueryParamsFromMessage(PRPAIN201305UV02 message) {
        LOG.trace("Entering HL7DbParser201305.ExtractHL7QueryParamsFromMessage method...");
        return hl7ParserUtils.extractHL7QueryParamsFromMessage(message);
    }

    /**
     * Method to extract the Patient from a PRPAIN201305UV02 message.
     *
     * @param message the incoming message which contains the patient to be extracted.
     * @return a Patient from the PRPAIN201305UV02 object
     */
    public static Patient extractMpiPatientFromMessage(PRPAIN201305UV02 message) {
        LOG.trace("Entering HL7DbParser201305.ExtractMpiPatientFromMessage method...");

        PRPAMT201306UV02ParameterList queryParamList = extractHL7QueryParamsFromMessage(message);
        Patient mpipatient = extractMpiPatientFromQueryParams(queryParamList);

        LOG.trace("Exiting HL7DbParser201305.ExtractMpiPatientFromMessage method...");
        return mpipatient;
    }

    /**
     * Method to extract the Patient from a PRPAMT201306UV02ParameterList message.
     *
     * @param params the incoming message which contains the patient to be extracted.
     * @return a Patient from the PRPAMT201306UV02ParameterList object
     */
    public static Patient extractMpiPatientFromQueryParams(PRPAMT201306UV02ParameterList params) {
        LOG.trace("Entering HL7DbParser201305.ExtractMpiPatientFromQueryParams method...");

        Patient mpiDbPatient = new Patient();

        if (params != null) {
            LOG.trace("Populating mpiDbPatient from QueryParams...");

            mpiDbPatient.setPersonnames(extractPersonnames(params));

            mpiDbPatient.setGender(extractGender(params));

            mpiDbPatient.setDateOfBirth(extractBirthdate(params));

            mpiDbPatient.setIdentifiers(extractPersonIdentifiers(params));

            mpiDbPatient.setSsn(extractSSNIdentifier(params));

            mpiDbPatient.setAddresses(extractPersonAddresses(params));

            mpiDbPatient.setPhonenumbers(extractTelecoms(params));

        } else {
            LOG.trace("QueryParams empty or null...");
            mpiDbPatient = null;
        }

        LOG.trace("Exiting HL7DbParser201305.ExtractMpiPatientFromQueryParams method...");
        return mpiDbPatient;
    }
}
