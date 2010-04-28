/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.adapter.patientdiscovery.async.request.queue;

import gov.hhs.fha.nhinc.adapter.patientdiscovery.async.request.queue.proxy.AdapterPatientDiscoveryAsyncReqQueueProxy;
import gov.hhs.fha.nhinc.adapter.patientdiscovery.async.request.queue.proxy.AdapterPatientDiscoveryAsyncReqQueueProxyObjectFactory;
import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import javax.xml.ws.WebServiceContext;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;

/**
 *
 * @author JHOPPESC
 */
public class AdapterPatientDiscoveryAsyncReqQueueImpl {
    public MCCIIN000002UV01 addPatientDiscoveryAsyncReq(RespondingGatewayPRPAIN201305UV02RequestType request, WebServiceContext context) {
        MCCIIN000002UV01 ack = new MCCIIN000002UV01();

        // Extract the message id value from the WS-Addressing Header and place it in the Assertion Class
        if (request != null &&
                request.getAssertion() != null) {
            AsyncMessageIdExtractor msgIdExtractor = new AsyncMessageIdExtractor();
            request.getAssertion().setAsyncMessageId(msgIdExtractor.GetAsyncMessageId(context));
        }

        AdapterPatientDiscoveryAsyncReqQueueProxyObjectFactory adapterPatDiscAsyncReqQueueFactory = new AdapterPatientDiscoveryAsyncReqQueueProxyObjectFactory();

        AdapterPatientDiscoveryAsyncReqQueueProxy proxy = adapterPatDiscAsyncReqQueueFactory.getAdapterPatientDiscoveryAsyncReqQueueProxy();

        ack = proxy.addPatientDiscoveryAsyncReq(request);

        return ack;
    }

}
