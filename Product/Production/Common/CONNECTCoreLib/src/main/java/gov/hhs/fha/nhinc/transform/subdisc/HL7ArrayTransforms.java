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
import org.apache.commons.collections.CollectionUtils;
import org.hl7.v3.CS;
import org.hl7.v3.ENExplicit;
import org.hl7.v3.II;
import org.hl7.v3.MCCIMT000100UV01Agent;
import org.hl7.v3.MCCIMT000100UV01AttentionLine;
import org.hl7.v3.MCCIMT000100UV01Device;
import org.hl7.v3.MCCIMT000100UV01Organization;
import org.hl7.v3.MCCIMT000100UV01Receiver;
import org.hl7.v3.MCCIMT000300UV01AttentionLine;
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

/**
 *
 * @author dunnek
 */
public class HL7ArrayTransforms {

    public static PRPAIN201301UV02 copyNullFlavors(final PRPAIN201305UV02 from, final PRPAIN201301UV02 to) {
        if (!from.getNullFlavor().isEmpty()) {
            to.getNullFlavor().clear();
            for (final String item : from.getNullFlavor()) {
                to.getNullFlavor().add(item);
            }
        }

        return to;
    }

    public static PRPAIN201301UV02 copyNullFlavors(final PRPAIN201306UV02 from, final PRPAIN201301UV02 to) {
        if (!from.getNullFlavor().isEmpty()) {
            to.getNullFlavor().clear();
            for (final String item : from.getNullFlavor()) {
                to.getNullFlavor().add(item);
            }
        }

        return to;
    }

    public static MFMIMT700701UV01InformationRecipient copyNullFlavors(final MFMIMT700711UV01InformationRecipient from,
        final MFMIMT700701UV01InformationRecipient to) {
        if (!from.getNullFlavor().isEmpty()) {
            to.getNullFlavor().clear();
            for (final String item : from.getNullFlavor()) {
                to.getNullFlavor().add(item);
            }
        }

        return to;
    }

    public static ENExplicit copyNullFlavors(final PNExplicit from, final ENExplicit to) {
        if (!from.getNullFlavor().isEmpty()) {
            to.getNullFlavor().clear();
            for (final String item : from.getNullFlavor()) {
                to.getNullFlavor().add(item);
            }
        }

        return to;
    }

    public static PNExplicit copyNullFlavors(final ENExplicit from, final PNExplicit to) {
        if (!from.getNullFlavor().isEmpty()) {
            to.getNullFlavor().clear();
            for (final String item : from.getNullFlavor()) {
                to.getNullFlavor().add(item);
            }
        }

        return to;
    }

    public static MCCIMT000100UV01AttentionLine copyNullFlavors(final MCCIMT000300UV01AttentionLine from,
        final MCCIMT000100UV01AttentionLine to) {
        if (!from.getNullFlavor().isEmpty()) {
            to.getNullFlavor().clear();
            for (final String item : from.getNullFlavor()) {
                to.getNullFlavor().add(item);
            }
        }

        return to;
    }

    public static PRPAMT201301UV02Person copyNullFlavors(final PRPAMT201306UV02ParameterList from,
        final PRPAMT201301UV02Person to) {
        if (!from.getNullFlavor().isEmpty()) {
            to.getNullFlavor().clear();
            for (final String item : from.getNullFlavor()) {
                to.getNullFlavor().add(item);
            }
        }

        return to;
    }

    public static MFMIMT700701UV01DataEnterer copyNullFlavors(final MFMIMT700711UV01DataEnterer from,
        final MFMIMT700701UV01DataEnterer to) {
        if (!from.getNullFlavor().isEmpty()) {
            to.getNullFlavor().clear();
            for (final String item : from.getNullFlavor()) {
                to.getNullFlavor().add(item);
            }
        }

        return to;
    }

    public static MFMIMT700701UV01DataEnterer copyNullFlavors(final QUQIMT021001UV01DataEnterer from,
        final MFMIMT700701UV01DataEnterer to) {
        if (!from.getNullFlavor().isEmpty()) {
            to.getNullFlavor().clear();
            for (final String item : from.getNullFlavor()) {
                to.getNullFlavor().add(item);
            }
        }

        return to;
    }

    public static PRPAIN201301UV02MFMIMT700701UV01ControlActProcess copyNullFlavors(
        final PRPAIN201306UV02MFMIMT700711UV01ControlActProcess from,
        final PRPAIN201301UV02MFMIMT700701UV01ControlActProcess to) {
        if (!from.getNullFlavor().isEmpty()) {
            to.getNullFlavor().clear();
            for (final String item : from.getNullFlavor()) {
                to.getNullFlavor().add(item);
            }
        }

        return to;
    }

    public static PRPAIN201301UV02MFMIMT700701UV01ControlActProcess copyNullFlavors(
        final PRPAIN201305UV02QUQIMT021001UV01ControlActProcess from,
        final PRPAIN201301UV02MFMIMT700701UV01ControlActProcess to) {
        if (!from.getNullFlavor().isEmpty()) {
            to.getNullFlavor().clear();
            for (final String item : from.getNullFlavor()) {
                to.getNullFlavor().add(item);
            }
        }

        return to;
    }

    public static MFMIMT700701UV01AuthorOrPerformer copyNullFlavors(final MFMIMT700711UV01AuthorOrPerformer from,
        final MFMIMT700701UV01AuthorOrPerformer to) {
        if (!from.getNullFlavor().isEmpty()) {
            to.getNullFlavor().clear();
            for (final String item : from.getNullFlavor()) {
                to.getNullFlavor().add(item);
            }
        }

        return to;
    }

    public static MFMIMT700701UV01AuthorOrPerformer copyNullFlavors(final QUQIMT021001UV01AuthorOrPerformer from,
        final MFMIMT700701UV01AuthorOrPerformer to) {
        if (!from.getNullFlavor().isEmpty()) {
            to.getNullFlavor().clear();
            for (final String item : from.getNullFlavor()) {
                to.getNullFlavor().add(item);
            }
        }

        return to;
    }

    public static PRPAIN201301UV02 copyMCCIMT000100UV01AttentionLine(final PRPAIN201306UV02 from, PRPAIN201301UV02 to) {
        if (to == null) {
            to = new PRPAIN201301UV02();
        }
        if (!from.getAttentionLine().isEmpty()) {
            to.getAttentionLine().clear();
            for (final MCCIMT000300UV01AttentionLine line : from.getAttentionLine()) {

                to.getAttentionLine().add(copyAttentionLine(line));
            }
        }

        return to;
    }

    private static MCCIMT000100UV01AttentionLine copyAttentionLine(final MCCIMT000300UV01AttentionLine line) {
        MCCIMT000100UV01AttentionLine result = null;

        if (line != null) {
            result = new MCCIMT000100UV01AttentionLine();
            result.setKeyWordText(line.getKeyWordText());
            result.setTypeId(line.getTypeId());
            result.setValue(line.getValue());

            result = copyNullFlavors(line, result);
            result = copyRealmCodes(line, result);
            result = copyTemplateIds(line, result);
        }

        return result;
    }

    public static PRPAIN201301UV02 copyMCCIMT000100UV01AttentionLine(final PRPAIN201305UV02 from, PRPAIN201301UV02 to) {
        if (to == null) {
            to = new PRPAIN201301UV02();
        }
        if (!from.getAttentionLine().isEmpty()) {
            to.getAttentionLine().clear();
            for (final MCCIMT000100UV01AttentionLine line : from.getAttentionLine()) {
                to.getAttentionLine().add(line);
            }
        }

        return to;
    }

    public static PRPAIN201301UV02 copyMCCIMT000100UV01Receiver(final PRPAIN201306UV02 from, PRPAIN201301UV02 to) {
        if (to == null) {
            to = new PRPAIN201301UV02();
        }
        if (!from.getReceiver().isEmpty()) {
            to.getReceiver().clear();
            for (final MCCIMT000300UV01Receiver rec : from.getReceiver()) {
                to.getReceiver().add(copyReceiver(rec));
            }
        }

        return to;
    }

    private static MCCIMT000100UV01Receiver copyReceiver(final MCCIMT000300UV01Receiver orig) {
        MCCIMT000100UV01Receiver result = null;
        final MCCIMT000100UV01Device newDevice = new MCCIMT000100UV01Device();

        if (orig != null) {
            result = new MCCIMT000100UV01Receiver();
            newDevice.setDesc(orig.getDevice().getDesc());
            newDevice.setClassCode(orig.getDevice().getClassCode());
            newDevice.setDeterminerCode(orig.getDevice().getDeterminerCode());
            newDevice.setExistenceTime(orig.getDevice().getExistenceTime());
            newDevice.setManufacturerModelName(orig.getDevice().getManufacturerModelName());
            newDevice.setSoftwareName(orig.getDevice().getSoftwareName());
            newDevice.setTypeId(orig.getDevice().getTypeId());

            final MCCIMT000100UV01Agent agent = new MCCIMT000100UV01Agent();
            final MCCIMT000100UV01Organization org = new MCCIMT000100UV01Organization();
            org.setClassCode(HL7Constants.ORG_CLASS_CODE);
            org.setDeterminerCode(HL7Constants.RECEIVER_DETERMINER_CODE);
            if (orig.getDevice() != null && orig.getDevice().getAsAgent() != null
                && orig.getDevice().getAsAgent().getValue().getRepresentedOrganization() != null
                && CollectionUtils.isNotEmpty(orig.getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId())) {
                org.getId().add(orig.getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId()
                    .get(0));
            }

            final javax.xml.namespace.QName xmlqnameorg = new javax.xml.namespace.QName("urn:hl7-org:v3",
                "representedOrganization");
            final JAXBElement<MCCIMT000100UV01Organization> orgElem = new JAXBElement<>(xmlqnameorg,
                MCCIMT000100UV01Organization.class, org);
            agent.setRepresentedOrganization(orgElem);
            agent.getClassCode().add(HL7Constants.AGENT_CLASS_CODE);

            final javax.xml.namespace.QName xmlqnameagent = new javax.xml.namespace.QName("urn:hl7-org:v3", "asAgent");
            final JAXBElement<MCCIMT000100UV01Agent> agentElem = new JAXBElement<>(xmlqnameagent,
                MCCIMT000100UV01Agent.class, agent);
            newDevice.setAsAgent(agentElem);

            if (orig.getDevice() != null && CollectionUtils.isNotEmpty(orig.getDevice().getId())) {
                final II deviceId = orig.getDevice().getId().get(0);
                newDevice.getId().add(deviceId);
            }
            result.setTelecom(orig.getTelecom());
            result.setTypeCode(orig.getTypeCode());
            result.setTypeId(orig.getTypeId());
            result.setDevice(newDevice);
        }
        return result;
    }

    public static PRPAIN201301UV02 copyMCCIMT000100UV01Receiver(final PRPAIN201305UV02 from, PRPAIN201301UV02 to) {
        if (to == null) {
            to = new PRPAIN201301UV02();
        }
        if (!from.getReceiver().isEmpty()) {
            to.getReceiver().clear();
            for (final MCCIMT000100UV01Receiver rec : from.getReceiver()) {
                to.getReceiver().add(rec);
            }
        }

        return to;
    }

    public static PRPAIN201301UV02 copyRealmCodes(final PRPAIN201305UV02 from, final PRPAIN201301UV02 to) {
        if (from != null) {
            to.getRealmCode().clear();

            for (final CS realmCode : from.getRealmCode()) {
                to.getRealmCode().add(realmCode);
            }
        }

        return to;
    }

    public static PRPAIN201301UV02 copyRealmCodes(final PRPAIN201306UV02 from, final PRPAIN201301UV02 to) {
        if (from != null) {
            to.getRealmCode().clear();

            for (final CS realmCode : from.getRealmCode()) {
                to.getRealmCode().add(realmCode);
            }
        }

        return to;
    }

    public static MCCIMT000100UV01AttentionLine copyRealmCodes(final MCCIMT000300UV01AttentionLine from,
        final MCCIMT000100UV01AttentionLine to) {
        if (from != null) {
            to.getRealmCode().clear();

            for (final CS realmCode : from.getRealmCode()) {
                to.getRealmCode().add(realmCode);
            }
        }

        return to;
    }

    public static MFMIMT700701UV01InformationRecipient copyRealmCodes(final MFMIMT700711UV01InformationRecipient from,
        final MFMIMT700701UV01InformationRecipient to) {
        if (from != null) {
            to.getRealmCode().clear();

            for (final CS realmCode : from.getRealmCode()) {
                to.getRealmCode().add(realmCode);
            }
        }

        return to;
    }

    public static MFMIMT700701UV01AuthorOrPerformer copyRealmCodes(final QUQIMT021001UV01AuthorOrPerformer from,
        final MFMIMT700701UV01AuthorOrPerformer to) {

        if (from != null) {
            to.getRealmCode().clear();

            for (final CS realmCode : from.getRealmCode()) {
                to.getRealmCode().add(realmCode);
            }
        }

        return to;
    }

    public static PRPAIN201301UV02MFMIMT700701UV01ControlActProcess copyRealmCodes(
        final PRPAIN201306UV02MFMIMT700711UV01ControlActProcess from,
        final PRPAIN201301UV02MFMIMT700701UV01ControlActProcess to) {

        if (from != null) {
            to.getRealmCode().clear();

            for (final CS realmCode : from.getRealmCode()) {
                to.getRealmCode().add(realmCode);
            }
        }

        return to;
    }

    public static MFMIMT700701UV01InformationRecipient copyTemplateIds(final MFMIMT700711UV01InformationRecipient from,
        final MFMIMT700701UV01InformationRecipient to) {
        if (from != null) {
            to.getTemplateId().clear();

            for (final II id : from.getTemplateId()) {
                to.getTemplateId().add(id);
            }
        }

        return to;
    }

    public static MCCIMT000100UV01AttentionLine copyTemplateIds(final MCCIMT000300UV01AttentionLine from,
        final MCCIMT000100UV01AttentionLine to) {
        if (from != null) {
            to.getTemplateId().clear();

            for (final II id : from.getTemplateId()) {
                to.getTemplateId().add(id);
            }
        }

        return to;
    }

    public static PRPAMT201301UV02Patient copyIIs(final PRPAMT201310UV02Patient from,
        final PRPAMT201301UV02Patient to) {
        if (from != null) {
            to.getId().clear();

            for (final II id : from.getId()) {
                to.getId().add(id);
            }
        }

        return to;
    }
}
