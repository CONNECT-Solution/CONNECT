/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.universalclientgui.interfaces;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.CeType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.PersonNameType;
import gov.hhs.fha.nhinc.common.nhinccommon.UserType;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.xml.ws.BindingProvider;
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
public class DocumentAccessManager extends Thread {
    private static Log log = LogFactory.getLog(DocumentAccessManager.class);
    private String homeCommunityDesc = "";
    private String homeCommunityId = "";
    private String homeCommunityName = "";
    private String patientUnitNumber = "";
    private String dateOfBirth = "";
    private String patientLastName = "";
    private String patientFirstName = "";
    private String patientMiddleInitial = "";
    
    private String role = "";
    private String roleCode = "";
    private String roleSystem = "";
    private String roleSystemName = "";
    private String roleSystemVersion = "";

    private String userName = "";
    private String providerName = "";
    private String providerFirstName = "";
    private String providerLastName = "";
    private String providerMiddleName = "";

    private String purposeOfUse = "";
    
    private String purposeOfUseCode = "";
    private String purposeOfUseCodeSystem = "";
    private String purposeOfUseCodeSystemName = "";
    private String purposeOfUseCodeSystemVersion = "";
    private String assigningAuthorityId = "";
    private String uniqueDocumentId = "";
    private String providerId = "";

    private String claimFormRef = "";
    private String claimFormRaw = "";
    private String signatureDate = "";
    private String expirationDate = "";

    private int numberOfYears;

    private String DOCVIEWER_REQUEST_SERVICE = "http://localhost:8080/UniversalClientWS/DocViewerRequestServicesService";

    private static final String CREATION_TIME_FROM_SLOT_NAME = "$XDSDocumentEntryCreationTimeFrom";
    private static final String CREATION_TIME_TO_SLOT_NAME = "$XDSDocumentEntryCreationTimeTo";
    private static final String HL7_DATE_FORMAT = "yyyyMMddHHmmssZ";
    private static final String REGULAR_DATE_FORMAT = "MM/dd/yyyy";



    public DocumentAccessManager(String homecommunity, String homecommunityname, String homecommunitydesc,
                                 String patientid, String dateofbirth, String patientfirstname, String patientlastname,
                                 String patientmiddleinitial, String role, String username,
                                 String providername, String purposeofuse, String assigningauthid,
                                 String providerid, String providerlastname, String providerfirstname, String providermiddlename,
                                 String rolecode, String rolesystem, String rolesystemname, String rolesystemversion,
                                 String purposeofusecode, String purposeofusecodesystem, String purposeofusecodesystemname,
                                 String purposeofusecodesystemversion, String claimformref, String claimformraw,
                                 String signaturedate, String expirationdate, int numberofyears) {

        log.debug("DocumentAccessManager - Entering Instance");
        this.assigningAuthorityId = assigningauthid;
        this.dateOfBirth = dateofbirth;
        this.homeCommunityId = homecommunity;
        this.homeCommunityDesc = homecommunitydesc;
        this.homeCommunityName = homecommunityname;
        this.patientFirstName = patientfirstname;
        this.patientLastName = patientlastname;
        this.patientMiddleInitial = patientmiddleinitial;
        this.patientUnitNumber = patientid;

        this.userName = username;
        this.providerId = providerid;
        this.providerName = providername;
        this.providerFirstName = providerfirstname;
        this.providerLastName = providerlastname;
        this.providerMiddleName = providermiddlename;

        this.purposeOfUse = purposeofuse;
        this.purposeOfUseCode = purposeofusecode;
        this.purposeOfUseCodeSystem = purposeofusecodesystem;
        this.purposeOfUseCodeSystemName = purposeofusecodesystemname;
        this.purposeOfUseCodeSystemVersion = purposeofusecodesystemversion;

        this.role = role;
        this.roleCode = rolecode;
        this.roleSystem = rolesystem;
        this.roleSystemName = rolesystemname;
        this.roleSystemVersion = rolesystemversion;

        this.claimFormRef = claimformref;
        this.claimFormRaw = claimformraw;
        this.signatureDate = signaturedate;
        this.expirationDate = expirationdate;

        this.numberOfYears = numberofyears;
    }

    public void run() {

        AdhocQueryRequest query = createAdhocQueryRequest();
        AssertionType assertion = createAssertion();

        try { // Call Web Service Operation
            gov.hhs.fha.nhinc.universalclient.ws.DocViewerRequestServicesService service = new gov.hhs.fha.nhinc.universalclient.ws.DocViewerRequestServicesService();
            gov.hhs.fha.nhinc.universalclient.ws.DocViewerRequestServicesPortType port = service.getDocViewerRequestServicesPort();
            ((BindingProvider)port).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, DOCVIEWER_REQUEST_SERVICE);

            org.netbeans.xml.schema.docviewer.DocViewerRequestType docRequest = new org.netbeans.xml.schema.docviewer.DocViewerRequestType();
            docRequest.setAdhocQueryRequest(query);
            docRequest.setAssertion(assertion);
            docRequest.setHomeCommunityId(homeCommunityId);
            docRequest.setPatientId(patientUnitNumber);
            docRequest.setUserId(providerId);

            org.netbeans.xml.schema.docviewer.DocViewerResponseType result = port.requestAllNHINDocuments(docRequest);
            System.out.println("Result = "+result);
        } catch (Exception ex) {
            log.error("DocumentAccessManager:run ERROR "+ex.getMessage());
            ex.printStackTrace();
        }


       log.debug("DocumentAccessManager - Exiting Instance");
    }


    private AdhocQueryRequest createAdhocQueryRequest() {
            AdhocQueryRequest query = new AdhocQueryRequest();
            query.setFederated(false);
            query.setStartIndex(BigInteger.valueOf(0));
            query.setMaxResults(BigInteger.valueOf(-1));
            ResponseOptionType resp = new ResponseOptionType();
            resp.setReturnComposedObjects(true);
            resp.setReturnType("LeafClass");
            query.setResponseOption(resp);

            AdhocQueryType queryType = new AdhocQueryType();
            queryType.setId("urn:uuid:14d4debf-8f97-4251-9a74-a90016b0af0d");


            SlotType1 patientslot = new SlotType1();
            patientslot.setName("$XDSDocumentEntryPatientId");
            ValueListType patientvalList = new ValueListType();
            StringBuffer sb = new StringBuffer();
            sb.append(patientUnitNumber);
            sb.append("^^^&");
            sb.append(homeCommunityId);
            sb.append("&ISO");
            patientvalList.getValue().add(sb.toString());
            patientslot.setValueList(patientvalList);
            queryType.getSlot().add(patientslot);

            SlotType1 entryslot = new SlotType1();
            entryslot.setName("$XDSDocumentEntryStatus");
            ValueListType entryvalList = new ValueListType();
            entryvalList.getValue().add("('urn:oasis:names:tc:ebxml-regrep:StatusType:Approved')");
            entryslot.setValueList(entryvalList);
            queryType.getSlot().add(entryslot);

            //Set Creation From Date
            SlotType1 creationStartTimeSlot = new SlotType1();
            creationStartTimeSlot.setName(CREATION_TIME_FROM_SLOT_NAME);

            ValueListType creationStartTimeValueList = new ValueListType();
            Date creationFromDate = getStartDate();
            creationStartTimeValueList.getValue().add(formatDate(creationFromDate, HL7_DATE_FORMAT));

            creationStartTimeSlot.setValueList(creationStartTimeValueList);
            queryType.getSlot().add(creationStartTimeSlot);

            // Set Creation To Date
            SlotType1 creationEndTimeSlot = new SlotType1();
            creationEndTimeSlot.setName(CREATION_TIME_TO_SLOT_NAME);

            ValueListType creationEndTimeSlotValueList = new ValueListType();
            Date creationToDate = new Date();
            creationEndTimeSlotValueList.getValue().add(formatDate(creationToDate, HL7_DATE_FORMAT));

            creationEndTimeSlot.setValueList(creationEndTimeSlotValueList);
            queryType.getSlot().add(creationEndTimeSlot);


            query.setAdhocQuery(queryType);

            return query;
    }

    public AssertionType createAssertion() {
            AssertionType assertion = new AssertionType();
            assertion.setDateOfBirth(dateOfBirth);
//            assertion.setExpirationDate(expirationDate);
//            assertion.setDateOfSignature(signatureDate);

            PersonNameType pName = new PersonNameType();
            pName.setFamilyName(patientLastName);
            pName.setGivenName(patientFirstName);
            pName.setSecondNameOrInitials(patientMiddleInitial);
            assertion.setPersonName(pName);

            HomeCommunityType hc = new HomeCommunityType();
            hc.setDescription(homeCommunityDesc);
            hc.setHomeCommunityId(homeCommunityId);
            hc.setName(homeCommunityName);


            UserType muser = new UserType();
            CeType roleType = new CeType();
            roleType.setCode(roleCode);
            roleType.setCodeSystem(roleSystem);
            roleType.setCodeSystemName(roleSystemName);
            roleType.setCodeSystemVersion(roleSystemVersion);
            roleType.setDisplayName(role);
            roleType.setOriginalText(role);
            muser.setOrg(hc);
//            muser.setRole(role);
            muser.setRoleCoded(roleType);
            //VA DoD Requirement
            String uPlusDoD = userName+"*DoD";
            muser.setUserName(uPlusDoD);
            PersonNameType uName = new PersonNameType();
            CeType nType = new CeType();
            nType.setCode("P");
            nType.setCodeSystem("30");
            nType.setCodeSystemName("nameType");
            nType.setCodeSystemVersion("1.0");
            nType.setDisplayName("P");
            nType.setOriginalText("P");

            uName.setNameType(nType);
            uName.setFamilyName(providerLastName);
            uName.setGivenName(providerFirstName);
            uName.setSecondNameOrInitials(providerMiddleName);
            muser.setPersonName(uName);
            assertion.setUserInfo(muser);

            assertion.setHomeCommunity(hc);
//            assertion.setPurposeOfDisclosure(purposeOfUse);
            CeType pouType = new CeType();
            pouType.setCode(purposeOfUseCode);
            pouType.setCodeSystem(purposeOfUseCodeSystem);
            pouType.setCodeSystemName(purposeOfUseCodeSystemName);
            pouType.setCodeSystemVersion(purposeOfUseCodeSystemVersion);
            pouType.setDisplayName(purposeOfUse);
            pouType.setOriginalText(purposeOfUse);
            assertion.setPurposeOfDisclosureCoded(pouType);

//            assertion.setClaimFormRef(claimFormRef);
//          assertion.setClaimFormRaw(claimFormRaw.getBytes());


            return assertion;
    }

    private String formatDate(String dateString, String inputFormat, String outputFormat) {
        SimpleDateFormat inputFormatter = new SimpleDateFormat(inputFormat);
        SimpleDateFormat outputFormatter = new SimpleDateFormat(outputFormat);

        Date date = null;

        try {
            date = inputFormatter.parse(dateString);
        } catch (Exception ex) {
            log.error("DocumentAccessManager.formatDate "+ex.getMessage());
        }

        return outputFormatter.format(date);
    }

    private String formatDate(Date date, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }

    private Date getStartDate() {
        Date res = new Date();
        try {
            Date d = new Date();
            GregorianCalendar gc = new GregorianCalendar();
            gc.setTime(d);
            gc.add(Calendar.YEAR, -numberOfYears);
            res = gc.getTime();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return res;
    }

}
