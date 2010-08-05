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

/**
 *
 * @author dunnek
 */
public class NhinAdminDistSecuredWebServiceImpl implements NhinAdminDistProxy{
   private Log log = null;
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
            RespondingGatewayAdministrativeDistributionPortType port = getPort(url);

            SamlTokenCreator tokenCreator = new SamlTokenCreator();
            Map requestContext = tokenCreator.CreateRequestContext(assertion, url, NhincConstants.ADMIN_DIST_ACTION);

            WebServiceProxyHelper oHelper = new WebServiceProxyHelper();
            oHelper.initializePort((javax.xml.ws.BindingProvider) port, url);
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

}
