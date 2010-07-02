/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.xdr.async.response.adapter;

import javax.xml.ws.WebServiceContext;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import gov.hhs.fha.nhinc.adapterxdrresponse.AdapterXDRResponsePortType;
import gov.hhs.fha.nhinc.adapterxdrresponse.AdapterXDRResponseService;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterProvideAndRegisterDocumentSetRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.nhincadapterxdr.AdapterXDRPortType;
import gov.hhs.fha.nhinc.nhincadapterxdr.AdapterXDRService;
import gov.hhs.fha.nhinc.nhincentityxdrsecured.async.response.EntityXDRSecuredResponsePortType;
import gov.hhs.fha.nhinc.nhincentityxdrsecured.async.response.EntityXDRSecuredResponseService;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import gov.hhs.fha.nhinc.xdr.adapter.AdapterComponentXDRImpl;
import ihe.iti.xdr._2007.AcknowledgementType;
import java.util.Map;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceContext;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterRegistryResponseType;

/**
 *
 * @author patlollav
 */
public class AdapterXDRResponseSecuredImpl {

    private static final Log logger = LogFactory.getLog(AdapterXDRResponseSecuredImpl.class);
    private static final String ACK_SUCCESS_MESSAGE = "SUCCESS";
    private static AdapterXDRResponseService service = new AdapterXDRResponseService();

    public ihe.iti.xdr._2007.AcknowledgementType provideAndRegisterDocumentSetBResponse(RegistryResponseType body, WebServiceContext context) {
        getLogger().debug("Entering provideAndRegisterDocumentSetBResponse");

        // Log the Registry Response
        getLogger().debug("Registry Response " + body);

        String url = getUrl();
        AdapterXDRResponsePortType port = getPort(url);

        // Call AdapterComponent implementation to process the request.
        AssertionType assertion = SamlTokenExtractor.GetAssertion(context);

        AdapterRegistryResponseType unsecured = new AdapterRegistryResponseType();

        unsecured.setRegistryResponse(body);
        unsecured.setAssertion(assertion);
        
        ihe.iti.xdr._2007.AcknowledgementType ack = port.provideAndRegisterDocumentSetBResponse(unsecured);
        
        getLogger().debug("Exiting provideAndRegisterDocumentSetBResponse");

        return ack;
    }

    protected Log getLogger(){
        return logger;
    }

    protected String getUrl() {
        String url = null;

        try {
            url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.ADAPTER_XDR_RESPONSE_SERVICE_NAME);
        } catch (ConnectionManagerException ex) {
            getLogger().error("Error: Failed to retrieve url for service: " + NhincConstants.ADAPTER_XDR_RESPONSE_SERVICE_NAME, ex);
        }

        return url;
    }
    protected AdapterXDRResponsePortType getPort(String url) {
        AdapterXDRResponsePortType port = service.getAdapterXDRResponsePort();
        gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().initializePort((javax.xml.ws.BindingProvider) port, url);
        return port;
    }
}
