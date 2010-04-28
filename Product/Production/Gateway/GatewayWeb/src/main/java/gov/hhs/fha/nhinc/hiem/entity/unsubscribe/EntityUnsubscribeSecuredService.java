package gov.hhs.fha.nhinc.hiem.entity.unsubscribe;

import gov.hhs.fha.nhinc.entitysubscriptionmanagementsecured.ResourceUnknownFault;
import gov.hhs.fha.nhinc.entitysubscriptionmanagementsecured.UnableToDestroySubscriptionFault;
import javax.jws.WebService;
import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;
import javax.jws.HandlerChain;

/**
 *
 * @author Neil Webb
 */
@WebService(serviceName = "EntitySubscriptionManagerSecured", portName = "EntitySubscriptionManagerSecuredPortSoap11", endpointInterface = "gov.hhs.fha.nhinc.entitysubscriptionmanagementsecured.EntitySubscriptionManagerSecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:entitysubscriptionmanagementsecured", wsdlLocation = "WEB-INF/wsdl/EntitySubscribeSecuredService/EntitySubscriptionManagementSecured.wsdl")
@HandlerChain(file = "EntityUnsubscribeSoapHeaderHandler.xml")
public class EntityUnsubscribeSecuredService
{
    @Resource
    private WebServiceContext context;

    public org.oasis_open.docs.wsn.b_2.UnsubscribeResponse unsubscribe(org.oasis_open.docs.wsn.b_2.Unsubscribe unsubscribeRequestSecured) throws ResourceUnknownFault, UnableToDestroySubscriptionFault
    {
        return new EntityUnsubscribeServiceImpl().unsubscribe(unsubscribeRequestSecured, context);
    }

}
