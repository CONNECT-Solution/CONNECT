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
package gov.hhs.fha.nhinc.transform.subdisc;

import javax.xml.bind.JAXBElement;
import org.hl7.v3.CS;
import org.hl7.v3.EDExplicit;
import org.hl7.v3.ENExplicit;
import org.hl7.v3.II;
import org.hl7.v3.IVLTSExplicit;
import org.hl7.v3.MCCIMT000100UV01Agent;
import org.hl7.v3.MCCIMT000100UV01AttentionLine;
import org.hl7.v3.MCCIMT000100UV01Device;
import org.hl7.v3.MCCIMT000100UV01Organization;
import org.hl7.v3.MCCIMT000100UV01Receiver;
import org.hl7.v3.MCCIMT000300UV01Agent;
import org.hl7.v3.MCCIMT000300UV01AttentionLine;
import org.hl7.v3.MCCIMT000300UV01Device;
import org.hl7.v3.MCCIMT000300UV01Organization;
import org.hl7.v3.MCCIMT000300UV01Receiver;
import org.hl7.v3.MFMIMT700701UV01AuthorOrPerformer;
import org.hl7.v3.MFMIMT700701UV01DataEnterer;
import org.hl7.v3.MFMIMT700701UV01InformationRecipient;
import org.hl7.v3.MFMIMT700711UV01AuthorOrPerformer;
import org.hl7.v3.MFMIMT700711UV01DataEnterer;
import org.hl7.v3.MFMIMT700711UV01InformationRecipient;
import org.hl7.v3.PNExplicit;
import org.hl7.v3.PRPAIN201301UV02;
import org.hl7.v3.PRPAIN201301UV02MFMIMT700701UV01ControlActProcess;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201305UV02QUQIMT021001UV01ControlActProcess;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.PRPAIN201306UV02MFMIMT700711UV01ControlActProcess;
import org.hl7.v3.PRPAMT201301UV02Patient;
import org.hl7.v3.PRPAMT201301UV02Person;
import org.hl7.v3.PRPAMT201306UV02ParameterList;
import org.hl7.v3.PRPAMT201310UV02Patient;
import org.hl7.v3.QUQIMT021001UV01AuthorOrPerformer;
import org.hl7.v3.QUQIMT021001UV01DataEnterer;
import org.hl7.v3.SC;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * @author achidambaram
 *
 */
public class HL7ArrayTransformsTest {

    @Test
    public void testCopyNullFlavors() {
        HL7ArrayTransforms trans = new HL7ArrayTransforms();
        PRPAIN201301UV02 returnedToRequest = trans.copyNullFlavors(createPRPAIN201305UV02(), createPRPAIN201301UV02());
        assertEquals(returnedToRequest.getNullFlavor().get(0), "NA");
        assertEquals(returnedToRequest.getNullFlavor().get(1), "NATest");
    }

    @Test
    public void testCopyNullFlavorsWhenFromRequestNullFlavorNull() {
        HL7ArrayTransforms trans = new HL7ArrayTransforms();
        PRPAIN201301UV02 returnedToRequest = trans.copyNullFlavors(createPRPAIN201305UV02Null(),
                createPRPAIN201301UV02());
        assertEquals(returnedToRequest.getNullFlavor().get(0), "NA201301");
    }

    @Test
    public void testCopyNullFlavorsPRPAIN201306UV02() {
        HL7ArrayTransforms trans = new HL7ArrayTransforms();
        PRPAIN201301UV02 returnedToRequest = trans.copyNullFlavors(createPRPAIN201306UV02(), createPRPAIN201301UV02());
        assertEquals(returnedToRequest.getNullFlavor().get(0), "NA");
        assertEquals(returnedToRequest.getNullFlavor().get(1), "NATest");
    }

    @Test
    public void testCopyNullFlavorsWhenPRPAIN201306UV02Empty() {
        HL7ArrayTransforms trans = new HL7ArrayTransforms();
        PRPAIN201301UV02 returnedToRequest = trans.copyNullFlavors(createPRPAIN201306UV02Empty(),
                createPRPAIN201301UV02());
        assertEquals(returnedToRequest.getNullFlavor().get(0), "NA201301");
    }

    @Test
    public void testCopyNullFlavorsMFMIMT700701UV01InformationRecipient() {
        HL7ArrayTransforms trans = new HL7ArrayTransforms();
        MFMIMT700701UV01InformationRecipient returnedToRequest = trans.copyNullFlavors(
                createMFMIMT700711UV01InformationRecipientFromRequest(),
                createMFMIMT700701UV01InformationRecipientToRequest());
        assertEquals(returnedToRequest.getNullFlavor().get(0), "NA");
        assertEquals(returnedToRequest.getNullFlavor().get(1), "NATest");
    }

    @Test
    public void testCopyNullFlavorsMFMIMT700701UV01InformationRecipientEmpty() {
        HL7ArrayTransforms trans = new HL7ArrayTransforms();
        MFMIMT700701UV01InformationRecipient returnedToRequest = trans.copyNullFlavors(
                createMFMIMT700701UV01InformationRecipientFromRequestEmpty(),
                createMFMIMT700701UV01InformationRecipientToRequest());
        assertEquals(returnedToRequest.getNullFlavor().get(0), "NAInformationRecipient");
    }

    @Test
    public void testCopyNullFlavorsPNExplicit() {
        HL7ArrayTransforms trans = new HL7ArrayTransforms();
        ENExplicit returnedToRequest = trans
                .copyNullFlavors(createPNExplicitFromRequest(), createENExplicitToRequest());
        assertEquals(returnedToRequest.getNullFlavor().get(0), "NA");
        assertEquals(returnedToRequest.getNullFlavor().get(1), "NATest");
    }

    @Test
    public void testCopyNullFlavorsPNExplicitEmpty() {
        HL7ArrayTransforms trans = new HL7ArrayTransforms();
        ENExplicit returnedToRequest = trans.copyNullFlavors(createPNExplicitFromRequestEmpty(),
                createENExplicitToRequest());
        assertEquals(returnedToRequest.getNullFlavor().get(0), "NAPNExplicit");
    }

    @Test
    public void testCopyNullFlavorsENExplicit() {
        HL7ArrayTransforms trans = new HL7ArrayTransforms();
        PNExplicit returnedToRequest = trans
                .copyNullFlavors(createENExplicitFromRequest(), createPNExplicitToRequest());
        assertEquals(returnedToRequest.getNullFlavor().get(0), "NA");
        assertEquals(returnedToRequest.getNullFlavor().get(1), "NATest");
    }

    @Test
    public void testCopyNullFlavorsENExplicitEmpty() {
        HL7ArrayTransforms trans = new HL7ArrayTransforms();
        PNExplicit returnedToRequest = trans.copyNullFlavors(createENExplicitFromRequestEmpty(),
                createPNExplicitToRequest());
        assertEquals(returnedToRequest.getNullFlavor().get(0), "NAPNExplicit");
    }

    @Test
    public void testCopyNullFlavorsAttentionLine() {
        HL7ArrayTransforms trans = new HL7ArrayTransforms();
        MCCIMT000100UV01AttentionLine returnedToRequest = trans.copyNullFlavors(
                createMCCIMT000300UV01AttentionLineFromRequest(), createMCCIMT000100UV01AttentionLineToRequest());
        assertEquals(returnedToRequest.getNullFlavor().get(0), "NA");
        assertEquals(returnedToRequest.getNullFlavor().get(1), "NATest");
    }

    @Test
    public void testCopyNullFlavorsAttentionLineEmpty() {
        HL7ArrayTransforms trans = new HL7ArrayTransforms();
        MCCIMT000100UV01AttentionLine returnedToRequest = trans.copyNullFlavors(
                createMCCIMT000300UV01AttentionLineFromRequestEmpty(), createMCCIMT000100UV01AttentionLineToRequest());
        assertEquals(returnedToRequest.getNullFlavor().get(0), "NAAttentionLine");
    }

    @Test
    public void testCopyNullFlavorPatientPerson() {
        HL7ArrayTransforms trans = new HL7ArrayTransforms();
        PRPAMT201301UV02Person returnedToRequest = trans.copyNullFlavors(createPRPAMT201306UV02ParameterList(),
                createPRPAMT201301UV02Person());
        assertEquals(returnedToRequest.getNullFlavor().get(0), "NA");
        assertEquals(returnedToRequest.getNullFlavor().get(1), "NATest");
    }

    @Test
    public void testCopyNullFlavorPatientPersonEmpty() {
        HL7ArrayTransforms trans = new HL7ArrayTransforms();
        PRPAMT201301UV02Person returnedToRequest = trans.copyNullFlavors(createPRPAMT201306UV02ParameterListEmpty(),
                createPRPAMT201301UV02Person());
        assertEquals(returnedToRequest.getNullFlavor().get(0), "NAPatientPerson");
    }

    @Test
    public void testCopyNullFlavorsDataEnterer() {
        HL7ArrayTransforms trans = new HL7ArrayTransforms();
        MFMIMT700701UV01DataEnterer returnedToRequest = trans.copyNullFlavors(
                createMFMIMT700711UV01DataEntererFromRequest(), createMFMIMT700701UV01DataEntererToRequest());
        assertEquals(returnedToRequest.getNullFlavor().get(0), "NA");
        assertEquals(returnedToRequest.getNullFlavor().get(1), "NATest");

    }

    @Test
    public void testCopyNullFlavorsDataEntererEmpty() {
        HL7ArrayTransforms trans = new HL7ArrayTransforms();
        MFMIMT700701UV01DataEnterer returnedToRequest = trans.copyNullFlavors(
                createMFMIMT700711UV01DataEntererFromRequestEmpty(), createMFMIMT700701UV01DataEntererToRequest());
        assertEquals(returnedToRequest.getNullFlavor().get(0), "NADataEnterer");
    }

    @Test
    public void testCopyNullFlavorsQUQIMT021001UV01DataEnterer() {
        HL7ArrayTransforms trans = new HL7ArrayTransforms();
        MFMIMT700701UV01DataEnterer returnedToRequest = trans.copyNullFlavors(
                createQUQIMT021001UV01DataEntererFromRequest(), createMFMIMT700701UV01DataEntererToRequest());
        assertEquals(returnedToRequest.getNullFlavor().get(0), "NA");
        assertEquals(returnedToRequest.getNullFlavor().get(1), "NATest");
    }

    @Test
    public void testCopyNullFlavorsQUQIMT021001UV01DataEntererEmpty() {
        HL7ArrayTransforms trans = new HL7ArrayTransforms();
        MFMIMT700701UV01DataEnterer returnedToRequest = trans.copyNullFlavors(
                createQUQIMT021001UV01DataEntererFromRequestEmpty(), createMFMIMT700701UV01DataEntererToRequest());
        assertEquals(returnedToRequest.getNullFlavor().get(0), "NADataEnterer");
    }

    @Test
    public void testCopyNullFlavorsControlActProcess() {
        HL7ArrayTransforms trans = new HL7ArrayTransforms();
        PRPAIN201301UV02MFMIMT700701UV01ControlActProcess returnedToRequest = trans.copyNullFlavors(
                createPRPAIN201306UV02MFMIMT700711UV01ControlActProcess(),
                createPRPAIN201301UV02MFMIMT700701UV01ControlActProcess());
        assertEquals(returnedToRequest.getNullFlavor().get(0), "NA");
        assertEquals(returnedToRequest.getNullFlavor().get(1), "NATest");
    }

    @Test
    public void testCopyNullFlavorsControlActProcessEmpty() {
        HL7ArrayTransforms trans = new HL7ArrayTransforms();
        PRPAIN201301UV02MFMIMT700701UV01ControlActProcess returnedToRequest = trans.copyNullFlavors(
                createPRPAIN201306UV02MFMIMT700711UV01ControlActProcessEmpty(),
                createPRPAIN201301UV02MFMIMT700701UV01ControlActProcess());
        assertEquals(returnedToRequest.getNullFlavor().get(0), "NAControlActProcess");

    }

    @Test
    public void testCopyNullFlavorsQUQIMT021001UV01ControlActProcess() {
        HL7ArrayTransforms trans = new HL7ArrayTransforms();
        PRPAIN201301UV02MFMIMT700701UV01ControlActProcess returnedToRequest = trans.copyNullFlavors(
                createPRPAIN201305UV02QUQIMT021001UV01ControlActProcess(),
                createPRPAIN201301UV02MFMIMT700701UV01ControlActProcess());
        assertEquals(returnedToRequest.getNullFlavor().get(0), "NA");
        assertEquals(returnedToRequest.getNullFlavor().get(1), "NATest");
    }

    @Test
    public void testCopyNullFlavorsQUQIMT021001UV01ControlActProcessEmpty() {
        HL7ArrayTransforms trans = new HL7ArrayTransforms();
        PRPAIN201301UV02MFMIMT700701UV01ControlActProcess returnedToRequest = trans.copyNullFlavors(
                createPRPAIN201305UV02QUQIMT021001UV01ControlActProcessEmpty(),
                createPRPAIN201301UV02MFMIMT700701UV01ControlActProcess());
        assertEquals(returnedToRequest.getNullFlavor().get(0), "NAControlActProcess");
    }

    @Test
    public void testCopyNullFlavorsMFMIMT700711UV01AuthorOrPerformer() {
        HL7ArrayTransforms trans = new HL7ArrayTransforms();
        MFMIMT700701UV01AuthorOrPerformer returnedToRequest = trans.copyNullFlavors(
                createMFMIMT700711UV01AuthorOrPerformer(), createMFMIMT700701UV01AuthorOrPerformer());
        assertEquals(returnedToRequest.getNullFlavor().get(0), "NA");
        assertEquals(returnedToRequest.getNullFlavor().get(1), "NATest");
    }

    @Test
    public void testCopyNullFlavorsMFMIMT700711UV01AuthorOrPerformerEmpty() {
        HL7ArrayTransforms trans = new HL7ArrayTransforms();
        MFMIMT700701UV01AuthorOrPerformer returnedToRequest = trans.copyNullFlavors(
                createMFMIMT700711UV01AuthorOrPerformerEmpty(), createMFMIMT700701UV01AuthorOrPerformer());
        assertEquals(returnedToRequest.getNullFlavor().get(0), "NAAuthorOrPerformer");
    }

    @Test
    public void testCopyNullFlavorsAuthorOrPerformer() {
        HL7ArrayTransforms trans = new HL7ArrayTransforms();
        MFMIMT700701UV01AuthorOrPerformer returnedToRequest = trans.copyNullFlavors(
                createQUQIMT021001UV01AuthorOrPerformer(), createMFMIMT700701UV01AuthorOrPerformer());
        assertEquals(returnedToRequest.getNullFlavor().get(0), "NA");
        assertEquals(returnedToRequest.getNullFlavor().get(1), "NATest");
    }

    @Test
    public void testCopyNullFlavorsAuthorOrPerformerEmpty() {
        HL7ArrayTransforms trans = new HL7ArrayTransforms();
        MFMIMT700701UV01AuthorOrPerformer returnedToRequest = trans.copyNullFlavors(
                createQUQIMT021001UV01AuthorOrPerformerEmpty(), createMFMIMT700701UV01AuthorOrPerformer());
        assertEquals(returnedToRequest.getNullFlavor().get(0), "NAAuthorOrPerformer");
    }

    @Test
    public void copyMCCIMT000100UV01AttentionLine() {
        PRPAIN201301UV02 toRequest = null;
        HL7ArrayTransforms trans = new HL7ArrayTransforms();
        PRPAIN201301UV02 returnedToRequest = trans.copyMCCIMT000100UV01AttentionLine(
                createPRPAIN201306UV02WithAttentionLine(), toRequest);
        assertEquals(returnedToRequest.getAttentionLine().get(0).getNullFlavor().get(0), "NA");
        assertEquals(returnedToRequest.getAttentionLine().get(0).getNullFlavor().get(1), "NATest");
    }

    @Test
    public void copyMCCIMT000100UV01AttentionLineWhenToRequestNotNull() {
        HL7ArrayTransforms trans = new HL7ArrayTransforms();
        PRPAIN201301UV02 returnedToRequest = trans.copyMCCIMT000100UV01AttentionLine(
                createPRPAIN201306UV02WithAttentionLine(), createPRPAIN201301UV02());
        assertEquals(returnedToRequest.getAttentionLine().get(0).getNullFlavor().get(0), "NA");
        assertEquals(returnedToRequest.getAttentionLine().get(0).getNullFlavor().get(1), "NATest");
    }

    @Test
    public void copyMCCIMT000100UV01AttentionLineWhenAttentionLineEmpty() {
        HL7ArrayTransforms trans = new HL7ArrayTransforms();
        PRPAIN201301UV02 returnedToRequest = trans.copyMCCIMT000100UV01AttentionLine(createPRPAIN201306UV02Empty(),
                createPRPAIN201301UV02WithAttentionLine());
        assertEquals(returnedToRequest.getAttentionLine().get(0).getNullFlavor().get(0), "NAAttentionLine");
    }

    @Test
    public void copyMCCIMT000100UV01AttentionLinePRPAIN201305UV02() {
        PRPAIN201301UV02 toRequest = null;
        HL7ArrayTransforms trans = new HL7ArrayTransforms();
        PRPAIN201301UV02 returnedToRequest = trans.copyMCCIMT000100UV01AttentionLine(
                createPRPAIN201305UV02WithAttentionLine(), toRequest);
        assertEquals(returnedToRequest.getAttentionLine().get(0).getNullFlavor().get(0), "NA");
        assertEquals(returnedToRequest.getAttentionLine().get(0).getNullFlavor().get(1), "NATest");
    }

    @Test
    public void copyMCCIMT000100UV01AttentionLinePRPAIN201301UV02WhenToRequestNotNull() {
        HL7ArrayTransforms trans = new HL7ArrayTransforms();
        PRPAIN201301UV02 returnedToRequest = trans.copyMCCIMT000100UV01AttentionLine(
                createPRPAIN201305UV02WithAttentionLine(), createPRPAIN201301UV02());
        assertEquals(returnedToRequest.getAttentionLine().get(0).getNullFlavor().get(0), "NA");
        assertEquals(returnedToRequest.getAttentionLine().get(0).getNullFlavor().get(1), "NATest");
    }

    @Test
    public void copyMCCIMT000100UV01AttentionLinePRPAIN201301UV02WhenAttentionLineEmpty() {
        HL7ArrayTransforms trans = new HL7ArrayTransforms();
        PRPAIN201301UV02 returnedToRequest = trans.copyMCCIMT000100UV01AttentionLine(createPRPAIN201305UV02Null(),
                createPRPAIN201301UV02WithAttentionLine());
        assertEquals(returnedToRequest.getAttentionLine().get(0).getNullFlavor().get(0), "NAAttentionLine");
    }

    @Test
    public void copyMCCIMT000100UV01Receiver() {
        PRPAIN201301UV02 toRequest = null;
        HL7ArrayTransforms trans = new HL7ArrayTransforms();
        PRPAIN201301UV02 returnedToRequest = trans.copyMCCIMT000100UV01Receiver(createPRPAIN201306UV02Receiver(),
                toRequest);
        assertEquals(returnedToRequest.getReceiver().get(0).getDevice().getId().get(0).getExtension(), "D123401");
    }

    @Test
    public void copyMCCIMT000100UV01ReceiverWhenFromRequestReceiverNull() {
        HL7ArrayTransforms trans = new HL7ArrayTransforms();
        PRPAIN201301UV02 returnedToRequest = trans.copyMCCIMT000100UV01Receiver(createPRPAIN201306UV02(),
                createPRPAIN201301UV02Receiver());
        assertEquals(returnedToRequest.getReceiver().get(0).getDevice().getId().get(0).getExtension(), "1.16.17.19");
    }

    @Test
    public void copyMCCIMT000100UV01PRPAIN201305UV02Receiver() {
        PRPAIN201301UV02 toRequest = null;
        HL7ArrayTransforms trans = new HL7ArrayTransforms();
        PRPAIN201301UV02 returnedToRequest = trans.copyMCCIMT000100UV01Receiver(
                createPRPAIN201305UV02ReceiverFromRequest(), toRequest);
        assertEquals(returnedToRequest.getReceiver().get(0).getDevice().getId().get(0).getExtension(), "1.16.17.19");
    }

    @Test
    public void copyReceiverWhenPRPAIN201305UV02FromRequestReceiverNull() {
        HL7ArrayTransforms trans = new HL7ArrayTransforms();
        PRPAIN201301UV02 returnedToRequest = trans.copyMCCIMT000100UV01Receiver(createPRPAIN201305UV02Null(),
                createPRPAIN201301UV02Receiver());
        assertEquals(returnedToRequest.getReceiver().get(0).getDevice().getId().get(0).getExtension(), "1.16.17.19");
    }

    @Test
    public void copyRealmCodesPRPAIN201305UV02() {
        HL7ArrayTransforms trans = new HL7ArrayTransforms();
        PRPAIN201301UV02 returnedToRequest = trans.copyRealmCodes(createPRPAIN201305UV02(), createPRPAIN201301UV02());
        assertEquals(returnedToRequest.getRealmCode().get(0).getCode(), "CONNECT");
        assertEquals(returnedToRequest.getRealmCode().get(0).getCodeSystem(), "CONNECTDomain");
    }

    @Test
    public void copyRealmCodesPRPAIN201305UV02FromRequestNull() {
        PRPAIN201305UV02 fromRequest = null;
        HL7ArrayTransforms trans = new HL7ArrayTransforms();
        PRPAIN201301UV02 returnedToRequest = trans.copyRealmCodes(fromRequest, createPRPAIN201301UV02());
        assertEquals(returnedToRequest.getRealmCode().get(0).getCode(), "ToRequestCode");
        assertEquals(returnedToRequest.getRealmCode().get(0).getCodeSystem(), "ToRequestCONNECTDomain");
    }

    @Test
    public void copyRealmCodesPRPAIN201306UV02() {
        HL7ArrayTransforms trans = new HL7ArrayTransforms();
        PRPAIN201301UV02 returnedToRequest = trans.copyRealmCodes(createPRPAIN201306UV02(), createPRPAIN201301UV02());
        assertEquals(returnedToRequest.getRealmCode().get(0).getCode(), "CONNECT");
        assertEquals(returnedToRequest.getRealmCode().get(0).getCodeSystem(), "CONNECTDomain");
    }

    @Test
    public void copyRealmCodesPRPAIN201306UV02FromRequestNull() {
        PRPAIN201306UV02 fromRequest = null;
        HL7ArrayTransforms trans = new HL7ArrayTransforms();
        PRPAIN201301UV02 returnedToRequest = trans.copyRealmCodes(fromRequest, createPRPAIN201301UV02());
        assertEquals(returnedToRequest.getRealmCode().get(0).getCode(), "ToRequestCode");
        assertEquals(returnedToRequest.getRealmCode().get(0).getCodeSystem(), "ToRequestCONNECTDomain");
    }

    @Test
    public void copyRealmCodesAttentionLine() {
        HL7ArrayTransforms trans = new HL7ArrayTransforms();
        MCCIMT000100UV01AttentionLine returnedToRequest = trans.copyRealmCodes(
                createMCCIMT000300UV01AttentionLineFromRequest(), createMCCIMT000100UV01AttentionLineToRequest());
        assertEquals(returnedToRequest.getRealmCode().get(0).getCode(), "CONNECT");
        assertEquals(returnedToRequest.getRealmCode().get(0).getCodeSystem(), "CONNECTDomain");
    }

    @Test
    public void copyRealmCodesAttentionLineFromRequestNull() {
        MCCIMT000300UV01AttentionLine fromRequest = null;
        HL7ArrayTransforms trans = new HL7ArrayTransforms();
        MCCIMT000100UV01AttentionLine returnedToRequest = trans.copyRealmCodes(fromRequest,
                createMCCIMT000100UV01AttentionLineToRequest());
        assertEquals(returnedToRequest.getRealmCode().get(0).getCode(), "ToRequestCode");
        assertEquals(returnedToRequest.getRealmCode().get(0).getCodeSystem(), "ToRequestCONNECTDomain");
    }

    @Test
    public void copyRealmCodesInformationRecipient() {
        HL7ArrayTransforms trans = new HL7ArrayTransforms();
        MFMIMT700701UV01InformationRecipient returnedToRequest = trans.copyRealmCodes(
                createMFMIMT700711UV01InformationRecipientFromRequest(),
                createMFMIMT700701UV01InformationRecipientToRequest());
        assertEquals(returnedToRequest.getRealmCode().get(0).getCode(), "CONNECT");
        assertEquals(returnedToRequest.getRealmCode().get(0).getCodeSystem(), "CONNECTDomain");
    }

    @Test
    public void copyRealmCodesInformationRecipientFromRequestNull() {
        MFMIMT700711UV01InformationRecipient fromRequest = null;
        HL7ArrayTransforms trans = new HL7ArrayTransforms();
        MFMIMT700701UV01InformationRecipient returnedToRequest = trans.copyRealmCodes(fromRequest,
                createMFMIMT700701UV01InformationRecipientToRequest());
        assertEquals(returnedToRequest.getRealmCode().get(0).getCode(), "ToRequestCode");
        assertEquals(returnedToRequest.getRealmCode().get(0).getCodeSystem(), "ToRequestCONNECTDomain");
    }

    @Test
    public void copyRealmCodesAuthorOrPerformer() {
        HL7ArrayTransforms trans = new HL7ArrayTransforms();
        MFMIMT700701UV01AuthorOrPerformer returnedToRequest = trans.copyRealmCodes(
                createQUQIMT021001UV01AuthorOrPerformer(), createMFMIMT700701UV01AuthorOrPerformer());
        assertEquals(returnedToRequest.getRealmCode().get(0).getCode(), "CONNECT");
        assertEquals(returnedToRequest.getRealmCode().get(0).getCodeSystem(), "CONNECTDomain");
    }

    @Test
    public void copyRealmCodesAuthorOrPerformerFromRequestNull() {
        QUQIMT021001UV01AuthorOrPerformer fromRequest = null;
        HL7ArrayTransforms trans = new HL7ArrayTransforms();
        MFMIMT700701UV01AuthorOrPerformer returnedToRequest = trans.copyRealmCodes(fromRequest,
                createMFMIMT700701UV01AuthorOrPerformer());
        assertEquals(returnedToRequest.getRealmCode().get(0).getCode(), "ToRequestCode");
        assertEquals(returnedToRequest.getRealmCode().get(0).getCodeSystem(), "ToRequestCONNECTDomain");
    }

    @Test
    public void copyRealmCodesControlActProcess() {
        HL7ArrayTransforms trans = new HL7ArrayTransforms();
        PRPAIN201301UV02MFMIMT700701UV01ControlActProcess returnedToRequest = trans.copyRealmCodes(
                createPRPAIN201306UV02MFMIMT700711UV01ControlActProcess(),
                createPRPAIN201301UV02MFMIMT700701UV01ControlActProcess());
        assertEquals(returnedToRequest.getRealmCode().get(0).getCode(), "CONNECT");
        assertEquals(returnedToRequest.getRealmCode().get(0).getCodeSystem(), "CONNECTDomain");
    }

    @Test
    public void copyRealmCodesControlActProcessFromRequestNull() {
        PRPAIN201306UV02MFMIMT700711UV01ControlActProcess fromRequest = null;
        HL7ArrayTransforms trans = new HL7ArrayTransforms();
        PRPAIN201301UV02MFMIMT700701UV01ControlActProcess returnedToRequest = trans.copyRealmCodes(fromRequest,
                createPRPAIN201301UV02MFMIMT700701UV01ControlActProcess());
        assertEquals(returnedToRequest.getRealmCode().get(0).getCode(), "ToRequestCode");
        assertEquals(returnedToRequest.getRealmCode().get(0).getCodeSystem(), "ToRequestCONNECTDomain");
    }

    @Test
    public void copyTemplateIdsInformationRecipient() {
        HL7ArrayTransforms trans = new HL7ArrayTransforms();
        MFMIMT700701UV01InformationRecipient returnedToRequest = trans.copyTemplateIds(
                createMFMIMT700711UV01InformationRecipientFromRequest(),
                createMFMIMT700701UV01InformationRecipientToRequest());
        assertEquals(returnedToRequest.getTemplateId().get(0).getExtension(), "D123401");
        assertEquals(returnedToRequest.getTemplateId().get(0).getRoot(), "1.1");
    }

    @Test
    public void copyTemplateIdsInformationRecipientFromRequestNull() {
        HL7ArrayTransforms trans = new HL7ArrayTransforms();
        MFMIMT700711UV01InformationRecipient fromRequest = null;
        MFMIMT700701UV01InformationRecipient returnedToRequest = trans.copyTemplateIds(fromRequest,
                createMFMIMT700701UV01InformationRecipientToRequest());
        assertEquals(returnedToRequest.getTemplateId().get(0).getExtension(), "1.16.17.19");
        assertEquals(returnedToRequest.getTemplateId().get(0).getRoot(), "1.1");
    }

    @Test
    public void copyTemplateIdsAttentionLine() {
        HL7ArrayTransforms trans = new HL7ArrayTransforms();
        MCCIMT000100UV01AttentionLine returnedToRequest = trans.copyTemplateIds(
                createMCCIMT000300UV01AttentionLineFromRequest(), createMCCIMT000100UV01AttentionLineToRequest());
        assertEquals(returnedToRequest.getTemplateId().get(0).getExtension(), "D123401");
        assertEquals(returnedToRequest.getTemplateId().get(0).getRoot(), "1.1");

    }

    @Test
    public void copyTemplateIdsAttentionLineFromRequestNull() {
        HL7ArrayTransforms trans = new HL7ArrayTransforms();
        MCCIMT000300UV01AttentionLine fromRequest = null;
        MCCIMT000100UV01AttentionLine returnedToRequest = trans.copyTemplateIds(fromRequest,
                createMCCIMT000100UV01AttentionLineToRequest());
        assertEquals(returnedToRequest.getTemplateId().get(0).getExtension(), "1.16.17.19");
        assertEquals(returnedToRequest.getTemplateId().get(0).getRoot(), "1.1");

    }

    @Test
    public void copyIIs() {
        HL7ArrayTransforms trans = new HL7ArrayTransforms();
        PRPAMT201301UV02Patient returnedToRequest = trans.copyIIs(createPRPAMT201310UV02Patient(),
                createPRPAMT201301UV02PatientToRequest());
        assertEquals(returnedToRequest.getId().get(0).getExtension(), "D123401");
        assertEquals(returnedToRequest.getId().get(0).getRoot(), "1.1");
    }

    @Test
    public void copyIIsFromRequestNull() {
        PRPAMT201310UV02Patient fromRequest = null;
        HL7ArrayTransforms trans = new HL7ArrayTransforms();
        PRPAMT201301UV02Patient returnedToRequest = trans
                .copyIIs(fromRequest, createPRPAMT201301UV02PatientToRequest());
        assertEquals(returnedToRequest.getId().get(0).getExtension(), "1.16.17.19");
        assertEquals(returnedToRequest.getId().get(0).getRoot(), "1.1");
    }

    private PRPAMT201310UV02Patient createPRPAMT201310UV02Patient() {
        PRPAMT201310UV02Patient fromRequest = new PRPAMT201310UV02Patient();
        fromRequest.getId().add(createTypeId());
        return fromRequest;
    }

    private PRPAMT201301UV02Patient createPRPAMT201301UV02PatientToRequest() {
        PRPAMT201301UV02Patient toRequest = new PRPAMT201301UV02Patient();
        toRequest.getId().add(createII());
        return toRequest;
    }

    private PRPAIN201305UV02 createPRPAIN201305UV02ReceiverFromRequest() {
        PRPAIN201305UV02 fromRequest = createPRPAIN201305UV02();
        MCCIMT000100UV01Receiver receiver = createMCCIMT000100UV01Receiver();
        fromRequest.getReceiver().add(receiver);
        return fromRequest;
    }

    private PRPAIN201301UV02 createPRPAIN201301UV02Receiver() {
        PRPAIN201301UV02 toRequest = createPRPAIN201301UV02();
        MCCIMT000100UV01Receiver receiver = createMCCIMT000100UV01Receiver();
        toRequest.getReceiver().add(receiver);
        return toRequest;
    }

    private MCCIMT000100UV01Receiver createMCCIMT000100UV01Receiver() {
        MCCIMT000100UV01Receiver receiver = new MCCIMT000100UV01Receiver();
        MCCIMT000100UV01Device device = new MCCIMT000100UV01Device();
        device.setDeterminerCode("INSTANCE");
        device.setDesc(createEDExplicit());
        device.setTypeId(createII());
        device.getId().add(createII());
        device.setSoftwareName(createSoftwareName());
        device.setExistenceTime(createIVLTSExplicit());
        device.setAsAgent(createMCCIMT000100UV01Agent());
        receiver.setDevice(device);
        return receiver;
    }

    private JAXBElement<MCCIMT000100UV01Agent> createMCCIMT000100UV01Agent() {
        MCCIMT000100UV01Agent agent = new MCCIMT000100UV01Agent();
        MCCIMT000100UV01Organization org = new MCCIMT000100UV01Organization();
        org.getId().add(createII());
        org.hl7.v3.ObjectFactory JaxbObjectFactory = new org.hl7.v3.ObjectFactory();
        JAXBElement<MCCIMT000100UV01Organization> repOrgReceiver = JaxbObjectFactory
                .createMCCIMT000100UV01AgentRepresentedOrganization(org);
        repOrgReceiver.setValue(org);
        agent.setRepresentedOrganization(repOrgReceiver);
        JAXBElement<MCCIMT000100UV01Agent> agentReceiver = JaxbObjectFactory.createMCCIMT000100UV01DeviceAsAgent(agent);
        agentReceiver.setValue(agent);
        return agentReceiver;
    }

    private II createII() {
        II ii = new II();
        ii.setAssigningAuthorityName("CONNECT");
        ii.setExtension("1.16.17.19");
        ii.setRoot("1.1");
        return ii;
    }

    private PRPAIN201306UV02 createPRPAIN201306UV02Receiver() {
        PRPAIN201306UV02 fromRequest = createPRPAIN201306UV02();
        MCCIMT000300UV01Receiver receiver = createMCCIMT000300UV01Receiver();
        fromRequest.getReceiver().add(receiver);
        return fromRequest;
    }

    private MCCIMT000300UV01Receiver createMCCIMT000300UV01Receiver() {
        MCCIMT000300UV01Receiver receiver = new MCCIMT000300UV01Receiver();
        MCCIMT000300UV01Device device = new MCCIMT000300UV01Device();
        device.setDeterminerCode("INSTANCE");
        device.setDesc(createEDExplicit());
        device.setTypeId(createTypeId());
        device.getId().add(createTypeId());
        device.setSoftwareName(createSoftwareName());
        device.setExistenceTime(createIVLTSExplicit());
        device.setAsAgent(createMCCIMT000300UV01Agent());
        receiver.setDevice(device);
        return receiver;
    }

    private JAXBElement<MCCIMT000300UV01Agent> createMCCIMT000300UV01Agent() {
        MCCIMT000300UV01Agent agent = new MCCIMT000300UV01Agent();
        MCCIMT000300UV01Organization org = new MCCIMT000300UV01Organization();
        org.getId().add(createTypeId());
        org.hl7.v3.ObjectFactory JaxbObjectFactory = new org.hl7.v3.ObjectFactory();
        JAXBElement<MCCIMT000300UV01Organization> repOrgReceiver = JaxbObjectFactory
                .createMCCIMT000300UV01AgentRepresentedOrganization(org);
        repOrgReceiver.setValue(org);
        agent.setRepresentedOrganization(repOrgReceiver);
        JAXBElement<MCCIMT000300UV01Agent> agentReceiver = JaxbObjectFactory.createMCCIMT000300UV01DeviceAsAgent(agent);
        agentReceiver.setValue(agent);
        return agentReceiver;
    }

    private IVLTSExplicit createIVLTSExplicit() {
        IVLTSExplicit timeExplicit = new IVLTSExplicit();
        timeExplicit.setValue("time");
        return timeExplicit;
    }

    private SC createSoftwareName() {
        SC sc = new SC();
        sc.setCode("code");
        sc.setCodeSystem("CONNECT4.0");
        return sc;
    }

    private EDExplicit createEDExplicit() {
        EDExplicit ed = new EDExplicit();
        ed.setLanguage("English");
        return ed;
    }

    private II createTypeId() {
        II ii = new II();
        ii.setAssigningAuthorityName("CONNECT");
        ii.setExtension("D123401");
        ii.setRoot("1.1");
        return ii;
    }

    private PRPAIN201305UV02 createPRPAIN201305UV02WithAttentionLine() {
        PRPAIN201305UV02 fromRequest = createPRPAIN201305UV02();
        MCCIMT000100UV01AttentionLine attentionLine = new MCCIMT000100UV01AttentionLine();
        attentionLine.getNullFlavor().add("NA");
        attentionLine.getNullFlavor().add("NATest");
        fromRequest.getAttentionLine().add(attentionLine);
        return fromRequest;
    }

    private PRPAIN201301UV02 createPRPAIN201301UV02WithAttentionLine() {
        PRPAIN201301UV02 toRequest = createPRPAIN201301UV02();
        toRequest.getAttentionLine().add(createMCCIMT000100UV01AttentionLineToRequest());
        return toRequest;
    }

    private PRPAIN201306UV02 createPRPAIN201306UV02WithAttentionLine() {
        PRPAIN201306UV02 fromRequest = new PRPAIN201306UV02();
        fromRequest.getNullFlavor().add("NA");
        fromRequest.getNullFlavor().add("NATest");
        fromRequest.getAttentionLine().add(createMCCIMT000300UV01AttentionLineFromRequest());
        return fromRequest;
    }

    private MFMIMT700711UV01AuthorOrPerformer createMFMIMT700711UV01AuthorOrPerformer() {
        MFMIMT700711UV01AuthorOrPerformer fromRequest = new MFMIMT700711UV01AuthorOrPerformer();
        fromRequest.getNullFlavor().add("NA");
        fromRequest.getNullFlavor().add("NATest");
        return fromRequest;
    }

    private MFMIMT700711UV01AuthorOrPerformer createMFMIMT700711UV01AuthorOrPerformerEmpty() {
        MFMIMT700711UV01AuthorOrPerformer fromRequest = new MFMIMT700711UV01AuthorOrPerformer();
        return fromRequest;
    }

    private QUQIMT021001UV01AuthorOrPerformer createQUQIMT021001UV01AuthorOrPerformerEmpty() {
        QUQIMT021001UV01AuthorOrPerformer fromRequest = new QUQIMT021001UV01AuthorOrPerformer();
        return fromRequest;
    }

    private MFMIMT700701UV01AuthorOrPerformer createMFMIMT700701UV01AuthorOrPerformer() {
        MFMIMT700701UV01AuthorOrPerformer toRequest = new MFMIMT700701UV01AuthorOrPerformer();
        toRequest.getNullFlavor().add("NAAuthorOrPerformer");
        toRequest.getRealmCode().add(createRealmCodeToRequest());
        return toRequest;
    }

    private QUQIMT021001UV01AuthorOrPerformer createQUQIMT021001UV01AuthorOrPerformer() {
        QUQIMT021001UV01AuthorOrPerformer fromRequest = new QUQIMT021001UV01AuthorOrPerformer();
        fromRequest.getNullFlavor().add("NA");
        fromRequest.getNullFlavor().add("NATest");
        fromRequest.getRealmCode().add(createRealmCodeFromRequest());
        return fromRequest;
    }

    private PRPAIN201305UV02QUQIMT021001UV01ControlActProcess createPRPAIN201305UV02QUQIMT021001UV01ControlActProcessEmpty() {
        PRPAIN201305UV02QUQIMT021001UV01ControlActProcess toRequest = new PRPAIN201305UV02QUQIMT021001UV01ControlActProcess();
        toRequest.getNullFlavor().add("NAControlActProcess");
        return toRequest;
    }

    private PRPAIN201305UV02QUQIMT021001UV01ControlActProcess createPRPAIN201305UV02QUQIMT021001UV01ControlActProcess() {
        PRPAIN201305UV02QUQIMT021001UV01ControlActProcess fromRequest = new PRPAIN201305UV02QUQIMT021001UV01ControlActProcess();
        fromRequest.getNullFlavor().add("NA");
        fromRequest.getNullFlavor().add("NATest");
        return fromRequest;
    }

    private PRPAIN201306UV02MFMIMT700711UV01ControlActProcess createPRPAIN201306UV02MFMIMT700711UV01ControlActProcessEmpty() {
        PRPAIN201306UV02MFMIMT700711UV01ControlActProcess fromRequest = new PRPAIN201306UV02MFMIMT700711UV01ControlActProcess();
        return fromRequest;
    }

    private PRPAIN201301UV02MFMIMT700701UV01ControlActProcess createPRPAIN201301UV02MFMIMT700701UV01ControlActProcess() {
        PRPAIN201301UV02MFMIMT700701UV01ControlActProcess toRequest = new PRPAIN201301UV02MFMIMT700701UV01ControlActProcess();
        toRequest.getNullFlavor().add("NAControlActProcess");
        toRequest.getRealmCode().add(createRealmCodeToRequest());
        return toRequest;
    }

    private PRPAIN201306UV02MFMIMT700711UV01ControlActProcess createPRPAIN201306UV02MFMIMT700711UV01ControlActProcess() {
        PRPAIN201306UV02MFMIMT700711UV01ControlActProcess fromRequest = new PRPAIN201306UV02MFMIMT700711UV01ControlActProcess();
        fromRequest.getNullFlavor().add("NA");
        fromRequest.getNullFlavor().add("NATest");
        fromRequest.getRealmCode().add(createRealmCodeFromRequest());
        return fromRequest;
    }

    private QUQIMT021001UV01DataEnterer createQUQIMT021001UV01DataEntererFromRequestEmpty() {
        QUQIMT021001UV01DataEnterer fromRequest = new QUQIMT021001UV01DataEnterer();
        return fromRequest;
    }

    private QUQIMT021001UV01DataEnterer createQUQIMT021001UV01DataEntererFromRequest() {
        QUQIMT021001UV01DataEnterer fromRequest = new QUQIMT021001UV01DataEnterer();
        fromRequest.getNullFlavor().add("NA");
        fromRequest.getNullFlavor().add("NATest");
        return fromRequest;
    }

    private MFMIMT700711UV01DataEnterer createMFMIMT700711UV01DataEntererFromRequestEmpty() {
        MFMIMT700711UV01DataEnterer fromRequest = new MFMIMT700711UV01DataEnterer();
        return fromRequest;
    }

    private MFMIMT700701UV01DataEnterer createMFMIMT700701UV01DataEntererToRequest() {
        MFMIMT700701UV01DataEnterer toRequest = new MFMIMT700701UV01DataEnterer();
        toRequest.getNullFlavor().add("NADataEnterer");
        return toRequest;
    }

    private MFMIMT700711UV01DataEnterer createMFMIMT700711UV01DataEntererFromRequest() {
        MFMIMT700711UV01DataEnterer fromRequest = new MFMIMT700711UV01DataEnterer();
        fromRequest.getNullFlavor().add("NA");
        fromRequest.getNullFlavor().add("NATest");
        return fromRequest;
    }

    private PRPAMT201306UV02ParameterList createPRPAMT201306UV02ParameterListEmpty() {
        PRPAMT201306UV02ParameterList fromRequest = new PRPAMT201306UV02ParameterList();
        return fromRequest;
    }

    private PRPAMT201301UV02Person createPRPAMT201301UV02Person() {
        PRPAMT201301UV02Person toRequest = new PRPAMT201301UV02Person();
        toRequest.getNullFlavor().add("NAPatientPerson");
        return toRequest;
    }

    private PRPAMT201306UV02ParameterList createPRPAMT201306UV02ParameterList() {
        PRPAMT201306UV02ParameterList fromRequest = new PRPAMT201306UV02ParameterList();
        fromRequest.getNullFlavor().add("NA");
        fromRequest.getNullFlavor().add("NATest");
        return fromRequest;
    }

    private MCCIMT000300UV01AttentionLine createMCCIMT000300UV01AttentionLineFromRequestEmpty() {
        MCCIMT000300UV01AttentionLine fromRequest = new MCCIMT000300UV01AttentionLine();
        return fromRequest;
    }

    private MCCIMT000300UV01AttentionLine createMCCIMT000300UV01AttentionLineFromRequest() {
        MCCIMT000300UV01AttentionLine fromRequest = new MCCIMT000300UV01AttentionLine();
        fromRequest.getNullFlavor().add("NA");
        fromRequest.getNullFlavor().add("NATest");
        fromRequest.getRealmCode().add(createRealmCodeFromRequest());
        fromRequest.getTemplateId().add(createTypeId());
        return fromRequest;
    }

    private MCCIMT000100UV01AttentionLine createMCCIMT000100UV01AttentionLineToRequest() {
        MCCIMT000100UV01AttentionLine toRequest = new MCCIMT000100UV01AttentionLine();
        toRequest.getNullFlavor().add("NAAttentionLine");
        toRequest.getRealmCode().add(createRealmCodeToRequest());
        toRequest.getTemplateId().add(createII());
        return toRequest;
    }

    private ENExplicit createENExplicitFromRequestEmpty() {
        ENExplicit fromRequest = new ENExplicit();
        return fromRequest;
    }

    private PNExplicit createPNExplicitToRequest() {
        PNExplicit toRequest = new PNExplicit();
        toRequest.getNullFlavor().add("NAPNExplicit");
        return toRequest;
    }

    private ENExplicit createENExplicitFromRequest() {
        ENExplicit fromRequest = new ENExplicit();
        fromRequest.getNullFlavor().add("NA");
        fromRequest.getNullFlavor().add("NATest");
        return fromRequest;
    }

    private PNExplicit createPNExplicitFromRequestEmpty() {
        PNExplicit fromRequest = new PNExplicit();
        return fromRequest;
    }

    private ENExplicit createENExplicitToRequest() {
        ENExplicit toRequest = new ENExplicit();
        toRequest.getNullFlavor().add("NAPNExplicit");
        return toRequest;
    }

    private PNExplicit createPNExplicitFromRequest() {
        PNExplicit fromRequest = new PNExplicit();
        fromRequest.getNullFlavor().add("NA");
        fromRequest.getNullFlavor().add("NATest");
        return fromRequest;
    }

    private MFMIMT700711UV01InformationRecipient createMFMIMT700701UV01InformationRecipientFromRequestEmpty() {
        MFMIMT700711UV01InformationRecipient fromRequest = new MFMIMT700711UV01InformationRecipient();
        return fromRequest;
    }

    private MFMIMT700711UV01InformationRecipient createMFMIMT700711UV01InformationRecipientFromRequest() {
        MFMIMT700711UV01InformationRecipient fromRequest = new MFMIMT700711UV01InformationRecipient();
        fromRequest.getNullFlavor().add("NA");
        fromRequest.getNullFlavor().add("NATest");
        fromRequest.getRealmCode().add(createRealmCodeFromRequest());
        fromRequest.getTemplateId().add(createTypeId());
        return fromRequest;
    }

    private MFMIMT700701UV01InformationRecipient createMFMIMT700701UV01InformationRecipientToRequest() {
        MFMIMT700701UV01InformationRecipient toRequest = new MFMIMT700701UV01InformationRecipient();
        toRequest.getNullFlavor().add("NAInformationRecipient");
        toRequest.getRealmCode().add(createRealmCodeToRequest());
        toRequest.getTemplateId().add(createII());
        return toRequest;
    }

    private PRPAIN201306UV02 createPRPAIN201306UV02Empty() {
        PRPAIN201306UV02 fromRequest = new PRPAIN201306UV02();
        return fromRequest;
    }

    private PRPAIN201306UV02 createPRPAIN201306UV02() {
        PRPAIN201306UV02 fromRequest = new PRPAIN201306UV02();
        fromRequest.getNullFlavor().add("NA");
        fromRequest.getNullFlavor().add("NATest");
        fromRequest.getRealmCode().add(createRealmCodeFromRequest());
        return fromRequest;
    }

    private PRPAIN201305UV02 createPRPAIN201305UV02() {
        PRPAIN201305UV02 fromRequest = new PRPAIN201305UV02();
        fromRequest.getNullFlavor().add("NA");
        fromRequest.getNullFlavor().add("NATest");
        fromRequest.getRealmCode().add(createRealmCodeFromRequest());
        return fromRequest;
    }

    private CS createRealmCodeFromRequest() {
        CS cs = new CS();
        cs.setCode("CONNECT");
        cs.setCodeSystem("CONNECTDomain");
        return cs;
    }

    private PRPAIN201305UV02 createPRPAIN201305UV02Null() {
        PRPAIN201305UV02 fromRequest = new PRPAIN201305UV02();
        return fromRequest;
    }

    private PRPAIN201301UV02 createPRPAIN201301UV02() {
        PRPAIN201301UV02 toRequest = new PRPAIN201301UV02();
        toRequest.getNullFlavor().add("NA201301");
        toRequest.getRealmCode().add(createRealmCodeToRequest());
        return toRequest;
    }

    private CS createRealmCodeToRequest() {
        CS cs = new CS();
        cs.setCode("ToRequestCode");
        cs.setCodeSystem("ToRequestCONNECTDomain");
        return cs;
    }

}
