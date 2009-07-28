/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.adapterdocquery.proxy;

import gov.hhs.fha.nhinc.adapterdocquery.AdapterDocQuery;
import gov.hhs.fha.nhinc.adapterdocquery.AdapterDocQueryPortType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayQueryRequestType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.docquery.adapter.proxy.AdapterDocQueryProxy;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import javax.xml.ws.BindingProvider;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author svalluripalli
 */
public class AdapterDocQueryWebServiceProxy implements AdapterDocQueryProxy
{
    private static Log log = LogFactory.getLog(AdapterDocQueryWebServiceProxy.class);
    private static AdapterDocQuery adapterDocQueryservice = new AdapterDocQuery();

    /**
     *
     * @param respondingGatewayCrossGatewayQueryReq
     * @return AdhocQueryResponse
     */
    public AdhocQueryResponse respondingGatewayCrossGatewayQuery(RespondingGatewayCrossGatewayQueryRequestType
            respondingGatewayCrossGatewayQueryReq)
    {
        log.debug("Entering AdapterDocQueryWebServiceProxy.respondingGatewayCrossGatewayQuery()");
        AdhocQueryResponse result = null;
        try {
            String url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.ADAPTER_DOC_QUERY_SERVICE_NAME);
            AdapterDocQueryPortType port = getPort(url);
            result = port.respondingGatewayCrossGatewayQuery(respondingGatewayCrossGatewayQueryReq);
            System.out.println("Result = "+result);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        log.debug("End AdapterDocQueryWebServiceProxy.respondingGatewayCrossGatewayQuery()");
        return result;
    }

    /**
     * 
     * @param url
     * @return AdapterDocQueryPortType
     */
    private AdapterDocQueryPortType getPort(String url) {
        AdapterDocQueryPortType port = adapterDocQueryservice.getAdapterDocQueryPortSoap11();
        log.info("Setting endpoint address to Nhin Audit Query Service to " + url);
        ((BindingProvider) port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);
        return port;
    }

}
