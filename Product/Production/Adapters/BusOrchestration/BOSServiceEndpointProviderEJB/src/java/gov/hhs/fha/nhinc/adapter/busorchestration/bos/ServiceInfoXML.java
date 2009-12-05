/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.adapter.busorchestration.bos;

import com.thoughtworks.xstream.XStream;
import org.netbeans.xml.schema.endpoint.EPR;

/**
 *
 * @author Jerry Goodnough
 */
public class ServiceInfoXML {

        /**
     * This method serializes an Service object to an
     * XML string.
     *
     * @param oServiceInfo The object to be serialized.
         * @return The XML string representation of the object.
         * @throws ServiceLookupException
     */
    public static String serialize(ServiceInfo oServiceInfo)
        throws ServiceLookupException
    {
        String sXML = "";

        try
        {
            XStream oXStream = setupXStream();

            sXML = oXStream.toXML(oServiceInfo);
        }
        catch (Exception e)
        {
            String sErrorMessage = "Failed to serialize the CMUDDIConnectionInfo object to XML.  Error: " + e.getMessage();
            throw new ServiceLookupException(sErrorMessage, e);
        }

        return sXML;
    }

    /**
     * This method takes an XML representation of ServiceInfo and
     * produces an instance of the object.
     *
     * @param sXML The serialized representation of the CMUDDIConnectionInfo object.
     * @return The object instance of the XML.
     * @throws ServiceLookupException
     */
    public static ServiceInfo deserialize(String sXML)
        throws ServiceLookupException
    {
        ServiceInfo oServiceInfo = null;

        try
        {
            oServiceInfo = new ServiceInfo();

            XStream oXStream = setupXStream();
            Object oObject = oXStream.fromXML(sXML);
            if (oObject instanceof ServiceInfo)
            {
                oServiceInfo = (ServiceInfo) oObject;
            }
        }
        catch (Exception e)
        {
            String sErrorMessage = "Failed to deserialize the XML to a CMUDDIConnectionInfo object.  Error: " + e.getMessage();
            throw new ServiceLookupException(sErrorMessage, e);
        }


        return oServiceInfo;
    }

    private static XStream setupXStream()
    {
            XStream oXStream = new XStream();
            oXStream.alias("serviceinfo", ServiceInfo.class);
            oXStream.alias("servicemapping",ServiceMapping.class);
            oXStream.alias("endpoint",EPR.class);
            oXStream.alias("qname",javax.xml.namespace.QName.class);
            oXStream.addImplicitCollection(ServiceInfo.class,"m_ServiceList");
            oXStream.processAnnotations(ServiceInfo.class);
            return oXStream;
    }
}
