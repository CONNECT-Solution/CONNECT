/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.adapterdocrepository;

import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.StringTokenizer;
import java.util.UUID;
import javax.xml.namespace.QName;
import com.sun.xml.ws.api.message.Headers;
import com.sun.xml.ws.api.message.Header;
import com.sun.xml.ws.developer.WSBindingProvider;
import javax.xml.ws.soap.MTOMFeature;

/**
 * This class calls a soap 1.2 enabled document repository given a soap 1.1
 * retrieve document set or provide and register document set request message.
 * This class was initially created to connect to the Vangent/HIEOS document
 * repository.
 *
 * @author shawc
 */
public class AdapterDocRepository2Soap12Client
{
    private static Log log = LogFactory.getLog(AdapterDocRepository2Soap12Client.class);
    private static String ADAPTER_XDS_REP_DEFAULT_SERVICE_URL = "http://localhost:50967/axis2/services/xdsrepositoryb";
    private static ihe.iti.xds_b._2007.DocumentRepositoryService service = null;

    /**
     * Default constructor.
     */
    public AdapterDocRepository2Soap12Client()
    {

    }

    /**
     * This method connects to a soap 1.2 enabled document repository and stores
     * a document given a ProvideAndRegisterDocumentSetRequestType object.
     * @param storeRequest A ProvideAndRegisterDocumentSetRequestType object containing
     *      the document and metadata to be stored.
     * @return Returns a RegistryResponseType indicating whether the document
     *      was successfully stored.
     */
    public oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType provideAndRegisterDocumentSet(ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType storeRequest)
    {
        oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType response = null;

        log.debug("Entering AdapterDocRepository2Soap12Client.provideAndRegisterDocumentSet() method");

        try
        {

            // Call Web Service Operation
            service = new ihe.iti.xds_b._2007.DocumentRepositoryService();
            ihe.iti.xds_b._2007.DocumentRepositoryPortType port = getSoap12Port(NhincConstants.WS_PROVIDE_AND_REGISTER_DOCUMENT_ACTION);


            // call the soap 1.2 provide and register document set web service
		int retryCount = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getRetryAttempts();
	int retryDelay = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getRetryDelay();
        String exceptionText = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getExceptionText();
        javax.xml.ws.WebServiceException catchExp = null;
        if (retryCount > 0 && retryDelay > 0 && exceptionText != null && !exceptionText.equalsIgnoreCase("")) {
            int i = 1;
            while (i <= retryCount) {
                try {
                    response = port.documentRepositoryProvideAndRegisterDocumentSetB(storeRequest);
                    break;
                } catch (javax.xml.ws.WebServiceException e) {
                    catchExp = e;
                    int flag = 0;
                    StringTokenizer st = new StringTokenizer(exceptionText, ",");
                    while (st.hasMoreTokens()) {
                        if (e.getMessage().contains(st.nextToken())) {
                            flag = 1;
                        }
                    }
                    if (flag == 1) {
                        log.warn("Exception calling ... web service: " + e.getMessage());
                        System.out.println("retrying the connection for attempt [ " + i + " ] after [ " + retryDelay + " ] seconds");
                        log.info("retrying attempt [ " + i + " ] the connection after [ " + retryDelay + " ] seconds");
                        i++;
                        try {
                            Thread.sleep(retryDelay);
                        } catch (InterruptedException iEx) {
                            log.error("Thread Got Interrupted while waiting on DocumentRepositoryService call :" + iEx);
                        } catch (IllegalArgumentException iaEx) {
                            log.error("Thread Got Interrupted while waiting on DocumentRepositoryService call :" + iaEx);
                        }
                        retryDelay = retryDelay + retryDelay; //This is a requirement from Customer
                    } else {
                        log.error("Unable to call DocumentRepositoryService Webservice due to  : " + e);
                        throw e;
                    }
                }
            }

            if (i > retryCount) {
                log.error("Unable to call DocumentRepositoryService Webservice due to  : " + catchExp);
                throw catchExp;
            }

        } else {
            response = port.documentRepositoryProvideAndRegisterDocumentSetB(storeRequest);
        }
            log.debug("ProvideAndRegisterRequest Response = " + response.getStatus());

        }
        catch (Exception e)
        {
            String sErrorMessage = "Failed to retrieve a handle to the soap 1.2 web service.  Error: " +
                                   e.getMessage();
            log.error(sErrorMessage, e);
            throw new RuntimeException(sErrorMessage, e);
        }

        log.debug("Leaving AdapterDocRepository2Soap12Client.provideAndRegisterDocumentSet() method");
        return response;
    }

    /**
     * This method connects to a soap 1.2 enabled document repository and retrieves
     * a document with the document id found in the given RetrieveDocumentSetRequestType
     * object.
     * @param retrieveRequest A RetrieveDocumentSetRequestType object containing
     *      a document id and repository id of the desired document.
     * @return Returns a RetrieveDocumentSetResponseType containing the desired
     *      document.
     */
    public ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType retrieveDocument(ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType retrieveRequest)
    {
        ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType response = null;

        log.debug("Entering AdapterDocRepository2Soap12Client.retrieveDocument() method");

        try
        {
            //get a connection to the soap 1.2 retrieve document web service
            ihe.iti.xds_b._2007.DocumentRepositoryPortType port = getSoap12Port(NhincConstants.WS_RETRIEVE_DOCUMENT_ACTION);

            // call the soap 1.2 retrieve document web service
            response = port.documentRepositoryRetrieveDocumentSet(retrieveRequest);
            log.debug("RetrieveDocumentSetRequest Response = " + response.getRegistryResponse().getStatus());
        }
        catch (Exception e)
        {
            String sErrorMessage = "Failed to retrieve the requested document from the soap 1.2 web service.  Error: " +
                                   e.getMessage();
            log.error(sErrorMessage, e);
            throw new RuntimeException(sErrorMessage, e);
        }

        log.debug("Leaving AdapterDocRepository2Soap12Client.retrieveDocument() method");
        return response;
    }

    /**
     * This method connects to a soap 1.2 enabled document repository based on the
     * configuration found in the internalConnectionInfo.xml file, creates the
     * appropriate soap 1.2 header and returns a DocumentRepositoryPortType object
     * so that a retrieve or provide and register document set request can be made
     * on a soap 1.2 enabled document repository.
     * @param action A string representing the soap header action needed to either
     *      retrieve or store a document.
     * @return Returns a DocumentRepositoryPortType object which will enable a retrieve
     *      or store document transaction.
     */
    private ihe.iti.xds_b._2007.DocumentRepositoryPortType getSoap12Port (String action)
    {
        log.debug("Entering AdapterDocRepository2Soap12Client.getSoap12Port() method");

        ihe.iti.xds_b._2007.DocumentRepositoryPortType port = null;

        try
        {
            // Call Web Service Operation
            service = new ihe.iti.xds_b._2007.DocumentRepositoryService();
            port = service.getDocumentRepositoryPortSoap12(new MTOMFeature());

            // Get the real endpoint URL for this service.
            //--------------------------------------------
            //Note, set the sEndpointURL to null and comment out the ConnectionMangerCache logic if running outside of GF.
            String sEndpointURL = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.ADAPTER_XDS_REP_SERVICE_NAME);

            if ((sEndpointURL == null) ||
                (sEndpointURL.length() <= 0))
            {
                sEndpointURL = ADAPTER_XDS_REP_DEFAULT_SERVICE_URL;
                String sErrorMessage = "Failed to retrieve the Endpoint URL for service: '" +
                                       NhincConstants.ADAPTER_XDS_REP_SERVICE_NAME + "'.  " +
                                       "Setting this to: '" + sEndpointURL + "'";
                log.warn(sErrorMessage);
            }
            
			gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().initializePort((javax.xml.ws.BindingProvider) port, sEndpointURL);
            //add the soap header
            List<Header> headers = new ArrayList<Header>();
            QName qname = new QName(NhincConstants.WS_ADDRESSING_URL, NhincConstants.WS_SOAP_HEADER_ACTION);
            Header tmpHeader = Headers.create(qname, action);
            headers.add(tmpHeader);
            qname = new QName(NhincConstants.WS_ADDRESSING_URL, NhincConstants.WS_SOAP_HEADER_TO);
            tmpHeader = Headers.create(qname, sEndpointURL);
            headers.add(tmpHeader);
            qname = new QName(NhincConstants.WS_ADDRESSING_URL, NhincConstants.WS_SOAP_HEADER_MESSAGE_ID);
            UUID oMessageId = UUID.randomUUID();
            String sMessageId = oMessageId.toString();
            tmpHeader = Headers.create(qname, NhincConstants.WS_SOAP_HEADER_MESSAGE_ID_PREFIX + sMessageId);
            headers.add(tmpHeader);

            ((WSBindingProvider)port).setOutboundHeaders(headers);
        }
        catch (Exception ex)
        {
            String sErrorMessage = "Failed to retrieve a handle to the soap 1.2 web service.  Error: " +
                                   ex.getMessage();
            log.error(sErrorMessage, ex);
            throw new RuntimeException(sErrorMessage, ex);

        }

        log.debug("Leaving AdapterDocRepository2Soap12Client.getSoap12Port() method");
        return port;
    }
}
