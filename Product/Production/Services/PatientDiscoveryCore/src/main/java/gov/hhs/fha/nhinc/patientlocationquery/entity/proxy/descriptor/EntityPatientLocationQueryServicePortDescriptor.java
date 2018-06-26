package gov.hhs.fha.nhinc.patientlocationquery.entity.proxy.descriptor;

import gov.hhs.fha.nhinc.entitypatientlocationquery.EntityPatientLocationQueryPortType;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import javax.xml.ws.soap.SOAPBinding;

public class EntityPatientLocationQueryServicePortDescriptor
    implements ServicePortDescriptor<EntityPatientLocationQueryPortType> {

    @Override
    public String getWSAddressingAction() {
        return "urn:gov:hhs:fha:nhinc:entitypatientlocationquery:RespondingGateway_PatientLocationQueryRequestMessage";
    }

    @Override
    public Class<EntityPatientLocationQueryPortType> getPortClass() {
        return EntityPatientLocationQueryPortType.class;
    }

    @Override
    public String getSOAPBindingVersion() {
        return SOAPBinding.SOAP12HTTP_BINDING;
    }

}
