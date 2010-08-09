/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docquery.nhinc.deferred.request;

import gov.hhs.healthit.nhin.DocQueryAcknowledgementType;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Unsecured Nhin proxy for DocQueryDeferredRequest service
 *
 * @author patlollav
 */
@WebService(serviceName = "NhincProxyDocQueryDeferredRequest", portName = "NhincProxyDocQueryDeferredRequestPortSoap", endpointInterface = "gov.hhs.fha.nhinc.nhincproxydocquerydeferredrequest.NhincProxyDocQueryDeferredRequestPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincproxydocquerydeferredrequest", wsdlLocation = "WEB-INF/wsdl/NhincDocQueryDeferredRequest/NhincProxyDocQueryDeferredRequest.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class NhincDocQueryDeferredRequest {

    @Resource
    private WebServiceContext context;

    //Logger
    private static final Log logger = LogFactory.getLog(NhincDocQueryDeferredRequest.class);

    /**
     * Delegates method call to Implementation class in the core library
     *
     * @param crossGatewayQueryRequest
     * @return
     */
    public gov.hhs.healthit.nhin.DocQueryAcknowledgementType crossGatewayQueryRequest(
            gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayQueryRequestType crossGatewayQueryRequest) {

        if (getLogger().isDebugEnabled()){
            getLogger().debug("Beginning of NhincProxyDocQueryDeferredRequest.crossGatewayQueryRequest()");
        }

        // Fwd request to orchestrator
        DocQueryAcknowledgementType ackResponse = getNhincDocQueryDeferredRequestOrchImpl().crossGatewayQueryRequest(crossGatewayQueryRequest, context);

        if (getLogger().isDebugEnabled()){
            getLogger().debug("End of NhincProxyDocQueryDeferredRequest.crossGatewayQueryRequest()");
        }

        return ackResponse;
    }

    /**
     * Returns the static logger for this class
     * @return
     */
    protected Log getLogger(){
        return logger;
    }

    /**
     * Implementation class for 
     *
     * @return
     */
    protected NhincDocQueryDeferredRequestOrchImpl getNhincDocQueryDeferredRequestOrchImpl(){
        return new NhincDocQueryDeferredRequestOrchImpl();
    }
}
