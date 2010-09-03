/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.hiem.nhin.unsubscribe;

import gov.hhs.fha.nhinc.hiem.dte.SoapUtil;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPHeader;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author webbn
 */
public class HiemUnsubscribeSoapHeaderHandler implements SOAPHandler<SOAPMessageContext> {

    private static Log log = LogFactory.getLog(HiemUnsubscribeSoapHeaderHandler.class);

    @SuppressWarnings("unchecked")
    public Set<QName> getHeaders() {
        return Collections.EMPTY_SET;
    }

    public boolean handleMessage(SOAPMessageContext context) {
        new SoapUtil().extractReferenceParameters(context, NhincConstants.HIEM_UNSUBSCRIBE_SOAP_HDR_ATTR_TAG);
        return true;

    }

    public boolean handleFault(SOAPMessageContext context) {
        return true;
    }

    public void close(MessageContext context) {
    }
}
