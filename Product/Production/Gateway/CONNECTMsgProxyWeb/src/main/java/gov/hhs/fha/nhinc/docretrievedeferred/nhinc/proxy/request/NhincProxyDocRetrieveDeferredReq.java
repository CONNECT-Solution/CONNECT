package gov.hhs.fha.nhinc.docretrievedeferred.nhinc.proxy.request;

import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayRetrieveRequestType;
import gov.hhs.fha.nhinc.docretrievedeferred.nhinc.request.NhincProxyDocRetrieveDeferredReqImpl;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 * This is an Nhinc Proxy Unsecure service for Document Retrieve deferred Request message
 * @author Sai Valluripalli
 */
@WebService(serviceName = "NhincProxyDocRetrieveDeferredRequest", portName = "NhincProxyDocRetrieveDeferredRequestPortSoap", endpointInterface = "gov.hhs.fha.nhinc.nhincproxydocretrievedeferred.NhincProxyDocRetrieveDeferredRequestPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincproxydocretrievedeferred", wsdlLocation = "WEB-INF/wsdl/NhincProxyDocRetrieveDeferredReq/NhincProxyDocumentRetrieveDeferredRequest.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class NhincProxyDocRetrieveDeferredReq {

    @Resource
    private WebServiceContext context;

    /**
     * This is the cross gateway retrieve request operation exposed as a service and call underlying implementation methods
     * @param crossGatewayRetrieveRequest
     * @return DocRetrieveAcknowledgementType
     */
    public DocRetrieveAcknowledgementType crossGatewayRetrieveRequest(RespondingGatewayCrossGatewayRetrieveRequestType crossGatewayRetrieveRequest) {
        AssertionType assertion = null;
        NhinTargetSystemType nhinTargetSystem = null;
        RetrieveDocumentSetRequestType retrieveDocumentSetRequest = null;
        DocRetrieveAcknowledgementType ack = null;
        if (null != crossGatewayRetrieveRequest) {
            assertion = extractAssertion(crossGatewayRetrieveRequest);
            if (null != assertion) {
                assertion.setMessageId(AsyncMessageIdExtractor.GetAsyncMessageId(context));
                assertion.getRelatesToList().add(AsyncMessageIdExtractor.GetAsyncRelatesTo(context));
            }
            nhinTargetSystem = extractNhinTargetSystem(crossGatewayRetrieveRequest);
            retrieveDocumentSetRequest = extractDocRetrieveRequest(crossGatewayRetrieveRequest);
            NhincProxyDocRetrieveDeferredReqImpl impl = new NhincProxyDocRetrieveDeferredReqImpl();
            ack = impl.crossGatewayRetrieveRequest(retrieveDocumentSetRequest, assertion, nhinTargetSystem);
        }
        return ack;
    }

    /**
     * This method returns assertion information from the Inbound Request object
     * @param crossGatewayRetrieveRequest
     * @return AssertionType
     */
    protected AssertionType extractAssertion(RespondingGatewayCrossGatewayRetrieveRequestType crossGatewayRetrieveRequest) {
        return crossGatewayRetrieveRequest.getAssertion();
    }

    /**
     * This method extracts Document Retrieve request from the Inbound Request Message
     * @param crossGatewayRetrieveRequest
     * @return RetrieveDocumentSetRequestType
     */
    protected RetrieveDocumentSetRequestType extractDocRetrieveRequest(RespondingGatewayCrossGatewayRetrieveRequestType crossGatewayRetrieveRequest) {
        return crossGatewayRetrieveRequest.getRetrieveDocumentSetRequest();
    }

    /**
     * This method extracts NhinTargetSystem from Inbound request object
     * @param crossGatewayRetrieveRequest
     * @return NhinTargetSystemType
     */
    protected NhinTargetSystemType extractNhinTargetSystem(RespondingGatewayCrossGatewayRetrieveRequestType crossGatewayRetrieveRequest) {
        return crossGatewayRetrieveRequest.getNhinTargetSystem();
    }
}
