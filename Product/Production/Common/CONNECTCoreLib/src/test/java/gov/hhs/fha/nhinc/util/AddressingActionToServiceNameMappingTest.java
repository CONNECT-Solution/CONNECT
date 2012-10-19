/**
 * 
 */
package gov.hhs.fha.nhinc.util;

import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import org.junit.Test;

/**
 * @author mweaver
 *
 */
public class AddressingActionToServiceNameMappingTest {

    @Test
    public void negativeTest() {
        NhincConstants.NHIN_SERVICE_NAMES service = AddressingActionToServiceNameMapping.get("absent key");
        assert(service == null);
    }

    @Test
    public void ADTest() {
        NhincConstants.NHIN_SERVICE_NAMES service = AddressingActionToServiceNameMapping.get("urn:oasis:names:tc:emergency:EDXL:DE:1.0:SendAlertMessage");
        assert(service == NhincConstants.NHIN_SERVICE_NAMES.ADMINISTRATIVE_DISTRIBUTION);
    }
}
