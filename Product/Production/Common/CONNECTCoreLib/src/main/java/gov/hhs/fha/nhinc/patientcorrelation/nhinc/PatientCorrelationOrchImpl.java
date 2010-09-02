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

package gov.hhs.fha.nhinc.patientcorrelation.nhinc;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.dao.CorrelatedIdentifiersDao;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.model.CorrelatedIdentifiers;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.model.QualifiedPatientIdentifier;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.parsers.PRPAIN201309UV.PRPAIN201309UVParser;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.ack.AckBuilder;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.config.ConfigurationManager;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.config.Expiration;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.config.ExpirationConfiguration;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.parsers.PRPAIN201301UV.PRPAIN201301UVParser;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.parsers.PRPAIN201309UV.PixRetrieveResponseBuilder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.hl7.v3.AddPatientCorrelationResponseType;
import org.hl7.v3.II;
import org.hl7.v3.PRPAIN201301UV02;
import org.hl7.v3.PRPAIN201309UV02;
import org.hl7.v3.PRPAIN201310UV02;
import org.hl7.v3.PRPAMT201301UV02Patient;
import org.hl7.v3.PRPAMT201307UV02DataSource;
import org.hl7.v3.PRPAMT201307UV02ParameterList;
import org.hl7.v3.PRPAMT201307UV02PatientIdentifier;
import org.hl7.v3.RetrievePatientCorrelationsResponseType;

/**
 *
 * @author jhoppesc
 */
public class PatientCorrelationOrchImpl {
    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(PatientCorrelationOrchImpl.class);

    public RetrievePatientCorrelationsResponseType retrievePatientCorrelations(PRPAIN201309UV02 retrievePatientCorrelationsRequest, AssertionType assertion) {
        PRPAMT201307UV02PatientIdentifier patIdentifier = PRPAIN201309UVParser.parseHL7PatientPersonFrom201309Message(retrievePatientCorrelationsRequest);
        if (patIdentifier == null) {
            return null;
        }
        List<II> listII = patIdentifier.getValue();
        if (listII == null) {
            log.warn("patient identifier was null");
            return null;
        }
        if (listII.get(0) == null) {
            log.warn("patient identifier(0) was null");
            return null;
        }

        II inputPatientId = listII.get(0);

        List<String> dataSourceList = extractDataSourceList(retrievePatientCorrelationsRequest);


        CorrelatedIdentifiersDao dao = new CorrelatedIdentifiersDao();
        QualifiedPatientIdentifier inputQualifiedPatientIdentifier = QualifiedPatientIdentifierFactory(inputPatientId);

        //only non-expired patient correlation records will be returned
        //expired correlation records will be removed by the following call.
        List<QualifiedPatientIdentifier> qualifiedPatientIdentifiers = dao.retrievePatientCorrelation(inputQualifiedPatientIdentifier , dataSourceList);

        List<II> iiList = buildList(qualifiedPatientIdentifiers);
        PRPAIN201310UV02 IN201310 = PixRetrieveResponseBuilder.createPixRetrieveResponse(retrievePatientCorrelationsRequest, iiList);

        RetrievePatientCorrelationsResponseType result = new RetrievePatientCorrelationsResponseType();
        result.setPRPAIN201310UV02(IN201310);

        return result;
    }

    public AddPatientCorrelationResponseType addPatientCorrelation(PRPAIN201301UV02 addPatientCorrelationRequest, AssertionType assertion) {


        PRPAMT201301UV02Patient patient = PRPAIN201301UVParser.ParseHL7PatientPersonFrom201301Message(addPatientCorrelationRequest);
        String patientId = "";
        String patientAssigningAuthId = "";
        String correlatedPatientId = "";
        String correlatedPatientAssigningAuthId = "";

        if (patient == null) {
            log.warn("Patient was null");
            return null;
        }
        List<II> ids = patient.getId();
        if (ids == null) {
            log.warn("id's were null");
            return null;
        }
        if (ids.get(0) == null) {
            log.warn("id(0) was null");
            return null;
        }
        if (ids.get(1) == null) {
            log.warn("id(1) was null");
            return null;
        }
        patientId = ids.get(0).getExtension();
        if (patientId != null && !patientId.equals("")) {
        } else {
            log.warn("patient id was not supplied");
            return null;
        }
        patientAssigningAuthId = ids.get(0).getRoot();
        if (patientAssigningAuthId != null && !patientAssigningAuthId.equals("")) {
        } else {
            log.warn("patient assigning authority was not supplied");
            return null;
        }
        correlatedPatientId = ids.get(1).getExtension();
        if (correlatedPatientId != null & !correlatedPatientId.equals("")) {
        } else {
            log.warn("correlatedPatientId was not supplied");
            return null;
        }
        correlatedPatientAssigningAuthId = ids.get(1).getRoot();
        if (correlatedPatientAssigningAuthId != null && !correlatedPatientAssigningAuthId.equals("")) {
        } else {
            log.warn("correlatedPatientId assigning authority was not supplied");
            return null;
        }
        if (patientAssigningAuthId.equals(correlatedPatientAssigningAuthId) && patientId.equals(correlatedPatientId)) {
            log.warn("patient was self-correlated");
            return null;
        }

        //calculate the correlation expiration date
        Date newExpirationDate = calculateCorrelationExpirationDate(correlatedPatientAssigningAuthId);

        CorrelatedIdentifiers correlatedIdentifers = new CorrelatedIdentifiers();
        correlatedIdentifers.setCorrelatedPatientAssigningAuthorityId(correlatedPatientAssigningAuthId);
        correlatedIdentifers.setCorrelatedPatientId(correlatedPatientId);
        correlatedIdentifers.setPatientId(patientId);
        correlatedIdentifers.setPatientAssigningAuthorityId(patientAssigningAuthId);
        correlatedIdentifers.setCorrelationExpirationDate(newExpirationDate);
        CorrelatedIdentifiersDao dao = new CorrelatedIdentifiersDao();
        dao.addPatientCorrelation(correlatedIdentifers);


        AddPatientCorrelationResponseType result = new AddPatientCorrelationResponseType();
        result.setMCCIIN000002UV01(AckBuilder.BuildAck(addPatientCorrelationRequest));
        return result;
    }

    private static List<String> extractDataSourceList(PRPAIN201309UV02 IN201309) {
        List<String> dataSourceStringList = new ArrayList<String>();
        PRPAMT201307UV02ParameterList parameterList = PRPAIN201309UVParser.parseHL7ParameterListFrom201309Message(IN201309);
        List<PRPAMT201307UV02DataSource> dataSources = parameterList.getDataSource();

        if (dataSources != null) {
            for (PRPAMT201307UV02DataSource datasource : dataSources) {
                for (II value : datasource.getValue()) {
                    dataSourceStringList.add(value.getRoot());
                }
            }
        }

        return dataSourceStringList;
    }

    private static List<II> buildList(List<QualifiedPatientIdentifier> qualifiedPatientIdentifiers) {
        if (qualifiedPatientIdentifiers == null) {
            return null;
        }
        List<II> iiList = new ArrayList<II>();

        for (QualifiedPatientIdentifier qualifiedPatientIdentifier : qualifiedPatientIdentifiers) {
            iiList.add(IIFactory(qualifiedPatientIdentifier));
        }
        return iiList;
    }

    private static II IIFactory(QualifiedPatientIdentifier qualifiedPatientIdentifier) {
        II ii = new II();
        ii.setRoot(qualifiedPatientIdentifier.getAssigningAuthorityId());
        ii.setExtension(qualifiedPatientIdentifier.getPatientId());
        return ii;
    }

    private static QualifiedPatientIdentifier QualifiedPatientIdentifierFactory(II ii) {
        QualifiedPatientIdentifier qualifiedPatientIdentifier = new QualifiedPatientIdentifier();
        qualifiedPatientIdentifier.setAssigningAuthority(ii.getRoot());
        qualifiedPatientIdentifier.setPatientId(ii.getExtension());
        return qualifiedPatientIdentifier;
    }

    public static Date calculateCorrelationExpirationDate(String assigningAuthority) {
        ExpirationConfiguration pcConfig;

        pcConfig = new ConfigurationManager().loadExpirationConfiguration();
        log.debug("assigningAuthorityId = " + assigningAuthority);

        Expiration exp = new ConfigurationManager().loadConfiguration(pcConfig, assigningAuthority);
        return calculateCorrelationExpirationDate(exp);

    }

    public static Date calculateCorrelationExpirationDate(Expiration config) {

        Date result = null;

        if (config != null) {
            log.debug(" Expiration = " + config.getDuration());
            try {
                result = getExpirationDate(config.getUnits(), config.getDuration());
            } catch (Exception ex) {
                log.error(ex.getMessage(), ex);
                result = null;
            }
        }

        log.debug("Expiration Date = " + result);
        return result;

    }

    private static Date getExpirationDate(String expirationUnits, int expiration) throws Exception {
        Calendar calendar = Calendar.getInstance();

        if ("YEAR".equalsIgnoreCase(expirationUnits)) {
            calendar.add(Calendar.YEAR, expiration);
        } else if ("MONTH".equalsIgnoreCase(expirationUnits)) {
            calendar.add(Calendar.MONTH, expiration);
        } else if ("WEEK".equalsIgnoreCase(expirationUnits)) {
            calendar.add(Calendar.WEEK_OF_YEAR, expiration);
        } else if ("DAY".equalsIgnoreCase(expirationUnits)) {
            calendar.add(Calendar.DAY_OF_YEAR, expiration);
        } else if ("HOUR".equalsIgnoreCase(expirationUnits)) {
            calendar.add(Calendar.HOUR_OF_DAY, expiration);
        } else if ("MINUTE".equalsIgnoreCase(expirationUnits)) {
            calendar.add(Calendar.MINUTE, expiration);
        } else if ("SECOND".equalsIgnoreCase(expirationUnits)) {
            calendar.add(Calendar.SECOND, expiration);
        } else {
            throw new Exception("Invalid Expiration Units");
        }

        return calendar.getTime();

    }

}
