/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.patientdiscovery.response;

import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.xml.ws.WebServiceContext;
import gov.hhs.fha.nhinc.mpi.proxy.AdapterMpiProxy;
import gov.hhs.fha.nhinc.mpi.proxy.AdapterMpiProxyObjectFactory;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.transform.subdisc.HL7PatientTransforms;
import gov.hhs.fha.nhinc.transform.subdisc.HL7PRPA201305Transforms;
import java.util.List;
import javax.xml.bind.JAXBElement;
import org.hl7.v3.II;
import org.hl7.v3.PRPAIN201305UV02QUQIMT021001UV01ControlActProcess;
import org.hl7.v3.PRPAMT201301UV02Patient;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectAdministrativeGender;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectBirthPlaceAddress;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectBirthPlaceName;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectBirthTime;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectId;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectName;
import org.hl7.v3.PRPAMT201306UV02MothersMaidenName;
import org.hl7.v3.PRPAMT201306UV02ParameterList;
import org.hl7.v3.PRPAMT201306UV02QueryByParameter;
import org.hl7.v3.PRPAMT201310UV02Patient;

/**
 *
 * @author dunnek
 */
public class VerifyMode implements ResponseMode {

    private Log log = null;

    public VerifyMode() {
        super();
        log = createLogger();
    }

    public PRPAIN201306UV02 processResponse(ResponseParams params) {
        log.debug("begin processResponse");
        PRPAIN201306UV02 response = params.response;
        WebServiceContext context = params.context;
        PRPAIN201305UV02 requestMsg = params.origRequest.getPRPAIN201305UV02();

        PRPAIN201306UV02 result = response;

        // Create an assertion class from the contents of the SAML token
        AssertionType assertion = SamlTokenExtractor.GetAssertion(context);

        if (patientExistsLocally(requestMsg, assertion, response)) {
            log.debug("patient exists locally, adding correlation");
            new TrustMode().processResponse(params);
        } else {
            log.warn("Patient does not exist locally, correlation not added");
        }
        return result;
    }

    protected Log createLogger() {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    protected PRPAMT201301UV02Patient getPatient(PRPAMT201306UV02ParameterList paramList) {
        return HL7PatientTransforms.create201301Patient(paramList);
    }

    protected String getLocalHomeCommunityId() {
        String result = "";

        try {
            result = PropertyAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.HOME_COMMUNITY_ID_PROPERTY);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
        return result;
    }

    private String getSenderCommunityId(PRPAIN201306UV02 response) {
        String hcid = null;

        if (response != null &&
                response.getSender() != null &&
                response.getSender().getDevice() != null &&
                response.getSender().getDevice().getAsAgent() != null &&
                response.getSender().getDevice().getAsAgent().getValue() != null &&
                response.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization() != null &&
                response.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue() != null &&
                NullChecker.isNotNullish(response.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId()) &&
                response.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0) != null &&
                NullChecker.isNotNullish(response.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0).getRoot())) {
            hcid = response.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0).getRoot();
        }

        return hcid;
    }

    protected PRPAMT201301UV02Patient extractPatient(PRPAIN201306UV02 response) {
        PRPAMT201301UV02Patient patient = null;

        if (response != null &&
                response.getControlActProcess() != null &&
                NullChecker.isNotNullish(response.getControlActProcess().getSubject()) &&
                response.getControlActProcess().getSubject().get(0) != null &&
                response.getControlActProcess().getSubject().get(0).getRegistrationEvent() != null &&
                response.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1() != null &&
                response.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient() != null) {
            PRPAMT201310UV02Patient remotePatient = response.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient();
            patient = HL7PatientTransforms.createPRPAMT201301UVPatient(remotePatient);
        }

        return patient;
    }

    protected PRPAIN201305UV02 convert201306to201305(PRPAIN201306UV02 response) {
        PRPAIN201305UV02 result = null;
        String localHCID = getLocalHomeCommunityId();
        String remoteHCID = getSenderCommunityId(response);
        PRPAMT201301UV02Patient patient = extractPatient(response);
        if (patient != null) {
            result = HL7PRPA201305Transforms.createPRPA201305(patient, remoteHCID, localHCID, localHCID);
        }

        return result;
    }

    protected boolean patientExistsLocally(PRPAIN201305UV02 query, AssertionType assertion, PRPAIN201306UV02 response) {
        boolean result = false;

        PRPAIN201305UV02 mpiQuery;
        List<II> mpiIds;
        List<PRPAMT201306UV02LivingSubjectId> requestIds;
        PRPAIN201305UV02QUQIMT021001UV01ControlActProcess controlActProcess = new PRPAIN201305UV02QUQIMT021001UV01ControlActProcess();

        // Check to see if a patient id was specified in the original request
        if (query != null &&
                query.getControlActProcess() != null &&
                query.getControlActProcess().getQueryByParameter() != null &&
                query.getControlActProcess().getQueryByParameter().getValue() != null &&
                query.getControlActProcess().getQueryByParameter().getValue().getParameterList() != null &&
                NullChecker.isNotNullish(query.getControlActProcess().getQueryByParameter().getValue().getParameterList().getLivingSubjectId())) {

            mpiQuery = convert201306to201305(response);

            requestIds = query.getControlActProcess().getQueryByParameter().getValue().getParameterList().getLivingSubjectId();

            log.debug("original Request Ids " + requestIds.size());

            PRPAIN201306UV02 mpiResult = queryMpi(mpiQuery, assertion);

            if (mpiResult != null) {
                try {
                    log.debug("Received result from mpi.");
                    mpiIds = mpiResult.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId();

                    log.debug("MPI Ids: " + mpiIds.size());
                    log.debug("Request Ids: " + requestIds.size());

                    for (PRPAMT201306UV02LivingSubjectId patId : requestIds) {
                        for (II id : patId.getValue()) {
                            result = compareId(mpiIds.get(0), id);

                            // If the patient id was found then break out of the inner loop
                            if (result == true) {
                                break;
                            }
                        }

                        // If the patient id was found then breka out of the outer loop
                        if (result == true) {
                            break;
                        }
                    }

                } catch (Exception ex) {
                    log.error(ex.getMessage(), ex);
                    result = false;
                }
            }
        } else {
            log.debug("There was no patient id specified in the original request message, bypassing MPI check and returning false");
        }

        return result;
    }

    protected boolean compareId(List<PRPAMT201306UV02LivingSubjectId> localIds, List<PRPAMT201306UV02LivingSubjectId> remoteIds) {
        boolean result = false;

        try {
            for (PRPAMT201306UV02LivingSubjectId localId : localIds) {
                for (PRPAMT201306UV02LivingSubjectId remoteId : remoteIds) {
                    if (compareId(localId, remoteId)) {
                        result = true;
                        break;

                    }


                }
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }

        return result;
    }

    protected boolean compareId(PRPAMT201306UV02LivingSubjectId localId, PRPAMT201306UV02LivingSubjectId remoteId) {
        boolean result = false;

        for (II localII : localId.getValue()) {
            for (II remoteII : remoteId.getValue()) {
                if (compareId(localII, remoteII)) {
                    result = true;
                    break;

                }


            }
        }
        return result;
    }

    private boolean compareId(II localId, II remoteId) {
        boolean result = false;

        if (localId != null && remoteId != null) {
            String localExt = localId.getExtension();
            String localRoot = localId.getRoot();
            log.debug("Comparing Root: " + localRoot + " to " + remoteId.getRoot());
            log.debug("Comparing Ext: " + localExt + " to " + remoteId.getExtension());
            if (remoteId.getExtension().equalsIgnoreCase(localExt) &&
                    remoteId.getRoot().equalsIgnoreCase(localRoot)) {
                result = true;
            }

        }

        return result;
    }

    protected PRPAIN201306UV02 queryMpi(PRPAIN201305UV02 query, AssertionType assertion) {
        PRPAIN201306UV02 queryResults = new PRPAIN201306UV02();

        if (query != null) {
            // Query the MPI to see if the patient is found
            AdapterMpiProxyObjectFactory mpiFactory = new AdapterMpiProxyObjectFactory();
            AdapterMpiProxy mpiProxy = mpiFactory.getAdapterMpiProxy();
            log.info("Sending query to the Secured MPI");
            queryResults =
                    mpiProxy.findCandidates(query, assertion);

        } else {
            log.error("MPI Request is null");
            queryResults =
                    null;
        }

        return queryResults;
    }

    private JAXBElement<PRPAMT201306UV02QueryByParameter> copyParameterList(PRPAMT201306UV02QueryByParameter input) {

        PRPAMT201306UV02QueryByParameter result = new PRPAMT201306UV02QueryByParameter();

        result.setParameterList(copyParameterList(input.getParameterList()));

        javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:hl7-org:v3", "PRPAMT201306UV02QueryByParameter");
        JAXBElement<PRPAMT201306UV02QueryByParameter> jaxbParams = new JAXBElement<PRPAMT201306UV02QueryByParameter>(xmlqname, PRPAMT201306UV02QueryByParameter.class, result);






        return jaxbParams;
    }

    private PRPAMT201306UV02ParameterList copyParameterList(
            PRPAMT201306UV02ParameterList input) {
        PRPAMT201306UV02ParameterList result = new PRPAMT201306UV02ParameterList();

        for (PRPAMT201306UV02LivingSubjectAdministrativeGender gender : input.getLivingSubjectAdministrativeGender()) {
            result.getLivingSubjectAdministrativeGender().add(gender);
        }

        for (PRPAMT201306UV02LivingSubjectBirthPlaceAddress birthPlace : input.getLivingSubjectBirthPlaceAddress()) {
            result.getLivingSubjectBirthPlaceAddress().add(birthPlace);
        }

        for (PRPAMT201306UV02LivingSubjectBirthPlaceName birthPlaceName : input.getLivingSubjectBirthPlaceName()) {
            result.getLivingSubjectBirthPlaceName().add(birthPlaceName);
        }

        for (PRPAMT201306UV02LivingSubjectBirthTime birthTime : input.getLivingSubjectBirthTime()) {
            result.getLivingSubjectBirthTime().add(birthTime);
        }

        for (PRPAMT201306UV02LivingSubjectName subjectName : input.getLivingSubjectName()) {
            result.getLivingSubjectName().add(subjectName);
        }

        for (PRPAMT201306UV02MothersMaidenName motherName : input.getMothersMaidenName()) {
            result.getMothersMaidenName().add(motherName);
        }

        result.setId(input.getId());

        return result;
    }
}
