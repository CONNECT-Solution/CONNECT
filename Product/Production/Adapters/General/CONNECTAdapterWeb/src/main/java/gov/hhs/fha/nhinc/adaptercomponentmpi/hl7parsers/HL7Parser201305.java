/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.adaptercomponentmpi.hl7parsers;

//import gov.hhs.fha.nhinc.mpi.*;
import gov.hhs.fha.nhinc.mpilib.*;
import java.util.List;
import java.io.Serializable;
import java.util.Iterator;
import javax.xml.bind.JAXBElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.*;

/**
 *
 * @author rayj
 */
public class HL7Parser201305 {

    private static Log log = LogFactory.getLog(HL7Parser201305.class);

    public static String ExtractGender(PRPAMT201306UV02ParameterList params) {
        log.debug("Entering HL7Parser201305.ExtractGender method...");

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

        log.debug("Exiting HL7Parser201305.ExtractGender method...");
        return genderCode;
    }

    public static String ExtractBirthdate(PRPAMT201306UV02ParameterList params) {
        log.debug("Entering HL7Parser201305.ExtractBirthdate method...");

        String birthDate = null;

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
                birthDate = birthday.getValue();
            } else {
                log.info("message does not contain a birthtime");
            }
        } else {
            log.info("message does not contain a birthtime");
        }

        log.debug("Exiting HL7Parser201305.ExtractBirthdate method...");
        return birthDate;
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
                EnExplicitFamily lastname = null;
                EnExplicitGiven firstname = null;

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
                            if(firstname == null){
                                firstname = new EnExplicitGiven();
                                firstname = (EnExplicitGiven) oJAXBElement.getValue();
                                log.info("found firstname element; content=" + firstname.getContent());
                            }else{
                                //this would be where to add handle for middlename
                            }
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
                if (lastname!= null && lastname.getContent() != null) {
                    personname.setLastName(lastname.getContent());
                    log.info("FamilyName : " + personname.getLastName());
                    namefound = true;
                }

                if (firstname!=null && firstname.getContent() != null) {
                    personname.setFirstName(firstname.getContent());
                    log.info("GivenName : " + personname.getFirstName());
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

    public static Identifiers ExtractPersonIdentifiers(
            PRPAMT201306UV02ParameterList params) {
        log.debug("Entering HL7Parser201305.ExtractPersonIdentifiers method...");

        Identifiers ids = new Identifiers();
        Identifier id = new Identifier();

        if (params.getLivingSubjectId() != null &&
                params.getLivingSubjectId().size() > 0 &&
                params.getLivingSubjectId().get(0) != null) {
            PRPAMT201306UV02LivingSubjectId livingSubjectId = params.getLivingSubjectId().get(0);

            if (livingSubjectId.getValue() != null &&
                    livingSubjectId.getValue().size() > 0 &&
                    livingSubjectId.getValue().get(0) != null) {
                II subjectId = livingSubjectId.getValue().get(0);

                if (subjectId.getExtension() != null &&
                        subjectId.getExtension().length() > 0 &&
                        subjectId.getRoot() != null &&
                        subjectId.getRoot().length() > 0) {
                    id.setId(subjectId.getExtension());
                    id.setOrganizationId(subjectId.getRoot());
                    log.info("Created id from patient identifier [organization=" + id.getOrganizationId() + "][id=" + id.getId() + "]");
                    ids.add(id);
                } else {
                    log.info("message does not contain an id");
                }
            } else {
                log.info("message does not contain an id");
            }
        } else {
            log.info("message does not contain an id");
        }

        log.debug("Exiting HL7Parser201305.ExtractPersonIdentifiers method...");
        return ids;
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

    public static Patient ExtractMpiPatientFromMessage(
            org.hl7.v3.PRPAIN201305UV02 message) {
        log.debug("Entering HL7Parser201305.ExtractMpiPatientFromMessage method...");

        PRPAMT201306UV02ParameterList queryParamList = ExtractHL7QueryParamsFromMessage(message);
        Patient mpipatient = ExtractMpiPatientFromQueryParams(queryParamList);

        log.debug("Exiting HL7Parser201305.ExtractMpiPatientFromMessage method...");
        return mpipatient;
    }

    public static Patient ExtractMpiPatientFromQueryParams(
            PRPAMT201306UV02ParameterList params) {
        log.debug("Entering HL7Parser201305.ExtractMpiPatientFromQueryParams method...");

        Patient mpiPatient = new Patient();

        if (params != null) {

            //mpiPatient.setName(ExtractPersonName(params));
            mpiPatient.getNames().add(ExtractPersonName(params));
            mpiPatient.setGender(ExtractGender(params));

            String birthdateString = ExtractBirthdate(params);
            mpiPatient.setDateOfBirth(birthdateString);

            Identifiers ids = ExtractPersonIdentifiers(params);
            mpiPatient.setIdentifiers(ids);
        } else {
            mpiPatient = null;
        }

        log.debug("Exiting HL7Parser201305.ExtractMpiPatientFromQueryParams method...");
        return mpiPatient;
    }
}
