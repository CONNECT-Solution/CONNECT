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
package gov.hhs.fha.nhinc.auditrepository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.hl7.v3.II;
import org.hl7.v3.MCCIIN000002UV01;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.transform.subdisc.HL7AckTransforms;

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
    public void testLogNhinPatientDiscAck() {
        System.out.println("testLogNhinPatientDiscAckServiceEnabled");

        AuditRepositoryLogger instance = new AuditRepositoryLogger();

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

}