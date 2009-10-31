/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package asyncdocquerywsclient;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.CeType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommon.PersonNameType;
import gov.hhs.fha.nhinc.common.nhinccommon.SamlAuthnStatementType;
import gov.hhs.fha.nhinc.common.nhinccommon.UserType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayQueryRequestType;
import gov.hhs.fha.nhinc.nhincproxydocquery.NhincProxyDocQuery;
import gov.hhs.fha.nhinc.nhincproxydocquery.NhincProxyDocQueryPortType;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import java.util.concurrent.Future;
import javax.xml.ws.AsyncHandler;
import javax.xml.ws.BindingProvider;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.query._3.ResponseOptionType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.AdhocQueryType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ValueListType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author svalluripalli
 */
public class Main {
    private static final Log log = LogFactory.getLog(Main.class);
    private static final String ASSERTIONINFO_PROPFILE_NAME = "assertioninfo";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        try {
            // Call Web Service Operation(async. callback)
            NhincProxyDocQueryPortType port = buildEndPoint();
            RespondingGatewayCrossGatewayQueryRequestType respondingGatewayCrossGatewayQueryRequest = new RespondingGatewayCrossGatewayQueryRequestType();
            setRequest(respondingGatewayCrossGatewayQueryRequest);
            AsyncHandler<AdhocQueryResponse> asyncHandler = new AsyncHandler<AdhocQueryResponse>() {
                public void handleResponse(javax.xml.ws.Response<oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse> response) {
                    try {
                        System.out.println("Print Status: "+response.get().getStatus());
                        System.out.println("Result = "+ response.get());
                    } catch(Exception ex) {
                        ex.printStackTrace();
                    }
                }
            };
            Future<? extends Object> result = port.respondingGatewayCrossGatewayQueryAsync(respondingGatewayCrossGatewayQueryRequest, asyncHandler);
            while(!result.isDone()) {
                System.out.println("I am waiting on DocQuery Response...");
                Thread.sleep(100);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
 
    }
    public static void setRequest(RespondingGatewayCrossGatewayQueryRequestType req)
    {
        AdhocQueryType adhocQuery = new AdhocQueryType();
        AdhocQueryRequest adhocQueryRequest = new AdhocQueryRequest();
        adhocQuery.setHome("urn:oid:1.1");
        adhocQuery.setId("urn:uuid:14d4debf-8f97-4251-9a74-a90016b0af0d");
        //Set Response options
        ResponseOptionType responseOptn = new ResponseOptionType();
        responseOptn.setReturnComposedObjects(Boolean.TRUE);
        responseOptn.setReturnType("LeafClass");
        adhocQueryRequest.setResponseOption(responseOptn);

        //Create Slots
        SlotType1 t1 = new SlotType1();
        t1.setName("$XDSDocumentEntryStatus");
        ValueListType value = new ValueListType();
        value.getValue().add("('urn:oasis:names:tc:ebxml-regrep:StatusType:Approved')");
        t1.setValueList(value);
        SlotType1 t2 = new SlotType1();
        t2.setName("$XDSDocumentEntryPatientId");
        ValueListType value1 = new ValueListType();
        value1.getValue().add("'D123401^^^&amp;1.1&amp;ISO'");
        t2.setValueList(value1);
        adhocQuery.getSlot().add(t1);
        adhocQuery.getSlot().add(t2);
        adhocQueryRequest.setAdhocQuery(adhocQuery);
        //Set AdhocQueryRequest
        req.setAdhocQueryRequest(adhocQueryRequest);
        //Set Assertion
        req.setAssertion(buildAssertionInfo("1.1"));
        //Set Target System
        NhinTargetSystemType targetSystem = new NhinTargetSystemType();
        HomeCommunityType homeCommunityType = new HomeCommunityType();
        homeCommunityType.setDescription("InternalTest1");
        homeCommunityType.setName("InternalTest1");
        homeCommunityType.setHomeCommunityId("1.1");
        targetSystem.setHomeCommunity(homeCommunityType);
        req.setNhinTargetSystem(targetSystem);
    }

    /**
     * This method is used to build Asserton information to send Notification to Entity Notification Consumer
     * @param sHid
     * @return AssertionType
     */
    private static AssertionType buildAssertionInfo(String sHid)
    {
        log.debug("Begin - CPPOperations.buildAssertion() - ");
        AssertionType assertion = new AssertionType();
        String svalue = "";
        try
        {
            assertion.setHaveSignature(true);
            assertion.setHaveWitnessSignature(true);
            svalue = PropertyAccessor.getProperty(ASSERTIONINFO_PROPFILE_NAME, CDAConstants.DATE_OF_SIGNATURE);
            if (svalue != null && svalue.length() > 0)
            {
                assertion.setDateOfSignature(svalue.trim());
            }
            else
            {
                assertion.setDateOfSignature("");
            }
            svalue = PropertyAccessor.getProperty(ASSERTIONINFO_PROPFILE_NAME, CDAConstants.EXPIRATION_DATE);
            if (null != svalue && svalue.length() > 0)
            {
                assertion.setExpirationDate(svalue.trim());
            } else
            {
                assertion.setExpirationDate("");
            }
            PersonNameType aPersonName = new PersonNameType();
            svalue = PropertyAccessor.getProperty(ASSERTIONINFO_PROPFILE_NAME, CDAConstants.FIRST_NAME);
            if (null != svalue && svalue.length() > 0)
            {
                aPersonName.setGivenName(svalue.trim());
            }
            else
            {
                aPersonName.setGivenName("");
            }
            svalue = PropertyAccessor.getProperty(ASSERTIONINFO_PROPFILE_NAME, CDAConstants.LAST_NAME);
            if (null != svalue && svalue.length() > 0)
            {
                aPersonName.setFamilyName(svalue.trim());
            }
            else
            {
                aPersonName.setFamilyName("");
            }
            UserType aUser = new UserType();
            aUser.setPersonName(aPersonName);
            HomeCommunityType userHm = new HomeCommunityType();
            svalue = PropertyAccessor.getProperty(CDAConstants.SubscribeeCommunityList_PROPFILE_NAME, sHid);
            if (null != svalue && svalue.length() > 0)
            {
                userHm.setName(svalue.trim());
            }
            else
            {
                userHm.setName("");
            }
            userHm.setHomeCommunityId(sHid);
            aUser.setOrg(userHm);
            CeType userCe = new CeType();
            svalue = PropertyAccessor.getProperty(ASSERTIONINFO_PROPFILE_NAME, CDAConstants.USER_ROLE_CD);
            if (null != svalue && svalue.length() > 0)
            {
                userCe.setCode(svalue.trim());
            }
            else
            {
                userCe.setCode("");
            }
            svalue = PropertyAccessor.getProperty(ASSERTIONINFO_PROPFILE_NAME, CDAConstants.USER_ROLE_CD_SYSTEM);
            if (null != svalue && svalue.length() > 0)
            {
                userCe.setCodeSystem(svalue.trim());
            }
            else
            {
                userCe.setCodeSystem("");
            }
            svalue = PropertyAccessor.getProperty(ASSERTIONINFO_PROPFILE_NAME, CDAConstants.USER_ROLE_CD_SYSTEM_NAME);
            if (null != svalue && svalue.length() > 0)
            {
                userCe.setCodeSystemName(svalue.trim());
            }
            else
            {
                userCe.setCodeSystemName("");
            }
            svalue = PropertyAccessor.getProperty(ASSERTIONINFO_PROPFILE_NAME, CDAConstants.USER_ROLE_DISPLAY_NAME);
            if (null != svalue && svalue.length() > 0)
            {
                userCe.setDisplayName(svalue.trim());
            }
            else
            {
                userCe.setDisplayName("");
            }
            aUser.setRoleCoded(userCe);
            svalue = PropertyAccessor.getProperty(ASSERTIONINFO_PROPFILE_NAME, CDAConstants.USER_NAME);
            if (null != svalue && svalue.length() > 0)
            {
                aUser.setUserName(svalue.trim());
            } else
            {
                aUser.setUserName("");
            }
            assertion.setUserInfo(aUser);
            svalue = PropertyAccessor.getProperty(ASSERTIONINFO_PROPFILE_NAME, CDAConstants.ORG_NAME);
            HomeCommunityType hm = new HomeCommunityType();
            if (null != svalue && svalue.length() > 0)
            {
                hm.setName(svalue.trim());
            }
            else
            {
                hm.setName("");
            }
            assertion.setHomeCommunity(hm);
            CeType ce = new CeType();
            svalue = PropertyAccessor.getProperty(ASSERTIONINFO_PROPFILE_NAME, CDAConstants.PURPOSE_FOR_USE_ROLE_CD);
            if (null != svalue && svalue.length() > 0)
            {
                ce.setCode(svalue.trim());
            }
            else
            {
                ce.setCode("");
            }
            svalue = PropertyAccessor.getProperty(ASSERTIONINFO_PROPFILE_NAME, CDAConstants.PURPOSE_FOR_USE_CODE_SYSTEM);
            if (null != svalue && svalue.length() > 0)
            {
                ce.setCodeSystem(svalue.trim());
            }
            else
            {
                ce.setCodeSystem("");
            }
            svalue = PropertyAccessor.getProperty(ASSERTIONINFO_PROPFILE_NAME, CDAConstants.PURPOSE_FOR_USE_CODE_SYSTEM_NAME);
            if (null != svalue && svalue.length() > 0)
            {
                ce.setCodeSystemName(svalue.trim());
            }
            else
            {
                ce.setCodeSystemName("");
            }
            svalue = PropertyAccessor.getProperty(ASSERTIONINFO_PROPFILE_NAME, CDAConstants.PURPOSE_FOR_USE_DISPLAY_NAME);
            if (null != svalue && svalue.length() > 0)
            {
                ce.setDisplayName(svalue.trim());
            }
            else
            {
                ce.setDisplayName("");
            }
            assertion.setPurposeOfDisclosureCoded(ce);
            svalue = PropertyAccessor.getProperty(ASSERTIONINFO_PROPFILE_NAME, CDAConstants.CONTENT_REFERENCE);
            if (null != svalue && svalue.length() > 0)
            {
                assertion.setClaimFormRef(svalue.trim());
            }
            else
            {
                assertion.setClaimFormRef("");
            }
            svalue = PropertyAccessor.getProperty(ASSERTIONINFO_PROPFILE_NAME, CDAConstants.CONTENT);
            svalue = svalue.trim();
            if (null != svalue && svalue.length() > 0)
            {
                assertion.setClaimFormRaw(svalue.getBytes());
            }
            else
            {
                assertion.setClaimFormRaw("".getBytes());
            }
            SamlAuthnStatementType samlAuthn = new SamlAuthnStatementType();
            samlAuthn.setAuthInstant("2009-04-16T13:15:39Z");
            samlAuthn.setSessionIndex("987");
            samlAuthn.setAuthContextClassRef("urn:oasis:names:tc:SAML:2.0:ac:classes:X509");
            samlAuthn.setSubjectLocalityAddress("158.147.185.168");
            samlAuthn.setSubjectLocalityDNSName("cs.myharris.net");
            assertion.setSamlAuthnStatement(samlAuthn);


            //assertion.setSamlAuthzDecisionStatement(null);
        }
        catch (PropertyAccessException propExp)
        {
            propExp.printStackTrace();
        }
        log.debug("End - CPPOperations.buildAssertion() - ");
        return assertion;
    }

    public static NhincProxyDocQueryPortType buildEndPoint()
    {
        String endpointURL = "http://localhost:8080/NhinConnect/NhincProxyDocQuery";
        NhincProxyDocQuery service = new NhincProxyDocQuery();
        NhincProxyDocQueryPortType port = service.getNhincProxyDocQueryPortSoap11();
        ((BindingProvider) port).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpointURL);
        return port;
    }

}
