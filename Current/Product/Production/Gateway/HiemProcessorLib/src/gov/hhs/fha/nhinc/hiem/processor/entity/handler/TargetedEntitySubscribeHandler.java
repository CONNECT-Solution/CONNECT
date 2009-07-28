package gov.hhs.fha.nhinc.hiem.processor.entity.handler;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunityType;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.oasis_open.docs.wsn.b_2.Subscribe;
import org.oasis_open.docs.wsn.bw_2.InvalidTopicExpressionFault;
import org.oasis_open.docs.wsn.bw_2.SubscribeCreationFailedFault;
import org.oasis_open.docs.wsn.bw_2.TopicNotSupportedFault;
import org.w3._2005._08.addressing.EndpointReferenceType;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.oasis_open.docs.wsn.b_2.SubscribeResponse;
import java.util.Iterator;
import javax.xml.ws.wsaddressing.W3CEndpointReference;
import gov.hhs.fha.nhinc.hiem.configuration.topicconfiguration.TopicConfigurationEntry;
import gov.hhs.fha.nhinc.xmlCommon.XmlUtility;
import org.w3c.dom.ls.LSException;

/**
 * Entity subscribe message handler for messages that are not patient centric.
 * One or more NHIN target system entries are required for this handler.
 *
 * @author Neil Webb
 */
class TargetedEntitySubscribeHandler extends BaseEntitySubscribeHandler
{
    public SubscribeResponse handleSubscribe(TopicConfigurationEntry topicConfig, Subscribe subscribe, Element subscribeElement, AssertionType assertion, NhinTargetCommunitiesType targetCommunitites) throws TopicNotSupportedFault, InvalidTopicExpressionFault, SubscribeCreationFailedFault
    {
        SubscribeResponse response = new SubscribeResponse();

        EndpointReferenceType parentSubscriptionReference = storeSubscription(subscribe, subscribeElement, assertion, targetCommunitites);
        String parentSubscriptionReferenceXml = null;
        if(parentSubscriptionReference != null)
        {
            parentSubscriptionReferenceXml = serializeEndpointReferenceType(parentSubscriptionReference);
        }
        // Determine targets
        NhinTargetCommunitiesType targets = getTargets(targetCommunitites);

        if((targets != null) && (targets.getNhinTargetCommunity() != null))
        {
            Iterator<NhinTargetCommunityType> targetCommunityIter = targets.getNhinTargetCommunity().iterator();
            while(targetCommunityIter.hasNext())
            {
                NhinTargetCommunityType community = targetCommunityIter.next();
        //      Update Subscribe
                updateSubscribeNotificationConsumerEndpointAddress(subscribeElement);
        //      Policy check - performed in proxy?
        //      Audit Event - performed in proxy?
        //      Send Subscribe
//                SubscribeRequestType subscribeRequest = buildSubscribeRequest(subscribe, assertion, community);
                Element childSubscribeElement = subscribeElement;
                SubscribeResponse subscribeResponse = sendSubscribeRequest(childSubscribeElement, assertion, community);

        //      Store subscription
                if(subscribeResponse != null)
                {
                    String childSubscriptionReference = null;

                    // Use reflection to get the correct subscription reference object
                    Object subRef = getSubscriptionReference(subscribeResponse);
                    if(subRef != null)
                    {
                        if(subRef.getClass().isAssignableFrom(EndpointReferenceType.class))
                        {
                            childSubscriptionReference = serializeEndpointReferenceType((EndpointReferenceType)subRef);
                        }
                        else if(subRef.getClass().isAssignableFrom(W3CEndpointReference.class))
                        {
                            childSubscriptionReference = serializeW3CEndpointReference((W3CEndpointReference)subRef);
                        }
                        else
                        {
                            log.error("Unknown subscription reference type: " + subRef.getClass().getName());
                        }
                        String childSubscribeXml ;
                        try {
                            childSubscribeXml = XmlUtility.serializeElement(childSubscribeElement);
                        } catch (Exception ex) {
                            log.error("failed to process subscribe xml", ex);
                            childSubscribeXml = null;
                        }
                        storeChildSubscription(childSubscribeXml, childSubscriptionReference, parentSubscriptionReferenceXml);
                    }
                    else
                    {
                        log.error("Subscription reference was null");
                    }
                }
                else
                {
                    log.error("The subscribe response message was null.");
                }
            }
        }
        setSubscriptionReference(response, parentSubscriptionReference);

        return response;
    }
    
    private NhinTargetCommunitiesType getTargets(NhinTargetCommunitiesType targetCommunities)
    {
        return targetCommunities;
    }

}
