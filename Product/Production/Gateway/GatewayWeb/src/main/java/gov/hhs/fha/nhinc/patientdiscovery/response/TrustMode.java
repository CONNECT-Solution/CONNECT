/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.patientdiscovery.response;

import org.hl7.v3.II;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201301UV02;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectId;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.QualifiedSubjectIdentifierType;
import gov.hhs.fha.nhinc.common.patientcorrelationfacade.AddPatientCorrelationRequestType;
import gov.hhs.fha.nhinc.common.patientcorrelationfacade.AddPatientCorrelationResponseType;
import gov.hhs.fha.nhinc.transform.subdisc.HL7PRPA201301Transforms;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.xml.ws.WebServiceContext;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.patientcorrelation.proxy.PatientCorrelationProxy;
import gov.hhs.fha.nhinc.patientcorrelation.proxy.PatientCorrelationProxyObjectFactory;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;

/**
 *
 * @author dunnek
 */
public class TrustMode implements ResponseMode {

    private Log log = null;

    public TrustMode() {
        super();
        log = createLogger();
    }

    public PRPAIN201306UV02 processResponse(ResponseParams params) {
        log.debug("begin processResponse");


        PRPAIN201306UV02 response = params.response;
        WebServiceContext context = params.context;
        PRPAIN201305UV02 requestMsg = params.origRequest.getPRPAIN201305UV02();

        // Create an assertion class from the contents of the SAML token
        AssertionType assertion = SamlTokenExtractor.GetAssertion(context);

        PRPAIN201301UV02 request = new PRPAIN201301UV02();

        if (requestHasLivingSubjectId(requestMsg)) {
            try {


                II localPatId = getPatientId(requestMsg);
                II remotePatId = getPatientId(response);

                if (localPatId != null &&
                        NullChecker.isNotNullish(localPatId.getRoot()) &&
                        NullChecker.isNotNullish(localPatId.getExtension()) &&
                        remotePatId != null &&
                        NullChecker.isNotNullish(remotePatId.getRoot()) &&
                        NullChecker.isNotNullish(remotePatId.getExtension())) {
                    request = HL7PRPA201301Transforms.createPRPA201301(response, localPatId.getRoot());

                    if (request != null &&
                            request.getControlActProcess() != null &&
                            NullChecker.isNotNullish(request.getControlActProcess().getSubject()) &&
                            request.getControlActProcess().getSubject().get(0) != null &&
                            request.getControlActProcess().getSubject().get(0).getRegistrationEvent() != null &&
                            request.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1() != null &&
                            request.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient() != null &&
                            NullChecker.isNotNullish(request.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId())) {

                        request.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().add(localPatId);

                        PatientCorrelationProxyObjectFactory patCorrelationFactory = new PatientCorrelationProxyObjectFactory();
                        PatientCorrelationProxy patCorrelationProxy = patCorrelationFactory.getPatientCorrelationProxy();
                        patCorrelationProxy.addPatientCorrelation(request, assertion, null);
                    }
                }
            } catch (Exception ex) {
                log.error(ex.getMessage(), ex);
            }
        } else {
            log.debug("original request did not have a living subject id");
        }

        return response;
    }

    protected boolean requestHasLivingSubjectId(PRPAIN201305UV02 request) {
        boolean result = false;

        result = (getPatientId(request) != null);

        return result;
    }

    protected II getPatientId(PRPAIN201305UV02 request) {
        II patId = null;
        String aaId = null;

        if (request != null &&
                request.getControlActProcess() != null) {
            if (NullChecker.isNotNullish(request.getControlActProcess().getAuthorOrPerformer()) &&
                    request.getControlActProcess().getAuthorOrPerformer().get(0) != null &&
                    request.getControlActProcess().getAuthorOrPerformer().get(0).getAssignedDevice() != null &&
                    request.getControlActProcess().getAuthorOrPerformer().get(0).getAssignedDevice().getValue() != null &&
                    NullChecker.isNotNullish(request.getControlActProcess().getAuthorOrPerformer().get(0).getAssignedDevice().getValue().getId()) &&
                    request.getControlActProcess().getAuthorOrPerformer().get(0).getAssignedDevice().getValue().getId().get(0) != null &&
                    NullChecker.isNotNullish(request.getControlActProcess().getAuthorOrPerformer().get(0).getAssignedDevice().getValue().getId().get(0).getRoot())) {
                aaId = request.getControlActProcess().getAuthorOrPerformer().get(0).getAssignedDevice().getValue().getId().get(0).getRoot();
            }

            if (NullChecker.isNotNullish(aaId)) {
                if (request.getControlActProcess().getQueryByParameter() != null &&
                        request.getControlActProcess().getQueryByParameter().getValue() != null &&
                        request.getControlActProcess().getQueryByParameter().getValue().getParameterList() != null &&
                        NullChecker.isNotNullish(request.getControlActProcess().getQueryByParameter().getValue().getParameterList().getLivingSubjectId())) {
                    for (PRPAMT201306UV02LivingSubjectId livingSubId : request.getControlActProcess().getQueryByParameter().getValue().getParameterList().getLivingSubjectId()) {
                        for (II id : livingSubId.getValue()) {
                            if (id != null &&
                                    NullChecker.isNotNullish(id.getRoot()) &&
                                    NullChecker.isNotNullish(id.getExtension()) &&
                                    aaId.contentEquals(id.getRoot())) {
                                patId = new II();
                                patId.setRoot(id.getRoot());
                                patId.setExtension(id.getExtension());

                                // break out of inner loop
                                break;
                            }
                        }

                        // If the patient id was found then break out of outer loop
                        if (patId != null) {
                            break;
                        }
                    }
                }
            }
        }

        return patId;
    }

    protected II getPatientId(PRPAIN201306UV02 request) {
        II patId = null;

        if (request != null &&
                request.getControlActProcess() != null &&
                NullChecker.isNotNullish(request.getControlActProcess().getSubject()) &&
                request.getControlActProcess().getSubject().get(0) != null &&
                request.getControlActProcess().getSubject().get(0).getRegistrationEvent() != null &&
                request.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1() != null &&
                request.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient() != null &&
                NullChecker.isNotNullish(request.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId()) &&
                request.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0) != null &&
                NullChecker.isNotNullish(request.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0).getExtension()) &&
                NullChecker.isNotNullish(request.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0).getRoot())) {

            patId = new II();
            patId.setRoot(request.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0).getRoot());
            patId.setExtension(request.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0).getExtension());
        }

        return patId;
    }

    protected Log createLogger() {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    protected PRPAIN201301UV02 createPRPA201301(PRPAIN201306UV02 input) {
        PRPAIN201301UV02 result = null;

        result =
                HL7PRPA201301Transforms.createPRPA201301(input, getLocalHomeCommunityId());

        return result;
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

    private PRPAIN201301UV02 mergeIds(PRPAIN201301UV02 patient, II localId) {
        PRPAIN201301UV02 result = patient;

        II remoteId;

        log.debug("begin MergeIds");
        try {
            remoteId = result.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0);

            log.debug("Local Id = " + localId.getExtension() + "; remote id = " + remoteId.getExtension());

            //clear Id's
            result.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().clear();

            //add both the local and remote id. 
            result.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().add(localId);
            result.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().add(remoteId);

        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }

        return result;
    }
}


