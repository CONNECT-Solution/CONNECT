/**
 *
 */
package gov.hhs.fha.nhinc.mpi.adapter.component;

import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;

/**
 * @author bhumphrey
 *
 */
public class AdapterComponentMpiCheckerFaultImpl implements AdapterComponentMpiChecker {
    @Override
    public PRPAIN201306UV02 findPatient(PRPAIN201305UV02 query) {

        runtimeError("[TEST ERROR]");
        return null;
    }

    @Override
    public boolean isNhinRequiredParamsFound(PRPAIN201305UV02 query) {
        runtimeError("[TEST ERROR]");
        return false;
    }

    /**
     * @param message the runtime error message
     */
    public void runtimeError(String message) {

        throw new RuntimeException(message);
    }

}
