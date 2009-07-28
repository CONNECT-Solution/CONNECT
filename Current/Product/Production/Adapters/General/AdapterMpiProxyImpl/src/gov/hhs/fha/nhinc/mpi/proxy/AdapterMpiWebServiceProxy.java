/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.mpi.proxy;

import gov.hhs.fha.nhinc.adaptercomponentmpi.AdapterComponentMpiPortType;
import gov.hhs.fha.nhinc.adaptercomponentmpi.AdapterComponentMpiService;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.PRPAIN201305UV;
import org.hl7.v3.PRPAIN201306UV;

/**
 *
 * @author jhoppesc
 */
public class AdapterMpiWebServiceProxy implements AdapterMpiProxy {

    private static Log log = LogFactory.getLog(AdapterMpiWebServiceProxy.class);
    static AdapterComponentMpiService mpiService = new AdapterComponentMpiService();

    public PRPAIN201306UV findCandidates(PRPAIN201305UV findCandidatesRequest) {
        String url = null;
        PRPAIN201306UV result = new PRPAIN201306UV();

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

        log.info("Setting endpoint address to MPI Service to " + url);
        ((javax.xml.ws.BindingProvider) port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);
        return port;
    }

}
