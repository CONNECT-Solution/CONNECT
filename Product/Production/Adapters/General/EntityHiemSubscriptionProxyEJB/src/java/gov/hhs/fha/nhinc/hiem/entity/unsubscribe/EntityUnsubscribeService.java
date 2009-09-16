package gov.hhs.fha.nhinc.hiem.entity.unsubscribe;

import gov.hhs.fha.nhinc.entitysubscriptionmanagement.EntitySubscriptionManagerPortType;
import gov.hhs.fha.nhinc.entitysubscriptionmanagement.ResourceUnknownFault;
import gov.hhs.fha.nhinc.entitysubscriptionmanagement.UnableToDestroySubscriptionFault;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author dunnek
 */
@WebService(serviceName = "EntitySubscriptionManager", portName = "EntitySubscriptionManagerPortSoap11", endpointInterface = "gov.hhs.fha.nhinc.entitysubscriptionmanagement.EntitySubscriptionManagerPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:entitysubscriptionmanagement", wsdlLocation = "META-INF/wsdl/EntityUnsubscribeService/EntitySubscriptionManagement.wsdl")
@Stateless
public class EntityUnsubscribeService implements EntitySubscriptionManagerPortType
{

    @Resource
    private WebServiceContext context;

    public org.oasis_open.docs.wsn.b_2.UnsubscribeResponse unsubscribe(gov.hhs.fha.nhinc.common.nhinccommonentity.UnsubscribeRequestType unsubscribeRequest) throws UnableToDestroySubscriptionFault, ResourceUnknownFault
    {
        return new EntityUnsubscribeServiceImpl().unsubscribe(unsubscribeRequest, context);
    }
}
