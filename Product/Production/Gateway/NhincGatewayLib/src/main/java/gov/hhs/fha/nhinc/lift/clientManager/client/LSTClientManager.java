package gov.hhs.fha.nhinc.lift.clientManager.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URL;
import java.net.UnknownHostException;

import gov.hhs.fha.nhinc.lift.clientManager.client.properties.interfaces.ClientPropertiesFacade;
import gov.hhs.fha.nhinc.lift.common.util.LiftMessage;
import gov.hhs.fha.nhinc.lift.common.util.RequestToken;
import gov.hhs.fha.nhinc.lift.fileConsumer.HttpFileConsumer;
import gov.hhs.fha.nhinc.lift.proxy.client.ClientConnectorManager;
import gov.hhs.fha.nhinc.lift.proxy.properties.interfaces.ConsumerProxyPropertiesFacade;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author rrobin20
 * 
 */
public class LSTClientManager implements ClientManager {

    private final ClientPropertiesFacade props;
    private final ConsumerProxyPropertiesFacade proxyProps;
    private Log log = null;

    public LSTClientManager(ClientPropertiesFacade props,
            ConsumerProxyPropertiesFacade proxyProps) {
        super();
        this.props = props;
        this.proxyProps = proxyProps;
        log = createLogger();
    }

    protected Log createLogger() {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    @Override
    public void startClient(LiftMessage message) throws UnknownHostException,
            IOException {

        /*
         * Use default file destination
         */
        String fileDest = props.getDefaultFileDestination();

        /*
         * Use message contents to get server proxy location and security token
         */
        String serverAddress = message.getData().getServerProxyData().getServerProxyAddress();
        int serverPort = message.getData().getServerProxyData().getServerProxyPort();
        log.debug("Server Proxy Address: " + serverAddress);
        log.debug("Server proxy port: " + serverPort);

        /*
         * Use message contents to start client accordingly. Need to get from
         * the data the file to grab from the server and buffer size. May need
         * protocol or maybe some other more complex later on. Client Manager
         * implementations are responsible for that functionality.
         */
        String clientData = message.getData().getClientData().getData();
        log.debug("Client data: " + clientData);

        /*
         * Just going to fix a buffer size, not really important at this point.
         *
         * TODO Might was to put this in the properties somewhere though.
         */
        int bufferSize = 65536;
        String serverFile = clientData;

        /* ClientConnectionManager lives in LiFTProxy */
        ClientConnectorManager ccm = ClientConnectorManager.getInstance();

        RequestToken token = message.getRequest();

        InetSocketAddress clientProxyAddress = ccm.startConnector(token,
                InetAddress.getByName(serverAddress), serverPort, bufferSize,
                proxyProps);
        log.debug("Client proxy address: " + clientProxyAddress.getAddress().toString());
        /*
         * Take the client proxy address and turn it into a URL with client
         * data and give it to the test client.
         *  Knows to do this because it knows the specific client it is trying to
         *  launch
         */
        URL url = new URL("http", clientProxyAddress.getHostName(),
                clientProxyAddress.getPort(), serverFile.replace('\\', '/'));

        log.debug("URL going to give file consumer: " + url);

        /*
         * Launch file downloader. Could instead do whatever is necessary to
         * start some other client if desired.
         */
        HttpFileConsumer.consumeFile(url.toString(), fileDest, bufferSize);
    }
}
