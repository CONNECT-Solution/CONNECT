/**
 * 
 */
package gov.hhs.fha.nhinc.docrepository.adapter.proxy.service;

import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import ihe.iti.xds_b._2007.DocumentRepositoryPortType;

/**
 * @author mweaver
 *
 */
public class AdapterDocRepositoryUnsecuredServicePortDescriptor implements
        ServicePortDescriptor<DocumentRepositoryPortType> {

    private static final String NAMESPACE_URI = "urn:ihe:iti:xds-b:2007";
    private static final String SERVICE_LOCAL_PART = "DocumentRepository_Service";
    private static final String PORT_LOCAL_PART = "DocumentRepository_Port_Soap";
    private static final String WSDL_FILE = "AdapterComponentDocRepository.wsdl";
    private static final String WS_ADDRESSING_ACTION = "urn:ihe:iti:2007:RetrieveDocumentSet";
    
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
    public Class<DocumentRepositoryPortType> getPortClass() {
        return DocumentRepositoryPortType.class;
    }

}
