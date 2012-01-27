/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docretrieve.entity;

import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.orchestration.OutboundOrchestratable;
import gov.hhs.fha.nhinc.orchestration.InboundAggregator;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import java.util.Set;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author mweaver
 */
public class OutboundDocRetrieveAggregator_a0 implements InboundAggregator {

    private static final Log logger = LogFactory.getLog(OutboundDocRetrieveAggregator_a0.class);
    
    private Log getLogger() {
        return logger;
    }
    public void aggregate(OutboundOrchestratable to, OutboundOrchestratable from) {
        if (to instanceof EntityDocRetrieveOrchestratableImpl_a0) {
            if (from instanceof EntityDocRetrieveOrchestratableImpl_a0) {
                EntityDocRetrieveOrchestratableImpl_a0 to_a0 = (EntityDocRetrieveOrchestratableImpl_a0)to;
                EntityDocRetrieveOrchestratableImpl_a0 from_a0 = (EntityDocRetrieveOrchestratableImpl_a0)from;
                if (to_a0.getResponse() == null)
                {
                    to_a0.setResponse(new RetrieveDocumentSetResponseType());
                }

                if (to_a0.getResponse().getRegistryResponse() == null) {
                    RegistryResponseType rrt = new RegistryResponseType();
                    to_a0.getResponse().setRegistryResponse(rrt);
                }

                if (from_a0.getResponse() == null ||
                        from_a0.getResponse().getRegistryResponse() == null ||
                        NhincConstants.NHINC_ADHOC_QUERY_SUCCESS_RESPONSE.equalsIgnoreCase(from_a0.getResponse().getRegistryResponse().getStatus()))
                {
                    to_a0.getResponse().getRegistryResponse().setStatus(NhincConstants.NHINC_ADHOC_QUERY_SUCCESS_RESPONSE);
                } else if ("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure".equalsIgnoreCase(from_a0.getResponse().getRegistryResponse().getStatus()) ||
                        !from_a0.getResponse().getRegistryResponse().getRegistryErrorList().getRegistryError().isEmpty()) {
                    to_a0.getResponse().getRegistryResponse().setStatus("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure");
                    if (to_a0.getResponse().getRegistryResponse().getRegistryErrorList() == null)
                    {
                        to_a0.getResponse().getRegistryResponse().setRegistryErrorList(new RegistryErrorList());
                    }
                    to_a0.getResponse().getRegistryResponse().getRegistryErrorList().getRegistryError().addAll(from_a0.getResponse().getRegistryResponse().getRegistryErrorList().getRegistryError());
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
