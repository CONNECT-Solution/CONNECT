/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.adapter.patientdiscovery.async.request.error;

import gov.hhs.fha.nhinc.adapter.patientdiscovery.async.request.error.proxy.AdapterPatientDiscoveryAsyncReqErrorProxy;
import gov.hhs.fha.nhinc.adapter.patientdiscovery.async.request.error.proxy.AdapterPatientDiscoveryAsyncReqErrorProxyObjectFactory;
import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import javax.xml.ws.WebServiceContext;
import org.hl7.v3.AsyncAdapterPatientDiscoveryErrorRequestType;
import org.hl7.v3.MCCIIN000002UV01;

/**
 *
 * @author jhoppesc
 */
public class AdapterPatientDiscoveryAsyncReqErrorImpl {
    public MCCIIN000002UV01 processPatientDiscoveryAsyncReqError(AsyncAdapterPatientDiscoveryErrorRequestType request, WebServiceContext context) {
        MCCIIN000002UV01 ack = new MCCIIN000002UV01();

        // Extract the message id value from the WS-Addressing Header and place it in the Assertion Class
        if (request != null &&
                request.getAssertion() != null) {
            AsyncMessageIdExtractor msgIdExtractor = new AsyncMessageIdExtractor();
            request.getAssertion().setAsyncMessageId(msgIdExtractor.GetAsyncMessageId(context));
        }

        AdapterPatientDiscoveryAsyncReqErrorProxyObjectFactory adapterPatDiscAsyncReqErrorFactory = new AdapterPatientDiscoveryAsyncReqErrorProxyObjectFactory();

        AdapterPatientDiscoveryAsyncReqErrorProxy proxy = adapterPatDiscAsyncReqErrorFactory.getAdapterPatientDiscoveryAsyncReqErrorProxy();

        ack = proxy.processPatientDiscoveryAsyncReqError(request);

        return ack;
    }

}
