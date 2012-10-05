/**
 * 
 */
package gov.hhs.fha.nhinc.docretrieve.adapter.proxy.service;

import gov.hhs.fha.nhinc.adapterdocretrieve.AdapterDocRetrievePortType;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;

/**
 * @author mweaver
 *
 */
public class AdapterDocRetrieveUnsecuredServicePortDescriptor implements
        ServicePortDescriptor<AdapterDocRetrievePortType> {

    private static final String NAMESPACE_URI = "urn:gov:hhs:fha:nhinc:adapterdocretrieve";
    private static final String SERVICE_LOCAL_PART = "AdapterDocRetrieve";
    private static final String PORT_LOCAL_PART = "AdapterDocRetrievePortSoap";
    private static final String WSDL_FILE = "AdapterDocRetrieve.wsdl";
    private static final String WS_ADDRESSING_ACTION = NAMESPACE_URI + ":"
            + "RespondingGateway_CrossGatewayRetrieveRequestMessage";
    
    /* (non-Javadoc)
     * @see gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor#getNamespaceUri()
     */
    @Override
    public String getNamespaceUri() {
        return NAMESPACE_URI;
    }

    /* (non-Javadoc)
     * @see gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor#getServiceLocalPart()
     */
    @Override
    public String getServiceLocalPart() {
        return SERVICE_LOCAL_PART;
    }

    /* (non-Javadoc)
     * @see gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor#getPortLocalPart()
     */
    @Override
    public String getPortLocalPart() {
        return PORT_LOCAL_PART;
    }

    /* (non-Javadoc)
     * @see gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor#getWSDLFileName()
     */
    @Override
    public String getWSDLFileName() {
        return WSDL_FILE;
    }

    /* (non-Javadoc)
     * @see gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor#getWSAddressingAction()
     */
    @Override
    public String getWSAddressingAction() {
        return WS_ADDRESSING_ACTION;
    }

    /* (non-Javadoc)
     * @see gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor#getPortClass()
     */
    @Override
    public Class<AdapterDocRetrievePortType> getPortClass() {
        return AdapterDocRetrievePortType.class;
    }

}
