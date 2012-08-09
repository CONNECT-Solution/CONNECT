/**
 * 
 */
package gov.hhs.fha.nhinc.docretrieve.nhin.proxy.description;


import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import ihe.iti.xds_b._2007.RespondingGatewayRetrievePortType;

/**
 * @author achidamb
 *
 */
public class NhinDocRetrieveServicePortDescriptor implements ServicePortDescriptor<RespondingGatewayRetrievePortType>  {

    private static final String NAMESPACE_URI = "urn:ihe:iti:xds-b:2007";
    private static final String SERVICE_LOCAL_PART = "RespondingGateway_Retrieve_Service";
    private static final String PORT_LOCAL_PART = "RespondingGateway_Retrieve_Port_Soap";
    private static final String WSDL_FILE = "NhinDocRetrieve.wsdl";
    private static final String WS_ADDRESSING_ACTION = "urn:ihe:iti:2007:CrossGatewayQuery";

    
    @Override
    public String getNamespaceUri(){
        return NAMESPACE_URI;
    }
    
    @Override
    public String getServiceLocalPart(){
        return SERVICE_LOCAL_PART;
    }
    
    @Override
    public Class<RespondingGatewayRetrievePortType> getPortClass() {
        return RespondingGatewayRetrievePortType.class;
    }
    
    @Override
    public String getPortLocalPart(){
        return PORT_LOCAL_PART;
    }
    
    @Override
    public String getWSDLFileName(){
        return WSDL_FILE;
    }
    
    @Override
    public String getWSAddressingAction(){
        return WS_ADDRESSING_ACTION;
    }
    
}
