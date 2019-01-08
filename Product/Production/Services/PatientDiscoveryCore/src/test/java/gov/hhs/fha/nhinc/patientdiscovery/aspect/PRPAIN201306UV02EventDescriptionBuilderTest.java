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
package gov.hhs.fha.nhinc.patientdiscovery.aspect;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.event.AssertionEventDescriptionBuilder;
import gov.hhs.fha.nhinc.event.BaseDescriptionBuilderTest;
import gov.hhs.fha.nhinc.event.EventDescription;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.util.Base64Coder;
import java.io.Serializable;
import java.util.List;
import static org.hamcrest.CoreMatchers.hasItems;
import org.hl7.v3.BinaryDataEncoding;
import org.hl7.v3.COCTMT090003UV01AssignedEntity;
import org.hl7.v3.EDExplicit;
import org.hl7.v3.II;
import org.hl7.v3.MCCIMT000300UV01Acknowledgement;
import org.hl7.v3.MCCIMT000300UV01AcknowledgementDetail;
import org.hl7.v3.MFMIMT700711UV01Custodian;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.PRPAIN201306UV02MFMIMT700711UV01ControlActProcess;
import org.hl7.v3.PRPAIN201306UV02MFMIMT700711UV01RegistrationEvent;
import org.hl7.v3.PRPAIN201306UV02MFMIMT700711UV01Subject1;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import org.springframework.util.CollectionUtils;

public class PRPAIN201306UV02EventDescriptionBuilderTest extends BaseDescriptionBuilderTest {

    private PRPAIN201306UV02 body;
    private List<MCCIMT000300UV01Acknowledgement> acknowledgementList;

    @Before
    public void before() {
        body = new PRPAIN201306UV02();
        acknowledgementList = body.getAcknowledgement();
    }

    @Test
    public void handlesAssertions() {
        assertTrue(AssertionEventDescriptionBuilder.class
                .isAssignableFrom(PRPAIN201306UV02EventDescriptionBuilder.class));
        PRPAIN201306UV02EventDescriptionBuilder builder = new PRPAIN201306UV02EventDescriptionBuilder();

        AssertionType assertion = mock(AssertionType.class);

        builder.setArguments(new Object[] { assertion });
        assertTrue(builder.getAssertion().isPresent());
    }

    @Test
    public void noReturnValue() {
        PRPAIN201306UV02EventDescriptionBuilder builder = new PRPAIN201306UV02EventDescriptionBuilder();
        EventDescription eventDescription = getEventDescription(builder);
        assertNotNull(eventDescription);
    }

    @Test
    public void basicStatus() {
        addAcknowledgements(1, createDetail("Test Content"));

        PRPAIN201306UV02EventDescriptionBuilder builder = new PRPAIN201306UV02EventDescriptionBuilder();
        builder.setReturnValue(body);

        EventDescription eventDescription = getEventDescription(builder);
        assertEquals(1, eventDescription.getStatuses().size());
        assertEquals("Test Content", eventDescription.getStatuses().get(0));
    }

    @Test
    public void twoAcknowledgements() {
        addAcknowledgements(2, createDetail("Test Content"));

        PRPAIN201306UV02EventDescriptionBuilder builder = new PRPAIN201306UV02EventDescriptionBuilder();
        builder.setReturnValue(body);

        EventDescription eventDescription = getEventDescription(builder);
        assertEquals(1, eventDescription.getStatuses().size());
        assertTrue(eventDescription.getStatuses().contains("Test Content"));
    }

    @Test
    public void multipleDetailsInAcknowledgement() {
        addAcknowledgements(1, createDetail("Test Content"), createDetail("Test Content2"));

        PRPAIN201306UV02EventDescriptionBuilder builder = new PRPAIN201306UV02EventDescriptionBuilder();
        builder.setReturnValue(body);

        EventDescription eventDescription = getEventDescription(builder);
        assertEquals(2, eventDescription.getStatuses().size());
        assertTrue(eventDescription.getStatuses().contains("Test Content"));
        assertTrue(eventDescription.getStatuses().contains("Test Content2"));
    }

    @Test
    public void base64StatusContent() {
        addAcknowledgements(1, createDetail(BinaryDataEncoding.B_64, "Test Content"));

        PRPAIN201306UV02EventDescriptionBuilder builder = new PRPAIN201306UV02EventDescriptionBuilder();
        builder.setReturnValue(body);

        EventDescription eventDescription = getEventDescription(builder);
        assertEquals(1, eventDescription.getStatuses().size());
        assertEquals("Test Content", eventDescription.getStatuses().get(0));
    }

    @Test
    public void nullFlavoredStatus() {
        MCCIMT000300UV01AcknowledgementDetail detail = new MCCIMT000300UV01AcknowledgementDetail();
        detail.getNullFlavor().add("NI");

        addAcknowledgements(1, detail);

        PRPAIN201306UV02EventDescriptionBuilder builder = new PRPAIN201306UV02EventDescriptionBuilder();
        builder.setReturnValue(body);

        EventDescription eventDescription = getEventDescription(builder);
        assertEquals(0, eventDescription.getStatuses().size());
    }

    @Test
    public void missingSerializedContent() {
        addAcknowledgements(1, createDetail(BinaryDataEncoding.TXT, null));

        PRPAIN201306UV02EventDescriptionBuilder builder = new PRPAIN201306UV02EventDescriptionBuilder();
        builder.setReturnValue(body);

        EventDescription eventDescription = getEventDescription(builder);
        assertEquals(0, eventDescription.getStatuses().size());
    }

    @Test
    public void basicHCID() {
        II ii = new II();
        ii.setRoot("1.2.3");

        EventDescription eventDescription = getEventDescriptionWithIIs(ii);
        assertEquals(1, eventDescription.getRespondingHCIDs().size());
        assertEquals("Specification says that prefix must be prepended to oid in response", NhincConstants.HCID_PREFIX
                + "1.2.3", eventDescription.getRespondingHCIDs().get(0));
    }

    @Test
    public void multipleIis() {
        II ii1 = new II();
        ii1.setRoot("1.2.3");

        II ii2 = new II();
        ii2.setRoot("1.2.3.4");

        EventDescription eventDescription = getEventDescriptionWithIIs(ii1, ii2);
        assertEquals(2, eventDescription.getRespondingHCIDs().size());
        assertThat(eventDescription.getRespondingHCIDs(),
            hasItems(NhincConstants.HCID_PREFIX + "1.2.3", NhincConstants.HCID_PREFIX + "1.2.3.4"));
    }

    private EventDescription getEventDescriptionWithIIs(II... iis) {
        COCTMT090003UV01AssignedEntity assignedEntity = getAssignedEntity(iis);
        MFMIMT700711UV01Custodian custodian = getCustodian(assignedEntity);
        PRPAIN201306UV02MFMIMT700711UV01RegistrationEvent registrationEvent = getRegistrationEvent(custodian);
        PRPAIN201306UV02MFMIMT700711UV01Subject1 subject = getSubject(registrationEvent);
        PRPAIN201306UV02MFMIMT700711UV01ControlActProcess controlAct = getControlAct(subject);

        body.setControlActProcess(controlAct);

        PRPAIN201306UV02EventDescriptionBuilder builder = new PRPAIN201306UV02EventDescriptionBuilder();
        builder.setReturnValue(body);
        return getEventDescription(builder);
    }

    @Test
    public void noSubject() {
        PRPAIN201306UV02MFMIMT700711UV01ControlActProcess controlAct = getControlAct(null);
        body.setControlActProcess(controlAct);

        assertEmptyRespondingHCIDs();
    }

    @Test
    public void noRegistartionEvent() {
        PRPAIN201306UV02MFMIMT700711UV01Subject1 subject = getSubject(null);
        PRPAIN201306UV02MFMIMT700711UV01ControlActProcess controlAct = getControlAct(subject);

        body.setControlActProcess(controlAct);

        assertEmptyRespondingHCIDs();
    }

    @Test
    public void noCustodian() {
        PRPAIN201306UV02MFMIMT700711UV01RegistrationEvent registrationEvent = getRegistrationEvent(null);
        PRPAIN201306UV02MFMIMT700711UV01Subject1 subject = getSubject(registrationEvent);
        PRPAIN201306UV02MFMIMT700711UV01ControlActProcess controlAct = getControlAct(subject);

        body.setControlActProcess(controlAct);

        assertEmptyRespondingHCIDs();
    }

    @Test
    public void noAssignedEntity() {
        MFMIMT700711UV01Custodian custodian = getCustodian(null);
        PRPAIN201306UV02MFMIMT700711UV01RegistrationEvent registrationEvent = getRegistrationEvent(custodian);
        PRPAIN201306UV02MFMIMT700711UV01Subject1 subject = getSubject(registrationEvent);
        PRPAIN201306UV02MFMIMT700711UV01ControlActProcess controlAct = getControlAct(subject);

        body.setControlActProcess(controlAct);

        assertEmptyRespondingHCIDs();
    }

    @Test
    public void noIi() {
        COCTMT090003UV01AssignedEntity assignedEntity = getAssignedEntity((II[]) null);
        MFMIMT700711UV01Custodian custodian = getCustodian(assignedEntity);
        PRPAIN201306UV02MFMIMT700711UV01RegistrationEvent registrationEvent = getRegistrationEvent(custodian);
        PRPAIN201306UV02MFMIMT700711UV01Subject1 subject = getSubject(registrationEvent);
        PRPAIN201306UV02MFMIMT700711UV01ControlActProcess controlAct = getControlAct(subject);

        body.setControlActProcess(controlAct);

        assertEmptyRespondingHCIDs();
    }

    private PRPAIN201306UV02MFMIMT700711UV01ControlActProcess getControlAct(
            PRPAIN201306UV02MFMIMT700711UV01Subject1 subject) {
        PRPAIN201306UV02MFMIMT700711UV01ControlActProcess controlAct = new PRPAIN201306UV02MFMIMT700711UV01ControlActProcess();
        if (subject != null) {
            controlAct.getSubject().add(subject);
        }
        return controlAct;
    }

    private PRPAIN201306UV02MFMIMT700711UV01Subject1 getSubject(PRPAIN201306UV02MFMIMT700711UV01RegistrationEvent event) {
        PRPAIN201306UV02MFMIMT700711UV01Subject1 subject = new PRPAIN201306UV02MFMIMT700711UV01Subject1();
        subject.setRegistrationEvent(event);
        return subject;
    }

    private PRPAIN201306UV02MFMIMT700711UV01RegistrationEvent getRegistrationEvent(MFMIMT700711UV01Custodian custodian) {
        PRPAIN201306UV02MFMIMT700711UV01RegistrationEvent registrationEvent = new PRPAIN201306UV02MFMIMT700711UV01RegistrationEvent();
        registrationEvent.setCustodian(custodian);
        return registrationEvent;
    }

    private MFMIMT700711UV01Custodian getCustodian(COCTMT090003UV01AssignedEntity assignedEntity) {
        MFMIMT700711UV01Custodian custodian = new MFMIMT700711UV01Custodian();
        custodian.setAssignedEntity(assignedEntity);
        return custodian;
    }

    private COCTMT090003UV01AssignedEntity getAssignedEntity(II... iis) {
        COCTMT090003UV01AssignedEntity assignedEntity = new COCTMT090003UV01AssignedEntity();
        if (iis != null) {
            for (int i = 0; i < iis.length; ++i) {
                assignedEntity.getId().add(iis[i]);
            }
        }
        return assignedEntity;
    }

    private void assertEmptyRespondingHCIDs() {
        PRPAIN201306UV02EventDescriptionBuilder builder = new PRPAIN201306UV02EventDescriptionBuilder();
        builder.setReturnValue(body);
        EventDescription eventDescription = getEventDescription(builder);
        assertTrue(CollectionUtils.isEmpty(eventDescription.getRespondingHCIDs()));
    }

    private void addAcknowledgements(int count, MCCIMT000300UV01AcknowledgementDetail... details) {
        for (int i = 0; i < count; ++i) {
            MCCIMT000300UV01Acknowledgement acknowledgement = new MCCIMT000300UV01Acknowledgement();
            acknowledgementList.add(acknowledgement);

            List<MCCIMT000300UV01AcknowledgementDetail> acknowledgementDetailList = acknowledgement
                    .getAcknowledgementDetail();
            for (int j = 0; j < details.length; ++j) {
                acknowledgementDetailList.add(details[j]);
            }
        }
    }

    private MCCIMT000300UV01AcknowledgementDetail createDetail(String rawContent) {
        return createDetail(BinaryDataEncoding.TXT, rawContent);
    }

    private MCCIMT000300UV01AcknowledgementDetail createDetail(BinaryDataEncoding encoding, String rawContent) {
        EDExplicit ed = new EDExplicit();
        ed.setRepresentation(encoding);
        if (rawContent != null) {
            List<Serializable> content = ed.getContent();
            if (encoding.equals(BinaryDataEncoding.TXT)) {
                content.add(rawContent);
            } else {
                content.add(Base64Coder.encodeString(rawContent));
            }
        }
        MCCIMT000300UV01AcknowledgementDetail detail = new MCCIMT000300UV01AcknowledgementDetail();
        detail.setText(ed);

        return detail;
    }
}
