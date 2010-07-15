/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.adapter.patientdiscovery.async.request.error.proxy;

import org.hl7.v3.AsyncAdapterPatientDiscoveryErrorRequestType;
import org.hl7.v3.MCCIIN000002UV01;

/**
 *
 * @author jhoppesc
 */
public class AdapterPatientDiscoveryAsyncReqErrorNoOpImpl implements AdapterPatientDiscoveryAsyncReqErrorProxy {

    public MCCIIN000002UV01 processPatientDiscoveryAsyncReqError(AsyncAdapterPatientDiscoveryErrorRequestType processPatientDiscoveryAsyncReqErrorRequest) {
        return new MCCIIN000002UV01();
    }

}
