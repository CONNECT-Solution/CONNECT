package gov.hhs.fha.nhinc.lift.clientManager.client;

import java.io.IOException;
import java.net.UnknownHostException;

import gov.hhs.fha.nhinc.lift.common.util.LiftMessage;

/**
 * @author rrobin20
 *
 */
public interface ClientManager {
	public void startClient(LiftMessage message) throws UnknownHostException, IOException;
}
