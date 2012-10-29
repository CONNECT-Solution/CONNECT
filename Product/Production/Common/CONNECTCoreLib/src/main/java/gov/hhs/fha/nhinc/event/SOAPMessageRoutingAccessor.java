package gov.hhs.fha.nhinc.event;

import java.util.List;

import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import gov.hhs.fha.nhinc.logging.transaction.dao.TransactionDAO;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;

import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import org.apache.cxf.jaxws.context.WebServiceContextImpl;

public class SOAPMessageRoutingAccessor implements MessageRoutingAccessor  {

    private WebServiceContext context;

    public SOAPMessageRoutingAccessor() {
        this.context = new WebServiceContextImpl();
    }

    public SOAPMessageRoutingAccessor(WebServiceContext context) {
        this.context = context;
    }

    
    /* (non-Javadoc)
     * @see gov.hhs.fha.nhinc.event.HeaderEvent#getMessageId()
     */
    @Override
    public  String getMessageId() {
        return AsyncMessageIdExtractor.getMessageId(context);
    }

    /* (non-Javadoc)
     * @see gov.hhs.fha.nhinc.event.HeaderEvent#getTransactionId()
     */
    @Override
    public String getTransactionId() {

        String messageId = AsyncMessageIdExtractor.getMessageId(context);
        String transactionId = null;

        List<String> transactionIdList = AsyncMessageIdExtractor.getAsyncRelatesTo(context);
        if (NullChecker.isNotNullish(transactionIdList)) {
            transactionId = transactionIdList.get(0);
        }

        if ((transactionId == null) && (messageId != null)) {
            transactionId = TransactionDAO.getInstance().getTransactionId(messageId);
        }

        return transactionId;
    }

    /* (non-Javadoc)
     * @see gov.hhs.fha.nhinc.event.HeaderEvent#buildResponseMsgIdList()
     */
    @Override
    public List<String> getResponseMsgIdList() {
        MessageContext mContext = context.getMessageContext();
        return (List<String>) mContext.get(NhincConstants.RESPONSE_MESSAGE_ID_LIST_KEY);
    }
    
    

}
