/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.admindistribution.entity.proxy;
import gov.hhs.fha.nhinc.admindistribution.AdminDistributionHelper;
import oasis.names.tc.emergency.edxl.de._1.EDXLDistribution;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import oasis.names.tc.emergency.edxl.de._1.EDXLDistribution;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewaySendAlertMessageType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import gov.hhs.fha.nhinc.entityadmindistribution.AdministrativeDistributionPortType;
import gov.hhs.fha.nhinc.entityadmindistribution.AdministrativeDistributionService;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
/**
 *
 * @author dunnek
 */
public class EntityAdminDistUnsecuredWebServiceImpl {
    private static final String NAMESPACE_URI = "urn:gov:hhs:fha:nhinc:entityadmindistribution";
    private static final String SERVICE_LOCAL_PART = "AdministrativeDistribution_Service";
    private static final String PORT_LOCAL_PART = "AdministrativeDistribution_PortType";
    private static final String WSDL_FILE = "EntityAdminDist.wsdl";
    
    private Log log = null;
    static AdministrativeDistributionService service = null;
    private static Service cachedService = null;
    private WebServiceProxyHelper proxyHelper = null;
    
    public EntityAdminDistUnsecuredWebServiceImpl()
    {
        log = createLogger();
        service = getWebService();
        proxyHelper =  getWebServiceProxyHelper();
    }
    protected WebServiceProxyHelper getWebServiceProxyHelper()
    {
        return new WebServiceProxyHelper();
    }
    protected AdministrativeDistributionService getWebService()
    {
        return new AdministrativeDistributionService();
    }
    protected Log createLogger()
    {
        return LogFactory.getLog(getClass());
    }

    /**
     * This method retrieves and initializes the port.
     *
     * @param url The URL for the web service.
     * @param serviceAction The action for the web service.
     * @param assertion The assertion information for the web service
     * @return The port object for the web service.
     */
    protected AdministrativeDistributionPortType getPort(String url)
    {
        AdministrativeDistributionPortType port = null;
        Service cacheService = getService(WSDL_FILE,NAMESPACE_URI, SERVICE_LOCAL_PART);
        if (cacheService != null)
        {
            log.debug("Obtained service - creating port.");
            port = cacheService.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), AdministrativeDistributionPortType.class);
            proxyHelper.initializePort((javax.xml.ws.BindingProvider) port, url);
        }
        else
        {
            log.error("Unable to obtain serivce - no port created.");
        }
        return port;
    }
    /**
     * Retrieve the service class for this web service.
     *
     * @return The service class for this web service.
     */
    protected Service getService(String wsdl, String uri, String service)
    {
        if (cachedService == null)
        {
            try
            {
                cachedService = proxyHelper.createService(wsdl, uri, service);
            }
            catch (Throwable t)
            {
                log.error("Error creating service: " + t.getMessage(), t);
            }
        }
        return cachedService;
    }
    protected AdminDistributionHelper getHelper()
    {
        return new AdminDistributionHelper();
    }
  
    public void sendAlertMessage(EDXLDistribution body, AssertionType assertion, NhinTargetCommunitiesType target)
    {
        log.debug("begin sendAlert()");

        AdminDistributionHelper helper = getHelper();
        String hcid = helper.getLocalCommunityId();
        String url = helper.getUrl(hcid, NhincConstants.ENTITY_ADMIN_DIST_SERVICE_NAME);
        
        if (NullChecker.isNotNullish(url))
        {
            AdministrativeDistributionPortType port = getPort(url);


            RespondingGatewaySendAlertMessageType message = new RespondingGatewaySendAlertMessageType();
            message.setEDXLDistribution(body);
            message.setNhinTargetCommunities(target);
            message.setAssertion(assertion);

            try
            {
                proxyHelper.invokePort(port, RespondingGatewaySendAlertMessageType.class, "sendAlertMessage", message);
            }
            catch(Exception ex)
            {
                log.error("Unable to send message: " + ex.getMessage());
            }
        }
    }

}
