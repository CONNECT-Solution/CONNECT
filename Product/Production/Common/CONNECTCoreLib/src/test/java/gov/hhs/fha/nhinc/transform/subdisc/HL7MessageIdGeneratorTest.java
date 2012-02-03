/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
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
package gov.hhs.fha.nhinc.transform.subdisc;

import gov.hhs.fha.nhinc.transform.subdisc.HL7MessageIdGenerator;
import org.hl7.v3.II;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Jon Hoppesch
 */
public class HL7MessageIdGeneratorTest {

    private static Log log = LogFactory.getLog(HL7MessageIdGeneratorTest.class);

    public HL7MessageIdGeneratorTest() {
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
     * Test of GenerateHL7MessageId method, of class HL7MessageIdGenerator.
     * device id
     */
    @Test
    public void testGenerateHL7MessageId_1arg() {
        log.info("testGenerateHL7MessageId_1arg");

        String myDeviceId = "2.16.840.1.113883.3.166.4";

        II result = HL7MessageIdGenerator.GenerateHL7MessageId(myDeviceId);
        assertEquals(result.getRoot(), "2.16.840.1.113883.3.166.4");
        assertNotNull(result.getExtension());
    }

        /**
     * Test of GenerateHL7MessageId method, of class HL7MessageIdGenerator.
     * device id, no input
     */
    @Test
    public void testGenerateHL7MessageId_1arg_NoInput() {
        log.info("testGenerateHL7MessageId_1arg");

        II result = HL7MessageIdGenerator.GenerateHL7MessageId(null);
        assertEquals(result.getRoot(), result.getRoot());
        assertNotNull(result.getExtension());
    }

    /**
     * Test of GenerateHL7MessageId method, of class HL7MessageIdGenerator.
     */
    @Test
    public void testGenerateHL7MessageId_0args() {
        log.info("testGenerateHL7MessageId_0args");

        II result = HL7MessageIdGenerator.GenerateHL7MessageId();
        assertEquals(result.getRoot(), result.getRoot());
        assertNotNull(result.getExtension());
    }

    /**
     * Test of GenerateMessageId method, of class HL7MessageIdGenerator.
     */
    @Test
    public void testGenerateMessageId() {
        log.info("testGenerateMessageId");

        String result = HL7MessageIdGenerator.GenerateMessageId();
        assertNotNull(result);
    }
}