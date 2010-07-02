package gov.hhs.fha.nhinc.xdr.request.entity;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import ihe.iti.xdr._2007.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import org.apache.commons.logging.Log;
import gov.hhs.fha.nhinc.nhincentityxdrsecured.async.request.EntityXDRSecuredRequestService;
import gov.hhs.fha.nhinc.nhincentityxdrsecured.async.request.EntityXDRSecuredRequestPortType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import java.util.Map;
import javax.xml.ws.BindingProvider;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Neil Webb
 */
public class EntityXDRRequestImpl
{
    private static Log log = null;
    private static EntityXDRSecuredRequestService service = null;

    public EntityXDRRequestImpl()
    {
        log = createLogger();
        service = createService();
    }

    public AcknowledgementType provideAndRegisterDocumentSetBRequest(RespondingGatewayProvideAndRegisterDocumentSetRequestType provideAndRegisterRequestRequest)
    {
        AcknowledgementType response = null;

        String url = getURL();
        if(NullChecker.isNotNullish(url))
        {
            try
            {
                EntityXDRSecuredRequestPortType port = getPort(url);

                RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType provideAndRegisterRequestRequestSecured = new RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType();
                provideAndRegisterRequestRequestSecured.setProvideAndRegisterDocumentSetRequest(provideAndRegisterRequestRequest.getProvideAndRegisterDocumentSetRequest());
                provideAndRegisterRequestRequestSecured.setNhinTargetSystem(provideAndRegisterRequestRequest.getNhinTargetSystem());

                AssertionType assertion = provideAndRegisterRequestRequest.getAssertion();
                setRequestContext(assertion, url, port);

                // TODO: Audit log

                // TODO: Policy check

                response = port.provideAndRegisterDocumentSetBRequest(provideAndRegisterRequestRequestSecured);
            }
            catch (Exception ex)
            {
                log.error("Error in Unsecured Entity XDR Request: " + ex.getMessage(), ex);
                response = new AcknowledgementType();
                response.setMessage("Error");
            }
        }
        else
        {
            log.error("The URL for service: " + NhincConstants.ENTITY_XDR_REQUEST_SECURED_SERVICE_NAME + " is null");
            response = new AcknowledgementType();
            response.setMessage("Error");
        }

        return response;
    }

    protected void setRequestContext(AssertionType assertion, String url, EntityXDRSecuredRequestPortType port)
    {
        SamlTokenCreator tokenCreator = new SamlTokenCreator();
        Map requestContext = tokenCreator.CreateRequestContext(assertion, url, NhincConstants.ENTITY_XDR_REQUEST_SERVICE_NAME);
        ((BindingProvider) port).getRequestContext().putAll(requestContext);
    }

    protected Log createLogger()
    {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    protected EntityXDRSecuredRequestService createService()
    {
        return ((service != null) ? service : new EntityXDRSecuredRequestService());
    }

    protected String getURL()
    {
        String url = "";

        try
        {
            url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.ENTITY_XDR_REQUEST_SECURED_SERVICE_NAME);
        }
        catch (Exception ex)
        {
            log.error(ex.getMessage(), ex);
        }

        return url;
    }

    protected EntityXDRSecuredRequestPortType getPort(String url)
    {
        EntityXDRSecuredRequestPortType port = service.getEntityXDRSecuredRequestPort();
        gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().initializePort((javax.xml.ws.BindingProvider) port, url);
        return port;
    }
}
