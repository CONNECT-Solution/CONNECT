/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.async;

import com.sun.xml.ws.api.message.HeaderList;
import com.sun.xml.ws.developer.JAXWSProperties;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author JHOPPESC
 */
public class AsyncMessageIdExtractor {

    public static String GetAsyncMessageId(WebServiceContext context) {
        String messageId = null;

        HeaderList hl = (HeaderList) context.getMessageContext().get(JAXWSProperties.INBOUND_HEADER_LIST_PROPERTY);

        // Extract the message id from the NHIN header
        messageId = hl.get(NhincConstants.NS_ADDRESSING_2005, NhincConstants.HEADER_MESSAGEID, false).getStringContent();
        
        return messageId;
    }

    public static String GetAsyncRelatesTo(WebServiceContext context) {
        String relatesToId = null;

        HeaderList hl = (HeaderList) context.getMessageContext().get(JAXWSProperties.INBOUND_HEADER_LIST_PROPERTY);

        // Extract the relates to relates to id from the NHIN header
        relatesToId = hl.get(NhincConstants.NS_ADDRESSING_2005, NhincConstants.HEADER_RELATESTO, false).getStringContent();

        return relatesToId;
    }

}
