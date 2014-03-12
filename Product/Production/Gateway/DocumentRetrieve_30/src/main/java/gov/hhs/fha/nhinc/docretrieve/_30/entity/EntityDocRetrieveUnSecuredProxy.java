/**
 * 
 */
package gov.hhs.fha.nhinc.docretrieve._30.entity;

import gov.hhs.fha.nhinc.aspect.OutboundMessageEvent;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayRetrieveRequestType;
import gov.hhs.fha.nhinc.docretrieve.aspect.RetrieveDocumentSetResponseTypeDescriptionBuilder;
import gov.hhs.fha.nhinc.docretrieve.aspect.RetrieveDocumentSetTransformingBuilder;
import gov.hhs.fha.nhinc.docretrieve.entity.proxy.EntityDocRetrieveProxyObjectFactory;
import gov.hhs.fha.nhinc.entitydocretrieve.EntityDocRetrievePortType;
import gov.hhs.fha.nhinc.messaging.server.BaseService;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import javax.xml.ws.BindingType;
import javax.xml.ws.soap.Addressing;

/**
 * @author achidambaram
 * 
 */

@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@Addressing(enabled = true)
public class EntityDocRetrieveUnSecuredProxy extends BaseService implements EntityDocRetrievePortType {

    @OutboundMessageEvent(beforeBuilder = RetrieveDocumentSetTransformingBuilder.class, afterReturningBuilder = RetrieveDocumentSetResponseTypeDescriptionBuilder.class, serviceType = "Retrieve Document", version = "3.0")
    public RetrieveDocumentSetResponseType respondingGatewayCrossGatewayRetrieve(
            RespondingGatewayCrossGatewayRetrieveRequestType request) {

        EntityDocRetrieveProxyObjectFactory factory = new EntityDocRetrieveProxyObjectFactory();
        return factory.getEntityDocRetrieveProxy().respondingGatewayCrossGatewayRetrieve(
                request.getRetrieveDocumentSetRequest(), request.getAssertion(), request.getNhinTargetCommunities());
    }

}
