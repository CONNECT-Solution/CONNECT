/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.muralmpi;
import com.sun.mdm.index.webservice.SearchPatientResult;
import com.sun.mdm.index.webservice.PatientBean;
import com.sun.mdm.index.webservice.EnterprisePatient;
import com.sun.mdm.index.webservice.SystemPatient;
import org.hl7.v3.PRPAMT201301UV02Patient;
import org.hl7.v3.PRPAMT201306UV02ParameterList;
import org.hl7.v3.PRPAMT201306UV02QueryByParameter;
import org.hl7.v3.PRPAIN201305UV02QUQIMT021001UV01ControlActProcess;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectAdministrativeGender;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectName;
import org.hl7.v3.EnExplicitGiven;
import org.hl7.v3.EnExplicitFamily;
import org.hl7.v3.ENXPExplicit;
import org.hl7.v3.IVLTSExplicit;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectBirthTime;
import org.hl7.v3.CE;
import  gov.hhs.fha.nhinc.transform.subdisc.HL7PatientTransforms;
import  gov.hhs.fha.nhinc.transform.subdisc.HL7PRPA201306Transforms;
import org.hl7.v3.PRPAMT201301UV02Patient;
import org.hl7.v3.PRPAMT201301UV02Person;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.List;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.Serializable;
import java.util.Iterator;
import javax.xml.bind.JAXBElement;
import gov.hhs.fha.nhinc.muralmpi.PersonName;
/**
 *
 * @author dunnek
 */
public class HL7Parser {
     private static Log log = LogFactory.getLog(HL7Parser.class);
     
    public static JAXBElement<PRPAMT201301UV02Person>  createPatientPerson(PatientBean patient)
    {

        JAXBElement<PRPAMT201301UV02Person> result;
        log.debug("begin createPatientPerson");
        String patFirstName;
        String patLastName;
        String gender;
        String birthTime;
        String ssn;

        patFirstName = patient.getFirstName().trim();
        patLastName = patient.getLastName().trim();
        gender = patient.getGender();
        birthTime = convertMuralDateToHL7Date(patient.getDOB());
        ssn= patient.getSSN();
        log.debug("firstname = " + patFirstName + "; lastName = " + patLastName
                + "; gender = " + gender + "; birthtime = " + birthTime +
                "; ssn = " + ssn);
        result = HL7PatientTransforms.create201301PatientPerson(patFirstName, patLastName,gender, birthTime, ssn);

        log.debug("end createPatientPerson");
        return result;

    }
    private static String convertFormattedDates(String date, String fromFormat, String toFormat)
    {
        SimpleDateFormat fromDF = new SimpleDateFormat(fromFormat);
        SimpleDateFormat toDF = new SimpleDateFormat(toFormat);
        Date dte;
        String result = "";

        try
        {
             dte = fromDF.parse(date);
             result = toDF.format(dte);
        }
        catch(Exception ex)
        {

        }

        return result;
    }
    private static String convertMuralDateToHL7Date(String date)
    {
        SimpleDateFormat hl7DateFormat = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat muralDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date dob;
        String result = convertFormattedDates(date,"MM/dd/yyyy", "yyyyMMdd" );

        return result;
    }
    public static PRPAIN201306UV02 createPRPA201306(SearchPatientResult muralPatient, PRPAIN201305UV02 query, String localId, String localAAID, String localOID)
    {
        log.debug("Begin createPRPA201306");
        JAXBElement<PRPAMT201301UV02Person> person;
        String senderOID = "";

        person = createPatientPerson(muralPatient.getPatient());
        log.debug("Created HL7 Person");
        
        log.debug("mural patient id = " + localId );
        PRPAMT201301UV02Patient hl7Patient;
        hl7Patient = HL7PatientTransforms.create201301Patient(person, localId, localAAID);
        log.debug("Created HL7 patient");

        log.debug("end createPRPA201306");

        senderOID = query.getSender().getDevice().getId().get(0).getRoot();

        log.info("senderOID = " + senderOID);
        return HL7PRPA201306Transforms.createPRPA201306(hl7Patient, senderOID, localAAID, localOID, localId, query);
    }
   
    public static String ExtractGender(PRPAMT201306UV02ParameterList params) {
        log.debug("Entering HL7Parser201305.ExtractGender method...");

        String genderCode = "";

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

        log.debug("Exiting HL7Parser201305.ExtractGender method...");
        return genderCode;
    }

    public static String ExtractBirthdate(PRPAMT201306UV02ParameterList params) {
        log.debug("Entering HL7Parser201305.ExtractBirthdate method...");

        String birthDate = "";
        java.util.Date dob;
        SimpleDateFormat hl7DateFormat = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat muralDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        
        // Extract the birth time from the query parameters - Assume only one was specified
        if (params.getLivingSubjectBirthTime() != null &&
                params.getLivingSubjectBirthTime().size() > 0 &&
                params.getLivingSubjectBirthTime().get(0) != null) {
            PRPAMT201306UV02LivingSubjectBirthTime birthTime = params.getLivingSubjectBirthTime().get(0);

            if (birthTime.getValue() != null &&
                    birthTime.getValue().size() > 0 &&
                    birthTime.getValue().get(0) != null) {
                IVLTSExplicit birthday = birthTime.getValue().get(0);
                log.debug("Found birthTime in query parameters = " + birthday.getValue());
                
                try
                {
                    dob = hl7DateFormat.parse(birthday.getValue());
                    log.debug("Extracted dob = " + dob.toString());
                    birthDate = muralDateFormat.format(dob);
                    log.debug("Extracted birthTime in query parameters = " + birthDate);
                }
                catch (Exception ex)
                {
                    log.error("unable to extract birthdate");
                }
                
            } else {
                log.debug("message does not contain a birthtime");
            }
        } else {
            log.debug("message does not contain a birthtime");
        }

        log.debug("Exiting HL7Parser201305.ExtractBirthdate method...");
        return birthDate;
    }
    
    public static PatientBean extractPatientSearchCritieria(org.hl7.v3.PRPAIN201305UV02 findCandidatesRequest)
    {
        PatientBean result = new PatientBean();
        PRPAMT201306UV02ParameterList queryParams = ExtractHL7QueryParamsFromMessage(findCandidatesRequest);

        if (queryParams == null) {
            log.error("no query parameters were supplied");
        } else {

            PersonName name = ExtractPersonName(queryParams);
            result.setFirstName(name.getFirstName());
            result.setLastName(name.getLastName());
            result.setMiddleName(name.getMiddleName());

            result.setGender(ExtractGender(queryParams));
            result.setDOB(ExtractBirthdate(queryParams));
       
        }

        return result;
    }
    public static PRPAMT201306UV02ParameterList ExtractHL7QueryParamsFromMessage(
            org.hl7.v3.PRPAIN201305UV02 message) {
        log.debug("Entering HL7Parser201305.ExtractHL7QueryParamsFromMessage method...");
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

        log.debug("Exiting HL7Parser201305.ExtractHL7QueryParamsFromMessage method...");
        return queryParamList;
    }

     public static PersonName ExtractPersonName(PRPAMT201306UV02ParameterList params) {
        log.debug("Entering HL7Parser201305.ExtractPersonName method...");

        PersonName personname = new PersonName();

        // Extract the name from the query parameters - Assume only one was specified
        if (params.getLivingSubjectName() != null &&
                params.getLivingSubjectName().size() > 0 &&
                params.getLivingSubjectName().get(0) != null) {
            PRPAMT201306UV02LivingSubjectName name = params.getLivingSubjectName().get(0);

            if (name.getValue() != null &&
                    name.getValue().size() > 0 &&
                    name.getValue().get(0) != null) {
                List<Serializable> choice = name.getValue().get(0).getContent();

                log.info("choice.size()=" + choice.size());

                Iterator<Serializable> iterSerialObjects = choice.iterator();

                String nameString = "";
                EnExplicitFamily lastname = new EnExplicitFamily();
                EnExplicitGiven firstname = null;
                EnExplicitGiven middlename = null;
                
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
                            lastname = (EnExplicitFamily) oJAXBElement.getValue();
                            log.info("found lastname element content=" + lastname.getContent());
                        } else if (oJAXBElement.getValue() instanceof EnExplicitGiven) {
                            if(firstname==null)
                            {
                                firstname = (EnExplicitGiven) oJAXBElement.getValue();
                                log.info("found firstname element content=" + firstname.getContent());
                            }
                            else
                            {
                                middlename = (EnExplicitGiven) oJAXBElement.getValue();
                                log.info("found middlename element content=" + middlename.getContent());
                            }
                        } else {
                            //log.info("other name part=" + (ENXPExplicit) oJAXBElement.getValue());
                        }
                    } else {
                        log.info("contentItem is other");
                    }
                }

                // If text string in patient name, then set in name
                // else set in element.
                boolean namefound = false;
                if (lastname !=null && lastname.getContent() != null) {
                    personname.setLastName(lastname.getContent());
                    log.info("FamilyName : " + personname.getLastName());
                    namefound = true;
                }

                if (firstname != null && firstname.getContent() != null) {
                    personname.setFirstName(firstname.getContent());
                    log.info("GivenName : " + personname.getFirstName());
                    namefound = true;
                }

                if (middlename != null && middlename.getContent() != null) {
                    personname.setMiddleName(middlename.getContent());
                    log.info("MiddleName : " + personname.getMiddleName());
                    namefound = true;
                }
                
                if (!namefound && !nameString.trim().contentEquals("")) {
                    log.info("setting name by nameString " + nameString);
                    personname.setLastName(nameString);
                }
            } else {
                log.info("message does not contain a subject name");
            }
        } else {
            log.info("message does not contain a subject name");
        }

        log.debug("Exiting HL7Parser201305.ExtractPersonName method...");
        return personname;
    }


}
