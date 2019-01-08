/*
 * Copyright (c) 2009-2019, United States Government, as represented by the Secretary of Health and Human Services.
 * All rights reserved.
 *  
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above
 *       copyright notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the documentation
 *       and/or other materials provided with the distribution.
 *     * Neither the name of the United States Government nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package gov.hhs.fha.nhinc.util.format;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 *
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

        catch (Throwable t)

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

        catch (Throwable t)

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

        catch (Throwable t)

        {

            t.printStackTrace();

            fail("Failed to parse patient id: " + t.getMessage());

        }

    }

}
