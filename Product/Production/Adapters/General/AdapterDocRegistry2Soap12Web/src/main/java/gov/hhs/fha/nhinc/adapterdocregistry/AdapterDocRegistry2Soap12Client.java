/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.adapterdocregistry;

import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.UUID;
import javax.xml.namespace.QName;
import com.sun.xml.ws.api.message.Headers;
import com.sun.xml.ws.api.message.Header;
import com.sun.xml.ws.developer.WSBindingProvider;

/**
 * This class calls a SOAP 1.2 enabled document registry given a SOAP 1.1
 * registry stored query request message.
 *
 * @author Anand Sastry
 */
public class AdapterDocRegistry2Soap12Client
{
    private static Log log = LogFactory.getLog(AdapterDocRegistry2Soap12Client.class);
    private static String ADAPTER_XDS_REG_DEFAULT_SERVICE_URL = "http://localhost:8080/axis2/services/xdsregistryb";
    private static ihe.iti.xds_b._2007.DocumentRegistryService service = null;

    // CONSTANTS - Were not created in NhincConstants to simplify provisioning of this component
    // as an adapter between CONNECT and SOAP 1.2 Registry.
    public static final String WS_REGISTRY_STORED_QUERY_ACTION = "urn:ihe:iti:2007:RegistryStoredQuery";
    public static final String ADAPTER_XDS_REG_SERVICE_NAME = "adapterxdsbdocregistrysoap12";
    /**
     * Default constructor.
     */
    public AdapterDocRegistry2Soap12Client()
    {

    }

    /**
     * This method connects to a soap 1.2 enabled document registry and retrieves
     * metadata.
     * @param body A AdhocQueryRequest object containing key criteria to query for registry metadata.
     *
     * @return Returns a AdhocQueryResponse containing the desired metadata.
     *
     */

    public oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse documentRegistryRegistryStoredQuery(oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest body) {

        oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse response = null;

        log.debug("Entering AdapterDocRegistry2Soap12Client.documentRegistryRegistryStoredQuery() method");

        try
        {
            //get a connection to the soap 1.2 registryStoreQuery document web service
            ihe.iti.xds_b._2007.DocumentRegistryPortType port = getSoap12Port(WS_REGISTRY_STORED_QUERY_ACTION);

            // call the soap 1.2 retrieve document web service
            response = port.documentRegistryRegistryStoredQuery(body);
            log.debug("RetrieveDocumentSetRequest Response = " + ((response != null) ? response.getStatus() : "null"));
        }
        catch (Exception e)
        {
            String sErrorMessage = "Failed to execute registry stored query from the soap 1.2 web service.  Error: " +
                                   e.getMessage();
            log.error(sErrorMessage, e);
            throw new RuntimeException(sErrorMessage, e);
        }

        log.debug("Leaving AdapterDocRegistry2Soap12Client.documentRegistryRegistryStoredQuery() method");
        return response;
    }

    /**
     * This method connects to a SOAP 1.2 enabled document registry based on the
     * configuration found in the internalConnectionInfo.xml file, creates the
     * appropriate SOAP 1.2 header and returns a DocumentRegistryPortType object
     * so that a registry stored query request can be made on a SOAP 1.2 enabled document registry.
     * @param action A string representing the soap header action needed to perform a registry stored query.
     * @return Returns a DocumentRegistryPortType object which will enable the registry stored query txn.
     */
    private ihe.iti.xds_b._2007.DocumentRegistryPortType getSoap12Port (String action)
    {
        log.debug("Entering AdapterDocRegistry2Soap12Client.getSoap12Port() method");

        ihe.iti.xds_b._2007.DocumentRegistryPortType port = null;

        try
        {
            // Call Web Service Operation
            service = new ihe.iti.xds_b._2007.DocumentRegistryService();
            port = service.getDocumentRegistryPortSoap12();

            // Get the real endpoint URL for this service.
            //--------------------------------------------
            //Note, set the sEndpointURL to null and comment out the ConnectionMangerCache logic if running outside of GF.
            String sEndpointURL = ConnectionManagerCache.getLocalEndpointURLByServiceName(ADAPTER_XDS_REG_SERVICE_NAME);

            if ((sEndpointURL == null) ||
                (sEndpointURL.length() <= 0))
            {
                sEndpointURL = ADAPTER_XDS_REG_DEFAULT_SERVICE_URL;
                String sErrorMessage = "Failed to retrieve the Endpoint URL for service: '" +
                                       ADAPTER_XDS_REG_SERVICE_NAME + "'.  " +
                                       "Setting this to: '" + sEndpointURL + "'";
                log.warn(sErrorMessage);
            }

			gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().initializePort((javax.xml.ws.BindingProvider) port,sEndpointURL);
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

        log.debug("Leaving AdapterDocRegistry2Soap12Client.getSoap12Port() method");
        return port;
    }
}
