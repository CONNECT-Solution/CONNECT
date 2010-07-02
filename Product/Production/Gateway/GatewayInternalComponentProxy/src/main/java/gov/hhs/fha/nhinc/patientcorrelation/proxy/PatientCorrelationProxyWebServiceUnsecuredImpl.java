/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientcorrelation.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.nhinccomponentpatientcorrelation.PatientCorrelationPortType;
import gov.hhs.fha.nhinc.nhinccomponentpatientcorrelation.PatientCorrelationService;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import java.util.Map;
import javax.xml.ws.BindingProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.AddPatientCorrelationRequestType;
import org.hl7.v3.AddPatientCorrelationResponseType;
import org.hl7.v3.PRPAIN201301UV02;
import org.hl7.v3.PRPAIN201309UV02;
import org.hl7.v3.RetrievePatientCorrelationsRequestType;
import org.hl7.v3.RetrievePatientCorrelationsResponseType;

/**
 *
 * @author jhoppesc
 */
public class PatientCorrelationProxyWebServiceUnsecuredImpl implements PatientCorrelationProxy {
    private static Log log = LogFactory.getLog(PatientCorrelationProxyWebServiceUnsecuredImpl.class);
    private static PatientCorrelationService service = new PatientCorrelationService();

    public RetrievePatientCorrelationsResponseType retrievePatientCorrelations(PRPAIN201309UV02 request, AssertionType assertion, NhinTargetCommunitiesType targets) {
        RetrievePatientCorrelationsResponseType result = new RetrievePatientCorrelationsResponseType();
        RetrievePatientCorrelationsRequestType msg = new RetrievePatientCorrelationsRequestType();
        msg.setAssertion(assertion);
        msg.setPRPAIN201309UV02(request);
        String url = null;

        url = getUrl();

        if (NullChecker.isNotNullish(url)) {
            PatientCorrelationPortType port = getPort(url, assertion);

            result = port.retrievePatientCorrelations(msg);
        }

        return result;
    }

    public AddPatientCorrelationResponseType addPatientCorrelation(PRPAIN201301UV02 request, AssertionType assertion, NhinTargetCommunitiesType targets) {
        AddPatientCorrelationResponseType result = new AddPatientCorrelationResponseType();
        AddPatientCorrelationRequestType msg = new AddPatientCorrelationRequestType();
        msg.setAssertion(assertion);
        msg.setPRPAIN201301UV02(request);

        String url = null;

        url = getUrl();

        if (NullChecker.isNotNullish(url)) {
            PatientCorrelationPortType port = getPort(url, assertion);

            result = port.addPatientCorrelation(msg);
        }

        return result;
    }

    private String getUrl () {
        String url = null;

        try {
            url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.PATIENT_CORRELATION_SERVICE_NAME);
        } catch (ConnectionManagerException ex) {
            log.error("Error: Failed to retrieve url for service: " + NhincConstants.PATIENT_CORRELATION_SERVICE_NAME + " for local home community");
            log.error(ex.getMessage());
        }

        return url;
    }

    private PatientCorrelationPortType getPort(String url, AssertionType assertion) {
        PatientCorrelationPortType port = service.getPatientCorrelationPort();

        gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().initializePort((javax.xml.ws.BindingProvider) port, url);

        SamlTokenCreator tokenCreator = new SamlTokenCreator();
        Map requestContext = tokenCreator.CreateRequestContext(assertion, url, NhincConstants.PAT_CORR_ACTION);

        ((BindingProvider) port).getRequestContext().putAll(requestContext);

        return port;
    }

}
