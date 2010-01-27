package gov.hhs.fha.nhinc.hiem.entity.subscribe;

import gov.hhs.fha.nhinc.entitysubscriptionmanagement.InvalidFilterFault;
import gov.hhs.fha.nhinc.entitysubscriptionmanagement.InvalidMessageContentExpressionFault;
import gov.hhs.fha.nhinc.entitysubscriptionmanagement.InvalidProducerPropertiesExpressionFault;
import gov.hhs.fha.nhinc.entitysubscriptionmanagement.InvalidTopicExpressionFault;
import gov.hhs.fha.nhinc.entitysubscriptionmanagement.NotifyMessageNotSupportedFault;
import gov.hhs.fha.nhinc.entitysubscriptionmanagement.ResourceUnknownFault;
import gov.hhs.fha.nhinc.entitysubscriptionmanagement.SubscribeCreationFailedFault;
import gov.hhs.fha.nhinc.entitysubscriptionmanagement.TopicExpressionDialectUnknownFault;
import gov.hhs.fha.nhinc.entitysubscriptionmanagement.TopicNotSupportedFault;
import gov.hhs.fha.nhinc.entitysubscriptionmanagement.UnacceptableInitialTerminationTimeFault;
import gov.hhs.fha.nhinc.entitysubscriptionmanagement.UnrecognizedPolicyRequestFault;
import gov.hhs.fha.nhinc.entitysubscriptionmanagement.UnsupportedPolicyRequestFault;
import javax.jws.WebService;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "EntityNotificationProducer", portName = "EntityNotificationProducerPortSoap11", endpointInterface = "gov.hhs.fha.nhinc.entitysubscriptionmanagement.EntityNotificationProducerPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:entitysubscriptionmanagement", wsdlLocation = "WEB-INF/wsdl/EntitySubscribeService/EntitySubscriptionManagement.wsdl")
public class EntitySubscribeService {

    public gov.hhs.fha.nhinc.common.nhinccommonentity.SubscribeDocumentResponseType subscribeDocument(gov.hhs.fha.nhinc.common.nhinccommonentity.SubscribeDocumentRequestType subscribeDocumentRequest) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public gov.hhs.fha.nhinc.common.nhinccommonentity.SubscribeCdcBioPackageResponseType subscribeCdcBioPackage(gov.hhs.fha.nhinc.common.nhinccommonentity.SubscribeCdcBioPackageRequestType subscribeCdcBioPackageRequest) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public org.oasis_open.docs.wsn.b_2.SubscribeResponse subscribe(gov.hhs.fha.nhinc.common.nhinccommonentity.SubscribeRequestType subscribeRequest) throws UnrecognizedPolicyRequestFault, InvalidMessageContentExpressionFault, UnsupportedPolicyRequestFault, TopicNotSupportedFault, ResourceUnknownFault, NotifyMessageNotSupportedFault, InvalidTopicExpressionFault, InvalidFilterFault, SubscribeCreationFailedFault, InvalidProducerPropertiesExpressionFault, TopicExpressionDialectUnknownFault, UnacceptableInitialTerminationTimeFault {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

}
