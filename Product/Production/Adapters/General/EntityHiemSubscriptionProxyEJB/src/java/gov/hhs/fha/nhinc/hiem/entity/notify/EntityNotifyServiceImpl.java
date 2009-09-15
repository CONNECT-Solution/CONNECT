/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.hiem.entity.notify;
//import gov.hhs.fha.nhinc.hiem.processor.entity.EntityNotifyProcessor;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.NotifyRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.NotifyRequestSecuredType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import javax.xml.ws.WebServiceContext;
//import gov.hhs.fha.nhinc.hiem.dte.SoapUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import java.util.Map;
import javax.xml.ws.BindingProvider;
import gov.hhs.fha.nhinc.entitynotificationconsumer.EntityNotificationConsumerSecured;
import gov.hhs.fha.nhinc.entitynotificationconsumer.EntityNotificationConsumerSecuredPortType;
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

            NotifyRequestSecuredType notifyRequestSecured = new NotifyRequestSecuredType();

            notifyRequestSecured.setNotify(notifyRequest.getNotify());
            
            result = port.notify(notifyRequestSecured);
        }
        catch(Exception ex)
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
        catch(Exception ex)
        {
            log.error(ex.getMessage(), ex);
        }

        return url;
    }

    private EntityNotificationConsumerSecuredPortType getPort(String url)
    {



        EntityNotificationConsumerSecuredPortType port = service.getEntityNotificationConsumerSecuredPortSoap11();

        log.info("Setting endpoint address to Entity Notification Secured Service to " + url);
        ((BindingProvider) port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);

        return port;
    }
}
