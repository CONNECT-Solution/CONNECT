/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.xdr.entity;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.nhincentityxdrsecured.EntityXDRSecuredPortType;
import gov.hhs.fha.nhinc.nhincentityxdrsecured.EntityXDRSecuredService;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import java.util.Map;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceContext;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 *
 * @author dunnek
 */
public class EntityXDRImpl {

    private static Log log = null;
    private static EntityXDRSecuredService service = new EntityXDRSecuredService();

    public EntityXDRImpl()
    {
        log = createLogger();
    }
    protected Log createLogger()
    {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    RegistryResponseType provideAndRegisterDocumentSetB(RespondingGatewayProvideAndRegisterDocumentSetRequestType request) {
        RegistryResponseType response = null;
        log.debug("Begin EntityXDRImpl.provideAndRegisterDocumentSetB");

        try {
            String url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.ENTITY_XDR_SECURED_SERVICE_NAME);

            EntityXDRSecuredPortType port = getPort(url);

            AssertionType assertIn = request.getAssertion();
            SamlTokenCreator tokenCreator = new SamlTokenCreator();
            Map requestContext = tokenCreator.CreateRequestContext(assertIn, url, "");
            ((BindingProvider) port).getRequestContext().putAll(requestContext);

            // Send message
           log.debug("Calling secure entity xdr: " + url);
           RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType securedRequest =  new RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType();
           securedRequest.setProvideAndRegisterDocumentSetRequest(request.getProvideAndRegisterDocumentSetRequest());
           securedRequest.setNhinTargetSystem(request.getNhinTargetSystem());
           response = port.provideAndRegisterDocumentSetB(securedRequest);

        } catch (Exception ex) {
            // TODO handle custom exceptions here
        }

        log.debug("End EntityXDRImpl.provideAndRegisterDocumentSetB");
        return response;
    }

    private EntityXDRSecuredPortType getPort(String url) {
        EntityXDRSecuredPortType port = service.getEntityXDRSecuredPort();
        gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().initializePort((javax.xml.ws.BindingProvider) port, url);
        return port;
    }
}
