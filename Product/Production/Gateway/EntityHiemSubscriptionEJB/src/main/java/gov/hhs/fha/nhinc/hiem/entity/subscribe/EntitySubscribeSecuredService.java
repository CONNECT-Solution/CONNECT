/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.hiem.entity.subscribe;

import gov.hhs.fha.nhinc.common.nhinccommonentity.SubscribeDocumentRequestSecuredType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.SubscribeDocumentResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.SubscribeRequestSecuredType;
import gov.hhs.fha.nhinc.entitysubscriptionmanagementsecured.InvalidFilterFault;
import gov.hhs.fha.nhinc.entitysubscriptionmanagementsecured.InvalidMessageContentExpressionFault;
import gov.hhs.fha.nhinc.entitysubscriptionmanagementsecured.InvalidProducerPropertiesExpressionFault;
import gov.hhs.fha.nhinc.entitysubscriptionmanagementsecured.InvalidTopicExpressionFault;
import gov.hhs.fha.nhinc.entitysubscriptionmanagementsecured.NotifyMessageNotSupportedFault;
import gov.hhs.fha.nhinc.entitysubscriptionmanagementsecured.ResourceUnknownFault;
import gov.hhs.fha.nhinc.entitysubscriptionmanagementsecured.SubscribeCreationFailedFault;
import gov.hhs.fha.nhinc.entitysubscriptionmanagementsecured.TopicExpressionDialectUnknownFault;
import gov.hhs.fha.nhinc.entitysubscriptionmanagementsecured.TopicNotSupportedFault;
import gov.hhs.fha.nhinc.entitysubscriptionmanagementsecured.UnacceptableInitialTerminationTimeFault;
import gov.hhs.fha.nhinc.entitysubscriptionmanagementsecured.UnrecognizedPolicyRequestFault;
import gov.hhs.fha.nhinc.entitysubscriptionmanagementsecured.UnsupportedPolicyRequestFault;
import gov.hhs.fha.nhinc.entitysubscriptionmanagementsecured.EntityNotificationProducerSecuredPortType;
import javax.jws.WebService;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.xml.ws.WebServiceContext;
import javax.jws.HandlerChain;

/**
 *
 * @author dunnek
 */
@WebService(serviceName = "EntityNotificationProducerSecured", portName = "EntityNotificationProducerSecuredPortSoap11", endpointInterface = "gov.hhs.fha.nhinc.entitysubscriptionmanagementsecured.EntityNotificationProducerSecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:entitysubscriptionmanagementsecured", wsdlLocation = "META-INF/wsdl/EntitySubscribeSecuredService/EntitySubscriptionManagementSecured.wsdl")
@Stateless
@HandlerChain(file = "EntitySubscribeSoapHeaderHandler.xml")
public class EntitySubscribeSecuredService implements EntityNotificationProducerSecuredPortType {
    @Resource
    private WebServiceContext context;
    
    public gov.hhs.fha.nhinc.common.nhinccommonentity.SubscribeDocumentResponseType subscribeDocument(gov.hhs.fha.nhinc.common.nhinccommonentity.SubscribeDocumentRequestSecuredType subscribeDocumentRequestSecured) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public org.oasis_open.docs.wsn.b_2.SubscribeResponse subscribe(gov.hhs.fha.nhinc.common.nhinccommonentity.SubscribeRequestSecuredType subscribeRequestSecured) throws ResourceUnknownFault, InvalidFilterFault, InvalidMessageContentExpressionFault, InvalidProducerPropertiesExpressionFault, SubscribeCreationFailedFault, UnrecognizedPolicyRequestFault, UnsupportedPolicyRequestFault, TopicNotSupportedFault, TopicExpressionDialectUnknownFault, NotifyMessageNotSupportedFault, UnacceptableInitialTerminationTimeFault, InvalidTopicExpressionFault {
				return new EntitySubscribeServiceImpl().subscribe(subscribeRequestSecured, context);
    }

}
