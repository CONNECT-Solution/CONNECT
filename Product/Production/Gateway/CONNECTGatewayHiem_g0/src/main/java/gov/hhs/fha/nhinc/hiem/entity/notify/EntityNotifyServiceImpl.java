/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.hiem.entity.notify;

import gov.hhs.fha.nhinc.hiem.processor.entity.EntityNotifyProcessor;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.NotifyRequestType;
import javax.xml.ws.WebServiceContext;
import gov.hhs.fha.nhinc.hiem.dte.SoapUtil;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import org.oasis_open.docs.wsn.b_2.Notify;

/**
 * 
 * 
 * @author Neil Webb
 */
public class EntityNotifyServiceImpl
{

    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(EntityNotifyServiceImpl.class);

    public AcknowledgementType notify(NotifyRequestType notifyRequest, WebServiceContext context)
    {
        log.debug("EntityNotifyServiceImpl.notify");
        AcknowledgementType ack = new AcknowledgementType();

        try
        {
            String rawNotifyXml = new SoapUtil().extractSoapMessage(context, "notifySoapMessage");

            EntityNotifyProcessor processor = new EntityNotifyProcessor();
            processor.processNotify(notifyRequest.getNotify(), notifyRequest.getAssertion(), rawNotifyXml);
        }
        catch (Throwable t)
        {
            log.error("Exception encountered processing notify message: " + t.getMessage(), t);
        // TODO: RETHROW!!!!
        }

        return ack;
    }

    public AcknowledgementType notify(Notify notifyRequest, WebServiceContext context)
    {
        log.debug("EntityNotifyServiceImpl.notify");
        AcknowledgementType ack = new AcknowledgementType();

        try
        {
            String rawNotifyXml = new SoapUtil().extractSoapMessage(context, "notifySoapMessage");

            EntityNotifyProcessor processor = new EntityNotifyProcessor();
            processor.processNotify(notifyRequest, SamlTokenExtractor.GetAssertion(context), rawNotifyXml);
        }
        catch (Throwable t)
        {
            log.error("Exception encountered processing notify message: " + t.getMessage(), t);
        // TODO: RETHROW!!!!
        }

        return ack;
    }
}
