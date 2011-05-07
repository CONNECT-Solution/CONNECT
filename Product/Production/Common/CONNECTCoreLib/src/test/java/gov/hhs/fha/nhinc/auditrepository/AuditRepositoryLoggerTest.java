package gov.hhs.fha.nhinc.auditrepository;

import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.transform.subdisc.HL7AckTransforms;
import org.hl7.v3.II;
import org.hl7.v3.MCCIIN000002UV01;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author JHOPPESC
 */
@Ignore
public class AuditRepositoryLoggerTest {

    // TODO: Tests reference other dependencies - move to integration test suite

    public AuditRepositoryLoggerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }



    /**
     * Test of logNhinPatientDiscAck method, of class AuditRepositoryLogger.
     */
    @Test
    public void testLogNhinPatientDiscAckServiceEnabled() {
        System.out.println("testLogNhinPatientDiscAckServiceEnabled");

        AuditRepositoryLogger instance = new AuditRepositoryLogger() {
            @Override
            protected boolean isServiceEnabled() {
                return true;
            }
        };

        II msgId = new II();
        msgId.setExtension("12345");
        msgId.setRoot("2.2");
        MCCIIN000002UV01 message = HL7AckTransforms.createAckMessage("1.1.1", msgId, "CA", "Success", "1.1", "2.2");
        AssertionType assertion = null;
        String direction = NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION;
        String _interface = NhincConstants.AUDIT_LOG_NHIN_INTERFACE;

        LogEventRequestType result = instance.logNhinPatientDiscAck(message, assertion, direction, _interface);

        assertNotNull(result);
        assertEquals(NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, result.getDirection());
        assertEquals(NhincConstants.AUDIT_LOG_NHIN_INTERFACE, result.getInterface());
        assertNotNull(result.getAuditMessage());
    }

    /**
     * Test of logNhinPatientDiscAck method, of class AuditRepositoryLogger.
     */
    @Test
    public void testLogNhinPatientDiscAckServiceDisabled() {
        System.out.println("testLogNhinPatientDiscAckServiceDisabled");

        AuditRepositoryLogger instance = new AuditRepositoryLogger() {
            @Override
            protected boolean isServiceEnabled() {
                return false;
            }
        };

        II msgId = new II();
        msgId.setExtension("12345");
        msgId.setRoot("2.2");
        MCCIIN000002UV01 message = HL7AckTransforms.createAckMessage("1.1.1", msgId, "CA", "Success", "1.1", "2.2");
        AssertionType assertion = null;
        String direction = NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION;
        String _interface = NhincConstants.AUDIT_LOG_NHIN_INTERFACE;

        LogEventRequestType result = instance.logNhinPatientDiscAck(message, assertion, direction, _interface);

        assertNull(result);
    }

}