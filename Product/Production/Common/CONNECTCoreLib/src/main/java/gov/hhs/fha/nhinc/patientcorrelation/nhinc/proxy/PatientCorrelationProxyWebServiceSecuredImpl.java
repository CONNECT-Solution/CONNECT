/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientcorrelation.nhinc.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.nhinccomponentpatientcorrelation.PatientCorrelationSecuredPortType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
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
    private Log log = null;
    private static Service cachedService = null;
    private static final String NAMESPACE_URI = "urn:gov:hhs:fha:nhinc:nhinccomponentpatientcorrelation";
    private static final String SERVICE_LOCAL_PART = "PatientCorrelationServiceSecured";
    private static final String PORT_LOCAL_PART = "PatientCorrelationSecuredPort";
    private static final String WSDL_FILE = "NhincComponentPatientCorrelationSecured.wsdl";
    private static final String WS_ADDRESSING_ACTION_RETRIEVE = "urn:gov:hhs:fha:nhinc:nhinccomponentpatientcorrelation:RetrievePatientCorrelationsRequestMessageSecured";
    private static final String WS_ADDRESSING_ACTION_ADD = "urn:gov:hhs:fha:nhinc:nhinccomponentpatientcorrelation:AddPatientCorrelationRequestMessageSecured";
    private WebServiceProxyHelper oProxyHelper = null;

    public PatientCorrelationProxyWebServiceSecuredImpl()
    {
        log = createLogger();
        oProxyHelper = createWebServiceProxyHelper();
    }

    protected Log createLogger()
    {
        return LogFactory.getLog(getClass());
    }

    protected WebServiceProxyHelper createWebServiceProxyHelper()
    {
        return new WebServiceProxyHelper();
    }

    /**
     * This method retrieves and initializes the port.
     *
     * @param url The URL for the web service.
     * @return The port object for the web service.
     */
    protected PatientCorrelationSecuredPortType getPort(String url, String serviceAction, String wsAddressingAction, AssertionType assertion)
    {
        PatientCorrelationSecuredPortType port = null;
        Service service = getService();
        if (service != null)
        {
            log.debug("Obtained service - creating port.");

            port = service.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), PatientCorrelationSecuredPortType.class);
            oProxyHelper.initializeSecurePort((javax.xml.ws.BindingProvider) port, url, serviceAction, wsAddressingAction, assertion);
        }
        else
        {
            log.error("Unable to obtain serivce - no port created.");
        }
        return port;
    }

    /**
     * Retrieve the service class for this web service.
     *
     * @return The service class for this web service.
     */
    protected Service getService()
    {
        if (cachedService == null)
        {
            try
            {
                cachedService = oProxyHelper.createService(WSDL_FILE, NAMESPACE_URI, SERVICE_LOCAL_PART);
            }
            catch (Throwable t)
            {
                log.error("Error creating service: " + t.getMessage(), t);
            }
        }
        return cachedService;
    }


    public RetrievePatientCorrelationsResponseType retrievePatientCorrelations(PRPAIN201309UV02 msg, AssertionType assertion) {
        log.debug("Begin retrievePatientCorrelations");
        RetrievePatientCorrelationsResponseType response = new RetrievePatientCorrelationsResponseType();
        RetrievePatientCorrelationsSecuredResponseType securedResp = new RetrievePatientCorrelationsSecuredResponseType();

        try
        {
            String url = oProxyHelper.getUrlLocalHomeCommunity(NhincConstants.PATIENT_CORRELATION_SECURED_SERVICE_NAME);
            PatientCorrelationSecuredPortType port = getPort(url, NhincConstants.DOC_QUERY_ACTION, WS_ADDRESSING_ACTION_RETRIEVE, assertion);

            if(msg == null)
            {
                log.error("Message was null");
            }
            else if(port == null)
            {
                log.error("port was null");
            }
            else
            {
                RetrievePatientCorrelationsSecuredRequestType request = new RetrievePatientCorrelationsSecuredRequestType();
                request.setPRPAIN201309UV02(msg);

                securedResp = (RetrievePatientCorrelationsSecuredResponseType)oProxyHelper.invokePort(port, PatientCorrelationSecuredPortType.class, "retrievePatientCorrelations", request);
                if (securedResp != null &&
                        securedResp.getPRPAIN201310UV02() != null) {
                    response.setPRPAIN201310UV02(securedResp.getPRPAIN201310UV02());
                }
            }
        }
        catch (Exception ex)
        {
            log.error("Error calling retrievePatientCorrelations: " + ex.getMessage(), ex);
        }

        log.debug("End retrievePatientCorrelations");
        return response;
    }

    public AddPatientCorrelationResponseType addPatientCorrelation(PRPAIN201301UV02 msg, AssertionType assertion) {
        log.debug("Begin addPatientCorrelation");
        AddPatientCorrelationResponseType response = new AddPatientCorrelationResponseType();
        AddPatientCorrelationSecuredResponseType securedResp = new AddPatientCorrelationSecuredResponseType();

        try
        {
            String url = oProxyHelper.getUrlLocalHomeCommunity(NhincConstants.PATIENT_CORRELATION_SECURED_SERVICE_NAME);
            PatientCorrelationSecuredPortType port = getPort(url, NhincConstants.DOC_QUERY_ACTION, WS_ADDRESSING_ACTION_ADD, assertion);

            if(msg == null)
            {
                log.error("Message was null");
            }
            else if(port == null)
            {
                log.error("port was null");
            }
            else
            {
                AddPatientCorrelationSecuredRequestType request = new AddPatientCorrelationSecuredRequestType();
                request.setPRPAIN201301UV02(msg);

                securedResp = (AddPatientCorrelationSecuredResponseType)oProxyHelper.invokePort(port, PatientCorrelationSecuredPortType.class, "addPatientCorrelation", request);
                if (securedResp != null &&
                        securedResp.getMCCIIN000002UV01() != null) {
                    response.setMCCIIN000002UV01(securedResp.getMCCIIN000002UV01());
                }
            }
        }
        catch (Exception ex)
        {
            log.error("Error calling addPatientCorrelation: " + ex.getMessage(), ex);
        }

        log.debug("End addPatientCorrelation");
        return response;
    }
}
