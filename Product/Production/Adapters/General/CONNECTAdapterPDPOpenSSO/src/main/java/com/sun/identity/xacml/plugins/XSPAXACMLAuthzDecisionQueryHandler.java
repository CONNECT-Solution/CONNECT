/*
 * Copyright (c) 2009-2015, United States Government, as represented by the Secretary of Health and Human Services.
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
package com.sun.identity.xacml.plugins;

import com.sun.identity.saml2.assertion.Assertion;
import com.sun.identity.saml2.assertion.AssertionFactory;
import com.sun.identity.saml2.assertion.Issuer;
import com.sun.identity.saml2.common.SAML2Exception;
import com.sun.identity.saml2.protocol.ProtocolFactory;
import com.sun.identity.saml2.protocol.RequestAbstract;
import com.sun.identity.saml2.soapbinding.RequestHandler;
import com.sun.identity.shared.xml.XMLUtils;
import com.sun.identity.xacml.common.XACMLConstants;
import com.sun.identity.xacml.common.XACMLException;
import com.sun.identity.xacml.context.Action;
import com.sun.identity.xacml.context.Attribute;
import com.sun.identity.xacml.context.ContextFactory;
import com.sun.identity.xacml.context.Decision;
import com.sun.identity.xacml.context.Environment;
import com.sun.identity.xacml.context.Request;
import com.sun.identity.xacml.context.Resource;
import com.sun.identity.xacml.context.Response;
import com.sun.identity.xacml.context.Result;
import com.sun.identity.xacml.context.Status;
import com.sun.identity.xacml.context.StatusCode;
import com.sun.identity.xacml.context.StatusDetail;
import com.sun.identity.xacml.context.StatusMessage;
import com.sun.identity.xacml.context.Subject;
import com.sun.identity.xacml.policy.Obligation;
import com.sun.identity.xacml.policy.Obligations;
import com.sun.identity.xacml.policy.PolicyFactory;
import com.sun.identity.xacml.saml2.XACMLAuthzDecisionQuery;
import com.sun.identity.xacml.saml2.XACMLAuthzDecisionStatement;
import com.sun.identity.xacml.spi.ActionMapper;
import com.sun.identity.xacml.spi.EnvironmentMapper;
import com.sun.identity.xacml.spi.ResourceMapper;
import com.sun.identity.xacml.spi.ResultMapper;
import com.sun.identity.xacml.spi.SubjectMapper;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.xml.soap.SOAPMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

/**
 * This class is an implementation of SAML2 query RequestHandler to handle XACMLAuthzDecisionQuery
 *
 */
public class XSPAXACMLAuthzDecisionQueryHandler implements RequestHandler {

    private static final Logger LOG = LoggerFactory.getLogger(XSPAXACMLAuthzDecisionQueryHandler.class);

    // effect
    public static final String PERMIT = "Permit";
    public static final String DENY = "Deny";
    public static final String INDETERMINATE = "Indetermiante";
    public static final String NOT_APPLICABLE = "NotApplicable";

    // subject attributes
    public static final String USER_ID = "urn:oasis:names:tc:xacml:2.0:subject:subject-id";
    public static final String USER_LOCALITY = "urn:oasis:names:tc:xacml:2.0:subject:locality";
    public static final String USER_ROLE = "urn:oasis:names:tc:xacml:2.0:subject:role";
    public static final String PURPOSE_OF_USE = "urn:oasis:names:tc:xspa:1.0:subject:purposeofuse";

    // resource attributes
    public static final String RESOURCE_ID = "urn:oasis:names:tc:xacml:2.0:resource:resource-id";
    public static final String HOME_COMMUNITY_ID = "urn:gov:hhs:fha:nhinc:assigning-authority-id";
    public static final String SERVICE_TYPE = "urn:gov:hhs:fha:nhinc:service-type";
    public static final String OPT_IN = "urn:gov:hhs:fha:nhinc:patient-opt-in";
    // action attributes
    public static final String ACTION_ID = "urn:oasis:names:tc:xacml:1.0:action:action-id";

    // environment attributes
    public static final String ENVIRONMENT_LOCALITY = "urn:oasis:names:tc:xacml:2.0:resource:locality";

    /**
     * This class is an implementation of SAML2 query RequestHandler to handle XACMLAuthzDecisionQuery
     *
     */
    public XSPAXACMLAuthzDecisionQueryHandler() {
    }

    /**
     * Processes an XACMLAuthzDecisionQuery and returns a SAML2 Response.
     *
     * @param pdpEntityId EntityID of PDP
     * @param pepEntityId EntityID of PEP
     * @param samlpRequest SAML2 Request, an XAMLAuthzDecisionQuery
     * @param soapMessage SOAPMessage that carried the SAML2 Request
     * @return SAML2 Response with an XAMLAuthzDecisionStatement
     * @exception SAML2Exception if the query can not be handled
     */
    @Override
    public com.sun.identity.saml2.protocol.Response handleQuery(String pdpEntityId, String pepEntityId,
        RequestAbstract samlpRequest, SOAPMessage soapMessage) throws SAML2Exception {

        LOG.info("Entering XSPAXACMLAuthzDecisionQueryHandler.handleQuery() with " + ":pdpEntityId="
            + pdpEntityId + ":pepEntityId=" + pepEntityId + ":samlpRequest=\n"
            + samlpRequest.toXMLString(true, true) + ":soapMessage=\n" + soapMessage);

        SubjectMapper subjectMapper = new FMSubjectMapper();
        subjectMapper.initialize(pdpEntityId, pepEntityId, null);
        ResourceMapper resourceMapper = new FMResourceMapper();
        resourceMapper.initialize(pdpEntityId, pepEntityId, null);
        ActionMapper actionMapper = new FMActionMapper();
        actionMapper.initialize(pdpEntityId, pepEntityId, null);
        EnvironmentMapper environmentMapper = new FMEnvironmentMapper();
        environmentMapper.initialize(pdpEntityId, pepEntityId, null);
        ResultMapper resultMapper = new FMResultMapper();
        resultMapper.initialize(pdpEntityId, pepEntityId, null);

        String effect = NOT_APPLICABLE;
        boolean evaluationFailed = false;
        String statusCodeValue = null;

        Request xacmlRequest = ((XACMLAuthzDecisionQuery) samlpRequest).getRequest();
        boolean returnContext = ((XACMLAuthzDecisionQuery) samlpRequest).getReturnContext();

        boolean permitAccess = false;
        String obligationId = null; // obligation on emergency, uba, ma
        String fullfillOn = null;

        // subject attributes
        String userId;
        Set userRoles;
        String userLocality;
        String pou; // purpose of use

        // resource attributes
        String resourceId = null;
        String communityId;
        String serviceType;
        boolean optIn;

        // action attributes
        String actionId;

        // environment attributes
        String environmentLocality;

        String detailText = "";

        try {

            // subject attributes
            userId = getUserId(xacmlRequest);
            userRoles = getUserRoles(xacmlRequest);
            userLocality = getUserLocality(xacmlRequest);

            // resource attributes
            resourceId = getResourceId(xacmlRequest);
            pou = getPurposeOfUse(xacmlRequest);
            communityId = getCommunityId(xacmlRequest);
            serviceType = getServiceType(xacmlRequest);
            optIn = isOptIn(xacmlRequest);

            // action attributes
            actionId = getActionId(xacmlRequest);

            // environment attributes
            environmentLocality = getEnvironmentLocality(xacmlRequest);

            LOG.info("xspa.handleQuery():\n" + "userId = " + userId + "\n" + "Roles = " + userRoles + "\n"
                + "resourceId = " + resourceId + "\n" + "purpose = " + pou + "\n" + "communityId = " + communityId
                + "\n" + "serviceType = " + serviceType + "\n" + "OptIN = " + Boolean.toString(optIn) + "\n"
                + "userLocality = " + userLocality + "\n" + "environmentLocality = " + environmentLocality + "\n"
                + "actionId = " + actionId + "\n");

            // BEGIN CUSTOM BUS LOGIC
            if (optIn) {
                effect = PERMIT;
                detailText = detailText + "PERMIT based upon OPT-IN";
                LOG.info("xspa.handleQuery():" + "Permit based upon OPT-IN");
            } else {
                effect = DENY;
                detailText = detailText + "DENY: based upon OPT-OUT";
                LOG.warn("xspa.handleQuery():" + "DENY: based upon OPT-OUT");
            }

        } catch (Exception e) {
            statusCodeValue = XACMLConstants.STATUS_CODE_MISSING_ATTRIBUTE;
            evaluationFailed = true;
            LOG.error("XSPAXACMLAuthzDecisionQueryHandler.handleQuery(), caught exception: {}",
                e.getLocalizedMessage(), e);
        }

        // decision: Indeterminate, Deny, Permit, NotApplicable
        // status code: missing_attribute, syntax_error, processing_error, ok
        Decision decision = ContextFactory.getInstance().createDecision();
        Status status = ContextFactory.getInstance().createStatus();
        StatusCode code = ContextFactory.getInstance().createStatusCode();
        StatusMessage message = ContextFactory.getInstance().createStatusMessage();
        StatusDetail detail = ContextFactory.getInstance().createStatusDetail();
        // Try this
        // detail.getElement().insertBefore(detail.getElement().cloneNode(true), null);
        // Instead of this
        try {
            Document doc = detail.getElement().getOwnerDocument();
            Text textNode = doc.createTextNode(detailText);
            detail.getElement().insertBefore(textNode, null);
        } catch (Exception e) {
            LOG.error("XSPAXACMLAuthzDecisionQueryHandler.handleQuery(), caught exception: {}",
                e.getLocalizedMessage(), e);
        }

        if (PERMIT.equals(effect)) {
            permitAccess = true;
        }

        if (evaluationFailed) {
            decision.setValue(XACMLConstants.INDETERMINATE);
            if (statusCodeValue == null) {
                statusCodeValue = XACMLConstants.STATUS_CODE_PROCESSING_ERROR;
            }
            code.setValue(statusCodeValue);
            message.setValue("processing_error");
        } else if (permitAccess) {
            decision.setValue(XACMLConstants.PERMIT);
            code.setValue(XACMLConstants.STATUS_CODE_OK);
            message.setValue("ok");
        } else {
            decision.setValue(XACMLConstants.DENY);
            code.setValue(XACMLConstants.STATUS_CODE_OK);
            message.setValue("ok");
        }

        Result result = ContextFactory.getInstance().createResult();
        result.setResourceId(resourceId);
        result.setDecision(decision);

        status.setStatusCode(code);
        status.setStatusMessage(message);
        status.setStatusDetail(detail);
        result.setStatus(status);

        if (obligationId != null) {
            Obligations obligations = createObligations(obligationId, permitAccess);
            result.setObligations(obligations);
        }

        Response response = ContextFactory.getInstance().createResponse();
        response.addResult(result);

        XACMLAuthzDecisionStatement statement = ContextFactory.getInstance().createXACMLAuthzDecisionStatement();
        statement.setResponse(response);
        if (returnContext) {
            // statement.setRequest(xacmlRequest);
        }

        com.sun.identity.saml2.protocol.Response samlpResponse = createSamlpResponse(statement, status.getStatusCode()
            .getValue());

        LOG.info("XSPAXACMLAuthzDecisionQueryHandler.handleQuery(), returning " + ":samlResponse=\n"
            + samlpResponse.toXMLString(true, true));

        return samlpResponse;
    }

    // END CUSTOM BUS LOGIC
    // BEGIN HELPER METHODS
    private com.sun.identity.saml2.protocol.Response createSamlpResponse(XACMLAuthzDecisionStatement statement,
        String statusCodeValue) throws XACMLException, SAML2Exception {

        com.sun.identity.saml2.protocol.Response samlpResponse = ProtocolFactory.getInstance().createResponse();
        samlpResponse.setID("response-id:1");
        samlpResponse.setVersion("2.0");
        samlpResponse.setIssueInstant(new Date());

        com.sun.identity.saml2.protocol.StatusCode samlStatusCode = ProtocolFactory.getInstance().createStatusCode();
        samlStatusCode.setValue(statusCodeValue);
        com.sun.identity.saml2.protocol.Status samlStatus = ProtocolFactory.getInstance().createStatus();
        samlStatus.setStatusCode(samlStatusCode);
        samlpResponse.setStatus(samlStatus);

        Assertion assertion = AssertionFactory.getInstance().createAssertion();
        assertion.setVersion("2.0");
        assertion.setID("response-id:1");
        assertion.setIssueInstant(new Date());
        Issuer issuer = AssertionFactory.getInstance().createIssuer();
        issuer.setValue("issuer-1");
        assertion.setIssuer(issuer);
        List statements = new ArrayList();
        statements.add(statement.toXMLString(true, true)); // add decisionstatement
        assertion.setStatements(statements);
        List assertions = new ArrayList();
        assertions.add(assertion);
        samlpResponse.setAssertion(assertions);

        return samlpResponse;
    }

    private Obligations createObligations(String obligationId, boolean permitAccess) throws XACMLException {
        Obligations obligations;
        try {
            Obligation obligation = PolicyFactory.getInstance().createObligation();
            obligation.setObligationId(new URI(obligationId));
            obligation.setFulfillOn(permitAccess ? PERMIT : DENY);
            obligations = PolicyFactory.getInstance().createObligations();
            obligations.addObligation(obligation);
        } catch (URISyntaxException | XACMLException e) {
            LOG.error("Error: failed to create obligations: {}", e.getLocalizedMessage(), e);
            throw new XACMLException("Error: failed to create obligations");
        }
        return obligations;
    }

    String getUserId(Request request) {
        String userId = null;
        List subjects = request.getSubjects();
        if ((subjects != null) && !subjects.isEmpty()) {
            Subject subject = (Subject) subjects.get(0);
            List attributes = subject.getAttributes();
            Attribute attr = getAttribute(attributes, USER_ID);
            if (attr != null) {
                userId = getStringValue(attr);
            }
        }
        return userId;
    }

    String getPurposeOfUse(Request request) {
        String pou = null;
        List subjects = request.getSubjects();
        if ((subjects != null) && !subjects.isEmpty()) {
            Subject subject = (Subject) subjects.get(0);
            List attributes = subject.getAttributes();
            Attribute attr = getAttribute(attributes, PURPOSE_OF_USE);
            if (attr != null) {
                pou = getStringValue(attr);
            }
        }
        return pou;
    }

    Set getUserRoles(Request request) {
        Set userRoles = new HashSet();
        List subjects = request.getSubjects();
        if ((subjects != null) && !subjects.isEmpty()) {
            Subject subject = (Subject) subjects.get(0);
            List attributes = subject.getAttributes();
            Attribute attr = getAttribute(attributes, USER_ROLE);
            if (attr != null) {
                userRoles = getStringValues(attr);
            }
        }
        return userRoles;
    }

    String getUserLocality(Request request) {
        String userLocality = null;
        List subjects = request.getSubjects();
        if ((subjects != null) && !subjects.isEmpty()) {
            Subject subject = (Subject) subjects.get(0);
            List attributes = subject.getAttributes();
            Attribute attr = getAttribute(attributes, USER_LOCALITY);
            if (attr != null) {
                userLocality = getStringValue(attr);
            }
        }
        return userLocality;
    }

    String getResourceId(Request request) {
        String resourceId = null;
        List resources = request.getResources();
        if ((resources != null) && !resources.isEmpty()) {
            Resource resource = (Resource) resources.get(0);
            List attributes = resource.getAttributes();
            Attribute attr = getAttribute(attributes, RESOURCE_ID);
            if (attr != null) {
                resourceId = getStringValue(attr);
            }
        }
        return resourceId;
    }

    String getCommunityId(Request request) {
        String communityId = null;
        List resources = request.getResources();
        if ((resources != null) && !resources.isEmpty()) {
            Resource resource = (Resource) resources.get(0);
            List attributes = resource.getAttributes();
            Attribute attr = getAttribute(attributes, HOME_COMMUNITY_ID);
            if (attr != null) {
                communityId = getStringValue(attr);
            }
        }
        return communityId;
    }

    String getServiceType(Request request) {
        String progressNoteAuthor = null;
        List resources = request.getResources();
        if ((resources != null) && !resources.isEmpty()) {
            Resource resource = (Resource) resources.get(0);
            List attributes = resource.getAttributes();
            Attribute attr = getAttribute(attributes, SERVICE_TYPE);
            if (attr != null) {
                progressNoteAuthor = getStringValue(attr);
            }
        }
        return progressNoteAuthor;
    }

    boolean isOptIn(Request request) {
        boolean optIn = false;
        String value;
        List resources = request.getResources();
        if ((resources != null) && !resources.isEmpty()) {
            Resource resource = (Resource) resources.get(0);
            List attributes = resource.getAttributes();
            Attribute attr = getAttribute(attributes, OPT_IN);
            if (attr != null) {
                value = getStringValue(attr);
                optIn = (value.equalsIgnoreCase("Yes")) ? true : false;
            }
        }
        return optIn;
    }

    String getActionId(Request request) {
        String actionId = null;
        Action action = request.getAction();
        if (action != null) {
            List attributes = action.getAttributes();
            Attribute attr = getAttribute(attributes, ACTION_ID);
            if (attr != null) {
                actionId = getStringValue(attr);
            }
        }
        return actionId;
    }

    String getEnvironmentLocality(Request request) {
        String environmentLocality = null;
        Environment environment = request.getEnvironment();
        if (environment != null) {
            List attributes = environment.getAttributes();
            Attribute attr = getAttribute(attributes, ENVIRONMENT_LOCALITY);
            if (attr != null) {
                environmentLocality = getStringValue(attr);
            }
        }
        return environmentLocality;
    }

    Attribute getAttribute(List attributes, String attributeId) {
        Attribute attribute = null;
        if (attributes != null) {
            for (int i = 0; i < attributes.size(); i++) {
                Attribute attr = (Attribute) attributes.get(i);
                if (attr != null) {
                    URI tmpURI = attr.getAttributeId();
                    if (tmpURI.toString().equals(attributeId)) {
                        attribute = attr;
                        break;
                    }
                }
            }
        }
        return attribute;
    }

    List getAttributes(List attributes, String attributeId) {
        List attrs = new ArrayList();
        if (attributes != null) {
            for (int i = 0; i < attributes.size(); i++) {
                Attribute attr = (Attribute) attributes.get(i);
                if (attr != null) {
                    URI tmpURI = attr.getAttributeId();
                    if (tmpURI.toString().equals(attributeId)) {
                        attrs.add(attr);
                    }
                }
            }
        }
        return attrs;
    }

    Set getStringValues(Attribute attribute) {
        Set values = new HashSet();
        if (attribute != null) {
            List vals = attribute.getAttributeValues();
            for (int i = 0; i < vals.size(); i++) {
                Element elem = (Element) vals.get(i);
                String val = XMLUtils.getElementValue(elem);
                values.add(val);
            }
        }
        return values;
    }

    Set getStringValues(List attributes) {
        Set values = new HashSet();
        for (Iterator iter = attributes.iterator(); iter.hasNext();) {
            Attribute attribute = (Attribute) iter.next();
            List vals = attribute.getAttributeValues();
            for (int i = 0; i < vals.size(); i++) {
                Element elem = (Element) vals.get(i);
                String val = XMLUtils.getElementValue(elem);
                values.add(val);
            }
        }
        return values;
    }

    String getStringValue(Attribute attribute) {
        String value = null;
        if (attribute != null) {
            List vals = attribute.getAttributeValues();
            if ((vals != null) && !vals.isEmpty()) {
                Element elem = (Element) vals.get(0);
                value = XMLUtils.getElementValue(elem);
            }
        }
        return value;
    }
    // END HELPER METHODS
}
