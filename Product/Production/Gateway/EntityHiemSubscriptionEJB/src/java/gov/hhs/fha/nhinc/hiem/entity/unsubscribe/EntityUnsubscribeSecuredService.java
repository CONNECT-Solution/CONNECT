package gov.hhs.fha.nhinc.hiem.entity.unsubscribe;

import gov.hhs.fha.nhinc.entitysubscriptionmanagementsecured.ResourceUnknownFault;
import gov.hhs.fha.nhinc.entitysubscriptionmanagementsecured.UnableToDestroySubscriptionFault;
import gov.hhs.fha.nhinc.entitysubscriptionmanagementsecured.EntitySubscriptionManagerSecuredPortType;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;
import javax.annotation.Resource;
import org.oasis_open.docs.wsn.b_2.Unsubscribe;
import org.oasis_open.docs.wsn.b_2.UnsubscribeResponse;

/**
 *
 * @author dunnek
 */
@WebService(serviceName = "EntitySubscriptionManagerSecured", portName = "EntitySubscriptionManagerSecuredPortSoap11", endpointInterface = "gov.hhs.fha.nhinc.entitysubscriptionmanagementsecured.EntitySubscriptionManagerSecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:entitysubscriptionmanagementsecured", wsdlLocation = "META-INF/wsdl/EntityUnsubscribeSecuredService/EntitySubscriptionManagementSecured.wsdl")
@Stateless
public class EntityUnsubscribeSecuredService implements EntitySubscriptionManagerSecuredPortType
{

    @Resource
    private WebServiceContext context;

    public UnsubscribeResponse unsubscribe(Unsubscribe unsubscribeRequest) throws ResourceUnknownFault, UnableToDestroySubscriptionFault
    {
        return new EntityUnsubscribeServiceImpl().unsubscribe(unsubscribeRequest, context);
    }
}
