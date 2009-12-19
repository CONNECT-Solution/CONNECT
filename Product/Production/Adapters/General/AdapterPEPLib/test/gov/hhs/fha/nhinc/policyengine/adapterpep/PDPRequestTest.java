/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.policyengine.adapterpep;

import com.sun.identity.xacml.context.Attribute;
import com.sun.identity.xacml.context.Request;
import com.sun.identity.xacml.context.Subject;
import com.sun.identity.xacml.context.Resource;
import com.sun.identity.xacml.context.Action;
import com.sun.identity.xacml.context.Environment;
import com.sun.org.apache.xerces.internal.dom.DeferredElementNSImpl;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import oasis.names.tc.xacml._2_0.context.schema.os.ActionType;
import oasis.names.tc.xacml._2_0.context.schema.os.AttributeType;
import oasis.names.tc.xacml._2_0.context.schema.os.AttributeValueType;
import oasis.names.tc.xacml._2_0.context.schema.os.RequestType;
import oasis.names.tc.xacml._2_0.context.schema.os.ResourceType;
import oasis.names.tc.xacml._2_0.context.schema.os.SubjectType;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;

/**
 *
 * @author vvickers
 */
public class PDPRequestTest {

    private static final String PROPERTY_FILE_NAME_GATEWAY = "gateway";
    private static final String PROPERTY_FILE_KEY_HOME_COMMUNITY = "localHomeCommunityId";
    private static final String XACML_DATATYPE = "http://www.w3.org/2001/XMLSchema#string";
    private static final String XACML_SUBJECT_ID = "urn:oasis:names:tc:xacml:1.0:subject:subject-id"; // same
    private static final String XACML_SUBJECT_ORG = "urn:gov:hhs:fha:nhinc:user-organization-name";//new
    private static final String XACML_SUBJECT_ORG_ID = "urn:oasis:names:tc:xspa:1.0:subject:organization-id";//new
    private static final String XACML_SUBJECT_ROLE = "urn:oasis:names:tc:xacml:2.0:subject:role";//name changed
    private static final String XACML_SUBJECT_PURPOSE = "urn:oasis:names:tc:xspa:1.0:subject:purposeofuse";//name changed
    private static final String XACML_HOME_COMMUNITY = "urn:gov:hhs:fha:nhinc:home-community-id";//same
    private static final String XACML_RESOURCE_ID = "urn:oasis:names:tc:xacml:1.0:resource:resource-id";//same
    private static final String XACML_ASSIGING_AUTH = "urn:gov:hhs:fha:nhinc:assigning-authority-id";//same
    private static final String XACML_DOCUMENT_ID = "urn:gov:hhs:fha:nhinc:document-id";
    private static final String XACML_DOC_COMMUNITY_ID = "urn:gov:hhs:fha:nhinc:home-community-id";
    private static final String XACML_DOC_REPOSITORY_ID = "urn:gov:hhs:fha:nhinc:document-repository-id";
    private static final String XACML_SUBSCRIPTION_ID = "urn:gov:hhs:fha:nhinc:subscription-topic";
    private static final String XACML_ACTION = "urn:oasis:names:tc:xacml:1.0:action:action-id";
    private static final String XSPA_SUBJECT_ID = "urn:oasis:names:tc:xacml:1.0:subject:subject-id"; // 1.0 instead of 2.0
    private static final String XSPA_SUBJECT_ORG = "urn:oasis:names:tc:xspa:1.0:subject:organization";//new
    private static final String XSPA_SUBJECT_ORG_ID = "urn:oasis:names:tc:xspa:1.0:subject:organization-id";//new
    private static final String XSPA_SUBJECT_ROLE = "urn:oasis:names:tc:xacml:2.0:subject:role"; // same
    private static final String XSPA_SUBJECT_PURPOSE = "urn:oasis:names:tc:xspa:1.0:subject:purposeofuse"; //same
    private static final String XSPA_RESOURCE_ID = "urn:oasis:names:tc:xacml:1.0:resource:resource-id"; // 1.0 instead of 2.0
    private static final String XSPA_ENVIRONMENT_LOCALITY = "urn:oasis:names:tc:xspa:1.0:environment:locality";//new
    private static final String XSPA_PATIENT_OPT_IN = "urn:gov:hhs:fha:nhinc:patient-opt-in";//same
    private static final String XSPA_ASSIGNING_AUTH = "urn:gov:hhs:fha:nhinc:assigning-authority-id";//same
    private static final String XSPA_SERVICE_TYPE = "urn:gov:hhs:fha:nhinc:service-type";//same
    private static final String XSPA_ACTION = "urn:oasis:names:tc:xacml:1.0:action:action-id"; //same
    private static final String TEST_USER_ID = "user1";
    private static final String TEST_HC_ID = "Home_1.1";
    private static final String TEST_PATIENT_ID = "Patient_123456";
    private static final String TEST_ASSIGN_AUTH = "Authority1";
    private static final String TEST_USER_ORG = "UserHospital";
    private static final String TEST_USER_ORG_ID = "urn:oid:2.22.840.1.113883.2.222";
    private static final String TEST_USER_ROLE = "Doctor";
    private static final String TEST_USER_PURPOSE = "testPurpose";
    private static final String TEST_REIDENT_USER_ROLE = "307969004";
    private static final String TEST_REIDENT_USER_PURPOSE = "PUBLICHEALTH";
    private static final String TEST_REPOSITORY_ID = "DocRepository.Documents";
    private static final String TEST_DOC_ID = "Doc_987";
    private static final String TEST_SUBSCRIPTION_ID = "Subscribe_111";
    private Map<String, String> expectedSubjectXSPA = new HashMap<String, String>();
    private Map<String, String> expectedResourceXSPA = new HashMap<String, String>();
    private Map<String, String> expectedActionXSPA = new HashMap<String, String>();
    private Map<String, String> expectedEnvXSPA = new HashMap<String, String>();
    private Mockery mockery;

    @Before
    public void setUp() {
        mockery = new Mockery(){
            {
                setImposteriser(ClassImposteriser.INSTANCE);
            }
        };
    }

    /**
     * Note that Audit Log Query can be both Patient-Specific as well as non-patient Specific.
     * If it is patient-specific, then the Local Assigning Authority and Local Patient Id must be passed in.
     * If it is not a patient-specific query, then these attributes should not be passed in.
     */
    @Test
    public void testAuditPatientQueryIn() {

        expectedSubjectXSPA.clear();
        expectedSubjectXSPA.put(XSPA_SUBJECT_ID, TEST_USER_ID);
        expectedSubjectXSPA.put(XSPA_SUBJECT_ORG, TEST_USER_ORG);
        expectedSubjectXSPA.put(XSPA_SUBJECT_ORG_ID, TEST_USER_ORG_ID);
        expectedSubjectXSPA.put(XSPA_SUBJECT_ROLE, TEST_USER_ROLE);
        expectedSubjectXSPA.put(XSPA_SUBJECT_PURPOSE, TEST_USER_PURPOSE);

        expectedResourceXSPA.clear();
        expectedResourceXSPA.put(XSPA_RESOURCE_ID, TEST_PATIENT_ID);
        expectedResourceXSPA.put(XSPA_PATIENT_OPT_IN, "Yes");
        expectedResourceXSPA.put(XSPA_ASSIGNING_AUTH, TEST_ASSIGN_AUTH);
        expectedResourceXSPA.put(XSPA_SERVICE_TYPE, "AuditLogQueryIn");

        expectedActionXSPA.clear();
        expectedActionXSPA.put(XSPA_ACTION, "read");

        expectedEnvXSPA.clear();
        expectedEnvXSPA.put(XSPA_ENVIRONMENT_LOCALITY, TEST_HC_ID);

        CheckPolicyRequestType request = new CheckPolicyRequestType();
        RequestType reqType = new RequestType();
        request.setRequest(reqType);

        List<SubjectType> subjectList = request.getRequest().getSubject();
        SubjectType subject = new SubjectType();
        List<AttributeType> subjAttrs = subject.getAttribute();
        subjAttrs.add(createAttr(XACML_SUBJECT_ID, XACML_DATATYPE, TEST_USER_ID));
        subjAttrs.add(createAttr(XACML_HOME_COMMUNITY, XACML_DATATYPE, TEST_HC_ID));
        subjAttrs.add(createAttr(XACML_SUBJECT_ORG, XACML_DATATYPE, TEST_USER_ORG));
        subjAttrs.add(createAttr(XACML_SUBJECT_ORG_ID, XACML_DATATYPE, TEST_USER_ORG_ID));
        subjAttrs.add(createAttr(XACML_SUBJECT_ROLE, XACML_DATATYPE, TEST_USER_ROLE));
        subjAttrs.add(createAttr(XACML_SUBJECT_PURPOSE, XACML_DATATYPE, TEST_USER_PURPOSE));
        subjectList.add(subject);

        List<ResourceType> resourceList = request.getRequest().getResource();
        ResourceType resource = new ResourceType();
        List<AttributeType> resAttrs = resource.getAttribute();
        resAttrs.add(createAttr(XACML_RESOURCE_ID, XACML_DATATYPE, TEST_PATIENT_ID));
        resAttrs.add(createAttr(XACML_ASSIGING_AUTH, XACML_DATATYPE, TEST_ASSIGN_AUTH));
        resourceList.add(resource);

        ActionType action = new ActionType();
        List<AttributeType> actionAttrs = action.getAttribute();
        actionAttrs.add(createAttr(XACML_ACTION, XACML_DATATYPE, "AuditLogQueryIn"));
        request.getRequest().setAction(action);

        AdapterPEPImpl pepImpl = new AdapterPEPImpl() {
            protected List<String> determinePatientOptStatus(List<String> resourceIds, List<String> assigningAuths) {
                List<String> optStatusList = new ArrayList<String>();
                optStatusList.add("Yes");
                return optStatusList;
            }
        };
        Request pdpRequest = pepImpl.createPdpRequest(request);

        verifyXSPASubjects(pdpRequest);
        verifyXSPAResources(pdpRequest);
        verifyXSPAAction(pdpRequest);
        verifyXSPAEnvironment(pdpRequest);
    }

    /**
     * Note that Audit Log Query can be both Patient-Specific as well as non-patient Specific.
     * If it is patient-specific, then the Local Assigning Authority and Local Patient Id must be passed in.
     * If it is not a patient-specific query, then these attributes should not be passed in.
     */
    @Test
    public void testAuditPatientQueryOut() {

        expectedSubjectXSPA.clear();
        expectedSubjectXSPA.put(XSPA_SUBJECT_ID, TEST_USER_ID);
        expectedSubjectXSPA.put(XSPA_SUBJECT_ORG, TEST_USER_ORG);
        expectedSubjectXSPA.put(XSPA_SUBJECT_ORG_ID, TEST_USER_ORG_ID);
        expectedSubjectXSPA.put(XSPA_SUBJECT_ROLE, TEST_USER_ROLE);
        expectedSubjectXSPA.put(XSPA_SUBJECT_PURPOSE, TEST_USER_PURPOSE);

        expectedResourceXSPA.clear();
        expectedResourceXSPA.put(XSPA_RESOURCE_ID, TEST_PATIENT_ID);
        expectedResourceXSPA.put(XSPA_PATIENT_OPT_IN, "Yes");
        expectedResourceXSPA.put(XSPA_ASSIGNING_AUTH, TEST_ASSIGN_AUTH);
        expectedResourceXSPA.put(XSPA_SERVICE_TYPE, "AuditLogQueryOut");

        expectedActionXSPA.clear();
        expectedActionXSPA.put(XSPA_ACTION, "read");

        expectedEnvXSPA.clear();
        try {
            String homeCommunityId = PropertyAccessor.getProperty(PROPERTY_FILE_NAME_GATEWAY, PROPERTY_FILE_KEY_HOME_COMMUNITY);
            expectedEnvXSPA.put(XSPA_ENVIRONMENT_LOCALITY, homeCommunityId);
        } catch (PropertyAccessException ex) {
            fail("Can not access: " + PROPERTY_FILE_KEY_HOME_COMMUNITY + " property from: " + PROPERTY_FILE_NAME_GATEWAY);
        }

        CheckPolicyRequestType request = new CheckPolicyRequestType();
        RequestType reqType = new RequestType();
        request.setRequest(reqType);

        List<SubjectType> subjectList = request.getRequest().getSubject();
        SubjectType subject = new SubjectType();
        List<AttributeType> subjAttrs = subject.getAttribute();
        subjAttrs.add(createAttr(XACML_SUBJECT_ID, XACML_DATATYPE, TEST_USER_ID));
        subjAttrs.add(createAttr(XACML_SUBJECT_ORG, XACML_DATATYPE, TEST_USER_ORG));
        subjAttrs.add(createAttr(XACML_SUBJECT_ORG_ID, XACML_DATATYPE, TEST_USER_ORG_ID));
        subjAttrs.add(createAttr(XACML_SUBJECT_ROLE, XACML_DATATYPE, TEST_USER_ROLE));
        subjAttrs.add(createAttr(XACML_SUBJECT_PURPOSE, XACML_DATATYPE, TEST_USER_PURPOSE));
        subjectList.add(subject);

        List<ResourceType> resourceList = request.getRequest().getResource();
        ResourceType resource = new ResourceType();
        List<AttributeType> resAttrs = resource.getAttribute();
        resAttrs.add(createAttr(XACML_HOME_COMMUNITY, XACML_DATATYPE, TEST_HC_ID));
        resAttrs.add(createAttr(XACML_RESOURCE_ID, XACML_DATATYPE, TEST_PATIENT_ID));
        resAttrs.add(createAttr(XACML_ASSIGING_AUTH, XACML_DATATYPE, TEST_ASSIGN_AUTH));
        resourceList.add(resource);

        ActionType action = new ActionType();
        List<AttributeType> actionAttrs = action.getAttribute();
        actionAttrs.add(createAttr(XACML_ACTION, XACML_DATATYPE, "AuditLogQueryOut"));
        request.getRequest().setAction(action);

        AdapterPEPImpl pepImpl = new AdapterPEPImpl() {
            protected List<String> determinePatientOptStatus(List<String> resourceIds, List<String> assigningAuths) {
                List<String> optStatusList = new ArrayList<String>();
                optStatusList.add("Yes");
                return optStatusList;
            }
        };
        Request pdpRequest = pepImpl.createPdpRequest(request);

        verifyXSPASubjects(pdpRequest);
        verifyXSPAResources(pdpRequest);
        verifyXSPAAction(pdpRequest);
        verifyXSPAEnvironment(pdpRequest);
    }

    /**
     * Note that Audit Log Query can be both Patient-Specific as well as non-patient Specific.
     * If it is patient-specific, then the Local Assigning Authority and Local Patient Id must be passed in.
     * If it is not a patient-specific query, then these attributes should not be passed in.
     */
    @Test
    public void testAuditQueryIn() {

        expectedSubjectXSPA.clear();
        expectedSubjectXSPA.put(XSPA_SUBJECT_ID, TEST_USER_ID);
        expectedSubjectXSPA.put(XSPA_SUBJECT_ORG, TEST_USER_ORG);
        expectedSubjectXSPA.put(XSPA_SUBJECT_ORG_ID, TEST_USER_ORG_ID);
        expectedSubjectXSPA.put(XSPA_SUBJECT_ROLE, TEST_USER_ROLE);
        expectedSubjectXSPA.put(XSPA_SUBJECT_PURPOSE, TEST_USER_PURPOSE);

        expectedResourceXSPA.clear();
        expectedResourceXSPA.put(XSPA_PATIENT_OPT_IN, "Yes");
        expectedResourceXSPA.put(XSPA_SERVICE_TYPE, "AuditLogQueryIn");

        expectedActionXSPA.clear();
        expectedActionXSPA.put(XSPA_ACTION, "read");

        expectedEnvXSPA.clear();
        expectedEnvXSPA.put(XSPA_ENVIRONMENT_LOCALITY, TEST_HC_ID);

        CheckPolicyRequestType request = new CheckPolicyRequestType();
        RequestType reqType = new RequestType();
        request.setRequest(reqType);

        List<SubjectType> subjectList = request.getRequest().getSubject();
        SubjectType subject = new SubjectType();
        List<AttributeType> subjAttrs = subject.getAttribute();
        subjAttrs.add(createAttr(XACML_SUBJECT_ID, XACML_DATATYPE, TEST_USER_ID));
        subjAttrs.add(createAttr(XACML_HOME_COMMUNITY, XACML_DATATYPE, TEST_HC_ID));
        subjAttrs.add(createAttr(XACML_SUBJECT_ORG, XACML_DATATYPE, TEST_USER_ORG));
        subjAttrs.add(createAttr(XACML_SUBJECT_ORG_ID, XACML_DATATYPE, TEST_USER_ORG_ID));
        subjAttrs.add(createAttr(XACML_SUBJECT_ROLE, XACML_DATATYPE, TEST_USER_ROLE));
        subjAttrs.add(createAttr(XACML_SUBJECT_PURPOSE, XACML_DATATYPE, TEST_USER_PURPOSE));
        subjectList.add(subject);

        List<ResourceType> resourceList = request.getRequest().getResource();
        ResourceType resource = new ResourceType();
        resourceList.add(resource);

        ActionType action = new ActionType();
        List<AttributeType> actionAttrs = action.getAttribute();
        actionAttrs.add(createAttr(XACML_ACTION, XACML_DATATYPE, "AuditLogQueryIn"));
        request.getRequest().setAction(action);

        AdapterPEPImpl pepImpl = new AdapterPEPImpl();
        Request pdpRequest = pepImpl.createPdpRequest(request);

        verifyXSPASubjects(pdpRequest);
        verifyXSPAResources(pdpRequest);
        verifyXSPAAction(pdpRequest);
        verifyXSPAEnvironment(pdpRequest);
    }

    /**
     * Note that Audit Log Query can be both Patient-Specific as well as non-patient Specific.
     * If it is patient-specific, then the Local Assigning Authority and Local Patient Id must be passed in.
     * If it is not a patient-specific query, then these attributes should not be passed in.
     */
    @Test
    public void testAuditQueryOut() {

        expectedSubjectXSPA.clear();
        expectedSubjectXSPA.put(XSPA_SUBJECT_ID, TEST_USER_ID);
        expectedSubjectXSPA.put(XSPA_SUBJECT_ORG, TEST_USER_ORG);
        expectedSubjectXSPA.put(XSPA_SUBJECT_ORG_ID, TEST_USER_ORG_ID);
        expectedSubjectXSPA.put(XSPA_SUBJECT_ROLE, TEST_USER_ROLE);
        expectedSubjectXSPA.put(XSPA_SUBJECT_PURPOSE, TEST_USER_PURPOSE);

        expectedResourceXSPA.clear();
        expectedResourceXSPA.put(XSPA_PATIENT_OPT_IN, "Yes");
        expectedResourceXSPA.put(XSPA_SERVICE_TYPE, "AuditLogQueryOut");

        expectedActionXSPA.clear();
        expectedActionXSPA.put(XSPA_ACTION, "read");

        expectedEnvXSPA.clear();
        try {
            String homeCommunityId = PropertyAccessor.getProperty(PROPERTY_FILE_NAME_GATEWAY, PROPERTY_FILE_KEY_HOME_COMMUNITY);
            expectedEnvXSPA.put(XSPA_ENVIRONMENT_LOCALITY, homeCommunityId);
        } catch (PropertyAccessException ex) {
            fail("Can not access: " + PROPERTY_FILE_KEY_HOME_COMMUNITY + " property from: " + PROPERTY_FILE_NAME_GATEWAY);
        }

        CheckPolicyRequestType request = new CheckPolicyRequestType();
        RequestType reqType = new RequestType();
        request.setRequest(reqType);

        List<SubjectType> subjectList = request.getRequest().getSubject();
        SubjectType subject = new SubjectType();
        List<AttributeType> subjAttrs = subject.getAttribute();
        subjAttrs.add(createAttr(XACML_SUBJECT_ID, XACML_DATATYPE, TEST_USER_ID));
        subjAttrs.add(createAttr(XACML_SUBJECT_ORG, XACML_DATATYPE, TEST_USER_ORG));
        subjAttrs.add(createAttr(XACML_SUBJECT_ORG_ID, XACML_DATATYPE, TEST_USER_ORG_ID));
        subjAttrs.add(createAttr(XACML_SUBJECT_ROLE, XACML_DATATYPE, TEST_USER_ROLE));
        subjAttrs.add(createAttr(XACML_SUBJECT_PURPOSE, XACML_DATATYPE, TEST_USER_PURPOSE));
        subjectList.add(subject);

        List<ResourceType> resourceList = request.getRequest().getResource();
        ResourceType resource = new ResourceType();
        List<AttributeType> resAttrs = resource.getAttribute();
        resAttrs.add(createAttr(XACML_HOME_COMMUNITY, XACML_DATATYPE, TEST_HC_ID));
        resourceList.add(resource);

        ActionType action = new ActionType();
        List<AttributeType> actionAttrs = action.getAttribute();
        actionAttrs.add(createAttr(XACML_ACTION, XACML_DATATYPE, "AuditLogQueryOut"));
        request.getRequest().setAction(action);

        AdapterPEPImpl pepImpl = new AdapterPEPImpl();
        Request pdpRequest = pepImpl.createPdpRequest(request);

        verifyXSPASubjects(pdpRequest);
        verifyXSPAResources(pdpRequest);
        verifyXSPAAction(pdpRequest);
        verifyXSPAEnvironment(pdpRequest);
    }

    @Test
    public void testDocumentQueryIn() {

        expectedSubjectXSPA.clear();
        expectedSubjectXSPA.put(XSPA_SUBJECT_ID, TEST_USER_ID);
        expectedSubjectXSPA.put(XSPA_SUBJECT_ORG, TEST_USER_ORG);
        expectedSubjectXSPA.put(XSPA_SUBJECT_ORG_ID, TEST_USER_ORG_ID);
        expectedSubjectXSPA.put(XSPA_SUBJECT_ROLE, TEST_USER_ROLE);
        expectedSubjectXSPA.put(XSPA_SUBJECT_PURPOSE, TEST_USER_PURPOSE);

        expectedResourceXSPA.clear();
        expectedResourceXSPA.put(XSPA_RESOURCE_ID, TEST_PATIENT_ID);
        expectedResourceXSPA.put(XSPA_PATIENT_OPT_IN, "Yes");
        expectedResourceXSPA.put(XSPA_ASSIGNING_AUTH, TEST_ASSIGN_AUTH);
        expectedResourceXSPA.put(XSPA_SERVICE_TYPE, "DocumentQueryIn");

        expectedActionXSPA.clear();
        expectedActionXSPA.put(XSPA_ACTION, "read");

        expectedEnvXSPA.clear();
        expectedEnvXSPA.put(XSPA_ENVIRONMENT_LOCALITY, TEST_HC_ID);

        CheckPolicyRequestType request = new CheckPolicyRequestType();
        RequestType reqType = new RequestType();
        request.setRequest(reqType);

        List<SubjectType> subjectList = request.getRequest().getSubject();
        SubjectType subject = new SubjectType();
        List<AttributeType> subjAttrs = subject.getAttribute();
        subjAttrs.add(createAttr(XACML_SUBJECT_ID, XACML_DATATYPE, TEST_USER_ID));
        subjAttrs.add(createAttr(XACML_HOME_COMMUNITY, XACML_DATATYPE, TEST_HC_ID));
        subjAttrs.add(createAttr(XACML_SUBJECT_ORG, XACML_DATATYPE, TEST_USER_ORG));
        subjAttrs.add(createAttr(XACML_SUBJECT_ORG_ID, XACML_DATATYPE, TEST_USER_ORG_ID));
        subjAttrs.add(createAttr(XACML_SUBJECT_ROLE, XACML_DATATYPE, TEST_USER_ROLE));
        subjAttrs.add(createAttr(XACML_SUBJECT_PURPOSE, XACML_DATATYPE, TEST_USER_PURPOSE));
        subjectList.add(subject);

        List<ResourceType> resourceList = request.getRequest().getResource();
        ResourceType resource = new ResourceType();
        List<AttributeType> resAttrs = resource.getAttribute();
        resAttrs.add(createAttr(XACML_RESOURCE_ID, XACML_DATATYPE, TEST_PATIENT_ID));
        resAttrs.add(createAttr(XACML_ASSIGING_AUTH, XACML_DATATYPE, TEST_ASSIGN_AUTH));
        resourceList.add(resource);

        ActionType action = new ActionType();
        List<AttributeType> actionAttrs = action.getAttribute();
        actionAttrs.add(createAttr(XACML_ACTION, XACML_DATATYPE, "DocumentQueryIn"));
        request.getRequest().setAction(action);

        AdapterPEPImpl pepImpl = new AdapterPEPImpl() {
            protected List<String> determinePatientOptStatus(List<String> resourceIds, List<String> assigningAuths) {
                List<String> optStatusList = new ArrayList<String>();
                optStatusList.add("Yes");
                return optStatusList;
            }
        };
        Request pdpRequest = pepImpl.createPdpRequest(request);

        verifyXSPASubjects(pdpRequest);
        verifyXSPAResources(pdpRequest);
        verifyXSPAAction(pdpRequest);
        verifyXSPAEnvironment(pdpRequest);
    }

    @Test
    public void testDocumentQueryOut() {

        expectedSubjectXSPA.clear();
        expectedSubjectXSPA.put(XSPA_SUBJECT_ID, TEST_USER_ID);
        expectedSubjectXSPA.put(XSPA_SUBJECT_ORG, TEST_USER_ORG);
        expectedSubjectXSPA.put(XSPA_SUBJECT_ORG_ID, TEST_USER_ORG_ID);
        expectedSubjectXSPA.put(XSPA_SUBJECT_ROLE, TEST_USER_ROLE);
        expectedSubjectXSPA.put(XSPA_SUBJECT_PURPOSE, TEST_USER_PURPOSE);

        expectedResourceXSPA.clear();
        expectedResourceXSPA.put(XSPA_RESOURCE_ID, TEST_PATIENT_ID);
        expectedResourceXSPA.put(XSPA_PATIENT_OPT_IN, "Yes");
        expectedResourceXSPA.put(XSPA_ASSIGNING_AUTH, TEST_ASSIGN_AUTH);
        expectedResourceXSPA.put(XSPA_SERVICE_TYPE, "DocumentQueryOut");

        expectedActionXSPA.clear();
        expectedActionXSPA.put(XSPA_ACTION, "read");

        expectedEnvXSPA.clear();
        try {
            String homeCommunityId = PropertyAccessor.getProperty(PROPERTY_FILE_NAME_GATEWAY, PROPERTY_FILE_KEY_HOME_COMMUNITY);
            expectedEnvXSPA.put(XSPA_ENVIRONMENT_LOCALITY, homeCommunityId);
        } catch (PropertyAccessException ex) {
            fail("Can not access: " + PROPERTY_FILE_KEY_HOME_COMMUNITY + " property from: " + PROPERTY_FILE_NAME_GATEWAY);
        }

        CheckPolicyRequestType request = new CheckPolicyRequestType();
        RequestType reqType = new RequestType();
        request.setRequest(reqType);

        List<SubjectType> subjectList = request.getRequest().getSubject();
        SubjectType subject = new SubjectType();
        List<AttributeType> subjAttrs = subject.getAttribute();
        subjAttrs.add(createAttr(XACML_SUBJECT_ID, XACML_DATATYPE, TEST_USER_ID));
        subjAttrs.add(createAttr(XACML_SUBJECT_ORG, XACML_DATATYPE, TEST_USER_ORG));
        subjAttrs.add(createAttr(XACML_SUBJECT_ORG_ID, XACML_DATATYPE, TEST_USER_ORG_ID));
        subjAttrs.add(createAttr(XACML_SUBJECT_ROLE, XACML_DATATYPE, TEST_USER_ROLE));
        subjAttrs.add(createAttr(XACML_SUBJECT_PURPOSE, XACML_DATATYPE, TEST_USER_PURPOSE));
        subjectList.add(subject);

        List<ResourceType> resourceList = request.getRequest().getResource();
        ResourceType resource = new ResourceType();
        List<AttributeType> resAttrs = resource.getAttribute();
        resAttrs.add(createAttr(XACML_HOME_COMMUNITY, XACML_DATATYPE, TEST_HC_ID));
        resAttrs.add(createAttr(XACML_RESOURCE_ID, XACML_DATATYPE, TEST_PATIENT_ID));
        resAttrs.add(createAttr(XACML_ASSIGING_AUTH, XACML_DATATYPE, TEST_ASSIGN_AUTH));
        resourceList.add(resource);

        ActionType action = new ActionType();
        List<AttributeType> actionAttrs = action.getAttribute();
        actionAttrs.add(createAttr(XACML_ACTION, XACML_DATATYPE, "DocumentQueryOut"));
        request.getRequest().setAction(action);

        AdapterPEPImpl pepImpl = new AdapterPEPImpl() {
            protected List<String> determinePatientOptStatus(List<String> resourceIds, List<String> assigningAuths) {
                List<String> optStatusList = new ArrayList<String>();
                optStatusList.add("Yes");
                return optStatusList;
            }
        };
        Request pdpRequest = pepImpl.createPdpRequest(request);

        verifyXSPASubjects(pdpRequest);
        verifyXSPAResources(pdpRequest);
        verifyXSPAAction(pdpRequest);
        verifyXSPAEnvironment(pdpRequest);
    }

    @Test
    public void testDocumentRetrieveIn() {

        expectedSubjectXSPA.clear();
        expectedSubjectXSPA.put(XSPA_SUBJECT_ID, TEST_USER_ID);
        expectedSubjectXSPA.put(XSPA_SUBJECT_ORG, TEST_USER_ORG);
        expectedSubjectXSPA.put(XSPA_SUBJECT_ORG_ID, TEST_USER_ORG_ID);
        expectedSubjectXSPA.put(XSPA_SUBJECT_ROLE, TEST_USER_ROLE);
        expectedSubjectXSPA.put(XSPA_SUBJECT_PURPOSE, TEST_USER_PURPOSE);

        expectedResourceXSPA.clear();
        expectedResourceXSPA.put(XSPA_PATIENT_OPT_IN, "Yes");
        expectedResourceXSPA.put(XSPA_SERVICE_TYPE, "DocumentRetrieveIn");

        expectedActionXSPA.clear();
        expectedActionXSPA.put(XSPA_ACTION, "read");

        expectedEnvXSPA.clear();
        expectedEnvXSPA.put(XSPA_ENVIRONMENT_LOCALITY, TEST_HC_ID);

        CheckPolicyRequestType request = new CheckPolicyRequestType();
        RequestType reqType = new RequestType();
        request.setRequest(reqType);

        List<SubjectType> subjectList = request.getRequest().getSubject();
        SubjectType subject = new SubjectType();
        List<AttributeType> subjAttrs = subject.getAttribute();
        subjAttrs.add(createAttr(XACML_SUBJECT_ID, XACML_DATATYPE, TEST_USER_ID));
        subjAttrs.add(createAttr(XACML_HOME_COMMUNITY, XACML_DATATYPE, TEST_HC_ID));
        subjAttrs.add(createAttr(XACML_SUBJECT_ORG, XACML_DATATYPE, TEST_USER_ORG));
        subjAttrs.add(createAttr(XACML_SUBJECT_ORG_ID, XACML_DATATYPE, TEST_USER_ORG_ID));
        subjAttrs.add(createAttr(XACML_SUBJECT_ROLE, XACML_DATATYPE, TEST_USER_ROLE));
        subjAttrs.add(createAttr(XACML_SUBJECT_PURPOSE, XACML_DATATYPE, TEST_USER_PURPOSE));
        subjectList.add(subject);

        List<ResourceType> resourceList = request.getRequest().getResource();
        ResourceType resource = new ResourceType();
        List<AttributeType> resAttrs = resource.getAttribute();
        resAttrs.add(createAttr(XACML_DOC_COMMUNITY_ID, XACML_DATATYPE, TEST_HC_ID));
        resAttrs.add(createAttr(XACML_DOC_REPOSITORY_ID, XACML_DATATYPE, TEST_REPOSITORY_ID));
        resAttrs.add(createAttr(XACML_DOCUMENT_ID, XACML_DATATYPE, TEST_DOC_ID));
        resourceList.add(resource);

        ActionType action = new ActionType();
        List<AttributeType> actionAttrs = action.getAttribute();
        actionAttrs.add(createAttr(XACML_ACTION, XACML_DATATYPE, "DocumentRetrieveIn"));
        request.getRequest().setAction(action);

        AdapterPEPImpl pepImpl = new AdapterPEPImpl() {
            protected List<String> determineDocumentOptStatus(List<String> documentIds, List<String> communityIds, List<String> repositoryIds) {
                List<String> optStatusList = new ArrayList<String>();
                optStatusList.add("Yes");
                return optStatusList;
            }
        };
        Request pdpRequest = pepImpl.createPdpRequest(request);

        verifyXSPASubjects(pdpRequest);
        verifyXSPAResources(pdpRequest);
        verifyXSPAAction(pdpRequest);
        verifyXSPAEnvironment(pdpRequest);
    }

    @Test
    public void testDocumentRetrieveOut() {

        expectedSubjectXSPA.clear();
        expectedSubjectXSPA.put(XSPA_SUBJECT_ID, TEST_USER_ID);
        expectedSubjectXSPA.put(XSPA_SUBJECT_ORG, TEST_USER_ORG);
        expectedSubjectXSPA.put(XSPA_SUBJECT_ORG_ID, TEST_USER_ORG_ID);
        expectedSubjectXSPA.put(XSPA_SUBJECT_ROLE, TEST_USER_ROLE);
        expectedSubjectXSPA.put(XSPA_SUBJECT_PURPOSE, TEST_USER_PURPOSE);

        expectedResourceXSPA.clear();
        expectedResourceXSPA.put(XSPA_PATIENT_OPT_IN, "Yes");
        expectedResourceXSPA.put(XSPA_SERVICE_TYPE, "DocumentRetrieveOut");

        expectedActionXSPA.clear();
        expectedActionXSPA.put(XSPA_ACTION, "read");

        expectedEnvXSPA.clear();
        try {
            String homeCommunityId = PropertyAccessor.getProperty(PROPERTY_FILE_NAME_GATEWAY, PROPERTY_FILE_KEY_HOME_COMMUNITY);
            expectedEnvXSPA.put(XSPA_ENVIRONMENT_LOCALITY, homeCommunityId);
        } catch (PropertyAccessException ex) {
            fail("Can not access: " + PROPERTY_FILE_KEY_HOME_COMMUNITY + " property from: " + PROPERTY_FILE_NAME_GATEWAY);
        }

        CheckPolicyRequestType request = new CheckPolicyRequestType();
        RequestType reqType = new RequestType();
        request.setRequest(reqType);

        List<SubjectType> subjectList = request.getRequest().getSubject();
        SubjectType subject = new SubjectType();
        List<AttributeType> subjAttrs = subject.getAttribute();
        subjAttrs.add(createAttr(XACML_SUBJECT_ID, XACML_DATATYPE, TEST_USER_ID));
        subjAttrs.add(createAttr(XACML_SUBJECT_ORG, XACML_DATATYPE, TEST_USER_ORG));
        subjAttrs.add(createAttr(XACML_SUBJECT_ORG_ID, XACML_DATATYPE, TEST_USER_ORG_ID));
        subjAttrs.add(createAttr(XACML_SUBJECT_ROLE, XACML_DATATYPE, TEST_USER_ROLE));
        subjAttrs.add(createAttr(XACML_SUBJECT_PURPOSE, XACML_DATATYPE, TEST_USER_PURPOSE));
        subjectList.add(subject);

        List<ResourceType> resourceList = request.getRequest().getResource();
        ResourceType resource = new ResourceType();
        List<AttributeType> resAttrs = resource.getAttribute();
        resAttrs.add(createAttr(XACML_DOC_COMMUNITY_ID, XACML_DATATYPE, TEST_HC_ID));
        resAttrs.add(createAttr(XACML_DOC_REPOSITORY_ID, XACML_DATATYPE, TEST_REPOSITORY_ID));
        resAttrs.add(createAttr(XACML_DOCUMENT_ID, XACML_DATATYPE, TEST_DOC_ID));
        resourceList.add(resource);

        ActionType action = new ActionType();
        List<AttributeType> actionAttrs = action.getAttribute();
        actionAttrs.add(createAttr(XACML_ACTION, XACML_DATATYPE, "DocumentRetrieveOut"));
        request.getRequest().setAction(action);

        AdapterPEPImpl pepImpl = new AdapterPEPImpl() {
            protected List<String> determineDocumentOptStatus(List<String> documentIds, List<String> communityIds, List<String> repositoryIds) {
                List<String> optStatusList = new ArrayList<String>();
                optStatusList.add("Yes");
                return optStatusList;
            }
        };
        Request pdpRequest = pepImpl.createPdpRequest(request);

        verifyXSPASubjects(pdpRequest);
        verifyXSPAResources(pdpRequest);
        verifyXSPAAction(pdpRequest);
        verifyXSPAEnvironment(pdpRequest);
    }

    @Test
    public void testHIEMNotifyIn() {

        expectedSubjectXSPA.clear();
        expectedSubjectXSPA.put(XSPA_SUBJECT_ID, TEST_USER_ID);
        expectedSubjectXSPA.put(XSPA_SUBJECT_ORG, TEST_USER_ORG);
        expectedSubjectXSPA.put(XSPA_SUBJECT_ORG_ID, TEST_USER_ORG_ID);
        expectedSubjectXSPA.put(XSPA_SUBJECT_ROLE, TEST_USER_ROLE);
        expectedSubjectXSPA.put(XSPA_SUBJECT_PURPOSE, TEST_USER_PURPOSE);

        expectedResourceXSPA.clear();
        expectedResourceXSPA.put(XSPA_RESOURCE_ID, TEST_PATIENT_ID);
        expectedResourceXSPA.put(XSPA_PATIENT_OPT_IN, "Yes");
        expectedResourceXSPA.put(XSPA_ASSIGNING_AUTH, TEST_ASSIGN_AUTH);
        expectedResourceXSPA.put(XSPA_SERVICE_TYPE, "HIEMNotifyIn");

        expectedActionXSPA.clear();
        expectedActionXSPA.put(XSPA_ACTION, "update");

        expectedEnvXSPA.clear();
        expectedEnvXSPA.put(XSPA_ENVIRONMENT_LOCALITY, TEST_HC_ID);

        CheckPolicyRequestType request = new CheckPolicyRequestType();
        RequestType reqType = new RequestType();
        request.setRequest(reqType);

        List<SubjectType> subjectList = request.getRequest().getSubject();
        SubjectType subject = new SubjectType();
        List<AttributeType> subjAttrs = subject.getAttribute();
        subjAttrs.add(createAttr(XACML_SUBJECT_ID, XACML_DATATYPE, TEST_USER_ID));
        subjAttrs.add(createAttr(XACML_HOME_COMMUNITY, XACML_DATATYPE, TEST_HC_ID));
        subjAttrs.add(createAttr(XACML_SUBJECT_ORG, XACML_DATATYPE, TEST_USER_ORG));
        subjAttrs.add(createAttr(XACML_SUBJECT_ORG_ID, XACML_DATATYPE, TEST_USER_ORG_ID));
        subjAttrs.add(createAttr(XACML_SUBJECT_ROLE, XACML_DATATYPE, TEST_USER_ROLE));
        subjAttrs.add(createAttr(XACML_SUBJECT_PURPOSE, XACML_DATATYPE, TEST_USER_PURPOSE));
        subjectList.add(subject);

        List<ResourceType> resourceList = request.getRequest().getResource();
        ResourceType resource = new ResourceType();
        List<AttributeType> resAttrs = resource.getAttribute();
        resAttrs.add(createAttr(XACML_RESOURCE_ID, XACML_DATATYPE, TEST_PATIENT_ID));
        resAttrs.add(createAttr(XACML_ASSIGING_AUTH, XACML_DATATYPE, TEST_ASSIGN_AUTH));
        resourceList.add(resource);

        ActionType action = new ActionType();
        List<AttributeType> actionAttrs = action.getAttribute();
        actionAttrs.add(createAttr(XACML_ACTION, XACML_DATATYPE, "HIEMNotifyIn"));
        request.getRequest().setAction(action);

        AdapterPEPImpl pepImpl = new AdapterPEPImpl() {
            protected List<String> determinePatientOptStatus(List<String> resourceIds, List<String> assigningAuths) {
                List<String> optStatusList = new ArrayList<String>();
                optStatusList.add("Yes");
                return optStatusList;
            }
        };
        Request pdpRequest = pepImpl.createPdpRequest(request);

        verifyXSPASubjects(pdpRequest);
        verifyXSPAResources(pdpRequest);
        verifyXSPAAction(pdpRequest);
        verifyXSPAEnvironment(pdpRequest);
    }

    @Test
    public void testHIEMNotifyOut() {

        expectedSubjectXSPA.clear();
        expectedSubjectXSPA.put(XSPA_SUBJECT_ID, TEST_USER_ID);
        expectedSubjectXSPA.put(XSPA_SUBJECT_ORG, TEST_USER_ORG);
        expectedSubjectXSPA.put(XSPA_SUBJECT_ORG_ID, TEST_USER_ORG_ID);
        expectedSubjectXSPA.put(XSPA_SUBJECT_ROLE, TEST_USER_ROLE);
        expectedSubjectXSPA.put(XSPA_SUBJECT_PURPOSE, TEST_USER_PURPOSE);

        expectedResourceXSPA.clear();
        expectedResourceXSPA.put(XSPA_RESOURCE_ID, TEST_PATIENT_ID);
        expectedResourceXSPA.put(XSPA_PATIENT_OPT_IN, "Yes");
        expectedResourceXSPA.put(XSPA_ASSIGNING_AUTH, TEST_ASSIGN_AUTH);
        expectedResourceXSPA.put(XSPA_SERVICE_TYPE, "HIEMNotifyOut");

        expectedActionXSPA.clear();
        expectedActionXSPA.put(XSPA_ACTION, "update");

        expectedEnvXSPA.clear();
        try {
            String homeCommunityId = PropertyAccessor.getProperty(PROPERTY_FILE_NAME_GATEWAY, PROPERTY_FILE_KEY_HOME_COMMUNITY);
            expectedEnvXSPA.put(XSPA_ENVIRONMENT_LOCALITY, homeCommunityId);
        } catch (PropertyAccessException ex) {
            fail("Can not access: " + PROPERTY_FILE_KEY_HOME_COMMUNITY + " property from: " + PROPERTY_FILE_NAME_GATEWAY);
        }

        CheckPolicyRequestType request = new CheckPolicyRequestType();
        RequestType reqType = new RequestType();
        request.setRequest(reqType);

        List<SubjectType> subjectList = request.getRequest().getSubject();
        SubjectType subject = new SubjectType();
        List<AttributeType> subjAttrs = subject.getAttribute();
        subjAttrs.add(createAttr(XACML_SUBJECT_ID, XACML_DATATYPE, TEST_USER_ID));
        subjAttrs.add(createAttr(XACML_SUBJECT_ORG, XACML_DATATYPE, TEST_USER_ORG));
        subjAttrs.add(createAttr(XACML_SUBJECT_ORG_ID, XACML_DATATYPE, TEST_USER_ORG_ID));
        subjAttrs.add(createAttr(XACML_SUBJECT_ROLE, XACML_DATATYPE, TEST_USER_ROLE));
        subjAttrs.add(createAttr(XACML_SUBJECT_PURPOSE, XACML_DATATYPE, TEST_USER_PURPOSE));
        subjectList.add(subject);

        List<ResourceType> resourceList = request.getRequest().getResource();
        ResourceType resource = new ResourceType();
        List<AttributeType> resAttrs = resource.getAttribute();
        resAttrs.add(createAttr(XACML_HOME_COMMUNITY, XACML_DATATYPE, TEST_HC_ID));
        resAttrs.add(createAttr(XACML_RESOURCE_ID, XACML_DATATYPE, TEST_PATIENT_ID));
        resAttrs.add(createAttr(XACML_ASSIGING_AUTH, XACML_DATATYPE, TEST_ASSIGN_AUTH));
        resourceList.add(resource);

        ActionType action = new ActionType();
        List<AttributeType> actionAttrs = action.getAttribute();
        actionAttrs.add(createAttr(XACML_ACTION, XACML_DATATYPE, "HIEMNotifyOut"));
        request.getRequest().setAction(action);

        AdapterPEPImpl pepImpl = new AdapterPEPImpl() {
            protected List<String> determinePatientOptStatus(List<String> resourceIds, List<String> assigningAuths) {
                List<String> optStatusList = new ArrayList<String>();
                optStatusList.add("Yes");
                return optStatusList;
            }
        };
        Request pdpRequest = pepImpl.createPdpRequest(request);

        verifyXSPASubjects(pdpRequest);
        verifyXSPAResources(pdpRequest);
        verifyXSPAAction(pdpRequest);
        verifyXSPAEnvironment(pdpRequest);
    }

    /**
     * Note that HIEM Subscription Request can be both Patient-Specific as well as non-patient Specific.
     * If it is patient-specific, then the Local Assigning Authority and Local Patient Id must be passed in.
     * The Topic is currently optional for patient specific messages.  If it is not a patient-specific message,
     * then the local patient ID and local assigning authority attributes should not be passed in,
     * but the Topic will need to be passed in.  The existence of the patient ID tells the policy engine
     * that it must check the patient’s opt-in preference settings.
     */
    @Test
    public void testHIEMPatientCancelIn() {

        expectedSubjectXSPA.clear();
        expectedSubjectXSPA.put(XSPA_SUBJECT_ID, TEST_USER_ID);
        expectedSubjectXSPA.put(XSPA_SUBJECT_ORG, TEST_USER_ORG);
        expectedSubjectXSPA.put(XSPA_SUBJECT_ORG_ID, TEST_USER_ORG_ID);
        expectedSubjectXSPA.put(XSPA_SUBJECT_ROLE, TEST_USER_ROLE);
        expectedSubjectXSPA.put(XSPA_SUBJECT_PURPOSE, TEST_USER_PURPOSE);

        expectedResourceXSPA.clear();
        expectedResourceXSPA.put(XSPA_RESOURCE_ID, TEST_PATIENT_ID);
        expectedResourceXSPA.put(XSPA_PATIENT_OPT_IN, "Yes");
        expectedResourceXSPA.put(XSPA_ASSIGNING_AUTH, TEST_ASSIGN_AUTH);
        expectedResourceXSPA.put(XSPA_SERVICE_TYPE, "HIEMSubscriptionCancelIn");

        expectedActionXSPA.clear();
        expectedActionXSPA.put(XSPA_ACTION, "delete");

        expectedEnvXSPA.clear();
        expectedEnvXSPA.put(XSPA_ENVIRONMENT_LOCALITY, TEST_HC_ID);

        CheckPolicyRequestType request = new CheckPolicyRequestType();
        RequestType reqType = new RequestType();
        request.setRequest(reqType);

        List<SubjectType> subjectList = request.getRequest().getSubject();
        SubjectType subject = new SubjectType();
        List<AttributeType> subjAttrs = subject.getAttribute();
        subjAttrs.add(createAttr(XACML_SUBJECT_ID, XACML_DATATYPE, TEST_USER_ID));
        subjAttrs.add(createAttr(XACML_HOME_COMMUNITY, XACML_DATATYPE, TEST_HC_ID));
        subjAttrs.add(createAttr(XACML_SUBJECT_ORG, XACML_DATATYPE, TEST_USER_ORG));
        subjAttrs.add(createAttr(XACML_SUBJECT_ORG_ID, XACML_DATATYPE, TEST_USER_ORG_ID));
        subjAttrs.add(createAttr(XACML_SUBJECT_ROLE, XACML_DATATYPE, TEST_USER_ROLE));
        subjAttrs.add(createAttr(XACML_SUBJECT_PURPOSE, XACML_DATATYPE, TEST_USER_PURPOSE));
        subjectList.add(subject);

        List<ResourceType> resourceList = request.getRequest().getResource();
        ResourceType resource = new ResourceType();
        List<AttributeType> resAttrs = resource.getAttribute();
        resAttrs.add(createAttr(XACML_RESOURCE_ID, XACML_DATATYPE, TEST_PATIENT_ID));
        resAttrs.add(createAttr(XACML_ASSIGING_AUTH, XACML_DATATYPE, TEST_ASSIGN_AUTH));
        resourceList.add(resource);

        ActionType action = new ActionType();
        List<AttributeType> actionAttrs = action.getAttribute();
        actionAttrs.add(createAttr(XACML_ACTION, XACML_DATATYPE, "HIEMSubscriptionCancelIn"));
        request.getRequest().setAction(action);

        AdapterPEPImpl pepImpl = new AdapterPEPImpl() {
            protected List<String> determinePatientOptStatus(List<String> resourceIds, List<String> assigningAuths) {
                List<String> optStatusList = new ArrayList<String>();
                optStatusList.add("Yes");
                return optStatusList;
            }
        };
        Request pdpRequest = pepImpl.createPdpRequest(request);

        verifyXSPASubjects(pdpRequest);
        verifyXSPAResources(pdpRequest);
        verifyXSPAAction(pdpRequest);
        verifyXSPAEnvironment(pdpRequest);
    }

    /**
     * Note that HIEM Subscription Request can be both Patient-Specific as well as non-patient Specific.
     * If it is patient-specific, then the Local Assigning Authority and Local Patient Id must be passed in.
     * The Topic is currently optional for patient specific messages.  If it is not a patient-specific message,
     * then the local patient ID and local assigning authority attributes should not be passed in,
     * but the Topic will need to be passed in.  The existence of the patient ID tells the policy engine
     * that it must check the patient’s opt-in preference settings.
     */
    @Test
    public void testHIEMPatientCancelOut() {

        expectedSubjectXSPA.clear();
        expectedSubjectXSPA.put(XSPA_SUBJECT_ID, TEST_USER_ID);
        expectedSubjectXSPA.put(XSPA_SUBJECT_ORG, TEST_USER_ORG);
        expectedSubjectXSPA.put(XSPA_SUBJECT_ORG_ID, TEST_USER_ORG_ID);
        expectedSubjectXSPA.put(XSPA_SUBJECT_ROLE, TEST_USER_ROLE);
        expectedSubjectXSPA.put(XSPA_SUBJECT_PURPOSE, TEST_USER_PURPOSE);

        expectedResourceXSPA.clear();
        expectedResourceXSPA.put(XSPA_RESOURCE_ID, TEST_PATIENT_ID);
        expectedResourceXSPA.put(XSPA_PATIENT_OPT_IN, "Yes");
        expectedResourceXSPA.put(XSPA_ASSIGNING_AUTH, TEST_ASSIGN_AUTH);
        expectedResourceXSPA.put(XSPA_SERVICE_TYPE, "HIEMSubscriptionCancelOut");

        expectedActionXSPA.clear();
        expectedActionXSPA.put(XSPA_ACTION, "delete");

        expectedEnvXSPA.clear();
        try {
            String homeCommunityId = PropertyAccessor.getProperty(PROPERTY_FILE_NAME_GATEWAY, PROPERTY_FILE_KEY_HOME_COMMUNITY);
            expectedEnvXSPA.put(XSPA_ENVIRONMENT_LOCALITY, homeCommunityId);
        } catch (PropertyAccessException ex) {
            fail("Can not access: " + PROPERTY_FILE_KEY_HOME_COMMUNITY + " property from: " + PROPERTY_FILE_NAME_GATEWAY);
        }

        CheckPolicyRequestType request = new CheckPolicyRequestType();
        RequestType reqType = new RequestType();
        request.setRequest(reqType);

        List<SubjectType> subjectList = request.getRequest().getSubject();
        SubjectType subject = new SubjectType();
        List<AttributeType> subjAttrs = subject.getAttribute();
        subjAttrs.add(createAttr(XACML_SUBJECT_ID, XACML_DATATYPE, TEST_USER_ID));
        subjAttrs.add(createAttr(XACML_SUBJECT_ORG, XACML_DATATYPE, TEST_USER_ORG));
        subjAttrs.add(createAttr(XACML_SUBJECT_ORG_ID, XACML_DATATYPE, TEST_USER_ORG_ID));
        subjAttrs.add(createAttr(XACML_SUBJECT_ROLE, XACML_DATATYPE, TEST_USER_ROLE));
        subjAttrs.add(createAttr(XACML_SUBJECT_PURPOSE, XACML_DATATYPE, TEST_USER_PURPOSE));
        subjectList.add(subject);

        List<ResourceType> resourceList = request.getRequest().getResource();
        ResourceType resource = new ResourceType();
        List<AttributeType> resAttrs = resource.getAttribute();
        resAttrs.add(createAttr(XACML_HOME_COMMUNITY, XACML_DATATYPE, TEST_HC_ID));
        resAttrs.add(createAttr(XACML_RESOURCE_ID, XACML_DATATYPE, TEST_PATIENT_ID));
        resAttrs.add(createAttr(XACML_ASSIGING_AUTH, XACML_DATATYPE, TEST_ASSIGN_AUTH));
        resourceList.add(resource);

        ActionType action = new ActionType();
        List<AttributeType> actionAttrs = action.getAttribute();
        actionAttrs.add(createAttr(XACML_ACTION, XACML_DATATYPE, "HIEMSubscriptionCancelOut"));
        request.getRequest().setAction(action);

        AdapterPEPImpl pepImpl = new AdapterPEPImpl() {
            protected List<String> determinePatientOptStatus(List<String> resourceIds, List<String> assigningAuths) {
                List<String> optStatusList = new ArrayList<String>();
                optStatusList.add("Yes");
                return optStatusList;
            }
        };
        Request pdpRequest = pepImpl.createPdpRequest(request);

        verifyXSPASubjects(pdpRequest);
        verifyXSPAResources(pdpRequest);
        verifyXSPAAction(pdpRequest);
        verifyXSPAEnvironment(pdpRequest);
    }

    /**
     * Note that HIEM Subscription Request can be both Patient-Specific as well as non-patient Specific.
     * If it is patient-specific, then the Local Assigning Authority and Local Patient Id must be passed in.
     * The Topic is currently optional for patient specific messages.  If it is not a patient-specific message,
     * then the local patient ID and local assigning authority attributes should not be passed in,
     * but the Topic will need to be passed in.  The existence of the patient ID tells the policy engine
     * that it must check the patient’s opt-in preference settings.
     */
    @Test
    public void testHIEMPatientSubscriptionIn() {

        expectedSubjectXSPA.clear();
        expectedSubjectXSPA.put(XSPA_SUBJECT_ID, TEST_USER_ID);
        expectedSubjectXSPA.put(XSPA_SUBJECT_ORG, TEST_USER_ORG);
        expectedSubjectXSPA.put(XSPA_SUBJECT_ORG_ID, TEST_USER_ORG_ID);
        expectedSubjectXSPA.put(XSPA_SUBJECT_ROLE, TEST_USER_ROLE);
        expectedSubjectXSPA.put(XSPA_SUBJECT_PURPOSE, TEST_USER_PURPOSE);

        expectedResourceXSPA.clear();
        expectedResourceXSPA.put(XSPA_RESOURCE_ID, TEST_PATIENT_ID);
        expectedResourceXSPA.put(XSPA_PATIENT_OPT_IN, "Yes");
        expectedResourceXSPA.put(XSPA_ASSIGNING_AUTH, TEST_ASSIGN_AUTH);
        expectedResourceXSPA.put(XSPA_SERVICE_TYPE, "HIEMSubscriptionRequestIn");

        expectedActionXSPA.clear();
        expectedActionXSPA.put(XSPA_ACTION, "create");

        expectedEnvXSPA.clear();
        expectedEnvXSPA.put(XSPA_ENVIRONMENT_LOCALITY, TEST_HC_ID);

        CheckPolicyRequestType request = new CheckPolicyRequestType();
        RequestType reqType = new RequestType();
        request.setRequest(reqType);

        List<SubjectType> subjectList = request.getRequest().getSubject();
        SubjectType subject = new SubjectType();
        List<AttributeType> subjAttrs = subject.getAttribute();
        subjAttrs.add(createAttr(XACML_SUBJECT_ID, XACML_DATATYPE, TEST_USER_ID));
        subjAttrs.add(createAttr(XACML_HOME_COMMUNITY, XACML_DATATYPE, TEST_HC_ID));
        subjAttrs.add(createAttr(XACML_SUBJECT_ORG, XACML_DATATYPE, TEST_USER_ORG));
        subjAttrs.add(createAttr(XACML_SUBJECT_ORG_ID, XACML_DATATYPE, TEST_USER_ORG_ID));
        subjAttrs.add(createAttr(XACML_SUBJECT_ROLE, XACML_DATATYPE, TEST_USER_ROLE));
        subjAttrs.add(createAttr(XACML_SUBJECT_PURPOSE, XACML_DATATYPE, TEST_USER_PURPOSE));
        subjectList.add(subject);

        List<ResourceType> resourceList = request.getRequest().getResource();
        ResourceType resource = new ResourceType();
        List<AttributeType> resAttrs = resource.getAttribute();
        resAttrs.add(createAttr(XACML_RESOURCE_ID, XACML_DATATYPE, TEST_PATIENT_ID));
        resAttrs.add(createAttr(XACML_ASSIGING_AUTH, XACML_DATATYPE, TEST_ASSIGN_AUTH));
        resourceList.add(resource);

        ActionType action = new ActionType();
        List<AttributeType> actionAttrs = action.getAttribute();
        actionAttrs.add(createAttr(XACML_ACTION, XACML_DATATYPE, "HIEMSubscriptionRequestIn"));
        request.getRequest().setAction(action);

        AdapterPEPImpl pepImpl = new AdapterPEPImpl() {
            protected List<String> determinePatientOptStatus(List<String> resourceIds, List<String> assigningAuths) {
                List<String> optStatusList = new ArrayList<String>();
                optStatusList.add("Yes");
                return optStatusList;
            }
        };
        Request pdpRequest = pepImpl.createPdpRequest(request);

        verifyXSPASubjects(pdpRequest);
        verifyXSPAResources(pdpRequest);
        verifyXSPAAction(pdpRequest);
        verifyXSPAEnvironment(pdpRequest);
    }

    /**
     * Note that HIEM Subscription Request can be both Patient-Specific as well as non-patient Specific.
     * If it is patient-specific, then the Local Assigning Authority and Local Patient Id must be passed in.
     * The Topic is currently optional for patient specific messages.  If it is not a patient-specific message,
     * then the local patient ID and local assigning authority attributes should not be passed in,
     * but the Topic will need to be passed in.  The existence of the patient ID tells the policy engine
     * that it must check the patient’s opt-in preference settings.
     */
    @Test
    public void testHIEMPatientSubscriptionOut() {

        expectedSubjectXSPA.clear();
        expectedSubjectXSPA.put(XSPA_SUBJECT_ID, TEST_USER_ID);
        expectedSubjectXSPA.put(XSPA_SUBJECT_ORG, TEST_USER_ORG);
        expectedSubjectXSPA.put(XSPA_SUBJECT_ORG_ID, TEST_USER_ORG_ID);
        expectedSubjectXSPA.put(XSPA_SUBJECT_ROLE, TEST_USER_ROLE);
        expectedSubjectXSPA.put(XSPA_SUBJECT_PURPOSE, TEST_USER_PURPOSE);

        expectedResourceXSPA.clear();
        expectedResourceXSPA.put(XSPA_RESOURCE_ID, TEST_PATIENT_ID);
        expectedResourceXSPA.put(XSPA_PATIENT_OPT_IN, "Yes");
        expectedResourceXSPA.put(XSPA_ASSIGNING_AUTH, TEST_ASSIGN_AUTH);
        expectedResourceXSPA.put(XSPA_SERVICE_TYPE, "HIEMSubscriptionRequestOut");

        expectedActionXSPA.clear();
        expectedActionXSPA.put(XSPA_ACTION, "create");

        expectedEnvXSPA.clear();
        try {
            String homeCommunityId = PropertyAccessor.getProperty(PROPERTY_FILE_NAME_GATEWAY, PROPERTY_FILE_KEY_HOME_COMMUNITY);
            expectedEnvXSPA.put(XSPA_ENVIRONMENT_LOCALITY, homeCommunityId);
        } catch (PropertyAccessException ex) {
            fail("Can not access: " + PROPERTY_FILE_KEY_HOME_COMMUNITY + " property from: " + PROPERTY_FILE_NAME_GATEWAY);
        }

        CheckPolicyRequestType request = new CheckPolicyRequestType();
        RequestType reqType = new RequestType();
        request.setRequest(reqType);

        List<SubjectType> subjectList = request.getRequest().getSubject();
        SubjectType subject = new SubjectType();
        List<AttributeType> subjAttrs = subject.getAttribute();
        subjAttrs.add(createAttr(XACML_SUBJECT_ID, XACML_DATATYPE, TEST_USER_ID));
        subjAttrs.add(createAttr(XACML_SUBJECT_ORG, XACML_DATATYPE, TEST_USER_ORG));
        subjAttrs.add(createAttr(XACML_SUBJECT_ORG_ID, XACML_DATATYPE, TEST_USER_ORG_ID));
        subjAttrs.add(createAttr(XACML_SUBJECT_ROLE, XACML_DATATYPE, TEST_USER_ROLE));
        subjAttrs.add(createAttr(XACML_SUBJECT_PURPOSE, XACML_DATATYPE, TEST_USER_PURPOSE));
        subjectList.add(subject);

        List<ResourceType> resourceList = request.getRequest().getResource();
        ResourceType resource = new ResourceType();
        List<AttributeType> resAttrs = resource.getAttribute();
        resAttrs.add(createAttr(XACML_HOME_COMMUNITY, XACML_DATATYPE, TEST_HC_ID));
        resAttrs.add(createAttr(XACML_RESOURCE_ID, XACML_DATATYPE, TEST_PATIENT_ID));
        resAttrs.add(createAttr(XACML_ASSIGING_AUTH, XACML_DATATYPE, TEST_ASSIGN_AUTH));
        resourceList.add(resource);

        ActionType action = new ActionType();
        List<AttributeType> actionAttrs = action.getAttribute();
        actionAttrs.add(createAttr(XACML_ACTION, XACML_DATATYPE, "HIEMSubscriptionRequestOut"));
        request.getRequest().setAction(action);

        AdapterPEPImpl pepImpl = new AdapterPEPImpl() {
            protected List<String> determinePatientOptStatus(List<String> resourceIds, List<String> assigningAuths) {
                List<String> optStatusList = new ArrayList<String>();
                optStatusList.add("Yes");
                return optStatusList;
            }
        };
        Request pdpRequest = pepImpl.createPdpRequest(request);

        verifyXSPASubjects(pdpRequest);
        verifyXSPAResources(pdpRequest);
        verifyXSPAAction(pdpRequest);
        verifyXSPAEnvironment(pdpRequest);
    }

    /**
     * Note that HIEM Subscription Request can be both Patient-Specific as well as non-patient Specific.
     * If it is patient-specific, then the Local Assigning Authority and Local Patient Id must be passed in.
     * The Topic is currently optional for patient specific messages.  If it is not a patient-specific message,
     * then the local patient ID and local assigning authority attributes should not be passed in,
     * but the Topic will need to be passed in.  The existence of the patient ID tells the policy engine
     * that it must check the patient’s opt-in preference settings.
     */
    @Test
    public void testHIEMSubscriptionIn() {

        expectedSubjectXSPA.clear();
        expectedSubjectXSPA.put(XSPA_SUBJECT_ID, TEST_USER_ID);
        expectedSubjectXSPA.put(XSPA_SUBJECT_ORG, TEST_USER_ORG);
        expectedSubjectXSPA.put(XSPA_SUBJECT_ORG_ID, TEST_USER_ORG_ID);
        expectedSubjectXSPA.put(XSPA_SUBJECT_ROLE, TEST_USER_ROLE);
        expectedSubjectXSPA.put(XSPA_SUBJECT_PURPOSE, TEST_USER_PURPOSE);

        expectedResourceXSPA.clear();
        expectedResourceXSPA.put(XSPA_PATIENT_OPT_IN, "Yes");
        expectedResourceXSPA.put(XSPA_SERVICE_TYPE, "HIEMSubscriptionRequestIn");

        expectedActionXSPA.clear();
        expectedActionXSPA.put(XSPA_ACTION, "create");

        expectedEnvXSPA.clear();
        expectedEnvXSPA.put(XSPA_ENVIRONMENT_LOCALITY, TEST_HC_ID);

        CheckPolicyRequestType request = new CheckPolicyRequestType();
        RequestType reqType = new RequestType();
        request.setRequest(reqType);

        List<SubjectType> subjectList = request.getRequest().getSubject();
        SubjectType subject = new SubjectType();
        List<AttributeType> subjAttrs = subject.getAttribute();
        subjAttrs.add(createAttr(XACML_SUBJECT_ID, XACML_DATATYPE, TEST_USER_ID));
        subjAttrs.add(createAttr(XACML_HOME_COMMUNITY, XACML_DATATYPE, TEST_HC_ID));
        subjAttrs.add(createAttr(XACML_SUBJECT_ORG, XACML_DATATYPE, TEST_USER_ORG));
        subjAttrs.add(createAttr(XACML_SUBJECT_ORG_ID, XACML_DATATYPE, TEST_USER_ORG_ID));
        subjAttrs.add(createAttr(XACML_SUBJECT_ROLE, XACML_DATATYPE, TEST_USER_ROLE));
        subjAttrs.add(createAttr(XACML_SUBJECT_PURPOSE, XACML_DATATYPE, TEST_USER_PURPOSE));
        subjectList.add(subject);

        List<ResourceType> resourceList = request.getRequest().getResource();
        ResourceType resource = new ResourceType();
        List<AttributeType> resAttrs = resource.getAttribute();
        resAttrs.add(createAttr(XACML_SUBSCRIPTION_ID, XACML_DATATYPE, TEST_SUBSCRIPTION_ID));
        resourceList.add(resource);

        ActionType action = new ActionType();
        List<AttributeType> actionAttrs = action.getAttribute();
        actionAttrs.add(createAttr(XACML_ACTION, XACML_DATATYPE, "HIEMSubscriptionRequestIn"));
        request.getRequest().setAction(action);

        AdapterPEPImpl pepImpl = new AdapterPEPImpl();
        Request pdpRequest = pepImpl.createPdpRequest(request);

        verifyXSPASubjects(pdpRequest);
        verifyXSPAResources(pdpRequest);
        verifyXSPAAction(pdpRequest);
        verifyXSPAEnvironment(pdpRequest);
    }

    /**
     * Note that HIEM Subscription Request can be both Patient-Specific as well as non-patient Specific.
     * If it is patient-specific, then the Local Assigning Authority and Local Patient Id must be passed in.
     * The Topic is currently optional for patient specific messages.  If it is not a patient-specific message,
     * then the local patient ID and local assigning authority attributes should not be passed in,
     * but the Topic will need to be passed in.  The existence of the patient ID tells the policy engine
     * that it must check the patient’s opt-in preference settings.
     */
    @Test
    public void testHIEMSubscriptionOut() {

        expectedSubjectXSPA.clear();
        expectedSubjectXSPA.put(XSPA_SUBJECT_ID, TEST_USER_ID);
        expectedSubjectXSPA.put(XSPA_SUBJECT_ORG, TEST_USER_ORG);
        expectedSubjectXSPA.put(XSPA_SUBJECT_ORG_ID, TEST_USER_ORG_ID);
        expectedSubjectXSPA.put(XSPA_SUBJECT_ROLE, TEST_USER_ROLE);
        expectedSubjectXSPA.put(XSPA_SUBJECT_PURPOSE, TEST_USER_PURPOSE);

        expectedResourceXSPA.clear();
        expectedResourceXSPA.put(XSPA_PATIENT_OPT_IN, "Yes");
        expectedResourceXSPA.put(XSPA_SERVICE_TYPE, "HIEMSubscriptionRequestOut");

        expectedActionXSPA.clear();
        expectedActionXSPA.put(XSPA_ACTION, "create");

        expectedEnvXSPA.clear();
        try {
            String homeCommunityId = PropertyAccessor.getProperty(PROPERTY_FILE_NAME_GATEWAY, PROPERTY_FILE_KEY_HOME_COMMUNITY);
            expectedEnvXSPA.put(XSPA_ENVIRONMENT_LOCALITY, homeCommunityId);
        } catch (PropertyAccessException ex) {
            fail("Can not access: " + PROPERTY_FILE_KEY_HOME_COMMUNITY + " property from: " + PROPERTY_FILE_NAME_GATEWAY);
        }

        CheckPolicyRequestType request = new CheckPolicyRequestType();
        RequestType reqType = new RequestType();
        request.setRequest(reqType);

        List<SubjectType> subjectList = request.getRequest().getSubject();
        SubjectType subject = new SubjectType();
        List<AttributeType> subjAttrs = subject.getAttribute();
        subjAttrs.add(createAttr(XACML_SUBJECT_ID, XACML_DATATYPE, TEST_USER_ID));
        subjAttrs.add(createAttr(XACML_SUBJECT_ORG, XACML_DATATYPE, TEST_USER_ORG));
        subjAttrs.add(createAttr(XACML_SUBJECT_ORG_ID, XACML_DATATYPE, TEST_USER_ORG_ID));
        subjAttrs.add(createAttr(XACML_SUBJECT_ROLE, XACML_DATATYPE, TEST_USER_ROLE));
        subjAttrs.add(createAttr(XACML_SUBJECT_PURPOSE, XACML_DATATYPE, TEST_USER_PURPOSE));
        subjectList.add(subject);

        List<ResourceType> resourceList = request.getRequest().getResource();
        ResourceType resource = new ResourceType();
        List<AttributeType> resAttrs = resource.getAttribute();
        resAttrs.add(createAttr(XACML_HOME_COMMUNITY, XACML_DATATYPE, TEST_HC_ID));
        resAttrs.add(createAttr(XACML_SUBSCRIPTION_ID, XACML_DATATYPE, TEST_SUBSCRIPTION_ID));
        resourceList.add(resource);

        ActionType action = new ActionType();
        List<AttributeType> actionAttrs = action.getAttribute();
        actionAttrs.add(createAttr(XACML_ACTION, XACML_DATATYPE, "HIEMSubscriptionRequestOut"));
        request.getRequest().setAction(action);

        AdapterPEPImpl pepImpl = new AdapterPEPImpl();
        Request pdpRequest = pepImpl.createPdpRequest(request);

        verifyXSPASubjects(pdpRequest);
        verifyXSPAResources(pdpRequest);
        verifyXSPAAction(pdpRequest);
        verifyXSPAEnvironment(pdpRequest);
    }

    @Test
    public void testOptOutReidentificationIn() {

        expectedSubjectXSPA.clear();
        expectedSubjectXSPA.put(XSPA_SUBJECT_ID, TEST_USER_ID);
        expectedSubjectXSPA.put(XSPA_SUBJECT_ORG, TEST_USER_ORG);
        expectedSubjectXSPA.put(XSPA_SUBJECT_ORG_ID, TEST_USER_ORG_ID);
        expectedSubjectXSPA.put(XSPA_SUBJECT_ROLE, TEST_USER_ROLE);
        expectedSubjectXSPA.put(XSPA_SUBJECT_PURPOSE, TEST_USER_PURPOSE);

        expectedResourceXSPA.clear();
        expectedResourceXSPA.put(XSPA_RESOURCE_ID, TEST_PATIENT_ID);
        expectedResourceXSPA.put(XSPA_PATIENT_OPT_IN, "No");
        expectedResourceXSPA.put(XSPA_ASSIGNING_AUTH, TEST_ASSIGN_AUTH);
        expectedResourceXSPA.put(XSPA_SERVICE_TYPE, "SubjectDiscoveryReidentificationIn");

        expectedActionXSPA.clear();
        expectedActionXSPA.put(XSPA_ACTION, "read");

        expectedEnvXSPA.clear();
        expectedEnvXSPA.put(XSPA_ENVIRONMENT_LOCALITY, TEST_HC_ID);

        CheckPolicyRequestType request = new CheckPolicyRequestType();
        RequestType reqType = new RequestType();
        request.setRequest(reqType);

        List<SubjectType> subjectList = request.getRequest().getSubject();
        SubjectType subject = new SubjectType();
        List<AttributeType> subjAttrs = subject.getAttribute();
        subjAttrs.add(createAttr(XACML_SUBJECT_ID, XACML_DATATYPE, TEST_USER_ID));
        subjAttrs.add(createAttr(XACML_HOME_COMMUNITY, XACML_DATATYPE, TEST_HC_ID));
        subjAttrs.add(createAttr(XACML_SUBJECT_ORG, XACML_DATATYPE, TEST_USER_ORG));
        subjAttrs.add(createAttr(XACML_SUBJECT_ORG_ID, XACML_DATATYPE, TEST_USER_ORG_ID));
        subjAttrs.add(createAttr(XACML_SUBJECT_ROLE, XACML_DATATYPE, TEST_USER_ROLE));
        subjAttrs.add(createAttr(XACML_SUBJECT_PURPOSE, XACML_DATATYPE, TEST_USER_PURPOSE));
        subjectList.add(subject);

        List<ResourceType> resourceList = request.getRequest().getResource();
        ResourceType resource = new ResourceType();
        List<AttributeType> resAttrs = resource.getAttribute();
        resAttrs.add(createAttr(XACML_RESOURCE_ID, XACML_DATATYPE, TEST_PATIENT_ID));
        resAttrs.add(createAttr(XACML_ASSIGING_AUTH, XACML_DATATYPE, TEST_ASSIGN_AUTH));
        resourceList.add(resource);

        ActionType action = new ActionType();
        List<AttributeType> actionAttrs = action.getAttribute();
        actionAttrs.add(createAttr(XACML_ACTION, XACML_DATATYPE, "SubjectDiscoveryReidentificationIn"));
        request.getRequest().setAction(action);

        AdapterPEPImpl pepImpl = new AdapterPEPImpl();
        Request pdpRequest = pepImpl.createPdpRequest(request);

        verifyXSPASubjects(pdpRequest);
        verifyXSPAResources(pdpRequest);
        verifyXSPAAction(pdpRequest);
        verifyXSPAEnvironment(pdpRequest);
    }

    @Test
    public void testOptOutReidentificationOut() {

        expectedSubjectXSPA.clear();
        expectedSubjectXSPA.put(XSPA_SUBJECT_ID, TEST_USER_ID);
        expectedSubjectXSPA.put(XSPA_SUBJECT_ORG, TEST_USER_ORG);
        expectedSubjectXSPA.put(XSPA_SUBJECT_ORG_ID, TEST_USER_ORG_ID);
        expectedSubjectXSPA.put(XSPA_SUBJECT_ROLE, TEST_USER_ROLE);
        expectedSubjectXSPA.put(XSPA_SUBJECT_PURPOSE, TEST_USER_PURPOSE);

        expectedResourceXSPA.clear();
        expectedResourceXSPA.put(XSPA_RESOURCE_ID, TEST_PATIENT_ID);
        expectedResourceXSPA.put(XSPA_PATIENT_OPT_IN, "No");
        expectedResourceXSPA.put(XSPA_ASSIGNING_AUTH, TEST_ASSIGN_AUTH);
        expectedResourceXSPA.put(XSPA_SERVICE_TYPE, "SubjectDiscoveryReidentificationOut");

        expectedActionXSPA.clear();
        expectedActionXSPA.put(XSPA_ACTION, "read");

        expectedEnvXSPA.clear();
        try {
            String homeCommunityId = PropertyAccessor.getProperty(PROPERTY_FILE_NAME_GATEWAY, PROPERTY_FILE_KEY_HOME_COMMUNITY);
            expectedEnvXSPA.put(XSPA_ENVIRONMENT_LOCALITY, homeCommunityId);
        } catch (PropertyAccessException ex) {
            fail("Can not access: " + PROPERTY_FILE_KEY_HOME_COMMUNITY + " property from: " + PROPERTY_FILE_NAME_GATEWAY);
        }

        CheckPolicyRequestType request = new CheckPolicyRequestType();
        RequestType reqType = new RequestType();
        request.setRequest(reqType);

        List<SubjectType> subjectList = request.getRequest().getSubject();
        SubjectType subject = new SubjectType();
        List<AttributeType> subjAttrs = subject.getAttribute();
        subjAttrs.add(createAttr(XACML_SUBJECT_ID, XACML_DATATYPE, TEST_USER_ID));
        subjAttrs.add(createAttr(XACML_SUBJECT_ORG, XACML_DATATYPE, TEST_USER_ORG));
        subjAttrs.add(createAttr(XACML_SUBJECT_ORG_ID, XACML_DATATYPE, TEST_USER_ORG_ID));
        subjAttrs.add(createAttr(XACML_SUBJECT_ROLE, XACML_DATATYPE, TEST_USER_ROLE));
        subjAttrs.add(createAttr(XACML_SUBJECT_PURPOSE, XACML_DATATYPE, TEST_USER_PURPOSE));
        subjectList.add(subject);

        List<ResourceType> resourceList = request.getRequest().getResource();
        ResourceType resource = new ResourceType();
        List<AttributeType> resAttrs = resource.getAttribute();
        resAttrs.add(createAttr(XACML_HOME_COMMUNITY, XACML_DATATYPE, TEST_HC_ID));
        resAttrs.add(createAttr(XACML_RESOURCE_ID, XACML_DATATYPE, TEST_PATIENT_ID));
        resAttrs.add(createAttr(XACML_ASSIGING_AUTH, XACML_DATATYPE, TEST_ASSIGN_AUTH));
        resourceList.add(resource);

        ActionType action = new ActionType();
        List<AttributeType> actionAttrs = action.getAttribute();
        actionAttrs.add(createAttr(XACML_ACTION, XACML_DATATYPE, "SubjectDiscoveryReidentificationOut"));
        request.getRequest().setAction(action);

        AdapterPEPImpl pepImpl = new AdapterPEPImpl();
        Request pdpRequest = pepImpl.createPdpRequest(request);

        verifyXSPASubjects(pdpRequest);
        verifyXSPAResources(pdpRequest);
        verifyXSPAAction(pdpRequest);
        verifyXSPAEnvironment(pdpRequest);
    }

        @Test
    public void testOptInReidentificationIn() {

        expectedSubjectXSPA.clear();
        expectedSubjectXSPA.put(XSPA_SUBJECT_ID, TEST_USER_ID);
        expectedSubjectXSPA.put(XSPA_SUBJECT_ORG, TEST_USER_ORG);
        expectedSubjectXSPA.put(XSPA_SUBJECT_ORG_ID, TEST_USER_ORG_ID);
        expectedSubjectXSPA.put(XSPA_SUBJECT_ROLE, TEST_REIDENT_USER_ROLE);
        expectedSubjectXSPA.put(XSPA_SUBJECT_PURPOSE, TEST_REIDENT_USER_PURPOSE);

        expectedResourceXSPA.clear();
        expectedResourceXSPA.put(XSPA_RESOURCE_ID, TEST_PATIENT_ID);
        expectedResourceXSPA.put(XSPA_PATIENT_OPT_IN, "Yes");
        expectedResourceXSPA.put(XSPA_ASSIGNING_AUTH, TEST_ASSIGN_AUTH);
        expectedResourceXSPA.put(XSPA_SERVICE_TYPE, "SubjectDiscoveryReidentificationIn");

        expectedActionXSPA.clear();
        expectedActionXSPA.put(XSPA_ACTION, "read");

        expectedEnvXSPA.clear();
        expectedEnvXSPA.put(XSPA_ENVIRONMENT_LOCALITY, TEST_HC_ID);

        CheckPolicyRequestType request = new CheckPolicyRequestType();
        RequestType reqType = new RequestType();
        request.setRequest(reqType);

        List<SubjectType> subjectList = request.getRequest().getSubject();
        SubjectType subject = new SubjectType();
        List<AttributeType> subjAttrs = subject.getAttribute();
        subjAttrs.add(createAttr(XACML_SUBJECT_ID, XACML_DATATYPE, TEST_USER_ID));
        subjAttrs.add(createAttr(XACML_HOME_COMMUNITY, XACML_DATATYPE, TEST_HC_ID));
        subjAttrs.add(createAttr(XACML_SUBJECT_ORG, XACML_DATATYPE, TEST_USER_ORG));
        subjAttrs.add(createAttr(XACML_SUBJECT_ORG_ID, XACML_DATATYPE, TEST_USER_ORG_ID));
        subjAttrs.add(createAttr(XACML_SUBJECT_ROLE, XACML_DATATYPE, TEST_REIDENT_USER_ROLE));
        subjAttrs.add(createAttr(XACML_SUBJECT_PURPOSE, XACML_DATATYPE, TEST_REIDENT_USER_PURPOSE));
        subjectList.add(subject);

        List<ResourceType> resourceList = request.getRequest().getResource();
        ResourceType resource = new ResourceType();
        List<AttributeType> resAttrs = resource.getAttribute();
        resAttrs.add(createAttr(XACML_RESOURCE_ID, XACML_DATATYPE, TEST_PATIENT_ID));
        resAttrs.add(createAttr(XACML_ASSIGING_AUTH, XACML_DATATYPE, TEST_ASSIGN_AUTH));
        resourceList.add(resource);

        ActionType action = new ActionType();
        List<AttributeType> actionAttrs = action.getAttribute();
        actionAttrs.add(createAttr(XACML_ACTION, XACML_DATATYPE, "SubjectDiscoveryReidentificationIn"));
        request.getRequest().setAction(action);

        AdapterPEPImpl pepImpl = new AdapterPEPImpl();
        Request pdpRequest = pepImpl.createPdpRequest(request);

        verifyXSPASubjects(pdpRequest);
        verifyXSPAResources(pdpRequest);
        verifyXSPAAction(pdpRequest);
        verifyXSPAEnvironment(pdpRequest);
    }

    @Test
    public void testOptInReidentificationOut() {

        expectedSubjectXSPA.clear();
        expectedSubjectXSPA.put(XSPA_SUBJECT_ID, TEST_USER_ID);
        expectedSubjectXSPA.put(XSPA_SUBJECT_ORG, TEST_USER_ORG);
        expectedSubjectXSPA.put(XSPA_SUBJECT_ORG_ID, TEST_USER_ORG_ID);
        expectedSubjectXSPA.put(XSPA_SUBJECT_ROLE, TEST_REIDENT_USER_ROLE);
        expectedSubjectXSPA.put(XSPA_SUBJECT_PURPOSE, TEST_REIDENT_USER_PURPOSE);

        expectedResourceXSPA.clear();
        expectedResourceXSPA.put(XSPA_RESOURCE_ID, TEST_PATIENT_ID);
        expectedResourceXSPA.put(XSPA_PATIENT_OPT_IN, "Yes");
        expectedResourceXSPA.put(XSPA_ASSIGNING_AUTH, TEST_ASSIGN_AUTH);
        expectedResourceXSPA.put(XSPA_SERVICE_TYPE, "SubjectDiscoveryReidentificationOut");

        expectedActionXSPA.clear();
        expectedActionXSPA.put(XSPA_ACTION, "read");

        expectedEnvXSPA.clear();
        try {
            String homeCommunityId = PropertyAccessor.getProperty(PROPERTY_FILE_NAME_GATEWAY, PROPERTY_FILE_KEY_HOME_COMMUNITY);
            expectedEnvXSPA.put(XSPA_ENVIRONMENT_LOCALITY, homeCommunityId);
        } catch (PropertyAccessException ex) {
            fail("Can not access: " + PROPERTY_FILE_KEY_HOME_COMMUNITY + " property from: " + PROPERTY_FILE_NAME_GATEWAY);
        }

        CheckPolicyRequestType request = new CheckPolicyRequestType();
        RequestType reqType = new RequestType();
        request.setRequest(reqType);

        List<SubjectType> subjectList = request.getRequest().getSubject();
        SubjectType subject = new SubjectType();
        List<AttributeType> subjAttrs = subject.getAttribute();
        subjAttrs.add(createAttr(XACML_SUBJECT_ID, XACML_DATATYPE, TEST_USER_ID));
        subjAttrs.add(createAttr(XACML_SUBJECT_ORG, XACML_DATATYPE, TEST_USER_ORG));
        subjAttrs.add(createAttr(XACML_SUBJECT_ORG_ID, XACML_DATATYPE, TEST_USER_ORG_ID));
        subjAttrs.add(createAttr(XACML_SUBJECT_ROLE, XACML_DATATYPE, TEST_REIDENT_USER_ROLE));
        subjAttrs.add(createAttr(XACML_SUBJECT_PURPOSE, XACML_DATATYPE, TEST_REIDENT_USER_PURPOSE));
        subjectList.add(subject);

        List<ResourceType> resourceList = request.getRequest().getResource();
        ResourceType resource = new ResourceType();
        List<AttributeType> resAttrs = resource.getAttribute();
        resAttrs.add(createAttr(XACML_HOME_COMMUNITY, XACML_DATATYPE, TEST_HC_ID));
        resAttrs.add(createAttr(XACML_RESOURCE_ID, XACML_DATATYPE, TEST_PATIENT_ID));
        resAttrs.add(createAttr(XACML_ASSIGING_AUTH, XACML_DATATYPE, TEST_ASSIGN_AUTH));
        resourceList.add(resource);

        ActionType action = new ActionType();
        List<AttributeType> actionAttrs = action.getAttribute();
        actionAttrs.add(createAttr(XACML_ACTION, XACML_DATATYPE, "SubjectDiscoveryReidentificationOut"));
        request.getRequest().setAction(action);

        AdapterPEPImpl pepImpl = new AdapterPEPImpl();
        Request pdpRequest = pepImpl.createPdpRequest(request);

        verifyXSPASubjects(pdpRequest);
        verifyXSPAResources(pdpRequest);
        verifyXSPAAction(pdpRequest);
        verifyXSPAEnvironment(pdpRequest);
    }

        @Test
    public void testPatientDiscoveryIn() {

        expectedSubjectXSPA.clear();
        expectedSubjectXSPA.put(XSPA_SUBJECT_ID, TEST_USER_ID);
        expectedSubjectXSPA.put(XSPA_SUBJECT_ORG, TEST_USER_ORG);
        expectedSubjectXSPA.put(XSPA_SUBJECT_ORG_ID, TEST_USER_ORG_ID);
        expectedSubjectXSPA.put(XSPA_SUBJECT_ROLE, TEST_USER_ROLE);
        expectedSubjectXSPA.put(XSPA_SUBJECT_PURPOSE, TEST_USER_PURPOSE);

        expectedResourceXSPA.clear();
        expectedResourceXSPA.put(XSPA_RESOURCE_ID, TEST_PATIENT_ID);
        expectedResourceXSPA.put(XSPA_PATIENT_OPT_IN, "Yes");
        expectedResourceXSPA.put(XSPA_ASSIGNING_AUTH, TEST_ASSIGN_AUTH);
        expectedResourceXSPA.put(XSPA_SERVICE_TYPE, "PatientDiscoveryIn");

        expectedActionXSPA.clear();
        expectedActionXSPA.put(XSPA_ACTION, "create");

        expectedEnvXSPA.clear();
        expectedEnvXSPA.put(XSPA_ENVIRONMENT_LOCALITY, TEST_HC_ID);

        CheckPolicyRequestType request = new CheckPolicyRequestType();
        RequestType reqType = new RequestType();
        request.setRequest(reqType);

        List<SubjectType> subjectList = request.getRequest().getSubject();
        SubjectType subject = new SubjectType();
        List<AttributeType> subjAttrs = subject.getAttribute();
        subjAttrs.add(createAttr(XACML_SUBJECT_ID, XACML_DATATYPE, TEST_USER_ID));
        subjAttrs.add(createAttr(XACML_HOME_COMMUNITY, XACML_DATATYPE, TEST_HC_ID));
        subjAttrs.add(createAttr(XACML_SUBJECT_ORG, XACML_DATATYPE, TEST_USER_ORG));
        subjAttrs.add(createAttr(XACML_SUBJECT_ORG_ID, XACML_DATATYPE, TEST_USER_ORG_ID));
        subjAttrs.add(createAttr(XACML_SUBJECT_ROLE, XACML_DATATYPE, TEST_USER_ROLE));
        subjAttrs.add(createAttr(XACML_SUBJECT_PURPOSE, XACML_DATATYPE, TEST_USER_PURPOSE));
        subjectList.add(subject);

        List<ResourceType> resourceList = request.getRequest().getResource();
        ResourceType resource = new ResourceType();
        List<AttributeType> resAttrs = resource.getAttribute();
        resAttrs.add(createAttr(XACML_RESOURCE_ID, XACML_DATATYPE, TEST_PATIENT_ID));
        resAttrs.add(createAttr(XACML_ASSIGING_AUTH, XACML_DATATYPE, TEST_ASSIGN_AUTH));
        resourceList.add(resource);

        ActionType action = new ActionType();
        List<AttributeType> actionAttrs = action.getAttribute();
        actionAttrs.add(createAttr(XACML_ACTION, XACML_DATATYPE, "PatientDiscoveryIn"));
        request.getRequest().setAction(action);

        AdapterPEPImpl pepImpl = new AdapterPEPImpl() {
            protected List<String> determinePatientOptStatus(List<String> resourceIds, List<String> assigningAuths) {
                List<String> optStatusList = new ArrayList<String>();
                optStatusList.add("Yes");
                return optStatusList;
            }
        };
        Request pdpRequest = pepImpl.createPdpRequest(request);

        verifyXSPASubjects(pdpRequest);
        verifyXSPAResources(pdpRequest);
        verifyXSPAAction(pdpRequest);
        verifyXSPAEnvironment(pdpRequest);
    }

    @Test
    public void testPatientDiscoveryOut() {

        expectedSubjectXSPA.clear();
        expectedSubjectXSPA.put(XSPA_SUBJECT_ID, TEST_USER_ID);
        expectedSubjectXSPA.put(XSPA_SUBJECT_ORG, TEST_USER_ORG);
        expectedSubjectXSPA.put(XSPA_SUBJECT_ORG_ID, TEST_USER_ORG_ID);
        expectedSubjectXSPA.put(XSPA_SUBJECT_ROLE, TEST_USER_ROLE);
        expectedSubjectXSPA.put(XSPA_SUBJECT_PURPOSE, TEST_USER_PURPOSE);

        expectedResourceXSPA.clear();
        expectedResourceXSPA.put(XSPA_RESOURCE_ID, TEST_PATIENT_ID);
        expectedResourceXSPA.put(XSPA_PATIENT_OPT_IN, "Yes");
        expectedResourceXSPA.put(XSPA_ASSIGNING_AUTH, TEST_ASSIGN_AUTH);
        expectedResourceXSPA.put(XSPA_SERVICE_TYPE, "PatientDiscoveryOut");

        expectedActionXSPA.clear();
        expectedActionXSPA.put(XSPA_ACTION, "create");

        expectedEnvXSPA.clear();
        try {
            String homeCommunityId = PropertyAccessor.getProperty(PROPERTY_FILE_NAME_GATEWAY, PROPERTY_FILE_KEY_HOME_COMMUNITY);
            expectedEnvXSPA.put(XSPA_ENVIRONMENT_LOCALITY, homeCommunityId);
        } catch (PropertyAccessException ex) {
            fail("Can not access: " + PROPERTY_FILE_KEY_HOME_COMMUNITY + " property from: " + PROPERTY_FILE_NAME_GATEWAY);
        }

        CheckPolicyRequestType request = new CheckPolicyRequestType();
        RequestType reqType = new RequestType();
        request.setRequest(reqType);

        List<SubjectType> subjectList = request.getRequest().getSubject();
        SubjectType subject = new SubjectType();
        List<AttributeType> subjAttrs = subject.getAttribute();
        subjAttrs.add(createAttr(XACML_SUBJECT_ID, XACML_DATATYPE, TEST_USER_ID));
        subjAttrs.add(createAttr(XACML_SUBJECT_ORG, XACML_DATATYPE, TEST_USER_ORG));
        subjAttrs.add(createAttr(XACML_SUBJECT_ORG_ID, XACML_DATATYPE, TEST_USER_ORG_ID));
        subjAttrs.add(createAttr(XACML_SUBJECT_ROLE, XACML_DATATYPE, TEST_USER_ROLE));
        subjAttrs.add(createAttr(XACML_SUBJECT_PURPOSE, XACML_DATATYPE, TEST_USER_PURPOSE));
        subjectList.add(subject);

        List<ResourceType> resourceList = request.getRequest().getResource();
        ResourceType resource = new ResourceType();
        List<AttributeType> resAttrs = resource.getAttribute();
        resAttrs.add(createAttr(XACML_HOME_COMMUNITY, XACML_DATATYPE, TEST_HC_ID));
        resAttrs.add(createAttr(XACML_RESOURCE_ID, XACML_DATATYPE, TEST_PATIENT_ID));
        resAttrs.add(createAttr(XACML_ASSIGING_AUTH, XACML_DATATYPE, TEST_ASSIGN_AUTH));
        resourceList.add(resource);

        ActionType action = new ActionType();
        List<AttributeType> actionAttrs = action.getAttribute();
        actionAttrs.add(createAttr(XACML_ACTION, XACML_DATATYPE, "PatientDiscoveryOut"));
        request.getRequest().setAction(action);

        AdapterPEPImpl pepImpl = new AdapterPEPImpl() {
            protected List<String> determinePatientOptStatus(List<String> resourceIds, List<String> assigningAuths) {
                List<String> optStatusList = new ArrayList<String>();
                optStatusList.add("Yes");
                return optStatusList;
            }
        };
        Request pdpRequest = pepImpl.createPdpRequest(request);

        verifyXSPASubjects(pdpRequest);
        verifyXSPAResources(pdpRequest);
        verifyXSPAAction(pdpRequest);
        verifyXSPAEnvironment(pdpRequest);
    }

    private AttributeType createAttr(String xacmlId, String xacmlType, String value) {
        AttributeType retAttr = new AttributeType();
        retAttr.setAttributeId(xacmlId);
        retAttr.setDataType(xacmlType);
        List<AttributeValueType> attrValList = retAttr.getAttributeValue();
        AttributeValueType attrVal = new AttributeValueType();
        attrVal.getContent().add(value);
        attrValList.add(attrVal);
        return retAttr;
    }

    private void verifyXSPASubjects(Request pdpRequest) {
        List<Subject> pdpSubjList = pdpRequest.getSubjects();
        assertNotNull("Generated PDP should contain a subject", pdpSubjList);
        if (pdpSubjList != null && pdpSubjList.size() == 1) {
            Subject subj = pdpSubjList.get(0);
            assertNotNull("Generated PDP subject is null", subj);
            List<Attribute> subjAttrList = subj.getAttributes();
            assertNotNull("Generated PDP subject attribute list is null", subjAttrList);
            if (subjAttrList != null && subjAttrList.size() == expectedSubjectXSPA.size()) {
                for (Attribute subjAttr : subjAttrList) {
                    assertNotNull("Subject Attribute is null", subjAttr);
                    assertNotNull("Subject Attribute Id is null", subjAttr);
                    if (subjAttr != null && subjAttr.getAttributeId() != null) {
                        String subjectKey = subjAttr.getAttributeId().toString();
                        assertTrue(subjectKey + " is not valid", expectedSubjectXSPA.containsKey(subjectKey));
                        List<DeferredElementNSImpl> attrValList = subjAttr.getAttributeValues();
                        if (attrValList != null && attrValList.size() == 1) {
                            for (DeferredElementNSImpl attrVal : attrValList) {
                                String attrValText = attrVal.getTextContent();
                                assertTrue("Attribute: " + subjectKey +
                                        " expects value: " + expectedSubjectXSPA.get(subjectKey) +
                                        " but has: " + attrValText, expectedSubjectXSPA.get(subjectKey).equals(attrValText));
                            }
                        } else {
                            fail("Attribute: " + subjectKey + " should have 1 attribute value");
                        }
                    }
                }
            } else {
                fail("Generated PDP should contain " + expectedSubjectXSPA.size() + " subject attributes");
            }
        } else {
            fail("Generated PDP should contain 1 subject entry");
        }
    }

    private void verifyXSPAResources(Request pdpRequest) {
        List<Resource> pdpResourceList = pdpRequest.getResources();
        assertNotNull("Generated PDP should contain a resource", pdpResourceList);
        if (pdpResourceList != null && pdpResourceList.size() == 1) {
            Resource res = pdpResourceList.get(0);
            assertNotNull("Generated PDP resource is null", res);
            List<Attribute> resAttrList = res.getAttributes();
            assertNotNull("Generated PDP resource attribute list is null", resAttrList);
            if (resAttrList != null && resAttrList.size() == expectedResourceXSPA.size()) {
                for (Attribute resAttr : resAttrList) {
                    assertNotNull("Resource Attribute is null", resAttr);
                    assertNotNull("Resource Attribute Id is null", resAttr);
                    if (resAttr != null && resAttr.getAttributeId() != null) {
                        String resourceKey = resAttr.getAttributeId().toString();
                        assertTrue(resourceKey + " is not valid", expectedResourceXSPA.containsKey(resourceKey));
                        List<DeferredElementNSImpl> attrValList = resAttr.getAttributeValues();
                        if (attrValList != null && attrValList.size() == 1) {
                            for (DeferredElementNSImpl attrVal : attrValList) {
                                String attrValText = attrVal.getTextContent();
                                assertTrue("Attribute: " + resourceKey +
                                        " expects value: " + expectedResourceXSPA.get(resourceKey) +
                                        " but has: " + attrValText, expectedResourceXSPA.get(resourceKey).equals(attrValText));
                            }
                        } else {
                            fail("Attribute: " + resourceKey + " should have 1 attribute value");
                        }
                    }
                }
            } else {
                fail("Generated PDP should contain " + expectedResourceXSPA.size() + " resource attributes");
            }
        } else {
            fail("Generated PDP should contain 1 resource entry");
        }
    }

    private void verifyXSPAAction(Request pdpRequest) {
        Action action = pdpRequest.getAction();
        assertNotNull("Generated PDP action is null", action);
        List<Attribute> actionAttrList = action.getAttributes();
        assertNotNull("Generated PDP action attribute list is null", actionAttrList);
        if (actionAttrList != null && actionAttrList.size() == expectedActionXSPA.size()) {
            for (Attribute actionAttr : actionAttrList) {
                assertNotNull("Action Attribute is null", actionAttr);
                assertNotNull("Action Attribute Id is null", actionAttr);
                if (actionAttr != null && actionAttr.getAttributeId() != null) {
                    String actionKey = actionAttr.getAttributeId().toString();
                    assertTrue(actionKey + " is not valid", expectedActionXSPA.containsKey(actionKey));
                    List<DeferredElementNSImpl> attrValList = actionAttr.getAttributeValues();
                    if (attrValList != null && attrValList.size() == 1) {
                        for (DeferredElementNSImpl attrVal : attrValList) {
                            String attrValText = attrVal.getTextContent();
                            assertTrue("Attribute: " + actionKey +
                                    " expects value: " + expectedActionXSPA.get(actionKey) +
                                    " but has: " + attrValText, expectedActionXSPA.get(actionKey).equals(attrValText));
                        }
                    } else {
                        fail("Attribute: " + actionKey + " should have 1 attribute value");
                    }
                }
            }
        } else {
            fail("Generated PDP should contain " + expectedActionXSPA.size() + " action attribute");
        }

    }

    private void verifyXSPAEnvironment(Request pdpRequest) {
        Environment environment = pdpRequest.getEnvironment();
        assertNotNull("Generated PDP environment is null", environment);
        List<Attribute> environmentAttrList = environment.getAttributes();
        assertNotNull("Generated PDP environment attribute list is null", environmentAttrList);
        if (environmentAttrList != null && environmentAttrList.size() == expectedEnvXSPA.size()) {
            for (Attribute environmentAttr : environmentAttrList) {
                assertNotNull("Environment Attribute is null", environmentAttr);
                assertNotNull("Environment Attribute Id is null", environmentAttr);
                if (environmentAttr != null && environmentAttr.getAttributeId() != null) {
                    String environmentKey = environmentAttr.getAttributeId().toString();
                    assertTrue(environmentKey + " is not valid", expectedEnvXSPA.containsKey(environmentKey));
                    List<DeferredElementNSImpl> attrValList = environmentAttr.getAttributeValues();
                    if (attrValList != null && attrValList.size() == 1) {
                        for (DeferredElementNSImpl attrVal : attrValList) {
                            String attrValText = attrVal.getTextContent();
                            assertTrue("Attribute: " + environmentKey +
                                    " expects value: " + expectedEnvXSPA.get(environmentKey) +
                                    " but has: " + attrValText, expectedEnvXSPA.get(environmentKey).equals(attrValText));
                        }
                    } else {
                        fail("Attribute: " + environmentKey + " should have 1 attribute value");
                    }
                }
            }
        } else {
            fail("Generated PDP should contain " + expectedEnvXSPA.size() + " environment attribute");
        }
    }
}
