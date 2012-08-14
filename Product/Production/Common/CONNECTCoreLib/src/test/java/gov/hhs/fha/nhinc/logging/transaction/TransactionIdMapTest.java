/**
 * 
 */
package gov.hhs.fha.nhinc.logging.transaction;

import static org.junit.Assert.*;


import org.junit.Before;
import org.junit.Test;

/**
 * @author bhumphrey
 *
 */
public class TransactionIdMapTest {
    
    @Before
    public void setupMap() {
        TransactionIdMap.getInstance().storeTransactionId("AAAA-AAAA-AAAA-AAAA", "ZZZZ-ZZZZ-ZZZZ-ZZZZ");
        TransactionIdMap.getInstance().storeTransactionId("BBBB-BBBB-BBBB-BBBB", "ZZZZ-ZZZZ-ZZZZ-ZZZZ");
        TransactionIdMap.getInstance().storeTransactionId("CCCC-CCCC-CCCC-CCCC", "YYYY-YYYY-YYYY-YYYY");
        
    }
    
    @Test
    public void simpleMapping() {
        assertEquals("'AAAA-AAAA-AAAA-AAAA' has transaction id of 'ZZZZ-ZZZZ-ZZZZ-ZZZZ'", "ZZZZ-ZZZZ-ZZZZ-ZZZZ", TransactionIdMap.getInstance().getTransactionId("AAAA-AAAA-AAAA-AAAA"));
    }
    
    @Test
    public void noTransactionMapping() {
        assertNull("'DDDD-DDDD-DDDD-DDDD' isn't part of a transaction", TransactionIdMap.getInstance().getTransactionId("DDDD-DDDD-DDDD-DDDD"));
    }
    
    
    @Test
    public void transactionIdResuse() {
        TransactionIdMap.getInstance().storeTransactionId("AAAA-AAAA-AAAA-AAAA", "XXXX-XXXX-XXXX-XXXX");
        assertEquals("'AAAA-AAAA-AAAA-AAAA' has transaction id of 'XXXX-XXXX-XXXX-XXXX'", "XXXX-XXXX-XXXX-XXXX", TransactionIdMap.getInstance().getTransactionId("AAAA-AAAA-AAAA-AAAA"));
    }

    

}
