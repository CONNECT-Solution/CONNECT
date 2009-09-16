package gov.hhs.fha.nhinc.hiem.entity.proxy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import java.util.Map;
import javax.xml.ws.BindingProvider;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import gov.hhs.fha.nhinc.nhincproxynotificationconsumer.NhincProxyNotificationConsumerSecured;
import gov.hhs.fha.nhinc.nhincproxynotificationconsumer.NhincProxyNotificationConsumerSecuredPortType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.NotifyRequestSecuredType;

/**
 *
 * @author dunnek
 */
public class ProxyHiemNotifyImpl
{

    private static Log log = LogFactory.getLog(ProxyHiemNotifyImpl.class);
    private static NhincProxyNotificationConsumerSecured service = new NhincProxyNotificationConsumerSecured();

    public void notify(gov.hhs.fha.nhinc.common.nhinccommonproxy.NotifyRequestType request)
    {
        log.debug("Begin Proxy Notify");

        String url = getURL();
        NhincProxyNotificationConsumerSecuredPortType port = getPort(url);

        AssertionType assertIn = request.getAssertion();

        SamlTokenCreator tokenCreator = new SamlTokenCreator();
        Map requestContext = tokenCreator.CreateRequestContext(assertIn, url, NhincConstants.HIEM_NOTIFY_ENTITY_SERVICE_NAME_SECURED);
        ((BindingProvider) port).getRequestContext().putAll(requestContext);

        NotifyRequestSecuredType securedRequest = new NotifyRequestSecuredType();

        securedRequest.setNotify(request.getNotify());
        securedRequest.setNhinTargetSystem(request.getNhinTargetSystem());

        port.notify(securedRequest);

    }

    private String getURL()
    {
        String url = "";

        try
        {
            url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.HIEM_NOTIFY_PROXY_SERVICE_NAME_SECURED);
        }
        catch (Exception ex)
        {
            log.error(ex.getMessage(), ex);
        }

        return url;
    }

    private NhincProxyNotificationConsumerSecuredPortType getPort(String url)
    {
        NhincProxyNotificationConsumerSecuredPortType port = service.getNhincProxyNotificationConsumerPortSoap11();

        log.info("Setting endpoint address to Proxy Unsubscribe Secured Service to " + url);
        ((BindingProvider) port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);

        return port;
    }
}
