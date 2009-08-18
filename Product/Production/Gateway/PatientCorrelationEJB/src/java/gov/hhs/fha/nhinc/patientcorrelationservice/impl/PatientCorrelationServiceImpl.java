/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.patientcorrelationservice.impl;

import gov.hhs.fha.nhinc.patientcorrelation.dao.CorrelatedIdentifiersDao;
import gov.hhs.fha.nhinc.patientcorrelation.model.*;
import gov.hhs.fha.nhinc.patientcorrelationservice.ack.AckBuilder;
import gov.hhs.fha.nhinc.patientcorrelationservice.parsers.PRPAIN201301UV.PRPAIN201301UVParser;
import gov.hhs.fha.nhinc.patientcorrelationservice.parsers.PRPAIN201303UV.PRPAIN201303UVParser;
import gov.hhs.fha.nhinc.patientcorrelationservice.parsers.PRPAIN201309UV.PRPAIN201309UVParser;
import gov.hhs.fha.nhinc.patientcorrelationservice.parsers.PRPAIN201309UV.PixRetrieveResponseBuilder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.hl7.v3.*;

/**
 *
 * @author svalluripalli
 */
public class PatientCorrelationServiceImpl {

    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(PatientCorrelationServiceImpl.class);

    public static RetrievePatientCorrelationsSecuredResponseType retrievePatientCorrelations(RetrievePatientCorrelationsSecuredRequestType retrievePatientCorrelationsRequest) {
        log.info("start PatientCorrelationServiceImpl.retrievePatientCorrelations");

        PRPAIN201309UV IN201309 = retrievePatientCorrelationsRequest.getPRPAIN201309UV();

        PRPAMT201307UVPatientIdentifier patIdentifier = PRPAIN201309UVParser.parseHL7PatientPersonFrom201309Message(IN201309);
        if (patIdentifier == null) {
            return null;
        }
        List<II> listII = patIdentifier.getValue();
        if (listII == null) {
            return null;
        }
        if (listII.get(0) == null) {
            return null;
        }

        II inputPatientId = listII.get(0);

        List<String> dataSourceList = extractDataSourceList(IN201309);
        
        
        CorrelatedIdentifiersDao dao = new CorrelatedIdentifiersDao();
        QualifiedPatientIdentifier inputQualifiedPatientIdentifier = QualifiedPatientIdentifierFactory(inputPatientId);
        List<QualifiedPatientIdentifier> qualifiedPatientIdentifiers = dao.retrievePatientCorrelation(inputQualifiedPatientIdentifier , dataSourceList);
        List<II> iiList = buildList(qualifiedPatientIdentifiers);
        PRPAIN201310UV IN201310 = PixRetrieveResponseBuilder.createPixRetrieveResponse(IN201309, iiList);
        RetrievePatientCorrelationsSecuredResponseType result = new RetrievePatientCorrelationsSecuredResponseType();
        result.setPRPAIN201310UV(IN201310);
        return result;
    }
    //controlActProcess/queryByParameter/parameterList/DataSource/value/@root
    private static List<String> extractDataSourceList(PRPAIN201309UV IN201309) {
        List<String> dataSourceStringList = new ArrayList<String>();
        PRPAMT201307UVParameterList parameterList = PRPAIN201309UVParser.parseHL7ParameterListFrom201309Message(IN201309);
        List<PRPAMT201307UVDataSource> dataSources = parameterList.getDataSource();

        if (dataSources != null) {
            for (PRPAMT201307UVDataSource datasource : dataSources) {
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

//    private static List<II> buildListFromSingleItem(II item) {
//        List<II> list = new ArrayList<II>();
//        list.add(item);
//        return list;
//    }
//
//    private static List<II> filterList(List<II> originalList, List<II> inclusionList, List<II> exclusionList) {
//        List<II> filteredList;
//
//        if ((originalList == null) || (originalList.size() == 0)) {
//            filteredList = originalList;
//        } else if ((inclusionList == null) && (exclusionList == null)) {
//            filteredList = originalList;
//        } else {
//            filteredList = new ArrayList<II>();
//            for (II item : originalList) {
//                if (itemAllowed(item, inclusionList, exclusionList)) {
//                    filteredList.add(item);
//                }
//            }
//        }
//
//        return filteredList;
//    }
//
//    private static boolean itemAllowed(II item, List<II> inclusionList, List<II> exclusionList) {
//        boolean result = true;
//
//        if (inclusionList != null) {
//            boolean found = false;
//            for (II inclusionItem : inclusionList) {
//                if (rootsEqual(item, inclusionItem)) {
//                    found = true;
//                    break;
//                }
//            }
//
//            result = found;
//        }
//
//        if (result) {
//            if (exclusionList != null) {
//                boolean found = false;
//                for (II exclusionItem : exclusionList) {
//                    if (rootsEqual(item, exclusionItem)) {
//                        found = true;
//                        break;
//                    }
//                }
//                result = !found;
//            }
//        }
//
//        return result;
//    }
//
//    private static boolean rootsEqual(II a, II b) {
//        boolean result;
//        if (a == null) {
//            result = (b == null);
//        } else if (b == null) {
//            result = (a == null);
//        } else if (a.getRoot() == null) {
//            result = (b.getRoot() == null);
//        } else if (b.getRoot() == null) {
//            result = (a.getRoot() == null);
//        } else {
//            result = (a.getRoot().contentEquals(b.getRoot()));
//        }
//        return result;
//    }
    public static AddPatientCorrelationSecuredResponseType addPatientCorrelation(AddPatientCorrelationSecuredRequestType addPatientCorrelationRequest) {
        PRPAIN201301UV IN201301 = addPatientCorrelationRequest.getPRPAIN201301UV();

        PRPAMT201301UVPatient patient = PRPAIN201301UVParser.ParseHL7PatientPersonFrom201301Message(IN201301);
        String patientId = "";
        String patientAssigningAuthId = "";
        String correlatedPatientId = "";
        String correlatedPatientAssigningAuthId = "";
        if (patient == null) {
            return null;
        }
        List<II> ids = patient.getId();
        if (ids == null) {
            return null;
        }
        if (ids.get(0) == null) {
            return null;
        }
        if (ids.get(1) == null) {
            return null;
        }
        patientId = ids.get(0).getExtension();
        if (patientId != null && !patientId.equals("")) {
        } else {
            return null;
        }
        patientAssigningAuthId = ids.get(0).getRoot();
        if (patientAssigningAuthId != null && !patientAssigningAuthId.equals("")) {
        } else {
            return null;
        }
        correlatedPatientId = ids.get(1).getExtension();
        if (correlatedPatientId != null & !correlatedPatientId.equals("")) {
        } else {
            return null;
        }
        correlatedPatientAssigningAuthId = ids.get(1).getRoot();
        if (correlatedPatientAssigningAuthId != null && !correlatedPatientAssigningAuthId.equals("")) {
        } else {
            return null;
        }
        if (patientAssigningAuthId.equals(correlatedPatientAssigningAuthId) && patientId.equals(correlatedPatientId)) {
            return null;
        }
        CorrelatedIdentifiers correlatedIdentifers = new CorrelatedIdentifiers();
        correlatedIdentifers.setCorrelatedPatientAssigningAuthorityId(correlatedPatientAssigningAuthId);
        correlatedIdentifers.setCorrelatedPatientId(correlatedPatientId);
        correlatedIdentifers.setPatientId(patientId);
        correlatedIdentifers.setPatientAssigningAuthorityId(patientAssigningAuthId);
        CorrelatedIdentifiersDao dao = new CorrelatedIdentifiersDao();
        dao.addPatientCorrelation(correlatedIdentifers);


        AddPatientCorrelationSecuredResponseType result = new AddPatientCorrelationSecuredResponseType();
        result.setMCCIIN000002UV01(AckBuilder.BuildAck(IN201301) );
        return result;
    }

    public static RemovePatientCorrelationSecuredResponseType removePatientCorrelation(RemovePatientCorrelationSecuredRequestType removePatientCorrelationRequest) {
        PRPAIN201303UV IN201303 = removePatientCorrelationRequest.getPRPAIN201303UV();

        PRPAMT201305UVPatient patient = PRPAIN201303UVParser.parseHL7PatientPersonFrom201303Message(IN201303);
        if (patient == null) {
            return null;
        }

        List<II> ids = patient.getId();
        if (ids == null) {
            return null;
        }

        String patientId = (ids.get(0) != null) ? ids.get(0).getExtension() : "";
        String patientAssigningAuthId = (ids.get(0) != null) ? ids.get(0).getRoot() : "";
        String correlatedPatientId = (ids.get(1) != null) ? ids.get(1).getExtension() : "";
        String correlatedPatientAssigningAuthId = (ids.get(1) != null) ? ids.get(1).getRoot() : "";
        CorrelatedIdentifiers correlatedIdentifers = new CorrelatedIdentifiers();
        CorrelatedIdentifiers correlatedPatientIdentifers = new CorrelatedIdentifiers();
        correlatedIdentifers.setCorrelatedPatientAssigningAuthorityId(correlatedPatientAssigningAuthId);
        correlatedIdentifers.setCorrelatedPatientId(correlatedPatientId);
        correlatedIdentifers.setPatientId(patientId);
        correlatedIdentifers.setPatientAssigningAuthorityId(patientAssigningAuthId);
        correlatedPatientIdentifers.setCorrelatedPatientAssigningAuthorityId(patientAssigningAuthId);
        correlatedPatientIdentifers.setCorrelatedPatientId(patientId);
        correlatedPatientIdentifers.setPatientId(correlatedPatientId);
        correlatedPatientIdentifers.setPatientAssigningAuthorityId(correlatedPatientAssigningAuthId);
        CorrelatedIdentifiersDao dao = new CorrelatedIdentifiersDao();
        dao.removePatientCorrelation(correlatedIdentifers);
        dao.removePatientCorrelation(correlatedPatientIdentifers);

        RemovePatientCorrelationSecuredResponseType result = new RemovePatientCorrelationSecuredResponseType();
        MCCIIN000002UV01 ack = new MCCIIN000002UV01();
        result.setMCCIIN000002UV01(ack);
        return result;
    }
}
