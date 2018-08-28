package gov.hhs.fha.nhinc.configuration;

import gov.hhs.fha.nhinc.adminguimanagement.AdminGUIManagementPortType;
import gov.hhs.fha.nhinc.messaging.service.port.SOAP12ServicePortDescriptor;

public class AdminGUIManagementPortDescriptor extends SOAP12ServicePortDescriptor<AdminGUIManagementPortType> {

    private static final String WS_ADDRESSING_ACTION = "urn:gov:hhs:fha:nhinc:adminguimanagement:DashboardStatus";

    @Override
    public String getWSAddressingAction() {
        return WS_ADDRESSING_ACTION;
    }

    @Override
    public Class<AdminGUIManagementPortType> getPortClass() {
        return AdminGUIManagementPortType.class;
    }

}
