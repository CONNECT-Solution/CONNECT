package gov.hhs.fha.nhinc.adapters.general.adapterpolicyenginetransform.adapterpolicyengine;

import javax.jws.WebService;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.TransformXACMLRequestToCppAQRResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.TransformXACMLRequestToCppAQRRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.TransformAQRToCppRDSRResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.TransformAQRToCppRDSRRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPatientOptInResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPatientOptInRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.TransformXACMLRequestToAQRForPatientIdResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.TransformXACMLRequestToAQRForPatientIdRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.TransformPatientIdAQRToCppXACMLResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.TransformPatientIdAQRToCppXACMLRequestType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "AdapterPolicyEngineTransform", portName = "AdapterPolicyEngineTransformPortSoap11", endpointInterface = "gov.hhs.fha.nhinc.adapterpolicyenginetransform.AdapterPolicyEngineTransformPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterpolicyenginetransform", wsdlLocation = "WEB-INF/wsdl/AdapterPolicyEngineTransform/AdapterPolicyEngineTransform.wsdl")
public class AdapterPolicyEngineTransform {

    private static Log log = LogFactory.getLog(AdapterPolicyEngineTransform.class);

    /**
     * This message transforms a XACML request message to an AdhocQueryRequest
     * message that can be used to find the CPP (Consumer Preferences Profile)
     * document for the patient.
     *
     * @param transformXACMLRequestToCppAQRRequest The XACML request to be
     *        transformed.
     *
     * @return The AdhocQueryRequest to get the CPP document.
     */
    public TransformXACMLRequestToCppAQRResponseType transformXACMLRequestToCppAQR(TransformXACMLRequestToCppAQRRequestType transformXACMLRequestToCppAQRRequest)
    {
        TransformXACMLRequestToCppAQRResponseType oResponse = new TransformXACMLRequestToCppAQRResponseType();

        try
        {
            oResponse = AdapterPolicyEngineTransformHelper.transformXACMLRequestToCppAQR(transformXACMLRequestToCppAQRRequest);
        }
        catch (Exception e)
        {
            String sErrorMessage = "Failed to transform XACML request to a CPP AdhocQueryRequest.  Error: " +
                                   e.getMessage();
            log.error(sErrorMessage);
        }

        return oResponse;
    }

    /**
     * This method transforms a message from a CPP AdhocQueryResponse message to
     * a RetrieveDocumentSetRequest message that can be used to retrieve the
     * CPP document.
     *
     * @param transformAQRToCppRDSRRequest The AdhocQueryResponse message that
     *        was returned form the Document Query to get the CPP document.
     * @return The RetrieveDocumentSetRequest that can be used to retrieve
     *         the CPP document.
     */
    public TransformAQRToCppRDSRResponseType transformAQRToCppRDSR(TransformAQRToCppRDSRRequestType transformAQRToCppRDSRRequest)
    {
        TransformAQRToCppRDSRResponseType oResponse = new TransformAQRToCppRDSRResponseType();

        try
        {
            oResponse = AdapterPolicyEngineTransformHelper.transformAQRToCppRDSR(transformAQRToCppRDSRRequest);
        }
        catch (Exception e)
        {
            String sErrorMessage = "Failed to transform CPP AdhocQueryResponse to a CPP RetrieveDocumentSetRequest.  Error: " +
                                   e.getMessage();
            log.error(sErrorMessage);
        }

        return oResponse;
    }

    /**
     * This method takes the RetrieveDocumentSetResponse message from the
     * document repository that contains the CPP document and looks at the
     * CPP document and returns TRUE if the patient has opted in and false if
     * they have not.  If the document does not exist or the input parameter
     * is null, then it means that the patient has opted out and false is
     * returned.
     *
     * @param checkPatientOptInRequest The RetrieveDocumentSetResponse containing
     *        the CPP document.
     * @return TRUE if the patient has opted in and false if they have not.
     */
    public CheckPatientOptInResponseType checkPatientOptIn(CheckPatientOptInRequestType checkPatientOptInRequest)
    {
        CheckPatientOptInResponseType oResponse = new CheckPatientOptInResponseType();

        try
        {
            oResponse = AdapterPolicyEngineTransformHelper.checkPatientOptIn(checkPatientOptInRequest);
        }
        catch (Exception e)
        {
            String sErrorMessage = "Failed to transform CPP RetrieveDocumentSetResponse to a valid patient opt in value.  Error: " +
                                   e.getMessage();
            log.error(sErrorMessage);
        }

        return oResponse;
    }

    /**
     * This method transforms a XACML request to the information needed to
     * retrienve the patient ID from the Document Registry based on the
     * document information.   This will return an AdhocQueryRequest.
     *
     * @param transformXACMLRequestToAQRForPatientIdRequest The XACML information
     *        that contains the document identifiers.
     * @return The AdhocQueryRequest to retrieve the patient ID
     *         information based on the document identifiers.
     */
    public TransformXACMLRequestToAQRForPatientIdResponseType transformXACMLRequestToAQRForPatientId(TransformXACMLRequestToAQRForPatientIdRequestType transformXACMLRequestToAQRForPatientIdRequest)
    {
        TransformXACMLRequestToAQRForPatientIdResponseType oResponse = new TransformXACMLRequestToAQRForPatientIdResponseType();

        try
        {
            oResponse = AdapterPolicyEngineTransformHelper.transformXACMLRequestToAQRForPatientId(transformXACMLRequestToAQRForPatientIdRequest);
        }
        catch (Exception e)
        {
            String sErrorMessage = "Failed to transform XACML Request to a valid AdhocQueryRequest to retrive the meta data for the document.  Error: " +
                                   e.getMessage();
            log.error(sErrorMessage);
        }

        return oResponse;
    }

    /**
     * This method takes the AdhocQueryResponse containing the document
     * meta data including the Patient ID and it will create a XACML request
     * containing the patient ID so that it can be passed into the
     * CheckPolicyPatientOptIn method to determine if the patient has opted in.
     *
     * @param transformPatientIdAQRToCppXACMLRequest The AdhocQueryResponse
     *        containing the patient ID for the patient associated with the document.
     * @return The XACML policy containing the patient ID.
     *
     */
    public TransformPatientIdAQRToCppXACMLResponseType transformPatientIdAQRToCppXACML(TransformPatientIdAQRToCppXACMLRequestType transformPatientIdAQRToCppXACMLRequest)
    {
        TransformPatientIdAQRToCppXACMLResponseType oResponse = new TransformPatientIdAQRToCppXACMLResponseType();

        try
        {
            oResponse = AdapterPolicyEngineTransformHelper.transformPatientIdAQRToCppXACML(transformPatientIdAQRToCppXACMLRequest);
        }
        catch (Exception e)
        {
            String sErrorMessage = "Failed to transform AdhocQueryRequest to a valid XACML Request object containing the patient ID.  Error: " +
                                   e.getMessage();
            log.error(sErrorMessage);
        }

        return oResponse;
    }

}
