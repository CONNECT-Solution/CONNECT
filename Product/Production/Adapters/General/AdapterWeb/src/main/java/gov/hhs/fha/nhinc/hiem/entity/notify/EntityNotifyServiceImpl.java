package gov.hhs.fha.nhinc.hiem.entity.notify;

import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.NotifyRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import javax.xml.ws.WebServiceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import java.util.Map;
import javax.xml.ws.BindingProvider;
import gov.hhs.fha.nhinc.entitynotificationconsumersecured.EntityNotificationConsumerSecured;
import gov.hhs.fha.nhinc.entitynotificationconsumersecured.EntityNotificationConsumerSecuredPortType;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;

/**
 *
 * @author dunnek
 */
public class EntityNotifyServiceImpl
{

    private static Log log = LogFactory.getLog(EntityNotifyServiceImpl.class);
    private static EntityNotificationConsumerSecured service = new EntityNotificationConsumerSecured();

    public AcknowledgementType notify(NotifyRequestType notifyRequest, WebServiceContext context)
    {
        log.debug("begin notify");
        AcknowledgementType result = null;

        try
        {
            String url = getURL();
            EntityNotificationConsumerSecuredPortType port = getPort(url);

            AssertionType assertIn = notifyRequest.getAssertion();

            SamlTokenCreator tokenCreator = new SamlTokenCreator();
            Map requestContext = tokenCreator.CreateRequestContext(assertIn, url, NhincConstants.HIEM_NOTIFY_ENTITY_SERVICE_NAME_SECURED);
            ((BindingProvider) port).getRequestContext().putAll(requestContext);

            result = port.notify(notifyRequest.getNotify());
        }
        catch (Exception ex)
        {
            log.error(ex.getMessage(), ex);
        }

        return result;
    }

    private String getURL()
    {
        String url = "";

        try
        {
            url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.HIEM_NOTIFY_ENTITY_SERVICE_NAME_SECURED);
        }
        catch (Exception ex)
        {
            log.error(ex.getMessage(), ex);
        }

        return url;
    }

    private EntityNotificationConsumerSecuredPortType getPort(String url)
    {
        EntityNotificationConsumerSecuredPortType port = service.getEntityNotificationConsumerSecuredPortSoap11();
        gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().initializePort((javax.xml.ws.BindingProvider) port, url);
        return port;
    }
}
