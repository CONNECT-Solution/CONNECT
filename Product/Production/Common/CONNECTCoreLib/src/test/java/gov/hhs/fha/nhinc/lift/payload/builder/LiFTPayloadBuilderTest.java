/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.lift.payload.builder;

import gov.hhs.healthit.nhin.LIFTMessageType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.UrlInfoType;
import gov.hhs.fha.nhinc.transform.marshallers.JAXBContextHandler;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType.Document;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
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

        LiFTPayloadBuilder instance = new LiFTPayloadBuilder() {
            @Override
            protected String addEntryToLiftDatabase(String fileUrl) {
                return "123456";
            }

            @Override
            protected String getProxyAddressProperty() {
                return "localhost";
            }

            @Override
            protected int getProxyAddressPort() {
                return 1037;
            }
        };

        List<UrlInfoType> urlInfoList = new ArrayList<UrlInfoType>();
        UrlInfoType urlInfo = new UrlInfoType();
        urlInfo.setId("Document01");
        urlInfo.setUrl("file://C:/Temp/document.pdf");
        urlInfoList.add(urlInfo);

        String result = instance.buildLiFTPayload(msg, assertion, urlInfoList);
        assertNotNull(result);
        assertNotNull(msg);
        assertNotNull(msg.getDocument());
        assertEquals(1, msg.getDocument().size());
        assertNotNull(msg.getDocument().get(0));
        unmarshallDocument(msg.getDocument().get(0));
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

        LiFTPayloadBuilder instance = new LiFTPayloadBuilder() {
            @Override
            protected String addEntryToLiftDatabase(String fileUrl) {
                return "123456";
            }

            @Override
            protected String getProxyAddressProperty() {
                return "localhost";
            }

            @Override
            protected int getProxyAddressPort() {
                return 1037;
            }
        };

        List<UrlInfoType> urlInfoList = new ArrayList<UrlInfoType>();
        UrlInfoType urlInfo = new UrlInfoType();
        urlInfo.setId("Document01");
        urlInfo.setUrl("file://C:/Temp/document.pdf");
        urlInfoList.add(urlInfo);

        String result = instance.buildLiFTPayload(msg, assertion, urlInfoList);
        assertNotNull(result);
    }

    /**
     * Test of buildLiFTPayload method, of class LiFTPayloadBuilder.
     */
    @Test
    public void testBuildLiFTPayloadInvalidParamMsg() {
        System.out.println("testBuildLiFTPayloadInvalidParamMsg");

        AssertionType assertion = new AssertionType();

        LiFTPayloadBuilder instance = new LiFTPayloadBuilder() {
            @Override
            protected String getProxyAddressProperty() {
                return "localhost";
            }

            @Override
            protected int getProxyAddressPort() {
                return 1037;
            }
        };

        List<UrlInfoType> urlInfoList = new ArrayList<UrlInfoType>();
        UrlInfoType urlInfo = new UrlInfoType();
        urlInfo.setId("Document01");
        urlInfo.setUrl("file:///C:/Temp/document.pdf");
        urlInfoList.add(urlInfo);

        String result = instance.buildLiFTPayload(null, assertion, urlInfoList);
        assertNull(result);
    }

    /**
     * Test of buildLiFTPayload method, of class LiFTPayloadBuilder.
     */
    @Test
    public void testBuildLiFTPayloadInvalidParamAssertion() {
        System.out.println("testBuildLiFTPayloadInvalidParamAssertion");
        ProvideAndRegisterDocumentSetRequestType msg = createRequest("Document01");

        LiFTPayloadBuilder instance = new LiFTPayloadBuilder() {
            @Override
            protected String getProxyAddressProperty() {
                return "localhost";
            }

            @Override
            protected int getProxyAddressPort() {
                return 1037;
            }
        };

        List<UrlInfoType> urlInfoList = new ArrayList<UrlInfoType>();
        UrlInfoType urlInfo = new UrlInfoType();
        urlInfo.setId("Document01");
        urlInfo.setUrl("file:///C:/Temp/document.pdf");
        urlInfoList.add(urlInfo);

        String result = instance.buildLiFTPayload(msg, null, urlInfoList);
        assertNull(result);
    }

    /**
     * Test of buildLiFTPayload method, of class LiFTPayloadBuilder.
     */
    @Test
    public void testBuildLiFTPayloadInvalidParamUrlInfo() {
        System.out.println("testBuildLiFTPayloadInvalidParamUrlInfo");
        ProvideAndRegisterDocumentSetRequestType msg = createRequest("Document01");
        AssertionType assertion = new AssertionType();

        LiFTPayloadBuilder instance = new LiFTPayloadBuilder() {
            @Override
            protected String getProxyAddressProperty() {
                return "localhost";
            }

            @Override
            protected int getProxyAddressPort() {
                return 1037;
            }
        };

        List<UrlInfoType> urlInfoList = new ArrayList<UrlInfoType>();
        UrlInfoType urlInfo = new UrlInfoType();
        urlInfo.setId("Document01");
        urlInfo.setUrl("file://C:/Temp/document.pdf");
        urlInfoList.add(urlInfo);

        String result = instance.buildLiFTPayload(msg, assertion, null);
        assertNull(result);
    }

    /**
     * Test of buildLiFTPayload method, of class LiFTPayloadBuilder.
     */
    @Test
    public void testBuildLiFTPayloadInvalidParamUrlInfoUrl() {
        System.out.println("testBuildLiFTPayloadInvalidParamUrlInfoUrl");
        ProvideAndRegisterDocumentSetRequestType msg = createRequest("Document01");
        AssertionType assertion = new AssertionType();

        LiFTPayloadBuilder instance = new LiFTPayloadBuilder() {
            @Override
            protected String getProxyAddressProperty() {
                return "localhost";
            }

            @Override
            protected int getProxyAddressPort() {
                return 1037;
            }
        };

        List<UrlInfoType> urlInfoList = new ArrayList<UrlInfoType>();
        UrlInfoType urlInfo = new UrlInfoType();
        urlInfo.setId("Document01");
        urlInfo.setUrl(null);
        urlInfoList.add(urlInfo);

        String result = instance.buildLiFTPayload(msg, assertion, urlInfoList);
       assertNull(result);
    }

    /**
     * Test of buildLiFTPayload method, of class LiFTPayloadBuilder.
     */
    @Test
    public void testBuildLiFTPayloadInvalidParamUrlInfoId() {
        System.out.println("testBuildLiFTPayloadInvalidParamUrlInfoUrl");
        ProvideAndRegisterDocumentSetRequestType msg = createRequest("Document01");
        AssertionType assertion = new AssertionType();

        LiFTPayloadBuilder instance = new LiFTPayloadBuilder() {
            @Override
            protected String getProxyAddressProperty() {
                return "localhost";
            }

            @Override
            protected int getProxyAddressPort() {
                return 1037;
            }
        };

        List<UrlInfoType> urlInfoList = new ArrayList<UrlInfoType>();
        UrlInfoType urlInfo = new UrlInfoType();
        urlInfo.setId(null);
        urlInfo.setUrl("file://C:/Temp/document.pdf");
        urlInfoList.add(urlInfo);

        String result = instance.buildLiFTPayload(msg, assertion, urlInfoList);
        assertNull(result);
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

        LiFTPayloadBuilder instance = new LiFTPayloadBuilder() {
            @Override
            protected String getProxyAddressProperty() {
                return "localhost";
            }

            @Override
            protected int getProxyAddressPort() {
                return 1037;
            }
        };

        List<UrlInfoType> urlInfoList = new ArrayList<UrlInfoType>();
        UrlInfoType urlInfo = new UrlInfoType();
        urlInfo.setId("Document01");
        urlInfo.setUrl("file://C:/Temp/document.pdf");
        urlInfoList.add(urlInfo);

        String result = instance.buildLiFTPayload(msg, assertion, urlInfoList);
        assertNull(result);
    }

    /**
     * Test of buildLiFTPayload method, of class LiFTPayloadBuilder.
     */
    @Test
    public void testBuildLiFTPayloadInvAddrProp() {
        System.out.println("testBuildLiFTPayload");
        ProvideAndRegisterDocumentSetRequestType msg = createRequest("Document01");
        AssertionType assertion = new AssertionType();

        LiFTPayloadBuilder instance = new LiFTPayloadBuilder() {
            @Override
            protected String addEntryToLiftDatabase(String fileUrl) {
                return "123456";
            }

            @Override
            protected String getProxyAddressProperty() {
                return null;
            }

            @Override
            protected int getProxyAddressPort() {
                return 1037;
            }
        };

        List<UrlInfoType> urlInfoList = new ArrayList<UrlInfoType>();
        UrlInfoType urlInfo = new UrlInfoType();
        urlInfo.setId("Document01");
        urlInfo.setUrl("file://C:/Temp/document.pdf");
        urlInfoList.add(urlInfo);

        String result = instance.buildLiFTPayload(msg, assertion, urlInfoList);
        assertNotNull(result);
    }

    /**
     * Test of buildLiFTPayload method, of class LiFTPayloadBuilder.
     */
    @Test
    public void testBuildLiFTPayloadInvPortProp() {
        System.out.println("testBuildLiFTPayloadInvPortProp");
        ProvideAndRegisterDocumentSetRequestType msg = createRequest("Document01");
        AssertionType assertion = new AssertionType();

        LiFTPayloadBuilder instance = new LiFTPayloadBuilder() {
            @Override
            protected String addEntryToLiftDatabase(String fileUrl) {
                return "123456";
            }
            
            @Override
            protected String getProxyAddressProperty() {
                return "localhost";
            }

            @Override
            protected int getProxyAddressPort() {
                return -1;
            }
        };

        List<UrlInfoType> urlInfoList = new ArrayList<UrlInfoType>();
        UrlInfoType urlInfo = new UrlInfoType();
        urlInfo.setId("Document01");
        urlInfo.setUrl("file://C:/Temp/document.pdf");
        urlInfoList.add(urlInfo);

        String result = instance.buildLiFTPayload(msg, assertion, urlInfoList);
        assertNotNull(result);
    }

    /**
     * Test of extractLiftPayload method, of class LiFTPayloadBuilder.
     */
    @Test
    public void testExtractLiftPayload() {
        System.out.println("testExtractLiftPayload");
        ProvideAndRegisterDocumentSetRequestType msg = createRequest("Document01");
        AssertionType assertion = new AssertionType();

        LiFTPayloadBuilder instance = new LiFTPayloadBuilder() {
            @Override
            protected String addEntryToLiftDatabase(String fileUrl) {
                return "123456";
            }

            @Override
            protected String getProxyAddressProperty() {
                return "localhost";
            }

            @Override
            protected int getProxyAddressPort() {
                return 1037;
            }
        };

        List<UrlInfoType> urlInfoList = new ArrayList<UrlInfoType>();
        UrlInfoType urlInfo = new UrlInfoType();
        urlInfo.setId("Document01");
        urlInfo.setUrl("file://C:/Temp/document.pdf");
        urlInfoList.add(urlInfo);

        String guid = instance.buildLiFTPayload(msg, assertion, urlInfoList);

        assertNotNull(msg);
        assertNotNull(msg.getDocument());
        assertNotNull(msg.getDocument().get(0));

        LIFTMessageType result = instance.extractLiftPayload(msg.getDocument().get(0));

        assertNotNull(result);
        assertNotNull(result.getDataElement());
        assertNotNull(result.getDataElement().getClientData());
        assertNotNull(result.getDataElement().getClientData().getClientData());
        assertNotNull(result.getRequestElement());
        assertNotNull(result.getRequestElement().getRequestGuid());
        assertEquals(guid, result.getRequestElement().getRequestGuid());
        assertNotNull(result.getDataElement().getServerProxyData());
        assertNotNull(result.getDataElement().getServerProxyData().getServerProxyAddress());
    }

    /**
     * Test of extractLiftPayload method, of class LiFTPayloadBuilder.
     */
    @Test
    public void testExtractLiftPayloadNullDoc() {
        System.out.println("testExtractLiftPayloadNullDoc");

        LiFTPayloadBuilder instance = new LiFTPayloadBuilder();

        LIFTMessageType result = instance.extractLiftPayload(null);

        assertNull(result);
    }

    /**
     * Test of extractLiftPayload method, of class LiFTPayloadBuilder.
     */
    @Test
    public void testExtractLiftPayloadEmptyDoc() {
        System.out.println("testExtractLiftPayloadEmptyDoc");

        LiFTPayloadBuilder instance = new LiFTPayloadBuilder();

        LIFTMessageType result = instance.extractLiftPayload(new Document());

        assertNull(result);
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

    private void unmarshallDocument (Document doc) {
        byte[] data = doc.getValue();
        LIFTMessageType liftMsg = new LIFTMessageType();
        ByteArrayInputStream baInStrm = new ByteArrayInputStream(data);

        try {
            if (data != null && data.length > 0) {
                //InputStream in = new InputStream(data);
                JAXBContextHandler oHandler = new JAXBContextHandler();
                JAXBContext jc = oHandler.getJAXBContext("gov.hhs.healthit.nhin");
                Unmarshaller unmarshaller = jc.createUnmarshaller();
                JAXBElement jaxEle = (JAXBElement) unmarshaller.unmarshal(baInStrm);
                liftMsg = (LIFTMessageType) jaxEle.getValue();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println();
    }

}