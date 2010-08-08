package gov.hhs.fha.nhinc.docretrievedeferred.entity.response;

import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayRetrieveResponseType;
import gov.hhs.fha.nhinc.docretrieve.entity.deferred.response.EntityDocRetrieveDeferredRespImpl;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 * Webservice class for Entity Document retrieve deferred response
 * @author Sai Valluripalli
 */
@WebService(serviceName = "EntityDocRetrieveDeferredResponse", portName = "EntityDocRetrieveDeferredResponsePortSoap", endpointInterface = "gov.hhs.fha.nhinc.entitydocretrieve.EntityDocRetrieveDeferredResponsePortType", targetNamespace = "urn:gov:hhs:fha:nhinc:entitydocretrieve", wsdlLocation = "WEB-INF/wsdl/EntityDocRetrieveDeferredResp/EntityDocumentRetrieveDeferredResponse.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class EntityDocRetrieveDeferredResp {

    @Resource
    WebServiceContext context;

    /**
     * Entity Document retrieve deferred response operation
     * @param crossGatewayRetrieveResponse
     * @return DocRetrieveAcknowledgementType
     */
    public DocRetrieveAcknowledgementType crossGatewayRetrieveResponse(RespondingGatewayCrossGatewayRetrieveResponseType crossGatewayRetrieveResponse) {
        DocRetrieveAcknowledgementType ack = null;
        if (null != crossGatewayRetrieveResponse) {
            RetrieveDocumentSetResponseType retrieveDocumentSetResponse = crossGatewayRetrieveResponse.getRetrieveDocumentSetResponse();
            AssertionType assertion = crossGatewayRetrieveResponse.getAssertion();
            if (null != assertion) {
                assertion.setMessageId(AsyncMessageIdExtractor.GetAsyncMessageId(context));
                assertion.getRelatesToList().add(AsyncMessageIdExtractor.GetAsyncRelatesTo(context));
            }
            NhinTargetCommunitiesType nhinTargetCommunities = crossGatewayRetrieveResponse.getNhinTargetCommunities();
            ack = sendToCrossGatewayRetrieveResponseImpl(retrieveDocumentSetResponse, assertion, nhinTargetCommunities);
        }
        return ack;
    }

    /**
     *
     * @param retrieveDocumentSetResponse
     * @param assertion
     * @param nhinTargetCommunities
     * @return DocRetrieveAcknowledgementType
     */
    protected DocRetrieveAcknowledgementType sendToCrossGatewayRetrieveResponseImpl(RetrieveDocumentSetResponseType retrieveDocumentSetResponse, AssertionType assertion, NhinTargetCommunitiesType nhinTargetCommunities) {
        return new EntityDocRetrieveDeferredRespImpl().crossGatewayRetrieveResponse(retrieveDocumentSetResponse, assertion, nhinTargetCommunities);
    }
}
