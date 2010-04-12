/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.adapter.patientdiscovery.async.request.error;

import gov.hhs.fha.nhinc.adapter.patientdiscovery.async.request.error.proxy.AdapterPatientDiscoveryAsyncReqErrorProxy;
import gov.hhs.fha.nhinc.adapter.patientdiscovery.async.request.error.proxy.AdapterPatientDiscoveryAsyncReqErrorProxyObjectFactory;
import org.hl7.v3.AsyncAdapterPatientDiscoveryErrorRequestType;
import org.hl7.v3.MCCIIN000002UV01;

/**
 *
 * @author jhoppesc
 */
public class AdapterPatientDiscoveryAsyncReqErrorImpl {
    public MCCIIN000002UV01 processPatientDiscoveryAsyncReqError(AsyncAdapterPatientDiscoveryErrorRequestType request) {
        MCCIIN000002UV01 ack = new MCCIIN000002UV01();

        AdapterPatientDiscoveryAsyncReqErrorProxyObjectFactory adapterPatDiscAsyncReqErrorFactory = new AdapterPatientDiscoveryAsyncReqErrorProxyObjectFactory();

        AdapterPatientDiscoveryAsyncReqErrorProxy proxy = adapterPatDiscAsyncReqErrorFactory.getAdapterPatientDiscoveryAsyncReqErrorProxy();

        ack = proxy.processPatientDiscoveryAsyncReqError(request);

        return ack;
    }

}
