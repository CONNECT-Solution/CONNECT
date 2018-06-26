package gov.hhs.fha.nhinc.patientlocationquery.entity.proxy.descriptor;

import gov.hhs.fha.nhinc.entitypatientlocationquerysecured.EntityPatientLocationQuerySecuredPortType;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import javax.xml.ws.soap.SOAPBinding;

public class EntityPatientLocationQuerySecuredServicePortDescriptor
    implements ServicePortDescriptor<EntityPatientLocationQuerySecuredPortType> {

    @Override
    public String getWSAddressingAction() {
        return "urn:gov:hhs:fha:nhinc:entitypatientlocationquerysecured:RespondingGateway_PatientLocationQueryRequestMessage";
    }

    @Override
    public Class<EntityPatientLocationQuerySecuredPortType> getPortClass() {
        return EntityPatientLocationQuerySecuredPortType.class;
    }

    @Override
    public String getSOAPBindingVersion() {
        return SOAPBinding.SOAP12HTTP_BINDING;
    }

}
