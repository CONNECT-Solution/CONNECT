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

import org.hl7.v3.MCCIMT000100UV01Sender;
import org.hl7.v3.MCCIMT000200UV01Sender;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.CommunicationFunctionType;
import org.hl7.v3.MCCIMT000300UV01Sender;

/**
 *
 * @author jhoppesc
 */
public class HL7SenderTransformsTest {
    
    private static Log log = LogFactory.getLog(HL7SenderTransformsTest.class);

    public HL7SenderTransformsTest() {
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
     * Test of testCreateMCCIMT000200UV01Sender method, of class HL7DataTransformHelper.
     */
    @Test
    public void testCreateMCCIMT000200UV01Sender() {
        log.info("testCreateMCCIMT000200UV01Sender");
        
        String OID = "2.16.840.1.113883.3.200";
        MCCIMT000200UV01Sender result = HL7SenderTransforms.createMCCIMT000200UV01Sender(OID);
        
        assertEquals(result.getTypeCode(), CommunicationFunctionType.SND);
        assertEquals(result.getDevice().getDeterminerCode(), HL7Constants.SENDER_DETERMINER_CODE);
        assertEquals(result.getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0).getRoot(), "2.16.840.1.113883.3.200");
    }

    /**
     * Test of testCreateMCCIMT000200UV01Sender method, of class HL7DataTransformHelper
     * with no input.
     */
    @Test
    public void testCreateMCCIMT000200UV01Sender_NullInput() {
        log.info("testCreateMCCIMT000200UV01Senderr_NullInput");
        
        MCCIMT000200UV01Sender result = HL7SenderTransforms.createMCCIMT000200UV01Sender(null);
        
        assertNotNull(result);
    }
    
    /**
     * Test of testCreateMCCIMT000100UV01Sender method, of class HL7DataTransformHelper.
     */
    @Test
    public void testCreateMCCIMT000100UV01Sender() {
        log.info("testCreateMCCIMT000100UV01Sender");
        
        String OID = "2.16.840.1.113883.3.166.4";
        MCCIMT000100UV01Sender result = HL7SenderTransforms.createMCCIMT000100UV01Sender(OID);
        
        assertEquals(result.getTypeCode(), CommunicationFunctionType.SND);
        assertEquals(result.getDevice().getDeterminerCode(), HL7Constants.SENDER_DETERMINER_CODE);
        assertEquals(result.getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0).getRoot(), "2.16.840.1.113883.3.166.4");
    }

    /**
     * Test of testCreateMCCIMT000100UV01Sender method, of class HL7DataTransformHelper
     * with no input.
     */
    @Test
    public void testCreateMCCIMT000100UV01Sender_NoInput() {
        log.info("testCreateMCCIMT000100UV01Sender_NoInput");
        
        MCCIMT000100UV01Sender result = HL7SenderTransforms.createMCCIMT000100UV01Sender(null);
        
        assertNotNull(result);
    }

    /**
     * Test of testCreateMCCIMT000300UV01Sender method, of class HL7DataTransformHelper.
     */
    @Test
    public void testCreateMCCIMT000300UV01Sender() {
        log.info("testCreateMCCIMT000300UV01Sender");

        String OID = "2.16.840.1.113883.3.200";
        MCCIMT000300UV01Sender result = HL7SenderTransforms.createMCCIMT000300UV01Sender(OID);

        assertEquals(result.getTypeCode(), CommunicationFunctionType.SND);
        assertEquals(result.getDevice().getDeterminerCode(), HL7Constants.SENDER_DETERMINER_CODE);
        assertEquals(result.getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0).getRoot(), "2.16.840.1.113883.3.200");
    }

    /**
     * Test of testCreateMCCIMT000300UV01Sender method, of class HL7DataTransformHelper
     * with no input.
     */
    @Test
    public void testCreateMCCIMT000300UV01Sender_NoInput() {
        log.info("testCreateMCCIMT000300UV01Sender_NoInput");

        MCCIMT000300UV01Sender result = HL7SenderTransforms.createMCCIMT000300UV01Sender(null);

        assertNotNull(result);
    }
   
}