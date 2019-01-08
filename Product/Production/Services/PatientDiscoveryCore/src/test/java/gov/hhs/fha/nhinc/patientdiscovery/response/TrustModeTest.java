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
package gov.hhs.fha.nhinc.patientdiscovery.response;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import org.hl7.v3.II;
import org.hl7.v3.PRPAIN201301UV02;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author mastan.ketha
 */
public class TrustModeTest {

    public TrustModeTest() {
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
     * Test of processResponse method, of class TrustMode.
     */
    @Test
    public void testProcessResponse_ResponseParams() {
        System.out.println("processResponse");
        ResponseParams params = null;
        TrustMode instance = new TrustMode();
        PRPAIN201306UV02 expResult = null;
        PRPAIN201306UV02 result = instance.processResponse(params);
        assertEquals(expResult, result);
    }

    /**
     * Test of processResponse method, of class TrustMode.
     */
    @Test
    public void testProcessResponse_3args() {
        System.out.println("processResponse");
        PRPAIN201306UV02 response = null;
        AssertionType assertion = null;
        II localPatId = null;
        TrustMode instance = new TrustMode();
        PRPAIN201306UV02 expResult = null;
        PRPAIN201306UV02 result = instance.processResponse(response, assertion, localPatId);
        assertEquals(expResult, result);
    }

    /**
     * Test of sendToPatientCorrelationComponent method, of class TrustMode.
     */
    @Test
    public void testSendToPatientCorrelationComponent() {
        System.out.println("sendToPatientCorrelationComponent");
        II localPatId = null;
        II remotePatId = null;
        AssertionType assertion = null;
        PRPAIN201306UV02 response = null;
        TrustMode instance = new TrustMode();
        instance.sendToPatientCorrelationComponent(localPatId, remotePatId, assertion, response);
    }

    /**
     * Test of requestHasLivingSubjectId method, of class TrustMode.
     */
    @Test
    public void testRequestHasLivingSubjectId() {
        System.out.println("requestHasLivingSubjectId");
        PRPAIN201305UV02 request = null;
        TrustMode instance = new TrustMode();
        boolean expResult = false;
        boolean result = instance.requestHasLivingSubjectId(request);
        assertEquals(expResult, result);
    }

    /**
     * Test of getPatientId method, of class TrustMode.
     */
    @Test
    public void testGetPatientId_PRPAIN201305UV02() {
        System.out.println("getPatientId");
        PRPAIN201305UV02 request = null;
        TrustMode instance = new TrustMode();
        II expResult = null;
        II result = instance.getPatientId(request);
        assertEquals(expResult, result);
    }

    /**
     * Test of getPatientId method, of class TrustMode.
     */
    @Test
    public void testGetPatientId_PRPAIN201306UV02() {
        System.out.println("getPatientId");
        PRPAIN201306UV02 request = null;
        TrustMode instance = new TrustMode();
        II expResult = null;
        II result = instance.getPatientId(request);
        assertEquals(expResult, result);
    }

    /**
     * Test of createPRPA201301 method, of class TrustMode.
     */
    @Test
    public void testCreatePRPA201301() {
        System.out.println("createPRPA201301");
        PRPAIN201306UV02 input = null;
        TrustMode instance = new TrustMode();
        PRPAIN201301UV02 expResult = null;
        PRPAIN201301UV02 result = instance.createPRPA201301(input);
        assertEquals(expResult, result);
    }

}
