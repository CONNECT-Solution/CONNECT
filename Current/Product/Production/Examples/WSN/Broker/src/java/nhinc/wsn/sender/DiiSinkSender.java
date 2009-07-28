package nhinc.wsn.sender;

import java.net.URL;
import javax.xml.rpc.Call;
import javax.xml.rpc.Service;
import javax.xml.rpc.JAXRPCException;
import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceFactory;
import javax.xml.rpc.ParameterMode;

/**
 *
 * @author webbn
 */
public class DiiSinkSender
{

    private static String qnameService = "SubscriptionSinkService";
    private static String qnamePort = "SubscriptionSinkPortTypeBindingPort";
    private static String BODY_NAMESPACE_VALUE = "http://j2ee.netbeans.org/wsdl/Interfaces/SubscriptionSink";
    private static String ENCODING_STYLE_PROPERTY = "javax.xml.rpc.encodingstyle.namespace.uri";
    private static String NS_XSD = "http://www.w3.org/2001/XMLSchema";
    private static String URI_ENCODING = "http://schemas.xmlsoap.org/soap/encoding/";

    public String sendMessage(String msg)
    {

        String response = null;
        String endpointAddress = "http://localhost:8088/mockSubscriptionSinkPortTypeBinding";

        try
        {
            //System.setProperty("javax.xml.soap.MessageFactory", "com.sun.xml.messaging.saaj.soap.ver1_1.SOAPMessageFactory1_1Impl"); 

            ServiceFactory factory = ServiceFactory.newInstance();
//            Service service = factory.createService(new QName(BODY_NAMESPACE_VALUE, qnameService));

            String wsdlURL = "http://localhost:8088/mockSubscriptionSinkPortTypeBinding?WSDL";
            QName serviceQN = new QName(BODY_NAMESPACE_VALUE, qnameService);
            Service service = factory.createService(new URL(wsdlURL), serviceQN);

            QName port = new QName(BODY_NAMESPACE_VALUE, qnamePort);

            Call call = service.createCall(port);
            call.setTargetEndpointAddress(endpointAddress);

//            call.setProperty(Call.SOAPACTION_USE_PROPERTY, new Boolean(true));
//            call.setProperty(Call.SOAPACTION_URI_PROPERTY, "");
//            call.setProperty(ENCODING_STYLE_PROPERTY, URI_ENCODING);
            call.setProperty(Call.ENCODINGSTYLE_URI_PROPERTY, URI_ENCODING); 
            call.setProperty(Call.OPERATION_STYLE_PROPERTY, "document");
            QName QNAME_TYPE_STRING = new QName(NS_XSD, "string");
            call.setReturnType(QNAME_TYPE_STRING);

            call.setOperationName(new QName(BODY_NAMESPACE_VALUE, "Echo"));
            call.addParameter("EchoInput", QNAME_TYPE_STRING, ParameterMode.IN);
            String[] params =
            {
                "Murph!"
            };

            response = (String) call.invoke(params);
            System.out.println(response);

        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return response;
    }

    public String sendUsingClient(String msg)
    {
        String response = null;
        String endpointAddress = "http://localhost:8088/mockSubscriptionSinkPortTypeBinding";

        try
        { // Call Web Service Operation
            org.netbeans.j2ee.wsdl.interfaces.subscriptionsink.SubscriptionSinkService service = new org.netbeans.j2ee.wsdl.interfaces.subscriptionsink.SubscriptionSinkService();
            org.netbeans.j2ee.wsdl.interfaces.subscriptionsink.SubscriptionSinkPortType port = service.getSubscriptionSinkPortTypeBindingPort();
            ((javax.xml.ws.BindingProvider) port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpointAddress);
            // TODO initialize WS operation arguments here
            // TODO process result here
            response = port.echo(msg);
            System.out.println("Result = " + response);
        } catch (Exception ex)
        {
            System.out.println("Send failed: " + ex.getMessage());
            ex.printStackTrace();
        }

        return response;
    }
}
