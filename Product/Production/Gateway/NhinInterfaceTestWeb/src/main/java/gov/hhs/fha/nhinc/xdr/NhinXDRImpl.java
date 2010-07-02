/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.xdr;
import gov.hhs.fha.nhinc.nhincproxyxdr.ProxyXDRPortType;
import gov.hhs.fha.nhinc.nhincproxyxdr.ProxyXDRService;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetRequestType;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractorHelper;
/**
 *
 * @author dunnek
 */
public class NhinXDRImpl {
    private static Log log = LogFactory.getLog(NhinXDRImpl.class);
    private static final String SERVICE_NAME = "mockxdr";
    public RegistryResponseType documentRepositoryProvideAndRegisterDocumentSetB(
                ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType body,
                javax.xml.ws.WebServiceContext context
                )
    {
        RegistryResponseType response =null;
        RespondingGatewayProvideAndRegisterDocumentSetRequestType request = null;

        request = new RespondingGatewayProvideAndRegisterDocumentSetRequestType();

        request.setProvideAndRegisterDocumentSetRequest(body);
        request.setAssertion(SamlTokenExtractor.GetAssertion(context));

        String homeCommunityId = SamlTokenExtractorHelper.getHomeCommunityId();
        if (NullChecker.isNotNullish(homeCommunityId)) {
            ProxyXDRService service = new ProxyXDRService();
            ProxyXDRPortType port = service.getProxyXDRPort();
			gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().initializePort((javax.xml.ws.BindingProvider) port,SamlTokenExtractorHelper.getEndpointURL(homeCommunityId, SERVICE_NAME));
            response = port.provideAndRegisterDocumentSetB(request);
        } else {
           response = null;
        }
        return response;
    }
}
