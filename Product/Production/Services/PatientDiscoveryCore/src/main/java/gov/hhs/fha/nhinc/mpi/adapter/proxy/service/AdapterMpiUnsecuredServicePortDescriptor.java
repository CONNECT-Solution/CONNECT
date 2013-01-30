package gov.hhs.fha.nhinc.mpi.adapter.proxy.service;

import gov.hhs.fha.nhinc.adaptermpi.AdapterMpiPortType;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;

/**
 *
 *
 */
public class AdapterMpiUnsecuredServicePortDescriptor implements ServicePortDescriptor<AdapterMpiPortType> {

    private static final String WS_ADDRESSING_ACTION = "urn:gov:hhs:fha:nhinc:adaptermpi:FindCandidatesRequest";

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor#getWSAddressingAction()
     */
    @Override
    public String getWSAddressingAction() {
        return WS_ADDRESSING_ACTION;
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor#getPortClass()
     */
    @Override
    public Class<AdapterMpiPortType> getPortClass() {
        return AdapterMpiPortType.class;
    }
}
