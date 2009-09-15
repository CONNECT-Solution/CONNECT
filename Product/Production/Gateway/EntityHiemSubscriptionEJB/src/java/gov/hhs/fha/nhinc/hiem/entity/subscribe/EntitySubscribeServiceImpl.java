package gov.hhs.fha.nhinc.hiem.entity.subscribe;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
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
import gov.hhs.fha.nhinc.hiem.processor.entity.EntitySubscribeProcessor;
import org.oasis_open.docs.wsn.b_2.Subscribe;
import org.oasis_open.docs.wsn.b_2.SubscribeResponse;
import javax.xml.ws.WebServiceContext;
import gov.hhs.fha.nhinc.hiem.dte.SoapUtil;
//import org.oasis_open.docs.wsn.bw_2.InvalidTopicExpressionFault;
//import org.oasis_open.docs.wsn.bw_2.SubscribeCreationFailedFault;
//import org.oasis_open.docs.wsn.bw_2.TopicNotSupportedFault;
import org.w3c.dom.Element;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
/**
 *
 *
 * @author Neil Webb
 */
public class EntitySubscribeServiceImpl
{
    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(EntitySubscribeServiceImpl.class);

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

    //Deprecated
    public org.oasis_open.docs.wsn.b_2.SubscribeResponse subscribe(gov.hhs.fha.nhinc.common.nhinccommonentity.SubscribeRequestType subscribeRequest, WebServiceContext context) throws InvalidFilterFault, InvalidMessageContentExpressionFault, InvalidProducerPropertiesExpressionFault, InvalidTopicExpressionFault, NotifyMessageNotSupportedFault, ResourceUnknownFault, SubscribeCreationFailedFault, TopicExpressionDialectUnknownFault, TopicNotSupportedFault, UnacceptableInitialTerminationTimeFault, UnrecognizedPolicyRequestFault, UnsupportedPolicyRequestFault
    {
        log.debug("In subscribe");
        SubscribeResponse response = null;
        Subscribe subscribe = subscribeRequest.getSubscribe();
        AssertionType assertion = subscribeRequest.getAssertion();
        NhinTargetCommunitiesType targetCommunitites = subscribeRequest.getNhinTargetCommunities();
        Element subscribeElement = new SoapUtil().extractFirstElement(context, "subscribeSoapMessage", "Subscribe");

        EntitySubscribeProcessor processor = new EntitySubscribeProcessor();
        try
        {
            response = processor.processSubscribe(subscribe, subscribeElement, assertion, targetCommunitites);
        }
        catch (org.oasis_open.docs.wsn.bw_2.TopicNotSupportedFault ex)
        {
            throw new TopicNotSupportedFault(ex.getMessage(), ex.getFaultInfo(), ex.getCause());
        }
        catch (org.oasis_open.docs.wsn.bw_2.InvalidTopicExpressionFault ex)
        {
            throw new InvalidTopicExpressionFault(ex.getMessage(), ex.getFaultInfo(), ex.getCause());
        }
        catch (org.oasis_open.docs.wsn.bw_2.SubscribeCreationFailedFault ex)
        {
            throw new SubscribeCreationFailedFault(ex.getMessage(), ex.getFaultInfo(), ex.getCause());
        }
        catch (org.oasis_open.docs.wsn.bw_2.ResourceUnknownFault ex)
        {
            throw new ResourceUnknownFault(ex.getMessage(), ex.getFaultInfo(), ex.getCause());
        }

        return response;
    }
    public org.oasis_open.docs.wsn.b_2.SubscribeResponse subscribe(gov.hhs.fha.nhinc.common.nhinccommonentity.SubscribeRequestSecuredType subscribeRequest, WebServiceContext context) throws InvalidFilterFault, InvalidMessageContentExpressionFault, InvalidProducerPropertiesExpressionFault, InvalidTopicExpressionFault, NotifyMessageNotSupportedFault, ResourceUnknownFault, SubscribeCreationFailedFault, TopicExpressionDialectUnknownFault, TopicNotSupportedFault, UnacceptableInitialTerminationTimeFault, UnrecognizedPolicyRequestFault, UnsupportedPolicyRequestFault
    {
        log.debug("In subscribe");
        AssertionType assertion = SamlTokenExtractor.GetAssertion(context);
        
        SubscribeResponse response = null;
        Subscribe subscribe = subscribeRequest.getSubscribe();


        NhinTargetCommunitiesType targetCommunitites = subscribeRequest.getNhinTargetCommunities();
        Element subscribeElement = new SoapUtil().extractFirstElement(context, "subscribeSoapMessage", "Subscribe");

        EntitySubscribeProcessor processor = new EntitySubscribeProcessor();
        try
        {
            response = processor.processSubscribe(subscribe, subscribeElement, assertion, targetCommunitites);
        }
        catch (org.oasis_open.docs.wsn.bw_2.TopicNotSupportedFault ex)
        {
            throw new TopicNotSupportedFault(ex.getMessage(), ex.getFaultInfo(), ex.getCause());
        }
        catch (org.oasis_open.docs.wsn.bw_2.InvalidTopicExpressionFault ex)
        {
            throw new InvalidTopicExpressionFault(ex.getMessage(), ex.getFaultInfo(), ex.getCause());
        }
        catch (org.oasis_open.docs.wsn.bw_2.SubscribeCreationFailedFault ex)
        {
            throw new SubscribeCreationFailedFault(ex.getMessage(), ex.getFaultInfo(), ex.getCause());
        }
        catch (org.oasis_open.docs.wsn.bw_2.ResourceUnknownFault ex)
        {
            throw new ResourceUnknownFault(ex.getMessage(), ex.getFaultInfo(), ex.getCause());
        }

        return response;
    }
    
}
