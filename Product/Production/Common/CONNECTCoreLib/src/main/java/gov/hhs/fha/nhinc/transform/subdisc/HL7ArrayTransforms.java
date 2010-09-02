/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.transform.subdisc;
import javax.xml.bind.JAXBElement;
import org.hl7.v3.MFMIMT700701UV01DataEnterer;
import org.hl7.v3.MFMIMT700711UV01DataEnterer;
import org.hl7.v3.QUQIMT021001UV01DataEnterer;
import org.hl7.v3.PRPAIN201305UV02QUQIMT021001UV01ControlActProcess;
import org.hl7.v3.PRPAIN201306UV02MFMIMT700711UV01ControlActProcess;
import org.hl7.v3.PRPAIN201301UV02MFMIMT700701UV01ControlActProcess;
import org.hl7.v3.QUQIMT021001UV01AuthorOrPerformer;
import org.hl7.v3.MFMIMT700701UV01AuthorOrPerformer;
import org.hl7.v3.MFMIMT700711UV01AuthorOrPerformer;
import org.hl7.v3.MFMIMT700711UV01InformationRecipient;
import org.hl7.v3.MFMIMT700701UV01InformationRecipient;
import org.hl7.v3.PRPAIN201301UV02;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.PRPAMT201301UV02Person;
import org.hl7.v3.PRPAMT201306UV02ParameterList;
import org.hl7.v3.MCCIMT000100UV01AttentionLine;
import org.hl7.v3.MCCIMT000300UV01AttentionLine;
import org.hl7.v3.MCCIMT000100UV01Receiver;
import org.hl7.v3.MCCIMT000300UV01Receiver;
import org.hl7.v3.ENExplicit;
import org.hl7.v3.PNExplicit;
import org.hl7.v3.II;
import org.hl7.v3.CS;
import org.hl7.v3.MCCIMT000100UV01Agent;
import org.hl7.v3.MCCIMT000100UV01Device;
import org.hl7.v3.MCCIMT000100UV01Organization;
import org.hl7.v3.PRPAMT201301UV02Patient;
import org.hl7.v3.PRPAMT201310UV02Patient;

/**
 *
 * @author dunnek
 */
public class HL7ArrayTransforms
{

    public static PRPAIN201301UV02 copyNullFlavors(PRPAIN201305UV02 from, PRPAIN201301UV02 to)
    {
        if(from.getNullFlavor() != null)
        {
            to.getNullFlavor().clear();
            for(String item : from.getNullFlavor())
            {
                to.getNullFlavor().add(item);
            }
        }

        return to;
    }
    public static PRPAIN201301UV02 copyNullFlavors(PRPAIN201306UV02 from, PRPAIN201301UV02 to)
    {
        if(from.getNullFlavor() != null)
        {
            to.getNullFlavor().clear();
            for(String item : from.getNullFlavor())
            {
                to.getNullFlavor().add(item);
            }
        }

        return to;
    }
    public static MFMIMT700701UV01InformationRecipient copyNullFlavors(MFMIMT700711UV01InformationRecipient from, MFMIMT700701UV01InformationRecipient to)
    {
        if(from.getNullFlavor() != null)
        {
            to.getNullFlavor().clear();
            for(String item : from.getNullFlavor())
            {
                to.getNullFlavor().add(item);
            }
        }

        return to;
    }
    public static ENExplicit copyNullFlavors(PNExplicit from, ENExplicit to)
    {
        if(from.getNullFlavor() != null)
        {
            to.getNullFlavor().clear();
            for(String item : from.getNullFlavor())
            {
                to.getNullFlavor().add(item);
            }
        }

        return to;
    }
    public static PNExplicit copyNullFlavors(ENExplicit from, PNExplicit to)
    {
        if(from.getNullFlavor() != null)
        {
            to.getNullFlavor().clear();
            for(String item : from.getNullFlavor())
            {
                to.getNullFlavor().add(item);
            }
        }

        return to;
    }

    public static MCCIMT000100UV01AttentionLine copyNullFlavors(MCCIMT000300UV01AttentionLine from, MCCIMT000100UV01AttentionLine to)
    {
        if(from.getNullFlavor() != null)
        {
            to.getNullFlavor().clear();
            for(String item : from.getNullFlavor())
            {
                to.getNullFlavor().add(item);
            }
        }

        return to;
    }
    public static PRPAMT201301UV02Person copyNullFlavors(PRPAMT201306UV02ParameterList from, PRPAMT201301UV02Person to)
    {
        if(from.getNullFlavor() != null)
        {
            to.getNullFlavor().clear();
            for(String item : from.getNullFlavor())
            {
                to.getNullFlavor().add(item);
            }
        }

        return to;
    }

    public static MFMIMT700701UV01DataEnterer copyNullFlavors(MFMIMT700711UV01DataEnterer from, MFMIMT700701UV01DataEnterer to)
    {
        if(from.getNullFlavor() != null)
        {
            to.getNullFlavor().clear();
            for(String item : from.getNullFlavor())
            {
                to.getNullFlavor().add(item);
            }
        }

        return to;
    }
    public static MFMIMT700701UV01DataEnterer copyNullFlavors(QUQIMT021001UV01DataEnterer from, MFMIMT700701UV01DataEnterer to)
    {
        if(from.getNullFlavor() != null)
        {
            to.getNullFlavor().clear();
            for(String item : from.getNullFlavor())
            {
                to.getNullFlavor().add(item);
            }
        }

        return to;
    }


    public static PRPAIN201301UV02MFMIMT700701UV01ControlActProcess copyNullFlavors(PRPAIN201306UV02MFMIMT700711UV01ControlActProcess from, PRPAIN201301UV02MFMIMT700701UV01ControlActProcess to)
    {
        if(from.getNullFlavor() != null)
        {
            to.getNullFlavor().clear();
            for(String item : from.getNullFlavor())
            {
                to.getNullFlavor().add(item);
            }
        }

        return to;
    }
    public static PRPAIN201301UV02MFMIMT700701UV01ControlActProcess copyNullFlavors(PRPAIN201305UV02QUQIMT021001UV01ControlActProcess from, PRPAIN201301UV02MFMIMT700701UV01ControlActProcess to)
    {
        if(from.getNullFlavor() != null)
        {
            to.getNullFlavor().clear();
            for(String item : from.getNullFlavor())
            {
                to.getNullFlavor().add(item);
            }
        }

        return to;
    }


    public static MFMIMT700701UV01AuthorOrPerformer copyNullFlavors(MFMIMT700711UV01AuthorOrPerformer from, MFMIMT700701UV01AuthorOrPerformer to)
    {
        if(from.getNullFlavor() != null)
        {
            to.getNullFlavor().clear();
            for(String item : from.getNullFlavor())
            {
                to.getNullFlavor().add(item);
            }
        }

        return to;
    }

    public static MFMIMT700701UV01AuthorOrPerformer copyNullFlavors(QUQIMT021001UV01AuthorOrPerformer from, MFMIMT700701UV01AuthorOrPerformer to)
    {
        if(from.getNullFlavor() != null)
        {
            to.getNullFlavor().clear();
            for(String item : from.getNullFlavor())
            {
                to.getNullFlavor().add(item);
            }
        }

        return to;
    }


    public static PRPAIN201301UV02 copyMCCIMT000100UV01AttentionLine(PRPAIN201306UV02 from, PRPAIN201301UV02 to)
    {
        if (to == null)
        {
            to = new PRPAIN201301UV02();
        }
        if(from.getAttentionLine() != null)
        {
            to.getAttentionLine().clear();
            for(MCCIMT000300UV01AttentionLine line : from.getAttentionLine())
            {

                to.getAttentionLine().add(copyAttentionLine(line));
            }
        }

        return to;
    }

    private static MCCIMT000100UV01AttentionLine copyAttentionLine(MCCIMT000300UV01AttentionLine line)
    {
        MCCIMT000100UV01AttentionLine result = null;

        if (line != null)
        {
            result.setKeyWordText(line.getKeyWordText());
            result.setTypeId(line.getTypeId());
            result.setValue(line.getValue());

            result = copyNullFlavors(line, result);
            result = copyRealmCodes(line, result);
            result = copyTemplateIds(line, result);
        }

        return result;
    }
    public static PRPAIN201301UV02 copyMCCIMT000100UV01AttentionLine(PRPAIN201305UV02 from, PRPAIN201301UV02 to)
    {
        if (to == null)
        {
            to = new PRPAIN201301UV02();
        }
        if(from.getAttentionLine() != null)
        {
            to.getAttentionLine().clear();
            for(MCCIMT000100UV01AttentionLine line : from.getAttentionLine())
            {
                to.getAttentionLine().add(line);
            }
        }

        return to;
    }

    public static PRPAIN201301UV02 copyMCCIMT000100UV01Receiver(PRPAIN201306UV02 from, PRPAIN201301UV02 to)
    {
        if (to == null)
        {
            to = new PRPAIN201301UV02();
        }
        if(from.getReceiver() !=null)
        {
            to.getReceiver().clear();
            for(MCCIMT000300UV01Receiver rec : from.getReceiver())
            {                
                to.getReceiver().add(copyReceiver(rec));
            }
        }

        return to;
    }
    private static MCCIMT000100UV01Receiver copyReceiver(MCCIMT000300UV01Receiver orig)
    {
        MCCIMT000100UV01Receiver result = null;
        MCCIMT000100UV01Device newDevice = new MCCIMT000100UV01Device();

        if(orig != null)
        {
            result = new MCCIMT000100UV01Receiver();
            newDevice.setDesc(orig.getDevice().getDesc());
            newDevice.setClassCode(orig.getDevice().getClassCode());
            newDevice.setDeterminerCode(orig.getDevice().getDeterminerCode());
            newDevice.setExistenceTime(orig.getDevice().getExistenceTime());
            newDevice.setManufacturerModelName(orig.getDevice().getManufacturerModelName());
            newDevice.setSoftwareName(orig.getDevice().getSoftwareName());
            newDevice.setTypeId(orig.getDevice().getTypeId());

            MCCIMT000100UV01Agent agent = new MCCIMT000100UV01Agent();
            MCCIMT000100UV01Organization org = new MCCIMT000100UV01Organization();
            org.setClassCode(HL7Constants.ORG_CLASS_CODE);
            org.setDeterminerCode(HL7Constants.RECEIVER_DETERMINER_CODE);
            org.getId().add(orig.getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0));

            javax.xml.namespace.QName xmlqnameorg = new javax.xml.namespace.QName("urn:hl7-org:v3", "representedOrganization");
            JAXBElement<MCCIMT000100UV01Organization> orgElem = new JAXBElement<MCCIMT000100UV01Organization>(xmlqnameorg, MCCIMT000100UV01Organization.class, org);
            agent.setRepresentedOrganization(orgElem);
            agent.getClassCode().add(HL7Constants.AGENT_CLASS_CODE);

            javax.xml.namespace.QName xmlqnameagent = new javax.xml.namespace.QName("urn:hl7-org:v3", "asAgent");
            JAXBElement<MCCIMT000100UV01Agent> agentElem = new JAXBElement<MCCIMT000100UV01Agent>(xmlqnameagent, MCCIMT000100UV01Agent.class, agent);
            newDevice.setAsAgent(agentElem);

            if (orig.getDevice() != null && orig.getDevice().getId().size() > 0) {
                II deviceId = orig.getDevice().getId().get(0);
                newDevice.getId().add(deviceId);
            }
            result.setTelecom(orig.getTelecom());
            result.setTypeCode(orig.getTypeCode());
            result.setTypeId(orig.getTypeId());
            result.setDevice(newDevice);
        }


        return result;
    }
    public static PRPAIN201301UV02 copyMCCIMT000100UV01Receiver(PRPAIN201305UV02 from, PRPAIN201301UV02 to)
    {
        if (to == null)
        {
            to = new PRPAIN201301UV02();
        }
        if(from.getReceiver() !=null)
        {
            to.getReceiver().clear();
            for(MCCIMT000100UV01Receiver rec : to.getReceiver())
            {
                to.getReceiver().add(rec);
            }
        }

        return to;
    }
     public static PRPAIN201301UV02 copyRealmCodes(PRPAIN201305UV02 from, PRPAIN201301UV02 to)
     {
        if (from != null)
        {
            to.getRealmCode().clear();

            for(CS realmCode : from.getRealmCode())
            {
                to.getRealmCode().add(realmCode);
            }
        }

        return to;         
     }
     public static PRPAIN201301UV02 copyRealmCodes(PRPAIN201306UV02 from, PRPAIN201301UV02 to)
     {
        if (from != null)
        {
            to.getRealmCode().clear();

            for(CS realmCode : from.getRealmCode())
            {
                to.getRealmCode().add(realmCode);
            }
        }

        return to;
     }
    public static MCCIMT000100UV01AttentionLine copyRealmCodes(MCCIMT000300UV01AttentionLine from, MCCIMT000100UV01AttentionLine to)
    {
        if (from != null)
        {
            to.getRealmCode().clear();

            for(CS realmCode : from.getRealmCode())
            {
                to.getRealmCode().add(realmCode);
            }
        }

        return to;
    }
    public static MFMIMT700701UV01InformationRecipient copyRealmCodes(MFMIMT700711UV01InformationRecipient from, MFMIMT700701UV01InformationRecipient to)
    {
         if (from != null)
        {
            to.getRealmCode().clear();

            for(CS realmCode : from.getRealmCode())
            {
                to.getRealmCode().add(realmCode);
            }
        }

        return to;
    }
    public static MFMIMT700701UV01AuthorOrPerformer copyRealmCodes(QUQIMT021001UV01AuthorOrPerformer from, MFMIMT700701UV01AuthorOrPerformer to)
    {

        if (from != null)
        {
            to.getRealmCode().clear();

            for(CS realmCode : from.getRealmCode())
            {
                to.getRealmCode().add(realmCode);
            }
        }

        return to;
    }
    public static PRPAIN201301UV02MFMIMT700701UV01ControlActProcess copyRealmCodes(PRPAIN201306UV02MFMIMT700711UV01ControlActProcess from, PRPAIN201301UV02MFMIMT700701UV01ControlActProcess to)
    {

        if (from != null)
        {
            to.getRealmCode().clear();

            for(CS realmCode : from.getRealmCode())
            {
                to.getRealmCode().add(realmCode);
            }
        }

        return to;
    }

    public static MFMIMT700701UV01InformationRecipient copyTemplateIds(MFMIMT700711UV01InformationRecipient from, MFMIMT700701UV01InformationRecipient to)
    {
        if (from != null)
        {
            to.getTemplateId().clear();

            for(II id : from.getTemplateId())
            {
                to.getTemplateId().add(id);
            }
        }

        return to;
    }
    public static MCCIMT000100UV01AttentionLine copyTemplateIds(MCCIMT000300UV01AttentionLine from, MCCIMT000100UV01AttentionLine to)
    {
        if (from != null)
        {
            to.getTemplateId().clear();

            for(II id : from.getTemplateId())
            {
                to.getTemplateId().add(id);
            }
        }

        return to;
    }
    public static PRPAMT201301UV02Patient copyIIs(PRPAMT201310UV02Patient from, PRPAMT201301UV02Patient to)
    {
        if (from != null)
        {
            to.getTemplateId().clear();

            for(II id : from.getId())
            {
                to.getId().add(id);
            }
        }

        return to;
    }
}
