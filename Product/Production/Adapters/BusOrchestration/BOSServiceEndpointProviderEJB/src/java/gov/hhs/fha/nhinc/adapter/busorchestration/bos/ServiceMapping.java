/*
 */

package gov.hhs.fha.nhinc.adapter.busorchestration.bos;

import org.netbeans.xml.schema.endpoint.EPR;
import org.xmlsoap.schemas.ws._2004._08.addressing.EndpointReferenceType;

/**
 * Basic Data class 
 *
 * @author Jerry Goodnough
 */
public class ServiceMapping {

    /**
     * The Name of the Service (For mapping lookup)
     */
    private String serviceName;
    /**
     * The URL of the Service (For mapping lookup)
     */
    private String serviceURL;
    /**
     * The Endpoint reference that is mapped to the two identifiers
     */
    private EPR endPoint;

    /**
     * Get the enpoint mapped to the service.
     * @return The mapped endpoint
     */
    public EPR getEndPoint()
    {
        return endPoint;
    }

    /**
     * Set ther Endpoint that is mapped
     * @param newEndPoint
     */
    public void setEndPoint(EPR newEndPoint)
    {
        endPoint = newEndPoint;
    }

    /**
     * Get the Name of the Service used for mapping
     * @return The Name of the Service or null
     */
    public String getServiceName()
    {
        return serviceName;
    }

    /**
     * Set the Service name by which the endpoint is mapped
     * @param newServiceName
     */
    public void setServiceName(String newServiceName)
    {
        serviceName = newServiceName;
    }

    /**
     * Get the URL by which the Service is mapped
     * @return The Mapping URL
     */
    public String getServiceURL()
    {
        return serviceURL;
    }

    /**
     * Set the Mapping URL for the Service
     * @param newServiceURL
     */
    public void setServiceURL(String newServiceURL)
    {
        serviceURL = newServiceURL;
    }

    public boolean equals(Object o)
    {
        //Filter nulls
        if (o == null )
        {
            return false;
        }

        if (!(o instanceof ServiceMapping))
        {
            return false;
        }

        ServiceMapping ro = (ServiceMapping) o;

        if ((serviceName == null) && (ro.serviceName!=null))
        {
           return false;
        }
        if ((serviceName != null) && (ro.serviceName==null))
        {
            return false;
        }

        if ((serviceName!=null)&&(ro.serviceName!=null)&&serviceName.compareTo(ro.serviceName)!=0)
        {
            return false;
        }

        if ((serviceURL== null) && (ro.serviceURL!=null))
        {
            return false;
        }
        if ((serviceURL != null) && (ro.serviceURL ==null))
        {
            return false;
        }
        if ((serviceURL!=null) && (ro.serviceURL!=null) && serviceURL.compareTo(ro.serviceURL)!=0)
        {
            return false;
        }

        //More involved check of the EPR
        EndpointReferenceType epr1 = endPoint.getEndpointReference();
        EndpointReferenceType epr2 = ro.endPoint.getEndpointReference();
        if (!epr1.getAddress().getValue().equals(epr2.getAddress().getValue()))
        {
            return false;
        }

        return true;

    }
}
