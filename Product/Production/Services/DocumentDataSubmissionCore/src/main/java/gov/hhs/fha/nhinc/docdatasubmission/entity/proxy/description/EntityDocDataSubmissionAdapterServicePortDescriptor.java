/**
 * 
 */
package gov.hhs.fha.nhinc.docdatasubmission.entity.proxy.description;

import gov.hhs.fha.nhinc.entityxds.EntityXDSPortType;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;

/**
 * @author CONNECT New Staff
 *
 */
public class EntityDocDataSubmissionAdapterServicePortDescriptor implements ServicePortDescriptor<EntityXDSPortType> {

    /* (non-Javadoc)
     * @see gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor#getWSAddressingAction()
     */
    @Override
    public String getWSAddressingAction() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor#getPortClass()
     */
    @Override
    public Class<EntityXDSPortType> getPortClass() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor#getSOAPBindingVersion()
     */
    @Override
    public String getSOAPBindingVersion() {
        // TODO Auto-generated method stub
        return null;
    }

}
