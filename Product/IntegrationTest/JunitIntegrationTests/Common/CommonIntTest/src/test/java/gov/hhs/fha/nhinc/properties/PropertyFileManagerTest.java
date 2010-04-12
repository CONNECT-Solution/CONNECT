package gov.hhs.fha.nhinc.properties;

import java.util.Properties;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This unit test is used to test the FileManagerTest class.
 * 
 * @author Les Westberg
 */
public class PropertyFileManagerTest
{

    public PropertyFileManagerTest()
    {
    }

    @BeforeClass
    public static void setUpClass() throws Exception
    {
    }

    @AfterClass
    public static void tearDownClass() throws Exception
    {
    }

    @Before
    public void setUp()
    {
    }

    @After
    public void tearDown()
    {
    }

    /**
     * Test of writePropertyFile method, of class PropertyFileManager.
     */
    @Ignore //Refactor this test to Integration Test project
    @Test
    public void testWritePropertyFile() throws Exception
    {
        System.out.println("writePropertyFile");
        String sPropertyFile = "testprops";
        Properties oProps = new Properties();
        oProps.setProperty("CacheRefreshDuration", "0");
        oProps.setProperty("Prop1", "Value1");
        oProps.setProperty("Prop2", "Value2");
        PropertyFileManager.writePropertyFile(sPropertyFile, oProps);
        
        String sValue = PropertyAccessor.getProperty(sPropertyFile, "CacheRefreshDuration");
        assertEquals("0", sValue);
        
        sValue = PropertyAccessor.getProperty(sPropertyFile, "Prop1");
        assertEquals("Value1", sValue);
        
        sValue = PropertyAccessor.getProperty(sPropertyFile, "Prop2");
        assertEquals("Value2", sValue);
        
        oProps.setProperty("Prop3", "Value3");
        PropertyFileManager.writePropertyFile(sPropertyFile, oProps);
        
        sValue = PropertyAccessor.getProperty(sPropertyFile, "CacheRefreshDuration");
        assertEquals("0", sValue);
        
        sValue = PropertyAccessor.getProperty(sPropertyFile, "Prop1");
        assertEquals("Value1", sValue);
        
        sValue = PropertyAccessor.getProperty(sPropertyFile, "Prop2");
        assertEquals("Value2", sValue);

        sValue = PropertyAccessor.getProperty(sPropertyFile, "Prop3");
        assertEquals("Value3", sValue);
        
        PropertyFileManager.deletePropertyFile(sPropertyFile);
        
    }
}