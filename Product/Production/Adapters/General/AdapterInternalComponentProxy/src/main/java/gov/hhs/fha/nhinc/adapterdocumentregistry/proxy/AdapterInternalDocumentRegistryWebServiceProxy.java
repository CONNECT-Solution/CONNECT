/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.adapterdocumentregistry.proxy;

import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.docregistryadapter.proxy.AdapterDocumentRegistryProxy;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import ihe.iti.xds_b._2007.DocumentRegistryPortType;
import ihe.iti.xds_b._2007.DocumentRegistryService;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author svalluripalli
 */
public class AdapterInternalDocumentRegistryWebServiceProxy implements gov.hhs.fha.nhinc.docregistryadapter.proxy.AdapterDocumentRegistryProxy
{
    private static Log log = LogFactory.getLog(AdapterDocumentRegistryWebServiceProxy.class);
    private static String ADAPTER_PROPERTY_FILE_NAME = "adapter";
    private static String XDS_HOME_COMMUNITY_ID_PROPERTY = "XDSbHomeCommunityId";
    private static DocumentRegistryService documentRegistryservice = new DocumentRegistryService();

    /**
     * 
     * @param request
     * @return AdhocQueryResponse
     */
    public AdhocQueryResponse registryStoredQuery(AdhocQueryRequest request) {
        AdhocQueryResponse result = null;
        String url = null;
        try {
            String xdsbHomeCommunityId = PropertyAccessor.getProperty(ADAPTER_PROPERTY_FILE_NAME, XDS_HOME_COMMUNITY_ID_PROPERTY);
            if(log.isDebugEnabled())
            {
                log.debug("Value of " + XDS_HOME_COMMUNITY_ID_PROPERTY + " retrieved from the " + ADAPTER_PROPERTY_FILE_NAME + ".properties file: " + xdsbHomeCommunityId);
            }
            if(NullChecker.isNotNullish(xdsbHomeCommunityId))
            {
                url = ConnectionManagerCache.getEndpointURLByServiceName(xdsbHomeCommunityId, NhincConstants.ADAPTER_DOC_REGISTRY_SERVICE_NAME);
            }
            
            if(NullChecker.isNullish(url))
            {
                url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.ADAPTER_DOC_REGISTRY_SERVICE_NAME);
                if(NullChecker.isNotNullish(xdsbHomeCommunityId))
                {
                    log.warn("The endpoint URL retrieved for " + XDS_HOME_COMMUNITY_ID_PROPERTY + " (" + xdsbHomeCommunityId + ") from the " + ADAPTER_PROPERTY_FILE_NAME + ".properties file was not found. The default local adapter doc registry endpoint will be used: " + url);
                }
            }
            else if(log.isDebugEnabled())
            {
                log.debug("The doc registry endpoint URL retrieved for the " + XDS_HOME_COMMUNITY_ID_PROPERTY + " property was found: " + url);
            }
            DocumentRegistryPortType port = getPort(url);
            result = port.documentRegistryRegistryStoredQuery(request);
            System.out.println("Result = "+result);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return result;
    }

    /**
     * 
     * @param url
     * @return DocumentRegistryPortType
     */
    private DocumentRegistryPortType getPort(String url)
    {
        DocumentRegistryPortType port = documentRegistryservice.getDocumentRegistryPortSoap();

        gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().initializePort((javax.xml.ws.BindingProvider) port, url);

        return port;
    }
    
}
