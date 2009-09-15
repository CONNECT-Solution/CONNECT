/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.hiem.entity.subscribe;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.entitysubscriptionmanagement.EntityNotificationProducerSecured;
import gov.hhs.fha.nhinc.entitysubscriptionmanagement.EntityNotificationProducerSecuredPortType;
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
//import gov.hhs.fha.nhinc.hiem.processor.entity.EntitySubscribeProcessor;
import org.oasis_open.docs.wsn.b_2.Subscribe;
import org.oasis_open.docs.wsn.b_2.SubscribeResponse;
import javax.xml.ws.WebServiceContext;
//import gov.hhs.fha.nhinc.hiem.dte.SoapUtil;
//import org.oasis_open.docs.wsn.bw_2.InvalidTopicExpressionFault;
//import org.oasis_open.docs.wsn.bw_2.SubscribeCreationFailedFault;
//import org.oasis_open.docs.wsn.bw_2.TopicNotSupportedFault;
import org.w3c.dom.Element;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import java.util.Map;
import javax.xml.ws.BindingProvider;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.SubscribeRequestSecuredType;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;

/**
 *
 * @author dunnek
 */
public class EntitySubscribeServiceImpl
{
    private static Log log = LogFactory.getLog(EntitySubscribeServiceImpl.class);
    private static EntityNotificationProducerSecured service = new EntityNotificationProducerSecured();

    public gov.hhs.fha.nhinc.common.nhinccommonentity.SubscribeDocumentResponseType subscribeDocument(gov.hhs.fha.nhinc.common.nhinccommonentity.SubscribeDocumentRequestType subscribeDocumentRequest)
    {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public gov.hhs.fha.nhinc.common.nhinccommonentity.SubscribeCdcBioPackageResponseType subscribeCdcBioPackage(gov.hhs.fha.nhinc.common.nhinccommonentity.SubscribeCdcBioPackageRequestType subscribeCdcBioPackageRequest)
    {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }
    public org.oasis_open.docs.wsn.b_2.SubscribeResponse subscribe(gov.hhs.fha.nhinc.common.nhinccommonentity.SubscribeRequestType subscribeRequest, WebServiceContext context) throws InvalidFilterFault, InvalidMessageContentExpressionFault, InvalidProducerPropertiesExpressionFault, InvalidTopicExpressionFault, NotifyMessageNotSupportedFault, ResourceUnknownFault, SubscribeCreationFailedFault, TopicExpressionDialectUnknownFault, TopicNotSupportedFault, UnacceptableInitialTerminationTimeFault, UnrecognizedPolicyRequestFault, UnsupportedPolicyRequestFault
    {
        log.debug("begin notify");
        SubscribeResponse result = null;

        String url = getURL();
        EntityNotificationProducerSecuredPortType port = getPort(url);

        AssertionType assertIn = subscribeRequest.getAssertion();

        SamlTokenCreator tokenCreator = new SamlTokenCreator();
        Map requestContext = tokenCreator.CreateRequestContext(assertIn, url, NhincConstants.HIEM_NOTIFY_ENTITY_SERVICE_NAME_SECURED);
        ((BindingProvider) port).getRequestContext().putAll(requestContext);

        SubscribeRequestSecuredType securedRequest = new SubscribeRequestSecuredType();

        securedRequest.setSubscribe(subscribeRequest.getSubscribe());
        securedRequest.setNhinTargetCommunities(subscribeRequest.getNhinTargetCommunities());

        result = port.subscribe(securedRequest);

        return result;

    }


    private String getURL()
    {
        String url = "";

        try
        {
            url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.HIEM_SUBSCRIBE_ENTITY_SERVICE_NAME_SECURED);
        }
        catch(Exception ex)
        {
            log.error(ex.getMessage(), ex);
        }

        return url;
    }

    private EntityNotificationProducerSecuredPortType getPort(String url)
    {



        EntityNotificationProducerSecuredPortType port = service.getEntityNotificationProducerSecuredPortSoap11();

        log.info("Setting endpoint address to Entity Notification Secured Service to " + url);
        ((BindingProvider) port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);

        return port;
    }

}
