/**
 * 
 */
package gov.hhs.fha.nhinc.patientcorrelation.nhinc.proxy.description;

/**
 * @author mweaver
 *
 */
public class PatientCorrelationAddServicePortDescriptor extends AbstractServicePortDescriptor {
    private static final String WS_ADDRESSING_ACTION_ADD = "urn:gov:hhs:fha:nhinc:nhinccomponentpatientcorrelation:AddPatientCorrelationRequestMessage";
    
    /* (non-Javadoc)
     * @see gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor#getWSAddressingAction()
     */
    @Override
    public String getWSAddressingAction() {
        return WS_ADDRESSING_ACTION_ADD;
    }
}
