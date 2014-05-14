package gov.hhs.fha.nhinc.mpi.adapter.component.hl7parsers;

import org.joda.time.DateTime;
import org.junit.Test;

import static org.junit.Assert.*;

public class HL7DbParser201306Test {

    //see http://connect-forums.3294226.n2.nabble.com/NwHIN-PD-can-have-invalid-creation-time-td7579571.html#a7579572
    @Test
    public void creationTimeShouldHaveCorrectFormat() {
        DateTime creationTimeDate = new DateTime(2014, 5, 14, 1, 15, 30, 2);
        assertEquals("20140514011530", HL7DbParser201306.buildCreationTimeString(creationTimeDate.toDate()));
    }


}