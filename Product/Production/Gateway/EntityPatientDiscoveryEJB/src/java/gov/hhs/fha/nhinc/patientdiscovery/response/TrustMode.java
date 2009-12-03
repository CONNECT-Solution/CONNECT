/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientdiscovery.response;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.PRPAIN201301UV02;
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
    public PRPAIN201306UV02 processResponse(PRPAIN201306UV02 response, WebServiceContext context)
    {
        log.debug("begin processResponse");
         // Create an assertion class from the contents of the SAML token
        AssertionType assertion = SamlTokenExtractor.GetAssertion(context);
                

        AddPatientCorrelationSecuredRequestType request = new AddPatientCorrelationSecuredRequestType();
        AddPatientCorrelationSecuredResponseType responseType;
        String url;

        url = getPCUrl();

        request.setPRPAIN201301UV02(createPRPA201301(response));

        if(request.getPRPAIN201301UV02().getControlActProcess().getSubject().size() == 0)
        {
            log.warn("no subjects");
        }
        PatientCorrelationSecuredPortType port =  getPCPort(url, assertion);

        responseType = port.addPatientCorrelation(request);

        
        return response;
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
    private PatientCorrelationSecuredPortType getPCPort(String url,  AssertionType assertion) {
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


}
