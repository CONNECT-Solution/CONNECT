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

import static org.junit.Assert.*;

import org.hl7.v3.MCCIMT000100UV01Device;

import org.hl7.v3.MCCIMT000100UV01Receiver;

import org.hl7.v3.MCCIMT000300UV01Device;

import org.hl7.v3.MCCIMT000300UV01Receiver;

import org.hl7.v3.PRPAIN201301UV02;

import org.hl7.v3.PRPAIN201306UV02;

import org.junit.Test;

/**
 * 
 * @author Konstantin Shtabnoy
 * 
 * 
 */

public class HL7ArrayTransformerTest {

    @Test
    public void testCopyMCCIMT000100UV01Receiver() {

        PRPAIN201306UV02 from = new PRPAIN201306UV02();

        PRPAIN201301UV02 to = new PRPAIN201301UV02();

        MCCIMT000300UV01Receiver r = new MCCIMT000300UV01Receiver();

        MCCIMT000300UV01Device d = new MCCIMT000300UV01Device();

        r.setDevice(d);

        from.getReceiver().add(r);

        try {

            HL7ArrayTransforms.copyMCCIMT000100UV01Receiver(from, to);

            assertNotNull(to.getReceiver());

            assertEquals(from.getReceiver().size(), to.getReceiver().size());

            assertEquals(to.getReceiver().size(), 1);

            MCCIMT000100UV01Receiver toReceiver = to.getReceiver().get(0);

            assertNotNull(toReceiver);

            assertEquals(toReceiver.getTelecom(), r.getTelecom());

            assertEquals(toReceiver.getTypeCode(), r.getTypeCode());

            assertEquals(toReceiver.getTypeId(), r.getTypeId());

            MCCIMT000100UV01Device toDevice = toReceiver.getDevice();

            assertNotNull(toDevice);

            assertEquals(toDevice.getDesc(), d.getDesc());

            assertEquals(toDevice.getClassCode(), d.getClassCode());

            assertEquals(toDevice.getDeterminerCode(), d.getDeterminerCode());

            assertEquals(toDevice.getExistenceTime(), d.getExistenceTime());

            assertEquals(toDevice.getManufacturerModelName(), d.getManufacturerModelName());

            assertEquals(toDevice.getSoftwareName(), d.getSoftwareName());

            assertEquals(toDevice.getTypeId(), d.getTypeId());

            assertEquals(toDevice.getAsAgent().getValue().getRepresentedOrganization().getValue().getId().size(), 0);

        } catch (NullPointerException ex) {

            fail("NPE during copying indicates an attempt to copy optional elements.");

        }

    }

}
