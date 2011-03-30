/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2011(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package gov.hhs.fha.nhinc.mpi.adapter.component.hl7parsers;

import gov.hhs.fha.nhinc.patientdb.model.*;
import gov.hhs.fha.nhinc.util.format.UTCDateUtil;
import java.util.List;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import javax.xml.bind.JAXBElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.*;

/**
 *
 * @author richard.ettema
 */
public class HL7DbParser201305 {

    private static Log log = LogFactory.getLog(HL7DbParser201305.class);

    public static final String SSN_ROOT_IDENTIFIER = "2.16.840.1.113883.4.1";

    public static String ExtractGender(PRPAMT201306UV02ParameterList params) {
        log.debug("Entering HL7DbParser201305.ExtractGender method...");

        String genderCode = null;

        // Extract the gender from the query parameters - Assume only one was specified
        if (params.getLivingSubjectAdministrativeGender() != null &&
                params.getLivingSubjectAdministrativeGender().size() > 0 &&
                params.getLivingSubjectAdministrativeGender().get(0) != null) {
            PRPAMT201306UV02LivingSubjectAdministrativeGender gender = params.getLivingSubjectAdministrativeGender().get(0);

            if (gender.getValue() != null &&
                    gender.getValue().size() > 0 &&
                    gender.getValue().get(0) != null) {
                CE administrativeGenderCode = gender.getValue().get(0);

                log.info("Found gender in query parameters = " + administrativeGenderCode.getCode());
                genderCode = administrativeGenderCode.getCode();
            } else {
                log.info("query does not contain a gender code");
            }
        } else {
            log.info("query does not contain a gender code");
        }

        log.debug("Exiting HL7DbParser201305.ExtractGender method...");
        return genderCode;
    }

    public static Timestamp ExtractBirthdate(PRPAMT201306UV02ParameterList params) {
        log.debug("Entering HL7DbParser201305.ExtractBirthdate method...");

        Timestamp birthDate = null;

        try {
            // Extract the birth time from the query parameters - Assume only one was specified
            if (params.getLivingSubjectBirthTime() != null &&
                    params.getLivingSubjectBirthTime().size() > 0 &&
                    params.getLivingSubjectBirthTime().get(0) != null) {
                PRPAMT201306UV02LivingSubjectBirthTime birthTime = params.getLivingSubjectBirthTime().get(0);

                if (birthTime.getValue() != null &&
                        birthTime.getValue().size() > 0 &&
                        birthTime.getValue().get(0) != null) {
                    IVLTSExplicit birthday = birthTime.getValue().get(0);
                    log.info("Found birthTime in query parameters = " + birthday.getValue());
                    UTCDateUtil utcDateUtil = new UTCDateUtil();
                    // Check date string length
                    if (birthday.getValue().length() == 8) {
                        birthDate = new Timestamp(utcDateUtil.parseDate(birthday.getValue(), UTCDateUtil.DATE_ONLY_FORMAT, null).getTime());
                    } else if (birthday.getValue().length() > 8) {
                        birthDate = new Timestamp(utcDateUtil.parseDate(birthday.getValue(), UTCDateUtil.DATE_FORMAT_UTC, null).getTime());
                    } else {
                        log.info("message does not contain a valid formatted birthtime");
                    }
                } else {
                    log.info("message does not contain a birthtime");
                }
            } else {
                log.info("message does not contain a birthtime");
            }
        }
        catch (Exception e) {
            log.error("Exception parsing birth date: ", e);
            return null;
        }

        log.debug("Exiting HL7DbParser201305.ExtractBirthdate method...");
        return birthDate;
    }

    public static List<Personname> ExtractPersonnames(PRPAMT201306UV02ParameterList params) {
        log.debug("Entering HL7DbParser201305.ExtractPersonnames method...");

        List<Personname> personnames = new ArrayList<Personname>();

        // Extract the person names from the query parameters
        if (params.getLivingSubjectName() != null &&
                params.getLivingSubjectName().size() > 0 &&
                params.getLivingSubjectName().get(0) != null) {

            for (PRPAMT201306UV02LivingSubjectName name : params.getLivingSubjectName()) {
                if (name.getValue() != null &&
                        name.getValue().size() > 0 &&
                        name.getValue().get(0) != null) {
                    List<Serializable> choice = name.getValue().get(0).getContent();

                    log.info("choice.size()=" + choice.size());

                    Iterator<Serializable> iterSerialObjects = choice.iterator();

                    String nameString = "";
                    EnExplicitFamily lastname = null;
                    EnExplicitGiven firstname = null;
                    EnExplicitGiven middlename = null;
                    EnExplicitPrefix prefix = null;
                    EnExplicitSuffix suffix = null;

                    while (iterSerialObjects.hasNext()) {
                        log.info("in iterSerialObjects.hasNext() loop");

                        Serializable contentItem = iterSerialObjects.next();

                        if (contentItem instanceof String) {
                            log.info("contentItem is string");
                            String strValue = (String) contentItem;

                            if (nameString != null) {
                                nameString += strValue;
                            } else {
                                nameString = strValue;
                            }
                            log.info("nameString=" + nameString);
                        } else if (contentItem instanceof JAXBElement) {
                            log.info("contentItem is JAXBElement");

                            JAXBElement oJAXBElement = (JAXBElement) contentItem;

                            if (oJAXBElement.getValue() instanceof EnExplicitFamily) {
                                lastname = new EnExplicitFamily();
                                lastname = (EnExplicitFamily) oJAXBElement.getValue();
                                log.info("found lastname element; content=" + lastname.getContent());
                            } else if (oJAXBElement.getValue() instanceof EnExplicitGiven) {
                                if (firstname == null) {
                                    firstname = new EnExplicitGiven();
                                    firstname = (EnExplicitGiven) oJAXBElement.getValue();
                                    log.info("found firstname element; content=" + firstname.getContent());
                                } else if (middlename == null) {
                                    middlename = new EnExplicitGiven();
                                    middlename = (EnExplicitGiven) oJAXBElement.getValue();
                                    log.info("found middlename element; content=" + middlename.getContent());
                                } else {
                                    //ignore all other given values
                                }
                            } else if (oJAXBElement.getValue() instanceof EnExplicitPrefix) {
                                prefix = new EnExplicitPrefix();
                                prefix = (EnExplicitPrefix) oJAXBElement.getValue();
                                log.info("found prefix element; content=" + prefix.getContent());
                            } else if (oJAXBElement.getValue() instanceof EnExplicitSuffix) {
                                suffix = new EnExplicitSuffix();
                                suffix = (EnExplicitSuffix) oJAXBElement.getValue();
                                log.info("found suffix element; content=" + suffix.getContent());
                            } else {
                                log.info("other name part=" + (ENXPExplicit) oJAXBElement.getValue());
                            }
                        } else {
                            log.info("contentItem is other");
                        }
                    }

                    // If text string in patient name, then set in name
                    // else set in element.
                    boolean namefound = false;
                    Personname personname = new Personname();
                    if (lastname != null && lastname.getContent() != null) {
                        personname.setLastName(lastname.getContent());
                        log.info("FamilyName : " + personname.getLastName());
                        namefound = true;
                    }

                    if (firstname != null && firstname.getContent() != null) {
                        personname.setFirstName(firstname.getContent());
                        log.info("GivenName(first) : " + personname.getFirstName());
                        namefound = true;
                    }

                    if (middlename != null && middlename.getContent() != null) {
                        personname.setMiddleName(middlename.getContent());
                        log.info("GivenName(middle) : " + personname.getMiddleName());
                        namefound = true;
                    }

                    if (prefix != null && prefix.getContent() != null) {
                        personname.setPrefix(prefix.getContent());
                        log.info("Prefix : " + personname.getPrefix());
                        namefound = true;
                    }

                    if (suffix != null && suffix.getContent() != null) {
                        personname.setSuffix(suffix.getContent());
                        log.info("Suffix : " + personname.getSuffix());
                        namefound = true;
                    }

                    if (!namefound && !nameString.trim().contentEquals("")) {
                        log.info("setting name by nameString " + nameString);
                        personname.setLastName(nameString);

                    }

                    personnames.add(personname);
                } else {
                    log.info("message does not contain a subject name");
                }
            }
        } else {
            log.info("message does not contain a subject name");
        }

        log.debug("Exiting HL7DbParser201305.ExtractPersonnames method...");
        return personnames;
    }

    public static List<Identifier> ExtractPersonIdentifiers(PRPAMT201306UV02ParameterList params) {
        log.debug("Entering HL7DbParser201305.ExtractPersonIdentifiers method...");

        List<Identifier> ids = new ArrayList<Identifier>();

        if (params.getLivingSubjectId() != null &&
                params.getLivingSubjectId().size() > 0 &&
                params.getLivingSubjectId().get(0) != null) {

            for (PRPAMT201306UV02LivingSubjectId livingSubjectId : params.getLivingSubjectId()) {
                if (livingSubjectId.getValue() != null &&
                        livingSubjectId.getValue().size() > 0 &&
                        livingSubjectId.getValue().get(0) != null) {
                    II subjectId = livingSubjectId.getValue().get(0);

                    if (subjectId.getExtension() != null &&
                            subjectId.getExtension().length() > 0 &&
                            subjectId.getRoot() != null &&
                            subjectId.getRoot().length() > 0) {
                        // Ignore SSN identifiers
                        if (!subjectId.getRoot().equals(SSN_ROOT_IDENTIFIER)) {
                            Identifier id = new Identifier();
                            id.setId(subjectId.getExtension());
                            id.setOrganizationId(subjectId.getRoot());
                            log.info("Created id from patient identifier [organization=" + id.getOrganizationId() + "][id=" + id.getId() + "]");
                            ids.add(id);
                        }
                    } else {
                        log.info("message does not contain an id");
                    }
                } else {
                    log.info("message does not contain an id");
                }
            }
        } else {
            log.info("message does not contain an id");
        }

        log.debug("Exiting HL7DbParser201305.ExtractPersonIdentifiers method...");
        return ids;
    }

    public static String ExtractSSNIdentifier(PRPAMT201306UV02ParameterList params) {
        log.debug("Entering HL7DbParser201305.ExtractSSNIdentifier method...");

        String ssn = null;

        if (params.getLivingSubjectId() != null &&
                params.getLivingSubjectId().size() > 0 &&
                params.getLivingSubjectId().get(0) != null) {

            for (PRPAMT201306UV02LivingSubjectId livingSubjectId : params.getLivingSubjectId()) {
                if (livingSubjectId.getValue() != null &&
                        livingSubjectId.getValue().size() > 0 &&
                        livingSubjectId.getValue().get(0) != null) {
                    II subjectId = livingSubjectId.getValue().get(0);

                    if (subjectId.getExtension() != null &&
                            subjectId.getExtension().length() > 0 &&
                            subjectId.getRoot() != null &&
                            subjectId.getRoot().length() > 0) {
                        // Look for first SSN identifier
                        if (subjectId.getRoot().equals(SSN_ROOT_IDENTIFIER)) {
                            Identifier ssnId = new Identifier();
                            ssnId.setId(subjectId.getExtension());
                            ssnId.setOrganizationId(subjectId.getRoot());
                            log.info("Created id from ssn identifier [organization=" + ssnId.getOrganizationId() + "][id=" + ssnId.getId() + "]");
                            ssn = ssnId.getId();
                        }
                    } else {
                        log.info("message does not contain an id");
                    }
                } else {
                    log.info("message does not contain an id");
                }
            }
        } else {
            log.info("message does not contain an id");
        }

        log.debug("Exiting HL7DbParser201305.ExtractSSNIdentifier method...");
        return ssn;
    }

    public static List<Address> ExtractPersonAddresses(PRPAMT201306UV02ParameterList params) {
        log.debug("Entering HL7DbParser201305.ExtractPersonAddress method...");

        List<Address> addresses = new ArrayList<Address>();

        if (params.getPatientAddress() != null &&
                params.getPatientAddress().size() > 0 &&
                params.getPatientAddress().get(0) != null) {

            for (PRPAMT201306UV02PatientAddress patientAddress : params.getPatientAddress()) {
                if (patientAddress.getValue() != null &&
                        patientAddress.getValue().size() > 0 &&
                        patientAddress.getValue().get(0) != null) {
                    ADExplicit adExplicit = patientAddress.getValue().get(0);

                    List<Serializable> choice = adExplicit.getContent();

                    log.info("choice.size()=" + choice.size());

                    Iterator<Serializable> iterSerialObjects = choice.iterator();

                    int addressLineCounter = 0;
                    AdxpExplicitStreetAddressLine addressLine1 = null;
                    AdxpExplicitStreetAddressLine addressLine2 = null;
                    AdxpExplicitCity city = null;
                    AdxpExplicitState state = null;
                    AdxpExplicitPostalCode postalCode = null;

                    Address address = null;

                    while (iterSerialObjects.hasNext()) {
                        log.info("in iterSerialObjects.hasNext() loop");

                        Serializable contentItem = iterSerialObjects.next();

                        if (contentItem instanceof JAXBElement) {
                            log.info("contentItem is JAXBElement");

                            JAXBElement oJAXBElement = (JAXBElement) contentItem;

                            if (oJAXBElement.getValue() instanceof AdxpExplicitStreetAddressLine) {
                                addressLineCounter++;
                                if (addressLineCounter == 1) {
                                    addressLine1 = new AdxpExplicitStreetAddressLine();
                                    addressLine1 = (AdxpExplicitStreetAddressLine) oJAXBElement.getValue();
                                    log.info("found addressLine1 element; content=" + addressLine1.getContent());
                                    if (address == null) {
                                        address = new Address();
                                    }
                                    address.setStreet1(addressLine1.getContent());
                                }
                                if (addressLineCounter == 2) {
                                    addressLine2 = new AdxpExplicitStreetAddressLine();
                                    addressLine2 = (AdxpExplicitStreetAddressLine) oJAXBElement.getValue();
                                    log.info("found addressLine2 element; content=" + addressLine2.getContent());
                                    if (address == null) {
                                        address = new Address();
                                    }
                                    address.setStreet2(addressLine2.getContent());
                                }
                            } else if (oJAXBElement.getValue() instanceof AdxpExplicitCity) {
                                city = new AdxpExplicitCity();
                                city = (AdxpExplicitCity) oJAXBElement.getValue();
                                log.info("found city element; content=" + city.getContent());
                                if (address == null) {
                                    address = new Address();
                                }
                                address.setCity(city.getContent());
                            } else if (oJAXBElement.getValue() instanceof AdxpExplicitState) {
                                state = new AdxpExplicitState();
                                state = (AdxpExplicitState) oJAXBElement.getValue();
                                log.info("found state element; content=" + state.getContent());
                                if (address == null) {
                                    address = new Address();
                                }
                                address.setState(state.getContent());
                            } else if (oJAXBElement.getValue() instanceof AdxpExplicitPostalCode) {
                                postalCode = new AdxpExplicitPostalCode();
                                postalCode = (AdxpExplicitPostalCode) oJAXBElement.getValue();
                                log.info("found postalCode element; content=" + postalCode.getContent());
                                if (address == null) {
                                    address = new Address();
                                }
                                address.setPostal(postalCode.getContent());
                            } else {
                                log.info("other address part=" + (ADXPExplicit) oJAXBElement.getValue());
                            }
                        } else {
                            log.info("contentItem is other");
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

    public static List<Phonenumber> ExtractTelecoms(PRPAMT201306UV02ParameterList params) {
        log.debug("Entering HL7DbParser201305.ExtractTelecoms method...");

        List<Phonenumber> phonenumbers = new ArrayList<Phonenumber>();

        Phonenumber phonenumber = null;

        // Extract the telecom (phone number) from the query parameters - Assume only one was specified
        if (params.getPatientTelecom() != null &&
                params.getPatientTelecom().size() > 0 &&
                params.getPatientTelecom().get(0) != null) {

            int count = 0;
            for (PRPAMT201306UV02PatientTelecom patientTelecom : params.getPatientTelecom()) {
                if (patientTelecom.getValue() != null &&
                        patientTelecom.getValue().size() > 0 &&
                        patientTelecom.getValue().get(0) != null) {
                    TELExplicit telecomValue = patientTelecom.getValue().get(0);
                    log.info("Found patientTelecom in query parameters = " + telecomValue.getValue());
                    String telecom = telecomValue.getValue();
                    if (telecom != null) {
                        if (!telecom.startsWith("tel:")) {
                            // telecom is not valid without tel: prefix
                            log.info("Found patientTelecom [" + telecom + "] in query parameters is not in the correct uri format");
                            telecom = null;
                        }
                        else {
                            phonenumber = new Phonenumber();
                            phonenumber.setValue(telecom);
                            phonenumbers.add(phonenumber);
                        }
                    }
                } else {
                    log.info("patientTelecom[" + count + "] does not contain a value.");
                }
                count++;
            }
        } else {
            log.info("message does not contain a patientTelecom");
        }

        log.debug("Exiting HL7DbParser201305.ExtractTelecoms method...");
        return phonenumbers;
    }

    public static PRPAMT201306UV02ParameterList ExtractHL7QueryParamsFromMessage(org.hl7.v3.PRPAIN201305UV02 message) {
        log.debug("Entering HL7DbParser201305.ExtractHL7QueryParamsFromMessage method...");
        PRPAMT201306UV02ParameterList queryParamList = null;

        if (message == null) {
            log.warn("input message was null, no query parameters present in message");
            return null;
        }

        PRPAIN201305UV02QUQIMT021001UV01ControlActProcess controlActProcess = message.getControlActProcess();
        if (controlActProcess == null) {
            log.info("controlActProcess is null - no query parameters present in message");
            return null;
        }

        if (controlActProcess.getQueryByParameter() != null &&
                controlActProcess.getQueryByParameter().getValue() != null) {
            PRPAMT201306UV02QueryByParameter queryParams = (PRPAMT201306UV02QueryByParameter) controlActProcess.getQueryByParameter().getValue();

            if (queryParams.getParameterList() != null) {
                queryParamList = queryParams.getParameterList();
            }

        }

        log.debug("Exiting HL7DbParser201305.ExtractHL7QueryParamsFromMessage method...");
        return queryParamList;
    }

    public static Patient ExtractMpiPatientFromMessage(
            org.hl7.v3.PRPAIN201305UV02 message) {
        log.debug("Entering HL7DbParser201305.ExtractMpiPatientFromMessage method...");

        PRPAMT201306UV02ParameterList queryParamList = ExtractHL7QueryParamsFromMessage(message);
        Patient mpipatient = ExtractMpiPatientFromQueryParams(queryParamList);

        log.debug("Exiting HL7DbParser201305.ExtractMpiPatientFromMessage method...");
        return mpipatient;
    }

    public static Patient ExtractMpiPatientFromQueryParams(PRPAMT201306UV02ParameterList params) {
        log.debug("Entering HL7DbParser201305.ExtractMpiPatientFromQueryParams method...");

        Patient mpiDbPatient = new Patient();

        if (params != null) {
            log.debug("Populating mpiDbPatient from QueryParams...");

            mpiDbPatient.setPersonnames(ExtractPersonnames(params));

            mpiDbPatient.setGender(ExtractGender(params));

            mpiDbPatient.setDateOfBirth(ExtractBirthdate(params));

            mpiDbPatient.setIdentifiers(ExtractPersonIdentifiers(params));

            mpiDbPatient.setSsn(ExtractSSNIdentifier(params));

            mpiDbPatient.setAddresses(ExtractPersonAddresses(params));

            mpiDbPatient.setPhonenumbers(ExtractTelecoms(params));

            log.debug("mpiDbPatient is " + mpiDbPatient.toString());
        } else {
            log.debug("QueryParams empty or null...");
            mpiDbPatient = null;
        }

        log.debug("Exiting HL7DbParser201305.ExtractMpiPatientFromQueryParams method...");
        return mpiDbPatient;
    }
}
