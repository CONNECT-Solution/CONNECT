package gov.hhs.fha.nhinc.event;

import java.util.List;

import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import gov.hhs.fha.nhinc.logging.transaction.dao.TransactionDAO;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;

import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import org.apache.cxf.jaxws.context.WebServiceContextImpl;

public class ContextEventDescriptionBuilder extends BaseEventDescriptionBuilder {

    private WebServiceContext context;

    public ContextEventDescriptionBuilder() {
        this.context = new WebServiceContextImpl();
    }

    @Override
    public void buildMessageId() {
        description.setMessageId(AsyncMessageIdExtractor.getMessageId(context));
    }

    @Override
    public void buildTransactionId() {

        String messageId = AsyncMessageIdExtractor.getMessageId(context);
        String transactionId = null;

        List<String> transactionIdList = AsyncMessageIdExtractor.getAsyncRelatesTo(context);
        if (NullChecker.isNotNullish(transactionIdList)) {
            transactionId = transactionIdList.get(0);
        }

        if ((transactionId == null) && (messageId != null)) {
            transactionId = TransactionDAO.getInstance().getTransactionId(messageId);
        }

        description.setTransactionId(transactionId);
    }

    @Override
    public void buildResponseMsgIdList() {
        MessageContext mContext = context.getMessageContext();
        description.setResponseMsgids((List<String>) mContext.get(NhincConstants.RESPONSE_MESSAGE_ID_LIST_KEY));
    }
    
    

}
