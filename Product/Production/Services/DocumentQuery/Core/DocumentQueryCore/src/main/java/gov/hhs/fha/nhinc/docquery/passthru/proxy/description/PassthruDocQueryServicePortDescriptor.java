/**
 * 
 */
package gov.hhs.fha.nhinc.docquery.passthru.proxy.description;

import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import gov.hhs.fha.nhinc.nhincproxydocquery.NhincProxyDocQueryPortType;

/**
 * @author mweaver
 *
 */
public class PassthruDocQueryServicePortDescriptor implements ServicePortDescriptor<NhincProxyDocQueryPortType> {

    private static final String NAMESPACE_URI = "urn:gov:hhs:fha:nhinc:nhincproxydocquery";
    private static final String SERVICE_LOCAL_PART = "NhincProxyDocQuery";
    private static final String PORT_LOCAL_PART = "NhincProxyDocQueryPortSoap";
    private static final String WSDL_FILE = "NhincProxyDocQuery.wsdl";
    private static final String WS_ADDRESSING_ACTION = "urn:gov:hhs:fha:nhinc:nhincproxydocquery:RespondingGateway_CrossGatewayQueryRequestMessage";
    
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
    public Class<NhincProxyDocQueryPortType> getPortClass() {
        return NhincProxyDocQueryPortType.class;
    }

}
