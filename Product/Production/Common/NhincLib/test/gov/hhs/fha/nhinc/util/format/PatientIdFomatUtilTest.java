/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.util.format;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author webbn
 */
public class PatientIdFomatUtilTest {

    @Test
    public void testPatientIdParse()
    {
        try
        {
            String toParse = "1234";
            String expected = "1234";
            
            String parsed = PatientIdFormatUtil.parsePatientId(toParse);
            assertNotNull("Parsed - no HL7 was null", parsed);
            assertEquals("Parsed - no HL7  was incorrect", expected, parsed);
            
            toParse = "4567^^^&2.16.840.1.113883.3.166.1.1&ISO";
            expected = "4567";
            
            parsed = PatientIdFormatUtil.parsePatientId(toParse);
            assertNotNull("Parsed - HL7 was null", parsed);
            assertEquals("Parsed - HL7  was incorrect", expected, parsed);
        }
        catch(Throwable t)
        {
            t.printStackTrace();
            fail("Failed to parse patient id: " + t.getMessage());
        }
    }
    
    @Test
    public void testCommunityIdParse()
    {
        try
        {
            String toParse = "4567^^^&2.16.840.1.113883.3.166.1.1&ISO";
            String expectedHomeCommunity = "2.16.840.1.113883.3.166.1.1";
            
            String parsedHomeCommunity = PatientIdFormatUtil.parseCommunityId(toParse);
            assertNotNull("Parsed home community was null", parsedHomeCommunity);
            assertEquals("Parsed home community  was incorrect", expectedHomeCommunity, parsedHomeCommunity);
            
            toParse = "4567^^^";
            parsedHomeCommunity = PatientIdFormatUtil.parseCommunityId(toParse);
            assertNull("Parsed home community was not null when invalid", parsedHomeCommunity);
            
            toParse = "4567";
            parsedHomeCommunity = PatientIdFormatUtil.parseCommunityId(toParse);
            assertNull("Parsed home community was not null when not HL7 formatted", parsedHomeCommunity);
            
            toParse = null;
            parsedHomeCommunity = PatientIdFormatUtil.parseCommunityId(toParse);
            assertNull("Parsed home community was not null when id was null", parsedHomeCommunity);
            
        }
        catch(Throwable t)
        {
            t.printStackTrace();
            fail("Failed to parse community id: " + t.getMessage());
        }
    }

    @Test
    public void testPatientIdFormat()
    {
        try
        {
            String patientId = "1234";
            String homeCommunityId = "2.16.840.1.113883.3.166.1.1";
            String expected = "'1234^^^&2.16.840.1.113883.3.166.1.1&ISO'";
            
            String formatted = PatientIdFormatUtil.hl7EncodePatientId(patientId, homeCommunityId);
            assertNotNull("Formatted - id was null", formatted);
            assertEquals("Formatted - id was incorrect", expected, formatted);
            
        }
        catch(Throwable t)
        {
            t.printStackTrace();
            fail("Failed to parse patient id: " + t.getMessage());
        }
    }

}