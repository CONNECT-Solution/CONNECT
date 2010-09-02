/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.patientcorrelation.nhinc.parsers.helpers;

import org.hl7.v3.TSExplicit;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author rayj
 */
public class CreationTimeHelper {
    public static final String DateFormat = "yyyyMMDDhhmmss";
    public static TSExplicit getCreationTime() {
        TSExplicit time = new TSExplicit();
        String formattedTime;
        try {
            Calendar cal = Calendar.getInstance();
            Date now = new Date();
            String dateString = now.toString();
            SimpleDateFormat sdf = new SimpleDateFormat(DateFormat);

            formattedTime = sdf.format(now);
            time.setValue(formattedTime);
        } catch (Exception ex) {
            System.out.print(ex);
        }

        return time;
    }

}
