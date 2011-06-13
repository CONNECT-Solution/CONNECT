/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docretrieve.nhin.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import ihe.iti.xds_b._2007.RespondingGatewayRetrievePortType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.docrepository.DocumentProcessHelper;
import gov.hhs.fha.nhinc.perfrepo.PerformanceManager;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;
import javax.xml.ws.Service;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import java.sql.Timestamp;
import javax.xml.namespace.QName;

import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 *
 * @author Neil Webb
 */
public class NhinDocRetrieveProxyWebServiceSecuredImpl implements NhinDocRetrieveProxy {

    private static Service cachedService = null;
    private static final String NAMESPACE_URI = "urn:ihe:iti:xds-b:2007";
    private static final String SERVICE_LOCAL_PART = "RespondingGateway_Retrieve_Service";
    private static final String PORT_LOCAL_PART = "RespondingGateway_Retrieve_Port_Soap";
    private static final String WSDL_FILE = "NhinDocRetrieve.wsdl";
    // NOTE: The WS_ADDRESSING_ACTION does not match the pattern defined. BUT it does match the .wsdl
    private static final String WS_ADDRESSING_ACTION = "urn:ihe:iti:2007:CrossGatewayRetrieve";
    private Log log = null;
    private WebServiceProxyHelper oProxyHelper = new WebServiceProxyHelper();

    /**
     * Default constructor.
     */
    public NhinDocRetrieveProxyWebServiceSecuredImpl() {
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

    protected DocumentProcessHelper getDocumentProcessHelper() {
        return new DocumentProcessHelper();
    }

    /**
     * Retrieve the document(s) specified in the request.
     *
     * @param request The identifier(s) of the document(s) to be retrieved.
     * @param targetSystem The target system where the message is being sent to.
     * @return The document(s) that were retrieved.
     */
    public RetrieveDocumentSetResponseType respondingGatewayCrossGatewayRetrieve(RetrieveDocumentSetRequestType request, AssertionType assertion, NhinTargetSystemType targetSystem) {
        String url = null;
        RetrieveDocumentSetResponseType response = new RetrieveDocumentSetResponseType();
        String sServiceName = NhincConstants.DOC_RETRIEVE_SERVICE_NAME;

        try {
            if (request != null) {
                log.debug("Before target system URL look up.");
                url = oProxyHelper.getUrlFromTargetSystem(targetSystem, sServiceName);
                log.debug("After target system URL look up. URL for service: " + sServiceName + " is: " + url);

                if (NullChecker.isNotNullish(url)) {
                    RespondingGatewayRetrievePortType port = getPort(url, NhincConstants.DOC_RETRIEVE_ACTION, WS_ADDRESSING_ACTION, assertion);

                    // Log the start of the performance record
                    String targetHomeCommunityId = HomeCommunityMap.getCommunityIdFromTargetSystem(targetSystem);
                    Timestamp starttime = new Timestamp(System.currentTimeMillis());
                    Long logId = PerformanceManager.getPerformanceManagerInstance().logPerformanceStart(starttime, NhincConstants.DOC_RETRIEVE_SERVICE_NAME, NhincConstants.AUDIT_LOG_NHIN_INTERFACE, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, targetHomeCommunityId);

                    response = (RetrieveDocumentSetResponseType) oProxyHelper.invokePort(port, RespondingGatewayRetrievePortType.class, "respondingGatewayCrossGatewayRetrieve", request);

                    // Check for Demo Mode
                    if (DocumentProcessHelper.isDemoOperationModeEnabled()) {
                        log.debug("CONNECT Demo Operation Mode Enabled");
                        DocumentProcessHelper documentProcessHelper = getDocumentProcessHelper();

                    // Demo mode enabled, process RetrieveDocumentSetRequestType to save document content to the CONNECT default document repository
                    documentProcessHelper.documentRepositoryProvideAndRegisterDocumentSet(response);
                    } else {
                        log.debug("CONNECT Demo Operation Mode Disabled");
                    }

                    // Log the end of the performance record
                    Timestamp stoptime = new Timestamp(System.currentTimeMillis());
                    PerformanceManager.getPerformanceManagerInstance().logPerformanceStop(logId, starttime, stoptime);
                } else {
                    log.error("Failed to call the web service (" + sServiceName + ").  The URL is null.");
                }
            } else {
                log.error("Failed to call the web service (" + sServiceName + ").  The input parameter is null.");
            }
        } catch (Exception e) {
            log.error("Failed to call the web service (" + sServiceName + ").  An unexpected exception occurred.  " +
                    "Exception: " + e.getMessage(), e);
            RegistryResponseType regResp = new RegistryResponseType();

            regResp.setStatus("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure");

            RegistryError registryError = new RegistryError();
            registryError.setCodeContext("Processing Adapter Doc Query document retrieve");
            registryError.setErrorCode("XDSRepostoryError");
            registryError.setSeverity("Error");
            regResp.getRegistryErrorList().getRegistryError().add(registryError);
            response.setRegistryResponse(regResp);
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
    protected RespondingGatewayRetrievePortType getPort(String url, String serviceAction, String wsAddressingAction, AssertionType assertion) {
        RespondingGatewayRetrievePortType port = null;
        Service service = getService();
        if (service != null) {
            log.debug("Obtained service - creating port.");

            port = service.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), RespondingGatewayRetrievePortType.class);
            oProxyHelper.initializeSecurePort((javax.xml.ws.BindingProvider) port, url, serviceAction, wsAddressingAction, assertion);
        } else {
            log.error("Unable to obtain serivce - no port created.");
        }
        return port;
    }
}
