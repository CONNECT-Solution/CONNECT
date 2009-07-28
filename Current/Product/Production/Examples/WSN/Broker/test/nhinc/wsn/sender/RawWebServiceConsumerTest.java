package nhinc.wsn.sender;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author webbn
 */
public class RawWebServiceConsumerTest {

    @Test
    public void testSender()
    {
        System.out.println("Begin testSender");
        try
        {
            RawWebServiceConsumer consumer = new RawWebServiceConsumer();
            String response = consumer.sendRawMessage("from unit test");
            assertNotNull("Response", response);
            System.out.println("Respone received: " + response);
        }
        catch(Throwable t)
        {
            System.out.println("Error in testSender: " + t.getMessage());
            t.printStackTrace();
            fail(t.getMessage());
        }
        System.out.println("End testSender");
    }
  
//    @Test
    public void testDiiSender()
    {
        System.out.println("Begin testDiiSender");
        try
        {
            DiiSinkSender consumer = new DiiSinkSender();
            String response = consumer.sendMessage("from unit test");
            assertNotNull("Response", response);
            System.out.println("Respone received: " + response);
        }
        catch(Throwable t)
        {
            System.out.println("Error in testDiiSender: " + t.getMessage());
            t.printStackTrace();
            fail(t.getMessage());
        }
        System.out.println("End testDiiSender");
    }
}