package gov.hhs.fha.nhinc.mpi.adapter.proxy;

import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;

/**
 * NoOp Implementation for the AdapterMpi component proxy.
 *
 * @author Les Westberg
 */
public class AdapterMpiProxyNoOpImpl implements AdapterMpiProxy
{
    /**
     * Find the matching candidates from the MPI.
     *
     * @param request The information to use for matching.
     * @param assertion The assertion data.
     * @return The matches that are found.
     */
    public PRPAIN201306UV02 findCandidates(PRPAIN201305UV02 findCandidatesRequest, AssertionType assertion)
    {
        return new PRPAIN201306UV02();
    }
}
