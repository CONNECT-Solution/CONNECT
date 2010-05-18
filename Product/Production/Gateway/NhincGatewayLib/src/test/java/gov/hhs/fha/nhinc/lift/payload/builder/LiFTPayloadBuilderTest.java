/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.lift.payload.builder;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.UrlInfoType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType.Document;
import java.util.ArrayList;
import java.util.List;
import oasis.names.tc.ebxml_regrep.xsd.lcm._3.SubmitObjectsRequest;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectListType;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author JHOPPESC
 */
public class LiFTPayloadBuilderTest {

    public LiFTPayloadBuilderTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of buildLiFTPayload method, of class LiFTPayloadBuilder.
     */
    @Test
    public void testBuildLiFTPayload() {
        System.out.println("testBuildLiFTPayload");
        ProvideAndRegisterDocumentSetRequestType msg = createRequest("Document01");
        AssertionType assertion = new AssertionType();

        LiFTPayloadBuilder instance = new LiFTPayloadBuilder();

        List<UrlInfoType> urlInfoList = new ArrayList<UrlInfoType>();
        UrlInfoType urlInfo = new UrlInfoType();
        urlInfo.setId("Document01");
        urlInfo.setUrl("file://C:/Temp/document.pdf");
        urlInfoList.add(urlInfo);

        boolean result = instance.buildLiFTPayload(msg, assertion, urlInfoList);
        assertEquals(true, result);
    }

    /**
     * Test of buildLiFTPayload method, of class LiFTPayloadBuilder.
     */
    @Test
    public void testBuildLiFTPayloadWithDocument() {
        System.out.println("testBuildLiFTPayloadWithDocument");
        ProvideAndRegisterDocumentSetRequestType msg = createRequest("Document01");
        msg.getDocument().add(new Document());
        AssertionType assertion = new AssertionType();

        LiFTPayloadBuilder instance = new LiFTPayloadBuilder();

        List<UrlInfoType> urlInfoList = new ArrayList<UrlInfoType>();
        UrlInfoType urlInfo = new UrlInfoType();
        urlInfo.setId("Document01");
        urlInfo.setUrl("file://C:/Temp/document.pdf");
        urlInfoList.add(urlInfo);

        boolean result = instance.buildLiFTPayload(msg, assertion, urlInfoList);
        assertEquals(true, result);
    }

    /**
     * Test of buildLiFTPayload method, of class LiFTPayloadBuilder.
     */
    @Test
    public void testBuildLiFTPayloadInvalidParamMsg() {
        System.out.println("testBuildLiFTPayloadInvalidParamMsg");

        AssertionType assertion = new AssertionType();

        LiFTPayloadBuilder instance = new LiFTPayloadBuilder();

        List<UrlInfoType> urlInfoList = new ArrayList<UrlInfoType>();
        UrlInfoType urlInfo = new UrlInfoType();
        urlInfo.setId("Document01");
        urlInfo.setUrl("file://C:/Temp/document.pdf");
        urlInfoList.add(urlInfo);

        boolean result = instance.buildLiFTPayload(null, assertion, urlInfoList);
        assertEquals(false, result);
    }

    /**
     * Test of buildLiFTPayload method, of class LiFTPayloadBuilder.
     */
    @Test
    public void testBuildLiFTPayloadInvalidParamAssertion() {
        System.out.println("testBuildLiFTPayloadInvalidParamAssertion");
        ProvideAndRegisterDocumentSetRequestType msg = createRequest("Document01");

        LiFTPayloadBuilder instance = new LiFTPayloadBuilder();

        List<UrlInfoType> urlInfoList = new ArrayList<UrlInfoType>();
        UrlInfoType urlInfo = new UrlInfoType();
        urlInfo.setId("Document01");
        urlInfo.setUrl("file://C:/Temp/document.pdf");
        urlInfoList.add(urlInfo);

        boolean result = instance.buildLiFTPayload(msg, null, urlInfoList);
        assertEquals(false, result);
    }

    /**
     * Test of buildLiFTPayload method, of class LiFTPayloadBuilder.
     */
    @Test
    public void testBuildLiFTPayloadInvalidParamUrl() {
        System.out.println("testBuildLiFTPayloadInvalidParamUrl");
        ProvideAndRegisterDocumentSetRequestType msg = createRequest("Document01");
        AssertionType assertion = new AssertionType();

        LiFTPayloadBuilder instance = new LiFTPayloadBuilder();

        List<UrlInfoType> urlInfoList = new ArrayList<UrlInfoType>();
        UrlInfoType urlInfo = new UrlInfoType();
        urlInfo.setId("Document01");
        urlInfo.setUrl("file://C:/Temp/document.pdf");
        urlInfoList.add(urlInfo);

        boolean result = instance.buildLiFTPayload(msg, assertion, null);
        assertEquals(false, result);
    }

    /**
     * Test of buildLiFTPayload method, of class LiFTPayloadBuilder.
     */
    @Test
    public void testBuildLiFTPayloadWithNoSubmitObjectsRequest() {
        System.out.println("testBuildLiFTPayloadWithNoSubmitObjectsRequest");
        ProvideAndRegisterDocumentSetRequestType msg = new ProvideAndRegisterDocumentSetRequestType();
        msg.getDocument().add(new Document());
        AssertionType assertion = new AssertionType();

        LiFTPayloadBuilder instance = new LiFTPayloadBuilder();

        List<UrlInfoType> urlInfoList = new ArrayList<UrlInfoType>();
        UrlInfoType urlInfo = new UrlInfoType();
        urlInfo.setId("Document01");
        urlInfo.setUrl("file://C:/Temp/document.pdf");
        urlInfoList.add(urlInfo);

        boolean result = instance.buildLiFTPayload(msg, assertion, urlInfoList);
        assertEquals(false, result);
    }

    private ProvideAndRegisterDocumentSetRequestType createRequest (String docId) {
        ProvideAndRegisterDocumentSetRequestType message = new ProvideAndRegisterDocumentSetRequestType();

        // Build Registry Object List for response
        oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory rimObjFact = new oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory();
        RegistryObjectListType registryObjectList = new RegistryObjectListType();

        SubmitObjectsRequest subObjList = new SubmitObjectsRequest();
        subObjList.setRegistryObjectList(registryObjectList);
        message.setSubmitObjectsRequest(subObjList);

        // Add first result - ExtrinsicObject
        ExtrinsicObjectType extObj = new ExtrinsicObjectType();

        extObj.setId(docId);
        extObj.setObjectType("urn:uuid:571a20d5-948c-4580-b863-514fdd63511d");
        extObj.setMimeType("text/xml");

        registryObjectList.getIdentifiable().add(rimObjFact.createExtrinsicObject(extObj));

        return message;
    }

}