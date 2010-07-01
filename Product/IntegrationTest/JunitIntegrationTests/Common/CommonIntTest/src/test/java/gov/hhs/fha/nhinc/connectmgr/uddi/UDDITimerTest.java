/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.connectmgr.uddi;

import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Properties;
import org.apache.commons.logging.Log;
import org.jmock.Expectations;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import static org.junit.Assert.*;

/**
 *
 * @author vvickers
 */
public class UDDITimerTest {

    private static final String UDDI_SWITCH_PROPERTY = "UDDIRefreshActive";
    private static Properties origProperties = new Properties();
    private static String gatewayPropFileName;
    private static String gatewayPropFileBackupName;
    Mockery context = new Mockery() {

        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };
    final Log mockLog = context.mock(Log.class);
    UDDITimerTask timer = new UDDITimerTask() {

        @Override
        protected Log createLogger() {
            return mockLog;
        }

        @Override
        protected boolean isLogEnabled() {
            return true;
        }

        @Override
        protected void forceRefreshUDDIFile() {
            // no action taken
            }
    };

    public UDDITimerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        System.setProperty("nhinc.properties.dir", System.getenv("NHINC_PROPERTIES_DIR"));

        // Create the property file backup
        gatewayPropFileName = PropertyAccessor.getPropertyFileLocation() + "gateway.properties";
        gatewayPropFileBackupName = gatewayPropFileName + "_backup";

        copyFile(gatewayPropFileName, gatewayPropFileBackupName);

        // Read property file and save off the original settings
        gatewayPropFileName = PropertyAccessor.getPropertyFileLocation() + "gateway.properties";
        File fPropFile = new File(gatewayPropFileName);
        assertTrue(gatewayPropFileName + " does not exist.", fPropFile.exists());
        if (fPropFile.exists()) {
            FileReader frPropFile = null;
            try {
                frPropFile = new FileReader(fPropFile);
                origProperties.load(frPropFile);
            } catch (IOException exio) {
                fail("Unable to read properties " + gatewayPropFileName + ": " + exio.getMessage());
            } finally {
                try {
                    if (frPropFile != null) {
                        frPropFile.close();
                    }
                } catch (IOException ex) {
                    fail("Unable to close properties " + gatewayPropFileName + ": " + ex.getMessage());
                }
            }
        }

    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        // Rewrite original properties
        copyFile(gatewayPropFileBackupName, gatewayPropFileName);
        File backup = new File(gatewayPropFileBackupName);
        backup.delete();
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testUDDINonInvocation() {

        try {
            FileWriter fwInvoke = new FileWriter(gatewayPropFileName);
            Properties invokeProps = new Properties();
            invokeProps.putAll(origProperties);
            invokeProps.setProperty(UDDI_SWITCH_PROPERTY, "false");
            invokeProps.store(fwInvoke, "");

            PropertyAccessor.forceRefresh("gateway");

            // Set expectations
            context.checking(new Expectations() {

                {
                    allowing(mockLog).isDebugEnabled();
                    one(mockLog).debug("UDDITimerTask is disabled by the UDDIRefreshActive property.");
                }
            });

            timer.run();
            context.assertIsSatisfied();

        } catch (PropertyAccessException ex) {
            fail("Unable to refresh properties cache " + gatewayPropFileName + ": " + ex.getMessage());
        } catch (IOException ex) {
            fail("Unable to write properties " + gatewayPropFileName + ": " + ex.getMessage());
        }
    }

    @Test
    public void testUDDIInvocation() {

        try {
            FileWriter fwInvoke = new FileWriter(gatewayPropFileName);
            Properties invokeProps = new Properties();
            invokeProps.putAll(origProperties);
            invokeProps.setProperty(UDDI_SWITCH_PROPERTY, "true");
            invokeProps.store(fwInvoke, "");

            PropertyAccessor.forceRefresh("gateway");

            // Set expectations
            context.checking(new Expectations() {

                {
                    allowing(mockLog).isDebugEnabled();
                    one(mockLog).debug("Start: UDDITimerTask.run method - loading from UDDI server.");
                    one(mockLog).debug("Done: UDDITimerTask.run method - loading from UDDI server.");
                }
            });

            timer.run();
            context.assertIsSatisfied();

        } catch (PropertyAccessException ex) {
            fail("Unable to refresh properties cache " + gatewayPropFileName + ": " + ex.getMessage());
        } catch (IOException ex) {
            fail("Unable to write properties " + gatewayPropFileName + ": " + ex.getMessage());
        }
    }

        @Test
    public void testUDDIPropNotFound() {
        try {
            FileWriter fwInvoke = new FileWriter(gatewayPropFileName);
            Properties invokeProps = new Properties();
            invokeProps.store(fwInvoke, "");

            PropertyAccessor.forceRefresh("gateway");

            // Set expectations - PropertyAccessor returns false if the boolean property is not found
            context.checking(new Expectations() {

                {
                    allowing(mockLog).isDebugEnabled();
                    one(mockLog).debug("UDDITimerTask is disabled by the UDDIRefreshActive property.");
                }
            });

            timer.run();
            context.assertIsSatisfied();

        } catch (PropertyAccessException ex) {
            fail("Unable to refresh properties cache " + gatewayPropFileName + ": " + ex.getMessage());
        } catch (IOException ex) {
            fail("Unable to write properties " + gatewayPropFileName + ": " + ex.getMessage());
        }
    }

    private static void copyFile(String source, String destination) {

        FileInputStream fis = null;
        FileOutputStream fos = null;
        FileChannel fci = null;
        FileChannel fco = null;
        try {
            fis = new FileInputStream(source);
            fos = new FileOutputStream(destination);
            fci = fis.getChannel();
            fco = fos.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            while (true) {
                int read = fci.read(buffer);
                if (read == -1) {
                    break;
                }
                buffer.flip();
                fco.write(buffer);
                buffer.clear();
            }
        } catch (IOException ex) {
            fail("Unable to create a backup properties files: " + destination);
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                if (fos != null) {
                    fos.close();
                }
                if (fci != null) {
                    fci.close();
                }
                if (fco != null) {
                    fco.close();
                }
            } catch (IOException ex) {
                fail("Unable to close properties files: " + ex.getMessage());
            }
        }
    }
}
