/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientcorrelation.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.nhinccomponentpatientcorrelation.PatientCorrelationSecuredPortType;
import gov.hhs.fha.nhinc.nhinccomponentpatientcorrelation.PatientCorrelationServiceSecured;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import java.util.Map;
import javax.xml.ws.BindingProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.AddPatientCorrelationResponseType;
import org.hl7.v3.AddPatientCorrelationSecuredRequestType;
import org.hl7.v3.AddPatientCorrelationSecuredResponseType;
import org.hl7.v3.PRPAIN201301UV02;
import org.hl7.v3.PRPAIN201309UV02;
import org.hl7.v3.RetrievePatientCorrelationsResponseType;
import org.hl7.v3.RetrievePatientCorrelationsSecuredRequestType;
import org.hl7.v3.RetrievePatientCorrelationsSecuredResponseType;

/**
 *
 * @author jhoppesc
 */
public class PatientCorrelationProxyWebServiceSecuredImpl implements PatientCorrelationProxy {
    private static Log log = LogFactory.getLog(PatientCorrelationProxyWebServiceSecuredImpl.class);
    private static PatientCorrelationServiceSecured service = new PatientCorrelationServiceSecured();

    public RetrievePatientCorrelationsResponseType retrievePatientCorrelations(PRPAIN201309UV02 request, AssertionType assertion, NhinTargetCommunitiesType targets) {
        RetrievePatientCorrelationsResponseType result = new RetrievePatientCorrelationsResponseType();
        RetrievePatientCorrelationsSecuredRequestType securedRequest = new RetrievePatientCorrelationsSecuredRequestType();
        securedRequest.setPRPAIN201309UV02(request);
        String url = null;

        url = getUrl();

        if (NullChecker.isNotNullish(url)) {
            PatientCorrelationSecuredPortType port = getPort(url, assertion);

            RetrievePatientCorrelationsSecuredResponseType securedResult = port.retrievePatientCorrelations(securedRequest);
            result.setPRPAIN201310UV02(securedResult.getPRPAIN201310UV02());
        }

        return result;
    }

    public AddPatientCorrelationResponseType addPatientCorrelation(PRPAIN201301UV02 request, AssertionType assertion, NhinTargetCommunitiesType targets) {
        AddPatientCorrelationResponseType result = new AddPatientCorrelationResponseType();
        AddPatientCorrelationSecuredRequestType securedRequest = new AddPatientCorrelationSecuredRequestType();
        securedRequest.setPRPAIN201301UV02(request);

        String url = null;

        url = getUrl();

        if (NullChecker.isNotNullish(url)) {
            PatientCorrelationSecuredPortType port = getPort(url, assertion);

            AddPatientCorrelationSecuredResponseType securedResult = port.addPatientCorrelation(securedRequest);
            result.setMCCIIN000002UV01(securedResult.getMCCIIN000002UV01());
        }

        return result;
    }

    private String getUrl () {
        String url = null;

        try {
            url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.PATIENT_CORRELATION_SECURED_SERVICE_NAME);
        } catch (ConnectionManagerException ex) {
            log.error("Error: Failed to retrieve url for service: " + NhincConstants.PATIENT_CORRELATION_SECURED_SERVICE_NAME + " for local home community");
            log.error(ex.getMessage());
        }

        return url;
    }

    private PatientCorrelationSecuredPortType getPort(String url, AssertionType assertion) {
        PatientCorrelationSecuredPortType port = service.getPatientCorrelationSecuredPort();

        gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().initializePort((javax.xml.ws.BindingProvider) port, url);

        SamlTokenCreator tokenCreator = new SamlTokenCreator();
        Map requestContext = tokenCreator.CreateRequestContext(assertion, url, NhincConstants.PAT_CORR_ACTION);

        ((BindingProvider) port).getRequestContext().putAll(requestContext);

        return port;
    }

}
