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
package gov.hhs.fha.nhinc.mpilib;

import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *
 */
public class PatientMatcher {

    private static final String TELEPHONE_PREFIX_STRING = "tel:";

    private static final Logger LOG = LoggerFactory.getLogger(PatientMatcher.class);
    private static PatientMatcher instance = null;

    /**
     * Private/Protected constructor for singleton class. Protected for testing purposes.
     */
    protected PatientMatcher() {
    }

    /**
     * Factory method for singleton instance.
     * @return an insatnce of this class
     */
    public static PatientMatcher getInstance() {
        if (instance == null) {
            instance = new PatientMatcher();
        }
        return instance;
    }

    public boolean hasMatchByIds(Patient possibleMatch, Patient searchParams) {
        for (Identifier possibleMatchIdentifier : possibleMatch.getIdentifiers()) {
            if (searchParams.getIdentifiers().contains(possibleMatchIdentifier)) {
                return true;
            }
        }

        return false;
    }

    public boolean isPatientOptedInCriteriaMet(Patient possibleMatch) {
        return possibleMatch.isOptedIn();
    }

    public boolean hasMatchByDemographics(Patient possibleMatch, Patient searchParams) {
        PersonName possibleMatchName = getPatientName(possibleMatch);
        PersonName searchName = getPatientName(searchParams);

        boolean match = isNameEquals(possibleMatchName, searchName);
        if (match) {
            match = isBirthdateEquals(possibleMatch.getDateOfBirth(), searchParams.getDateOfBirth());
        }
        if (match) {
            match = isGenderEquals(possibleMatch.getGender(), searchParams.getGender());
        }
        if (match && CollectionUtils.isNotEmpty(possibleMatch.getAddresses()) && CollectionUtils.isNotEmpty(searchParams.getAddresses())) {
            match = isAddressEquals(possibleMatch.getAddresses().get(0), searchParams.getAddresses().get(0));
        }
        if (match && CollectionUtils.isNotEmpty(possibleMatch.getPhoneNumbers()) && CollectionUtils.isNotEmpty(searchParams.getPhoneNumbers())) {
            match = isPhoneNumberEquals(possibleMatch.getPhoneNumbers().get(0).getPhoneNumber(), searchParams
                .getPhoneNumbers().get(0).getPhoneNumber());
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("hasMatchByDemoagraphics result ==>" + match);
            LOG.debug("[" + serializePatient(searchParams) + "]==[" + serializePatient(possibleMatch) + "]");
            LOG.debug("[" + searchParams.getDateOfBirth() + "]==[" + possibleMatch.getDateOfBirth() + "]");
            LOG.debug("[" + searchParams.getGender() + "]==[" + possibleMatch.getGender() + "]");
            LOG.debug("[" + serializePatientAddress(searchParams) + "]==[" + serializePatientAddress(possibleMatch)
            + "]");
            if (CollectionUtils.isNotEmpty(searchParams.getPhoneNumbers())) {
                LOG.debug("[" + searchParams.getPhoneNumbers().get(0).getPhoneNumber() + "]==["
                    + possibleMatch.getPhoneNumbers().get(0).getPhoneNumber() + "]");
            }
        }

        return match;
    }

    private PersonName getPatientName(Patient patient) {
        PersonName name = null;
        if (CollectionUtils.isNotEmpty(patient.getNames())) {
            name = patient.getNames().get(0);
        }
        return name;
    }

    private boolean isNameValid(PersonName name) {
        boolean result = false;
        if (name != null) {
            result = name.isValid();
        }
        return result;
    }

    private boolean isNameEquals(PersonName name, PersonName searchName) {
        boolean result = false;

        if (isNameValid(name) && isNameValid(searchName)) {
            result = name.getLastName().equalsIgnoreCase(searchName.getLastName())
                && name.getFirstName().equalsIgnoreCase(searchName.getFirstName());
        }
        return result;
    }

    private static boolean isBirthdateEquals(String birthdate, String searchBirthdate) {
        boolean result;

        if (NullChecker.isNullish(searchBirthdate)) {
            result = true;
        } else if (NullChecker.isNullish(birthdate)) {
            result = false;
        } else {
            result = birthdate.equalsIgnoreCase(searchBirthdate);
        }

        return result;
    }

    private static boolean isGenderEquals(String gender, String searchGender) {
        boolean result;

        if (NullChecker.isNullish(searchGender)) {
            result = true;
        } else if (NullChecker.isNullish(gender)) {
            result = false;
        } else {
            result = gender.equalsIgnoreCase(searchGender);
        }

        return result;
    }

    private boolean isAddressNullish(Address address) {
        boolean result;
        if (address == null) {
            result = true;
        } else {
            result = false; // Individual contents of address can be null
        }
        return result;
    }

    private boolean isAddressEquals(Address address, Address searchAddress) {
        boolean result;

        if (isAddressNullish(searchAddress)) {
            result = false;
        } else {
            result = isAddressPartEquals(address.getStreet1(), searchAddress.getStreet1())
                && isAddressPartEquals(address.getStreet2(), searchAddress.getStreet2())
                && isAddressPartEquals(address.getCity(), searchAddress.getCity())
                && isAddressPartEquals(address.getState(), searchAddress.getState()) && isAddressPartEquals(
                    address.getZip(), searchAddress.getZip());
        }

        return result;
    }

    private static boolean isAddressPartEquals(String addresspart, String searchAddresspart) {
        boolean result;
        if (NullChecker.isNullish(searchAddresspart)) {
            result = true;
        } else if (NullChecker.isNullish(addresspart)) {
            result = false;
        } else {
            result = addresspart.equalsIgnoreCase(searchAddresspart);
        }

        return result;
    }

    private boolean isPhoneNumberEquals(String telecom, String searchTelecom) {
        boolean result;

        if (NullChecker.isNullish(searchTelecom)) {
            result = true;
        } else if (NullChecker.isNullish(telecom)) {
            result = false;
        } else {
            result = isPhoneNumberStringEquals(telecom, searchTelecom);
        }

        return result;
    }

    private boolean isPhoneNumberStringEquals(String telecom, String searchTelecom) {
        boolean result = false;

        if (telecom.startsWith(TELEPHONE_PREFIX_STRING) && searchTelecom.startsWith(TELEPHONE_PREFIX_STRING)) {
            String compareTelecom = removeTelecomVisualSeparators(telecom);
            String compareSearchTelecom = removeTelecomVisualSeparators(searchTelecom);

            result = compareTelecom.equalsIgnoreCase(compareSearchTelecom);
        }

        return result;
    }

    private String removeTelecomVisualSeparators(String telecomIn) {
        return telecomIn.replaceAll("[-.()]", "");
    }

    private String serializePatientAddress(Patient patient) {
        String serializedString = "";
        if (CollectionUtils.isNotEmpty(patient.getAddresses())
            && patient.getAddresses().get(0) != null) {
            serializedString = patient.getAddresses().get(0).getStreet1() + ","
                + patient.getAddresses().get(0).getStreet2() + "," + patient.getAddresses().get(0).getCity() + ","
                + patient.getAddresses().get(0).getState() + "," + patient.getAddresses().get(0).getZip();
        }
        return serializedString;
    }

    private String serializePatient(Patient patient) {
        return patient.toString();
    }
}
