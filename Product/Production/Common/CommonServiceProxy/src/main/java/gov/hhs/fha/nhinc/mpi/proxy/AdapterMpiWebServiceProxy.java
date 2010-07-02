/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.mpi.proxy;

import gov.hhs.fha.nhinc.adaptercomponentmpi.AdapterComponentMpiPortType;
import gov.hhs.fha.nhinc.adaptercomponentmpi.AdapterComponentMpiService;
import gov.hhs.fha.nhinc.adaptercomponentmpi.AdapterComponentMpiSecuredPortType;
import gov.hhs.fha.nhinc.adaptercomponentmpi.AdapterComponentMpiSecuredService;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import javax.xml.ws.BindingProvider;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
/**
 *
 * @author jhoppesc
 */
public class AdapterMpiWebServiceProxy implements AdapterMpiProxy {

    private static Log log = LogFactory.getLog(AdapterMpiWebServiceProxy.class);
    static AdapterComponentMpiService mpiService = new AdapterComponentMpiService();
    static AdapterComponentMpiSecuredService mpiSecuredService = new AdapterComponentMpiSecuredService();
    
    public PRPAIN201306UV02 findCandidates(PRPAIN201305UV02 findCandidatesRequest, AssertionType assertion)
    {
       log.warn("Calling secured interface from unsecured implementation");
 
       return findCandidates(findCandidatesRequest);
    }
    public PRPAIN201306UV02 findCandidates(PRPAIN201305UV02 findCandidatesRequest) {
        String url = null;
        PRPAIN201306UV02 result = new PRPAIN201306UV02();

        try {
            url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.ADAPTER_MPI_SERVICE_NAME);
        } catch (ConnectionManagerException ex) {
            log.error("Error: Failed to retrieve url for service: " + NhincConstants.ADAPTER_MPI_SERVICE_NAME + " for local home community");
            log.error(ex.getMessage());
        }

        if (NullChecker.isNotNullish(url)) {
            AdapterComponentMpiPortType port = getPort(url);

            result = port.findCandidates(findCandidatesRequest);
        }

        return result;
    }

    private AdapterComponentMpiPortType  getPort(String url) {
        AdapterComponentMpiPortType  port = mpiService.getAdapterComponentMpiPort();
        gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().initializePort((javax.xml.ws.BindingProvider) port, url);
        return port;
    }
    private AdapterComponentMpiSecuredPortType  getSecuredPort(String url) {
        AdapterComponentMpiSecuredPortType  port = mpiSecuredService.getAdapterComponentMpiSecuredPort();
        gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().initializePort((javax.xml.ws.BindingProvider) port, url);
        return port;
    }
    private String getSecuredURL()
    {
        String url = "";

        try
        {
            url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.ADAPTER_MPI_SECURED_SERVICE_NAME);
        }
        catch(Exception ex)
        {
            log.error(ex.getMessage(), ex);
        }

        return url;
    }
}
