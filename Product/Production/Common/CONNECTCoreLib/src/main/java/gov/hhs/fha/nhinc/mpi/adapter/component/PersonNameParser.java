/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.mpi.adapter.component;

import gov.hhs.fha.nhinc.mpilib.PersonName;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author rayj
 */
public class PersonNameParser {

    private static Log log = LogFactory.getLog(PersonNameParser.class);

    private enum nameorder {

        first_last, last_first
    }

    public static PersonName SplitName(String name) {
        log.debug("Begin PersonNameParser.SplitName(String)");
        PersonName personname;
        if (name == null || name.contentEquals("")) {
            personname = new PersonName();
        } else {
            personname = SplitNameByDelimiter(name, ",", nameorder.last_first);
            if (personname == null) {
                personname = SplitNameByDelimiter(name, " ", nameorder.first_last);
            }

            if (personname == null) {
                {
                    personname = new PersonName();
                    personname.setLastName(name);
                }
            }
        }
        log.debug("End PersonNameParser.SplitName(String)");
        return personname;
    }

    private static PersonName SplitNameByDelimiter(
            String name, String delimiter, nameorder order) {
        log.debug("Begin PersonNameParser.SplitNameByDelimiter(String,String,nameorder)");
        PersonName personname = null;
        if (name.contains(delimiter)) {
            String[] nameparts = name.split(delimiter);
            if (nameparts.length == 0 || nameparts.length == 1) {
                personname = null;
            } else if (nameparts.length == 2) {
                personname = new PersonName();

                if (order == nameorder.first_last) {
                    personname.setFirstName(nameparts[0]);
                    personname.setLastName(nameparts[1]);
                } else if (order == nameorder.last_first) {
                    personname.setLastName(nameparts[0]);
                    personname.setFirstName(nameparts[1]);
                }

            } else if (nameparts.length > 2) {
                personname = new PersonName();
                personname.setLastName(nameparts[0]);
                personname.setFirstName(nameparts[1]);
            }

        } else {
            personname = null;
        }
        log.debug("End PersonNameParser.SplitNameByDelimiter(String,String,nameorder)");
        return personname;
    }

    public static String SerializeName(PersonName personname) {
        log.debug("Begin PersonNameParser.SerializeName(PersonName)");
        String serializedname = null;
        if (personname == null) {
            serializedname = "";
        } else if (personname.getFirstName().contentEquals("")) {
            serializedname = personname.getLastName();
        } else if (personname.getLastName().contentEquals("")) {
            serializedname = personname.getFirstName();
        } else {
            serializedname = personname.getLastName() + "," + personname.getFirstName();
        }
        log.debug("End PersonNameParser.SerializeName(PersonName)");
        return serializedname;
    }
}
