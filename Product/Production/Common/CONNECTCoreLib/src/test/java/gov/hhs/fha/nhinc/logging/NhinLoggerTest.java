/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.logging;

import java.io.ByteArrayOutputStream;
import org.apache.log4j.Appender;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Layout;
import org.apache.log4j.SimpleLayout;
import org.apache.log4j.WriterAppender;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author zmelnick
 */
public class NhinLoggerTest {
    private static final String TEST_MESSAGE = "Message that should be in log";
    ByteArrayOutputStream logOutuput = new ByteArrayOutputStream();
    ByteArrayOutputStream errorOutput = new ByteArrayOutputStream();
    ByteArrayOutputStream performanceOutput = new ByteArrayOutputStream();
    ByteArrayOutputStream transactionOutput = new ByteArrayOutputStream();


    public NhinLoggerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        Layout layout = new SimpleLayout();
        Appender logAppender = new WriterAppender(layout, logOutuput);
        Appender errorAppender = new WriterAppender(layout, errorOutput);
        Appender performanceAppender = new WriterAppender(layout, performanceOutput);
        Appender transactionAppender = new WriterAppender(layout, transactionOutput);
        errorAppender.setName("Error");
        performanceAppender.setName("Performance");
        transactionAppender.setName("Transaction");
        BasicConfigurator.configure(logAppender);
        BasicConfigurator.configure(errorAppender);
        BasicConfigurator.configure(performanceAppender);
        BasicConfigurator.configure(transactionAppender);
        
    }

    @After
    public void tearDown() {
    }

    @Test
    public void doesLogTraceLevel() {
        NhinLog log = NhinLogFactory.getLog(NhinLoggerTest.class);
        log.getGeneralLog().trace(TEST_MESSAGE);
        String logMsg = logOutuput.toString();
        assertNotNull(logMsg);
        assertEquals("TRACE - " + TEST_MESSAGE + "\n", logMsg);
    }

    @Test
    public void doesLogDebugLevel() {
        NhinLog log = NhinLogFactory.getLog(NhinLoggerTest.class);
        log.getGeneralLog().debug(TEST_MESSAGE);
        String logMsg = logOutuput.toString();
        assertNotNull(logMsg);
        assertEquals("DEBUG - " + TEST_MESSAGE + "\n", logMsg);
    }
    @Test
    public void doesLogInfoLevel() {
        NhinLog log = NhinLogFactory.getLog(NhinLoggerTest.class);
        log.getGeneralLog().info(TEST_MESSAGE);
        String logMsg = logOutuput.toString();
        assertNotNull(logMsg);
        assertEquals("INFO - " + TEST_MESSAGE + "\n", logMsg);
    }

    @Test
    public void doesLogWarnLevel() {
        NhinLog log = NhinLogFactory.getLog(NhinLoggerTest.class);
        log.getGeneralLog().warn(TEST_MESSAGE);
        String logMsg = logOutuput.toString();
        assertNotNull(logMsg);
        assertEquals("WARN - " + TEST_MESSAGE + "\n", logMsg);
    }

    @Test
    public void doesLogErrorLevel() {
        NhinLog log = NhinLogFactory.getLog(NhinLoggerTest.class);
        log.getGeneralLog().error(TEST_MESSAGE);
        String logMsg = logOutuput.toString();
        assertNotNull(logMsg);
        assertEquals("ERROR - " + TEST_MESSAGE + "\n", logMsg);
    }

    @Test
    public void doesLogFatalLevel() {
        NhinLog log = NhinLogFactory.getLog(NhinLoggerTest.class);
        log.getGeneralLog().fatal(TEST_MESSAGE);
        String logMsg = logOutuput.toString();
        assertNotNull(logMsg);
        assertEquals("FATAL - " + TEST_MESSAGE + "\n", logMsg);
    }

    @Test
    public void doesLogErrorToErrorLog() {
        NhinLog log = NhinLogFactory.getLog(NhinLoggerTest.class);
        log.writeToErrorLog(TEST_MESSAGE);
        String logMsg = errorOutput.toString();
        assertNotNull(logMsg);
        assertEquals("ERROR - " + TEST_MESSAGE + "\n", logMsg);

    }

    @Test
    public void doesLogToPerformanceLog() {
        NhinLog log = NhinLogFactory.getLog(NhinLoggerTest.class);
        log.writeToPerformanceLog(TEST_MESSAGE);
        String logMsg = performanceOutput.toString();
        assertNotNull(logMsg);
        assertEquals("INFO - " + TEST_MESSAGE + "\n", logMsg);
    }

    @Test
    public void doesLogToTransactionLog() {
        NhinLog log = NhinLogFactory.getLog(NhinLoggerTest.class);
        log.writeToTransactionLog(TEST_MESSAGE);
        String logMsg = transactionOutput.toString();
        assertNotNull(logMsg);
        assertEquals("INFO - " + TEST_MESSAGE + "\n", logMsg);
    }

}
