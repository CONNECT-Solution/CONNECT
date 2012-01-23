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
package gov.hhs.fha.nhinc.patientdiscovery;

import gov.hhs.fha.nhinc.nhinclib.NullChecker;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.II;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.PRPAIN201306UV02MFMIMT700711UV01Subject1;

/**
 *
 * @author JHOPPESC
 */
public class NhinPatientDiscoveryUtils {

    private static Log log = LogFactory.getLog(NhinPatientDiscoveryUtils.class);

   

    /**
     * Extracts first patient id from response message
     *
     * @param msg
     * @return <code>II</code> with extracted patient id
     */
    public static II extractPatientIdFrom201306(PRPAIN201306UV02 msg) {
        II id = new II();

        if (msg != null &&
                msg.getControlActProcess() != null &&
                NullChecker.isNotNullish(msg.getControlActProcess().getSubject()) &&
                msg.getControlActProcess().getSubject().get(0) != null &&
                msg.getControlActProcess().getSubject().get(0).getRegistrationEvent() != null &&
                msg.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1() != null &&
                msg.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient() != null &&
                NullChecker.isNotNullish(msg.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId()) &&
                msg.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0) != null &&
                NullChecker.isNotNullish(msg.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0).getExtension()) &&
                NullChecker.isNotNullish(msg.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0).getRoot())) {
            id.setExtension(msg.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0).getExtension());
            id.setRoot(msg.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0).getRoot());
        } else {
            id = null;
        }

        return id;
    }

    /**
     * Extracts the patient id from response message subject element
     *
     * @param msg
     * @return <code>II</code> with extracted patient id
     */
    public static II extractPatientIdFromSubject(PRPAIN201306UV02MFMIMT700711UV01Subject1 subject) {
        II id = new II();

        if (subject != null &&
                subject.getRegistrationEvent() != null &&
                subject.getRegistrationEvent().getSubject1() != null &&
                subject.getRegistrationEvent().getSubject1().getPatient() != null &&
                NullChecker.isNotNullish(subject.getRegistrationEvent().getSubject1().getPatient().getId()) &&
                subject.getRegistrationEvent().getSubject1().getPatient().getId().get(0) != null &&
                NullChecker.isNotNullish(subject.getRegistrationEvent().getSubject1().getPatient().getId().get(0).getExtension()) &&
                NullChecker.isNotNullish(subject.getRegistrationEvent().getSubject1().getPatient().getId().get(0).getRoot())) {
            id.setExtension(subject.getRegistrationEvent().getSubject1().getPatient().getId().get(0).getExtension());
            id.setRoot(subject.getRegistrationEvent().getSubject1().getPatient().getId().get(0).getRoot());
        } else {
            id = null;
        }

        return id;
    }
}
