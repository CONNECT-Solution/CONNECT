/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
 * All rights reserved. 
 * Copyright (c) 2011, Conemaugh Valley Memorial Hospital
 * This source is subject to the Conemaugh public license.  Please see the
 * license.txt file for more information.
 * All other rights reserved.
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
package gov.hhs.fha.nhinc.assemblymanager.builder.cda.modules;

import org.hl7.v3.POCDMT000040Participant1;
import gov.hhs.fha.nhinc.assemblymanager.builder.DocumentBuilder;
import gov.hhs.fha.nhinc.assemblymanager.builder.DocumentBuilderException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.CE;
import org.hl7.v3.II;
import org.hl7.v3.PatientDemographicsPRPAMT201303UV02ResponseType;
import org.hl7.v3.POCDMT000040AssociatedEntity;
import org.hl7.v3.POCDMT000040Person;
import org.hl7.v3.PRPAMT201303UV02ContactParty;
import org.hl7.v3.TELExplicit;
import org.hl7.v3.PNExplicit;
import org.hl7.v3.COCTMT030207UVPerson;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import org.hl7.v3.IVLTSExplicit;
import org.hl7.v3.EnExplicitPrefix;
import org.hl7.v3.EnExplicitFamily;
import org.hl7.v3.EnExplicitGiven;
import org.hl7.v3.ADExplicit;

/**
 *
 * @author Nick
 */
public class ParticipantModule extends DocumentBuilder {

    private static Log log = LogFactory.getLog(ParticipantModule.class);
    private static String propertyFile = "SupportModule";
    private static String DATE_PROPERTY = "Date";
    private static String TYPE_PROPERTY = "ContactType";
    private static String PREFIX_PROPERTY = "ContactPrefix";
    private static String GIVEN_PROPERTY = "ContactGivenName";
    private static String GIVEN_QUALIFIER_PROPERTY = "ContactGivenQualifier";
    private static String FAMILY_PROPERTY = "ContactFamilyName";
    private static String FAMILY_QUALIFIER_PROPERTY = "ContactFamilyQualifier";
    private static String RELATIONSHIP_DISPLAY_NAME_PROPERTY = "ContactRelationshipDisplayName";
    private static String RELATIONSHIP_CODESYSTEM_NAME_PROPERTY = "ContactRelationshipCodeSystemName";
    private static String RELATIONSHIP_CODESYSTEM_PROPERTY = "ContactRelationshipCodeSystem";
    private static String RELATIONSHIP_CODE_PROPERTY = "ContactRelationshipCode";
    private static String STREET_ADRESS_LINE_PROPERTY = "ContactStreetAddressLine";
    private static String CITY_PROPERTY = "ContactCity";
    private static String STATE_PROPERTY = "ContactState";
    private static String POSTAL_CODE_PROPERTY = "ContactPostalCode";
    private static String COUNTRY_PROPERTY = "ContactCountry";
    private static String TELECOM_PROPERTY = "ContactTelecom";
    private static String TELECOMTYPE_PROPERTY = "ContactTelecomType";

    public POCDMT000040Participant1 build(PatientDemographicsPRPAMT201303UV02ResponseType response, POCDMT000040Participant1 participant) throws DocumentBuilderException {
        // set up the initial shell for the participant module
        participant.getTypeCode().add("IND");

        //  II templateID = new II();
        //  templateID.setRoot("2.16.840.1.113883.3.88.11.32.3");
        //  participant.getTemplateId().add(templateID);

        //HITSP/C83 Support modules templateId
        II hitspTemplateID = new II();
        hitspTemplateID.setRoot("2.16.840.1.113883.3.88.11.83.3");
        participant.getTemplateId().add(hitspTemplateID);

        //HITSP/C83 Support modules templateId
        II iheTemplateID = new II();
        iheTemplateID.setRoot("1.3.6.1.4.1.19376.1.5.3.1.2.4");
        participant.getTemplateId().add(iheTemplateID);

        // check the relevant fields of the response for emergency contact info
        PRPAMT201303UV02ContactParty contactParty = null;

        //If contact party section is not empty, then we have live data from which to get our emergency contact info
        if (response.getSubject() != null && response.getSubject().getPatientPerson() != null &&
            response.getSubject().getPatientPerson().getValue() != null &&
            response.getSubject().getPatientPerson().getValue().getContactParty() != null &&
            response.getSubject().getPatientPerson().getValue().getContactParty().size() > 0) {
            contactParty = response.getSubject().getPatientPerson().getValue().getContactParty().get(0);

            //Date: Required
            if (contactParty.getEffectiveTime() != null &&
                contactParty.getEffectiveTime().getValue() != null &&
                !contactParty.getEffectiveTime().getValue().equals("")) {
                participant.setTime(contactParty.getEffectiveTime());
            } else {
                log.warn("Participant support date is null.");
                IVLTSExplicit participantTime = new IVLTSExplicit();
                participantTime.getNullFlavor().add("UNK");
                //     IVXBTSExplicit lowVal = new IVXBTSExplicit();
                //    lowVal.getNullFlavor().add("UNK");
                //    participantTime.getContent().add(this.objectFactory.createIVLTSExplicitLow(lowVal));
                participant.setTime(participantTime);

            }

            participant.setAssociatedEntity(createAssociatedEntity(contactParty));
        } // The contactParty section is not available. Construct a default participant module from a properties file.
        else {
            log.info("Emergency Contatct Info not available from source system.  Will statically construct a participant module.");
            IVLTSExplicit time = objectFactory.createIVLTSExplicit();
            try {
                String timeValue = PropertyAccessor.getInstance().getProperty(propertyFile, DATE_PROPERTY);
                time.setValue(timeValue);
                participant.setTime(time);
            } catch (PropertyAccessException pae) {
                log.info("Could not access date property from properties file for participant module. Error is: " + pae + ". Inserting default value.");
                time.setValue("01/01/1800");
                participant.setTime(time);
            }
            participant.setAssociatedEntity(createStaticAssociatedEntity());
        }

        return participant;
    }

    public POCDMT000040AssociatedEntity createAssociatedEntity(PRPAMT201303UV02ContactParty contactParty) {
        POCDMT000040AssociatedEntity associatedEntity = objectFactory.createPOCDMT000040AssociatedEntity();

        //Contact Type: Required

        if (contactParty.getClassCode() != null && contactParty.getClassCode().value() != null && !(contactParty.getClassCode().value().equals(""))) {
            associatedEntity.getClassCode().add(contactParty.getClassCode().value());
        } else {
            log.warn("Contact Type, a required field, is missing from the source system. The field will not be populated in the CDA.");
        }

        //Contact Relationship: Required if known

        CE contactRelationship = contactParty.getCode();
        if (contactRelationship != null) {
            associatedEntity.setCode(contactRelationship);
        }

        //contact address: Required if Known
        if (contactParty.getAddr() != null && contactParty.getAddr().size() > 0) {
            for (ADExplicit contactAddress : contactParty.getAddr()) {
                associatedEntity.getAddr().add(contactAddress);
            }
        } else {
            //addr is a required tag - set nullFlavor if unknown
            ADExplicit adexp = objectFactory.createADExplicit();
            adexp.getNullFlavor().add("UNK");
            associatedEntity.getAddr().add(adexp);
        }

        //Contact phone/email/telecom: Required if known
        if (contactParty.getTelecom() != null && contactParty.getTelecom().size() > 0) {
            for (TELExplicit telecom : contactParty.getTelecom()) {
                associatedEntity.getTelecom().add(telecom);
            }
        }

        if (contactParty.getContactPerson() != null && contactParty.getContactPerson().getValue() != null && contactParty.getContactPerson().getValue().getName() != null && contactParty.getContactPerson().getValue().getName().size() > 0) {
            associatedEntity.setAssociatedPerson(createAssociatedPerson(contactParty.getContactPerson().getValue()));
        } else {
            log.warn("Contact name, a required field, is missing from the source system. The field will not be populated in the CDA.");
        }

        return associatedEntity;
    }

    public POCDMT000040Person createAssociatedPerson(COCTMT030207UVPerson contactPerson) {
        //Contact Name: Required
        POCDMT000040Person associatedPerson = objectFactory.createPOCDMT000040Person();

        for (PNExplicit nameExplicit : contactPerson.getName()) {
            associatedPerson.getName().add(nameExplicit);
        }
        return associatedPerson;
    }

    public POCDMT000040AssociatedEntity createStaticAssociatedEntity() {
        POCDMT000040AssociatedEntity staticAssociatedEntity = objectFactory.createPOCDMT000040AssociatedEntity();
        //try to access the properties file. If successfull, fill in the fields that the file has to offer. Otherwise, fill in default values

        // contact type
        String classCode = "";

        try {
            classCode = PropertyAccessor.getInstance().getProperty(propertyFile, TYPE_PROPERTY);
        } catch (PropertyAccessException pae) {
            log.info("Could not access contact type property from properties file for participant module. Error is: " + pae + ". Inserting default value.");
            classCode = "ECON";
        }
        staticAssociatedEntity.getClassCode().add(classCode);

        // contact name
        PNExplicit staticName = objectFactory.createPNExplicit();

        String prefixValue = "";
        String givenValue = "";
        String familyValue = "";
        String givenQualifierValue = "";
        String familyQualifierValue = "";

        try {
            prefixValue = PropertyAccessor.getInstance().getProperty(propertyFile, PREFIX_PROPERTY);
            givenValue = PropertyAccessor.getInstance().getProperty(propertyFile, GIVEN_PROPERTY);
            familyValue = PropertyAccessor.getInstance().getProperty(propertyFile, FAMILY_PROPERTY);
            givenQualifierValue = PropertyAccessor.getInstance().getProperty(propertyFile, GIVEN_QUALIFIER_PROPERTY);
            familyQualifierValue = PropertyAccessor.getInstance().getProperty(propertyFile, FAMILY_QUALIFIER_PROPERTY);
        } catch (PropertyAccessException pae) {
            log.info("Could not load name property from properties file. Filling with default values");
            prefixValue = "Mrs";
            givenValue = "Sample";
            familyValue = "Name";
            givenQualifierValue = "CL";
            familyQualifierValue = "BR";
        }

        if (prefixValue.equals("") || givenValue.equals("") || familyValue.equals("") || givenQualifierValue.equals("") || familyQualifierValue.equals("")) {
            log.warn("Insufficient data from properties file for contact name, a required field. The component will be added with default values");
            prefixValue = "Mrs";
            givenValue = "Sample";
            familyValue = "Name";
            givenQualifierValue = "CL";
            familyQualifierValue = "BR";
        }

        EnExplicitPrefix prefix = objectFactory.createEnExplicitPrefix();
        prefix.setContent(prefixValue);
        EnExplicitGiven given = objectFactory.createEnExplicitGiven();
        given.setContent(givenValue);
        given.getQualifier().add(givenQualifierValue);
        EnExplicitFamily family = objectFactory.createEnExplicitFamily();
        family.setContent(familyValue);
        family.getQualifier().add(familyQualifierValue);

        java.util.List nameList = staticName.getContent();
        nameList.add(objectFactory.createENExplicitPrefix(prefix));
        nameList.add(objectFactory.createENExplicitGiven(given));
        nameList.add(objectFactory.createENExplicitFamily(family));

        POCDMT000040Person staticAssociatedPerson = objectFactory.createPOCDMT000040Person();

        staticAssociatedPerson.getName().add(staticName);

        staticAssociatedEntity.setAssociatedPerson(staticAssociatedPerson);

        //contact relationship
        CE staticRelationshipCode = new CE();

        String relationshipDisplayNameValue = "";
        String relationshipCodeSystemValue = "";
        String relationshipCodeSystemNameValue = "";
        String relationshipCodeValue = "";

        try {
            relationshipDisplayNameValue = PropertyAccessor.getInstance().getProperty(propertyFile, RELATIONSHIP_DISPLAY_NAME_PROPERTY);
            relationshipCodeSystemValue = PropertyAccessor.getInstance().getProperty(propertyFile, RELATIONSHIP_CODESYSTEM_PROPERTY);
            relationshipCodeSystemNameValue = PropertyAccessor.getInstance().getProperty(propertyFile, RELATIONSHIP_CODESYSTEM_NAME_PROPERTY);
            relationshipCodeValue = PropertyAccessor.getInstance().getProperty(propertyFile, RELATIONSHIP_CODE_PROPERTY);
        } catch (PropertyAccessException pae) {
            log.debug("Could not retrieve info for contact relationship in support module. The component will not be added");
        }

        if (relationshipDisplayNameValue.equals("") || relationshipCodeSystemValue.equals("") || relationshipCodeSystemNameValue.equals("") || relationshipCodeValue.equals("")) {
            log.debug("Insufficient data from properties file for contact relationship in support module. The component will not be added");
        } else {
            staticRelationshipCode.setCode(relationshipCodeValue);
            staticRelationshipCode.setCodeSystem(relationshipCodeSystemValue);
            staticRelationshipCode.setCodeSystemName(relationshipCodeSystemNameValue);
            staticRelationshipCode.setDisplayName(relationshipDisplayNameValue);
            staticAssociatedEntity.setCode(staticRelationshipCode);
        }

        //contact address

        String streetValue = "";
        String cityValue = "";
        String stateValue = "";
        String postalValue = "";
        String countryValue = "";

        try {
            streetValue = PropertyAccessor.getInstance().getProperty(propertyFile, STREET_ADRESS_LINE_PROPERTY);
            cityValue = PropertyAccessor.getInstance().getProperty(propertyFile, CITY_PROPERTY);
            stateValue = PropertyAccessor.getInstance().getProperty(propertyFile, STATE_PROPERTY);
            postalValue = PropertyAccessor.getInstance().getProperty(propertyFile, POSTAL_CODE_PROPERTY);
            countryValue = PropertyAccessor.getInstance().getProperty(propertyFile, COUNTRY_PROPERTY);
        } catch (PropertyAccessException pae) {
            log.debug("Could not retrieve info for contact address in support module. The component will not be added");
        }

        if (streetValue.equals("") || cityValue.equals("") || stateValue.equals("") || postalValue.equals("") || countryValue.equals("")) {
            log.debug("Insufficient data from properties file for contact address in support module. The component will not be added");
        } else {
            ADExplicit staticAddress = objectFactory.createADExplicit();

            java.util.List addrList = staticAddress.getContent();

            org.hl7.v3.AdxpExplicitStreetAddressLine street = objectFactory.createAdxpExplicitStreetAddressLine();

            street.setContent(streetValue);
            addrList.add(objectFactory.createADExplicitStreetAddressLine(street));

            org.hl7.v3.AdxpExplicitCity city = new org.hl7.v3.AdxpExplicitCity();
            city.setContent(cityValue);
            addrList.add(objectFactory.createADExplicitCity(city));

            org.hl7.v3.AdxpExplicitState state = new org.hl7.v3.AdxpExplicitState();
            state.setContent(stateValue);
            addrList.add(objectFactory.createADExplicitState(state));

            org.hl7.v3.AdxpExplicitPostalCode postal = new org.hl7.v3.AdxpExplicitPostalCode();
            postal.setContent(postalValue);
            addrList.add(objectFactory.createADExplicitPostalCode(postal));

            org.hl7.v3.AdxpExplicitCountry country = new org.hl7.v3.AdxpExplicitCountry();
            country.setContent(countryValue);
            addrList.add(objectFactory.createADExplicitCountry(country));

            staticAssociatedEntity.getAddr().add(staticAddress);
        }

        // contact phone/email/url
        String telValue = "";
        String telUseValue = "";

        try {
            telValue = PropertyAccessor.getInstance().getProperty(propertyFile, TELECOM_PROPERTY);
            telUseValue = PropertyAccessor.getInstance().getProperty(propertyFile, TELECOMTYPE_PROPERTY);
        } catch (PropertyAccessException pae) {
            log.debug("Could not retrieve info for contact telecom in support module. The component will not be added");
        }

        if (telValue.equals("") || telUseValue.equals("")) {
            log.debug("Insufficient data from properties file for contact telecom in support module. The component will not be added");
        } else {
            org.hl7.v3.TELExplicit telecom = new org.hl7.v3.TELExplicit();
            telecom.setValue(telValue);
            telecom.getUse().add(telUseValue);

            staticAssociatedEntity.getTelecom().add(telecom);
        }

        return staticAssociatedEntity;
    }
}
