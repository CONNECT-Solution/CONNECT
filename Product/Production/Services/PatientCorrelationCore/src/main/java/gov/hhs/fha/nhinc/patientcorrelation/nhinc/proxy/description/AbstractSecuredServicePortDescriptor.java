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
public abstract class AbstractSecuredServicePortDescriptor implements ServicePortDescriptor<PatientCorrelationSecuredPortType> {

    private static final String NAMESPACE_URI = "urn:gov:hhs:fha:nhinc:nhinccomponentpatientcorrelation";
    private static final String SERVICE_LOCAL_PART = "PatientCorrelationServiceSecured";
    private static final String PORT_LOCAL_PART = "PatientCorrelationSecuredPort";
    private static final String WSDL_FILE = "NhincComponentPatientCorrelationSecured.wsdl";
    
    /* (non-Javadoc)
     * @see gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor#getNamespaceUri()
     */
    @Override
    public String getNamespaceUri() {
        return NAMESPACE_URI;
    }
    
    /* (non-Javadoc)
     * @see gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor#getServiceLocalPart()
     */
    @Override
    public String getServiceLocalPart() {
        return SERVICE_LOCAL_PART;
    }

    /* (non-Javadoc)
     * @see gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor#getPortLocalPart()
     */
    @Override
    public String getPortLocalPart() {
        return PORT_LOCAL_PART;
    }

    /* (non-Javadoc)
     * @see gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor#getWSDLFileName()
     */
    @Override
    public String getWSDLFileName() {
        return WSDL_FILE;
    }
    
    /* (non-Javadoc)
     * @see gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor#getPortClass()
     */
    @Override
    public Class<PatientCorrelationSecuredPortType> getPortClass() {
        return PatientCorrelationSecuredPortType.class;
    }
}
