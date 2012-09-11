/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.docretrieve.entity;

import gov.hhs.fha.nhinc.docretrieve.nhin.proxy.NhinDocRetrieveProxy;
import gov.hhs.fha.nhinc.docretrieve.nhin.proxy.NhinDocRetrieveProxyObjectFactory;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.GATEWAY_API_LEVEL;
import gov.hhs.fha.nhinc.orchestration.OrchestrationStrategy;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author mweaver
 */
public class OutboundDocRetrieveStrategyImpl_g1 extends OutboundDocRetrieveStrategyBase implements
        OrchestrationStrategy {

    private static Log log = LogFactory.getLog(OutboundDocRetrieveStrategyImpl_g1.class);

    public OutboundDocRetrieveStrategyImpl_g1() {
    }

    @Override
    protected Log getLogger() {
        return log;
    }

    @Override
    protected RetrieveDocumentSetResponseType callProxy(OutboundDocRetrieveOrchestratable message) {
        getLogger().debug("Creating nhin (g1) doc retrieve proxy");
        NhinDocRetrieveProxy proxy = new NhinDocRetrieveProxyObjectFactory().getNhinDocRetrieveProxy();
        getLogger().debug("Sending nhin doc retrieve to nhin (g1)");
        return proxy.respondingGatewayCrossGatewayRetrieve(message.getRequest(), message.getAssertion(),
                message.getTarget(), GATEWAY_API_LEVEL.LEVEL_g1);
    }
}
