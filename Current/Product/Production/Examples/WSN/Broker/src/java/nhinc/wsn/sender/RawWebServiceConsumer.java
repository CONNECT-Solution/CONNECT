package nhinc.wsn.sender;

import java.net.URL;
import javax.jws.soap.SOAPBinding;
import javax.xml.namespace.QName;
import javax.xml.rpc.Call;
import javax.xml.rpc.ServiceFactory;
import javax.xml.rpc.Service;

/**
 *
 * @author webbn
 */
public class RawWebServiceConsumer
{
    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(RawWebServiceConsumer.class);

    public String sendRawMessage(String message)
    {
        String response = null;
        try
        { // Call Web Service Operation
            String namespace = "http://j2ee.netbeans.org/wsdl/Interfaces/SubscriptionSink";
            
            String serviceName = "SubscriptionSinkService";
            QName serviceQN = new QName(namespace, serviceName);

            ServiceFactory serviceFactory = ServiceFactory.newInstance();
//            Service service = serviceFactory.createService(serviceQN);

            String wsdlURL = "http://localhost:8088/mockSubscriptionSinkPortTypeBinding?WSDL";
            Service service = serviceFactory.createService(new URL(wsdlURL), serviceQN);

//            String portName = "SubscriptionSinkPortTypeBindingPort";
            String portName = "SubscriptionSinkPortType";
            QName portQN = new QName(namespace, portName);
            String operationName = "Echo";

            Call call = service.createCall(portQN, new QName(namespace, operationName));
            // Standard property for encoding Style: Encoding style specified as a namespace URI. The default value is the SOAP 1.1 encoding http://schemas.xmlsoap.org/soap/encoding/
            call.setProperty(Call.ENCODINGSTYLE_URI_PROPERTY, "http://schemas.xmlsoap.org/soap/encoding/"); 
            // Standard property for operation style. This property is set to "rpc" if the operation style is rpc; "document" if the operation style is document.
//            call.setProperty(Call.OPERATION_STYLE_PROPERTY, "wrapped");
//            call.setProperty(Call.OPERATION_STYLE_PROPERTY, "bare");
//            call.setProperty(Call.OPERATION_STYLE_PROPERTY, "rpc");
            call.setProperty(Call.OPERATION_STYLE_PROPERTY, "document");
            
            call.setTargetEndpointAddress("http://localhost:8088/mockSubscriptionSinkPortTypeBinding");
            //call.removeAllParameters();
            //call.setPortTypeName(portQN);
            //call.setOperationName(new QName(namespace, operationName));
            
            call.addParameter("MsgIn", new QName("http://www.w3.org/2001/XMLSchema", "string"),
		    String.class, javax.xml.rpc.ParameterMode.IN);
            call.setReturnType(new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class);
            
            Object[] inParams = new Object[] {"Jane"};
            
            log.debug("Calling invoke");
            response = (String) call.invoke(inParams);
            log.debug("Return messge: " + response);
        } 
        catch (Exception ex)
        {
            log.error("Error calling web service: " + ex.getMessage(), ex);
        }
        
        
//        JAX-RPC ServiceFactory
//        String wsdlURL = http://localhost:6080/HelloWebService/services/Hello?wsdl";
//        String namespace = "http://Hello.com";
//        String serviceName = "HelloWebService";
//        QName serviceQN = new QName(namespace, serviceName);
//
//        ServiceFactory serviceFactory = ServiceFactory.newInstance();
//        /* The "new URL(wsdlURL)" parameter is optional */
//        Service service = serviceFactory.createService(new URL(wsdlURL), serviceQN);
        
        return response;
    }
}
