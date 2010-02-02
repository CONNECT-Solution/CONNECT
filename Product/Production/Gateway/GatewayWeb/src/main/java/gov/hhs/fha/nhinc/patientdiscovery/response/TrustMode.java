/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientdiscovery.response;
import org.hl7.v3.II;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201301UV02;
import org.hl7.v3.PRPAMT201306UV02QueryByParameter;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectId;
import org.hl7.v3.AddPatientCorrelationSecuredRequestType;
import org.hl7.v3.AddPatientCorrelationSecuredResponseType;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.transform.subdisc.HL7PRPA201301Transforms;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.xml.ws.WebServiceContext;
import gov.hhs.fha.nhinc.nhinccomponentpatientcorrelation.PatientCorrelationSecuredPortType;
import gov.hhs.fha.nhinc.nhinccomponentpatientcorrelation.PatientCorrelationServiceSecured;
import javax.xml.ws.WebServiceContext;
import java.util.Map;
import javax.xml.ws.BindingProvider;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
/**
 *
 * @author dunnek
 */
public class TrustMode implements ResponseMode{
    private Log log = null;
    private static PatientCorrelationServiceSecured service = new PatientCorrelationServiceSecured();
    public TrustMode() {
        super();
        log = createLogger();
    }
    public PRPAIN201306UV02 processResponse(ResponseParams params)
    {
        log.debug("begin processResponse");


         PRPAIN201306UV02 response = params.response;
         WebServiceContext context = params.context;
         PRPAIN201305UV02 requestMsg = params.origRequest.getPRPAIN201305UV02();

         // Create an assertion class from the contents of the SAML token
        AssertionType assertion = SamlTokenExtractor.GetAssertion(context);
        PRPAIN201301UV02 requestPatient = null;

        AddPatientCorrelationSecuredRequestType request = new AddPatientCorrelationSecuredRequestType();
        AddPatientCorrelationSecuredResponseType responseType;
        String url;

        url = getPCUrl();

        //requestPatient.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue().getAsOtherIDs()

        if(requestHasLivingSubjectId(requestMsg))
        {
            try
            {
                requestPatient = mergeIds(createPRPA201301(response), getPatientId(requestMsg));
                request.setPRPAIN201301UV02(requestPatient);
                PatientCorrelationSecuredPortType port =  getPCPort(url, assertion);

                responseType = port.addPatientCorrelation(request);
            }
            catch(Exception ex)
            {
                log.error(ex.getMessage(),ex);
            }
        }
        else
        {
            log.debug("original request did not have a living subject id");
        }
        
        return response;
    }
    protected boolean requestHasLivingSubjectId(PRPAIN201305UV02 request)
    {
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
                    for (PRPAMT201306UV02LivingSubjectId livingSubId:request.getControlActProcess().getQueryByParameter().getValue().getParameterList().getLivingSubjectId()) {
                        if (NullChecker.isNotNullish(livingSubId.getValue()) &&
                                livingSubId.getValue().get(0) != null &&
                                NullChecker.isNotNullish(livingSubId.getValue().get(0).getRoot()) &&
                                NullChecker.isNotNullish(livingSubId.getValue().get(0).getExtension()) &&
                                aaId.contentEquals(livingSubId.getValue().get(0).getRoot())){
                            patId = new II();
                            patId.setRoot(livingSubId.getValue().get(0).getRoot());
                            patId.setExtension(livingSubId.getValue().get(0).getExtension());
                        }
                    }
                }
            }
        }

        return patId;
    }
    protected Log createLogger()
    {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }
    protected PRPAIN201301UV02 createPRPA201301(PRPAIN201306UV02 input)
    {
        PRPAIN201301UV02 result = null;

        result = HL7PRPA201301Transforms.createPRPA201301(input, getLocalHomeCommunityId());

        return result;
    }
    protected String getLocalHomeCommunityId()
    {
        String result = "";

        try
        {
            result = PropertyAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.HOME_COMMUNITY_ID_PROPERTY);
        }
        catch(Exception ex)
        {
            log.error(ex.getMessage(), ex);
        }
        return result;
    }
    protected PatientCorrelationSecuredPortType getPCPort(String url,  AssertionType assertion) {
        PatientCorrelationSecuredPortType port = service.getPatientCorrelationSecuredPort();

        log.info("Setting endpoint address to Nhin Patient Corrleation Service to " + url);
        ((BindingProvider) port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);
         SamlTokenCreator tokenCreator = new SamlTokenCreator();
         Map requestContext = tokenCreator.CreateRequestContext(assertion, url, NhincConstants.PAT_CORR_ACTION);

         ((BindingProvider) port).getRequestContext().putAll(requestContext);
        return port;
    }
    private String getPCUrl() {
        String url = null;


        try {
            url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.PATIENT_CORRELATION_SECURED_SERVICE_NAME);
        } catch (ConnectionManagerException ex) {
            log.error("Error: Failed to retrieve url for service: " + NhincConstants.PATIENT_CORRELATION_SECURED_SERVICE_NAME);
            log.error(ex.getMessage());
        }


        return url;
    }

    private PRPAIN201301UV02 mergeIds(PRPAIN201301UV02 patient, II localId)
    {
        PRPAIN201301UV02 result = patient;
        
        II remoteId;

        log.debug("begin MergeIds");
        try
        {
            remoteId = result.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0);

            log.debug("Local Id = " + localId.getExtension() + "; remote id = " + remoteId.getExtension());

            //clear Id's
            result.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().clear();
            
            //add both the local and remote id. 
            result.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().add(localId);
            result.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().add(remoteId);

        }
        catch(Exception ex)
        {
            log.error(ex.getMessage(), ex);
        }
        return result;
    }
}


