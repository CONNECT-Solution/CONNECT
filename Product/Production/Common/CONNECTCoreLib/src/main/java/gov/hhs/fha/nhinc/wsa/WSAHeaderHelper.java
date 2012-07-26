/**
 * 
 */
package gov.hhs.fha.nhinc.wsa;

import java.util.UUID;

/**
 * @author mweaver
 * 
 */
public class WSAHeaderHelper {

    public static final String prefix = "urn:uuid:";

    public WSAHeaderHelper() {

    }

    /**
     * @param identifier
     * @return
     */
    public String addUrnUuid(String identifier) {
        if (identifier != null) {
            if (identifier.startsWith(prefix)) {
                return identifier;
            } else {
                return prefix.concat(identifier);
            }
        } else {
            return null;
        }
    }

    /**
     * Generates a message ID with the correct prefix
     * 
     * @return the message ID
     */
    public String generateMessageID() {        
        return addUrnUuid(UUID.randomUUID().toString());
    }
    
    /**
     * @param messageId
     */
    public String fixMessageIDPrefix(String messageId) {
        if (!hasProperMessageIDPrefix(messageId)) {
            if (hasPrefix(messageId, "uuid:")) {
                messageId = "urn:" + messageId;
            } else {
                messageId = prefix.concat(messageId);
            }
        }

        return messageId;
    }
    
    private boolean hasProperMessageIDPrefix(String messageId) {
        return messageId.trim().startsWith(prefix);
    }

    private boolean hasPrefix(String messageId, String illegalPrefix) {
        return messageId.trim().startsWith(illegalPrefix);
    }
    

}
