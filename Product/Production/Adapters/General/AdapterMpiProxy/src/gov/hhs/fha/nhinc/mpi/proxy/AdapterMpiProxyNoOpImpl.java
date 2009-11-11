/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.mpi.proxy;

import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;

/**
 *
 * @author jhoppesc
 */
public class AdapterMpiProxyNoOpImpl implements AdapterMpiProxy {

    public PRPAIN201306UV02 findCandidates(PRPAIN201305UV02 findCandidatesRequest) {
        return new PRPAIN201306UV02();
    }
    public PRPAIN201306UV02 findCandidates(PRPAIN201305UV02 findCandidatesRequest, AssertionType assertion)
    {
        return new PRPAIN201306UV02();
    }
}
