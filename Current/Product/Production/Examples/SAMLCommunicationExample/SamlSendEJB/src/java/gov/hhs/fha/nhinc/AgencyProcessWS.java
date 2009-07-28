/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc;

import gov.hhs.fha.nhinc.callback.SamlCallbackHandler;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.RequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.ResponseType;
import java.util.Map;
import java.util.Set;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.xml.ws.BindingProvider;
import org.netbeans.j2ee.wsdl.interfaces.samlreceive.SamlReceivePortType;
import org.netbeans.j2ee.wsdl.interfaces.samlreceive.SamlReceiveService;
import org.netbeans.j2ee.wsdl.interfaces.samlsend.SamlSendPortType;

/**
 *
 * @author vvickers
 */
@WebService(serviceName = "SamlSendService", portName = "SamlSendPort", endpointInterface = "org.netbeans.j2ee.wsdl.interfaces.samlsend.SamlSendPortType", targetNamespace = "http://j2ee.netbeans.org/wsdl/Interfaces/SamlSend", wsdlLocation = "META-INF/wsdl/AgencyProcessWS/SamlSend.wsdl")
@Stateless
public class AgencyProcessWS implements SamlSendPortType {

    public gov.hhs.fha.nhinc.common.nhinccommon.ResponseType samlSendOperation(gov.hhs.fha.nhinc.common.nhinccommon.SamlCreationInfoType agencyIn) {
        System.out.println("Enter AgencyProcessWS.AgencyProcessWS");
        ResponseType rsp = new ResponseType();
        SamlReceiveService service = new SamlReceiveService();
        SamlReceivePortType rcvPort = service.getSamlReceivePort();
        Map requestContext = ((BindingProvider) rcvPort).getRequestContext();
        //String endpoint = "https://158.147.185.168:8181/SamlReceiveService/SamlProcessWS"; //does not work my box
        //String endpoint = "https://SC065633.cs.myharris.net:8181/SamlReceiveService/SamlProcessWS"; //works my box
        //String endpoint = "https://SC065631.cs.myharris.net:8181/SamlReceiveService/SamlProcessWS"; //PKIX with dev certs - ok with nhin cert
        //String endpoint = "https://158.147.185.96:8181/SamlReceiveService/SamlProcessWS"; //PKIX with dev certs - ok with nhin cert

        if (agencyIn != null) {
            if (agencyIn.getEpr() != null &&
                    agencyIn.getEpr().getEndpointReference() != null &&
                    agencyIn.getEpr().getEndpointReference().getAddress() != null &&
                    agencyIn.getEpr().getEndpointReference().getAddress().getValue() != null &&
                    !agencyIn.getEpr().getEndpointReference().getAddress().getValue().isEmpty()) {
                String endpoint = agencyIn.getEpr().getEndpointReference().getAddress().getValue();
                requestContext.put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpoint);
                AssertionType assertIn = agencyIn.getAssertion();
                requestContext.put(SamlCallbackHandler.ACTION_PROP, "TestSaml");
                requestContext.put(SamlCallbackHandler.RESOURCE_PROP, endpoint);
                if (assertIn != null) {
                    if (assertIn.getExpirationDate() != null && !assertIn.getExpirationDate().isEmpty()) {
                        requestContext.put(SamlCallbackHandler.EXPIRE_PROP, assertIn.getExpirationDate());
                    }
                    if (assertIn.getDateOfSignature() != null && !assertIn.getDateOfSignature().isEmpty()) {
                        requestContext.put(SamlCallbackHandler.SIGN_PROP, assertIn.getDateOfSignature());
                    }
                    if (assertIn.getClaimFormRef() != null && !assertIn.getClaimFormRef().isEmpty()) {
                        requestContext.put(SamlCallbackHandler.CONTENT_REF_PROP, assertIn.getClaimFormRef());
                    }
                    if (assertIn.getClaimFormRaw() != null) {
                        requestContext.put(SamlCallbackHandler.CONTENT_PROP, assertIn.getClaimFormRaw());
                    }
                    if (assertIn.getUserInfo() != null) {
                        if (assertIn.getUserInfo().getUserName() != null && !assertIn.getUserInfo().getUserName().isEmpty()) {
                            requestContext.put(SamlCallbackHandler.USER_NAME_PROP, assertIn.getUserInfo().getUserName());
                        }
                        if (assertIn.getUserInfo().getOrg() != null) {
                            if (assertIn.getUserInfo().getOrg().getName() != null && !assertIn.getUserInfo().getOrg().getName().isEmpty()) {
                                requestContext.put(SamlCallbackHandler.USER_ORG_PROP, assertIn.getUserInfo().getOrg().getName());
                            }
                        } else {
                            System.out.println("Error: samlSendOperation input assertion user org is null");
                        }
                        if (assertIn.getUserInfo().getRoleCoded() != null) {
                            if (assertIn.getUserInfo().getRoleCoded().getCode() != null && !assertIn.getUserInfo().getRoleCoded().getCode().isEmpty()) {
                                requestContext.put(SamlCallbackHandler.USER_CODE_PROP, assertIn.getUserInfo().getRoleCoded().getCode());
                            }
                            if (assertIn.getUserInfo().getRoleCoded().getCodeSystem() != null && !assertIn.getUserInfo().getRoleCoded().getCodeSystem().isEmpty()) {
                                requestContext.put(SamlCallbackHandler.USER_SYST_PROP, assertIn.getUserInfo().getRoleCoded().getCodeSystem());
                            }
                            if (assertIn.getUserInfo().getRoleCoded().getCodeSystemName() != null && !assertIn.getUserInfo().getRoleCoded().getCodeSystemName().isEmpty()) {
                                requestContext.put(SamlCallbackHandler.USER_SYST_NAME_PROP, assertIn.getUserInfo().getRoleCoded().getCodeSystemName());
                            }
                            if (assertIn.getUserInfo().getRoleCoded().getDisplayName() != null && !assertIn.getUserInfo().getRoleCoded().getDisplayName().isEmpty()) {
                                requestContext.put(SamlCallbackHandler.USER_DISPLAY_PROP, assertIn.getUserInfo().getRoleCoded().getDisplayName());
                            }
                        } else {
                            System.out.println("Error: samlSendOperation input assertion user info role is null");
                        }
                        if (assertIn.getUserInfo().getPersonName() != null) {
                            if (assertIn.getUserInfo().getPersonName().getGivenName() != null && !assertIn.getUserInfo().getPersonName().getGivenName().isEmpty()) {
                                requestContext.put(SamlCallbackHandler.USER_FIRST_PROP, assertIn.getUserInfo().getPersonName().getGivenName());
                            }
                            if (assertIn.getUserInfo().getPersonName().getSecondNameOrInitials() != null && !assertIn.getUserInfo().getPersonName().getSecondNameOrInitials().isEmpty()) {
                                requestContext.put(SamlCallbackHandler.USER_MIDDLE_PROP, assertIn.getUserInfo().getPersonName().getSecondNameOrInitials());
                            }
                            if (assertIn.getUserInfo().getPersonName().getFamilyName() != null && !assertIn.getUserInfo().getPersonName().getFamilyName().isEmpty()) {
                                requestContext.put(SamlCallbackHandler.USER_LAST_PROP, assertIn.getUserInfo().getPersonName().getFamilyName());
                            }
                        } else {
                            System.out.println("Error: samlSendOperation input assertion user person name is null");
                        }
                    } else {
                        System.out.println("Error: samlSendOperation input assertion user info is null");
                    }
                    if (assertIn.getPurposeOfDisclosureCoded() != null) {
                        if (assertIn.getPurposeOfDisclosureCoded().getCode() != null && !assertIn.getPurposeOfDisclosureCoded().getCode().isEmpty()) {
                            requestContext.put(SamlCallbackHandler.PURPOSE_CODE_PROP, assertIn.getPurposeOfDisclosureCoded().getCode());
                        }
                        if (assertIn.getPurposeOfDisclosureCoded().getCodeSystem() != null && !assertIn.getPurposeOfDisclosureCoded().getCodeSystem().isEmpty()) {
                            requestContext.put(SamlCallbackHandler.PURPOSE_SYST_PROP, assertIn.getPurposeOfDisclosureCoded().getCodeSystem());
                        }
                        if (assertIn.getPurposeOfDisclosureCoded().getCodeSystemName() != null && !assertIn.getPurposeOfDisclosureCoded().getCodeSystemName().isEmpty()) {
                            requestContext.put(SamlCallbackHandler.PURPOSE_SYST_NAME_PROP, assertIn.getPurposeOfDisclosureCoded().getCodeSystemName());
                        }
                        if (assertIn.getPurposeOfDisclosureCoded().getDisplayName() != null && !assertIn.getPurposeOfDisclosureCoded().getDisplayName().isEmpty()) {
                            requestContext.put(SamlCallbackHandler.PURPOSE_DISPLAY_PROP, assertIn.getPurposeOfDisclosureCoded().getDisplayName());
                        }
                    } else {
                        System.out.println("Error: samlSendOperation input assertion purpose is null");
                    }
                } else {
                    System.out.println("Error: samlSendOperation input assertion is null");
                }
                System.out.println("Request Context:");
                Set allKeys = requestContext.keySet();
                for (Object key : allKeys) {
                    System.out.println(key + " = " + requestContext.get(key));
                }
                try { // Call Web Service Operation
                    RequestType body = new RequestType();
                    body.setMessage("Message");
                    System.out.println("Invoking SamlReceiveService");
                    AcknowledgementType ack = rcvPort.samlReceiveOperation(body);
                    System.out.println("Result = " + ack.getMessage());
                    rsp.setMessage(ack.getMessage());
                } catch (Exception ex) {
                    // TODO handle custom exceptions here
                    System.out.println("=========== error message: " + ex.getMessage());
                }
            } else {
                System.out.println("Error: samlSendOperation destination endpoint is not set");
            }
        } else {
            System.out.println("Error: samlSendOperation input paramter is null");
        }

        System.out.println("Exit AgencyProcessWS.AgencyProcessWS");
        return rsp;
    }
}
