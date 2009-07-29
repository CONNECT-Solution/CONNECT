/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.patientcorrelationservice.parsers.PRPAIN201309UV;

import gov.hhs.fha.nhinc.patientcorrelation.model.CorrelatedIdentifiers;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.CS;
import org.hl7.v3.II;
import org.hl7.v3.MCCIMT000300UV01Acknowledgement;
import org.hl7.v3.MFMIMT700711UV01QueryAck;
import org.hl7.v3.PRPAIN201309UV;
import org.hl7.v3.PRPAIN201309UVQUQIMT021001UV01ControlActProcess;
import org.hl7.v3.PRPAIN201310UV;
import org.hl7.v3.PRPAIN201310UVMFMIMT700711UV01ControlActProcess;
import org.hl7.v3.PRPAIN201310UVMFMIMT700711UV01RegistrationEvent;
import org.hl7.v3.PRPAIN201310UVMFMIMT700711UV01Subject1;
import org.hl7.v3.PRPAIN201310UVMFMIMT700711UV01Subject2;
import org.hl7.v3.PRPAMT201304UVPatient;
import org.hl7.v3.PRPAMT201304UVPerson;
import org.hl7.v3.PRPAMT201307UVDataSource;
import org.hl7.v3.PRPAMT201307UVParameterList;
import org.hl7.v3.PRPAMT201307UVPatientIdentifier;
import org.hl7.v3.PRPAMT201307UVQueryByParameter;

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

    public static PRPAMT201307UVParameterList parseHL7ParameterListFrom201309Message(PRPAIN201309UV message) {
        if (message == null) {
            return null;
        }
        PRPAIN201309UVQUQIMT021001UV01ControlActProcess ctrlAccess = message.getControlActProcess();
        if (ctrlAccess == null) {
            return null;
        }
        JAXBElement<PRPAMT201307UVQueryByParameter> queryByParameter = ctrlAccess.getQueryByParameter();
        if (queryByParameter == null) {
            return null;
        }
        PRPAMT201307UVParameterList parameterList = (queryByParameter.getValue() != null) ? queryByParameter.getValue().getParameterList() : null;
        if (parameterList == null) {
            return null;
        }
        return parameterList;
    }

    public static PRPAMT201307UVPatientIdentifier parseHL7PatientPersonFrom201309Message(PRPAIN201309UV message) {
        log.debug("---- Begin PRPAIN201309UVParser.parseHL7PatientPersonFrom201309Message()----");
        PRPAMT201307UVParameterList parameterList = parseHL7ParameterListFrom201309Message(message);
        PRPAMT201307UVPatientIdentifier patientIdentifier = (parameterList.getPatientIdentifier() != null) ? parameterList.getPatientIdentifier().get(0) : null;
        log.debug("---- End PRPAIN201309UVParser.parseHL7PatientPersonFrom201309Message()----");
        return patientIdentifier;
    }

    public static List<II> buildAssigningAuthorityInclusionFilterList(PRPAIN201309UV message) {
        List<II> list = new ArrayList<II>();
        PRPAMT201307UVParameterList parameterList = parseHL7ParameterListFrom201309Message(message);
        List<PRPAMT201307UVDataSource> dataSourceList;
        if (parameterList != null) {
            dataSourceList = parameterList.getDataSource();
            for (PRPAMT201307UVDataSource dataSource : dataSourceList) {
                for (II dataSourceValue : dataSource.getValue()) {
                    list.add(dataSourceValue);
                }
            }
        }
        return list;
    }

    public static PRPAMT201307UVQueryByParameter ExtractQueryId(PRPAIN201309UV message) {
        PRPAMT201307UVQueryByParameter queryByParameter = null;
        if (message.getControlActProcess() != null) {
            JAXBElement<PRPAMT201307UVQueryByParameter> queryByParamterElement = message.getControlActProcess().getQueryByParameter();
            if (queryByParamterElement != null) {
                queryByParameter = queryByParamterElement.getValue();
            }
        }
        return queryByParameter;
    }
}
