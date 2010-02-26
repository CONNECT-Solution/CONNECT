package gov.hhs.fha.nhinc.adapter.xdr.async.response.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;

/**
 * 
 * @author patlollav
 */
public interface AdapterXDRResponseProxy
{
    public ihe.iti.xdr._2007.AcknowledgementType provideAndRegisterDocumentSetBResponse(oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType body, AssertionType assertion);
}
