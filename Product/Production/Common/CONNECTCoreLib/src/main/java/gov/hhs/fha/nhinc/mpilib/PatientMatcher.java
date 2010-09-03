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
package gov.hhs.fha.nhinc.mpilib;

import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author rayj
 */
public class PatientMatcher {

    private static Log log = LogFactory.getLog(MiniMpi.class);

    public static boolean IsSearchMatchByIds(Patient possibleMatch, Patient searchParams) {
        //i will call it a match if any one of the ids matches any other ids
        //this is over simplified, but fine for now
        boolean match = false;
        for (Identifier searchParamIdentifier : searchParams.getIdentifiers()) {
            for (Identifier possibleMatchIdentifier : possibleMatch.getIdentifiers()) {
                match = match | IsSearchMatchbyId(searchParamIdentifier, possibleMatchIdentifier);
            }
        }
        return match;
    }

    public static boolean IsSearchMatchbyId(Identifier a, Identifier b) {
        boolean match;
        if (a == null) {
            log.info("a is null");
        }
        if (a.getId() == null) {
            log.info("a.getId() is null");
        }
        if (a.getOrganizationId() == null) {
            log.info("a.getOrganizationId() is null");
        }
        if (b == null) {
            log.info("b is null");
        }
        if (b.getId() == null) {
            log.info("b.getId() is null");
        }
        if (b.getOrganizationId() == null) {
            log.info("b.getOrganizationId() is null");
        }
        match = ((!a.getId().contentEquals("")) && (!b.getId().contentEquals(""))) && (a.getId().contentEquals(b.getId()) && a.getOrganizationId().contentEquals(b.getOrganizationId()));
        return match;
    }
    
    public static boolean isPatientOptedInCriteriaMet(Patient possibleMatch){
        return possibleMatch.isOptedIn();
    }
    
    public static boolean IsSearchMatchByDemographics(Patient possibleMatch, Patient searchParams) {

        PersonName possibleMatchName = null;
        PersonName searchName = null;

        if(possibleMatch.getNames().size() > 0)
        {
            possibleMatchName = possibleMatch.getNames().get(0);
        }
        else
        {
            possibleMatchName = possibleMatch.getName();
        }

        if(searchParams.getNames().size() > 0)
        {
            searchName = searchParams.getNames().get(0);
        }
        else
        {
            searchName = searchParams.getName();
        }

        boolean match = DoesNameMeetSearchCriteria(possibleMatchName, searchName);
        log.debug("[" + SerializePatient(searchParams) + "]==[" + SerializePatient(possibleMatch) + "] -> " + match);
        return match;
    }

    public static boolean IsMatchByDemographics(Patient patient, Patient possibleMatch) {
        return IsSearchMatchByDemographics(patient, possibleMatch);
    }

    private static boolean IsNameNullish(PersonName name) {
        boolean result;
        if (name == null) {
            result = true;
        } else {
            result = (NullChecker.isNullish(name.getFirstName()) && NullChecker.isNullish(name.getLastName()));
        }
        return result;
    }

    private static boolean DoesNameMeetSearchCriteria(PersonName name, PersonName searchName) {
        boolean result;

        if (IsNameNullish(searchName)) {
            log.info("search name nullish");
            result = false;
        } else {
            result = DoesNamePartMeetSearchCriteria(name.getLastName(), searchName.getLastName()) && DoesNamePartMeetSearchCriteria(name.getFirstName(), searchName.getFirstName());
        }
        return result;
    }

    private static boolean DoesNamePartMeetSearchCriteria(String namepart, String searchNamepart) {
        boolean result;
        if (NullChecker.isNullish(searchNamepart)) {
            result = true;
        } else if (NullChecker.isNullish(namepart)) {
            result = false;
        } else {
            result = namepart.equalsIgnoreCase(searchNamepart);
        }
        log.info("DoesNamePartMeetSearchCriteria  " + namepart + "=" + searchNamepart + " ->" + result);
        return result;
    }

    private static String SerializePatient(Patient patient) {
        return patient.toString();
    }
}
