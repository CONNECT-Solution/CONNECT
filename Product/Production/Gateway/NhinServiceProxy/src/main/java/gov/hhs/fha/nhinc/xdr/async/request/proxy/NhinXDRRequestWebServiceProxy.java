package gov.hhs.fha.nhinc.xdr.async.request.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import ihe.iti.xdr._2007.AcknowledgementType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ihe.iti.xdr.async.request._2007.XDRRequestService;
import ihe.iti.xdr.async.request._2007.XDRRequestPortType;
import java.util.Map;
import javax.xml.ws.BindingProvider;

/**
 *
 * @author Neil Webb
 */
public class NhinXDRRequestWebServiceProxy implements NhinXDRRequestProxy
{
    private static Log log;
    private static XDRRequestService service;

    public NhinXDRRequestWebServiceProxy()
    {
        log = createLogger();
        service = createService();
    }

    public AcknowledgementType provideAndRegisterDocumentSetBRequest(ProvideAndRegisterDocumentSetRequestType request, AssertionType assertion, NhinTargetSystemType targetSystem)
    {
        AcknowledgementType response = null;
        String url = getUrl(targetSystem);

        if (NullChecker.isNotNullish(url))
        {
            try
            {
                XDRRequestPortType port = getPort(url);

                setRequestContext(assertion, url, port);

                response = port.provideAndRegisterDocumentSetBRequest(request);
            }
            catch(Throwable t)
            {
                log.error("Error in NHIN client for XDR Request: " + t.getMessage(), t);
                response = new AcknowledgementType();
                response.setMessage("Error");
            }
        }
        else
        {
            log.error("The URL for service: " + NhincConstants.NHINC_XDR_REQUEST_SERVICE_NAME + " is null");
            response = new AcknowledgementType();
            response.setMessage("Error");
        }

        return response;
    }

    protected void setRequestContext(AssertionType assertion, String url, XDRRequestPortType port)
    {
        SamlTokenCreator tokenCreator = new SamlTokenCreator();
        Map requestContext = tokenCreator.CreateRequestContext(assertion, url, NhincConstants.XDR_REQUEST_ACTION);

        ((BindingProvider) port).getRequestContext().putAll(requestContext);
    }

    protected XDRRequestService createService()
    {
        return ((service != null) ? service : new XDRRequestService());
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
                url = ConnectionManagerCache.getEndpontURLFromNhinTarget(target, NhincConstants.NHINC_XDR_REQUEST_SERVICE_NAME);
            }
            catch (ConnectionManagerException ex)
            {
                log.error("Error: Failed to retrieve url for service: " + NhincConstants.NHINC_XDR_REQUEST_SERVICE_NAME);
                log.error(ex.getMessage());
            }
        }
        else
        {
            log.error("Target system passed into the proxy is null");
        }

        return url;
    }

    protected XDRRequestPortType getPort(String url)
    {
        XDRRequestPortType port = service.getXDRRequestPortSoap12();
        gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().initializePort((javax.xml.ws.BindingProvider) port, url);
        return port;
    }
}
