/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.common.ftaconfigmanager;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import java.util.List;
import java.util.ArrayList;
import static org.junit.Assert.*;

/**
 *
 * @author dunnek
 */
public class FTAConfigurationHelperTest {

    public FTAConfigurationHelperTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        System.setProperty("nhinc.properties.dir", System.getenv("NHINC_PROPERTIES_DIR"));
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testloadRealConfiguration()
    {
        FTAConfiguration config = FTAConfigurationHelper.loadFTAConfiguration();
        assertEquals(1, config.getInboundChannels().size());
    }

    @Test
    public void testgetChannelByTopic()
    {
        System.out.println("getChannelByTopic");
        List<FTAChannel> channels = new ArrayList<FTAChannel>();
        FTAChannel testChannel = new FTAChannel();
        FTAChannel result;

        testChannel.setLocation("Somewhere");
        testChannel.setTopic("topic");
        testChannel.setType("file");

        channels.add(new FTAChannel());
        channels.add(testChannel);

        result = FTAConfigurationHelper.getChannelByTopic(channels, "topic");

        assertEquals(testChannel,result);
    }
    @Test
    public void testloadConfiguration()
    {
        FTAConfiguration config = FTAConfigurationHelper.loadFTAConfiguration("sampleFTAConfiguration.xml");

        List<FTAChannel> inbound = config.getInboundChannels();
        List<FTAChannel> outbound = config.getOutboundChannels();

        assertEquals(2,inbound.size());
        assertEquals(1,outbound.size());
        System.out.println(inbound.get(0).getTopic());
    }


}