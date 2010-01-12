/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.adapter.commondatalayer;

import java.util.List;
import org.hl7.v3.ADExplicit;
import org.hl7.v3.AdxpExplicitCity;
import org.hl7.v3.AdxpExplicitCountry;
import org.hl7.v3.AdxpExplicitPostalCode;
import org.hl7.v3.AdxpExplicitState;
import org.hl7.v3.AdxpExplicitStreetAddressLine;
import org.hl7.v3.BL;
import org.hl7.v3.CE;
import org.hl7.v3.COCTMT030207UVPerson;
import org.hl7.v3.COCTMT150003UV03Organization;
import org.hl7.v3.EnExplicitFamily;
import org.hl7.v3.EnExplicitGiven;
import org.hl7.v3.II;
import org.hl7.v3.IVLTSExplicit;
import org.hl7.v3.ONExplicit;
import org.hl7.v3.PNExplicit;
import org.hl7.v3.PRPAMT201303UV02ContactParty;
import org.hl7.v3.PRPAMT201303UV02LanguageCommunication;
import org.hl7.v3.PRPAMT201303UV02Patient;
import org.hl7.v3.PRPAMT201303UV02Person;
import org.hl7.v3.PatientDemographicsPRPAMT201303UV02ResponseType;
import org.hl7.v3.TELExplicit;
import org.hl7.v3.TSExplicit;

/**
 *
 * @author kim
 */
public class PDResponseHelper {

   private static org.hl7.v3.ObjectFactory factory = new org.hl7.v3.ObjectFactory();

   public static PatientDemographicsPRPAMT201303UV02ResponseType createPatientDemographicsResponse(II subjectId) {
      PatientDemographicsPRPAMT201303UV02ResponseType msg = new PatientDemographicsPRPAMT201303UV02ResponseType();

      msg.setSubject(createSubject(subjectId));

      return msg;
   }

   private static PRPAMT201303UV02Patient createSubject(II subjectId) {
      PRPAMT201303UV02Patient subject = new PRPAMT201303UV02Patient();

      subject.getClassCode().add("PAT");
      subject.getId().add(subjectId);

      IVLTSExplicit effectiveTime = new IVLTSExplicit();
      effectiveTime.setValue("20090202000000");
      subject.setEffectiveTime(effectiveTime);

      subject.getAddr().add(createAddress("17 Daws Road", "Blue Bell", "MA", "00000", "US"));

      subject.setPatientPerson(factory.createPRPAMT201303UV02PatientPatientPerson(createPatientPerson("Josephine", "Ross", subjectId)));

      COCTMT150003UV03Organization providerOrg = new COCTMT150003UV03Organization();
      II providerOrgId = new II();
      providerOrgId.setRoot("2.16.840.1.113883.3.198");
      providerOrg.getId().add(providerOrgId);

      List<ONExplicit> providerOrgNames = providerOrg.getName();
      ONExplicit providerOrgName = (ONExplicit) (factory.createONExplicit());
      providerOrgName.getContent().add("Department of Defense");
      providerOrgNames.add(providerOrgName);

      subject.setProviderOrganization(factory.createPRPAMT201303UV02PatientProviderOrganization(providerOrg));

      return subject;
   }

   private static ADExplicit createAddress(String streetName, String city, String state, String zip, String country) {
      ADExplicit addr = (ADExplicit) (factory.createADExplicit());
      addr.getUse().add("HP");

      List addrlist = addr.getContent();
      if (streetName != null && streetName.length() > 0) {
         AdxpExplicitStreetAddressLine streetAddrLine = new AdxpExplicitStreetAddressLine();
         streetAddrLine.setContent(streetName);
         addrlist.add(factory.createADExplicitStreetAddressLine(streetAddrLine));
      }

      if (city != null && city.length() > 0) {
         AdxpExplicitCity aCity = new AdxpExplicitCity();
         aCity.setContent(city);
         addrlist.add(factory.createADExplicitCity(aCity));
      }

      if (state != null && state.length() > 0) {
         AdxpExplicitState aState = new AdxpExplicitState();
         aState.setContent(state);
         addrlist.add(factory.createADExplicitState(aState));
      }

      if (zip != null && zip.length() > 0) {
         AdxpExplicitPostalCode postalCode = new AdxpExplicitPostalCode();
         postalCode.setContent(zip);
         addrlist.add(factory.createADExplicitPostalCode(postalCode));
      }

      if (country != null && country.length() > 0) {
         AdxpExplicitCountry aCountry = new AdxpExplicitCountry();
         aCountry.setContent(country);
         addrlist.add(factory.createADExplicitCountry(aCountry));
      }

      return addr;
   }

   private static PRPAMT201303UV02Person createPatientPerson(String firstName, String familyName, II subjectId) {
      PRPAMT201303UV02Person person = new PRPAMT201303UV02Person();
      person.getName().add(createName(firstName, familyName));

      CE maritalStatus = new CE();
      maritalStatus.setCode("M");
      maritalStatus.setDisplayName("Married");
      maritalStatus.setCodeSystem("2.16.840.1.113883.5.2");
      maritalStatus.setCodeSystemName("MaritalStatusCode");
      person.setMaritalStatusCode(maritalStatus);

      CE religion = new CE();
      religion.setCode("1022");
      religion.setDisplayName("Independent");
      religion.setCodeSystem("2.16.840.1.113883.5.1076");
      religion.setCodeSystemName("ReligiousAffiliation");
      person.setReligiousAffiliationCode(religion);

      CE race = new CE();
      race.setCode("1004-1");
      race.setCodeSystemName("American Indian");
      race.setCodeSystem("2.16.840.1.113883.6.238");
      race.setCodeSystemName("CDC Race and Ethnicity");
      person.getRaceCode().add(race);

      CE ethnic = new CE();
      ethnic.setCode("2178-2");
      ethnic.setDisplayName("Latin American");
      ethnic.setCodeSystem("2.16.840.1.113883.6.238");
      ethnic.setCodeSystemName("CDC Race and Ethnicity");
      person.getEthnicGroupCode().add(ethnic);

      person.getLanguageCommunication().add(createLanguage("en-US"));

      TSExplicit birthTime = new TSExplicit();
      birthTime.setValue("19761212");
      person.setBirthTime(birthTime);

      person.getContactParty().add(createContactParty("Rosa", "Ross", "STPDAU", "Step-Daughter"));

      return person;
   }

   private static PRPAMT201303UV02ContactParty createContactParty(String firstName, String lastName, String relCode, String relDesc) {
      PRPAMT201303UV02ContactParty party = new PRPAMT201303UV02ContactParty();
      party.setClassCode(org.hl7.v3.RoleClassContact.ECON);

      CE code = new CE();
      code.setCode(relCode);
      code.setDisplayName(relDesc);
      code.setCodeSystemName("RoleCode");
      code.setCodeSystem("2.16.840.1.113883.5.111");
      party.setCode(code);

      party.getTelecom().add(createTelecom("tel:+1-999-999-9999", "HP"));

      party.setContactPerson(factory.createPRPAMT201303UV02ContactPartyContactPerson(createPerson(firstName, lastName)));

      return party;
   }

   private static COCTMT030207UVPerson createPerson(String firstName, String lastName) {
      COCTMT030207UVPerson person = new COCTMT030207UVPerson();
      person.getName().add(createName(firstName, lastName));
      return person;
   }

   private static PNExplicit createName(String firstName, String lastName) {
      PNExplicit name = (PNExplicit) (factory.createPNExplicit());
      name.getUse().add("P");

      List namelist = name.getContent();
      if (lastName != null && lastName.length() > 0) {
         EnExplicitFamily familyName = new EnExplicitFamily();
         familyName.setPartType("FAM");
         familyName.setContent(lastName);
         namelist.add(factory.createENExplicitFamily(familyName));
      }

      if (firstName != null && firstName.length() > 0) {
         EnExplicitGiven givenName = new EnExplicitGiven();
         givenName.setPartType("GIV");
         givenName.setContent(firstName);
         namelist.add(factory.createENExplicitGiven(givenName));
      }

      return name;
   }

   private static TELExplicit createTelecom(String phoneNumber, String type) {
      TELExplicit telecom = (TELExplicit) (factory.createTELExplicit());
      telecom.getUse().add(type);
      telecom.setValue(phoneNumber);

      return telecom;
   }

   private static PRPAMT201303UV02LanguageCommunication createLanguage(String type) {
      PRPAMT201303UV02LanguageCommunication communication = new PRPAMT201303UV02LanguageCommunication();

      II templateId = new II();
      templateId.setRoot("2.16.840.1.113883.3.88.11.83.2");

      CE code = new CE();
      code.setCode(type);
      communication.setLanguageCode(code);

      BL preference = new BL();
      preference.setValue(true);
      communication.setPreferenceInd(preference);

      return communication;
   }
}
