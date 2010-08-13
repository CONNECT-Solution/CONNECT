/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.admindistribution.entity.proxy;
import gov.hhs.fha.nhinc.admindistribution.AdminDistributionHelper;
import oasis.names.tc.emergency.edxl.de._1.EDXLDistribution;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewaySendAlertMessageSecuredType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import gov.hhs.fha.nhinc.entityadmindistribution.AdministrativeDistributionSecuredPortType;
import gov.hhs.fha.nhinc.entityadmindistribution.AdministrativeDistributionSecuredService;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import java.util.Map;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import javax.xml.namespace.QName;

/**
 *
 * @author dunnek
 */
public class EntityAdminDistSecuredWebServiceImpl {
    private static final String NAMESPACE_URI = "urn:gov:hhs:fha:nhinc:entityadmindistribution";
    private static final String SERVICE_LOCAL_PART = "AdministrativeDistributionSecured_Service";
    private static final String PORT_LOCAL_PART = "AdministrativeDistributionSecured_PortType";
    private static final String WSDL_FILE = "EntityAdminDistSecured.wsdl";
    
    private Log log = null;
    static AdministrativeDistributionSecuredService service = null;
    public EntityAdminDistSecuredWebServiceImpl()
    {
        log = createLogger();
        service = getWebService();
    }
    protected AdministrativeDistributionSecuredService getWebService()
    {
        return new AdministrativeDistributionSecuredService();
    }
    protected Log createLogger()
    {
        return LogFactory.getLog(getClass());
    }
    protected AdministrativeDistributionSecuredPortType getPort(String url) {
        AdministrativeDistributionSecuredPortType port = service.getAdministrativeDistributionSecuredPortType();

        log.info("Setting endpoint address to Entity Administrative DIstribution Service to " + url);
        ((BindingProvider) port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);

        return port;
    }

    protected AdminDistributionHelper getHelper()
    {
        return new AdminDistributionHelper();
    }
    public void sendAlertMessage(EDXLDistribution body, AssertionType assertion, NhinTargetCommunitiesType target)
    {
        log.debug("begin sendAlert()");
       
        
        AdminDistributionHelper helper = new AdminDistributionHelper();
        String hcid = helper.getLocalCommunityId();
        String url = helper.getUrl(hcid, NhincConstants.ENTITY_ADMIN_DIST_SECURED_SERVICE_NAME);

        if (NullChecker.isNotNullish(url))
        {
            AdministrativeDistributionSecuredPortType port = getPort(url);

            SamlTokenCreator tokenCreator = new SamlTokenCreator();
            Map requestContext = tokenCreator.CreateRequestContext(assertion, url, NhincConstants.ADMIN_DIST_ACTION);

            WebServiceProxyHelper webServiceHelper = new WebServiceProxyHelper();
            webServiceHelper.initializePort((javax.xml.ws.BindingProvider) port, url);
            
            ((BindingProvider) port).getRequestContext().putAll(requestContext);

            RespondingGatewaySendAlertMessageSecuredType message = new RespondingGatewaySendAlertMessageSecuredType();
            message.setEDXLDistribution(body);
            message.setNhinTargetCommunities(target);

            try
            {
                log.debug("invoke port");
                getWebServiceProxyHelper().invokePort(port, AdministrativeDistributionSecuredPortType.class, "sendAlertMessage", body);
            }
            catch(Exception ex)
            {
                log.error("Failed to call the web service (" + NhincConstants.ENTITY_ADMIN_DIST_SECURED_SERVICE_NAME + ").  An unexpected exception occurred.  " +
                        "Exception: " + ex.getMessage(), ex);
            }
        }

    }
    protected WebServiceProxyHelper getWebServiceProxyHelper()
    {
        return new WebServiceProxyHelper();
    }

}
