/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientdiscovery.entity.deferred.response.proxy;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.entitypatientdiscoverysecuredasyncresp.EntityPatientDiscoverySecuredAsyncResp;
import gov.hhs.fha.nhinc.entitypatientdiscoverysecuredasyncresp.EntityPatientDiscoverySecuredAsyncRespPortType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import org.hl7.v3.MCCIIN000002UV01;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.PRPAIN201306UV02;
import java.util.Map;
import javax.xml.ws.BindingProvider;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02SecuredRequestType;
/**
 *
 * @author dunnek
 */
public class EntityPatientDiscoveryDeferredResponseProxySecuredWebServiceImpl implements EntityPatientDiscoveryDeferredResponseProxy{
    private static final String NAMESPACE_URI = "urn:gov:hhs:fha:nhinc:entitypatientdiscoverysecuredasyncresp";
    private static final String SERVICE_LOCAL_PART = "EntityPatientDiscoverySecuredAsyncResp";
    private static final String PORT_LOCAL_PART = "EntityPatientDiscoverySecuredAsyncRespPortType";
    private static final String WSDL_FILE = "EntityPatientDiscoveryAsyncResp.wsdl";
    private static final String WS_ADDRESSING_ACTION = NAMESPACE_URI + ":ProcessPatientDiscoveryAsyncResp";
    
    private Log log = null;
    private static Service cachedService = null;
    private static EntityPatientDiscoverySecuredAsyncResp service = null;
    
    public EntityPatientDiscoveryDeferredResponseProxySecuredWebServiceImpl()
    {
        log = createLogger();
        service = getWebService();
    }
    protected EntityPatientDiscoverySecuredAsyncResp getWebService()
    {
        return new EntityPatientDiscoverySecuredAsyncResp();
    }
    protected Log createLogger()
    {
        return LogFactory.getLog(getClass());
    }
    protected WebServiceProxyHelper getWebServiceProxyHelper()
    {
        return new WebServiceProxyHelper();
    }
    protected String getUrl(String serviceName)
    {
        String result = "";
        try
        {
            result = this.getWebServiceProxyHelper().getUrlLocalHomeCommunity(serviceName);
        }
        catch(Exception ex)
        {
            log.warn("Unable to retreive url for service: " + serviceName);
            log.warn("Error: " + ex.getMessage(), ex);
        }

        return  result;
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
    protected EntityPatientDiscoverySecuredAsyncRespPortType getPort(String url, AssertionType assertion, String serviceName)
    {
        WebServiceProxyHelper proxyHelper =getWebServiceProxyHelper();

        EntityPatientDiscoverySecuredAsyncRespPortType port = null;
        Service cacheService = getService(WSDL_FILE,NAMESPACE_URI, SERVICE_LOCAL_PART);
        if (cacheService != null)
        {
            log.debug("Obtained service - creating port.");
            port = cacheService.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), EntityPatientDiscoverySecuredAsyncRespPortType.class);
            proxyHelper.initializeSecurePort((javax.xml.ws.BindingProvider) port, url,serviceName, WS_ADDRESSING_ACTION, assertion);
         }
        else
        {
            log.error("Unable to obtain serivce - no port created.");
        }
        return port;
    }
    public MCCIIN000002UV01 processPatientDiscoveryAsyncResp(PRPAIN201306UV02 body, AssertionType assertion, NhinTargetCommunitiesType target) {
     
        WebServiceProxyHelper proxyHelper = getWebServiceProxyHelper();
        String serviceName = NhincConstants.PATIENT_DISCOVERY_ENTITY_SECURED_ASYNC_RESP_SERVICE_NAME;
        String url = this.getUrl(serviceName);
        MCCIIN000002UV01 response = null;


        if (NullChecker.isNotNullish(url))
        {
            EntityPatientDiscoverySecuredAsyncRespPortType port = getPort(url, assertion, serviceName);
            RespondingGatewayPRPAIN201306UV02SecuredRequestType message = new RespondingGatewayPRPAIN201306UV02SecuredRequestType();

            SamlTokenCreator tokenCreator = new SamlTokenCreator();
           Map requestContext = tokenCreator.CreateRequestContext(assertion, url, NhincConstants.PATIENT_DISCOVERY_ACTION);

            ((BindingProvider) port).getRequestContext().putAll(requestContext);

            message.setNhinTargetCommunities(target);
            message.setPRPAIN201306UV02(body);


            try
            {
                log.debug("invoke port");
                response = (MCCIIN000002UV01) proxyHelper.invokePort(port, EntityPatientDiscoverySecuredAsyncRespPortType.class, "processPatientDiscoveryAsyncResp", message);
            }
            catch(Exception ex)
            {
                log.error("Failed to call the web service (" + serviceName + ").  An unexpected exception occurred.  " +
                        "Exception: " + ex.getMessage(), ex);
            }
        }

        return response;
        
    }
}
