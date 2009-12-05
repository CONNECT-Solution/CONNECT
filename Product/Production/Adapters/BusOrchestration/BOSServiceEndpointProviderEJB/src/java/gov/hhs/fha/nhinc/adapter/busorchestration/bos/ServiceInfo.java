/*

 */

package gov.hhs.fha.nhinc.adapter.busorchestration.bos;

import java.util.Iterator;
import java.util.LinkedList;
import org.netbeans.xml.schema.endpoint.EPR;
import org.xmlsoap.schemas.ws._2004._08.addressing.EndpointReferenceType;

/**
 * Collection of service mappings
 *
 * Note: we might consider making this more bean oriented and implementing
 * the tracking Hashmaps here rather the then ServiceEndpointCache. Minimun
 * additional method would include two lookups, and add and a clear. In addition
 * the serialization issues would need to be addressed and a remap method added
 *
 * @author Jerry Goodnough
 */
public class ServiceInfo {

    /**
     * Simple Linked list of the Services
     */
    public LinkedList<ServiceMapping> m_ServiceList = new LinkedList<ServiceMapping>();

    public boolean equals(Object o)
    {
        //Filter nulls
        if (o == null )
        {
            return false;
        }

        if (!(o instanceof ServiceInfo))
        {
            return false;
        }

        ServiceInfo so = (ServiceInfo)o;

        if (this.m_ServiceList.size() !=  so.m_ServiceList.size())
        {
           return false;
        }
        //Done with front en filters
        Iterator<ServiceMapping> itr1 = m_ServiceList.listIterator();
        Iterator<ServiceMapping> itr2 = so.m_ServiceList.iterator();

        //We know both lists are the same size so we only need to check on one for termination
        while (itr1.hasNext())
        {
            ServiceMapping e1 = itr1.next();
            ServiceMapping e2 = itr2.next();

            if (e1 == null)
            {
                if (e2 == null)
                {
                    continue;
                }
                else
                {
                    return false;
                }
            }
            if ( e2 == null && e1!= null )
            {
                return false;
            }

            if (!e1.equals(e2))
            {
                return false;
            }
        }
        return true;

    }
}
