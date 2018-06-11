package gov.hhs.fha.nhinc.docdatasubmission.entity.proxy.description;

import gov.hhs.fha.nhinc.entityxdssecured.EntityXDSSecuredPortType;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import javax.xml.ws.soap.SOAPBinding;


public class EntityDocDataSubmissionSecuredAdapterServicePortDescriptor
implements ServicePortDescriptor<EntityXDSSecuredPortType> {

    @Override
    public String getWSAddressingAction() {
        return "urn:gov:hhs:fha:nhinc:adapterxdssecured:RegisterDocumentSet-b";
    }


    @Override
    public Class<EntityXDSSecuredPortType> getPortClass() {
        return EntityXDSSecuredPortType.class;
    }


    @Override
    public String getSOAPBindingVersion() {
        return SOAPBinding.SOAP12HTTP_BINDING;
    }

}
