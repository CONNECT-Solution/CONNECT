/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.hiem.entity.unsubscribe;

import gov.hhs.fha.nhinc.entitysubscriptionmanagement.EntitySubscriptionManagerSecuredPortType;
import gov.hhs.fha.nhinc.entitysubscriptionmanagement.ResourceUnknownFault;
import gov.hhs.fha.nhinc.entitysubscriptionmanagement.UnableToDestroySubscriptionFault;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;
import javax.annotation.Resource;
/**
 *
 * @author dunnek
 */
@WebService(serviceName = "EntitySubscriptionManagerSecured", portName = "EntitySubscriptionManagerSecuredPortSoap11", endpointInterface = "gov.hhs.fha.nhinc.entitysubscriptionmanagement.EntitySubscriptionManagerSecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:entitysubscriptionmanagement", wsdlLocation = "META-INF/wsdl/EntityUnsubscribeSecuredService/EntitySubscriptionManagementSecured.wsdl")
@Stateless
public class EntityUnsubscribeSecuredService implements EntitySubscriptionManagerSecuredPortType {
    @Resource
    private WebServiceContext context;
    public org.oasis_open.docs.wsn.b_2.UnsubscribeResponse unsubscribe(gov.hhs.fha.nhinc.common.nhinccommonentity.UnsubscribeRequestSecuredType unsubscribeRequest) throws ResourceUnknownFault, UnableToDestroySubscriptionFault {

        return new EntityUnsubscribeServiceImpl().unsubscribe(unsubscribeRequest, context) ;
    }

}
