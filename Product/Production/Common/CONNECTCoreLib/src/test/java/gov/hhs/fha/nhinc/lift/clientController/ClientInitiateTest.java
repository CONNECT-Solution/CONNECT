/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.lift.clientController;

import gov.hhs.fha.nhinc.lift.common.util.ClientDataToken;
import gov.hhs.fha.nhinc.lift.common.util.LiftMessage;
import gov.hhs.fha.nhinc.lift.common.util.DataToken;
import gov.hhs.fha.nhinc.lift.common.util.InterProcessSocketProtocol;
import gov.hhs.fha.nhinc.lift.common.util.JaxbUtil;
import gov.hhs.fha.nhinc.lift.common.util.RequestToken;
import gov.hhs.fha.nhinc.lift.common.util.ServerProxyDataToken;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import javax.xml.bind.JAXBException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author vvickers
 */
public class ClientInitiateTest {

    public ClientInitiateTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }
    
    @Test
    public void testClientSocket() {
    //Temporary place holder
    }
     /*

    @Test
    public void testClientSocket() {
        InetSocketAddress caddr = null;
        Socket clientSocket = null;
        try {
            // client socket should be started
            String clientIP = PropertyAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.LIFT_CLIENT_IP);
            String clientPort = PropertyAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.LIFT_CLIENT_PORT);
            caddr = new InetSocketAddress(clientIP, Integer.parseInt(clientPort));
            clientSocket = new Socket(caddr.getAddress(), caddr.getPort());
            System.out.println("New socket: " + clientSocket);
            // client expects a LiftMessage type so we create one
            LiftMessage message = new LiftMessage();

            RequestToken request = new RequestToken("5d4bb0d5-414e-48b4-b86a-be31926a4076");
            message.setRequest(request);

            DataToken data = new DataToken();
            ClientDataToken cdata = new ClientDataToken();
            //cdata.setData("/5d4bb0d5-414e-48b4-b86a-be31926a4076/unknown.png");
            //cdata.setData("/5d4bb0d5-414e-48b4-b86a-be31926a4076/05222009-3.png");
            cdata.setData("/5d4bb0d5-414e-48b4-b86a-be31926a4076/TopLevel_OofBFT_LW.pdf");
            //cdata.setData("/5d4bb0d5-414e-48b4-b86a-be31926a4076/README.txt");
            data.setClientData(cdata);

            ServerProxyDataToken sdata = new ServerProxyDataToken();
            String serverIP = PropertyAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.LIFT_PROXY_ADDRESS);
            String serverPort = PropertyAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.LIFT_PROXY_PORT);
            InetSocketAddress saddr = new InetSocketAddress(serverIP, Integer.parseInt(serverPort));
            sdata.setServerProxyAddress(saddr.getAddress().getHostAddress());
            sdata.setServerProxyPort(saddr.getPort());
            data.setServerProxyData(sdata);
            message.setData(data);

            String content = JaxbUtil.marshalToString(message);
            System.out.println("Attempt to send: " + content);
            InterProcessSocketProtocol processSocket = new InterProcessSocketProtocol();
            processSocket.sendData(content, clientSocket.getOutputStream());
            System.out.println("Send data on socket: " + clientSocket);
        } catch (JAXBException ex) {
            Assert.fail("Could not JAXB marshal the the message: " + ex.getMessage());
        } catch (IOException ex) {
            Assert.fail("Could not connect to client socket: " + caddr);
        } catch (PropertyAccessException ex) {
            Assert.fail("Could not obtain client socket ip and port properties ");
        } finally {
            if (clientSocket != null) {
                try {
                    // Closing the socket also closes streams
                    System.out.println("Close socket: " + clientSocket);
                    clientSocket.close();
                } catch (IOException ex) {
                    System.out.println("Unable to close socket: " + clientSocket);
                }
            }
        }
    }
      */
}
