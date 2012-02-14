package gov.hhs.fha.nhinc.gateway.util;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.ResponseOptionType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.AdhocQueryType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ValueListType;

/**
 * Constructs an AdhocQueryRequest for testing
 * 
 * @author paul
 */
public class AdhocQueryRequestGenerator {

    private AdhocQueryRequest request = null;

    private String patientID = null;
    private String loincCode = null;
    private String queryId = null;
    private static final String ASYNC_QUERYID_SLOT_NAME = "$AsyncQueryId";
    private static final String ASYNC_ONEPASS_FLAG_SLOT_NAME = "$AsyncOnepassFlag";
    private static final String QUERY_RETURN_TYPE = "RegistryObject";

    public AdhocQueryRequest generateTestRequest(int counter, String patientid) {
        return generateAdhocQueryRequest(patientid, "48765-2", true, "a1b2c3d4e5-" + counter, "01-01-1990",
                "01-01-2020", counter, null, null);
    }

    public AdhocQueryRequest generateTestRequest(int counter, String patientid, String homeId, String aaId) {
        return generateAdhocQueryRequest(patientid, "48765-2", true, "a1b2c3d4e5-" + counter, "01-01-1990",
                "01-01-2020", counter, homeId, aaId);
    }

    /**
     * Construct associated AdhocQueryRequest for the input parameters
     * 
     * @param patientid
     * @param loinc
     * @param useOnepass
     * @param queryid
     * @param startDateStr // startDateStr, endDateStr formatted mm-dd-yyyy
     * @param endDateStr
     */
    public AdhocQueryRequest generateAdhocQueryRequest(String patientid, String loinc, boolean useOnepass,
            String queryid, String startDateStr, String endDateStr, int counter, String homeCommunityId,
            String assigningAuthorityId) {
        patientID = patientid;
        loincCode = loinc;
        queryId = queryid;

        request = new AdhocQueryRequest();
        ResponseOptionType responseType = new ResponseOptionType();
        responseType.setReturnComposedObjects(false);
        responseType.setReturnType(QUERY_RETURN_TYPE);
        request.setResponseOption(responseType);
        request.setFederated(false);
        request.setId("id-" + counter);

        AdhocQueryType query = new AdhocQueryType();
        // set the query id to FindDocuments query
        query.setId(queryid);
        if (homeCommunityId == null) {
            query.setHome(getHomeID());
        } else {
            query.setHome(homeCommunityId);
        }

        // now set the query slot name-values
        List<SlotType1> slotList = query.getSlot();
        ValueListType valueListType = new ValueListType();
        List<String> valueList = new ArrayList<String>();

        // set patientId
        if (assigningAuthorityId == null) {
            setPatientId(patientID, slotList);
        } else {
            setPatientId(patientID, slotList, assigningAuthorityId);
        }

        // set the query status (value is always urn:oasis:names:tc:ebxml-regrep:StatusType:Approved)
        setQueryStatus(slotList);

        // set the query classcode ($XDSDocumentEntryClassCode) and classcodescheme ($XDSDocumentEntryClassCodeScheme)
        setClasscodeAndScheme(loincCode, slotList);

        // set the $XDSDocumentEntryCreationTimeFrom and $XDSDocumentEntryCreationTimeTo
        // XDS_TIME_FORMAT = "yyyyMMddHHmmss" where MM is month in year (01 to 12) and HH is hour in day (00 to 24)
        // set start date-time (if startDate or endDate null we don't add these slots)
        if (startDateStr != null) {
            // startDateStr formatted mm-dd-yyyy
            // startDate = convertDateFromString(startDateStr);
            SlotType1 startDateSlot = new SlotType1();
            startDateSlot.setName("$XDSDocumentEntryCreationTimeFrom");
            valueListType = new ValueListType();
            valueList = valueListType.getValue();
            // startDate includes time 000000
            valueList.add(getXcaStartDateTime(startDateStr));
            startDateSlot.setValueList(valueListType);
            slotList.add(startDateSlot);
        }
        if (endDateStr != null) {
            // endDateStr formatted mm-dd-yyyy
            // endDate = convertDateFromString(endDateStr);
            // set end date-time
            SlotType1 endDateSlot = new SlotType1();
            endDateSlot.setName("$XDSDocumentEntryCreationTimeTo");
            valueListType = new ValueListType();
            valueList = valueListType.getValue();
            // endDate includes time 235959
            valueList.add(getXcaEndDateTime(startDateStr));
            endDateSlot.setValueList(valueListType);
            slotList.add(endDateSlot);
        }

        // set async queryID
        SlotType1 queryIDSlot = new SlotType1();
        queryIDSlot.setName(ASYNC_QUERYID_SLOT_NAME);

        valueListType = new ValueListType();
        valueList = valueListType.getValue();
        valueList.add(queryId);
        queryIDSlot.setValueList(valueListType);
        slotList.add(queryIDSlot);

        // set async onepass flag
        SlotType1 onepassSlot = new SlotType1();
        onepassSlot.setName(ASYNC_ONEPASS_FLAG_SLOT_NAME);
        valueListType = new ValueListType();
        valueList = valueListType.getValue();
        if (useOnepass) {
            valueList.add("true");
        } else {
            valueList.add("false");
        }
        onepassSlot.setValueList(valueListType);
        slotList.add(onepassSlot);

        request.setAdhocQuery(query);
        return request;
    }

    /**
     * Appends ^^^&homeid&ISO to original patientid and adds patientIDSlot to slotList slotList is passed by reference
     * from AdhocQueryRequestGenerator
     * 
     * @param requestedPatientID
     * @param slotList
     */
    public void setPatientId(String requestedPatientID, List<SlotType1> slotList) {
        requestedPatientID += "^^^&" + "1.1" + "&ISO";
        SlotType1 patientIDSlot = new SlotType1();
        patientIDSlot.setName("$XDSDocumentEntryPatientId");
        ValueListType valueListType = new ValueListType();
        List<String> valueList = valueListType.getValue();
        valueList.add(requestedPatientID);
        patientIDSlot.setValueList(valueListType);
        slotList.add(patientIDSlot);
    }

    public void setPatientId(String requestedPatientID, List<SlotType1> slotList, String aaid) {
        requestedPatientID += "^^^&" + aaid + "&ISO";
        SlotType1 patientIDSlot = new SlotType1();
        patientIDSlot.setName("$XDSDocumentEntryPatientId");
        ValueListType valueListType = new ValueListType();
        List<String> valueList = valueListType.getValue();
        valueList.add(requestedPatientID);
        patientIDSlot.setValueList(valueListType);
        slotList.add(patientIDSlot);
    }

    /**
     * Set the query status value to urn:oasis:names:tc:ebxml-regrep:StatusType:Approved slotList is passed by reference
     * from AdhocQueryRequestGenerator
     * 
     * @param slotList
     */
    public void setQueryStatus(List<SlotType1> slotList) {
        SlotType1 statusSlot = new SlotType1();
        statusSlot.setName("$XDSDocumentEntryStatus");
        ValueListType valueListType = new ValueListType();
        List<String> valueList = valueListType.getValue();
        valueList.add("urn:oasis:names:tc:ebxml-regrep:StatusType:Approved");
        statusSlot.setValueList(valueListType);
        slotList.add(statusSlot);
    }

    /**
     * slotList is passed by reference from AdhocQueryRequestGenerator Currently only notes need to be converted to a
     * set of classcode/classcodescheme
     * 
     * @param requestedLoinc
     * @param slotList
     */
    public void setClasscodeAndScheme(String requestedLoinc, List<SlotType1> slotList) {
        // set the query datatype/LOINC in the class code
        SlotType1 loincSlot = new SlotType1();
        loincSlot.setName("$XDSDocumentEntryClassCodeScheme");

        // get the classcode scheme oid
        String CLASS_CODE_SCHEME_OID = "class.code.oid";

        ValueListType classcodeValueListType = new ValueListType();
        List<String> classcodeValueList = classcodeValueListType.getValue();

        classcodeValueList.add(formatClasscodeSchemeValue(requestedLoinc, CLASS_CODE_SCHEME_OID));
        loincSlot.setValueList(classcodeValueListType);
        slotList.add(loincSlot);
    }

    /**
     * the classcode scheme value must be formatted as ('LOINC^^CLASS_CODE_SCHEME_OID')
     * 
     * @param loinc
     * @param oid
     * @return
     */
    private String formatClasscodeSchemeValue(String loinc, String oid) {
        return "('" + loinc + "^^" + oid + "')";
    }

    public AdhocQueryRequest getAdhocQueryRequest() {
        return request;
    }

    /**
     * Get the HOME_COMMUNITY_ID from the spring configs stored in InitServlet
     * 
     * @return string that is the HOME_COMMUNITY_ID
     */
    public String getHomeID() {
        return "1.1";
    }

    /**
     * Returns the dateStr formatted as XCA compliant date string that includes time XDS_TIME_FORMAT = "yyyyMMddHHmmss"
     * where MM is month in year (01 to 12) and HH is hour in day (00 to 24) with the time part set to 000000 for start
     * time will return null if dateStr is null or not formatted properly
     * 
     * @param dateStr
     * @return String in format yyyyMMddHHmmss
     */
    private String getXcaStartDateTime(String dStr) {
        String xcaStartDate = null;
        xcaStartDate = convertDateToXcaString(dStr);
        if (xcaStartDate != null) {
            xcaStartDate += "000000";
        }
        return xcaStartDate;
    }

    /**
     * Returns the dateStr formatted as XCA compliant date string that includes time XDS_TIME_FORMAT = "yyyyMMddHHmmss"
     * where MM is month in year (01 to 12) and HH is hour in day (00 to 24) with the time part set to 235959 for end
     * time will return null if dateStr is null or not formatted properly
     * 
     * @param dateStr
     * @return String in format yyyyMMddHHmmss
     */
    private String getXcaEndDateTime(String dStr) {
        String xcaStartDate = null;
        xcaStartDate = convertDateToXcaString(dStr);
        if (xcaStartDate != null) {
            xcaStartDate += "235959";
        }
        return xcaStartDate;
    }

    /**
     * converts the dateStr in mm-dd-yyyy format to a XCA compliant string which is format yyyymmdd will return null if
     * dateStr is null or not formatted properly
     * 
     * @param dateStr
     * @return String in format yyyymmdd
     */
    private String convertDateToXcaString(String dtStr) {
        String date = null;
        try {
            StringTokenizer st = new StringTokenizer(dtStr, "-");
            String mmStr = st.nextToken();
            String ddStr = st.nextToken();
            String yyyyStr = st.nextToken();
            date = yyyyStr + mmStr + ddStr;
        } catch (Exception ex) {
        }
        return date;
    }

}
