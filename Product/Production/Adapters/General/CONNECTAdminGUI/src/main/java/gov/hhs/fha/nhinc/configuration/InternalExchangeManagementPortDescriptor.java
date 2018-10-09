package gov.hhs.fha.nhinc.configuration;

import gov.hhs.fha.nhinc.internalexchangemanagement.EntityInternalExchangeManagementPortType;
import gov.hhs.fha.nhinc.messaging.service.port.SOAP12ServicePortDescriptor;

/**
 *
 * @author ptambellini
 */
public class InternalExchangeManagementPortDescriptor
extends SOAP12ServicePortDescriptor<EntityInternalExchangeManagementPortType> {

    private static final String NAMESPACE_URI = "urn:gov:hhs:fha:nhinc:internalexchangemangement";
    private static final String WS_ADDRESSING_ACTION = NAMESPACE_URI + ":EntityInternalExchangeManagementRequest";

    @Override
    public String getWSAddressingAction() {
        return WS_ADDRESSING_ACTION;
    }

    @Override
    public Class<EntityInternalExchangeManagementPortType> getPortClass() {
        return EntityInternalExchangeManagementPortType.class;
    }

}
