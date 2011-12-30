/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docretrieve.entity;

import gov.hhs.fha.nhinc.orchestration.EntityOrchestratable;
import gov.hhs.fha.nhinc.orchestration.NhinAggregator;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author mweaver
 */
public class NhinDocRetrieveAggregator_a0 implements NhinAggregator {

    private static final Log logger = LogFactory.getLog(NhinDocRetrieveAggregator_a0.class);
    
    private Log getLogger() {
        return logger;
    }
    public void aggregate(EntityOrchestratable to, EntityOrchestratable from) {
        if (to instanceof EntityDocRetrieveOrchestratableImpl_a0) {
            if (from instanceof EntityDocRetrieveOrchestratableImpl_a0) {
                EntityDocRetrieveOrchestratableImpl_a0 to_a0 = (EntityDocRetrieveOrchestratableImpl_a0)to;
                EntityDocRetrieveOrchestratableImpl_a0 from_a0 = (EntityDocRetrieveOrchestratableImpl_a0)from;
                if (to_a0.getResponse() == null)
                {
                    to_a0.setResponse(new RetrieveDocumentSetResponseType());
                }
                to_a0.getResponse().getDocumentResponse().addAll(from_a0.getResponse().getDocumentResponse());
            } /*else if (from instanceof EntityDocRetrieveOrchestratableImpl_a1)
            {

            }*/
        } else {
            // throw error, this aggregator does not handle this case
            getLogger().error("This aggregator only aggregates to EntityDocRetrieveOrchestratableImpl_a0.");
        }
    }

}
