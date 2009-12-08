
package gov.hhs.fha.nhinc.adapter.busorchestration.bos;

import org.netbeans.xml.schema.endpoint.EPR;
import org.xmlsoap.schemas.ws._2004._08.addressing.EndpointReferenceType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xmlsoap.schemas.ws._2004._08.addressing.AttributedURI;

/**
 *
 * @author Jerry Goodnough
 */
public class ServiceEndpointProviderImpl
{

    private static ServiceEndpointProviderImpl singlton = null;
    private static Log log =
                       LogFactory.getLog(ServiceEndpointProviderImpl.class);


    static
    {
        singlton = new ServiceEndpointProviderImpl();
    }

    /**
     * Provides access to the singleton
     * @return A ServiceEndpointProvider Singlton
     */
    public static ServiceEndpointProviderImpl getInstance()
    {
        return singlton;
    }

    /**
     * Core Lookup Service for the Service point lookup'
     * 
     * @param bosFindEndPointRequest
     * @return CreateEPRResponse - If the entry is not found the EPR element will be null.
     */
    public org.netbeans.xml.schema.endpoint.CreateEPRResponse findEndpoint(
            org.netbeans.xml.schema.endpoint.CreateEPRRequest bosFindEndPointRequest)
    {
        org.netbeans.xml.schema.endpoint.CreateEPRResponse out = null;

        if (bosFindEndPointRequest != null) {
            out = new org.netbeans.xml.schema.endpoint.CreateEPRResponse();
            String serviceName = bosFindEndPointRequest.getServiceName();
            String endpointURL = bosFindEndPointRequest.getEndpointURL();

            //bosFindEndPointRequest.getNamespacePrefix(); - Currently unused
            //bosFindEndPointRequest.getNamespaceURI(); - Currently unused
            //bosFindEndPointRequest.getPortName(); - Currently unused

            EPR epr = null;
            boolean bFnd = false;

            //Service Name, if present takes priority
            if (serviceName != null && !serviceName.isEmpty()) {
                log.info("Service Endpoint request for Service " + serviceName);
                //We look up the service and present the endpoint reference for it.
                epr = findEndpointbyServiceName(serviceName);

                if (epr != null && !epr.getEndpointReference().getAddress().getValue().
                        isEmpty()) {
                    bFnd = true;
                } else {
                    log.warn("Request for Service " + serviceName +
                            " is not currently mapped");
                }

            }

            if (!bFnd) {
                // If service name lookup failed we try
                if (endpointURL != null && !endpointURL.isEmpty()) //If no service name is present the we try the endpoint URL.
                {
                    log.info("Service Enpoint request for Service " + serviceName);
                    // First we see if it is in the registry
                    epr = findEndpointbyURL(endpointURL);
                    if (epr != null && !epr.getEndpointReference().getAddress().
                            getValue().isEmpty()) {
                        bFnd = true;
                    } else {
                        log.info("Endpoint not mapped to any redirection" +
                                endpointURL + " reflecting");

                        //The following code to reflects the URL
                        epr = new EPR();
                        EndpointReferenceType eprt = new EndpointReferenceType();
                        AttributedURI auVal = new AttributedURI();
                        auVal.setValue(endpointURL);
                        eprt.setAddress(auVal);
                        epr.setEndpointReference(eprt);

                    }
                } else {
                    if (serviceName == null || serviceName.isEmpty()) {
                        log.warn("Service lookup did not provide name or URL");
                        dumpRequest(bosFindEndPointRequest);
                    }

                    //We have already warned about the service not being found
                }
            }

            out.setEPR(epr);
        }
        return out;
    }

    private void dumpRequest(org.netbeans.xml.schema.endpoint.CreateEPRRequest request)
    {
        log.warn("Raw Request = "+request.toString());
        StringBuffer buf = new StringBuffer();
        buf.append("EndPointURL = "+request.getEndpointURL()+"\n");
        buf.append("NamespacePrefix = "+request.getNamespacePrefix()+"\n");
        buf.append("NamespaceURI = "+request.getNamespaceURI()+"\n");
        buf.append("PortName = "+request.getPortName()+"\n");
        buf.append("ServiceName = "+request.getServiceName());
        
        log.warn(buf.toString());
    }
    /**
     * Internal helper to find the EPR associated with a Service nane
     * @param serviceName The Service Name to search for
     * @return The EPR or null of not associated
     */

    private EPR findEndpointbyServiceName(String serviceName)
    {
        EPR out = null;
        try
        {
            out = ServiceEndpointCache.findEndpointByServiceName(serviceName);
        }
        catch (Exception exp)
        {
            log.error("Exception looking up endpoint by Name: " + serviceName,
                      exp);
        }
        return out;

    }
    /**
     * Internal helper to get a EPR using the URL. Delegates most work to the ServiceEndpointCache
     * @param url The Url to search for
     * @return The EPR that was found or Null
     */
    private EPR findEndpointbyURL(String url)
    {
        EPR out = null;
        try
        {
            out = ServiceEndpointCache.findEndpointByServiceURL(url);
        }
        catch (Exception exp)
        {
            log.error("Exception looking up endpoint by URL: " + url, exp);
        }
        return out;

    }
}
