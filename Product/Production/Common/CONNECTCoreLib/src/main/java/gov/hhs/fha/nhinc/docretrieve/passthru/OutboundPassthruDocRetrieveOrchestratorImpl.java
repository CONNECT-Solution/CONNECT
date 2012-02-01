/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2012(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package gov.hhs.fha.nhinc.docretrieve.passthru;

import gov.hhs.fha.nhinc.orchestration.CONNECTOutboundOrchestrator;
import gov.hhs.fha.nhinc.orchestration.Orchestratable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author akong
 */
public class OutboundPassthruDocRetrieveOrchestratorImpl extends CONNECTOutboundOrchestrator {

    private static final Log logger = LogFactory.getLog(OutboundPassthruDocRetrieveOrchestratorImpl.class);

    @Override
    public Orchestratable processEnabledMessage(Orchestratable message) {
        return super.processPassThruMessage(message);
    }

    @Override
    public Log getLogger() {
        return logger;
    }
}
