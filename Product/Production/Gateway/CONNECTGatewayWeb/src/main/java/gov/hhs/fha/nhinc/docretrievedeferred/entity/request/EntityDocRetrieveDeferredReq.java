package gov.hhs.fha.nhinc.docretrievedeferred.entity.request;

import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayRetrieveRequestType;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 * This is an Entity Unsecure service for Document Retrieve deferred Request message
 * @author Sai Valluripalli
 */
@WebService(serviceName = "EntityDocRetrieveDeferredRequest", portName = "EntityDocRetrieveDeferredRequestPortSoap", endpointInterface = "gov.hhs.fha.nhinc.entitydocretrieve.EntityDocRetrieveDeferredRequestPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:entitydocretrieve", wsdlLocation = "WEB-INF/wsdl/EntityDocRetrieveDeferredReq/EntityDocumentRetrieveDeferredRequest.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class EntityDocRetrieveDeferredReq {

    @Resource
    private WebServiceContext context;

    /**
     * The Entity Secured Method implementation for RespondingGatewayCrossGatewayRetrieveRequest makes call to actual implementation
     * @param crossGatewayRetrieveRequest
     * @return DocRetrieveAcknowledgementType
     */
    public DocRetrieveAcknowledgementType crossGatewayRetrieveRequest(RespondingGatewayCrossGatewayRetrieveRequestType crossGatewayRetrieveRequest) {
        RetrieveDocumentSetRequestType retrieveDocumentSetRequest = getDocRequest(crossGatewayRetrieveRequest);
        AssertionType assertion = getAssertionInfo(crossGatewayRetrieveRequest);
        if (null != assertion) {
            assertion.setMessageId(AsyncMessageIdExtractor.GetAsyncMessageId(context));
            assertion.getRelatesToList().add(AsyncMessageIdExtractor.GetAsyncRelatesTo(context));
        }
        NhinTargetCommunitiesType nhinTargetCommunities = getTargetCommunities(crossGatewayRetrieveRequest);
        DocRetrieveAcknowledgementType ack = getAckFromImpl(retrieveDocumentSetRequest, assertion, nhinTargetCommunities);
        return ack;
    }

    /**
     * 
     * @param oExtract
     * @param retrieveDocumentSetRequest
     * @param assertion
     * @param nhinTargetCommunities
     * @return DocRetrieveAcknowledgementType
     */
    protected DocRetrieveAcknowledgementType getAckFromImpl(RetrieveDocumentSetRequestType retrieveDocumentSetRequest, AssertionType assertion, NhinTargetCommunitiesType nhinTargetCommunities) {
        return new ExtractEntityDocRetrieveDeferredRequestValues().getEntityDocRetrieveDeferredReqImpl().crossGatewayRetrieveRequest(retrieveDocumentSetRequest, assertion, nhinTargetCommunities);
    }

    /**
     * 
     * @param oExtract
     * @param crossGatewayRetrieveRequest
     * @return AssertionType
     */
    protected AssertionType getAssertionInfo(RespondingGatewayCrossGatewayRetrieveRequestType crossGatewayRetrieveRequest) {
        return new ExtractEntityDocRetrieveDeferredRequestValues().extractAssertion(crossGatewayRetrieveRequest);
    }

    /**
     * 
     * @param oExtract
     * @param crossGatewayRetrieveRequest
     * @return RetrieveDocumentSetRequestType
     */
    protected RetrieveDocumentSetRequestType getDocRequest(RespondingGatewayCrossGatewayRetrieveRequestType crossGatewayRetrieveRequest) {
        return new ExtractEntityDocRetrieveDeferredRequestValues().extractRetrieveDocumentSetRequestType(crossGatewayRetrieveRequest);
    }

    /**
     * 
     * @param oExtract
     * @param crossGatewayRetrieveRequest
     * @return NhinTargetCommunitiesType
     */
    protected NhinTargetCommunitiesType getTargetCommunities(RespondingGatewayCrossGatewayRetrieveRequestType crossGatewayRetrieveRequest) {
        return new ExtractEntityDocRetrieveDeferredRequestValues().extractNhinTargetCommunities(crossGatewayRetrieveRequest);
    }
}
