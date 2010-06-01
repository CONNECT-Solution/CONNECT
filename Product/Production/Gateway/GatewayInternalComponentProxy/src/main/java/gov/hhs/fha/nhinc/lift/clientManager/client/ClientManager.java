package gov.hhs.fha.nhinc.lift.clientManager.client;

import gov.hhs.fha.nhinc.lift.clientController.SocketClientManagerController;
import gov.hhs.fha.nhinc.lift.common.util.LiftMessage;
import java.io.IOException;
import java.net.URI;

public interface ClientManager {
	public URI startClient(LiftMessage message, SocketClientManagerController controller) throws IOException;
}
