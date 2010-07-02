/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.adapterdocumentregistry.proxy;

import gov.hhs.fha.nhinc.adapterdocquery.AdapterDocQueryPortType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import javax.xml.ws.BindingProvider;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayQueryRequestType;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;

/**
 *
 * @author rayj
 */
public class AdapterDocumentRegistryWebServiceProxy implements AdapterDocumentRegistryProxy {

    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(AdapterDocumentRegistryWebServiceProxy.class);
    private static gov.hhs.fha.nhinc.adapterdocquery.AdapterDocQuery adapterDocumentRegistryService = new gov.hhs.fha.nhinc.adapterdocquery.AdapterDocQuery();

    public AdhocQueryResponse queryForDocument(AdhocQueryRequest adhocQuery, AssertionType assertion, NhinTargetSystemType target) throws Exception {
        AdapterDocQueryPortType port = getPort(target);

        RespondingGatewayCrossGatewayQueryRequestType request = new RespondingGatewayCrossGatewayQueryRequestType();
        request.setAssertion(assertion);
        request.setAdhocQueryRequest(adhocQuery);
        AdhocQueryResponse result = port.respondingGatewayCrossGatewayQuery(request);
        return result;
    }

    private gov.hhs.fha.nhinc.adapterdocquery.AdapterDocQueryPortType getPort(NhinTargetSystemType target) throws ConnectionManagerException {
        String serviceName = "adapterdocquery";  //todo: move this to const in nhinclib
        String url = getUrl(target, serviceName);
        return getPort(url);
    }

    private gov.hhs.fha.nhinc.adapterdocquery.AdapterDocQueryPortType getPort(String url) {
        gov.hhs.fha.nhinc.adapterdocquery.AdapterDocQueryPortType port = adapterDocumentRegistryService.getAdapterDocQueryPortSoap11();

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
