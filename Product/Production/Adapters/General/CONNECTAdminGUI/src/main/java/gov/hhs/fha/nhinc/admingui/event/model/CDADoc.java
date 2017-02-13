/**
 *
 */
package gov.hhs.fha.nhinc.admingui.event.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mpnguyen
 *
 */
public class CDADoc {

    private List<PrescriptionInfo> medications;

    /**
     * @return the medications
     */
    public List<PrescriptionInfo> getMedications() {
        if (medications == null) {
            medications = new ArrayList<PrescriptionInfo>();
        }
        return medications;
    }

    /**
     * @param medications the medications to set
     */
    public void setMedications(List<PrescriptionInfo> medications) {
        this.medications = medications;
    }

}