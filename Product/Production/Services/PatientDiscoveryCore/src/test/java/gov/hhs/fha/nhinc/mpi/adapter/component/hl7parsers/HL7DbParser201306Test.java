package gov.hhs.fha.nhinc.mpi.adapter.component.hl7parsers;

import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class HL7DbParser201306Test {

    @Before
    public void setUp() throws Exception {
        DateTime testCurrentTime = new DateTime(2014, 5, 13, 14, 15, 30, 2, DateTimeZone.forOffsetHours(-7));
        DateTimeUtils.setCurrentMillisFixed(testCurrentTime.getMillis());
    }

    @After
    public void tearDown() throws Exception {
        DateTimeUtils.setCurrentMillisSystem();
    }

    //see http://connect-forums.3294226.n2.nabble.com/NwHIN-PD-can-have-invalid-creation-time-td7579571.html#a7579572
    @Test
    public void creationTimeShouldHaveCorrectFormat() {
        assertEquals("20140513211530", HL7DbParser201306.buildCreationTimeString());
    }


}