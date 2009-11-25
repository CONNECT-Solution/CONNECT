package gov.hhs.fha.nhinc.genericheaderhandler;

import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import java.util.Collections;
import java.util.Set;
import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

/**
 *
 * @author rayj
 */
public class SoapHeaderHandler implements SOAPHandler<SOAPMessageContext>
{

    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(SoapHeaderHandler.class);

    public Set<QName> getHeaders()
    {
        log.debug("SoapHeaderHandler.getHeaders");
        return Collections.EMPTY_SET;
    }

    public boolean handleMessage(SOAPMessageContext context)
    {
        log.debug("SoapHeaderHandler.handleMessage");
        new gov.hhs.fha.nhinc.hiem.dte.SoapUtil().extractReferenceParameters(context, NhincConstants.HTTP_REQUEST_ATTRIBUTE_SOAPMESSAGE);
        return true;
    }

    public boolean handleFault(SOAPMessageContext context)
    {
        log.warn("SoapHeaderHandler.handleFault");
        return true;
    }

    public void close(MessageContext context)
    {
        log.debug("SoapHeaderHandler.close");
    }
}
