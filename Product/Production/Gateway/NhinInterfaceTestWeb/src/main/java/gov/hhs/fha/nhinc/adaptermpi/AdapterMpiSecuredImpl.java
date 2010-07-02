/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.adaptermpi;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractorHelper;
import javax.xml.ws.WebServiceContext;
import gov.hhs.fha.nhinc.adaptercomponentmpi.AdapterComponentMpiService;
import gov.hhs.fha.nhinc.adaptercomponentmpi.AdapterComponentMpiPortType;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;

/**
 *
 * @author dunnek
 */
public class AdapterMpiSecuredImpl {
    private static Log log = LogFactory.getLog(AdapterMpiSecuredImpl.class);
    private static final String SERVICE_NAME = "mockadaptermpi";
    private static AdapterComponentMpiService service = new AdapterComponentMpiService();
    
    public org.hl7.v3.PRPAIN201306UV02 findCandidates(org.hl7.v3.PRPAIN201305UV02 findCandidatesRequest,WebServiceContext context) {
        PRPAIN201306UV02 response =null;

        String homeCommunityId = SamlTokenExtractorHelper.getHomeCommunityId();
        if (NullChecker.isNotNullish(homeCommunityId)) {

            AdapterComponentMpiPortType port = service.getAdapterComponentMpiPort();
            response = port.findCandidates(findCandidatesRequest);
			gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().initializePort((javax.xml.ws.BindingProvider) port, SamlTokenExtractorHelper.getEndpointURL(homeCommunityId, SERVICE_NAME));
        }

        return response;
    }
}
