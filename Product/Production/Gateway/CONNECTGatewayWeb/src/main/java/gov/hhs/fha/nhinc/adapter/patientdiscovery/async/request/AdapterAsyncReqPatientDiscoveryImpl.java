/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.adapter.patientdiscovery.async.request;

import gov.hhs.fha.nhinc.adapter.patientdiscovery.async.request.proxy.AdapterPatientDiscoveryAsyncReqProxy;
import gov.hhs.fha.nhinc.adapter.patientdiscovery.async.request.proxy.AdapterPatientDiscoveryAsyncReqProxyObjectFactory;
import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import javax.xml.ws.WebServiceContext;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;

/**
 *
 * @author JHOPPESC
 */
public class AdapterAsyncReqPatientDiscoveryImpl {
    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(AdapterAsyncReqPatientDiscoveryImpl.class);

    public MCCIIN000002UV01 processPatientDiscoveryAsyncReq(RespondingGatewayPRPAIN201305UV02RequestType request, WebServiceContext context) {
        MCCIIN000002UV01 ack = new MCCIIN000002UV01();

        // Extract the message id value from the WS-Addressing Header and place it in the Assertion Class
        if (request != null &&
                request.getAssertion() != null) {
            AsyncMessageIdExtractor msgIdExtractor = new AsyncMessageIdExtractor();
            request.getAssertion().setMessageId(msgIdExtractor.GetAsyncMessageId(context));
        }
        
        AdapterPatientDiscoveryAsyncReqProxyObjectFactory adapterPatDiscAsyncReqFactory = new AdapterPatientDiscoveryAsyncReqProxyObjectFactory();

        AdapterPatientDiscoveryAsyncReqProxy proxy = adapterPatDiscAsyncReqFactory.getAdapterPatientDiscoveryAsyncReqProxy();

        ack = proxy.processPatientDiscoveryAsyncReq(request);

        return ack;
    }
}
