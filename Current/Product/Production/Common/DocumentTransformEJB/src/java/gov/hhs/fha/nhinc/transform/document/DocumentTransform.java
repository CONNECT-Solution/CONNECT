package gov.hhs.fha.nhinc.transform.document;

import gov.hhs.fha.nhinc.nhinccomponentdocumenttransform.NhincComponentDocumentTransformPortType;
import javax.ejb.Stateless;
import javax.jws.WebService;

/**
 * Service to perform document transform operations.
 * 
 * @author Neil Webb
 */
@WebService(serviceName = "NhincComponentDocumentTransformService", portName = "NhincComponentDocumentTransformPortTypeBindingPort", endpointInterface = "gov.hhs.fha.nhinc.nhinccomponentdocumenttransform.NhincComponentDocumentTransformPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhinccomponentdocumenttransform", wsdlLocation = "META-INF/wsdl/DocumentTransform/NhincComponentDocumentTransform.wsdl")
@Stateless
public class DocumentTransform implements NhincComponentDocumentTransformPortType 
{
    public gov.hhs.fha.nhinc.common.nhinccommondocumenttransform.ReplaceAdhocQueryPatientIdResponseType replaceAdhocQueryPatientId(gov.hhs.fha.nhinc.common.nhinccommondocumenttransform.ReplaceAdhocQueryPatientIdRequestType replaceAdhocQueryPatientIdRequest)
    {
        return new DocumentTransformHelper().replaceAdhocQueryPatientId(replaceAdhocQueryPatientIdRequest);
    }
}
