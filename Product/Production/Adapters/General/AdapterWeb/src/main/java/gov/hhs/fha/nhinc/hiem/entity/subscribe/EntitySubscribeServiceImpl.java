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
import java.util.StringTokenizer;

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
		int retryCount = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getRetryAttempts();
		int retryDelay = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getRetryDelay();
        String exceptionText = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getExceptionText();
        javax.xml.ws.WebServiceException catchExp = null;
        if (retryCount > 0 && retryDelay > 0 && exceptionText != null && !exceptionText.equalsIgnoreCase("")) {
            int i = 1;
            while (i <= retryCount) {
                try {
                    result = port.subscribe(securedRequest);
                    break;
                } catch (javax.xml.ws.WebServiceException e) {
                    catchExp = e;
                    int flag = 0;
                    StringTokenizer st = new StringTokenizer(exceptionText, ",");
                    while (st.hasMoreTokens()) {
                        if (e.getMessage().contains(st.nextToken())) {
                            flag = 1;
                        }
                    }
                    if (flag == 1) {
                        log.warn("Exception calling ... web service: " + e.getMessage());
                        System.out.println("retrying the connection for attempt [ " + i + " ] after [ " + retryDelay + " ] seconds");
                        log.info("retrying attempt [ " + i + " ] the connection after [ " + retryDelay + " ] seconds");
                        i++;
                        try {
                            Thread.sleep(retryDelay);
                        } catch (InterruptedException iEx) {
                            log.error("Thread Got Interrupted while waiting on EntityNotificationProducerSecured call :" + iEx);
                        } catch (IllegalArgumentException iaEx) {
                            log.error("Thread Got Interrupted while waiting on EntityNotificationProducerSecured call :" + iaEx);
                        }
                        retryDelay = retryDelay + retryDelay; //This is a requirement from Customer
                    } else {
                        log.error("Unable to call EntityNotificationProducerSecured Webservice due to  : " + e);
                        throw e;
                    }
                }
            }

            if (i > retryCount) {
                log.error("Unable to call EntityNotificationProducerSecured Webservice due to  : " + catchExp);
                throw catchExp;
            }

        } else {
            result = port.subscribe(securedRequest);
        }

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
        EntityNotificationProducerSecuredPortType port = service.getEntityNotificationProducerSecuredPortSoap11();
        gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().initializePort((javax.xml.ws.BindingProvider) port, url);
        return port;
    }
}
