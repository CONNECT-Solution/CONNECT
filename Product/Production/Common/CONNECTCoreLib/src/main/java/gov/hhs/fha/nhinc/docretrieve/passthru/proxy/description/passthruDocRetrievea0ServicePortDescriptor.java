/**
 * 
 */
package gov.hhs.fha.nhinc.docretrieve.passthru.proxy.description;

import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import gov.hhs.fha.nhinc.nhincproxydocretrieve.NhincProxyDocRetrievePortType;

/**
 * @author achidamb
 *
 */
public class passthruDocRetrievea0ServicePortDescriptor implements ServicePortDescriptor<NhincProxyDocRetrievePortType> {
    
          private static final String NAMESPACE_URI = "urn:gov:hhs:fha:nhinc:nhincproxydocretrieve";
          private static final String SERVICE_LOCAL_PART = "NhincProxyDocRetrieve";
          private static final String PORT_LOCAL_PART = "NhincProxyDocRetrievePortSoap";
          private static final String WSDL_FILE = "NhincProxyDocRetrieve.wsdl";
          private static final String WS_ADDRESSING_ACTION = "urn:gov:hhs:fha:nhinc:nhincproxydocretrieve:RespondingGateway_CrossGatewayRetrieveRequestMessage";
          
          @Override
          public String getNamespaceUri(){
              return NAMESPACE_URI;
          }
          
          @Override
          public String getServiceLocalPart(){
              return SERVICE_LOCAL_PART;
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

        @Override
        public Class<NhincProxyDocRetrievePortType> getPortClass() {
            return NhincProxyDocRetrievePortType.class;
        }
}
