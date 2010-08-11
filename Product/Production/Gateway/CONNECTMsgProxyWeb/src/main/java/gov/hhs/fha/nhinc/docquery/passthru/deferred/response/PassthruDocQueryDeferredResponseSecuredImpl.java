/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docquery.passthru.deferred.response;

import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayQueryResponseSecuredType;
import gov.hhs.healthit.nhin.DocQueryAcknowledgementType;
import javax.xml.ws.WebServiceContext;
import gov.hhs.fha.nhinc.service.WebServiceHelper;

/**
 *
 * @author jhoppesc
 */
public class PassthruDocQueryDeferredResponseSecuredImpl {
    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(PassthruDocQueryDeferredResponseSecuredImpl.class);

    public DocQueryAcknowledgementType respondingGatewayCrossGatewayQuery(RespondingGatewayCrossGatewayQueryResponseSecuredType body, WebServiceContext context) {
        WebServiceHelper oHelper = new WebServiceHelper();
        PassthruDocQueryDeferredResponseOrchImpl orchImpl = new PassthruDocQueryDeferredResponseOrchImpl();
        DocQueryAcknowledgementType response = null;

        try {
            if (body != null && orchImpl != null) {
                response = (DocQueryAcknowledgementType) oHelper.invokeSecureDeferredResponseWebService(orchImpl, orchImpl.getClass(), "respondingGatewayCrossGatewayQuery", body, context);
            } else {
                log.error("Failed to call the web orchestration (" + orchImpl.getClass() + ".respondingGatewayCrossGatewayQuery).  The input parameter is null.");
            }
        } catch (Exception e) {
            log.error("Failed to call the web orchestration (" + orchImpl.getClass() + ".respondingGatewayCrossGatewayQuery).  An unexpected exception occurred.  " +
                    "Exception: " + e.getMessage(), e);
        }
        return response;
    }

}
