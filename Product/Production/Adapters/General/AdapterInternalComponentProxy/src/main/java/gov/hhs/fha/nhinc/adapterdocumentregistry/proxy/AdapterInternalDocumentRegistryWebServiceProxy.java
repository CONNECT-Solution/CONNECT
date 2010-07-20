package gov.hhs.fha.nhinc.adapterdocumentregistry.proxy;

import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import ihe.iti.xds_b._2007.DocumentRegistryPortType;
import ihe.iti.xds_b._2007.DocumentRegistryService;
import java.net.SocketTimeoutException;
import java.util.StringTokenizer;
import javax.xml.ws.WebServiceException;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Sai Valluripalli
 */
public class AdapterInternalDocumentRegistryWebServiceProxy implements gov.hhs.fha.nhinc.docregistryadapter.proxy.AdapterDocumentRegistryProxy {

    private static Log log = LogFactory.getLog(AdapterDocumentRegistryWebServiceProxy.class);
    private static String ADAPTER_PROPERTY_FILE_NAME = "adapter";
    private static String XDS_HOME_COMMUNITY_ID_PROPERTY = "XDSbHomeCommunityId";
    private static DocumentRegistryService documentRegistryservice = new DocumentRegistryService();

    /**
     * 
     * @param request
     * @return AdhocQueryResponse
     */
    public AdhocQueryResponse registryStoredQuery(AdhocQueryRequest request){
        AdhocQueryResponse result = null;
        String url = null;
        try {
            String xdsbHomeCommunityId = PropertyAccessor.getProperty(ADAPTER_PROPERTY_FILE_NAME, XDS_HOME_COMMUNITY_ID_PROPERTY);
            if (log.isDebugEnabled()) {
                log.debug("Value of " + XDS_HOME_COMMUNITY_ID_PROPERTY + " retrieved from the " + ADAPTER_PROPERTY_FILE_NAME + ".properties file: " + xdsbHomeCommunityId);
            }
            if (NullChecker.isNotNullish(xdsbHomeCommunityId)) {
                url = ConnectionManagerCache.getEndpointURLByServiceName(xdsbHomeCommunityId, NhincConstants.ADAPTER_DOC_REGISTRY_SERVICE_NAME);
            }

            if (NullChecker.isNullish(url)) {
                url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.ADAPTER_DOC_REGISTRY_SERVICE_NAME);
                if (NullChecker.isNotNullish(xdsbHomeCommunityId)) {
                    log.warn("The endpoint URL retrieved for " + XDS_HOME_COMMUNITY_ID_PROPERTY + " (" + xdsbHomeCommunityId + ") from the " + ADAPTER_PROPERTY_FILE_NAME + ".properties file was not found. The default local adapter doc registry endpoint will be used: " + url);
                }
            } else if (log.isDebugEnabled()) {
                log.debug("The doc registry endpoint URL retrieved for the " + XDS_HOME_COMMUNITY_ID_PROPERTY + " property was found: " + url);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        DocumentRegistryPortType port = getPort(url);
        
        int retryCount = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getRetryAttempts();
	int retryDelay = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getRetryDelay();
        String exceptionText = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getExceptionText();
        javax.xml.ws.WebServiceException catchExp = null;
        if (retryCount > 0 && retryDelay > 0 && exceptionText != null && !exceptionText.equalsIgnoreCase("")) {
            int i = 1;
            while (i <= retryCount) {
                try {
                    result = port.documentRegistryRegistryStoredQuery(request);
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
                            log.error("Thread Got Interrupted while waiting on DocumentRegistryService call :" + iEx);
                        } catch (IllegalArgumentException iaEx) {
                            log.error("Thread Got Interrupted while waiting on DocumentRegistryService call :" + iaEx);
                        }
                        retryDelay = retryDelay + retryDelay; //This is a requirement from Customer
                    } else {
                        log.error("Unable to call DocumentRegistryService Webservice due to  : " + e);
                        throw e;
                    }
                }
            }

            if (i > retryCount) {
                log.error("Unable to call DocumentRegistryService Webservice due to  : " + catchExp);
                throw catchExp;
            }

        } else {
            result = port.documentRegistryRegistryStoredQuery(request);
        }
        log.info("Result = " + result);

        return result;
    }

    /**
     * 
     * @param url
     * @return DocumentRegistryPortType
     */
    private DocumentRegistryPortType getPort(String url) {
        DocumentRegistryPortType port = documentRegistryservice.getDocumentRegistryPortSoap();

        gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().initializePort((javax.xml.ws.BindingProvider) port, url);

        return port;
    }
}
