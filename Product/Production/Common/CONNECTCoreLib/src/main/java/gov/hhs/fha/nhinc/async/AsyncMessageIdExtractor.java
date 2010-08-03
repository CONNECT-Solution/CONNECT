/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.async;

import com.sun.xml.ws.api.message.HeaderList;
import com.sun.xml.ws.api.message.Header;
import com.sun.xml.ws.developer.JAXWSProperties;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import java.util.UUID;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author JHOPPESC
 */
public class AsyncMessageIdExtractor
{

    public static String GetAsyncMessageId(WebServiceContext context)
    {
        String messageId = null;

        if (context != null && context.getMessageContext() != null)
        {
            Object oList = context.getMessageContext().get(JAXWSProperties.INBOUND_HEADER_LIST_PROPERTY);
            if (oList instanceof HeaderList)
            {
                HeaderList hlist = (HeaderList) oList;
                Header header = hlist.get(NhincConstants.NS_ADDRESSING_2005, NhincConstants.HEADER_MESSAGEID, false);
                if (header != null)
                {
                    messageId = header.getStringContent();
                }
            }
        }

        if (messageId == null)
        {
            messageId = UUID.randomUUID().toString();
        }
        return messageId;
    }

    public static String GetAsyncRelatesTo(WebServiceContext context)
    {
        String relatesToId = null;

        if (context != null && context.getMessageContext() != null)
        {
            Object oList = context.getMessageContext().get(JAXWSProperties.INBOUND_HEADER_LIST_PROPERTY);
            if (oList instanceof HeaderList)
            {
                HeaderList hlist = (HeaderList) oList;
                Header header = hlist.get(NhincConstants.NS_ADDRESSING_2005, NhincConstants.HEADER_RELATESTO, false);
                if (header != null)
                {
                    relatesToId = header.getStringContent();
                }
            }
        }
        return relatesToId;
    }
}
