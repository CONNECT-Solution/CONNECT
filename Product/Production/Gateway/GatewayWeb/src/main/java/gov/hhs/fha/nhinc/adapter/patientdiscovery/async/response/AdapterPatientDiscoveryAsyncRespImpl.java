/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.adapter.patientdiscovery.async.response;

import gov.hhs.fha.nhinc.adapter.patientdiscovery.async.response.proxy.AdapterPatientDiscoveryAsyncRespProxy;
import gov.hhs.fha.nhinc.adapter.patientdiscovery.async.response.proxy.AdapterPatientDiscoveryAsyncRespProxyObjectFactory;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02RequestType;

/**
 *
 * @author JHOPPESC
 */
public class AdapterPatientDiscoveryAsyncRespImpl {
    public MCCIIN000002UV01 processPatientDiscoveryAsyncResp(RespondingGatewayPRPAIN201306UV02RequestType request) {
        MCCIIN000002UV01 ack = new MCCIIN000002UV01();

        AdapterPatientDiscoveryAsyncRespProxyObjectFactory adapterPatDiscAsyncRespFactory = new AdapterPatientDiscoveryAsyncRespProxyObjectFactory();

        AdapterPatientDiscoveryAsyncRespProxy proxy = adapterPatDiscAsyncRespFactory.getAdapterPatientDiscoveryAsyncRespProxy();

        ack = proxy.processPatientDiscoveryAsyncResp(request);

        return ack;
    }

}
