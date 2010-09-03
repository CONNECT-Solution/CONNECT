/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.lift.clientManager.client;

import gov.hhs.fha.nhinc.lift.clientController.SocketClientManagerController;
import gov.hhs.fha.nhinc.lift.common.util.LiftMessage;
import java.io.IOException;
import java.net.URI;

public interface ClientManager {
	public URI startClient(LiftMessage message, SocketClientManagerController controller) throws IOException;
}
