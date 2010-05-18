/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.lift.utils;

import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectListType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryPackageType;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jhoppesc
 */
public class LiFTMessageHelperTest {

    public LiFTMessageHelperTest() {
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
     * Test of extractExtrinsicObject method, of class LiFTMessageHelper.
     */
    @Test
    public void testExtractExtrinsicObject() {
        System.out.println("extractExtrinsicObject");
        RegistryObjectListType registryList = createRequest("Document02");
        
        ExtrinsicObjectType result = LiFTMessageHelper.extractExtrinsicObject(registryList);

        assertNotNull(result);
    }

    /**
     * Test of extractExtrinsicObject method, of class LiFTMessageHelper.
     */
    @Test
    public void testExtractExtrinsicObjectNull() {
        System.out.println("testExtractExtrinsicObjectNull");

        ExtrinsicObjectType result = LiFTMessageHelper.extractExtrinsicObject(null);

        assertNull(result);
    }

    /**
     * Test of extractExtrinsicObject method, of class LiFTMessageHelper.
     */
    @Test
    public void testExtractExtrinsicObjectEmptyList() {
        System.out.println("testExtractExtrinsicObjectEmptyList");
        RegistryObjectListType registryList = new RegistryObjectListType();

        ExtrinsicObjectType result = LiFTMessageHelper.extractExtrinsicObject(registryList);

        assertNull(result);
    }

    /**
     * Test of extractExtrinsicObject method, of class LiFTMessageHelper.
     */
    @Test
    public void testExtractExtrinsicObjectWithOtherObjects() {
        System.out.println("testExtractExtrinsicObjectWithOtherObjects");
        RegistryObjectListType registryList = createRequestWithRegistryPackage();

        ExtrinsicObjectType result = LiFTMessageHelper.extractExtrinsicObject(registryList);

        assertNull(result);
    }

    private RegistryObjectListType createRequest (String docId) {
        // Build Registry Object List for response
        oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory rimObjFact = new oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory();
        RegistryObjectListType registryObjectList = new RegistryObjectListType();

        // Add first result - ExtrinsicObject
        ExtrinsicObjectType extObj = new ExtrinsicObjectType();

        extObj.setId(docId);
        extObj.setObjectType("urn:uuid:571a20d5-948c-4580-b863-514fdd63511d");
        extObj.setMimeType("text/xml");

        registryObjectList.getIdentifiable().add(rimObjFact.createExtrinsicObject(extObj));

        return registryObjectList;
    }

    private RegistryObjectListType createRequestWithRegistryPackage () {
        // Build Registry Object List for response
        oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory rimObjFact = new oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory();
        RegistryObjectListType registryObjectList = new RegistryObjectListType();

        registryObjectList.getIdentifiable().add(rimObjFact.createRegistryPackage(new RegistryPackageType()));

        return registryObjectList;
    }

}