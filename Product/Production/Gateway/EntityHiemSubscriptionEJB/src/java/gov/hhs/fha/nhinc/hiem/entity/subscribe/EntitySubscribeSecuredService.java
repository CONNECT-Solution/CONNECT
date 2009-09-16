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
import org.oasis_open.docs.wsn.b_2.SubscribeResponse;

/**
 *
 * @author dunnek
 */
@WebService(serviceName = "EntityNotificationProducerSecured", portName = "EntityNotificationProducerSecuredPortSoap11", endpointInterface = "gov.hhs.fha.nhinc.entitysubscriptionmanagement.EntityNotificationProducerSecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:entitysubscriptionmanagement", wsdlLocation = "META-INF/wsdl/EntitySubscribeSecuredService/EntitySubscriptionManagementSecured.wsdl")
@Stateless
public class EntitySubscribeSecuredService implements EntityNotificationProducerSecuredPortType
{

    @Resource
    private WebServiceContext context;

    public SubscribeDocumentResponseType subscribeDocument(SubscribeDocumentRequestSecuredType subscribeRequest)
    {
        return new EntitySubscribeServiceImpl().subscribeDocument(subscribeRequest);
    }

    public SubscribeResponse subscribe(SubscribeRequestSecuredType subscribeRequestSecured) throws InvalidFilterFault, InvalidMessageContentExpressionFault, InvalidProducerPropertiesExpressionFault, InvalidTopicExpressionFault, NotifyMessageNotSupportedFault, ResourceUnknownFault, SubscribeCreationFailedFault, TopicExpressionDialectUnknownFault, TopicNotSupportedFault, UnacceptableInitialTerminationTimeFault, UnrecognizedPolicyRequestFault, UnsupportedPolicyRequestFault
    {
        return new EntitySubscribeServiceImpl().subscribe(subscribeRequestSecured, context);
    }
}
