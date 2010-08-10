package gov.hhs.fha.nhinc.policyengine.adapter.pip.proxy;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtDocIdRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtDocIdResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtIdRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtIdResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.StorePtConsentRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.StorePtConsentResponseType;

import gov.hhs.fha.nhinc.policyengine.adapterpip.AdapterPIPException;

import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import gov.hhs.fha.nhinc.adapterpip.AdapterPIP;
import gov.hhs.fha.nhinc.adapterpip.AdapterPIPPortType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

/**
 * This is the concrete implementation for the Web Service based call to the
 * AdapterPIP.
 *
 * @author Les Westberg
 */
public class AdapterPIPWebServiceProxy implements AdapterPIPProxy {

    private Log log = null;
    private static Service cachedService = null;
    private static final String NAMESPACE_URI = "urn:gov:hhs:fha:nhinc:adapterpip";
    private static final String SERVICE_LOCAL_PART = "AdapterPIP";
    private static final String PORT_LOCAL_PART = "AdapterPIPPortSoap";
    private static final String WSDL_FILE = "AdapterPIP.wsdl";
    private static final String WS_ADDRESSING_ACTION_RETRIEVEPTCONSENTBYPTID = "urn:gov:hhs:fha:nhinc:adapterpip:RetrievePtConsentByPtIdRequest";
    private static final String WS_ADDRESSING_ACTION_RETRIEVEPTCONSENTBYPTDOCID = "urn:gov:hhs:fha:nhinc:adapterpip:RetrievePtConsentByPtDocIdRequest";
    private static final String WS_ADDRESSING_ACTION_STOREPTCONSENT = "urn:gov:hhs:fha:nhinc:adapterpip:StorePtConsentRequest";

    private WebServiceProxyHelper oProxyHelper = null;

    public AdapterPIPWebServiceProxy()
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

    protected String invokeConnectionManager(String serviceName) throws ConnectionManagerException
    {
        return ConnectionManagerCache.getLocalEndpointURLByServiceName(serviceName);
    }

    protected String getEndpointURL()
    {
        String endpointURL = null;
        String serviceName = NhincConstants.ADAPTER_PIP_SERVICE_NAME;
        try
        {
            endpointURL = invokeConnectionManager(serviceName);
            log.debug("Retrieved endpoint URL for service " + serviceName + ": " + endpointURL);
        }
        catch (ConnectionManagerException ex)
        {
            log.error("Error getting url for " + serviceName + " from the connection manager. Error: " + ex.getMessage(), ex);
        }

        return endpointURL;
    }

    /**
     * This method retrieves and initializes the port.
     *
     * @param url The URL for the web service.
     * @param wsAddressingAction The action assigned to the input parameter for the web service operation.
     * @param assertion The assertion information for the web service
     * @return The port object for the web service.
     */
    protected AdapterPIPPortType getPort(String url, String wsAddressingAction, AssertionType assertion)
    {
        AdapterPIPPortType port = null;
        Service service = getService();
        if (service != null)
        {
            log.debug("Obtained service - creating port.");

            port = service.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), AdapterPIPPortType.class);
            oProxyHelper.initializeUnsecurePort((javax.xml.ws.BindingProvider) port, url, wsAddressingAction, assertion);
//            oProxyHelper.initializePort((javax.xml.ws.BindingProvider) port, url, serviceAction, wsAddressingAction, assertion);
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

//    /**
//     * Return a handle to the AdapterPIP web service.
//     *
//     * @return The handle to the Adapter PIP port web service.
//     */
//    private AdapterPIPPortType getAdapterPIPPort()
//            throws AdapterPIPException {
//        AdapterPIPPortType oAdapterPIPPort = null;
//
//        try {
//            if (oAdapterPIPService == null) {
//                oAdapterPIPService = new AdapterPIP();
//            }
//
//            oAdapterPIPPort = oAdapterPIPService.getAdapterPIPPortSoap();
//
//            // Get the real endpoint URL for this service.
//            //--------------------------------------------
//            String sEndpointURL = ConnectionManagerCache.getLocalEndpointURLByServiceName(ADAPTER_PIP_SERVICE_NAME);
//
//            if (sEndpointURL != null &&
//                    sEndpointURL.length() > 0) {
//                ((javax.xml.ws.BindingProvider) oAdapterPIPPort).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, sEndpointURL);
//            } else {
//                String sErrorMessage = "Failed to retrieve the Endpoint URL for service: '" +
//                        ADAPTER_PIP_SERVICE_NAME + "'.  " +
//                        "Setting this to: '" + sEndpointURL + "'";
//                log.error(sErrorMessage);
//            }
//        } catch (Exception e) {
//            String sErrorMessage = "Failed to retrieve a handle to the Adapter PIP web service.  Error: " +
//                    e.getMessage();
//            log.error(sErrorMessage, e);
//            throw new AdapterPIPException(sErrorMessage, e);
//        }
//
//        return oAdapterPIPPort;
//    }

    /**
     * Retrieve the patient consent settings for the given patient ID.
     *
     * @param request The patient ID for which the consent is being retrieved.
     * @return The patient consent information for that patient.
     */
    public RetrievePtConsentByPtIdResponseType retrievePtConsentByPtId(RetrievePtConsentByPtIdRequestType request) {
        RetrievePtConsentByPtIdResponseType oResponse = new RetrievePtConsentByPtIdResponseType();

        try
        {
            String url = getEndpointURL();
            AssertionType assertion = request.getAssertion();
            AdapterPIPPortType oAdapterPIPPort = getPort(url, WS_ADDRESSING_ACTION_RETRIEVEPTCONSENTBYPTID, assertion);
            oResponse = oAdapterPIPPort.retrievePtConsentByPtId(request);
        } catch (Exception e) {
            String sErrorMessage = "Error occurred calling AdapterPIPWebServiceProxy.retrievePtConsentByPtId.  Error: " +
                    e.getMessage();
            log.error(sErrorMessage, e);
            throw new RuntimeException(sErrorMessage, e);
        }

        return oResponse;
    }

    /**
     * Retrieve the patient consent settings for the patient associated with
     * the given document identifiers.
     *
     * @param request The doucment identifiers of a document in the repository.
     * @return The patient consent settings for the patient associated with
     *         the given document identifiers.
     */
    public RetrievePtConsentByPtDocIdResponseType retrievePtConsentByPtDocId(RetrievePtConsentByPtDocIdRequestType request) {
        RetrievePtConsentByPtDocIdResponseType oResponse = new RetrievePtConsentByPtDocIdResponseType();

        try {
            String url = getEndpointURL();
            AssertionType assertion = request.getAssertion();
            AdapterPIPPortType oAdapterPIPPort = getPort(url, WS_ADDRESSING_ACTION_RETRIEVEPTCONSENTBYPTDOCID, assertion);
            oResponse = oAdapterPIPPort.retrievePtConsentByPtDocId(request);
        } catch (Exception e) {
            String sErrorMessage = "Error occurred calling AdapterPIPWebServiceProxy.retrievePtConsentByPtDocId.  Error: " +
                    e.getMessage();
            log.error(sErrorMessage, e);
            throw new RuntimeException(sErrorMessage, e);
        }

        return oResponse;
    }

    /**
     * Store the patient consent information into the repository.
     *
     * @param request The patient consent settings to be stored.
     * @return Status of the storage.  Currently this is either "SUCCESS" or
     *         or the word "FAILED" followed by a ':' followed by the error information.
     */
    public StorePtConsentResponseType storePtConsent(StorePtConsentRequestType request) {
        StorePtConsentResponseType oResponse = new StorePtConsentResponseType();

        try {
            String url = getEndpointURL();
            AssertionType assertion = request.getAssertion();
            AdapterPIPPortType oAdapterPIPPort = getPort(url, WS_ADDRESSING_ACTION_STOREPTCONSENT, assertion);
            oResponse = oAdapterPIPPort.storePtConsent(request);
        } catch (Exception e) {
            String sErrorMessage = "Error occurred calling AdapterPIPWebServiceProxy.storePtConsent.  Error: " +
                    e.getMessage();
            oResponse.setStatus("FAILED: " + sErrorMessage);
            log.error(sErrorMessage, e);
            throw new RuntimeException(sErrorMessage, e);
        }

        return oResponse;
    }
}
