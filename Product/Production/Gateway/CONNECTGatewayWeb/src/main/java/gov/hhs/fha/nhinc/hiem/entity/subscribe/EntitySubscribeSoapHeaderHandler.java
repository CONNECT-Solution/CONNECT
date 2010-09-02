/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.hiem.entity.subscribe;

import java.util.Collections;
import java.util.Set;
import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import gov.hhs.fha.nhinc.hiem.dte.SoapUtil;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;

/**
 *
 *
 * @author Neil Webb
 */
public class EntitySubscribeSoapHeaderHandler implements SOAPHandler<SOAPMessageContext> {

    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(EntitySubscribeSoapHeaderHandler.class);

    @SuppressWarnings("unchecked")
    public Set<QName> getHeaders() {
        return Collections.EMPTY_SET;
    }

    public boolean handleMessage(SOAPMessageContext context) {
        new SoapUtil().extractReferenceParameters(context, NhincConstants.HIEM_SUBSCRIBE_SOAP_HDR_ATTR_TAG);
        return true;
    }

    public boolean handleFault(SOAPMessageContext context) {
        return true;
    }

    public void close(MessageContext context) {
    }
}
