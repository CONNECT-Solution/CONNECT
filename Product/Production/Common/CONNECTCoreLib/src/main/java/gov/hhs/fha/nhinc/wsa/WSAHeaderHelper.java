/**
 * 
 */
package gov.hhs.fha.nhinc.wsa;

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
            if (identifier.startsWith(prefix))
            {
                return identifier;
            } else {
                return prefix.concat(identifier);
            }
        } else {
            return null;
        }
    }
}
