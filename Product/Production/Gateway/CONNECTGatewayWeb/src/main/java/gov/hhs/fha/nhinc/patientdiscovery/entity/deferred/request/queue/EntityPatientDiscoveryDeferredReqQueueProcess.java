/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.patientdiscovery.entity.deferred.request.queue;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import org.hl7.v3.MCCIIN000002UV01;

@WebService(serviceName = "EntityPatientDiscoveryAsyncReqQueueProcess")
public class EntityPatientDiscoveryDeferredReqQueueProcess {

    /**
     * processPatientDiscoveryAsyncReqQueue WebMethod for processing request queues on reponding gateway
     * @param messageId
     * @return
     */
    @WebMethod(operationName = "processPatientDiscoveryAsyncReqQueue")
    public String processPatientDiscoveryAsyncReqQueue(@WebParam(name = "messageId") String messageId) {
        String result = "Failed";
        MCCIIN000002UV01 mCCIIN000002UV01 = new MCCIIN000002UV01();
        EntityPatientDiscoveryDeferredReqQueueProcessImpl entityPatientDiscoveryDeferredReqQueueProcessImpl = new EntityPatientDiscoveryDeferredReqQueueProcessImpl();
        mCCIIN000002UV01 = entityPatientDiscoveryDeferredReqQueueProcessImpl.processPatientDiscoveryAsyncReqQueue(messageId);
        if (mCCIIN000002UV01 != null) {
            result = "Success";
        }
        return result;
    }
}
