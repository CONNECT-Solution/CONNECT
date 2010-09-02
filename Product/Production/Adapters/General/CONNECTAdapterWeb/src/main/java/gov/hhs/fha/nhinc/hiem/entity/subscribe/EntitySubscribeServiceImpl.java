/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.hiem.entity.subscribe;

import gov.hhs.fha.nhinc.entitysubscriptionmanagementsecured.EntityNotificationProducerSecured;
import gov.hhs.fha.nhinc.entitysubscriptionmanagementsecured.EntityNotificationProducerSecuredPortType;
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
import org.oasis_open.docs.wsn.b_2.SubscribeResponse;
import javax.xml.ws.WebServiceContext;
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

    public SubscribeResponse subscribe(gov.hhs.fha.nhinc.common.nhinccommonentity.SubscribeRequestType subscribeRequest, WebServiceContext context) throws InvalidFilterFault, InvalidMessageContentExpressionFault, InvalidProducerPropertiesExpressionFault, InvalidTopicExpressionFault, NotifyMessageNotSupportedFault, ResourceUnknownFault, SubscribeCreationFailedFault, TopicExpressionDialectUnknownFault, TopicNotSupportedFault, UnacceptableInitialTerminationTimeFault, UnrecognizedPolicyRequestFault, UnsupportedPolicyRequestFault
    {
        log.debug("begin subscribe");
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
        try
        {
            result = port.subscribe(securedRequest);
        }
        catch (gov.hhs.fha.nhinc.entitysubscriptionmanagementsecured.InvalidFilterFault ex)
        {
            throw new InvalidFilterFault(ex.getMessage(), ex.getFaultInfo(), ex);
        }
        catch (gov.hhs.fha.nhinc.entitysubscriptionmanagementsecured.InvalidMessageContentExpressionFault ex)
        {
            throw new InvalidMessageContentExpressionFault(ex.getMessage(), ex.getFaultInfo(), ex);
        }
        catch (gov.hhs.fha.nhinc.entitysubscriptionmanagementsecured.InvalidProducerPropertiesExpressionFault ex)
        {
            throw new InvalidProducerPropertiesExpressionFault(ex.getMessage(), ex.getFaultInfo(), ex);
        }
        catch (gov.hhs.fha.nhinc.entitysubscriptionmanagementsecured.InvalidTopicExpressionFault ex)
        {
            throw new InvalidTopicExpressionFault(ex.getMessage(), ex.getFaultInfo(), ex);
        }
        catch (gov.hhs.fha.nhinc.entitysubscriptionmanagementsecured.NotifyMessageNotSupportedFault ex)
        {
            throw new NotifyMessageNotSupportedFault(ex.getMessage(), ex.getFaultInfo(), ex);
        }
        catch (gov.hhs.fha.nhinc.entitysubscriptionmanagementsecured.ResourceUnknownFault ex)
        {
            throw new ResourceUnknownFault(ex.getMessage(), ex.getFaultInfo(), ex);
        }
        catch (gov.hhs.fha.nhinc.entitysubscriptionmanagementsecured.SubscribeCreationFailedFault ex)
        {
            throw new SubscribeCreationFailedFault(ex.getMessage(), ex.getFaultInfo(), ex);
        }
        catch (gov.hhs.fha.nhinc.entitysubscriptionmanagementsecured.TopicExpressionDialectUnknownFault ex)
        {
            throw new TopicExpressionDialectUnknownFault(ex.getMessage(), ex.getFaultInfo(), ex);
        }
        catch (gov.hhs.fha.nhinc.entitysubscriptionmanagementsecured.TopicNotSupportedFault ex)
        {
            throw new TopicNotSupportedFault(ex.getMessage(), ex.getFaultInfo(), ex);
        }
        catch (gov.hhs.fha.nhinc.entitysubscriptionmanagementsecured.UnacceptableInitialTerminationTimeFault ex)
        {
            throw new UnacceptableInitialTerminationTimeFault(ex.getMessage(), ex.getFaultInfo(), ex);
        }
        catch (gov.hhs.fha.nhinc.entitysubscriptionmanagementsecured.UnrecognizedPolicyRequestFault ex)
        {
            throw new UnrecognizedPolicyRequestFault(ex.getMessage(), ex.getFaultInfo(), ex);
        }
        catch (gov.hhs.fha.nhinc.entitysubscriptionmanagementsecured.UnsupportedPolicyRequestFault ex)
        {
            throw new UnsupportedPolicyRequestFault(ex.getMessage(), ex.getFaultInfo(), ex);
        }

        return result;
    }

    private String getURL()
    {
        String url = "";

        try
        {
            url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.HIEM_SUBSCRIBE_ENTITY_SERVICE_NAME_SECURED);
        }
        catch (Exception ex)
        {
            log.error(ex.getMessage(), ex);
        }

        return url;
    }

    private EntityNotificationProducerSecuredPortType getPort(String url)
    {
        EntityNotificationProducerSecuredPortType port = service.getEntityNotificationProducerSecuredPortSoap();

        log.info("Setting endpoint address to Entity Notification Secured Service to " + url);
        ((BindingProvider) port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);

        return port;
    }
}
