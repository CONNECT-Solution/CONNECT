package gov.hhs.fha.nhinc.hiem.entity.unsubscribe;

import gov.hhs.fha.nhinc.entitysubscriptionmanagement.ResourceUnknownFault;
import gov.hhs.fha.nhinc.entitysubscriptionmanagement.UnableToDestroySubscriptionFault;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;
import javax.jws.HandlerChain;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "EntitySubscriptionManager", portName = "EntitySubscriptionManagerPortSoap11", endpointInterface = "gov.hhs.fha.nhinc.entitysubscriptionmanagement.EntitySubscriptionManagerPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:entitysubscriptionmanagement", wsdlLocation = "WEB-INF/wsdl/EntitySubscribeService/EntitySubscriptionManagement.wsdl")
@HandlerChain(file = "EntityUnsubscribeSoapHeaderHandler.xml")
public class EntityUnsubscribeService {

    @Resource
    private WebServiceContext context;
    
    public org.oasis_open.docs.wsn.b_2.UnsubscribeResponse unsubscribe(gov.hhs.fha.nhinc.common.nhinccommonentity.UnsubscribeRequestType unsubscribeRequest) throws ResourceUnknownFault, UnableToDestroySubscriptionFault {
        return new EntityUnsubscribeServiceImpl().unsubscribe(unsubscribeRequest, context);
    }

}
