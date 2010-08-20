/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientdiscovery.adapter.deferred.request.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201305UV02;

/**
 *
 * @author JHOPPESC
 */
public class AdapterPatientDiscoveryDeferredReqProxyNoOpImpl implements AdapterPatientDiscoveryDeferredReqProxy {

    public MCCIIN000002UV01 processPatientDiscoveryAsyncReq(PRPAIN201305UV02 request, AssertionType assertion) {
        return new MCCIIN000002UV01();
    }

}
