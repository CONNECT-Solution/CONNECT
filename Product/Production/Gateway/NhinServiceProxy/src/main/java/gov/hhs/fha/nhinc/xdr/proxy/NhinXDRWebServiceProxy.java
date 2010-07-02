/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.xdr.proxy;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import java.util.Map;
import javax.xml.ws.BindingProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ihe.iti.xdr._2007.DocumentRepositoryXDRPortType;
import ihe.iti.xdr._2007.DocumentRepositoryXDRService;

/**
 *
 * @author dunnek
 */
public class NhinXDRWebServiceProxy implements NhinXDRProxy{
    private static Log log;
    static DocumentRepositoryXDRService service = null;
    
    public NhinXDRWebServiceProxy()
    {
        log = createLogger();
        service = createService();
    }
    public RegistryResponseType provideAndRegisterDocumentSetB(ProvideAndRegisterDocumentSetRequestType request,
            AssertionType assertion, NhinTargetSystemType targetSystem)
    {
        log.debug("begin provideAndRegisterDocumentSetB()");
        
        String url = null;
        RegistryResponseType response = null;

        // Get the URL to the Nhin Subject Discovery Service
        url = getUrl(targetSystem);
        if (NullChecker.isNotNullish(url)) {
            DocumentRepositoryXDRPortType port = getPort(url);

            SamlTokenCreator tokenCreator = new SamlTokenCreator();
            Map requestContext = tokenCreator.CreateRequestContext(assertion, url, NhincConstants.XDR_ACTION);

            ((BindingProvider) port).getRequestContext().putAll(requestContext);

            response = port.documentRepositoryProvideAndRegisterDocumentSetB(request);

        } else {
            log.error("The URL for service: " + NhincConstants.NHINC_XDR_SERVICE_NAME + " is null");
        }

        return response;
    }

    protected DocumentRepositoryXDRService createService()
    {
        return new DocumentRepositoryXDRService();
    }
    protected Log createLogger()
    {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }
    protected String getUrl(NhinTargetSystemType target) {
        String url = null;

        if (target != null) {
            try {
                url = ConnectionManagerCache.getEndpontURLFromNhinTarget(target, NhincConstants.NHINC_XDR_SERVICE_NAME);
            } catch (ConnectionManagerException ex) {
                log.error("Error: Failed to retrieve url for service: " + NhincConstants.NHINC_XDR_SERVICE_NAME);
                log.error(ex.getMessage());
            }
        } else {
            log.error("Target system passed into the proxy is null");
        }

        return url;
    }
    private DocumentRepositoryXDRPortType getPort(String url) {
        DocumentRepositoryXDRPortType port = service.getDocumentRepositoryXDRPortSoap12();

        gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().initializePort((javax.xml.ws.BindingProvider) port, url);

        return port;
    }
}
