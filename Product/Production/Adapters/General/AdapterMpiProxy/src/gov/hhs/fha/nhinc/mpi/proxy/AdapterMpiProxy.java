/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.mpi.proxy;

import org.hl7.v3.PRPAIN201305UV;
import org.hl7.v3.PRPAIN201306UV;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
/**
 *
 * @author jhoppesc
 */
public interface AdapterMpiProxy {
    public PRPAIN201306UV findCandidates(PRPAIN201305UV findCandidatesRequest);
    public PRPAIN201306UV findCandidates(PRPAIN201305UV findCandidatesRequest, AssertionType assertion);

}
