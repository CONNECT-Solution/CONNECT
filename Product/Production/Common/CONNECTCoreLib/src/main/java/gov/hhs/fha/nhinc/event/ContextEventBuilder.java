package gov.hhs.fha.nhinc.event;

import java.util.List;

import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import gov.hhs.fha.nhinc.logging.transaction.dao.TransactionDAO;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;

import javax.xml.ws.WebServiceContext;

import org.apache.cxf.jaxws.context.WebServiceContextImpl;

public abstract class ContextEventBuilder extends BaseEventBuilder {

    
    protected WebServiceContext context;
    public ContextEventBuilder() {
        context = new WebServiceContextImpl();
    }
    
    @Override
    public Event getEvent() {
        return event;
    }

    @Override
    public void buildMessageID() {
        event.setMessageID(getMessageId(context));
    }

    @Override
    public void buildTransactionID() {
       event.setTransactionID(getTransactionID(context));
    }
    
    private String getMessageId(WebServiceContext context) {
        return AsyncMessageIdExtractor.getMessageId(context);
    }
    
    private String getTransactionID(WebServiceContext context) {
        String transactionId = null;
        String messageId = getMessageId(context);

        List<String> transactionIdList = AsyncMessageIdExtractor.getAsyncRelatesTo(context);
        if (NullChecker.isNotNullish(transactionIdList)) {
            transactionId = transactionIdList.get(0);
        }

        if ((transactionId == null) && (messageId != null)) {
            transactionId = TransactionDAO.getInstance().getTransactionId(messageId);
        }

        return transactionId;
    }
    
    
}
