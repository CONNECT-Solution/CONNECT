package gov.hhs.fha.nhinc.configuration;

import gov.hhs.fha.nhinc.exchangemanagement.EntityExchangeManagementPortType;
import gov.hhs.fha.nhinc.messaging.service.port.SOAP12ServicePortDescriptor;

/**
 *
 * @author ttang
 */
public class ExchangeManagementPortDescriptor extends SOAP12ServicePortDescriptor<EntityExchangeManagementPortType> {

    private static final String WS_ADDRESSING_ACTION = "urn:gov:hhs:fha:nhinc:exchangemangement"
        + "EntityExchangeManagementRequest";

    @Override
    public String getWSAddressingAction() {
        return WS_ADDRESSING_ACTION;
    }

    @Override
    public Class<EntityExchangeManagementPortType> getPortClass() {
        return EntityExchangeManagementPortType.class;
    }

}
