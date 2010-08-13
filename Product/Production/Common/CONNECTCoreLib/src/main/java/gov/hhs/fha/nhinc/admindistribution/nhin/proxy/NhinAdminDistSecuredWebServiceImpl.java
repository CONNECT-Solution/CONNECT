/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.admindistribution.nhin.proxy;
import gov.hhs.fha.nhinc.admindistribution.AdminDistributionHelper;
import oasis.names.tc.emergency.edxl.de._1.EDXLDistribution;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import gov.hhs.fha.nhinc.nhinadmindistribution.RespondingGatewayAdministrativeDistribution;
import gov.hhs.fha.nhinc.nhinadmindistribution.RespondingGatewayAdministrativeDistributionPortType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import java.util.Map;
import javax.xml.ws.BindingProvider;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

/**
 *
 * @author dunnek
 */
public class NhinAdminDistSecuredWebServiceImpl implements NhinAdminDistProxy{
    private Log log = null;
    private static Service cachedService = null;
    private static final String NAMESPACE_URI = "urn:gov:hhs:fha:nhinc:nhinadmindistribution";
    private static final String SERVICE_LOCAL_PART = "RespondingGateway_AdministrativeDistribution";
    private static final String PORT_LOCAL_PART = "RespondingGateway_AdministrativeDistribution_PortType";
    private static final String WSDL_FILE = "NhinAdminDist.wsdl";
    private static final String WS_ADDRESSING_ACTION = NAMESPACE_URI + ":SendAlertMessage_Message";

    static RespondingGatewayAdministrativeDistribution nhinService = new RespondingGatewayAdministrativeDistribution();
    public NhinAdminDistSecuredWebServiceImpl()
    {
        log = createLogger();
    }
    protected Log createLogger()
    {
        return LogFactory.getLog(getClass());
    }
    protected AdminDistributionHelper getHelper()
    {
        return new AdminDistributionHelper();
    }
    public void sendAlertMessage(EDXLDistribution body, AssertionType assertion, NhinTargetSystemType target)
    {
        String url = null;
        AdminDistributionHelper helper = getHelper();
        url = helper.getUrl(target, NhincConstants.NHIN_ADMIN_DIST_SERVICE_NAME);
        
        if (NullChecker.isNotNullish(url))
        {
            RespondingGatewayAdministrativeDistributionPortType port = getPort(url, assertion);

            SamlTokenCreator tokenCreator = new SamlTokenCreator();
            Map requestContext = tokenCreator.CreateRequestContext(assertion, url, NhincConstants.ADMIN_DIST_ACTION);
            
            ((BindingProvider) port).getRequestContext().putAll(requestContext);
            

            port.sendAlertMessage(body);
        }


    }
    private RespondingGatewayAdministrativeDistributionPortType getPort(String url) {
        RespondingGatewayAdministrativeDistributionPortType port = nhinService.getRespondingGatewayAdministrativeDistributionPortType();

        log.info("Setting endpoint address to Nhin Administrative DIstribution Service to " + url);
        ((BindingProvider) port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);

        return port;
    }
    protected RespondingGatewayAdministrativeDistributionPortType getPort(String url, AssertionType assertion)
    {
        WebServiceProxyHelper proxyHelper = new WebServiceProxyHelper();
        
        RespondingGatewayAdministrativeDistributionPortType port = null;
        Service cacheService = getService(WSDL_FILE,NAMESPACE_URI, SERVICE_LOCAL_PART);
        if (cacheService != null)
        {
            log.debug("Obtained service - creating port.");
            port = cacheService.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), RespondingGatewayAdministrativeDistributionPortType.class);
            proxyHelper.initializeSecurePort((javax.xml.ws.BindingProvider) port, url,NhincConstants.NHIN_ADMIN_DIST_SERVICE_NAME, WS_ADDRESSING_ACTION, assertion);            
         }
        else
        {
            log.error("Unable to obtain serivce - no port created.");
        }
        return port;
    }
    protected Service getService(String wsdl, String uri, String service)
    {
        if (cachedService == null)
        {
            try
            {
                WebServiceProxyHelper proxyHelper = new WebServiceProxyHelper();
                cachedService = proxyHelper.createService(wsdl, uri, service);
            }
            catch (Throwable t)
            {
                log.error("Error creating service: " + t.getMessage(), t);
            }
        }
        return cachedService;
    }
}
