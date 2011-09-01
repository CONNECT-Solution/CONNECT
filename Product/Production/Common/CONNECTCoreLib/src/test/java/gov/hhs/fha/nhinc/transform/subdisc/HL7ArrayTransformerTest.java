package gov.hhs.fha.nhinc.transform.subdisc;

import static org.junit.Assert.*;
import org.hl7.v3.MCCIMT000100UV01Device;
import org.hl7.v3.MCCIMT000100UV01Receiver;
import org.hl7.v3.MCCIMT000300UV01Device;
import org.hl7.v3.MCCIMT000300UV01Receiver;
import org.hl7.v3.PRPAIN201301UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.junit.Test;

/**
 * @author Konstantin Shtabnoy
 *
 */
public class HL7ArrayTransformerTest {
	@Test
	public void testCopyMCCIMT000100UV01Receiver() {
		PRPAIN201306UV02 from = new PRPAIN201306UV02();
		PRPAIN201301UV02 to = new PRPAIN201301UV02();
		MCCIMT000300UV01Receiver r = new MCCIMT000300UV01Receiver();
		MCCIMT000300UV01Device d = new MCCIMT000300UV01Device();
		r.setDevice(d);
		from.getReceiver().add(r);
		try {
			HL7ArrayTransforms.copyMCCIMT000100UV01Receiver(from, to);
			assertNotNull(to.getReceiver());
			assertEquals(from.getReceiver().size(), to.getReceiver().size());
			assertEquals(to.getReceiver().size(), 1);
			MCCIMT000100UV01Receiver toReceiver = to.getReceiver().get(0);
			assertNotNull(toReceiver);
			assertEquals(toReceiver.getTelecom(), r.getTelecom());
			assertEquals(toReceiver.getTypeCode(), r.getTypeCode());
			assertEquals(toReceiver.getTypeId(), r.getTypeId());
			MCCIMT000100UV01Device toDevice = toReceiver.getDevice();
			assertNotNull(toDevice);
			assertEquals(toDevice.getDesc(), d.getDesc());
			assertEquals(toDevice.getClassCode(), d.getClassCode());
			assertEquals(toDevice.getDeterminerCode(), d.getDeterminerCode());
			assertEquals(toDevice.getExistenceTime(), d.getExistenceTime());
			assertEquals(toDevice.getManufacturerModelName(), d.getManufacturerModelName());
			assertEquals(toDevice.getSoftwareName(), d.getSoftwareName());
			assertEquals(toDevice.getTypeId(), d.getTypeId());
			assertEquals(toDevice.getAsAgent().getValue().getRepresentedOrganization().getValue().getId().size(), 0);
		} catch (NullPointerException ex) {
			fail("NPE during copying indicates an attempt to copy optional elements.");
		}
	}
}


