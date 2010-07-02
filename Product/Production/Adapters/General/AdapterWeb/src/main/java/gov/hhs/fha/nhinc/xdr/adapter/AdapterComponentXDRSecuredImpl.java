/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.xdr.adapter;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.xml.ws.WebServiceContext;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterProvideAndRegisterDocumentSetRequestType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import gov.hhs.fha.nhinc.nhincadapterxdr.AdapterXDRPortType;
import gov.hhs.fha.nhinc.nhincadapterxdr.AdapterXDRService;
import javax.xml.ws.BindingProvider;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;


/**
 *
 * @author dunnek
 */
public class AdapterComponentXDRSecuredImpl {
    private static Log log = null;
    private static AdapterXDRService service = null;


    public AdapterComponentXDRSecuredImpl()
    {
        log = createLogger();
        service = createService();
    }
    public RegistryResponseType provideAndRegisterDocumentSetb(ProvideAndRegisterDocumentSetRequestType body, WebServiceContext context) {
        log.debug("begin provideAndRegisterDocumentSetb()");
        AssertionType assertion = SamlTokenExtractor.GetAssertion(context);
        AdapterProvideAndRegisterDocumentSetRequestType unsecured = new AdapterProvideAndRegisterDocumentSetRequestType();
        String url = "";
        AdapterXDRPortType port = null;
        RegistryResponseType result = null;
        XDRHelper helper = new XDRHelper();

        url = getUrl();
        port = getPort(url);
        
        unsecured.setAssertion(assertion);
        unsecured.setProvideAndRegisterDocumentSetRequest(body);

         result = port.provideAndRegisterDocumentSetb(unsecured);
        log.debug("end provideAndRegisterDocumentSetb()");

        return result;
    }

    protected Log createLogger()
    {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    protected AdapterXDRService createService()
    {
        return new AdapterXDRService();
    }
    protected String getUrl() {
        String url = null;
 
        try {
            url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.ADAPTER_XDR_SERVICE_NAME);
        } catch (ConnectionManagerException ex) {
            log.error("Error: Failed to retrieve url for service: " + NhincConstants.ADAPTER_XDR_SERVICE_NAME);
            log.error(ex.getMessage());
        }
 

        return url;
    }
    protected AdapterXDRPortType getPort(String url) {

        AdapterXDRPortType port = service.getAdapterXDRPort();

        gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().initializePort((javax.xml.ws.BindingProvider) port, url);

        return port;
    }
}
