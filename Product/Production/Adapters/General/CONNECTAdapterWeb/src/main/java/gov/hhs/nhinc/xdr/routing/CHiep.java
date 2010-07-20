/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.nhinc.xdr.routing;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;

/**
 *
 * @author dunnek
 */
public class CHiep implements XDRRouting{
    public RegistryResponseType provideAndRegisterDocumentSetB(ProvideAndRegisterDocumentSetRequestType request, AssertionType assertion)
    {
       return null;
    }

}
