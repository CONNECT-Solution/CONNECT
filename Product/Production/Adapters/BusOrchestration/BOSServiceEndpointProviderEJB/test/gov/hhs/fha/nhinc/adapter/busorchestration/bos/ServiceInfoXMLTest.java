/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.adapter.busorchestration.bos;

import javax.xml.namespace.QName;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import org.netbeans.xml.schema.endpoint.EPR;
import org.xmlsoap.schemas.ws._2004._08.addressing.AttributedQName;
import org.xmlsoap.schemas.ws._2004._08.addressing.AttributedURI;
import org.xmlsoap.schemas.ws._2004._08.addressing.EndpointReferenceType;
import org.xmlsoap.schemas.ws._2004._08.addressing.ReferenceParametersType;
import org.xmlsoap.schemas.ws._2004._08.addressing.ReferencePropertiesType;
import org.xmlsoap.schemas.ws._2004._08.addressing.ServiceNameType;

/**
 *
 * @author Jerry Goodnough
 */
public class ServiceInfoXMLTest
{
    static String serializedXML;
    static ServiceInfo serviceInfo;

    public ServiceInfoXMLTest()
    {
    }

    @BeforeClass
    public static void setUpClass() throws Exception
    {
        serviceInfo = new ServiceInfo();
        addSimpleService(serviceInfo,ServiceNameConstants.DOCUMENT_ASSEMBLY,"http://localhost:8080/DocumentAssembly/DocumentAssembly?WSDL" );
        addSimpleService(serviceInfo,ServiceNameConstants.DOCUMENT_MANAGER,"http://localhost:8080/DocumentManager/DocumentManager?WSDL" );
        addSimpleService(serviceInfo,ServiceNameConstants.DYNAMIC_DOCUMENT_CACHE,"http://localhost:8080/DyanamicDocCache/DyanamicDocCache?WSDL" );
        addSimpleService(serviceInfo,ServiceNameConstants.NHIN_ENTERPRISE_DOCUMENT_QUERY,"http://localhost:8080/DocumentQuery/DocumentQuery?WSDL" );
        addSimpleService(serviceInfo,ServiceNameConstants.NHIN_ENTERPRISE_DOCUMENT_RETRIEVE,"http://localhost:8080/DocumentRetrieve/DocumentRetrieve?WSDL" );
        addSimpleService(serviceInfo,ServiceNameConstants.NHIN_ADAPTER_DOCUMENT_QUERY,"http://localhost:8080/DocumentQuery/DocumentQuery?WSDL","http://adapter:8080/DocumentQuery/DocumentQuery?WSDL" );
        addSimpleService(serviceInfo,ServiceNameConstants.NHIN_ADAPTER_DOCUMENT_RETRIEVE,"http://localhost:8080/DocumentQuery/DocumentQuery?WSDL","http://adapter:8080/DocumentRetrieve/DocumentRetrieve?WSDL" );
    }

    @AfterClass
    public static void tearDownClass() throws Exception
    {
    }

    @Before
    public void setUp()
    {
    }

    @After
    public void tearDown()
    {
    }

    /**
     * Test of serialize method, of class ServiceInfoXML.
     */
    @Test
    public void testSerialize() throws Exception
    {
        System.out.println("serialize");

        //Note: The URLs below are dummy data and are not the real endpoints
        serializedXML = ServiceInfoXML.serialize(serviceInfo);
        
        assertNotNull( serializedXML);

        System.out.print(serializedXML);
        
    }
    
    private static void addSimpleService(ServiceInfo oServiceInfo, String name, String srvURL)
    {
        ServiceMapping sm = new ServiceMapping();
        sm.setServiceName(name);

        EPR e1 = new EPR();
        EndpointReferenceType et1 = new EndpointReferenceType();

        AttributedURI au1 = new AttributedURI();
        au1.setValue(srvURL);
        et1.setAddress(au1);
        e1.setEndpointReference(et1);
        sm.setEndPoint(e1);

        oServiceInfo.m_ServiceList.add(sm);

    }
    private static void addSimpleService(ServiceInfo oServiceInfo, String name, String url, String srvURL)
    {
        ServiceMapping sm = new ServiceMapping();
        sm.setServiceName(name);
        sm.setServiceURL(url);

        EPR e1 = new EPR();
        EndpointReferenceType et1 = new EndpointReferenceType();

        AttributedURI au1 = new AttributedURI();
        au1.setValue(srvURL);
        et1.setAddress(au1);
        e1.setEndpointReference(et1);
        sm.setEndPoint(e1);
        oServiceInfo.m_ServiceList.add(sm);

    }
    /**
     * Test of deserialize method, of class ServiceInfoXML.
     */
    @Test
    public void testDeserialize() throws Exception
    {
        System.out.println("deserialize");
        String sXML = serializedXML;
        
        ServiceInfo result = ServiceInfoXML.deserialize(sXML);

        assertNotNull(result);
        assertTrue(result.m_ServiceList.size()==serviceInfo.m_ServiceList.size());
        assertEquals(result,serviceInfo);
    }

    @Test
    public void testSerializeFullEPR() throws Exception
    {
        System.out.println("serialize complex EPR");
        serviceInfo = new ServiceInfo();

        ServiceMapping sm = new ServiceMapping();
        sm.setServiceName(ServiceNameConstants.NHIN_ADAPTER_DOCUMENT_RETRIEVE);
        sm.setServiceURL("http://localhost:8080/DocumentQuery/DocumentQuery?WSDL");


        EPR e1 = new EPR();
        EndpointReferenceType et1 = new EndpointReferenceType();

        AttributedURI au1 = new AttributedURI();
        //Address
        au1.setValue("http://OtherHost:8080/DocumentQuery/DocumentQuery?WSDL");
        QName qn = new QName("Namespace","Local-Attribute1","Prefix");
        au1.getOtherAttributes().put(qn, "AttributeValue");
        qn = new QName("Namespace","Local-Attribute2","Prefix");
        au1.getOtherAttributes().put(qn, "AttributeValue2");
        et1.setAddress(au1);
        //Port
        AttributedQName aqn = new AttributedQName();
        aqn.setValue(new QName("PortNamespace","LocalportName","Prefix"));
        aqn.getOtherAttributes().put(new QName("Namespace","Local-Attribute3","Prefix"),"Value 3");
        aqn.getOtherAttributes().put(new QName("Namespace2","Local-Attribute4","Prefix"),"Value 4");
        et1.setPortType(aqn);

        //Reference Type
        ReferenceParametersType rt1 = new ReferenceParametersType();
        rt1.getAny().add("Test String1");
        rt1.getAny().add("Test String2");
        rt1.getAny().add(new QName("Namespace","Local-Attribute3","Prefix"));
        et1.setReferenceParameters(rt1);

        ReferencePropertiesType rpt = new ReferencePropertiesType();
        rpt.getAny().add("RefProperty 1");
        rpt.getAny().add("RefProperty 2");
        rpt.getAny().add(new QName("RefProperty 3"));
        et1.setReferenceProperties(rpt);

        //Servie Name Type
        ServiceNameType sn1 = new ServiceNameType();
        sn1.setPortName("aPortName");
        sn1.setValue(new QName("Local"));
        sn1.getOtherAttributes().put(new QName("Local2"),"Value3");
        sn1.getOtherAttributes().put(new QName("Local3"),"Value4");
        et1.setServiceName(sn1);

        e1.setEndpointReference(et1);
        sm.setEndPoint(e1);


        serviceInfo.m_ServiceList.add(sm);

         //Note: The URLs below are dummy data and are not the real endpoints
        serializedXML = ServiceInfoXML.serialize(serviceInfo);

        assertNotNull( serializedXML);

        System.out.print(serializedXML);

    }
}