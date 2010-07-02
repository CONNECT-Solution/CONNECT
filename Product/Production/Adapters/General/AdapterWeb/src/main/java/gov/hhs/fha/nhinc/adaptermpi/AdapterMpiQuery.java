/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.adaptermpi;

import gov.hhs.fha.nhinc.adaptercomponentmpi.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.PRPAIN201305UV02;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;

import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import javax.xml.ws.BindingProvider;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author Jon Hoppesch
 */

public class AdapterMpiQuery {
   private static Log log = LogFactory.getLog(AdapterMpiQuery.class);
   
   private static final String GATEWAY_PROPERTY_FILE = "gateway";
   private static final String HOME_COMMUNITY_ID_PROPERTY = "localHomeCommunityId";
   private static final String SERVICE_NAME_ADAPTER_COMPONENT_MPI_SERVICE = "adaptercomponentmpiservice";


   /**
    * Send the patient query request to the actual MPI that is implemented
    * 
    * @param 
    * @return
    */
   public static PRPAIN201306UV02 query(PRPAIN201305UV02 findCandidatesRequest, WebServiceContext context) {
        log.debug("Entering AdapterMpiQuery.query secured method...");
       String url = getURL();
       AdapterComponentMpiSecuredPortType port = getPort(url);
       SamlTokenCreator tokenCreator = new SamlTokenCreator();

       AssertionType assertion = gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor.GetAssertion(context);

       java.util.Map requestContext;
       requestContext = tokenCreator.CreateRequestContext(assertion, url,  NhincConstants.ADAPTER_MPI_ACTION);
       ((BindingProvider) port).getRequestContext().putAll(requestContext);
       
       return port.findCandidates(findCandidatesRequest);

   }
   public static PRPAIN201306UV02 query(PRPAIN201305UV02 findCandidatesRequest) {
       log.debug("Entering AdapterMpiQuery.query method...");
       PRPAIN201306UV02  queryResponse = null;
       AdapterComponentMpiService mpiService = new AdapterComponentMpiService ();
       AdapterComponentMpiPortType mpiPort = mpiService.getAdapterComponentMpiPort(); 
       
       // Get the Home community ID for this box...
       //------------------------------------------
       String sHomeCommunityId = "";
       String sEndpointURL = "";
       try {
           sHomeCommunityId = PropertyAccessor.getProperty(GATEWAY_PROPERTY_FILE, HOME_COMMUNITY_ID_PROPERTY);
       }
       catch (Exception e) {
           log.error("Failed to read " + HOME_COMMUNITY_ID_PROPERTY + 
                     " property from the " + GATEWAY_PROPERTY_FILE + ".properties  file.  Error: " + 
                     e.getMessage(), e);
       }

       if ((sHomeCommunityId != null) && (sHomeCommunityId.length() > 0)) {
           try {
               sEndpointURL = ConnectionManagerCache.getEndpointURLByServiceName(sHomeCommunityId, SERVICE_NAME_ADAPTER_COMPONENT_MPI_SERVICE);
           }
           catch (Exception e) {
               log.error("Failed to retrieve endpoint URL for service:" + SERVICE_NAME_ADAPTER_COMPONENT_MPI_SERVICE + 
                         " from connection manager.  Error: " + e.getMessage(), e);
           }
       }
       
       if ((sEndpointURL != null) &&
           (sEndpointURL.length() > 0)) {
           log.debug("calling " + sEndpointURL);
		   gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().initializePort((javax.xml.ws.BindingProvider) mpiPort, sEndpointURL);
       }
       else {
           // Just a way to cover ourselves for the time being...  - assume port 8080
           //-------------------------------------------------------------------------
		   gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().initializePort((javax.xml.ws.BindingProvider) mpiPort, "http://localhost:8080/NhinConnect/AdapterComponentMpiService");

           log.warn("Did not find endpoint URL for service: " + SERVICE_NAME_ADAPTER_COMPONENT_MPI_SERVICE + " and " +
                    "Home Community: " + sHomeCommunityId + ".  Using default URL: " +
                    "'http://localhost:8080/NhinConnect/AdapterComponentMpiService'");
       }
       
       if (findCandidatesRequest != null) {
           queryResponse = mpiPort.findCandidates(findCandidatesRequest);
       }
       else {
           queryResponse = null;
       }
       
       log.debug("Exiting AdapterMpiQuery.query method...");
       return queryResponse;
   }
       private static AdapterComponentMpiSecuredPortType getPort(String url)
       {
           AdapterComponentMpiSecuredService mpiSecuredService = new AdapterComponentMpiSecuredService();
           AdapterComponentMpiSecuredPortType port = mpiSecuredService.getAdapterComponentMpiSecuredPort();

           gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().initializePort((javax.xml.ws.BindingProvider) port, url);

           return port;
       }
    private static String getURL()
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
