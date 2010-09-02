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
import gov.hhs.fha.nhinc.nhinccomponentpatientcorrelation.PatientCorrelationPortType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
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
    private Log log = null;
    private static Service cachedService = null;
    private static final String NAMESPACE_URI = "urn:gov:hhs:fha:nhinc:nhinccomponentpatientcorrelation";
    private static final String SERVICE_LOCAL_PART = "PatientCorrelationService";
    private static final String PORT_LOCAL_PART = "PatientCorrelationPort";
    private static final String WSDL_FILE = "NhincComponentPatientCorrelation.wsdl";
    private static final String WS_ADDRESSING_ACTION_RETRIEVE = "urn:gov:hhs:fha:nhinc:nhinccomponentpatientcorrelation:RetrievePatientCorrelationsRequestMessage";
    private static final String WS_ADDRESSING_ACTION_ADD = "urn:gov:hhs:fha:nhinc:nhinccomponentpatientcorrelation:AddPatientCorrelationRequestMessage";
    private WebServiceProxyHelper oProxyHelper = null;

    public PatientCorrelationProxyWebServiceUnsecuredImpl()
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
    protected PatientCorrelationPortType getPort(String url, String serviceAction, String wsAddressingAction, AssertionType assertion)
    {
        PatientCorrelationPortType port = null;
        Service service = getService();
        if (service != null)
        {
            log.debug("Obtained service - creating port.");

            port = service.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), PatientCorrelationPortType.class);
            oProxyHelper.initializeUnsecurePort((javax.xml.ws.BindingProvider) port, url, wsAddressingAction, assertion);
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
        RetrievePatientCorrelationsResponseType response = null;

        try
        {
            String url = oProxyHelper.getUrlLocalHomeCommunity(NhincConstants.PATIENT_CORRELATION_SERVICE_NAME);
            PatientCorrelationPortType port = getPort(url, NhincConstants.DOC_QUERY_ACTION, WS_ADDRESSING_ACTION_RETRIEVE, assertion);

            if(msg == null)
            {
                log.error("Message was null");
            }
            else if(assertion == null)
            {
                log.error("assertion was null");
            }
            else if(port == null)
            {
                log.error("port was null");
            }
            else
            {
                RetrievePatientCorrelationsRequestType request = new RetrievePatientCorrelationsRequestType();
                request.setPRPAIN201309UV02(msg);
                request.setAssertion(assertion);

                response = (RetrievePatientCorrelationsResponseType)oProxyHelper.invokePort(port, PatientCorrelationPortType.class, "retrievePatientCorrelations", request);
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
        AddPatientCorrelationResponseType response = null;

        try
        {
            String url = oProxyHelper.getUrlLocalHomeCommunity(NhincConstants.PATIENT_CORRELATION_SERVICE_NAME);
            PatientCorrelationPortType port = getPort(url, NhincConstants.DOC_QUERY_ACTION, WS_ADDRESSING_ACTION_ADD, assertion);

            if(msg == null)
            {
                log.error("Message was null");
            }
            else if(assertion == null)
            {
                log.error("assertion was null");
            }
            else if(port == null)
            {
                log.error("port was null");
            }
            else
            {
                AddPatientCorrelationRequestType request = new AddPatientCorrelationRequestType();
                request.setPRPAIN201301UV02(msg);
                request.setAssertion(assertion);

                response = (AddPatientCorrelationResponseType)oProxyHelper.invokePort(port, PatientCorrelationPortType.class, "addPatientCorrelation", request);
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
