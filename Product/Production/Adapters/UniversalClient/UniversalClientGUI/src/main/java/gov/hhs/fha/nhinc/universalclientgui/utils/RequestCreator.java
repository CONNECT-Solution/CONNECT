/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.universalclientgui.utils;

//import gov.hhs.fha.nhinc.common.nhinccommoninternalorch.RespondingGatewayCrossGatewayQueryRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayQueryRequestType;
import gov.hhs.fha.nhinc.universalclientgui.objects.PatientSearchData;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.ResponseOptionType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.AdhocQueryType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ValueListType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Duane DeCouteau
 */
public class RequestCreator {
    private static final String HOME_ID = "urn:oid:2.16.840.1.113883.3.200";
    private static final String ID = "urn:uuid:14d4debf-8f97-4251-9a74-a90016b0af0d";
    private static final String PATIENT_ID_SLOT_NAME = "$XDSDocumentEntryPatientId";
    private static final String CREATION_TIME_FROM_SLOT_NAME = "$XDSDocumentEntryCreationTimeFrom";
    private static final String CREATION_TIME_TO_SLOT_NAME = "$XDSDocumentEntryCreationTimeTo";
    private static final String HL7_DATE_FORMAT = "yyyyMMddHHmmssZ";
    private static final String REGULAR_DATE_FORMAT = "MM/dd/yyyy";
    private static final String PROPERTY_FILE_NAME = "gateway";
    private static final String PROPERTY_LOCAL_HOME_COMMUNITY = "localHomeCommunityId";
    private static Log log = LogFactory.getLog(RequestCreator.class);

    public RequestCreator() {

    }

    public RespondingGatewayCrossGatewayQueryRequestType createQueryRequest(PatientSearchData patientSearchData, Date creationFromDate, Date creationToDate) {
        AdhocQueryType adhocQuery = new AdhocQueryType();
        adhocQuery.setHome(HOME_ID);
        adhocQuery.setId(ID);

        // Set patient id
        SlotType1 patientIDSlot = new SlotType1();
        patientIDSlot.setName(PATIENT_ID_SLOT_NAME);

        ValueListType valueList = new ValueListType();

        StringBuffer universalPatientID = new StringBuffer();
        universalPatientID.append(patientSearchData.getPatientId());
        universalPatientID.append("^^^&");
        universalPatientID.append(patientSearchData.getAssigningAuthorityID());
        universalPatientID.append("&ISO");

        valueList.getValue().add(universalPatientID.toString());

        //valueList.getValue().add("D123401^^^&1.1&ISO");

        patientIDSlot.setValueList(valueList);
        adhocQuery.getSlot().add(patientIDSlot);

        //Set Creation From Date
        SlotType1 creationStartTimeSlot = new SlotType1();
        creationStartTimeSlot.setName(CREATION_TIME_FROM_SLOT_NAME);

        ValueListType creationStartTimeValueList = new ValueListType();

        creationStartTimeValueList.getValue().add(formatDate(creationFromDate, HL7_DATE_FORMAT));

        creationStartTimeSlot.setValueList(creationStartTimeValueList);
        adhocQuery.getSlot().add(creationStartTimeSlot);

        // Set Creation To Date
        SlotType1 creationEndTimeSlot = new SlotType1();
        creationEndTimeSlot.setName(CREATION_TIME_TO_SLOT_NAME);

        ValueListType creationEndTimeSlotValueList = new ValueListType();

        creationEndTimeSlotValueList.getValue().add(formatDate(creationToDate, HL7_DATE_FORMAT));

        creationEndTimeSlot.setValueList(creationEndTimeSlotValueList);
        adhocQuery.getSlot().add(creationEndTimeSlot);

        ResponseOptionType responseOption = new ResponseOptionType();
        responseOption.setReturnType("LeafClass");
        responseOption.setReturnComposedObjects(Boolean.FALSE);

        AdhocQueryRequest adhocQueryRequest = new AdhocQueryRequest();
        adhocQueryRequest.setAdhocQuery(adhocQuery);
        adhocQueryRequest.setResponseOption(responseOption);

        RespondingGatewayCrossGatewayQueryRequestType request = new RespondingGatewayCrossGatewayQueryRequestType();
        request.setAdhocQueryRequest(adhocQueryRequest);

        AssertionCreator assertionCreator = new AssertionCreator();

        request.setAssertion(assertionCreator.createAssertion());

        return request;

    }

    private String formatDate(String dateString, String inputFormat, String outputFormat) {
        SimpleDateFormat inputFormatter = new SimpleDateFormat(inputFormat);
        SimpleDateFormat outputFormatter = new SimpleDateFormat(outputFormat);

        Date date = null;

        try {
            date = inputFormatter.parse(dateString);
        } catch (Exception ex) {
            Logger.getLogger(RequestCreator.class.getName()).log(Level.SEVERE, null, ex);
        }

        return outputFormatter.format(date);
    }

    private String formatDate(Date date, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }

}
