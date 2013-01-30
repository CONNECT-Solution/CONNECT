package gov.hhs.fha.nhinc.mpi.adapter.component.proxy.service;

import gov.hhs.fha.nhinc.adaptercomponentmpi.AdapterComponentMpiPortType;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;

/**
 *
 *
 */
public class AdapterComponentMpiUnsecuredServicePortDescriptor implements
        ServicePortDescriptor<AdapterComponentMpiPortType> {

    private static final String WS_ADDRESSING_ACTION = "urn:gov:hhs:fha:nhinc:adaptercomponentmpi:FindCandidatesRequest";

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
    public Class<AdapterComponentMpiPortType> getPortClass() {
        return AdapterComponentMpiPortType.class;
    }
}
