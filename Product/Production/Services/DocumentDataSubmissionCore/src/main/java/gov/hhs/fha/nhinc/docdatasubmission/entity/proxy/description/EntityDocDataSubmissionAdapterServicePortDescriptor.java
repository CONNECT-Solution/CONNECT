/**
 *
 */
package gov.hhs.fha.nhinc.docdatasubmission.entity.proxy.description;

import gov.hhs.fha.nhinc.entityxds.EntityXDSPortType;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import javax.xml.ws.soap.SOAPBinding;

/**
 * @author CONNECT New Staff
 *
 */
public class EntityDocDataSubmissionAdapterServicePortDescriptor implements ServicePortDescriptor<EntityXDSPortType> {

    @Override
    public String getWSAddressingAction() {
        return "urn:gov:hhs:fha:nhinc:adapterxds:RegisterDocumentSet-b";
    }

    @Override
    public Class<EntityXDSPortType> getPortClass() {
        return EntityXDSPortType.class;
    }

    @Override
    public String getSOAPBindingVersion() {
        return SOAPBinding.SOAP12HTTP_BINDING;
    }

}
