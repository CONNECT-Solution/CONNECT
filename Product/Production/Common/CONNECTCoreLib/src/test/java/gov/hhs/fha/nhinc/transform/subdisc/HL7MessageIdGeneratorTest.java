/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
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