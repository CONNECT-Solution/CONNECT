package gov.hhs.fha.nhinc.xdr.response.entity;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import ihe.iti.xdr._2007.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetResponseRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import gov.hhs.fha.nhinc.nhincentityxdrsecured.async.response.EntityXDRSecuredResponseService;
import gov.hhs.fha.nhinc.nhincentityxdrsecured.async.response.EntityXDRSecuredResponsePortType;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import java.util.Map;
import javax.xml.ws.BindingProvider;

/**
 *
 * @author Neil Webb
 */
public class EntityXDRResponseImpl
{
    private static Log log = null;
    private static EntityXDRSecuredResponseService service = null;

    public EntityXDRResponseImpl()
    {
        log = createLogger();
        service = createService();
    }

    public AcknowledgementType provideAndRegisterDocumentSetBResponse(RespondingGatewayProvideAndRegisterDocumentSetResponseRequestType provideAndRegisterDocumentSetResponseRequest)
    {
        AcknowledgementType response = null;

        String url = getURL();
        if(NullChecker.isNotNullish(url))
        {
            try
            {
                EntityXDRSecuredResponsePortType port = getPort(url);

                RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType provideAndRegisterResponseRequestSecured = new RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType();
                provideAndRegisterResponseRequestSecured.setRegistryResponse(provideAndRegisterDocumentSetResponseRequest.getRegistryResponse());
                provideAndRegisterResponseRequestSecured.setNhinTargetSystem(provideAndRegisterDocumentSetResponseRequest.getNhinTargetSystem());

                AssertionType assertion = provideAndRegisterDocumentSetResponseRequest.getAssertion();
                setRequestContext(assertion, url, port);

                // TODO: Audit log

                // TODO: Policy check

                response = port.provideAndRegisterDocumentSetBResponse(provideAndRegisterResponseRequestSecured);
            }
            catch (Exception ex)
            {
                log.error("Error in Unsecured Entity XDR Respponse: " + ex.getMessage(), ex);
                response = new AcknowledgementType();
                response.setMessage("Error");
            }
        }
        else
        {
            log.error("The URL for service: " + NhincConstants.ENTITY_XDR_RESPONSE_SECURED_SERVICE_NAME + " is null");
            response = new AcknowledgementType();
            response.setMessage("Error");
        }

        return response;
    }

    protected void setRequestContext(AssertionType assertion, String url, EntityXDRSecuredResponsePortType port)
    {
        SamlTokenCreator tokenCreator = new SamlTokenCreator();
        Map requestContext = tokenCreator.CreateRequestContext(assertion, url, NhincConstants.ENTITY_XDR_RESPONSE_SERVICE_NAME);
        ((BindingProvider) port).getRequestContext().putAll(requestContext);
    }

    protected Log createLogger()
    {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    protected EntityXDRSecuredResponseService createService()
    {
        return ((service != null) ? service : new EntityXDRSecuredResponseService());
    }

    protected String getURL()
    {
        String url = "";

        try
        {
            url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.ENTITY_XDR_RESPONSE_SECURED_SERVICE_NAME);
        }
        catch (Exception ex)
        {
            log.error(ex.getMessage(), ex);
        }

        return url;
    }

    protected EntityXDRSecuredResponsePortType getPort(String url)
    {
        EntityXDRSecuredResponsePortType port = service.getEntityXDRSecuredResponsePort();
        gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().initializePort((javax.xml.ws.BindingProvider) port, url);
        return port;
    }

}
