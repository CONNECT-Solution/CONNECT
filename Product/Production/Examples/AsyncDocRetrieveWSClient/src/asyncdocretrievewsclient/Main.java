/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package asyncdocretrievewsclient;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.CeType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommon.PersonNameType;
import gov.hhs.fha.nhinc.common.nhinccommon.SamlAuthnStatementType;
import gov.hhs.fha.nhinc.common.nhinccommon.UserType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayRetrieveRequestType;
import gov.hhs.fha.nhinc.nhincproxydocretrieve.NhincProxyDocRetrieve;
import gov.hhs.fha.nhinc.nhincproxydocretrieve.NhincProxyDocRetrievePortType;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType.DocumentRequest;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import java.util.concurrent.Future;
import javax.xml.ws.AsyncHandler;
import javax.xml.ws.BindingProvider;
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
            NhincProxyDocRetrievePortType port = buildEndPoint();
            RespondingGatewayCrossGatewayRetrieveRequestType respondingGatewayCrossGatewayRetrieveRequest = new RespondingGatewayCrossGatewayRetrieveRequestType();
            setRequest(respondingGatewayCrossGatewayRetrieveRequest);
            AsyncHandler<RetrieveDocumentSetResponseType> asyncHandler = new AsyncHandler<RetrieveDocumentSetResponseType>() {

                public void handleResponse(javax.xml.ws.Response<ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType> response) {
                    try {
                        String value = new String(response.get().getDocumentResponse().get(0).getDocument());
                        System.out.println("Document = "+value);
                        System.out.println("Result = " + response.get());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            };
            Future<? extends Object> result = port.respondingGatewayCrossGatewayRetrieveAsync(respondingGatewayCrossGatewayRetrieveRequest, asyncHandler);
            while (!result.isDone()) {
                System.out.println("Waiting on DocRetrieve response...");
                Thread.sleep(100);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void setRequest(RespondingGatewayCrossGatewayRetrieveRequestType request)
    {
        //Set Document Retrieve request parameters
        RetrieveDocumentSetRequestType req = new RetrieveDocumentSetRequestType();
        DocumentRequest docRequest = new DocumentRequest();
        docRequest.setDocumentUniqueId("555555555");
        docRequest.setHomeCommunityId("urn:oid:1.1");
        docRequest.setRepositoryUniqueId("1");
        req.getDocumentRequest().add(docRequest);
        request.setRetrieveDocumentSetRequest(req);
        //Set Assertion Information
        request.setAssertion(buildAssertionInfo("1.1"));
        //Set Target System
        NhinTargetSystemType targetSystem = new NhinTargetSystemType();
        HomeCommunityType homeCommunityType = new HomeCommunityType();
        homeCommunityType.setDescription("InternalTest1");
        homeCommunityType.setName("InternalTest1");
        homeCommunityType.setHomeCommunityId("1.1");
        targetSystem.setHomeCommunity(homeCommunityType);
        request.setNhinTargetSystem(targetSystem);
    }
    
    /**
     * This method is used to build Asserton information to send Notification to Entity Notification Consumer
     * @param sHid
     * @return AssertionType
     */
    private static AssertionType buildAssertionInfo(String sHid) {
        log.debug("Begin - CPPOperations.buildAssertion() - ");
        AssertionType assertion = new AssertionType();
        String svalue = "";
        try {
            assertion.setHaveSignature(true);
            assertion.setHaveWitnessSignature(true);
            svalue = PropertyAccessor.getProperty(ASSERTIONINFO_PROPFILE_NAME, CDAConstants.DATE_OF_SIGNATURE);
            if (svalue != null && svalue.length() > 0) {
                assertion.setDateOfSignature(svalue.trim());
            } else {
                assertion.setDateOfSignature("");
            }
            svalue = PropertyAccessor.getProperty(ASSERTIONINFO_PROPFILE_NAME, CDAConstants.EXPIRATION_DATE);
            if (null != svalue && svalue.length() > 0) {
                assertion.setExpirationDate(svalue.trim());
            } else {
                assertion.setExpirationDate("");
            }
            PersonNameType aPersonName = new PersonNameType();
            svalue = PropertyAccessor.getProperty(ASSERTIONINFO_PROPFILE_NAME, CDAConstants.FIRST_NAME);
            if (null != svalue && svalue.length() > 0) {
                aPersonName.setGivenName(svalue.trim());
            } else {
                aPersonName.setGivenName("");
            }
            svalue = PropertyAccessor.getProperty(ASSERTIONINFO_PROPFILE_NAME, CDAConstants.LAST_NAME);
            if (null != svalue && svalue.length() > 0) {
                aPersonName.setFamilyName(svalue.trim());
            } else {
                aPersonName.setFamilyName("");
            }
            UserType aUser = new UserType();
            aUser.setPersonName(aPersonName);
            HomeCommunityType userHm = new HomeCommunityType();
            svalue = PropertyAccessor.getProperty(CDAConstants.SubscribeeCommunityList_PROPFILE_NAME, sHid);
            if (null != svalue && svalue.length() > 0) {
                userHm.setName(svalue.trim());
            } else {
                userHm.setName("");
            }
            userHm.setHomeCommunityId(sHid);
            aUser.setOrg(userHm);
            CeType userCe = new CeType();
            svalue = PropertyAccessor.getProperty(ASSERTIONINFO_PROPFILE_NAME, CDAConstants.USER_ROLE_CD);
            if (null != svalue && svalue.length() > 0) {
                userCe.setCode(svalue.trim());
            } else {
                userCe.setCode("");
            }
            svalue = PropertyAccessor.getProperty(ASSERTIONINFO_PROPFILE_NAME, CDAConstants.USER_ROLE_CD_SYSTEM);
            if (null != svalue && svalue.length() > 0) {
                userCe.setCodeSystem(svalue.trim());
            } else {
                userCe.setCodeSystem("");
            }
            svalue = PropertyAccessor.getProperty(ASSERTIONINFO_PROPFILE_NAME, CDAConstants.USER_ROLE_CD_SYSTEM_NAME);
            if (null != svalue && svalue.length() > 0) {
                userCe.setCodeSystemName(svalue.trim());
            } else {
                userCe.setCodeSystemName("");
            }
            svalue = PropertyAccessor.getProperty(ASSERTIONINFO_PROPFILE_NAME, CDAConstants.USER_ROLE_DISPLAY_NAME);
            if (null != svalue && svalue.length() > 0) {
                userCe.setDisplayName(svalue.trim());
            } else {
                userCe.setDisplayName("");
            }
            aUser.setRoleCoded(userCe);
            svalue = PropertyAccessor.getProperty(ASSERTIONINFO_PROPFILE_NAME, CDAConstants.USER_NAME);
            if (null != svalue && svalue.length() > 0) {
                aUser.setUserName(svalue.trim());
            } else {
                aUser.setUserName("");
            }
            assertion.setUserInfo(aUser);
            svalue = PropertyAccessor.getProperty(ASSERTIONINFO_PROPFILE_NAME, CDAConstants.ORG_NAME);
            HomeCommunityType hm = new HomeCommunityType();
            if (null != svalue && svalue.length() > 0) {
                hm.setName(svalue.trim());
            } else {
                hm.setName("");
            }
            assertion.setHomeCommunity(hm);
            CeType ce = new CeType();
            svalue = PropertyAccessor.getProperty(ASSERTIONINFO_PROPFILE_NAME, CDAConstants.PURPOSE_FOR_USE_ROLE_CD);
            if (null != svalue && svalue.length() > 0) {
                ce.setCode(svalue.trim());
            } else {
                ce.setCode("");
            }
            svalue = PropertyAccessor.getProperty(ASSERTIONINFO_PROPFILE_NAME, CDAConstants.PURPOSE_FOR_USE_CODE_SYSTEM);
            if (null != svalue && svalue.length() > 0) {
                ce.setCodeSystem(svalue.trim());
            } else {
                ce.setCodeSystem("");
            }
            svalue = PropertyAccessor.getProperty(ASSERTIONINFO_PROPFILE_NAME, CDAConstants.PURPOSE_FOR_USE_CODE_SYSTEM_NAME);
            if (null != svalue && svalue.length() > 0) {
                ce.setCodeSystemName(svalue.trim());
            } else {
                ce.setCodeSystemName("");
            }
            svalue = PropertyAccessor.getProperty(ASSERTIONINFO_PROPFILE_NAME, CDAConstants.PURPOSE_FOR_USE_DISPLAY_NAME);
            if (null != svalue && svalue.length() > 0) {
                ce.setDisplayName(svalue.trim());
            } else {
                ce.setDisplayName("");
            }
            assertion.setPurposeOfDisclosureCoded(ce);
            svalue = PropertyAccessor.getProperty(ASSERTIONINFO_PROPFILE_NAME, CDAConstants.CONTENT_REFERENCE);
            if (null != svalue && svalue.length() > 0) {
                assertion.setClaimFormRef(svalue.trim());
            } else {
                assertion.setClaimFormRef("");
            }
            svalue = PropertyAccessor.getProperty(ASSERTIONINFO_PROPFILE_NAME, CDAConstants.CONTENT);
            svalue = svalue.trim();
            if (null != svalue && svalue.length() > 0) {
                assertion.setClaimFormRaw(svalue.getBytes());
            } else {
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
        } catch (PropertyAccessException propExp) {
            propExp.printStackTrace();
        }
        log.debug("End - CPPOperations.buildAssertion() - ");
        return assertion;
    }

    public static NhincProxyDocRetrievePortType buildEndPoint() {
        String endpointURL = "http://localhost:8080/NhinConnect/NhincProxyDocRetrieve";
        NhincProxyDocRetrieve service = new NhincProxyDocRetrieve();
        NhincProxyDocRetrievePortType port = service.getNhincProxyDocRetrievePortSoap11();
        ((BindingProvider) port).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpointURL);
        return port;
    }
}
