/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.testpep;

import gov.hhs.fha.nhinc.testpepschema.EnforceResourceOutputType;
import gov.hhs.fha.nhinc.testpepservice.TestPEPServicePortType;

import com.sun.identity.xacml.client.XACMLRequestProcessor;
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
import com.sun.identity.xacml.context.Subject;
import com.sun.identity.xacml.policy.Obligations;


import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import javax.jws.WebService;
import javax.print.DocFlavor.STRING;


/**
 *
 * @author Admin
 */
@WebService(serviceName = "TestPEPServiceService", portName = "TestPEPServicePort", endpointInterface = "gov.hhs.fha.nhinc.testpepservice.TestPEPServicePortType", targetNamespace = "urn:gov:hhs:fha:nhinc:testpepservice", wsdlLocation = "WEB-INF/wsdl/TestPEPService/TestPEPService.wsdl")
public class TestPEPService implements TestPEPServicePortType
{
    public static final String PERMIT = "Permit";
    public static final String DENY   = "Deny";
    public static final String INDETERMINATE   = "Indeterminate";
    public static final String STRING = "http://www.w3.org/2001/XMLSchema#string";
    public static final String TIME = "http://www.w3.org/2001/XMLSchema#time";
    public static String ACCESS_SUBJECT = "urn:oasis:names:tc:xacml:1.0:subject-category:access-subject";


    private static String SUBJECT_ID_NS = "urn:oasis:names:tc:xacml:2.0:subject:subject-id";
    private static String SUBJECT_NPI_NS = "urn:oasis:names:tc:xspa:1.0:subject:npi";
    private static String SUBJECT_LOCALITY_NS = "urn:oasis:names:tc:xacml:2.0:subject:locality";
    private static String SUBJECT_PERMISSIONS_NS = "urn:oasis:names:tc:xspa:1.0:subject:hl7:permission";
    private static String SUBJECT_STRUCTURED_ROLE_NS = "urn:oasis:names:tc:xacml:2.0:subject:role";
    private static String SUBJECT_FUNCTIONAL_ROLE_NS = "urn:oasis:names:tc:xspa:1.0:subject:functional_role";
    private static String SUBJECT_PURPOSE_OF_USE_NS = "urn:oasis:names:tc:xspa:1.0:subject:purposeofuse";
    private static String RESOURCE_ID_NS = "urn:oasis:names:tc:xacml:2.0:resource:resource-id";
    private static String RESOURCE_TYPE_NS = "urn:oasis:names:tc:xspa:1.0:resource:hl7:type";
    private static String RESOURCE_ACTION_ID_NS = "urn:oasis:names:tc:xacml:1.0:action:action-id";
    private static String RESOURCE_LOCALITY_NS = "urn:oasis:names:tc:xacml:2.0:resource:locality";
    private static String RESOURCE_DIRECTIVE_PDF_NS = "urn:oasis:names:tc:xspa:1.0:resource:user:directive-pdf";
    private static String RESOURCE_POLICY_XACML_NS = "urn:oasis:names:tc:xspa:1.0:resource:user:directive-xacml";

    //namespace for organization and patient consent directives
    //these are non-normative
    private static String RESOURCE_DATETIME_REQ_NS = "urn:oasis:names:tc:xspa:1.0:resource:org:hoursofoperation";
    private static String DAY_OF_WEEK_NS = "urn:oasis:names:tc:xspa:1.0:resource:org:operating-day";
    private static String HOUR_OF_DAY_START_NS = "urn:oasis:names:tc:xspa:1.0:resource:org:hoursofoperation:start";
    private static String HOUR_OF_DAY_END_NS = "urn:oasis:names:tc:xspa:1.0:resource:org:hoursofoperation:end";
    private static String RESOURCE_ALLOW_ORGS_NS = "urn:oasis:names:tc:xspa:1.0:resource:org:allowed-organizations";
    private static String RESOURCE_REQUIRED_ROLES_NS = "urn:oasis:names:tc:xspa:1.0:resource:org:role";
    private static String RESOURCE_REQUIRED_PERMISSIONS_NS = "urn:oasis:names:tc:xspa:1.0:resource:org:hl7:permission";

    //Namespace for patient consent directive --- Normative
    private static String RESOURCE_PATIENT_ALLOWED_ORG_NS = "urn:oasis:names:tc:xspa:1.0:resource:patient:allowed-organizations";
    private static String RESOURCE_PATIENT_CONSENT_CODE_NS = "urn:oasis:names:tc:xspa:1.0.resource:patient:hl7:confidentiality-code";
    private static String RESOURCE_PATIENT_DISSENTING_ROLES_NS = "urn:oasis:names:tc:xspa:1.0:resource:patient:dissenting-role";
    private static String RESOURCE_PATIENT_DISSENTING_SUBJECTS_NS = "urn:oasis:names:tc:xspa:1.0:resource:patient:dissenting-subject-id";
    //this need to be added
    private static String RESOURCE_PATIENT_OBJECT_MASKING_NS = "urn:oasis:names:tc:xspa:1.0:resource:patient:masked";
    private static String SUBJECT_STRUCTURED_ROLE_OID = "codeSystem=\"1.2.840.1986.7\" codeSystemName=\"ISO\" displayName=";
    private static String RESOURCE_REQUIRED_PERMISSIONS_OID = "codeSystem=\"2.16.840.1.113883.13.27\" codeSystemName=\"HL7\" displayName=";


    //add this to service properties file
    private String pipEndpoint = "http://localhost/XSPAAccessControl/XSPAPolicyInformationPointService?wsdl";
    private String auditEndpoint = "http://localhost/XSPAAccessControl/XSPAAttributeServiceService?wsdl";

    private String pdpEntityId = "jerichoPdpEntity";
    ///private String pdpEntityId = "sunPdpEntity";
    private String pepEntityId = "vaPepEntity";

    public gov.hhs.fha.nhinc.testpepschema.EnforceResourceOutputType enforceResource(gov.hhs.fha.nhinc.testpepschema.EnforceResourceInputType enforceResourceInput) 
    {
        String sResponse = "Deny";
        System.out.println("Entered EnforceResource method.");
        gov.hhs.fha.nhinc.testpepschema.EnforceResourceOutputType oResponse = new EnforceResourceOutputType();

        try
        {
            Request xacmlRequest = createXacmlRequest();

            System.out.println("Request: " + xacmlRequest.toXMLString());

            Response xacmlResponse = XACMLRequestProcessor.getInstance()
                .processRequest(xacmlRequest, pdpEntityId, pepEntityId);

            if (xacmlResponse != null)
            {
                System.out.println("Response: " + xacmlResponse.toXMLString());

                // Just look at the first result...
                //---------------------------------
                List<Result> oaResult = xacmlResponse.getResults();
                if ((oaResult != null) &&
                    (oaResult.size() > 0) &&
                    (oaResult.get(0).getDecision() != null) &&
                    (oaResult.get(0).getDecision().getValue() != null) &&
                    (oaResult.get(0).getDecision().getValue().length() > 0))
                {
                    System.out.println("sResponse = " + oaResult.get(0).getDecision().getValue());
                   sResponse = oaResult.get(0).getDecision().getValue();
                }
            }

        }
        catch (Exception e)
        {
            System.out.println("An exception occurred: " + e.getMessage());
            e.printStackTrace();
        }

        oResponse.setOutputParam(sResponse);


        System.out.println("Leaving EnforceResource method. Output = " + oResponse.getOutputParam());
        return oResponse;
    }

    private Request createXacmlRequest()
            throws XACMLException, URISyntaxException {

        Request request = ContextFactory.getInstance().createRequest();

        Iterator valuesIter = null;

        //Subject
        Subject subject = ContextFactory.getInstance().createSubject();
        subject.setSubjectCategory(new URI(ACCESS_SUBJECT));

        //set subject id : subjectIen
        Attribute attribute = ContextFactory.getInstance().createAttribute();
        attribute.setAttributeId(new URI(SUBJECT_ID_NS));
        attribute.setDataType(new URI(STRING));
        List valueList = new ArrayList();
        valueList.add("John Doe");
        attribute.setAttributeStringValues(valueList);
        List attributeList = new ArrayList();
        attributeList.add(attribute);

        //US Domain Only NPI
        attribute = ContextFactory.getInstance().createAttribute();
        attribute.setAttributeId(new URI(SUBJECT_NPI_NS));
        attribute.setDataType(new URI(STRING));
        valueList = new ArrayList();
        valueList.add("");
        attribute.setAttributeStringValues(valueList);
        attributeList.add(attribute);

        //set subject Structured Roles
        attribute = ContextFactory.getInstance().createAttribute();
        attribute.setAttributeId(new URI(SUBJECT_STRUCTURED_ROLE_NS));
        attribute.setDataType(new URI(STRING));
        valueList = new ArrayList();
        valueList.add("");      // TODO put real role in here.
        attribute.setAttributeStringValues(valueList);
        attributeList.add(attribute);
        //set subject functional Role
        attribute = ContextFactory.getInstance().createAttribute();
        attribute.setAttributeId(new URI(SUBJECT_FUNCTIONAL_ROLE_NS));
        attribute.setDataType(new URI(STRING));
        valueList = new ArrayList();
        valueList.add(""); // Put xspasubject.getFunctionalRole()
        attribute.setAttributeStringValues(valueList);
        attributeList.add(attribute);
        //set subject purpose of use
        attribute = ContextFactory.getInstance().createAttribute();
        attribute.setAttributeId(new URI(SUBJECT_PURPOSE_OF_USE_NS));
        attribute.setDataType(new URI(STRING));
        valueList = new ArrayList();
        valueList.add("");  // xspasubject.getPurposeOfUse()
        attribute.setAttributeStringValues(valueList);
        attributeList.add(attribute);

        //set subjectHl7Permissions
        //if (xspasubject.getHl7Permissions() != null && !xspasubject.getHl7Permissions().isEmpty()) {
//        attribute = ContextFactory.getInstance().createAttribute();
//        attribute.setAttributeId(new URI(SUBJECT_PERMISSIONS_NS));
//        attribute.setDataType(new URI(STRING));
//        valueList = new ArrayList();
//        List<String> perms = new ArrayList<String>();//xspasubject.getHl7Permissions();
//        Iterator iter = perms.iterator();
//        while (iter.hasNext()) {
//            String perm = (String)iter.next();
//            valueList.add(perm);
//        }
//        attribute.setAttributeStringValues(valueList);
//        attributeList.add(attribute);
        //}

        //set subjectLocality
        attribute = ContextFactory.getInstance().createAttribute();
        attribute.setAttributeId(new URI(SUBJECT_LOCALITY_NS));
        attribute.setDataType(new URI(STRING));
        valueList = new ArrayList();
        valueList.add(""); // xspasubject.getXspaLocality()
        attribute.setAttributeStringValues(valueList);
        attributeList.add(attribute);

        //set subject attributes
        subject.setAttributes(attributeList);

        //set Subject in Request
        List subjectList = new ArrayList();
        subjectList.add(subject);
        request.setSubjects(subjectList);

        //Resource
        Resource resource = ContextFactory.getInstance().createResource();

        //set resource id: recordId
        //if (xsparesource.getResourceID() != null && xsparesource.getResourceID().length() > 0) {
        attribute = ContextFactory.getInstance().createAttribute();
        attribute.setAttributeId(new URI(RESOURCE_ID_NS));
        attribute.setDataType( new URI(STRING));
        valueList = new ArrayList();
        valueList.add(""); // xsparesource.getResourceID()
        attribute.setAttributeStringValues(valueList);
        attributeList = new ArrayList();
        attributeList.add(attribute);
        //}

        attribute = ContextFactory.getInstance().createAttribute();
        attribute.setAttributeId(new URI(RESOURCE_TYPE_NS));
        attribute.setDataType( new URI(STRING));
        valueList = new ArrayList();
        valueList.add(RESOURCE_TYPE_NS+":"+""); // xsparesource.getResourceType()
        attribute.setAttributeStringValues(valueList);
        attributeList = new ArrayList();
        attributeList.add(attribute);

        //organizations hours of operation
//        if (xspaorg.getBusinessHours() != null) {
//            Hoursofoperations[] hours = xspaorg.getBusinessHours();
//            Calendar cal = Calendar.getInstance();
//            String today = cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.US);
//            for (int i = 0; i < hours.length; i++) {
//                Hoursofoperations ops = hours[i];
//                if (today.toUpperCase().equals(ops.getDayofweek().toUpperCase())) {
//                    attribute = ContextFactory.getInstance().createAttribute();
//                    attribute.setAttributeId(new URI(HOUR_OF_DAY_START_NS));
//                    attribute.setDataType(new URI(TIME));
//                    valueList = new ArrayList();
//                    valueList.add(ops.getStarttime()+":00-08:00");
//                    attribute.setAttributeStringValues(valueList);
//                    attributeList.add(attribute);
//                    attribute = ContextFactory.getInstance().createAttribute();
//                    attribute.setAttributeId(new URI(HOUR_OF_DAY_END_NS));
//                    attribute.setDataType(new URI(TIME));
//                    valueList = new ArrayList();
//                    valueList.add(ops.getEndtime()+":00-08:00");
//                    attribute.setAttributeStringValues(valueList);
//                    attributeList.add(attribute);
//                }
//            }
//        }

        //allowed organizations
//        if (xspaorg.getAllOrganizations() != null) {
//            attribute = ContextFactory.getInstance().createAttribute();
//            attribute.setAttributeId(new URI(RESOURCE_ALLOW_ORGS_NS));
//            attribute.setDataType(new URI(STRING));
//            valueList = new ArrayList();
//            Allowedorganizations[] orgs = xspaorg.getAllOrganizations();
//            for (int i = 0; i < orgs.length; i++) {
//                Allowedorganizations org = orgs[i];
//                valueList.add(org.getOrgName());
//            }
//            attribute.setAttributeStringValues(valueList);
//            attributeList.add(attribute);
//        }

        //organization required roles for resource
//        if (xspaorg.getRequiredRoles() != null) {
//            attribute = ContextFactory.getInstance().createAttribute();
//            attribute.setAttributeId(new URI(RESOURCE_REQUIRED_ROLES_NS));
//            attribute.setDataType(new URI(STRING));
//            valueList = new ArrayList();
//            Orgrequiredroles[] roles = xspaorg.getRequiredRoles();
//            for (int i = 0; i < roles.length; i++) {
//                Orgrequiredroles role = roles[i];
//                String roleVal = SUBJECT_STRUCTURED_ROLE_OID + "\""+ role.getRoleName() + "\"";
//                valueList.add(roleVal);
//            }
//            attribute.setAttributeStringValues(valueList);
//            attributeList.add(attribute);
//        }

        //organization required permissions for resource
//        if (xspaorg.getRequiredPermissions() != null) {
//            attribute = ContextFactory.getInstance().createAttribute();
//            attribute.setAttributeId(new URI(RESOURCE_REQUIRED_PERMISSIONS_NS));
//            attribute.setDataType(new URI(STRING));
//            valueList = new ArrayList();
//            Requiredobjectpermissions[] perms = xspaorg.getRequiredPermissions();
//            for (int i = 0; i < perms.length; i++) {
//                Requiredobjectpermissions perm = perms[i];
//                String permval = RESOURCE_REQUIRED_PERMISSIONS_OID +"\""+ perm.getPermission() + "\"";
//                valueList.add(permval);
//            }
//            attribute.setAttributeStringValues(valueList);
//            attributeList.add(attribute);
//        }


        //patient consent directive
//        if (xspadirective.getAllowedOrgs() != null) {
//            attribute = ContextFactory.getInstance().createAttribute();
//            attribute.setAttributeId(new URI(RESOURCE_PATIENT_ALLOWED_ORG_NS));
//            attribute.setDataType(new URI(STRING));
//            valueList = new ArrayList();
//            Patientallowedorganizations[] orgs = xspadirective.getAllowedOrgs();
//            for (int i = 0; i < orgs.length; i++) {
//                Patientallowedorganizations org = orgs[i];
//                valueList.add(org.getOrgName());
//            }
//            attribute.setAttributeStringValues(valueList);
//            attributeList.add(attribute);
//        }

//        if (xspadirective.getConsentCodes() != null) {
//            attribute = ContextFactory.getInstance().createAttribute();
//            attribute.setAttributeId(new URI(RESOURCE_PATIENT_CONSENT_CODE_NS));
//            attribute.setDataType(new URI(STRING));
//            valueList = new ArrayList();
//            Xspaconsentcodes[] codes = xspadirective.getConsentCodes();
//            for (int i = 0; i < codes.length; i++) {
//                Xspaconsentcodes code = codes[i];
//                valueList.add(code.getConsentCode());
//            }
//            attribute.setAttributeStringValues(valueList);
//            attributeList.add(attribute);
//        }

//        if (xspadirective.getDissentedRoles() != null) {
//            attribute = ContextFactory.getInstance().createAttribute();
//            attribute.setAttributeId(new URI(RESOURCE_PATIENT_DISSENTING_ROLES_NS));
//            attribute.setDataType(new URI(STRING));
//            valueList = new ArrayList();
//            Xspaastmroles[] roles = xspadirective.getDissentedRoles();
//            for (int i = 0; i < roles.length; i++) {
//                Xspaastmroles role = roles[i];
//                String roleVal = SUBJECT_STRUCTURED_ROLE_OID + "\""+ role.getName() + "\"";
//                valueList.add(roleVal);
//            }
//            attribute.setAttributeStringValues(valueList);
//            attributeList.add(attribute);
//        }

//        if (xspadirective.getMaskedObjectRoles() != null) {
//            Patientdissentedrolesobjects[] objs = xspadirective.getMaskedObjectRoles();
//            for (int i = 0; i < objs.length; i++) {
//                Patientdissentedrolesobjects obj = objs[i];
//                String mURI = RESOURCE_PATIENT_OBJECT_MASKING_NS+":"+obj.getPatientdissentedrolesobjectsPK().getHealtcareObject()+":dissenting-role";
//                attribute = ContextFactory.getInstance().createAttribute();
//                attribute.setAttributeId(new URI(mURI));
//                attribute.setDataType(new URI(STRING));
//                valueList = new ArrayList();
//                //value
//                String roleVal = SUBJECT_STRUCTURED_ROLE_OID + "\""+ obj.getRoleName() + "\"";
//                valueList.add(roleVal);
//                attribute.setAttributeStringValues(valueList);
//                attributeList.add(attribute);
//            }
//        }

//        if (xspadirective.getDissentedProviders() != null) {
//            attribute = ContextFactory.getInstance().createAttribute();
//            attribute.setAttributeId(new URI(RESOURCE_PATIENT_DISSENTING_SUBJECTS_NS));
//            attribute.setDataType(new URI(STRING));
//            valueList = new ArrayList();
//            String[] npis = xspadirective.getDissentedProviders();
//            for (int i = 0; i < npis.length; i++) {
//                String npi = npis[i];
//                valueList.add(npi);
//            }
//            attribute.setAttributeStringValues(valueList);
//            attributeList.add(attribute);
//        }

//        if (xspadirective.getMaskedObjectProviders() != null) {
//            Patientdisproviderobjects[] objs = xspadirective.getMaskedObjectProviders();
//            for (int i = 0; i < objs.length; i++) {
//                Patientdisproviderobjects obj = objs[i];
//                String mURI = RESOURCE_PATIENT_OBJECT_MASKING_NS+":"+obj.getPatientdisproviderobjectsPK().getHealthcareObject()+":dissenting-subject-id";
//                attribute = ContextFactory.getInstance().createAttribute();
//                attribute.setAttributeId(new URI(mURI));
//                attribute.setDataType(new URI(STRING));
//                valueList = new ArrayList();
//                //value
//                String val = ""+obj.getPatientdisproviderobjectsPK().getProviderIen();
//                valueList.add(val);
//                attribute.setAttributeStringValues(valueList);
//                attributeList.add(attribute);
//            }
//        }

        //Not in HIMSS demo
        //business rule evaluation
        /*
        if (author != null && author.length() > 0) {
            attribute = ContextFactory.getInstance().createAttribute();
            attribute.setAttributeId(new URI(PROGRESS_NOTE_AUTHOR));
            attribute.setDataType( new URI(STRING));
            valueList = new ArrayList();
            valueList.add(author);
            attribute.setAttributeStringValues(valueList);
            //attributeList = new ArrayList();
            attributeList.add(attribute);

            attribute = ContextFactory.getInstance().createAttribute();
            attribute.setAttributeId(new URI(PROGRESS_NOTE_SIGNED));
            attribute.setDataType( new URI(STRING));
            valueList = new ArrayList();
            if (isSigned) {
                valueList.add("True");
            }
            else {
                valueList.add("False");
            }
            attribute.setAttributeStringValues(valueList);
            //attributeList = new ArrayList();
            attributeList.add(attribute);

        }
         */

        //set resource attributes
        resource.setAttributes(attributeList);

        //set Resource in Request
        List resourceList = new ArrayList();
        resourceList.add(resource);
        request.setResources(resourceList);


        //Action
        Action action = ContextFactory.getInstance().createAction();
        attribute = ContextFactory.getInstance().createAttribute();
        attribute.setAttributeId(new URI(RESOURCE_ACTION_ID_NS));
        attribute.setDataType(new URI(STRING));

        //set actionId
        valueList = new ArrayList();
        valueList.add("Read");
        attribute.setAttributeStringValues(valueList);
        attributeList = new ArrayList();
        attributeList.add(attribute);
        action.setAttributes(attributeList);

        //set Action in Request
        request.setAction(action);

        //Enviornment, our PDP does not use environment now
        Environment environment = ContextFactory.getInstance().createEnvironment();
        attribute = ContextFactory.getInstance().createAttribute();
        attribute.setAttributeId(new URI(RESOURCE_LOCALITY_NS));
        attribute.setDataType(new URI(STRING));
        valueList = new ArrayList();
        valueList.add("Healthcare Domain B");
        attribute.setAttributeStringValues(valueList);
        attributeList = new ArrayList();
        attributeList.add(attribute);
        environment.setAttributes(attributeList);
        request.setEnvironment(environment);
        return request;
    }


}
