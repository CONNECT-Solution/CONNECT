/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientdiscovery.entity.deferred.response.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.entitypatientdiscoveryasyncresp.EntityPatientDiscoveryAsyncResp;
import gov.hhs.fha.nhinc.entitypatientdiscoveryasyncresp.EntityPatientDiscoveryAsyncRespPortType;
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
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02RequestType;

/**
 *
 * @author dunnek
 */
public class EntityPatientDiscoveryDeferredResponseProxyUnsecuredWebServiceImpl implements EntityPatientDiscoveryDeferredResponseProxy{
    private static final String NAMESPACE_URI = "urn:gov:hhs:fha:nhinc:entitypatientdiscoveryasyncresp";
    private static final String SERVICE_LOCAL_PART = "EntityPatientDiscoveryAsyncResp";
    private static final String PORT_LOCAL_PART = "EntityPatientDiscoveryAsyncRespPortType";
    private static final String WSDL_FILE = "EntityPatientDiscoveryAsyncResp.wsdl";
    private static final String WS_ADDRESSING_ACTION = NAMESPACE_URI + ":ProcessPatientDiscoveryAsyncResp";

    private Log log = null;
    private static Service cachedService = null;
    
    public EntityPatientDiscoveryDeferredResponseProxyUnsecuredWebServiceImpl()
    {
        log = createLogger();
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
    protected EntityPatientDiscoveryAsyncRespPortType getPort(String url, AssertionType assertion, String serviceName)
    {
        WebServiceProxyHelper proxyHelper =getWebServiceProxyHelper();

        EntityPatientDiscoveryAsyncRespPortType port = null;
        Service cacheService = getService(WSDL_FILE,NAMESPACE_URI, SERVICE_LOCAL_PART);
        if (cacheService != null)
        {
            log.debug("Obtained service - creating port.");
            port = cacheService.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), EntityPatientDiscoveryAsyncRespPortType.class);            
            proxyHelper.initializeUnsecurePort((javax.xml.ws.BindingProvider) port, url, serviceName, assertion);
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
            EntityPatientDiscoveryAsyncRespPortType port = getPort(url, assertion, serviceName);

            RespondingGatewayPRPAIN201306UV02RequestType message = new RespondingGatewayPRPAIN201306UV02RequestType();

            message.setNhinTargetCommunities(target);
            message.setPRPAIN201306UV02(body);


            try
            {
                log.debug("invoke port");
                response = (MCCIIN000002UV01) proxyHelper.invokePort(port, EntityPatientDiscoveryAsyncRespPortType.class, "processPatientDiscoveryAsyncResp", message);
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
