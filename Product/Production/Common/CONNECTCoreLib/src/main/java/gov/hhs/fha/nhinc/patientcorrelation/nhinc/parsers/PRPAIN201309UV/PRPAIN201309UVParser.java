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
package gov.hhs.fha.nhinc.patientcorrelation.nhinc.parsers.PRPAIN201309UV;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.CS;
import org.hl7.v3.II;
import org.hl7.v3.PRPAIN201309UV02;
import org.hl7.v3.PRPAIN201309UV02QUQIMT021001UV01ControlActProcess;
import org.hl7.v3.PRPAMT201307UV02DataSource;
import org.hl7.v3.PRPAMT201307UV02ParameterList;
import org.hl7.v3.PRPAMT201307UV02PatientIdentifier;
import org.hl7.v3.PRPAMT201307UV02QueryByParameter;

/**
 *
 * @author svalluripalli
 */
public class PRPAIN201309UVParser {

    private static Log log = LogFactory.getLog(PRPAIN201309UVParser.class);
    private static String CODE = "CA";
    private static String CNTRL_MODCODE = "EVN";
    private static String CNTRL_CODE = "PRPA_TE201310UV";
    private static String CNTRL_SUBJ_TYPECODE = "SUBJ";
    private static String STATUS_CD = "active";
    private static String CNTRL_SUBJ_EVENT_SUBJ_CLASS_CODE = "PAT";
    private static String PATIENTPERSON_CLASSCODE = "PSN";
    //private static String DETERMINER_CODE = "INSTANCE";
    private static String QUERY_RESPONSE = "OK";

    private static CS getCS(String value) {
        CS cs = new CS();
        cs.setCode(value);
        return cs;
    }

    public static PRPAMT201307UV02ParameterList parseHL7ParameterListFrom201309Message(PRPAIN201309UV02 message) {
        if (message == null) {
            return null;
        }
        PRPAIN201309UV02QUQIMT021001UV01ControlActProcess ctrlAccess = message.getControlActProcess();
        if (ctrlAccess == null) {
            return null;
        }
        JAXBElement<PRPAMT201307UV02QueryByParameter> queryByParameter = ctrlAccess.getQueryByParameter();
        if (queryByParameter == null) {
            return null;
        }
        PRPAMT201307UV02ParameterList parameterList = (queryByParameter.getValue() != null) ? queryByParameter.getValue().getParameterList() : null;
        if (parameterList == null) {
            return null;
        }
        return parameterList;
    }

    public static PRPAMT201307UV02PatientIdentifier parseHL7PatientPersonFrom201309Message(PRPAIN201309UV02 message) {
        log.debug("---- Begin PRPAIN201309UVParser.parseHL7PatientPersonFrom201309Message()----");
        PRPAMT201307UV02ParameterList parameterList = parseHL7ParameterListFrom201309Message(message);
        PRPAMT201307UV02PatientIdentifier patientIdentifier = (parameterList.getPatientIdentifier() != null) ? parameterList.getPatientIdentifier().get(0) : null;
        log.debug("---- End PRPAIN201309UVParser.parseHL7PatientPersonFrom201309Message()----");
        return patientIdentifier;
    }

    public static List<II> buildAssigningAuthorityInclusionFilterList(PRPAIN201309UV02 message) {
        List<II> list = new ArrayList<II>();
        PRPAMT201307UV02ParameterList parameterList = parseHL7ParameterListFrom201309Message(message);
        List<PRPAMT201307UV02DataSource> dataSourceList;
        if (parameterList != null) {
            dataSourceList = parameterList.getDataSource();
            for (PRPAMT201307UV02DataSource dataSource : dataSourceList) {
                for (II dataSourceValue : dataSource.getValue()) {
                    list.add(dataSourceValue);
                }
            }
        }
        return list;
    }

    public static JAXBElement<PRPAMT201307UV02QueryByParameter> ExtractQueryId(PRPAIN201309UV02 message) {
        JAXBElement<PRPAMT201307UV02QueryByParameter> queryByParameter = null;
        if (message.getControlActProcess() != null) {
            queryByParameter = message.getControlActProcess().getQueryByParameter();
        }
        return queryByParameter;
    }
}
