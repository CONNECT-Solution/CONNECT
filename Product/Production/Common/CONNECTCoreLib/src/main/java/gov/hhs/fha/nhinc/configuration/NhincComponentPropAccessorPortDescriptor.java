package gov.hhs.fha.nhinc.configuration;

import gov.hhs.fha.nhinc.messaging.service.port.SOAP12ServicePortDescriptor;
import gov.hhs.fha.nhinc.nhinccomponentpropaccessor.NhincComponentPropAccessorPortType;

/**
 *
 * @author ptambellini
 */
public class NhincComponentPropAccessorPortDescriptor
extends SOAP12ServicePortDescriptor<NhincComponentPropAccessorPortType> {

    @Override
    public String getWSAddressingAction() {
        return "urn:gov:hhs:fha:nhinc:nhinccomponentpropaccessor:NhincComponentPropAccessor";
    }

    @Override
    public Class<NhincComponentPropAccessorPortType> getPortClass() {
        return NhincComponentPropAccessorPortType.class;
    }

}
