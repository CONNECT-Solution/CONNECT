/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.adapter.patientdiscovery.async.response;

import gov.hhs.fha.nhinc.adapter.patientdiscovery.async.response.proxy.AdapterPatientDiscoveryAsyncRespProxy;
import gov.hhs.fha.nhinc.adapter.patientdiscovery.async.response.proxy.AdapterPatientDiscoveryAsyncRespProxyObjectFactory;
import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import javax.xml.ws.WebServiceContext;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02RequestType;

/**
 *
 * @author JHOPPESC
 */
public class AdapterPatientDiscoveryAsyncRespImpl {
    public MCCIIN000002UV01 processPatientDiscoveryAsyncResp(RespondingGatewayPRPAIN201306UV02RequestType request, WebServiceContext context) {
        MCCIIN000002UV01 ack = new MCCIIN000002UV01();

        // Extract the message id value from the WS-Addressing Header and place it in the Assertion Class
        if (request != null &&
                request.getAssertion() != null) {
            AsyncMessageIdExtractor msgIdExtractor = new AsyncMessageIdExtractor();
            request.getAssertion().setAsyncMessageId(msgIdExtractor.GetAsyncRelatesTo(context));
        }

        AdapterPatientDiscoveryAsyncRespProxyObjectFactory adapterPatDiscAsyncRespFactory = new AdapterPatientDiscoveryAsyncRespProxyObjectFactory();

        AdapterPatientDiscoveryAsyncRespProxy proxy = adapterPatDiscAsyncRespFactory.getAdapterPatientDiscoveryAsyncRespProxy();

        ack = proxy.processPatientDiscoveryAsyncResp(request);

        return ack;
    }

}
