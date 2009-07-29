package gov.hhs.fha.nhinc.connectmgr.data;

import com.thoughtworks.xstream.XStream;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;

/**
 * This class is used to serialize/deserialize an XML document to/from a UDDIConnectionInfo object.
 * This class uses XStream to do this.
 * 
 * @author Les Westberg.
 */
public class CMUDDIConnectionInfoXML
{
    /**
     * This method serializes an CMUDDIConnectionInfo object to an
     * XML string.
     * 
     * @param oCMUDDIConnectionInfo The object to be serialized.
     * @return The XML string representation of the object.
     */
    public static String serialize(CMUDDIConnectionInfo oCMUDDIConnectionInfo)
        throws ConnectionManagerException
    {
        String sXML = "";
        
        try
        {
            XStream oXStream = new XStream();
            oXStream.alias("UDDIConnectionInfo", CMUDDIConnectionInfo.class);
            oXStream.addImplicitCollection(CMBusinessEntities.class, "businessEntityList", "businessEntity", CMBusinessEntity.class);
            oXStream.useAttributeFor(CMBusinessEntity.class, "businessKey");
            oXStream.addImplicitCollection(CMDiscoveryURLs.class, "discoveryURLList", "discoveryURL", String.class);
            oXStream.addImplicitCollection(CMBusinessNames.class, "businessNameList", "businessName", String.class);
            oXStream.addImplicitCollection(CMBusinessDescriptions.class, "businessDescriptionList", "businessDescription", String.class);
            oXStream.addImplicitCollection(CMContacts.class, "contactList", "contact", CMContact.class);
            oXStream.addImplicitCollection(CMContactDescriptions.class, "descriptionList", "description", String.class);
            oXStream.addImplicitCollection(CMPersonNames.class, "personNameList", "personName", String.class);
            oXStream.addImplicitCollection(CMPhones.class, "phoneList", "phone", String.class);
            oXStream.addImplicitCollection(CMEmails.class, "emailList", "email", String.class);
            oXStream.addImplicitCollection(CMAddresses.class, "addressList", "address", CMAddress.class);
            oXStream.addImplicitCollection(CMAddress.class, "addressLineList", "addressLine", String.class);
            oXStream.addImplicitCollection(CMStates.class, "stateList", "state", String.class);
            oXStream.addImplicitCollection(CMBusinessServices.class, "businessServiceList", "businessService", CMBusinessService.class);
            oXStream.useAttributeFor(CMBusinessService.class, "serviceKey");
            oXStream.addImplicitCollection(CMBindingNames.class, "nameList", "name", String.class);
            oXStream.addImplicitCollection(CMBindingDescriptions.class, "descriptionList", "description", String.class);
            oXStream.addImplicitCollection(CMBindingTemplates.class, "bindingTemplateList", "bindingTemplate", CMBindingTemplate.class);
            oXStream.useAttributeFor(CMBindingTemplate.class, "bindingKey");

            oXStream.processAnnotations(CMUDDIConnectionInfo.class);
            sXML = oXStream.toXML(oCMUDDIConnectionInfo);
        }
        catch (Exception e)
        {
            String sErrorMessage = "Failed to serialize the CMUDDIConnectionInfo object to XML.  Error: " + e.getMessage();
            throw new ConnectionManagerException(sErrorMessage, e);
        }
        
        return sXML;
    }
    
    /**
     * This method takes an XML representation of CMUDDIConnectionInfo and
     * produces an instance of the object.
     * 
     * @param sXML The serialized representation of the CMUDDIConnectionInfo object.
     * @return The object instance of the XML.
     */
    public static CMUDDIConnectionInfo deserialize(String sXML)
        throws ConnectionManagerException
    {
        CMUDDIConnectionInfo oCMUDDIConnectionInfo = null;

        try
        {
            oCMUDDIConnectionInfo = new CMUDDIConnectionInfo();
            
            XStream oXStream = new XStream();
            oXStream.alias("UDDIConnectionInfo", CMUDDIConnectionInfo.class);
            oXStream.addImplicitCollection(CMBusinessEntities.class, "businessEntityList", "businessEntity", CMBusinessEntity.class);
            oXStream.useAttributeFor(CMBusinessEntity.class, "businessKey");
            oXStream.addImplicitCollection(CMDiscoveryURLs.class, "discoveryURLList", "discoveryURL", String.class);
            oXStream.addImplicitCollection(CMBusinessNames.class, "businessNameList", "businessName", String.class);
            oXStream.addImplicitCollection(CMBusinessDescriptions.class, "businessDescriptionList", "businessDescription", String.class);
            oXStream.addImplicitCollection(CMContacts.class, "contactList", "contact", CMContact.class);
            oXStream.addImplicitCollection(CMContactDescriptions.class, "descriptionList", "description", String.class);
            oXStream.addImplicitCollection(CMPersonNames.class, "personNameList", "personName", String.class);
            oXStream.addImplicitCollection(CMPhones.class, "phoneList", "phone", String.class);
            oXStream.addImplicitCollection(CMEmails.class, "emailList", "email", String.class);
            oXStream.addImplicitCollection(CMAddresses.class, "addressList", "address", CMAddress.class);
            oXStream.addImplicitCollection(CMAddress.class, "addressLineList", "addressLine", String.class);
            oXStream.addImplicitCollection(CMStates.class, "stateList", "state", String.class);
            oXStream.addImplicitCollection(CMBusinessServices.class, "businessServiceList", "businessService", CMBusinessService.class);
            oXStream.useAttributeFor(CMBusinessService.class, "serviceKey");
            oXStream.addImplicitCollection(CMBindingNames.class, "nameList", "name", String.class);
            oXStream.addImplicitCollection(CMBindingDescriptions.class, "descriptionList", "description", String.class);
            oXStream.addImplicitCollection(CMBindingTemplates.class, "bindingTemplateList", "bindingTemplate", CMBindingTemplate.class);
            oXStream.useAttributeFor(CMBindingTemplate.class, "bindingKey");

            oXStream.processAnnotations(CMInternalConnectionInfos.class);
            Object oObject = oXStream.fromXML(sXML);
            if (oObject instanceof CMUDDIConnectionInfo)
            {
                oCMUDDIConnectionInfo = (CMUDDIConnectionInfo) oObject;
            }
        }
        catch (Exception e)
        {
            String sErrorMessage = "Failed to deserialize the XML to a CMUDDIConnectionInfo object.  Error: " + e.getMessage();
            throw new ConnectionManagerException(sErrorMessage, e);
        }
            
        
        return oCMUDDIConnectionInfo;
    }
    
}
