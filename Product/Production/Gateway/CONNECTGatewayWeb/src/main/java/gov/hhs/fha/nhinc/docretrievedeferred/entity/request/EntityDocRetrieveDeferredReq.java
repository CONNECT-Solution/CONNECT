package gov.hhs.fha.nhinc.docretrievedeferred.entity.request;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayRetrieveRequestType;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import javax.jws.WebService;
import javax.xml.ws.BindingType;

/**
 * This is an Entity Unsecure service for Document Retrieve deferred Request message
 * @author Sai Valluripalli
 */
@WebService(serviceName = "EntityDocRetrieveDeferredRequest", portName = "EntityDocRetrieveDeferredRequestPortSoap", endpointInterface = "gov.hhs.fha.nhinc.entitydocretrieve.EntityDocRetrieveDeferredRequestPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:entitydocretrieve", wsdlLocation = "WEB-INF/wsdl/EntityDocRetrieveDeferredReq/EntityDocumentRetrieveDeferredRequest.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class EntityDocRetrieveDeferredReq {

    /**
     * The Entity Secured Method implementation for RespondingGatewayCrossGatewayRetrieveRequest makes call to actual implementation
     * @param crossGatewayRetrieveRequest
     * @return DocRetrieveAcknowledgementType
     */
    public DocRetrieveAcknowledgementType crossGatewayRetrieveRequest(RespondingGatewayCrossGatewayRetrieveRequestType crossGatewayRetrieveRequest) {
        ExtractEntityDocRetrieveDeferredRequestValues oExtract = new ExtractEntityDocRetrieveDeferredRequestValues();
        RetrieveDocumentSetRequestType retrieveDocumentSetRequest = oExtract.extractRetrieveDocumentSetRequestType(crossGatewayRetrieveRequest);
        AssertionType assertion = oExtract.extractAssertion(crossGatewayRetrieveRequest);
        NhinTargetCommunitiesType nhinTargetCommunities = oExtract.extractNhinTargetCommunities(crossGatewayRetrieveRequest);
        DocRetrieveAcknowledgementType ack = oExtract.getEntityDocRetrieveDeferredReqImpl().crossGatewayRetrieveRequest(retrieveDocumentSetRequest, assertion, nhinTargetCommunities);
        return ack;
    }
}
