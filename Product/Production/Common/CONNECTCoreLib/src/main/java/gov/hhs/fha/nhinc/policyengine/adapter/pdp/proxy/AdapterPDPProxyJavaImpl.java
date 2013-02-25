/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
 * All rights reserved. 
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met: 
 *     * Redistributions of source code must retain the above 
 *       copyright notice, this list of conditions and the following disclaimer. 
 *     * Redistributions in binary form must reproduce the above copyright 
 *       notice, this list of conditions and the following disclaimer in the documentation 
 *       and/or other materials provided with the distribution. 
 *     * Neither the name of the United States Government nor the 
 *       names of its contributors may be used to endorse or promote products 
 *       derived from this software without specific prior written permission. 
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY 
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS 
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */
package gov.hhs.fha.nhinc.policyengine.adapter.pdp.proxy;

import com.sun.identity.shared.xml.XMLUtils;
import com.sun.identity.xacml.common.XACMLException;
import com.sun.identity.xacml.context.Attribute;
import com.sun.identity.xacml.context.ContextFactory;
import com.sun.identity.xacml.context.Decision;
import com.sun.identity.xacml.context.Request;
import com.sun.identity.xacml.context.Resource;
import com.sun.identity.xacml.context.Response;
import com.sun.identity.xacml.context.Result;
import com.sun.identity.xacml.context.Status;
import com.sun.identity.xacml.context.StatusCode;
import com.sun.identity.xacml.context.StatusMessage;
import com.sun.identity.xacml.context.Subject;
import gov.hhs.fha.nhinc.docrepository.adapter.model.Document;
import gov.hhs.fha.nhinc.docrepository.adapter.model.DocumentQueryParams;
import gov.hhs.fha.nhinc.docrepository.adapter.service.DocumentService;
import gov.hhs.fha.nhinc.policyengine.adapter.pip.AdapterPIPException;
import gov.hhs.fha.nhinc.policyengine.adapter.pip.XACMLSerializer;
import gov.hhs.fha.nhinc.util.StringUtil;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBException;
import oasis.names.tc.xacml._2_0.policy.schema.os.EffectType;
import oasis.names.tc.xacml._2_0.policy.schema.os.PolicyType;
import oasis.names.tc.xacml._2_0.policy.schema.os.RuleType;
import oasis.names.tc.xacml._2_0.policy.schema.os.SubjectMatchType;
import oasis.names.tc.xacml._2_0.policy.schema.os.SubjectType;
import oasis.names.tc.xacml._2_0.policy.schema.os.TargetType;
import org.apache.log4j.Logger;
import org.w3c.dom.Element;

/**
 * Java implementation of the adapter PDP service.
 *
 * @author Mastan.Ketha
 */
public class AdapterPDPProxyJavaImpl implements AdapterPDPProxy {

    private static final Logger LOG = Logger.getLogger(AdapterPDPProxyJavaImpl.class);
    private String statusCodeValue = "";
    private String statusMessageValue = "";
    private boolean foundMatchingAttributes = false;

    /**
     * processPDPRequest process the pdp request and evaluates the policy to permit or deny
     *
     * @param pdpRequest
     * @return pdpResponse
     */
    @Override
    public Response processPDPRequest(Request pdpRequest) {
        LOG.info("Begin AdapterPDPProxyJavaImpl.processPDPRequest(...)");
        EffectType effect = EffectType.DENY;
        PolicyType policyType = new PolicyType();
        try {
            String serviceType = getAttrValFromPdpRequest(pdpRequest,
                AdapterPDPConstants.REQUEST_CONTEXT_ATTRIBUTE_SERVICE_TYPE,
                AdapterPDPConstants.ATTRIBUTEVALUE_DATATYPE_STRING);
            LOG.debug("processPDPRequest - serviceType: " + serviceType);

            if (serviceType != null) {
                if ((serviceType.equalsIgnoreCase(AdapterPDPConstants.REQUEST_ACTION_PATIENT_DISCOVERY_OUT))
                    || (serviceType.equalsIgnoreCase(AdapterPDPConstants.REQUEST_ACTION_PATIENT_DISCOVERY_IN))
                    || (serviceType.equalsIgnoreCase(AdapterPDPConstants.REQUEST_ACTION_DOCUMENT_QUERY_OUT))
                    || (serviceType.equalsIgnoreCase(AdapterPDPConstants.REQUEST_ACTION_DOCUMENT_QUERY_IN))
                    || (serviceType.equalsIgnoreCase(AdapterPDPConstants.REQUEST_ACTION_DOCUMENT_RETRIEVE_IN))) {

                    DocumentQueryParams params = new DocumentQueryParams();
                    String patientId = getUniquePatientIdFromPdpRequest(pdpRequest, serviceType);
                    LOG.debug("processPDPRequest - patientid:" + patientId);
                    params.setPatientId(patientId);
                    List<String> classCodeValues = new ArrayList<String>();
                    classCodeValues.add(AdapterPDPConstants.DOCUMENT_CLASS_CODE);
                    params.setClassCodes(classCodeValues);
                    DocumentService service = new DocumentService();
                    List<Document> docs = service.documentQuery(params);
                    int docsSize = 0;
                    if ((docs != null) && (docs.size() > 0)) {
                        docsSize = docs.size();
                        LOG.debug("processPDPRequest - Policy Document Count:" + String.valueOf(docsSize));
                    } else {
                        LOG.debug("processPDPRequest - docs null/zero.");
                    }

                    String policyStrRawData = "";
                    if (docsSize == 1) {
                        for (Document doc : docs) {
                            byte[] rawData = doc.getRawData();
                            policyStrRawData = StringUtil.convertToStringUTF8(rawData);
                            LOG.debug("processPDPRequest - Policy rawData:" + policyStrRawData);
                        }
                    } else if (docsSize < 1) {
                        LOG.info("No policy documents found for the given criteria:");
                    } else if (docsSize > 1) {
                        LOG.info("More than one document found for the given criteria:");
                    }

                    if (policyStrRawData.trim().equals("")) {
                        LOG.info("No Policy info found for the given criteria:");
                    } else {
                        policyType = getPolicyObject(policyStrRawData);
                    }

                    if (pdpRequest == null) {
                        LOG.info("PDP request is null");
                    } else if (policyType == null) {
                        LOG.info("Policy is null");
                    } else {
                        effect = evaluatePolicy(pdpRequest, policyType);
                        effect = (effect == null) ? EffectType.DENY : effect;
                    }
                } else {
                    LOG.info("processPDPRequest - Permit for all other services except PD, QD and RD(in).");
                    effect = EffectType.PERMIT;
                }
            } else {
                LOG.info("processPDPRequest - Service Type is null");
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception ex) {
            effect = EffectType.DENY;
            LOG.error("Exception occured while retrieving documents");
            LOG.error(ex.getMessage());
        }

        LOG.info("processPDPRequest - Policy effect: " + effect.value());
        Response resp = createResponse(effect);
        LOG.info("End AdapterPDPProxyJavaImpl.processPDPRequest(...)");
        return resp;
    }

    private PolicyType getPolicyObject(String policyStrRawData) throws JAXBException {
        LOG.debug("Begin AdapterPDPProxyJavaImpl.getPolicyObject(...) ***");
        LOG.debug("getPolicyObject - Policy rawData:" + policyStrRawData);
        PolicyType policyType = new PolicyType();

        XACMLSerializer xACMLSerializer = new XACMLSerializer();
        try {
            policyType = xACMLSerializer.deserializeConsentXACMLDoc(policyStrRawData);
        } catch (AdapterPIPException ex) {
            LOG.error("getPolicyObject - Error occured while deserializing policy document");
        }
        if (policyType != null) {
            LOG.debug("getPolicyObject - Policy description:" + policyType.getDescription());
        } else {
            LOG.debug("getPolicyObject - Policy description: null");
        }
        return policyType;
    }

    private String getResourceIdFromPdpRequest(Request pdpRequest) {
        LOG.debug("Begin AdapterPDPProxyJavaImpl.getPatientIdFromPdpRequest()");
        List<Resource> resources = null;
        resources = (List<Resource>) pdpRequest.getResources();
        String attrValue = "";
        if (resources != null) {
            LOG.debug("Resources list size:" + resources.size());
            for (Resource resource : resources) {
                List<Attribute> attributes = null;
                attributes = (List<Attribute>) resource.getAttributes();
                LOG.debug("Attributes list size:" + attributes.size());
                for (Attribute attribute : attributes) {
                    String attrId = "";
                    String attrDataType = "";
                    if (attribute.getAttributeId() != null) {
                        attrId = attribute.getAttributeId().toString();
                        LOG.debug("AttributeId: " + attrId);
                    } else {
                        LOG.debug("AttributeId not found in the Attribute");
                    }
                    if (attribute.getDataType() != null) {
                        attrDataType = attribute.getDataType().toString();
                        LOG.debug("Attribute DataType : " + attrDataType);
                    } else {
                        LOG.debug("DataType not found in the Attribute");
                    }
                    if ((attrId.trim().equals(AdapterPDPConstants.REQUEST_CONTEXT_ATTRIBUTE_RESOURCEID))
                        && attrDataType.trim().equals(AdapterPDPConstants.ATTRIBUTEVALUE_DATATYPE_STRING)) {
                        Element sidElement = (Element) attribute.getAttributeValues().get(0);
                        attrValue = XMLUtils.getElementValue(sidElement);
                        LOG.debug("Attriute Value: " + attrValue);
                    }
                }
            }
        } else {
            LOG.info("No resources found in the Request context");
        }
        LOG.debug("End AdapterPDPProxyJavaImpl.getPatientIdFromPdpRequest()");
        return attrValue;
    }

    private String getAttrValFromPdpRequest(Request pdpRequest, String sAttrId, String sAttrDataType) {
        LOG.debug("Begin AdapterPDPProxyJavaImpl.getAttrValFromPdpRequest()");
        LOG.debug("getAttrValFromPdpRequest - sAttrId:" + sAttrId);
        LOG.debug("getAttrValFromPdpRequest - sAttrDataType:" + sAttrDataType);

        List<Resource> resources = null;
        resources = (List<Resource>) pdpRequest.getResources();
        String attrValue = "";
        if (resources != null) {
            LOG.debug("Resources list size:" + resources.size());
            for (Resource resource : resources) {
                List<Attribute> attributes = null;
                attributes = (List<Attribute>) resource.getAttributes();
                LOG.debug("Attributes list size:" + attributes.size());
                for (Attribute attribute : attributes) {
                    String attrId = "";
                    String attrDataType = "";
                    if (attribute.getAttributeId() != null) {
                        attrId = attribute.getAttributeId().toString();
                        LOG.debug("AttributeId: " + attrId);
                    } else {
                        LOG.debug("AttributeId not found in the Attribute");
                    }
                    if (attribute.getDataType() != null) {
                        attrDataType = attribute.getDataType().toString();
                        LOG.debug("Attribute DataType : " + attrDataType);
                    } else {
                        LOG.debug("DataType not found in the Attribute");
                    }
                    if ((attrId.trim().equals(sAttrId)) && (attrDataType.trim().equals(sAttrDataType))) {
                        Element sidElement = (Element) attribute.getAttributeValues().get(0);
                        attrValue = XMLUtils.getElementValue(sidElement);
                    }
                }
            }
        } else {
            LOG.info("No resources found in the Request context");
        }
        if (attrValue != null) {
            attrValue = attrValue.trim();
        }
        LOG.debug("getAttrValFromPdpRequest - attrValue:" + attrValue);

        LOG.debug("End AdapterPDPProxyJavaImpl.getAttrValFromPdpRequest()");
        return attrValue;
    }

    private String getUniquePatientIdFromPdpRequest(Request pdpRequest, String serviceType) {
        LOG.debug("Begin AdapterPDPProxyJavaImpl.getPatientIdFromPdpRequest()");
        String uniquePatientId = "";
        if ((serviceType != null) && (serviceType.equalsIgnoreCase("DocumentRetrieveIn"))) {
            LOG.debug("getPatientIdFromPdpRequest() - serviceType: inside DocumentRetrieveIn");
            String uniqueDocumentId = getAttrValFromPdpRequest(pdpRequest,
                AdapterPDPConstants.REQUEST_CONTEXT_ATTRIBUTE_RESOURCEID,
                AdapterPDPConstants.ATTRIBUTEVALUE_DATATYPE_STRING);
            LOG.debug("getPatientIdFromPdpRequest() - DocumentRetrieveIn uniqueDocumentId: " + uniqueDocumentId);
            uniquePatientId = getPatientIdByDocumentUniqueId(uniqueDocumentId);
            LOG.debug("getUniquePatientIdFromPdpRequest - DocumentRetrieveIn uniquePatientId: " + uniquePatientId);
        } else {
            String resourceId = getAttrValFromPdpRequest(pdpRequest,
                AdapterPDPConstants.REQUEST_CONTEXT_ATTRIBUTE_RESOURCEID,
                AdapterPDPConstants.ATTRIBUTEVALUE_DATATYPE_STRING);
            String aaId = getAttrValFromPdpRequest(pdpRequest, AdapterPDPConstants.REQUEST_CONTEXT_ATTRIBUTE_AA_ID,
                AdapterPDPConstants.ATTRIBUTEVALUE_DATATYPE_STRING);

            uniquePatientId = (resourceId + "^^^&" + aaId + "&ISO");
        }
        LOG.debug("getUniquePatientIdFromPdpRequest - uniquePatientId: " + uniquePatientId);

        LOG.debug("End AdapterPDPProxyJavaImpl.getPatientIdFromPdpRequest()");
        return uniquePatientId;
    }

    private String getPatientIdByDocumentUniqueId(String documentUniqueId) {

        String patientId = "";
        DocumentQueryParams params = new DocumentQueryParams();
        List<String> docIds = new ArrayList<String>();
        docIds.add(documentUniqueId);
        params.setDocumentUniqueId(docIds);
        List<Document> docs = new DocumentService().documentQuery(params);
        int docsSize = 0;
        if ((docs != null) && (docs.size() > 0)) {
            docsSize = docs.size();
            LOG.debug("getPatientIdByDocumentUniqueId - Document size:" + String.valueOf(docsSize));
            patientId = docs.get(0).getPatientId();
        } else {
            LOG.debug("getPatientIdByDocumentId - docs null/zero.");
        }

        return patientId;
    }

    private EffectType evaluatePolicy(Request pdpRequest, PolicyType policy) {
        LOG.debug("Begin AdapterPDPProxyJavaImpl.evaluatePolicy()");
        boolean isMatch = false;
        statusCodeValue = "";
        statusMessageValue = "";
        EffectType effect = EffectType.DENY;
        try {
            if (policy != null) {
                if (policy.getTarget() == null) {
                    LOG.info("Policy Target is null. Return Effect value Deny");
                    return EffectType.DENY;
                }
                List<RuleType> rules = new ArrayList<RuleType>();
                // rules = policy.getRule();
                if ((policy.getCombinerParametersOrRuleCombinerParametersOrVariableDefinition() != null)) {
                    LOG.debug("getCombinerParametersOrRuleCombinerParametersOrVariableDefinition list size: "
                        + policy.getCombinerParametersOrRuleCombinerParametersOrVariableDefinition().size());
                    for (Object obj : policy.getCombinerParametersOrRuleCombinerParametersOrVariableDefinition()) {
                        if (obj instanceof RuleType) {
                            rules.add((RuleType) obj);
                        }
                    }
                } else {
                    LOG.debug("getCombinerParametersOrRuleCombinerParametersOrVariableDefinition list size: null");
                }
                if ((rules != null) && (rules.size() > 0)) {
                    LOG.debug("Rules list size: " + rules.size());
                    String policyMatchId = "";
                    String policyAttrValue = "";
                    String policyAttrDataType = "";
                    String policyAttrDesAttrId = "";
                    String policyAttrDesAttrDataType = "";
                    rulesFor:
                        for (RuleType rule : rules) {
                            effect = rule.getEffect();
                            LOG.debug("Rule Effect value: " + effect);
                            TargetType targetType = null;
                            targetType = (rule.getTarget() == null) ? policy.getTarget() : rule.getTarget();

                            if (targetType != null) {
                                if (targetType.getSubjects() != null) {
                                    List<SubjectType> subjects = null;
                                    subjects = targetType.getSubjects().getSubject();
                                    if ((subjects != null) && (subjects.size() > 0)) {
                                        LOG.debug("Subjects list size" + subjects.size());
                                        subjectsFor:
                                            for (SubjectType subject : subjects) {
                                                isMatch = false;
                                                List<SubjectMatchType> subjectMatchs = null;
                                                subjectMatchs = subject.getSubjectMatch();
                                                if ((subjectMatchs != null) && (subjectMatchs.size() > 0)) {
                                                    LOG.debug("subjectMatchs list size" + subjectMatchs.size());
                                                    subjectMatchsFor:
                                                        for (SubjectMatchType subjectMatch : subjectMatchs) {
                                                            policyMatchId = subjectMatch.getMatchId();
                                                            LOG.debug("SubjectMatch MatchId: " + policyMatchId);
                                                            policyAttrValue = null;
                                                            policyAttrDataType = null;
                                                            if (subjectMatch.getAttributeValue() != null) {
                                                                if (subjectMatch.getAttributeValue().getContent() != null) {
                                                                    policyAttrValue = (String) subjectMatch.getAttributeValue()
                                                                        .getContent().get(0);
                                                                }
                                                                policyAttrDataType = (subjectMatch.getAttributeValue()
                                                                    .getDataType() == null) ? subjectMatch.getAttributeValue()
                                                                    .getDataType() : subjectMatch.getAttributeValue()
                                                                    .getDataType().trim();
                                                                LOG.debug("AttributeValue Value: " + policyAttrValue);
                                                                LOG.debug("AttributeValue DataType: " + policyAttrDataType);
                                                            } else {
                                                                LOG.debug("AttributeValue is null!");
                                                            }
                                                            if (subjectMatch.getSubjectAttributeDesignator() != null) {
                                                                policyAttrDesAttrId = (subjectMatch.getSubjectAttributeDesignator()
                                                                    .getAttributeId() == null) ? subjectMatch
                                                                    .getSubjectAttributeDesignator().getAttributeId()
                                                                    : subjectMatch.getSubjectAttributeDesignator()
                                                                    .getAttributeId().trim();
                                                                policyAttrDesAttrDataType = (subjectMatch
                                                                    .getSubjectAttributeDesignator().getDataType() == null) ? subjectMatch
                                                                    .getSubjectAttributeDesignator().getDataType()
                                                                    : subjectMatch.getSubjectAttributeDesignator()
                                                                    .getDataType().trim();
                                                                LOG.debug("SubjectAttributeDesignator DataType: "
                                                                    + policyAttrDesAttrDataType);
                                                                LOG.debug("SubjectAttributeDesignator AttributeId: "
                                                                    + policyAttrDesAttrId);
                                                                foundMatchingAttributes = false;
                                                                isMatch = evaluateSubjectMatch(pdpRequest, policyMatchId,
                                                                    policyAttrValue, policyAttrDesAttrId,
                                                                    policyAttrDesAttrDataType);
                                                                if (!foundMatchingAttributes) {
                                                                    isMatch = false;
                                                                    effect = EffectType.DENY;
                                                                    statusCodeValue = AdapterPDPConstants.POLICY_RESULT_STATUS_CODE_MISSING_ATTRIBUTE;
                                                                    statusMessageValue = AdapterPDPConstants.POLICY_RESULT_STATUS_MESSAGE_MISSING_ATTRIBUTE
                                                                        + " : "
                                                                        + policyAttrDesAttrId
                                                                        + " is incorrect or its info is missing in request context";
                                                                    break rulesFor;
                                                                }
                                                            } else {
                                                                LOG.debug("SubjectAttributeDesignator is null!");
                                                            }
                                                            if (!isMatch) {
                                                                break;
                                                            }
                                                        }
                                                } else {
                                                    LOG.debug("SubjectMatch not found!");
                                                }
                                                if (isMatch) {
                                                    break rulesFor;
                                                }
                                            }
                                    } else {
                                        LOG.debug("Rule Subject not found!");
                                    }
                                } else {
                                    LOG.debug("Rule Subjects is null");
                                    break;
                                }
                            } else {
                                LOG.debug("Rule Target is null");
                            }
                        }
                } else {
                    LOG.debug("Rules not found in policy document");
                }
            } else {
                LOG.info("Policy is null");
            }
        } catch (Exception ex) {
            statusCodeValue = AdapterPDPConstants.POLICY_RESULT_STATUS_CODE_PROCESSING_ERROR;
            statusMessageValue = AdapterPDPConstants.POLICY_RESULT_STATUS_MESSAGE_PROCESSING_ERROR;
            LOG.error("Exception occured while retrieving documents");
            LOG.error(ex.getMessage());
        }
        LOG.debug("End AdapterPDPProxyJavaImpl.evaluatePolicy()");
        LOG.debug("Rule Effect value: " + effect);
        return effect;
    }

    private boolean evaluateSubjectMatch(Request pdpRequest, String policyMatchId, String policyAttrValue,
        String policyAttrDesAttrId, String policyAttrDesAttrDataType) {
        LOG.debug("Begin AdapterPDPProxyJavaImpl.evaluateSubjectMatch()");
        boolean isMatch = false;
        List<Subject> subjects = null;
        subjects = (List<Subject>) pdpRequest.getSubjects();
        foundMatchingAttributes = false;
        LOG.debug("evaluateSubjectMatch - policyMatchId: " + policyMatchId);
        LOG.debug("evaluateSubjectMatch - policyAttrValue: " + policyAttrValue);
        LOG.debug("evaluateSubjectMatch - policyAttrDesAttrId: " + policyAttrDesAttrId);
        LOG.debug("evaluateSubjectMatch - policyAttrDesAttrDataType: " + policyAttrDesAttrDataType);
        if (policyMatchId == null) {
            LOG.debug("Policy - MatchId is null");
        }
        if (policyAttrValue == null) {
            LOG.debug("Policy - AttrValue is null");
        }
        if (policyAttrDesAttrId == null) {
            LOG.debug("Policy - AttributeId is null");
        }
        if (policyAttrDesAttrDataType == null) {
            LOG.debug("Policy - policyAttrDataType is null");
        }
        if (subjects != null) {
            LOG.debug("Subjects list size:" + subjects.size());
            boolean isAnyAttributeInfoNull = false;
            subjectsFor:
                for (Subject subject : subjects) {
                    List<Attribute> attributes = null;
                    attributes = (List<Attribute>) subject.getAttributes();
                    attributesFor:
                        for (Attribute attribute : attributes) {
                            String requestAttrId = "";
                            String requestAttrDataType = "";
                            String requestAttrValue = "";
                            isAnyAttributeInfoNull = false;
                            if (attribute.getAttributeId() != null) {
                                requestAttrId = attribute.getAttributeId().toString().trim();
                                LOG.debug("Request AttributeId: " + requestAttrId);
                            } else {
                                isAnyAttributeInfoNull = true;
                                LOG.debug("Request AttributeId is null");
                            }
                            if (attribute.getDataType() != null) {
                                requestAttrDataType = attribute.getDataType().toString().trim();
                                LOG.debug("Request Attribute DataType : " + requestAttrDataType);
                            } else {
                                isAnyAttributeInfoNull = true;
                                LOG.debug("Request DataType not found in the Attribute");
                            }
                            if (attribute.getAttributeValues() != null) {
                                Element sidElement = (Element) attribute.getAttributeValues().get(0);
                                requestAttrValue = (XMLUtils.getElementValue(sidElement) == null) ? XMLUtils
                                    .getElementValue(sidElement) : XMLUtils.getElementValue(sidElement).trim();
                                LOG.debug("Request Attriute Value: " + requestAttrValue);
                            } else {
                                isAnyAttributeInfoNull = true;
                                LOG.debug("Request Attriute Value not found in the Attribute");
                            }
                            if (!isAnyAttributeInfoNull) {
                                LOG.debug("evaluateSubjectMatch - Request AttributeId: " + requestAttrId);
                                LOG.debug("evaluateSubjectMatch - Request Attribute DataType : " + requestAttrDataType);
                                if ((policyAttrDesAttrId.equals(requestAttrId))
                                    && (policyAttrDesAttrDataType.equals(requestAttrDataType))) {
                                    isMatch = evaluateMatchWithFunction(policyMatchId, policyAttrValue, requestAttrValue);
                                    foundMatchingAttributes = true;
                                }
                            }
                            LOG.debug("evaluateSubjectMatch - loop - isMatch: " + isMatch);
                            if (isMatch) {
                                break subjectsFor;
                            }
                        }
                }
        } else {
            LOG.info("No subjects found in the Request context");
        }
        LOG.debug("evaluateSubjectMatch - isMatch: " + isMatch);
        return isMatch;
    }

    private boolean evaluateMatchWithFunction(String policyMatchId, String policyAttrValue, String requestAttrValue) {
        boolean isMatch = false;
        if (policyMatchId.equals(AdapterPDPConstants.MATCHID_FUNCTION_STRING_EQUAL)) {
            isMatch = evaluateMatchWithStringEqualFunction(policyAttrValue, requestAttrValue);
        } else if (policyMatchId.equals(AdapterPDPConstants.MATCHID_FUNCTION_ANYURI_EQUAL)) {
            isMatch = evaluateMatchWithAnyUriEqualFunction(policyAttrValue, requestAttrValue);
        }
        // log.debug("evaluateMatchWithFunction - isMatch: " + isMatch);
        return isMatch;
    }

    private boolean evaluateMatchWithStringEqualFunction(String policyAttrValue, String requestAttrValue) {
        boolean isMatch = false;
        if ((policyAttrValue == null) || (policyAttrValue.equals(""))) {
            LOG.debug("Policy Attribute Value is null or empty");
        } else if ((requestAttrValue == null) || (requestAttrValue.equals(""))) {
            LOG.debug("Request Attribute Value is null or empty");
        } else {
            if (policyAttrValue.trim().equalsIgnoreCase(requestAttrValue)) {
                isMatch = true;
            }
        }
        // log.debug("evaluateMatchWithStringEqualFunction -isMatch: " + isMatch);
        return isMatch;
    }

    private boolean evaluateMatchWithAnyUriEqualFunction(String policyAttrValue, String requestAttrValue) {
        // Need to work on this
        boolean isMatch = false;
        isMatch = evaluateMatchWithStringEqualFunction(policyAttrValue, requestAttrValue);
        // log.debug("evaluateMatchWithAnyUriEqualFunction -isMatch: " + isMatch);

        return isMatch;
    }

    private Response createResponse(EffectType effect) {
        Response response = null;
        try {
            response = ContextFactory.getInstance().createResponse();
            response.addResult(createResult(effect));
            LOG.debug("response-xml:" + response.toXMLString());
        } catch (XACMLException e) {
            LOG.error("Error adding a result: " + e.getMessage(), e);
        } catch (URISyntaxException u) {
            LOG.error("Error adding a result: " + u.getMessage(), u);
        }

        return response;
    }

    private Result createResult(EffectType effect) throws URISyntaxException {
        LOG.info("Begin AdapterPDPProxyJavaImpl.createResult(...)");
        Result result = null;
        try {
            Decision decision = ContextFactory.getInstance().createDecision();
            Status status = ContextFactory.getInstance().createStatus();
            StatusCode statusCode = ContextFactory.getInstance().createStatusCode();
            StatusMessage statusMessage = ContextFactory.getInstance().createStatusMessage();
            result = ContextFactory.getInstance().createResult();

            if (statusCodeValue.equals("")) {
                statusCodeValue = AdapterPDPConstants.POLICY_RESULT_STATUS_CODE_OK;
                statusMessageValue = AdapterPDPConstants.POLICY_RESULT_STATUS_MESSAGE_OK;
            }

            statusMessageValue = (statusMessageValue == null) ? "" : statusMessageValue;
            statusCode.setValue(statusCodeValue);
            statusMessage.setValue(statusMessageValue);

            decision.setValue(effect.value());
            status.setStatusCode(statusCode);
            status.setStatusMessage(statusMessage);
            result.setStatus(status);
            result.setDecision(decision);

        } catch (XACMLException e) {
            LOG.error("Error in setting decision and status: " + e.getMessage(), e);
        }
        LOG.info("End AdapterPDPProxyJavaImpl.createResult(...)");
        return result;
    }
}
