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
package gov.hhs.fha.nhinc.direct.addressparsing;

import gov.hhs.fha.nhinc.direct.DirectException;
import javax.mail.internet.InternetAddress;
import static org.junit.Assert.*;
import org.junit.Test;
import org.nhindirect.xd.common.DirectDocuments;

/**
 *
 * @author svalluripalli
 */
public class DefaultFromAddresParserTest {

    /**
     * Test of parse method, of class DefaultFromAddresParser.
     */
    @Test
    public void testParse_Positive1() {
        String addresses = "test@connectopensource.org";
        DirectDocuments oDocuments = null;
        DefaultFromAddresParser instance = new DefaultFromAddresParser();
        InternetAddress result = (InternetAddress) instance.parse(addresses, oDocuments);
        assertEquals(addresses, result.getAddress());
    }

    @Test
    public void testParse_Positive2() {
        String addresses = null;
        DefaultFromAddresParser instance = new DefaultFromAddresParser();
        DirectDocuments oDocuments = new DirectDocuments();
        DirectDocuments.SubmissionSet oSubmissionSet = new DirectDocuments.SubmissionSet();
        oSubmissionSet.setAuthorTelecommunication("^^^test@connectopensource.org");
        oDocuments.setSubmissionSet(oSubmissionSet);
        InternetAddress result = (InternetAddress) instance.parse(addresses, oDocuments);
        assertEquals("test@connectopensource.org", result.getAddress());
    }

    @Test
    public void testParse_Negative() throws Exception {
        String addresses = null;
        InternetAddress result = null;
        DirectDocuments oDirectDocuments = null;
        DefaultFromAddresParser instance = new DefaultFromAddresParser();
        try {
            result = (InternetAddress) instance.parse(addresses, oDirectDocuments);
        } catch (DirectException exp) {
            assertNotNull(exp.getMessage());
        }
        assertNull(result);
    }
}
