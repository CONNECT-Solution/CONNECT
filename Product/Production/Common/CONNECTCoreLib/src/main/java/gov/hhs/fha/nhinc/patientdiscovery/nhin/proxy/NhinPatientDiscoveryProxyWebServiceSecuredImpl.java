/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package gov.hhs.fha.nhinc.patientdiscovery.nhin.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.perfrepo.PerformanceManager;
import gov.hhs.fha.nhinc.transform.subdisc.HL7PRPA201306Transforms;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import ihe.iti.xcpd._2009.RespondingGatewayPortType;
import java.sql.Timestamp;
import javax.xml.namespace.QName;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import javax.xml.ws.Service;

/**
 *
 * @author jhoppesc
 */
public class NhinPatientDiscoveryProxyWebServiceSecuredImpl implements NhinPatientDiscoveryProxy {

    private static Service cachedService = null;
    private static final String NAMESPACE_URI = "urn:ihe:iti:xcpd:2009";
    private static final String SERVICE_LOCAL_PART = "RespondingGateway_Service";
    private static final String PORT_LOCAL_PART = "RespondingGateway_Port_Soap";
    private static final String WSDL_FILE = "NhinPatientDiscovery.wsdl";
    private static final String WS_ADDRESSING_ACTION = "urn:hl7-org:v3:PRPA_IN201305UV02:CrossGatewayPatientDiscovery";
    private Log log = null;
    private WebServiceProxyHelper oProxyHelper = new WebServiceProxyHelper();

    /**
     * Default constructor.
     */
    public NhinPatientDiscoveryProxyWebServiceSecuredImpl() {
        log = createLogger();
    }

    /**
     * Creates the log object for logging.
     *
     * @return The log object.
     */
    protected Log createLogger() {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    public PRPAIN201306UV02 respondingGatewayPRPAIN201305UV02(PRPAIN201305UV02 request, AssertionType assertion, NhinTargetSystemType target) {
        String url = null;
        PRPAIN201306UV02 response = new PRPAIN201306UV02();

        try {
            if (request != null && target != null) {
                log.debug("Before target system URL look up.");
                url = oProxyHelper.getUrlFromTargetSystem(target, NhincConstants.PATIENT_DISCOVERY_SERVICE_NAME);
                log.debug("After target system URL look up. URL for service: " + NhincConstants.PATIENT_DISCOVERY_SERVICE_NAME + " is: " + url);

                if (NullChecker.isNotNullish(url)) {
                    RespondingGatewayPortType port = getPort(url, NhincConstants.PATIENT_DISCOVERY_ACTION, WS_ADDRESSING_ACTION, assertion);

                    // Log the start of the performance record
                    String targetCommunityId = "";

                    if ((target != null) &&
                            (target.getHomeCommunity() != null)) {
                        targetCommunityId = target.getHomeCommunity().getHomeCommunityId();
                    }

                    Timestamp starttime = new Timestamp(System.currentTimeMillis());
                    Long logId = PerformanceManager.getPerformanceManagerInstance().logPerformanceStart(starttime, NhincConstants.PATIENT_DISCOVERY_SERVICE_NAME, NhincConstants.AUDIT_LOG_NHIN_INTERFACE, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, targetCommunityId);

                    response = (PRPAIN201306UV02) oProxyHelper.invokePort(port, RespondingGatewayPortType.class, "respondingGatewayPRPAIN201305UV02", request);

                    // Log the end of the performance record
                    Timestamp stoptime = new Timestamp(System.currentTimeMillis());
                    PerformanceManager.getPerformanceManagerInstance().logPerformanceStop(logId, starttime, stoptime);

                } else {
                    log.error("Failed to call the web service (" + NhincConstants.PATIENT_DISCOVERY_SERVICE_NAME + ").  The URL is null.");
                }
            } else {
                log.error("Failed to call the web service (" + NhincConstants.PATIENT_DISCOVERY_SERVICE_NAME + ").  The input parameters are null.");
            }
        } catch (Exception e) {
            log.error("Failed to call the web service (" + NhincConstants.PATIENT_DISCOVERY_SERVICE_NAME + ").  An unexpected exception occurred.  " +
                    "Exception: " + e.getMessage(), e);
            response = new HL7PRPA201306Transforms().createPRPA201306ForErrors(request, NhincConstants.PATIENT_DISCOVERY_ANSWER_NOT_AVAIL_ERR_CODE);
        }

        return response;
    }

    /**
     * Retrieve the service class for this web service.
     *
     * @return The service class for this web service.
     */
    protected Service getService() {
        if (cachedService == null) {
            try {
                cachedService = oProxyHelper.createService(WSDL_FILE, NAMESPACE_URI, SERVICE_LOCAL_PART);
            } catch (Throwable t) {
                log.error("Error creating service: " + t.getMessage(), t);
            }
        }
        return cachedService;
    }

    /**
     * This method retrieves and initializes the port.
     *
     * @param url The URL for the web service.
     * @param serviceAction The action for the web service.
     * @param wsAddressingAction The action assigned to the input parameter for the web service operation.
     * @param assertion The assertion information for the web service
     * @return The port object for the web service.
     */
    protected RespondingGatewayPortType getPort(String url, String serviceAction, String wsAddressingAction, AssertionType assertion) {
        RespondingGatewayPortType port = null;
        Service service = getService();
        if (service != null) {
            log.debug("Obtained service - creating port.");

            port = service.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), RespondingGatewayPortType.class);
            oProxyHelper.initializeSecurePort((javax.xml.ws.BindingProvider) port, url, serviceAction, wsAddressingAction, assertion);
        } else {
            log.error("Unable to obtain serivce - no port created.");
        }
        return port;
    }
}
