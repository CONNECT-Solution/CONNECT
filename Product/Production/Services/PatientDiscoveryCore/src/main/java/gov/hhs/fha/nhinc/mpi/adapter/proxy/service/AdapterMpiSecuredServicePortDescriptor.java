package gov.hhs.fha.nhinc.mpi.adapter.proxy.service;

import gov.hhs.fha.nhinc.adaptermpi.AdapterMpiSecuredPortType;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;

/**
 *
 *
 */
public class AdapterMpiSecuredServicePortDescriptor implements ServicePortDescriptor<AdapterMpiSecuredPortType> {

    private static final String WS_ADDRESSING_ACTION = "urn:gov:hhs:fha:nhinc:adaptermpi:FindCandidatesSecuredRequest";

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
    public Class<AdapterMpiSecuredPortType> getPortClass() {
        return AdapterMpiSecuredPortType.class;
    }
}
