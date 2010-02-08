package gov.hhs.fha.nhinc.subscription.repository.data.test;

import gov.hhs.fha.nhinc.subscription.repository.data.Criterion;
import org.junit.Test;
import static org.junit.Assert.*;
import gov.hhs.fha.nhinc.subscription.repository.data.Criterion;
import org.junit.Ignore;

/**
 * Unit test for criterion class
 * 
 * @author Neil Webb
 */
@Ignore
public class CriterionTest
{
    @Test
    public void testGettersAndSetters()
    {
        System.out.println("Begin testGettersAndSetters");
        try
        {
            String key = "CriterionKey";
            String value = "CriterionValue";
            Criterion crit = new Criterion();
            crit.setKey(key);
            crit.setValue(value);

            assertEquals("Key", key, crit.getKey());
            assertEquals("Value", value, crit.getValue());
        }
        catch (Throwable t)
        {
            t.printStackTrace();
            fail(t.getMessage());
        }
        System.out.println("End testGettersAndSetters");
    }

    @Test
    public void testEquals()
    {
        System.out.println("Begin testEquals");
        try
        {
            // Equals - both
            Criterion crit1 = new Criterion();
            crit1.setKey("Key1");
            crit1.setValue("Value1");
            Criterion crit2 = new Criterion();
            crit2.setKey("Key1");
            crit2.setValue("Value1");
            assertTrue("Equals - both", crit1.equals(crit2));

            // Equals - only key
            crit1 = new Criterion();
            crit1.setKey("Key1");
            crit2 = new Criterion();
            crit2.setKey("Key1");
            assertTrue("Equals - only key", crit1.equals(crit2));

            // Equals - only value
            crit1 = new Criterion();
            crit1.setValue("Value1");
            crit2 = new Criterion();
            crit2.setValue("Value1");
            assertTrue("Equals - only value", crit1.equals(crit2));

            // Not equal - both populated, key different
            crit1 = new Criterion();
            crit1.setKey("Key1");
            crit1.setValue("Value1");
            crit2 = new Criterion();
            crit2.setKey("Key2");
            crit2.setValue("Value1");
            assertFalse("Not equal - both populated, key different", crit1.equals(crit2));

            // Not equal - both populated, value different
            crit1 = new Criterion();
            crit1.setKey("Key1");
            crit1.setValue("Value1");
            crit2 = new Criterion();
            crit2.setKey("Key1");
            crit2.setValue("Value2");
            assertFalse("Not equal - both populated, value different", crit1.equals(crit2));

            // Not equal - c1 full, c2 key only
            crit1 = new Criterion();
            crit1.setKey("Key1");
            crit1.setValue("Value1");
            crit2 = new Criterion();
            crit2.setKey("Key1");
            assertFalse("Not equal - c1 full, c2 key only", crit1.equals(crit2));

            // Not equal - c1 full, c2 value only
            crit1 = new Criterion();
            crit1.setKey("Key1");
            crit1.setValue("Value1");
            crit2 = new Criterion();
            crit2.setValue("Value1");
            assertFalse("Not equal - c1 full, c2 value only", crit1.equals(crit2));

            // Not equal - c2 full, c1 key only
            crit1 = new Criterion();
            crit1.setKey("Key1");
            crit2 = new Criterion();
            crit2.setKey("Key1");
            crit2.setValue("Value1");
            assertFalse("Not equal - c2 full, c1 key only", crit1.equals(crit2));

            // Not equal - c2 full, c1 value only
            crit1 = new Criterion();
            crit1.setValue("Value1");
            crit2 = new Criterion();
            crit2.setKey("Key1");
            crit2.setValue("Value1");
            assertFalse("Not equal - c2 full, c1 value only", crit1.equals(crit2));

            // Not equal - c1 full, c2 null
            crit1 = new Criterion();
            crit1.setKey("Key1");
            crit1.setValue("Value1");
            crit2 = null;
            assertFalse("Not equal - c1 full, c2 null", crit1.equals(crit2));

        }
        catch (Throwable t)
        {
            t.printStackTrace();
            fail(t.getMessage());
        }
        System.out.println("Begin testEquals");
    }
}