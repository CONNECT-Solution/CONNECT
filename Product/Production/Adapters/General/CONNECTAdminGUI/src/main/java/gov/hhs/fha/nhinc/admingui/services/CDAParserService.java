
package gov.hhs.fha.nhinc.admingui.services;

import gov.hhs.fha.nhinc.admingui.event.model.PrescriptionInfo;
import java.io.InputStream;
import java.util.List;

/**
 *
 * @author jassmit
 */
public interface CDAParserService {

    /**
     * @param stream
     * @param prescriptions
     * @return
     */
    public String addMedicationSection(InputStream stream, List<PrescriptionInfo> prescriptions);

}
