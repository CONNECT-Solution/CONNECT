/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.adapter.commondatalayer.mappers;

import gov.hhs.fha.nhinc.adapter.commondatalayer.parsers.PRPAIN201305UVParser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.ADExplicit;
import org.hl7.v3.CE;
import org.hl7.v3.EnExplicitFamily;
import org.hl7.v3.EnExplicitGiven;
import org.hl7.v3.FindPatientsPRPAIN201305UV02RequestType;
import org.hl7.v3.FindPatientsPRPAMT201310UV02ResponseType;
import org.hl7.v3.II;
import org.hl7.v3.ObjectFactory;
import org.hl7.v3.PNExplicit;
import org.hl7.v3.PRPAIN201305UV02MCCIMT000100UV01Message;
import org.hl7.v3.PRPAMT201310UV02Patient;
import org.hl7.v3.PRPAMT201310UV02Person;
import org.hl7.v3.TSExplicit;

/**
 *
 * @author kim
 */
public class StaticFindPatientsQuery {

   private static Log logger = LogFactory.getLog(StaticFindPatientsQuery.class);
   private static ObjectFactory factory = new ObjectFactory();

   public static FindPatientsPRPAMT201310UV02ResponseType createFindPatientsResponse(FindPatientsPRPAIN201305UV02RequestType request) {
      FindPatientsPRPAMT201310UV02ResponseType response = new FindPatientsPRPAMT201310UV02ResponseType();
      String ptDob = null;
      String ptFirstName = null;
      String ptLastName = null;
      String ptGender = null;

      if (request != null) {
         PRPAIN201305UV02MCCIMT000100UV01Message query = request.getQuery();
         if (query != null) {
            PRPAIN201305UVParser parser = new PRPAIN201305UVParser(query);

            // get patient's date of birth (if any)
            ptDob = parser.getDateOfBirth();

            // get patient's first name
            ptLastName = parser.getSubjectLastName();

            // get patient's first name
            ptFirstName = parser.getSubjectFirstName();

            // get patient's gender
            ptGender = parser.getSubjectGender();

            response = createResponse(ptFirstName, ptLastName, ptDob, ptGender);
         }
      }

      return response;
   }

   private static FindPatientsPRPAMT201310UV02ResponseType createResponse(String firstName, String lastName, String dob, String gender) {
      FindPatientsPRPAMT201310UV02ResponseType response = new FindPatientsPRPAMT201310UV02ResponseType();
      PRPAMT201310UV02Patient patient = createPatient(firstName, lastName, dob, gender);
      patient.getClassCode().add("PAT");
      response.getSubject().add(patient);
      return response;
   }

   private static PRPAMT201310UV02Patient createPatient(String firstName, String lastName, String dob, String gender) {
      PRPAMT201310UV02Patient patient = new PRPAMT201310UV02Patient();
      
      II id = new II();
      id.setExtension("8237363");
      patient.getId().add(id);

      ADExplicit homeAdress = StaticUtil.createAddress("17 Daws Road", "Blue Bell", "MA", "00000", "US");
      patient.getAddr().add(homeAdress);

      patient.getTelecom().add(StaticUtil.createTelecom("tel:+1-999-999-9999", "HP"));

      patient.setPatientPerson(factory.createPRPAMT201310UV02PatientPatientPerson(createPatientPerson(firstName, lastName, dob, gender)));

      return patient;
   }

   private static PRPAMT201310UV02Person createPatientPerson(String firstName, String lastName, String dob, String gender) {
      PRPAMT201310UV02Person person = new PRPAMT201310UV02Person();

      // -------- Patient's name -------
      PNExplicit personName = new PNExplicit();
      EnExplicitFamily familyName = new EnExplicitFamily();
      familyName.setContent(lastName);
      personName.getContent().add(factory.createENExplicitFamily(familyName));
      
      EnExplicitGiven givenName = new EnExplicitGiven();
      givenName.setContent(firstName);
      personName.getContent().add(factory.createENExplicitGiven(givenName));

      personName.getUse().add("P");
      person.getName().add(personName);
      
      // -------- Patient's gender -------
      CE personGender = new CE();
      personGender.setCode(gender);
      person.setAdministrativeGenderCode(personGender);

      // -------- Birth Date -------
      TSExplicit birthTime = new TSExplicit();
      birthTime.setValue(dob);
      person.setBirthTime(birthTime);

      return person;
   }
}
