package gov.hhs.fha.nhinc.docdatasubmission.entity.proxy.description;

import gov.hhs.fha.nhinc.entityxdssecured.EntityXDSSecuredPortType;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;


public class EntityDocDataSubmissionSecuredAdapterServicePortDescriptor
implements ServicePortDescriptor<EntityXDSSecuredPortType> {

    @Override
    public String getWSAddressingAction() {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public Class<EntityXDSSecuredPortType> getPortClass() {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public String getSOAPBindingVersion() {
        // TODO Auto-generated method stub
        return null;
    }

}
