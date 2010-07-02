package gov.hhs.fha.nhinc.xdr.async.response.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import ihe.iti.xdr._2007.AcknowledgementType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import ihe.iti.xdr.async.response._2007.XDRResponseService;
import ihe.iti.xdr.async.response._2007.XDRResponsePortType;
import java.util.Map;
import javax.xml.ws.BindingProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Neil Webb
 */
public class NhinXDRResponseWebServiceProxy implements NhinXDRResponseProxy
{
    private static Log log;
    private static XDRResponseService service;

    public NhinXDRResponseWebServiceProxy()
    {
        log = createLogger();
        service = createService();
    }

    public AcknowledgementType provideAndRegisterDocumentSetBResponse(RegistryResponseType request, AssertionType assertion, NhinTargetSystemType targetSystem)
    {
        AcknowledgementType response = null;
        String url = getUrl(targetSystem);

        if (NullChecker.isNotNullish(url))
        {
            try
            {
                XDRResponsePortType port = getPort(url);

                setRequestContext(assertion, url, port);

                response = port.provideAndRegisterDocumentSetBResponse(request);
            }
            catch(Throwable t)
            {
                log.error("Error in NHIN client for XDR Response: " + t.getMessage(), t);
                response = new AcknowledgementType();
                response.setMessage("Error");
            }
        }
        else
        {
            log.error("The URL for service: " + NhincConstants.NHINC_XDR_RESPONSE_SERVICE_NAME + " is null");
            response = new AcknowledgementType();
            response.setMessage("Error");
        }

        return response;
    }

    protected void setRequestContext(AssertionType assertion, String url, XDRResponsePortType port)
    {
        SamlTokenCreator tokenCreator = new SamlTokenCreator();
        Map requestContext = tokenCreator.CreateRequestContext(assertion, url, NhincConstants.XDR_RESPONSE_ACTION);

        ((BindingProvider) port).getRequestContext().putAll(requestContext);
    }

    protected XDRResponseService createService()
    {
        return ((service != null) ? service : new XDRResponseService());
    }

    protected Log createLogger()
    {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    protected String getUrl(NhinTargetSystemType target)
    {
        String url = null;

        if (target != null)
        {
            try
            {
                url = ConnectionManagerCache.getEndpontURLFromNhinTarget(target, NhincConstants.NHINC_XDR_RESPONSE_SERVICE_NAME);
            }
            catch (ConnectionManagerException ex)
            {
                log.error("Error: Failed to retrieve url for service: " + NhincConstants.NHINC_XDR_RESPONSE_SERVICE_NAME);
                log.error(ex.getMessage());
            }
        }
        else
        {
            log.error("Target system passed into the proxy is null");
        }

        return url;
    }

    protected XDRResponsePortType getPort(String url)
    {
        XDRResponsePortType port = service.getXDRResponsePortSoap12();
        gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().initializePort((javax.xml.ws.BindingProvider) port, url);
        return port;
    }
}
