/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.adapter.busorchestration.nhinadapteserviceejb;

import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Common Service Helper Functions
 *
 * @author Jerry Goodnough
 */
public class ServiceHelper {
   private static Log log = LogFactory.getLog(
            ServiceHelper.class);

   //TODO: Make the following property driven.

    private static String BOSEndpoint = "http://localhost:8080/BOSServiceEndpointProvider/ServiceEndpointProvider?WSDL";
    /**
     * Helper Function to get an Endpoint from BOS by Service Name
     * @param srvName Name
     * @return The endpoint reference or null if BOS does not have a mapping
     */
    public static String getEndpointFromBOS(String srvName)
    {
        String endpoint=null;
        try
        {
            log.debug("Create BOSServiceEndpointProvider");

            // Call Web Service Operation
            gov.hhs.fha.nhinc.bosserviceenpointprovider.BOSServiceEndpointProvider service = new gov.hhs.fha.nhinc.bosserviceenpointprovider.BOSServiceEndpointProvider();

            log.debug("Create BOSServiceEndpointProviderPortType");

            gov.hhs.fha.nhinc.bosserviceenpointprovider.BOSServiceEndpointProviderPortType port = service.getBOSServiceEndpointProviderSoap11();

            //Force the endpoint
            log.debug("Bind the Endpoint");

            //TODO: In the near futue use a system property to grab the BOS Endpoint
            ((BindingProvider) port).getRequestContext().put(
                BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
                BOSEndpoint);

            log.debug("Prepare the request");

            org.netbeans.xml.schema.endpoint.CreateEPRRequest findEndPointRequest = new org.netbeans.xml.schema.endpoint.CreateEPRRequest();
            findEndPointRequest.setServiceName(srvName);

            log.debug("Lookup endpoint for "+srvName);

            org.netbeans.xml.schema.endpoint.CreateEPRResponse result = port.findEndpoint(findEndPointRequest);
            if (result.getEPR()!= null)
            {
                log.debug("Review Results");
                endpoint = result.getEPR().getEndpointReference().getAddress().getValue();
                if (endpoint == null)
                {
                    log.error("Service "+srvName+" reference Inplace, but no mapping is defined");
                    throw new WebServiceException("Service "+srvName+" reference Inplace, but no mapping is defined");
                }
            }
            else
            {
                //BOS does not have a mapping - Use what ever default the caller wants
                log.debug("Request for Service "+srvName+" not mapped to and endpoint");
            }
        }
        catch (Exception ex)
        {
            log.error("Exception during dynamic service lookup",ex);
            throw new WebServiceException("Exception during endpoint lookup ("+srvName+")",ex);
        }

        log.debug("Returning "+endpoint);
        return endpoint;

    }

}
