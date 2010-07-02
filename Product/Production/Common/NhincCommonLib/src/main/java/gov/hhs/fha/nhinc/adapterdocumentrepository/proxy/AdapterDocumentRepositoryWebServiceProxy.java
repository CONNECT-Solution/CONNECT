/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.adapterdocumentrepository.proxy;

import gov.hhs.fha.nhinc.adapterdocretrieve.AdapterDocRetrieve;
import gov.hhs.fha.nhinc.adapterdocretrieve.AdapterDocRetrievePortType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import javax.xml.ws.BindingProvider;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayRetrieveRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;

/**
 *
 * @author rayj
 */
public class AdapterDocumentRepositoryWebServiceProxy implements AdapterDocumentRepositoryProxy {

    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(AdapterDocumentRepositoryWebServiceProxy.class);
    private static AdapterDocRetrieve adapterDocumentRepositoryService = new AdapterDocRetrieve();

    public RetrieveDocumentSetResponseType retrieveDocument(RetrieveDocumentSetRequestType retrieveDocumentSetRequestType, AssertionType assertion, NhinTargetSystemType target) throws Exception {
        AdapterDocRetrievePortType port = getPort(target);

        RespondingGatewayCrossGatewayRetrieveRequestType respondingGatewayCrossGatewayRetrieveRequest = new RespondingGatewayCrossGatewayRetrieveRequestType();
        respondingGatewayCrossGatewayRetrieveRequest.setAssertion(assertion);
        respondingGatewayCrossGatewayRetrieveRequest.setRetrieveDocumentSetRequest(retrieveDocumentSetRequestType);

        RetrieveDocumentSetResponseType result = port.respondingGatewayCrossGatewayRetrieve(respondingGatewayCrossGatewayRetrieveRequest);
        return result;
    }

    private AdapterDocRetrievePortType getPort(NhinTargetSystemType target) throws ConnectionManagerException {
        String serviceName = "adapterdocretrieve";
        String url = getUrl(target, serviceName);
        return getPort(url);
    }

    private AdapterDocRetrievePortType getPort(String url) {
        AdapterDocRetrievePortType port = adapterDocumentRepositoryService.getAdapterDocRetrievePortSoap11();

        gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().initializePort((javax.xml.ws.BindingProvider) port, url);

        return port;
    }

    private String getUrl(NhinTargetSystemType target, String serviceName) throws ConnectionManagerException {
        String url = null;
        url = ConnectionManagerCache.getEndpontURLFromNhinTarget(target, serviceName);
        if (NullChecker.isNullish(url)) {
            url = ConnectionManagerCache.getLocalEndpointURLByServiceName(serviceName);
        }
        return url;
    }
}
