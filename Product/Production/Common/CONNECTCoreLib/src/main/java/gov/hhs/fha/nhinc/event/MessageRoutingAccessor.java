package gov.hhs.fha.nhinc.event;

import java.util.List;

public interface MessageRoutingAccessor {

    abstract String getMessageId();

    abstract String getTransactionId();

    abstract List<String> getResponseMsgIdList();

}