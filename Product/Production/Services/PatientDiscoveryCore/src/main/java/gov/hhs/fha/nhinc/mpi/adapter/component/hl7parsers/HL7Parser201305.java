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
package gov.hhs.fha.nhinc.mpi.adapter.component.hl7parsers;

import gov.hhs.fha.nhinc.mpilib.Address;
import gov.hhs.fha.nhinc.mpilib.Identifier;
import gov.hhs.fha.nhinc.mpilib.Identifiers;
import gov.hhs.fha.nhinc.mpilib.Patient;
import gov.hhs.fha.nhinc.mpilib.PersonName;
import gov.hhs.fha.nhinc.mpilib.PhoneNumber;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import javax.xml.bind.JAXBElement;
import org.hl7.v3.ADExplicit;
import org.hl7.v3.AdxpExplicitCity;
import org.hl7.v3.AdxpExplicitPostalCode;
import org.hl7.v3.AdxpExplicitState;
import org.hl7.v3.AdxpExplicitStreetAddressLine;
import org.hl7.v3.CE;
import org.hl7.v3.EnExplicitFamily;
import org.hl7.v3.EnExplicitGiven;
import org.hl7.v3.II;
import org.hl7.v3.IVLTSExplicit;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201305UV02QUQIMT021001UV01ControlActProcess;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectAdministrativeGender;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectBirthTime;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectId;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectName;
import org.hl7.v3.PRPAMT201306UV02ParameterList;
import org.hl7.v3.PRPAMT201306UV02PatientAddress;
import org.hl7.v3.PRPAMT201306UV02PatientTelecom;
import org.hl7.v3.PRPAMT201306UV02QueryByParameter;
import org.hl7.v3.TELExplicit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author rayj
 */
public class HL7Parser201305 {

    private static final Logger LOG = LoggerFactory.getLogger(HL7Parser201305.class);

    /**
     * Method to extract Gender Code from a PRPAMT201306UV02ParameterList.
     *
     * @param params the Paramater list from which to extract a Gender Code
     * @return The Gender Code is returned
     */
    public static String extractGender(PRPAMT201306UV02ParameterList params) {
        LOG.trace("Entering HL7Parser201305.ExtractGender method...");

        String genderCode = null;

        // Extract the gender from the query parameters - Assume only one was specified
        if (params.getLivingSubjectAdministrativeGender() != null
            && params.getLivingSubjectAdministrativeGender().size() > 0
            && params.getLivingSubjectAdministrativeGender().get(0) != null) {
            PRPAMT201306UV02LivingSubjectAdministrativeGender gender =
                params.getLivingSubjectAdministrativeGender().get(0);

            if (gender.getValue() != null && gender.getValue().size() > 0 && gender.getValue().get(0) != null) {
                CE administrativeGenderCode = gender.getValue().get(0);

                LOG.info("Found gender in query parameters = " + administrativeGenderCode.getCode());
                genderCode = administrativeGenderCode.getCode();
            } else {
                LOG.info("query does not contain a gender code");
            }
        } else {
            LOG.info("query does not contain a gender code");
        }

        LOG.trace("Exiting HL7Parser201305.ExtractGender method...");
        return genderCode;
    }

    /**
     * Method to extract birthdate from a PRPAMT201306UV02ParameterList.
     *
     * @param params the parameterList from which to extract the birthdate
     * @return a Timestamp object containing the birthdate.
     */
    public static String extractBirthdate(PRPAMT201306UV02ParameterList params) {
        LOG.trace("Entering HL7Parser201305.ExtractBirthdate method...");

        String birthDate = null;

        // Extract the birth time from the query parameters - Assume only one was specified
        if (params.getLivingSubjectBirthTime() != null && params.getLivingSubjectBirthTime().size() > 0
            && params.getLivingSubjectBirthTime().get(0) != null) {
            PRPAMT201306UV02LivingSubjectBirthTime birthTime = params.getLivingSubjectBirthTime().get(0);

            if (birthTime.getValue() != null && birthTime.getValue().size() > 0
                && birthTime.getValue().get(0) != null) {
                IVLTSExplicit birthday = birthTime.getValue().get(0);
                LOG.info("Found birthTime in query parameters = " + birthday.getValue());
                birthDate = birthday.getValue();
            } else {
                LOG.info("message does not contain a birthtime");
            }
        } else {
            LOG.info("message does not contain a birthtime");
        }

        LOG.trace("Exiting HL7Parser201305.ExtractBirthdate method...");
        return birthDate;
    }

    /**
     * Method to extract a list of Person names from a PRPAMT201306UV02ParameterList.
     * @param params the ParamaterList from which to extract names.
     * @return a list of names from the ParamaterList.
     */
    public static PersonName extractPersonName(PRPAMT201306UV02ParameterList params) {
        LOG.trace("Entering HL7Parser201305.ExtractPersonName method...");

        PersonName personname = new PersonName();

        // Extract the name from the query parameters - Assume only one was specified
        if (params.getLivingSubjectName() != null && params.getLivingSubjectName().size() > 0
            && params.getLivingSubjectName().get(0) != null) {
            PRPAMT201306UV02LivingSubjectName name = params.getLivingSubjectName().get(0);

            if (name.getValue() != null && name.getValue().size() > 0 && name.getValue().get(0) != null) {
                List<Serializable> choice = name.getValue().get(0).getContent();

                LOG.info("choice.size()=" + choice.size());

                Iterator<Serializable> iterSerialObjects = choice.iterator();

                String nameString = "";
                EnExplicitFamily lastname = null;
                EnExplicitGiven firstname = null;

                while (iterSerialObjects.hasNext()) {
                    LOG.info("in iterSerialObjects.hasNext() loop");

                    Serializable contentItem = iterSerialObjects.next();

                    if (contentItem instanceof String) {
                        LOG.info("contentItem is string");
                        String strValue = (String) contentItem;

                        if (nameString != null) {
                            nameString += strValue;
                        } else {
                            nameString = strValue;
                        }
                        LOG.info("nameString=" + nameString);
                    } else if (contentItem instanceof JAXBElement) {
                        LOG.info("contentItem is JAXBElement");

                        JAXBElement<?> oJAXBElement = (JAXBElement) contentItem;

                        if (oJAXBElement.getValue() instanceof EnExplicitFamily) {
                            lastname = (EnExplicitFamily) oJAXBElement.getValue();
                            LOG.info("found lastname element; content=" + lastname.getContent());
                        } else if (oJAXBElement.getValue() instanceof EnExplicitGiven) {
                            if (firstname == null) {
                                firstname = (EnExplicitGiven) oJAXBElement.getValue();
                                LOG.info("found firstname element; content=" + firstname.getContent());
                            } else {
                                // this would be where to add handle for middlename
                            }
                        } else {
                            LOG.info("other name part=" + oJAXBElement.getValue());
                        }
                    } else {
                        LOG.info("contentItem is other");
                    }
                }

                // If text string in patient name, then set in name
                // else set in element.
                boolean namefound = false;
                if (lastname != null && lastname.getContent() != null) {
                    personname.setLastName(lastname.getContent());
                    LOG.info("FamilyName : " + personname.getLastName());
                    namefound = true;
                }

                if (firstname != null && firstname.getContent() != null) {
                    personname.setFirstName(firstname.getContent());
                    LOG.info("GivenName : " + personname.getFirstName());
                    namefound = true;
                }

                if (!namefound && !nameString.trim().contentEquals("")) {
                    LOG.info("setting name by nameString " + nameString);
                    personname.setLastName(nameString);

                }
            } else {
                LOG.info("message does not contain a subject name");
            }
        } else {
            LOG.info("message does not contain a subject name");
        }

        LOG.trace("Exiting HL7Parser201305.ExtractPersonName method...");
        return personname;
    }

    /**
     * Method to extract a list of Person Identifiers from a PRPAMT201306UV02ParameterList.
     * @param params the PRPAMT201306UV02ParameterList from which to extract a list of Person Identifiers
     * @return a List of Identifiers.
     */
    public static Identifiers extractPersonIdentifiers(PRPAMT201306UV02ParameterList params) {
        LOG.trace("Entering HL7Parser201305.ExtractPersonIdentifiers method...");

        Identifiers ids = new Identifiers();
        Identifier id = new Identifier();

        if (params.getLivingSubjectId() != null && params.getLivingSubjectId().size() > 0
            && params.getLivingSubjectId().get(0) != null) {
            PRPAMT201306UV02LivingSubjectId livingSubjectId = params.getLivingSubjectId().get(0);

            if (livingSubjectId.getValue() != null && livingSubjectId.getValue().size() > 0
                && livingSubjectId.getValue().get(0) != null) {
                II subjectId = livingSubjectId.getValue().get(0);

                if (subjectId.getExtension() != null && subjectId.getExtension().length() > 0
                    && subjectId.getRoot() != null && subjectId.getRoot().length() > 0) {
                    id.setId(subjectId.getExtension());
                    id.setOrganizationId(subjectId.getRoot());
                    LOG.info("Created id from patient identifier [organization=" + id.getOrganizationId() + "][id="
                        + id.getId() + "]");
                    ids.add(id);
                } else {
                    LOG.info("message does not contain an id");
                }
            } else {
                LOG.info("message does not contain an id");
            }
        } else {
            LOG.info("message does not contain an id");
        }

        LOG.trace("Exiting HL7Parser201305.ExtractPersonIdentifiers method...");
        return ids;
    }

    /**
     * Method to extract an Address from a PRPAMT201306UV02ParameterList.
     * @param params the PRPAMT201306UV02ParameterList from which to extract the Address
     * @return an Address from PRPAMT201306UV02ParameterList.
     */
    public static Address extractPersonAddress(PRPAMT201306UV02ParameterList params) {
        LOG.trace("Entering HL7Parser201305.ExtractPersonAddress method...");

        Address address = null;

        if (params.getPatientAddress() != null && params.getPatientAddress().size() > 0
            && params.getPatientAddress().get(0) != null) {
            PRPAMT201306UV02PatientAddress patientAddress = params.getPatientAddress().get(0);

            if (patientAddress.getValue() != null && patientAddress.getValue().size() > 0
                && patientAddress.getValue().get(0) != null) {
                ADExplicit adExplicit = patientAddress.getValue().get(0);

                List<Serializable> choice = adExplicit.getContent();

                LOG.info("choice.size()=" + choice.size());

                Iterator<Serializable> iterSerialObjects = choice.iterator();

                int addressLineCounter = 0;
                AdxpExplicitStreetAddressLine addressLine1;
                AdxpExplicitStreetAddressLine addressLine2;
                AdxpExplicitCity city;
                AdxpExplicitState state;
                AdxpExplicitPostalCode postalCode;

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
                                LOG.info("found addressLine1 element; content=" + addressLine1.getContent());
                                if (address == null) {
                                    address = new Address();
                                }
                                address.setStreet1(addressLine1.getContent());
                            }
                            if (addressLineCounter == 2) {
                                addressLine2 = (AdxpExplicitStreetAddressLine) oJAXBElement.getValue();
                                LOG.info("found addressLine2 element; content=" + addressLine2.getContent());
                                if (address == null) {
                                    address = new Address();
                                }
                                address.setStreet2(addressLine2.getContent());
                            }
                        } else if (oJAXBElement.getValue() instanceof AdxpExplicitCity) {
                            city = (AdxpExplicitCity) oJAXBElement.getValue();
                            LOG.info("found city element; content=" + city.getContent());
                            if (address == null) {
                                address = new Address();
                            }
                            address.setCity(city.getContent());
                        } else if (oJAXBElement.getValue() instanceof AdxpExplicitState) {
                            state = (AdxpExplicitState) oJAXBElement.getValue();
                            LOG.info("found state element; content=" + state.getContent());
                            if (address == null) {
                                address = new Address();
                            }
                            address.setState(state.getContent());
                        } else if (oJAXBElement.getValue() instanceof AdxpExplicitPostalCode) {
                            postalCode = (AdxpExplicitPostalCode) oJAXBElement.getValue();
                            LOG.info("found postalCode element; content=" + postalCode.getContent());
                            if (address == null) {
                                address = new Address();
                            }
                            address.setZip(postalCode.getContent());
                        } else {
                            LOG.info("other address part=" + oJAXBElement.getValue());
                        }
                    } else {
                        LOG.info("contentItem is other");
                    }
                }

            }
        }

        return address;
    }

    /**
     * Method to extract the Phone Number from a PRPAMT201306UV02ParameterList.
     * @param params the PRPAMT201306UV02ParameterList from which to extract Phone Number
     * @return Phone Number from the PRPAMT201306UV02ParameterList
     */
    public static String extractTelecom(PRPAMT201306UV02ParameterList params) {
        LOG.trace("Entering HL7Parser201305.ExtractTelecom method...");

        String telecom = null;

        // Extract the telecom (phone number) from the query parameters - Assume only one was specified
        if (params.getPatientTelecom() != null && params.getPatientTelecom().size() > 0
            && params.getPatientTelecom().get(0) != null) {
            PRPAMT201306UV02PatientTelecom patientTelecom = params.getPatientTelecom().get(0);

            if (patientTelecom.getValue() != null && patientTelecom.getValue().size() > 0
                && patientTelecom.getValue().get(0) != null) {
                TELExplicit telecomValue = patientTelecom.getValue().get(0);
                LOG.info("Found patientTelecom in query parameters = " + telecomValue.getValue());
                telecom = telecomValue.getValue();
                if (telecom != null) {
                    if (!telecom.startsWith("tel:")) {
                        // telecom is not valid without tel: prefix
                        telecom = null;
                        LOG.info("Found patientTelecom in query parameters is not in the correct uri format");
                    }
                }
            } else {
                LOG.info("message does not contain a patientTelecom");
            }
        } else {
            LOG.info("message does not contain a patientTelecom");
        }

        LOG.trace("Exiting HL7Parser201305.ExtractTelecom method...");
        return telecom;
    }

    /**
     * Method to extract a PRPAMT201306UV02ParameterList object from a PRPAIN201305UV02 message.
     * @param message the PRPAIN201305UV02 message from which to extract the PRPAMT201306UV02ParameterList
     * @return PRPAMT201306UV02ParameterList
     */
    public static PRPAMT201306UV02ParameterList extractHL7QueryParamsFromMessage(PRPAIN201305UV02 message) {
        LOG.trace("Entering HL7Parser201305.ExtractHL7QueryParamsFromMessage method...");
        PRPAMT201306UV02ParameterList queryParamList = null;

        if (message == null) {
            LOG.warn("input message was null, no query parameters present in message");
            return null;
        }

        PRPAIN201305UV02QUQIMT021001UV01ControlActProcess controlActProcess = message.getControlActProcess();
        if (controlActProcess == null) {
            LOG.info("controlActProcess is null - no query parameters present in message");
            return null;
        }

        if (controlActProcess.getQueryByParameter() != null
            && controlActProcess.getQueryByParameter().getValue() != null) {
            PRPAMT201306UV02QueryByParameter queryParams = controlActProcess.getQueryByParameter().getValue();

            if (queryParams.getParameterList() != null) {
                queryParamList = queryParams.getParameterList();
            }

        }

        LOG.trace("Exiting HL7Parser201305.ExtractHL7QueryParamsFromMessage method...");
        return queryParamList;
    }

    /**
     * Method to extract the Patient from a PRPAIN201305UV02 message.
     * @param message the incoming message which contains the patient to be extracted.
     * @return a Patient from the PRPAIN201305UV02 object
     */
    public static Patient extractMpiPatientFromMessage(PRPAIN201305UV02 message) {
        LOG.trace("Entering HL7Parser201305.ExtractMpiPatientFromMessage method...");

        PRPAMT201306UV02ParameterList queryParamList = extractHL7QueryParamsFromMessage(message);
        Patient mpipatient = extractMpiPatientFromQueryParams(queryParamList);

        LOG.trace("Exiting HL7Parser201305.ExtractMpiPatientFromMessage method...");
        return mpipatient;
    }

    /**
     * Method to extract the Patient from a PRPAMT201306UV02ParameterList message.
     * @param params the incoming message which contains the patient to be extracted.
     * @return a Patient from the PRPAMT201306UV02ParameterList object
     */
    public static Patient extractMpiPatientFromQueryParams(PRPAMT201306UV02ParameterList params) {
        LOG.trace("Entering HL7Parser201305.ExtractMpiPatientFromQueryParams method...");

        Patient mpiPatient = new Patient();

        if (params != null) {

            mpiPatient.getNames().add(extractPersonName(params));
            mpiPatient.setGender(extractGender(params));

            String birthdateString = extractBirthdate(params);
            mpiPatient.setDateOfBirth(birthdateString);

            Identifiers ids = extractPersonIdentifiers(params);
            mpiPatient.setIdentifiers(ids);

            Address address = extractPersonAddress(params);
            if (address != null) {
                mpiPatient.getAddresses().add(address);
            }

            String telecom = extractTelecom(params);
            if (telecom != null) {
                PhoneNumber phoneNumber = new PhoneNumber();
                phoneNumber.setPhoneNumber(telecom);
                mpiPatient.getPhoneNumbers().add(phoneNumber);
            }
        } else {
            mpiPatient = null;
        }

        LOG.trace("Exiting HL7Parser201305.ExtractMpiPatientFromQueryParams method...");
        return mpiPatient;
    }
}
