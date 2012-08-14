/**
 * 
 */
package gov.hhs.fha.nhinc.logging.transaction;


import java.util.HashMap;
import java.util.Map;

/**
 * @author bhumphrey
 *
 */
public class TransactionIdMap {
    
    private  Map<String, String> internalMap;
    private static TransactionIdMap INSTANCE = new TransactionIdMap();
    
    private TransactionIdMap() {
        internalMap = new HashMap<String, String>();
    }
    
    public static TransactionIdMap getInstance() {
        return INSTANCE;
    }
    
    
    public String getTransactionId(String messageId) {
        return internalMap.get(messageId);
    }
    
    public void storeTransactionId(String messageId, String transactionId) {
        internalMap.put(messageId, transactionId);
    }

}
