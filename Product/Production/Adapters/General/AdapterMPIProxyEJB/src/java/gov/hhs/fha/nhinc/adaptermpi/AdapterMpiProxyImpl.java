/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.adaptermpi;

import gov.hhs.fha.nhinc.adaptercomponentmpi.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.PIXConsumerPRPAIN201305UVRequestType;
import org.hl7.v3.PRPAIN201306UV;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;

import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import java.util.Map;
import javax.xml.ws.BindingProvider;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;

/**
 *
 * @author Jon Hoppesch
 */

public class AdapterMpiProxyImpl {
   private static Log log = LogFactory.getLog(AdapterMpiProxyImpl.class);

   private static AdapterMpiSecuredService service = new gov.hhs.fha.nhinc.adaptermpi.AdapterMpiSecuredService();

   /**
    * Send the patient query request to the actual MPI that is implemented
    *
    * @param
    * @return
    */
   public  PRPAIN201306UV query(PIXConsumerPRPAIN201305UVRequestType findCandidatesRequest)
   {
       log.debug("Entering AdapterMpiProxyImpl.query method...");
       String url = getURL();
       AdapterMpiSecuredPortType port = getPort(url);
       SamlTokenCreator tokenCreator = new SamlTokenCreator();
       Map requestContext = tokenCreator.CreateRequestContext(findCandidatesRequest.getAssertion(), url, NhincConstants.ADAPTER_MPI_ACTION);

       log.debug("Calling Secured Endpoint");
       
       return port.findCandidates(findCandidatesRequest);

       
   }
    private  String getURL()
    {
        String url = "";

        try
        {
            url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.ADAPTER_MPI_PROXY_SECURED_SERVICE_NAME);
        }
        catch(Exception ex)
        {
            log.error(ex.getMessage(), ex);
        }

        return url;
    }
    private  AdapterMpiSecuredPortType getPort(String url)
    {



        AdapterMpiSecuredPortType port = service.getAdapterMpiSecuredPortType();

        log.info("Setting endpoint address to MPI Secured Service to " + url);
        ((BindingProvider) port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);

        return port;
    }
}