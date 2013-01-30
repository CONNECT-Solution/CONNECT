package gov.hhs.fha.nhinc.mpi.adapter.component.proxy.service;

import gov.hhs.fha.nhinc.adaptercomponentmpi.AdapterComponentMpiSecuredPortType;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;

/**
 *
 *
 */
public class AdapterComponentMpiSecuredServicePortDescriptor implements
        ServicePortDescriptor<AdapterComponentMpiSecuredPortType> {

    private static final String WS_ADDRESSING_ACTION = "urn:gov:hhs:fha:nhinc:adaptercomponentmpi:FindCandidatesSecuredRequest";

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
    public Class<AdapterComponentMpiSecuredPortType> getPortClass() {
        return AdapterComponentMpiSecuredPortType.class;
    }
}
