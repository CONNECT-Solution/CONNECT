package gov.hhs.fha.nhinc.auditrepository;

import gov.hhs.fha.nhinc.common.auditlog.DocRetrieveMessageType;
import gov.hhs.fha.nhinc.common.auditlog.DocRetrieveResponseMessageType;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;

public interface AuditRepositoryDocumentRetrieveLogger {

    /**
     * This method will create the generic Audit Log Message from a document retrieve request.
     * 
     * @param message The Document Retrieve Request message to be audit logged.
     * @param direction The direction this message is going (Inbound or Outbound)
     * @param _interface The interface this message is being received/sent on (Entity, Adapter, or Nhin)
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logDocRetrieve(DocRetrieveMessageType message, String direction, String _interface);

    /**
     * This method will create the generic Audit Log Message from a document retrieve request.
     * 
     * @param message The Document Retrieve Request message to be audit logged.
     * @param direction The direction this message is going (Inbound or Outbound)
     * @param _interface The interface this message is being received/sent on (Entity, Adapter, or Nhin)
     * @param responseCommunityID The response Community ID
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logDocRetrieve(DocRetrieveMessageType message, String direction, String _interface,
            String responseCommunityID);

    /**
     * This method will create the generic Audit Log Message from a document retrieve response.
     * 
     * @param message The Document Retrieve Response message to be audit logged.
     * @param direction The direction this message is going (Inbound or Outbound)
     * @param _interface The interface this message is being received/sent on (Entity, Adapter, or Nhin)
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logDocRetrieveResult(DocRetrieveResponseMessageType message, String direction,
            String _interface);

    /**
     * This method will create the generic Audit Log Message from a document retrieve response.
     * 
     * @param message The Document Retrieve Response message to be audit logged.
     * @param direction The direction this message is going (Inbound or Outbound)
     * @param _interface The interface this message is being received/sent on (Entity, Adapter, or Nhin)
     * @param requestCommunityID The Request Community ID
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logDocRetrieveResult(DocRetrieveResponseMessageType message, String direction,
            String _interface, String requestCommunityID);

}