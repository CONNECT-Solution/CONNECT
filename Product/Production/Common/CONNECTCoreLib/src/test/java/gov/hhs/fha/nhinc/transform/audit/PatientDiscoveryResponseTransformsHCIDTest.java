/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2009-2013, United States Government, as represented by the Secretary of Health and Human Services.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above
 *      copyright notice, this list of conditions and the following disclaimer.
 *    * Redistributions in binary form must reproduce the above copyright
 *      notice, this list of conditions and the following disclaimer in the documentation
 *      and/or other materials provided with the distribution.
 *    * Neither the name of the United States Government nor the
 *      names of its contributors may be used to endorse or promote products
 *      derived from this software without specific prior written permission.
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
package gov.hhs.fha.nhinc.transform.audit;

import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import junit.framework.Assert;

import org.apache.commons.lang.StringUtils;
import org.hl7.v3.PRPAIN201306UV02;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * @author msw
 * 
 */
public class PatientDiscoveryResponseTransformsHCIDTest extends PatientDiscoveryTransformsBase {

    /**
     * Test method for
     * {@link gov.hhs.fha.nhinc.transform.audit.PatientDiscoveryTransforms#getPatientDiscoveryMessageCommunityId(org.hl7.v3.PRPAIN201305UV02, java.lang.String, java.lang.String, java.lang.String, java.lang.String)}
     * .
     */
    @Test
    public void testGetPatientDiscoveryMessageCommunityIdPRPAIN201305UV02AdapterInbound() {
        // Setup Inputs
        PRPAIN201306UV02 message = Mockito.mock(PRPAIN201306UV02.class);
        String direction = NhincConstants.AUDIT_LOG_INBOUND_DIRECTION;
        String _interface = NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE;
        String type = StringUtils.EMPTY;

        // set up test params
        RECEIVER_VALUE = NON_TRUSTWORTHY_VALUE;
        SENDER_VALUE = NON_TRUSTWORTHY_VALUE;

        // expected result
        String expected = LOCAL_HCID;

        PatientDiscoveryTransforms instance = getPatientDiscoveryTransforms();
        String result = instance.getPatientDiscoveryMessageCommunityId(message, direction, _interface, type);
        Assert.assertEquals(expected, result);
    }

    /**
     * Test method for
     * {@link gov.hhs.fha.nhinc.transform.audit.PatientDiscoveryTransforms#getPatientDiscoveryMessageCommunityId(org.hl7.v3.PRPAIN201305UV02, java.lang.String, java.lang.String, java.lang.String, java.lang.String)}
     * .
     */
    @Test
    public void testGetPatientDiscoveryMessageCommunityIdPRPAIN201305UV02AdapterOutbound() {
        // Setup Inputs
        PRPAIN201306UV02 message = Mockito.mock(PRPAIN201306UV02.class);
        String direction = NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION;
        String _interface = NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE;
        String type = StringUtils.EMPTY;

        // set up test params
        RECEIVER_VALUE = NON_TRUSTWORTHY_VALUE;
        SENDER_VALUE = NON_TRUSTWORTHY_VALUE;

        // expected result
        String expected = LOCAL_HCID;

        PatientDiscoveryTransforms instance = getPatientDiscoveryTransforms();
        String result = instance.getPatientDiscoveryMessageCommunityId(message, direction, _interface, type);
        Assert.assertEquals(expected, result);
    }

    /**
     * Test method for
     * {@link gov.hhs.fha.nhinc.transform.audit.PatientDiscoveryTransforms#getPatientDiscoveryMessageCommunityId(org.hl7.v3.PRPAIN201305UV02, java.lang.String, java.lang.String, java.lang.String, java.lang.String)}
     * .
     */
    @Test
    public void testGetPatientDiscoveryMessageCommunityIdPRPAIN201305UV02EntityInbound() {
        PRPAIN201306UV02 message = Mockito.mock(PRPAIN201306UV02.class);
        String direction = NhincConstants.AUDIT_LOG_INBOUND_DIRECTION;
        String _interface = NhincConstants.AUDIT_LOG_ENTITY_INTERFACE;
        String type = StringUtils.EMPTY;

        // set up test params
        RECEIVER_VALUE = NON_TRUSTWORTHY_VALUE;
        SENDER_VALUE = NON_TRUSTWORTHY_VALUE;

        // expected result
        String expected = LOCAL_HCID;

        PatientDiscoveryTransforms instance = getPatientDiscoveryTransforms();
        String result = instance.getPatientDiscoveryMessageCommunityId(message, direction, _interface, type);
        Assert.assertEquals(expected, result);
    }

    /**
     * Test method for
     * {@link gov.hhs.fha.nhinc.transform.audit.PatientDiscoveryTransforms#getPatientDiscoveryMessageCommunityId(org.hl7.v3.PRPAIN201305UV02, java.lang.String, java.lang.String, java.lang.String, java.lang.String)}
     * .
     */
    @Test
    public void testGetPatientDiscoveryMessageCommunityIdPRPAIN201305UV02EntityOutbound() {
        PRPAIN201306UV02 message = Mockito.mock(PRPAIN201306UV02.class);
        String direction = NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION;
        String _interface = NhincConstants.AUDIT_LOG_ENTITY_INTERFACE;
        String type = StringUtils.EMPTY;

        // set up test params
        RECEIVER_VALUE = NON_TRUSTWORTHY_VALUE;
        SENDER_VALUE = NON_TRUSTWORTHY_VALUE;

        // expected result
        String expected = LOCAL_HCID;

        PatientDiscoveryTransforms instance = getPatientDiscoveryTransforms();
        String result = instance.getPatientDiscoveryMessageCommunityId(message, direction, _interface, type);
        Assert.assertEquals(expected, result);
    }

    /**
     * Test method for
     * {@link gov.hhs.fha.nhinc.transform.audit.PatientDiscoveryTransforms#getPatientDiscoveryMessageCommunityId(org.hl7.v3.PRPAIN201305UV02, java.lang.String, java.lang.String, java.lang.String, java.lang.String)}
     * .
     */
    @Test
    public void testGetPatientDiscoveryMessageCommunityIdPRPAIN201305UV02NhinInbound() {
        PRPAIN201306UV02 message = Mockito.mock(PRPAIN201306UV02.class);
        String direction = NhincConstants.AUDIT_LOG_INBOUND_DIRECTION;
        String _interface = NhincConstants.AUDIT_LOG_NHIN_INTERFACE;
        String type = StringUtils.EMPTY;

        // set up test params
        RECEIVER_VALUE = LOCAL_HCID;
        SENDER_VALUE = REMOTE_HCID;

        // expected result
        String expected = REMOTE_HCID;

        PatientDiscoveryTransforms instance = getPatientDiscoveryTransforms();
        String result = instance.getPatientDiscoveryMessageCommunityId(message, direction, _interface, type);
        Assert.assertEquals(expected, result);
    }

    /**
     * Test method for
     * {@link gov.hhs.fha.nhinc.transform.audit.PatientDiscoveryTransforms#getPatientDiscoveryMessageCommunityId(org.hl7.v3.PRPAIN201305UV02, java.lang.String, java.lang.String, java.lang.String, java.lang.String)}
     * .
     */
    @Test
    public void testGetPatientDiscoveryMessageCommunityIdPRPAIN201305UV02NhinOutbound() {
        PRPAIN201306UV02 message = Mockito.mock(PRPAIN201306UV02.class);
        String direction = NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION;
        String _interface = NhincConstants.AUDIT_LOG_NHIN_INTERFACE;
        String type = StringUtils.EMPTY;

        // set up test params
        RECEIVER_VALUE = REMOTE_HCID;
        SENDER_VALUE = LOCAL_HCID;

        // expected result
        String expected = REMOTE_HCID;

        PatientDiscoveryTransforms instance = getPatientDiscoveryTransforms();
        String result = instance.getPatientDiscoveryMessageCommunityId(message, direction, _interface, type);
        Assert.assertEquals(expected, result);
    }

}
