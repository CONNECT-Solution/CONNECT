/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.lift.clientManager.client;

import gov.hhs.fha.nhinc.lift.clientController.SocketClientManagerController;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URL;
import gov.hhs.fha.nhinc.lift.clientManager.client.properties.interfaces.ClientPropertiesFacade;
import gov.hhs.fha.nhinc.lift.common.util.LiftMessage;
import gov.hhs.fha.nhinc.lift.common.util.RequestToken;
import gov.hhs.fha.nhinc.lift.fileConsumer.HttpFileConsumer;
import gov.hhs.fha.nhinc.lift.proxy.client.ClientConnectorManager;
import gov.hhs.fha.nhinc.lift.proxy.properties.interfaces.ConsumerProxyPropertiesFacade;
import java.net.URI;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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

    protected final Log createLogger() {
        if (log == null) {
            log = LogFactory.getLog(getClass());
        }
        return log;
    }

    @Override
    public URI startClient(final LiftMessage message, final SocketClientManagerController controller) throws IOException {
        URI writtenFile = null;
        try {
            log.debug("Processing incoming message ");

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
            int bufferSize = 65536;
            String serverFile = clientData;
            log.debug("Client data: " + clientData);

            /* ClientConnectionManager lives in LiFTProxy */
            ClientConnectorManager ccm = ClientConnectorManager.getInstance();
            RequestToken token = message.getRequest();
            InetSocketAddress clientProxyAddress = ccm.startConnector(token, InetAddress.getByName(serverAddress), serverPort, bufferSize, proxyProps, controller);
            log.debug("Client proxy address: " + clientProxyAddress.getAddress().toString());

            /*
             * Take the client proxy address and turn it into a URL with client
             * data and give it to the test client.
             *  Knows to do this because it knows the specific client it is trying to
             *  launch
             */
            URL url = new URL("http", clientProxyAddress.getHostName(), clientProxyAddress.getPort(), serverFile.replace('\\', '/'));

            /*
             * Launch file downloader. Could instead do whatever is necessary to
             * start some other client if desired.
             */
            String fileDest = props.getDefaultFileDestination();
            log.debug("File destination set from properties as: " + fileDest);
            HttpFileConsumer consumer = new HttpFileConsumer();
            writtenFile = consumer.consumeFile(url.toString(), fileDest, bufferSize);

        } catch (IOException ex) {
            throw new IOException("Unable to process incoming message for " + message + ";" + ex.getMessage());
        }
        return writtenFile;
    }
}
