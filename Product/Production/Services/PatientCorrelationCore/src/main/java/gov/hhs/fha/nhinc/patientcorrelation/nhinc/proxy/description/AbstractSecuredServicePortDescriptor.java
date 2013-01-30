/**
 * 
 */
package gov.hhs.fha.nhinc.patientcorrelation.nhinc.proxy.description;

import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import gov.hhs.fha.nhinc.nhinccomponentpatientcorrelation.PatientCorrelationSecuredPortType;

/**
 * @author mweaver
 * 
 */
public abstract class AbstractSecuredServicePortDescriptor implements
        ServicePortDescriptor<PatientCorrelationSecuredPortType> {

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor#getPortClass()
     */
    @Override
    public Class<PatientCorrelationSecuredPortType> getPortClass() {
        return PatientCorrelationSecuredPortType.class;
    }
}
