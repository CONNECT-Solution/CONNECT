/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.direct.xdr;

import gov.hhs.fha.nhinc.direct.DirectBaseTest;
import gov.hhs.fha.nhinc.direct.DirectException;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType.Document;
import javax.xml.ws.WebServiceContext;
import oasis.names.tc.ebxml_regrep.xsd.lcm._3.SubmitObjectsRequest;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 * @author svalluripalli
 */
public class DirectXDRWebServiceImplTest extends DirectBaseTest {
    
    /**
     * Test of provideAndRegisterDocumentSet method, of class DirectXDRWebServiceImpl.
     */
    @Test(expected=DirectException.class)
    public void testProvideAndRegisterDocumentSet_Failure1() throws Exception {
        WebServiceContext mockWSContext = mock(WebServiceContext.class);
        DirectHeaderExtractor mockExtractor = mock(DirectHeaderExtractor.class);
        SoapEdgeContext mockSoap = mock(SoapEdgeContext.class);
        ProvideAndRegisterDocumentSetRequestType mockBody = mock(ProvideAndRegisterDocumentSetRequestType.class);
        DirectXDRWebServiceImpl instance = new DirectXDRWebServiceImpl();
        when(mockExtractor.getHeaderProperties(mockWSContext)).thenReturn(mockSoap);
        RegistryResponseType oResponse = instance.provideAndRegisterDocumentSet(mockBody, mockWSContext);
        assertNull(oResponse);
    }
    
    
    /**
     * Test of provideAndRegisterDocumentSet method, of class DirectXDRWebServiceImpl.
     */
    @Test(expected=DirectException.class)
    public void testProvideAndRegisterDocumentSet_Failure2() throws Exception {
        WebServiceContext mockWSContext = mock(WebServiceContext.class);
        DirectHeaderExtractor mockExtractor = mock(DirectHeaderExtractor.class);
        SoapEdgeContext mockSoap = mock(SoapEdgeContext.class);
        ProvideAndRegisterDocumentSetRequestType body = new ProvideAndRegisterDocumentSetRequestType();
        Document e = mock(Document.class);
        body.getDocument().add(e);
        SubmitObjectsRequest oSubmit = mock(SubmitObjectsRequest.class);
        body.setSubmitObjectsRequest(oSubmit);
        DirectXDRWebServiceImpl instance = new DirectXDRWebServiceImpl();
        when(mockExtractor.getHeaderProperties(mockWSContext)).thenReturn(mockSoap);
        RegistryResponseType oResponse = instance.provideAndRegisterDocumentSet(body, mockWSContext);
        assertNull(oResponse);
    }
}