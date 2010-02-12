/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.transform.policy;
import gov.hhs.fha.nhinc.common.eventcommon.XDREventType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.util.format.PatientIdFormatUtil;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectListType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryPackageType;
import oasis.names.tc.xacml._2_0.context.schema.os.RequestType;
import oasis.names.tc.xacml._2_0.context.schema.os.SubjectType;
import oasis.names.tc.xacml._2_0.context.schema.os.ResourceType;
import org.hl7.v3.II;
/**
 *
 * @author dunnek
 */
public class XDRPolicyTransformHelper {

    private static Log log = null;
    private static final String ActionInValue = "XDRIn";
    private static final String ActionOutValue = "XDROut";
    private static final String PatientAssigningAuthorityAttributeId = Constants.AssigningAuthorityAttributeId;
    private static final String PatientIdAttributeId = Constants.ResourceIdAttributeId;

    public XDRPolicyTransformHelper() {
        log = createLogger();
    }

   /**
     * Transform method to create a CheckPolicyRequest object from a 201306 message
     * @param request
     * @return CheckPolicyRequestType
     */

    public CheckPolicyRequestType transformXDRToCheckPolicy(XDREventType event) {
        log.debug("Begin -- XDRPolicyTransformHelper.transformXDRToCheckPolicy()");
        CheckPolicyRequestType checkPolicyRequest = null;

        if (event == null) {
            log.debug("Request is null.");
            return checkPolicyRequest;
        } else {
            checkPolicyRequest = new CheckPolicyRequestType();
        }

        RequestType request = new RequestType();

        SubjectHelper subjHelp = new SubjectHelper();
        SubjectType subject = subjHelp.subjectFactory(event.getSendingHomeCommunity(), event.getMessage().getAssertion());
        log.debug("transformXDRToCheckPolicy - adding subject");
        request.getSubject().add(subject);

        String encodedPatientId = getPatientIdFromEvent(event);
        String assigningAuthorityId = PatientIdFormatUtil.parseCommunityId(encodedPatientId);
        String patId = PatientIdFormatUtil.parsePatientId(encodedPatientId);
        
        if (patId != null && assigningAuthorityId != null) {
            ResourceType resource = new ResourceType();
            AttributeHelper attrHelper = new AttributeHelper();
            resource.getAttribute().add(attrHelper.attributeFactory(PatientAssigningAuthorityAttributeId, Constants.DataTypeString, assigningAuthorityId));

            log.debug("transformXDRToCheckPolicy: sStrippedPatientId = " + patId);
            resource.getAttribute().add(attrHelper.attributeFactory(PatientIdAttributeId, Constants.DataTypeString, patId));

            request.getResource().add(resource);
        }

        log.debug("transformXDRToCheckPolicy - adding assertion data");
        AssertionHelper assertHelp = new AssertionHelper();
        assertHelp.appendAssertionDataToRequest(request, event.getMessage().getAssertion());

        request.setAction(ActionHelper.actionFactory(ActionInValue));

        checkPolicyRequest.setRequest(request);
        checkPolicyRequest.setAssertion(event.getMessage().getAssertion());
        log.debug("End -- XDRPolicyTransformHelper.transformXDRToCheckPolicy()");
        return checkPolicyRequest;
    }

    protected Log createLogger() {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }
    private String getPatientIdFromEvent(XDREventType event)
    {
        return getIdentifiersFromRequest(event.getMessage().getProvideAndRegisterDocumentSetRequest());
    }
    private String getIdentifiersFromRequest(ProvideAndRegisterDocumentSetRequestType request)
    {
        String result = "";

        if(request == null)
        {
            log.error(("Incoming ProvideAndRegisterDocumentSetRequestType was null"));
            return null;
        }

        if(request.getSubmitObjectsRequest() == null)

        {
            log.error(("Incoming ProvideAndRegisterDocumentSetRequestType metadata was null"));
            return null;
        }

        System.out.println(request.getSubmitObjectsRequest().getRegistryObjectList().getIdentifiable());
        RegistryObjectListType object = request.getSubmitObjectsRequest().getRegistryObjectList();

        for(int x= 0; x<object.getIdentifiable().size();x++)
        {
            System.out.println(object.getIdentifiable().get(x).getName());

            if(object.getIdentifiable().get(x).getDeclaredType().equals(RegistryPackageType.class))
            {
                RegistryPackageType registryPackage = (RegistryPackageType) object.getIdentifiable().get(x).getValue();

                System.out.println(registryPackage.getSlot().size());

                for(int y=0; y< registryPackage.getExternalIdentifier().size();y++)
                {
                    String test = registryPackage.getExternalIdentifier().get(y).getName().getLocalizedString().get(0).getValue();
                    if(test.equals("XDSSubmissionSet.patientId"))
                    {
                        result = registryPackage.getExternalIdentifier().get(y).getValue();
                    }


                }


            }
        }


        return result;
    }
}
