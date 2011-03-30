/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.mpilib;

import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author rayj
 */
public class PatientMatcher {

    private static Log log = LogFactory.getLog(MiniMpi.class);

    public static boolean IsSearchMatchByIds(Patient possibleMatch, Patient searchParams) {
        //i will call it a match if any one of the ids matches any other ids
        //this is over simplified, but fine for now
        boolean match = false;
        for (Identifier searchParamIdentifier : searchParams.getIdentifiers()) {
            for (Identifier possibleMatchIdentifier : possibleMatch.getIdentifiers()) {
                match = match | IsSearchMatchbyId(searchParamIdentifier, possibleMatchIdentifier);
            }
        }
        return match;
    }

    public static boolean IsSearchMatchbyId(Identifier a, Identifier b) {
        boolean match;
        if (a == null) {
            log.info("a is null");
        }
        if (a.getId() == null) {
            log.info("a.getId() is null");
        }
        if (a.getOrganizationId() == null) {
            log.info("a.getOrganizationId() is null");
        }
        if (b == null) {
            log.info("b is null");
        }
        if (b.getId() == null) {
            log.info("b.getId() is null");
        }
        if (b.getOrganizationId() == null) {
            log.info("b.getOrganizationId() is null");
        }
        match = ((!a.getId().contentEquals("")) && (!b.getId().contentEquals(""))) && (a.getId().contentEquals(b.getId()) && a.getOrganizationId().contentEquals(b.getOrganizationId()));
        return match;
    }
    
    public static boolean isPatientOptedInCriteriaMet(Patient possibleMatch){
        return possibleMatch.isOptedIn();
    }
    
    public static boolean IsSearchMatchByDemographics(Patient possibleMatch, Patient searchParams) {

        PersonName possibleMatchName = null;
        PersonName searchName = null;

        if(possibleMatch.getNames().size() > 0)
        {
            possibleMatchName = possibleMatch.getNames().get(0);
        }
        else
        {
            possibleMatchName = possibleMatch.getName();
        }

        if(searchParams.getNames().size() > 0)
        {
            searchName = searchParams.getNames().get(0);
        }
        else
        {
            searchName = searchParams.getName();
        }

        boolean match = DoesNameMeetSearchCriteria(possibleMatchName, searchName);
        log.debug("[" + SerializePatient(searchParams) + "]==[" + SerializePatient(possibleMatch) + "] -> " + match);
        if (match) {
            match = DoesBirthdateMeetSearchCriteria(possibleMatch.getDateOfBirth(), searchParams.getDateOfBirth());
            log.debug("[" + searchParams.getDateOfBirth() + "]==[" + possibleMatch.getDateOfBirth() + "] -> " + match);
        }
        if (match) {
            match = DoesGenderMeetSearchCriteria(possibleMatch.getGender(), searchParams.getGender());
            log.debug("[" + searchParams.getGender() + "]==[" + possibleMatch.getGender() + "] -> " + match);
        }
        if (match &&
                possibleMatch.getAddresses().size() > 0 && searchParams.getAddresses().size()> 0) {
            match = DoesAddressMeetSearchCriteria(possibleMatch.getAddresses().get(0), searchParams.getAddresses().get(0));
            log.debug("[" + SerializePatientAddress(searchParams) + "]==[" + SerializePatientAddress(possibleMatch) + "] -> " + match);
        }
        if (match &&
                possibleMatch.getPhoneNumbers().size() > 0 && searchParams.getPhoneNumbers().size() > 0) {
            match = DoesTelecomMeetSearchCriteria(possibleMatch.getPhoneNumbers().get(0).getPhoneNumber(), searchParams.getPhoneNumbers().get(0).getPhoneNumber());
            log.debug("[" + searchParams.getPhoneNumbers().get(0).getPhoneNumber() + "]==[" + possibleMatch.getPhoneNumbers().get(0).getPhoneNumber() + "] -> " + match);
        }
        return match;
    }

    public static boolean IsMatchByDemographics(Patient patient, Patient possibleMatch) {
        return IsSearchMatchByDemographics(patient, possibleMatch);
    }

    private static boolean IsNameNullish(PersonName name) {
        boolean result;
        if (name == null) {
            result = true;
        } else {
            result = (NullChecker.isNullish(name.getFirstName()) && NullChecker.isNullish(name.getLastName()));
        }
        return result;
    }

    private static boolean DoesNameMeetSearchCriteria(PersonName name, PersonName searchName) {
        boolean result;

        if (IsNameNullish(searchName)) {
            log.info("search name nullish");
            result = false;
        } else {
            result = DoesNamePartMeetSearchCriteria(name.getLastName(), searchName.getLastName()) && DoesNamePartMeetSearchCriteria(name.getFirstName(), searchName.getFirstName());
        }
        return result;
    }

    private static boolean DoesNamePartMeetSearchCriteria(String namepart, String searchNamepart) {
        boolean result;
        if (NullChecker.isNullish(searchNamepart)) {
            result = true;
        } else if (NullChecker.isNullish(namepart)) {
            result = false;
        } else {
            result = namepart.equalsIgnoreCase(searchNamepart);
        }
        log.info("DoesNamePartMeetSearchCriteria  " + namepart + "=" + searchNamepart + " ->" + result);
        return result;
    }

    private static boolean DoesBirthdateMeetSearchCriteria(String birthdate, String searchBirthdate) {
        boolean result;

        if (NullChecker.isNullish(searchBirthdate)) {
            log.info("search birthdate nullish");
            result = true;
        } else if (NullChecker.isNullish(birthdate)) {
            result = false;
        } else {
            result = birthdate.equalsIgnoreCase(searchBirthdate);
        }

        return result;
    }

    private static boolean DoesGenderMeetSearchCriteria(String gender, String searchGender) {
        boolean result;

        if (NullChecker.isNullish(searchGender)) {
            log.info("search gender nullish");
            result = true;
        } else if (NullChecker.isNullish(gender)) {
            result = false;
        } else {
            result = gender.equalsIgnoreCase(searchGender);
        }

        return result;
    }

    private static boolean IsAddressNullish(Address address) {
        boolean result;
        if (address == null) {
            result = true;
        } else {
            result = false; // Individual contents of address can be null
        }
        return result;
    }

    private static boolean DoesAddressMeetSearchCriteria(Address address, Address searchAddress) {
        boolean result;

        if (IsAddressNullish(searchAddress)) {
            log.info("search address nullish");
            result = false;
        } else {
            result =
                (DoesAddressPartMeetSearchCriteria(address.getStreet1(), searchAddress.getStreet1()) &&
                    DoesAddressPartMeetSearchCriteria(address.getStreet2(), searchAddress.getStreet2()) &&
                    DoesAddressPartMeetSearchCriteria(address.getCity(), searchAddress.getCity()) &&
                    DoesAddressPartMeetSearchCriteria(address.getState(), searchAddress.getState()) &&
                    DoesAddressPartMeetSearchCriteria(address.getZip(), searchAddress.getZip()));
        }

        return result;
    }

    private static boolean DoesAddressPartMeetSearchCriteria(String addresspart, String searchAddresspart) {
        boolean result;
        if (NullChecker.isNullish(searchAddresspart)) {
            result = true;
        } else if (NullChecker.isNullish(addresspart)) {
            result = false;
        } else {
            result = addresspart.equalsIgnoreCase(searchAddresspart);
        }
        log.info("DoesAddressPartMeetSearchCriteria  " + addresspart + "=" + searchAddresspart + " ->" + result);
        return result;
    }

    private static boolean DoesTelecomMeetSearchCriteria(String telecom, String searchTelecom) {
        boolean result;

        if (NullChecker.isNullish(searchTelecom)) {
            log.info("search telecom nullish");
            result = true;
        } else if (NullChecker.isNullish(telecom)) {
            result = false;
        } else {
            result = compareTelecoms(telecom, searchTelecom);
        }

        return result;
    }

    private static boolean compareTelecoms(String telecom, String searchTelecom) {
        boolean result = false;

        // Check for valid uri prefix
        if (telecom != null  && searchTelecom != null) {
            if (!(telecom.startsWith("tel:") && searchTelecom.startsWith("tel:"))) {
                result = false;
            }
            else {
                String compareTelecom = removeTelecomVisualSeparators(telecom);
                String compareSearchTelecom = removeTelecomVisualSeparators(searchTelecom);

                result = compareTelecom.equalsIgnoreCase(compareSearchTelecom);
            }
        }

        return result;
    }

    private static String removeTelecomVisualSeparators(String telecomIn) {
        char[] visualSep = {'-', '.', '(', ')'};
        StringBuffer telecomOut = new StringBuffer("");
        boolean skip = false;

        for (int i=0; i<telecomIn.length(); i++) {
            skip = false;
            for (int j=0; j<4; j++) {
                if (telecomIn.charAt(i) == visualSep[j]) {
                    skip = true;
                    break;
                }
            }
            if (!skip) {
                telecomOut.append(telecomIn.charAt(i));
            }
        }
        return telecomOut.toString();
    }

    private static String SerializePatientAddress(Patient patient) {
        String serializedString = "";
        if (patient.getAddresses() != null &&
                patient.getAddresses().size() > 0 &&
                patient.getAddresses().get(0) != null) {
            serializedString = patient.getAddresses().get(0).getStreet1() + "," +
                    patient.getAddresses().get(0).getStreet2() + "," +
                    patient.getAddresses().get(0).getCity() + "," +
                    patient.getAddresses().get(0).getState() + "," +
                    patient.getAddresses().get(0).getZip();
        }
        return serializedString;
    }

    private static String SerializePatient(Patient patient) {
        return patient.toString();
    }
}
