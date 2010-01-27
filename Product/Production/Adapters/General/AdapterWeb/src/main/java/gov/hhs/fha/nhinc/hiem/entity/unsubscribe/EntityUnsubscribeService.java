package gov.hhs.fha.nhinc.hiem.entity.unsubscribe;

import gov.hhs.fha.nhinc.entitysubscriptionmanagement.ResourceUnknownFault;
import gov.hhs.fha.nhinc.entitysubscriptionmanagement.UnableToDestroySubscriptionFault;
import javax.jws.WebService;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "EntitySubscriptionManager", portName = "EntitySubscriptionManagerPortSoap11", endpointInterface = "gov.hhs.fha.nhinc.entitysubscriptionmanagement.EntitySubscriptionManagerPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:entitysubscriptionmanagement", wsdlLocation = "WEB-INF/wsdl/EntityUnsubscribeService/EntitySubscriptionManagement.wsdl")
public class EntityUnsubscribeService {

    public org.oasis_open.docs.wsn.b_2.UnsubscribeResponse unsubscribe(gov.hhs.fha.nhinc.common.nhinccommonentity.UnsubscribeRequestType unsubscribeRequest) throws ResourceUnknownFault, UnableToDestroySubscriptionFault {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

}
