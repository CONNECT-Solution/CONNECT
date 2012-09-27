package gov.hhs.fha.nhinc.mpi.adapter.proxy.service;

import gov.hhs.fha.nhinc.adaptermpi.AdapterMpiPortType;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;

public class AdapterMpiUnsecuredServicePortDescriptor implements ServicePortDescriptor<AdapterMpiPortType> {
	
	private static final String NAMESPACE_URI = "urn:gov:hhs:fha:nhinc:adaptermpi";
    private static final String SERVICE_LOCAL_PART = "AdapterMpiService";
    private static final String PORT_LOCAL_PART = "AdapterMpiPort";
    private static final String WSDL_FILE = "AdapterMpi.wsdl";
    private static final String WS_ADDRESSING_ACTION = "urn:gov:hhs:fha:nhinc:adaptermpi:FindCandidatesRequest";
	
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
     * @see gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor#getWSAddressingAction()
     */
    @Override
    public String getWSAddressingAction() {
        return WS_ADDRESSING_ACTION;
    }

    /* (non-Javadoc)
     * @see gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor#getWSDLFileName()
     */
	@Override
	public String getWSDLFileName() {
		return WSDL_FILE;
	}
    
    /* (non-Javadoc)
     * @see gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor#getPortClass()
     */
    @Override
    public Class<AdapterMpiPortType> getPortClass() {
        return AdapterMpiPortType.class;
    }
}
