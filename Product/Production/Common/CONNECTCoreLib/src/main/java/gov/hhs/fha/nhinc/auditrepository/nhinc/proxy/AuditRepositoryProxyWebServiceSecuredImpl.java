/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.auditrepository.nhinc.proxy;

import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.auditlog.LogEventSecureRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FindCommunitiesAndAuditEventsRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FindCommunitiesAndAuditEventsResponseType;
import gov.hhs.fha.nhinc.nhinccomponentauditrepository.AuditRepositoryManagerSecuredPortType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;

/**
 *
 * @author Jon Hoppesch
 */
public class AuditRepositoryProxyWebServiceSecuredImpl implements AuditRepositoryProxy {
    private static Service cachedService = null;
    private static final String NAMESPACE_URI = "urn:gov:hhs:fha:nhinc:nhinccomponentauditrepository";
    private static final String SERVICE_LOCAL_PART = "AuditRepositoryManagerSecuredService";
    private static final String PORT_LOCAL_PART = "AuditRepositoryManagerSecuredPort";
    private static final String WSDL_FILE = "NhincComponentAuditRepositorySecured.wsdl";
    private static final String WS_ADDRESSING_ACTION_LOG = "urn:gov:hhs:fha:nhinc:nhinccomponentauditrepository:LogEventSecuredRequest";
    private static final String WS_ADDRESSING_ACTION_FIND = "urn:gov:hhs:fha:nhinc:nhinccomponentauditrepository:QueryAuditEventsSecuredRequest";
    private Log log = null;

    private WebServiceProxyHelper oProxyHelper = new WebServiceProxyHelper();


    public AuditRepositoryProxyWebServiceSecuredImpl()
    {
        log = createLogger();
    }

    protected Log createLogger()
    {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    public FindCommunitiesAndAuditEventsResponseType auditQuery(FindCommunitiesAndAuditEventsRequestType request) {
        String url = null;
        FindCommunitiesAndAuditEventsResponseType result = new FindCommunitiesAndAuditEventsResponseType();

        try
        {
            if (request != null)
            {

                log.debug("Before target system URL look up.");
                url = oProxyHelper.getUrlLocalHomeCommunity(NhincConstants.AUDIT_REPO_SECURE_SERVICE_NAME);
                log.debug("After target system URL look up. URL for service: " + NhincConstants.AUDIT_REPO_SECURE_SERVICE_NAME + " is: " + url);
                if (NullChecker.isNotNullish(url))
                {
                    AuditRepositoryManagerSecuredPortType port = getPort(url, NhincConstants.AUDIT_REPO_SECURE_SERVICE_NAME, WS_ADDRESSING_ACTION_FIND, request.getAssertion());
                    result = (FindCommunitiesAndAuditEventsResponseType) oProxyHelper.invokePort(port, AuditRepositoryManagerSecuredPortType.class, "queryAuditEvents", request);
                }
                else
                {
                    log.error("Failed to call the web service (" + NhincConstants.AUDIT_REPO_SECURE_SERVICE_NAME + ").  The URL is null.");
                }
            }
        }
        catch (Exception e)
        {
            log.error("Failed to call the web service (" + NhincConstants.AUDIT_REPO_SECURE_SERVICE_NAME + ").  An unexpected exception occurred.  " +
                      "Exception: " + e.getMessage(), e);
        }

        return result;
    }

    public AcknowledgementType auditLog(LogEventRequestType request, AssertionType assertion) {
        log.debug("Entering AuditRepositoryProxyWebServiceSecured.auditLog(...)");
        String url = null;
        AcknowledgementType result = new AcknowledgementType();
        LogEventSecureRequestType secureRequest = new LogEventSecureRequestType();
        if(request.getAuditMessage() == null)
        {
            log.error("Audit Request is null");
        }
        secureRequest.setAuditMessage(request.getAuditMessage());
        secureRequest.setDirection(request.getDirection());
        secureRequest.setInterface(request.getInterface());

        try
        {
            if (request != null)
            {

                log.debug("Before target system URL look up.");
                url = oProxyHelper.getUrlLocalHomeCommunity(NhincConstants.AUDIT_REPO_SECURE_SERVICE_NAME);
                log.debug("After target system URL look up. URL for service: " + NhincConstants.AUDIT_REPO_SECURE_SERVICE_NAME + " is: " + url);
                if (NullChecker.isNotNullish(url))
                {
                    AuditRepositoryManagerSecuredPortType port = getPort(url, NhincConstants.AUDIT_REPO_SECURE_SERVICE_NAME, WS_ADDRESSING_ACTION_LOG, assertion);
                    result = (AcknowledgementType) oProxyHelper.invokePort(port, AuditRepositoryManagerSecuredPortType.class, "logEvent", secureRequest);
                }
                else
                {
                    log.error("Failed to call the web service (" + NhincConstants.AUDIT_REPO_SECURE_SERVICE_NAME + ").  The URL is null.");
                }
            }
        }
        catch (Exception e)
        {
            log.error("Failed to call the web service (" + NhincConstants.AUDIT_REPO_SECURE_SERVICE_NAME + ").  An unexpected exception occurred.  " +
                      "Exception: " + e.getMessage(), e);
        }

        log.debug("In AuditRepositoryProxyWebServiceSecured.auditLog(...) - completed called to ConnectionManager to retrieve endpoint.");

        return result;
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



    /**
     * This method retrieves and initializes the port.
     *
     * @param url The URL for the web service.
     * @param serviceAction The action for the web service.
     * @param wsAddressingAction The action assigned to the input parameter for the web service operation.
     * @param assertion The assertion information for the web service
     * @return The port object for the web service.
     */
    protected AuditRepositoryManagerSecuredPortType getPort(String url, String serviceAction, String wsAddressingAction, AssertionType assertion)
    {
        AuditRepositoryManagerSecuredPortType port = null;
        Service service = getService();
        if (service != null)
        {
            log.debug("Obtained service - creating port.");

            port = service.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), AuditRepositoryManagerSecuredPortType.class);
            oProxyHelper.initializeSecurePort((javax.xml.ws.BindingProvider) port, url, serviceAction, wsAddressingAction, assertion);
        }
        else
        {
            log.error("Unable to obtain serivce - no port created.");
        }
        return port;
    }

}
