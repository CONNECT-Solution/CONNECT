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

import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * @author akong
 *
 */
public class PatientMatcherTest {

    Mockery context = new JUnit4Mockery() {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };

    @Test
    public void testGetInstance() {
        PatientMatcher patientMatcher = PatientMatcher.getInstance();
        assertNotNull(patientMatcher);

        PatientMatcher patientMatcher2 = PatientMatcher.getInstance();
        assertEquals(patientMatcher, patientMatcher2);
    }

    @Test
    public void testIsPatientOptedInCriteriaMet() {
        PatientMatcher patientMatcher = createPatientMatcher();

        Patient patient = createPatient();
        patient.setOptedIn(false);
        assertFalse(patientMatcher.isPatientOptedInCriteriaMet(patient));
    }

    @Test
    public void testHasMatchByIds_Success() {
        PatientMatcher patientMatcher = createPatientMatcher();

        Patient patient = createPatient();
        addIdentifierToPatient(patient, "12345", "1.1");
        addIdentifierToPatient(patient, "qwerty", "2.2");

        Patient searchParams = new Patient();
        addIdentifierToPatient(searchParams, "qwerty", "2.2");

        assertTrue(patientMatcher.hasMatchByIds(patient, searchParams));
    }

    @Test
    public void testHasMatchByIds_None() {
        PatientMatcher patientMatcher = createPatientMatcher();

        Patient patient = createPatient("Joe", "Schmoe", "M", "01011950", "555-555-5555");
        addIdentifierToPatient(patient, "12345", "1.1");
        addIdentifierToPatient(patient, "qwerty", "2.2");

        Patient searchParams = new Patient();
        addIdentifierToPatient(searchParams, "qwerty", "3.3");

        assertFalse(patientMatcher.hasMatchByIds(patient, searchParams));
    }

    @Test
    public void testNameEmpty() {
        PatientMatcher patientMatcher = createPatientMatcher();

        Patient patient = createPatient();
        Patient searchParams = createPatient();

        searchParams.getNames().remove(0);
        assertFalse(patientMatcher.hasMatchByDemographics(patient, searchParams));

        patient.getNames().remove(0);
        assertFalse(patientMatcher.hasMatchByDemographics(patient, searchParams));
    }

    @Test
    public void testHasMatchByDemographics_FirstName() {
        PatientMatcher patientMatcher = createPatientMatcher();

        Patient patient = createPatient();
        Patient searchParams = createPatient();
        assertTrue(patientMatcher.hasMatchByDemographics(patient, searchParams));

        searchParams.getNames().get(0).setFirstName("Bob");
        assertFalse(patientMatcher.hasMatchByDemographics(patient, searchParams));

        searchParams.getNames().get(0).setFirstName(null);
        assertFalse(patientMatcher.hasMatchByDemographics(patient, searchParams));

        patient.getNames().get(0).setFirstName(null);
        assertFalse(patientMatcher.hasMatchByDemographics(patient, searchParams));
    }

    @Test
    public void testHasMatchByDemographics_LastName() {
        PatientMatcher patientMatcher = createPatientMatcher();

        Patient patient = createPatient();
        Patient searchParams = createPatient();
        assertTrue(patientMatcher.hasMatchByDemographics(patient, searchParams));

        searchParams.getNames().get(0).setLastName("Bob");
        assertFalse(patientMatcher.hasMatchByDemographics(patient, searchParams));

        searchParams = createPatient();
        searchParams.getNames().get(0).setLastName(null);
        assertFalse(patientMatcher.hasMatchByDemographics(patient, searchParams));

        patient.getNames().get(0).setLastName(null);
        assertFalse(patientMatcher.hasMatchByDemographics(patient, searchParams));
    }

    @Test
    public void testHasMatchByDemographics_Gender() {
        PatientMatcher patientMatcher = createPatientMatcher();

        Patient patient = createPatient();
        Patient searchParams = createPatient();
        assertTrue(patientMatcher.hasMatchByDemographics(patient, searchParams));

        searchParams.setGender("F");
        assertFalse(patientMatcher.hasMatchByDemographics(patient, searchParams));

        searchParams.setGender(null);
        assertTrue(patientMatcher.hasMatchByDemographics(patient, searchParams));

        patient.setGender(null);
        searchParams = createPatient();
        assertFalse(patientMatcher.hasMatchByDemographics(patient, searchParams));
    }

    @Test
    public void testHasMatchByDemographics_Birthdate() {
        PatientMatcher patientMatcher = createPatientMatcher();

        Patient patient = createPatient();
        Patient searchParams = createPatient();
        assertTrue(patientMatcher.hasMatchByDemographics(patient, searchParams));

        searchParams.setDateOfBirth("02111950");
        assertFalse(patientMatcher.hasMatchByDemographics(patient, searchParams));

        searchParams.setDateOfBirth(null);
        assertTrue(patientMatcher.hasMatchByDemographics(patient, searchParams));

        patient.setDateOfBirth(null);
        searchParams = createPatient();
        assertFalse(patientMatcher.hasMatchByDemographics(patient, searchParams));
    }

    @Test
    public void testHasMatchByDemographics_TelephoneNumbers() {
        PatientMatcher patientMatcher = createPatientMatcher();

        Patient patient = createPatient();
        Patient searchParams = createPatient();
        assertTrue(patientMatcher.hasMatchByDemographics(patient, searchParams));

        searchParams.getPhoneNumbers().get(0).setPhoneNumber("tel:444-555-5555");
        assertFalse(patientMatcher.hasMatchByDemographics(patient, searchParams));

        patient.getPhoneNumbers().get(0).setPhoneNumber("555-555-5555");
        searchParams.getPhoneNumbers().get(0).setPhoneNumber("555-555-5555");
        assertFalse(patientMatcher.hasMatchByDemographics(patient, searchParams));

        patient.getPhoneNumbers().get(0).setPhoneNumber(null);
        searchParams = createPatient();
        assertFalse(patientMatcher.hasMatchByDemographics(patient, searchParams));

        patient = createPatient();
        searchParams.getPhoneNumbers().get(0).setPhoneNumber(null);
        assertTrue(patientMatcher.hasMatchByDemographics(patient, searchParams));
    }

    @Test
    public void testHasMatchByDemographics_Addresses() {
        PatientMatcher patientMatcher = createPatientMatcher();

        Patient patient = createPatient();
        addAddressesToPatient(patient, "123 Street", "Dr.", "city", "nowhere", "12345");
        Patient searchParams = createPatient();
        addAddressesToPatient(searchParams, "123 Street", "Dr.", "city", "nowhere", "12345");
        assertTrue(patientMatcher.hasMatchByDemographics(patient, searchParams));

        searchParams = createPatient();
        addAddressesToPatient(searchParams, "567 Street", "Dr.", "city", "nowhere", "12345");
        assertFalse(patientMatcher.hasMatchByDemographics(patient, searchParams));

        patient = createPatient();
        addAddressesToPatient(patient, "123 Street", null, "city", "nowhere", "12345");
        searchParams = createPatient();
        addAddressesToPatient(searchParams, "123 Street", "Dr.", "city", "nowhere", "12345");
        assertFalse(patientMatcher.hasMatchByDemographics(patient, searchParams));
    }

    protected Patient createPatient() {
        return createPatient("Joe", "Schmoe", "M", "01011950", "tel:555-555-5555");
    }

    protected Patient createPatient(String firstName, String lastName, String gender, String dob, String phone) {
        Patient patient = new Patient();

        PersonName name = new PersonName();
        name.setFirstName(firstName);
        name.setLastName(lastName);
        patient.getNames().add(name);

        patient.setGender(gender);

        patient.setDateOfBirth(dob);

        PhoneNumber phoneNum = new PhoneNumber();
        phoneNum.setPhoneNumber(phone);
        PhoneNumbers phoneNums = new PhoneNumbers();
        phoneNums.add(phoneNum);
        patient.setPhoneNumbers(phoneNums);

        return patient;
    }

    protected void addAddressesToPatient(Patient patient, String street1, String street2, String city, String state, String zip) {
        Address address = new Address();

        address.setStreet1(street1);
        address.setStreet2(street2);
        address.setCity(city);
        address.setState(state);
        address.setZip(zip);

        if (patient.getAddresses() == null) {
            Addresses addresses = new Addresses();
            addresses.add(address);

            patient.setAddresses(addresses);
        } else {
            patient.getAddresses().add(address);
        }
    }

    protected void addIdentifierToPatient(Patient patient, String id, String orgId) {

        Identifier identifier = new Identifier();
        identifier.setId(id);
        identifier.setOrganizationId(orgId);

        if (patient.getIdentifiers() == null) {
            Identifiers identifiers = new Identifiers();
            identifiers.add(identifier);

            patient.setIdentifiers(identifiers);
        } else {
            patient.getIdentifiers().add(identifier);
        }
    }

    private PatientMatcher createPatientMatcher() {
        return new PatientMatcher();
    }
}
