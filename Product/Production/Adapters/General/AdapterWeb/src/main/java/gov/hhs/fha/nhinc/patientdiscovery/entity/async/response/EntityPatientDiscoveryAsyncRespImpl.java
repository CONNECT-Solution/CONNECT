/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientdiscovery.entity.async.response;

import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import gov.hhs.fha.nhinc.entity.patientdiscovery.async.response.proxy.EntityPatientDiscoveryAsyncRespProxy;
import gov.hhs.fha.nhinc.entity.patientdiscovery.async.response.proxy.EntityPatientDiscoveryAsyncRespProxyObjectFactory;
import javax.xml.ws.WebServiceContext;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02RequestType;

/**
 *
 * @author JHOPPESC
 */
public class EntityPatientDiscoveryAsyncRespImpl {
    public MCCIIN000002UV01 processPatientDiscoveryAsyncResp(RespondingGatewayPRPAIN201306UV02RequestType request, WebServiceContext context) {
        MCCIIN000002UV01 ack = new MCCIIN000002UV01();

        // Extract the message id value from the WS-Addressing Header and place it in the Assertion Class
        if (request != null &&
                request.getAssertion() != null) {
            AsyncMessageIdExtractor msgIdExtractor = new AsyncMessageIdExtractor();
            request.getAssertion().setAsyncMessageId(msgIdExtractor.GetAsyncRelatesTo(context));
        }

        EntityPatientDiscoveryAsyncRespProxyObjectFactory entityPatDiscAsyncReqFactory = new EntityPatientDiscoveryAsyncRespProxyObjectFactory();

        EntityPatientDiscoveryAsyncRespProxy proxy = entityPatDiscAsyncReqFactory.getEntityPatientDiscoveryAsyncRespProxy();

        ack = proxy.processPatientDiscoveryAsyncResp(request);

        return ack;
    }

}
