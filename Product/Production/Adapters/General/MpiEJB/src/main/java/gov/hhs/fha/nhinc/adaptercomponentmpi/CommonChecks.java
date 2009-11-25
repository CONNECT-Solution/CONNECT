/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.adaptercomponentmpi;

import gov.hhs.fha.nhinc.mpilib.Patients;

/**
 *
 * @author rayj
 */
public class CommonChecks {

    public static boolean isSingleSearchResult(Patients patients) {
        return ((patients != null) && (patients.size() == 1));
    }

    public static boolean isMultipleSearchResult(Patients patients) {
        return ((patients != null) && (patients.size() > 1));
    }

    public static boolean isZeroSearchResult(Patients patients) {
        return ((patients == null) || (patients.size() == 0));
    }

}
