/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.hiem.consumerreference;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author rayj
 */
public class ReferenceParametersHelperTest {

    public ReferenceParametersHelperTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void createReferenceParameterElementsFromSubscriptionReference_SingleParameter() throws Exception {
        String subscriptionReference = "" +
                "<wsnt:SubscriptionReference xmlns:wsnt='http://docs.oasis-open.org/wsn/b-2'> " +
                "	<Address xmlns='http://www.w3.org/2005/08/addressing'>http://localhost:18080/mockAdapterSubscriptionManager</Address> " +
                "	<wsa:ReferenceParameters xmlns:wsa='http://www.w3.org/2005/08/addressing'> " +
                "		<adapter:myId xmlns:adapter='http://www.hhs.gov/healthit/nhin'>ID-123</adapter:myId> " +
                "	</wsa:ReferenceParameters> " +
                "</wsnt:SubscriptionReference>";

        ReferenceParametersHelper helper = new ReferenceParametersHelper();
        ReferenceParametersElements referenceParameterElements = helper.createReferenceParameterElementsFromSubscriptionReference(subscriptionReference);
        assertNotNull(referenceParameterElements);
        assertEquals(1, referenceParameterElements.getElements().size());
    }
    @Test
    public void createReferenceParameterElementsFromSubscriptionReference_TwoSameParameters() throws Exception {
        String subscriptionReference = "" +
                "<wsnt:SubscriptionReference xmlns:wsnt='http://docs.oasis-open.org/wsn/b-2'> " +
                "	<Address xmlns='http://www.w3.org/2005/08/addressing'>http://localhost:18080/mockAdapterSubscriptionManager</Address> " +
                "	<wsa:ReferenceParameters xmlns:wsa='http://www.w3.org/2005/08/addressing'> " +
                "		<adapter:myId xmlns:adapter='http://www.hhs.gov/healthit/nhin'>ID-123</adapter:myId> " +
                "		<adapter:myId xmlns:adapter='http://www.hhs.gov/healthit/nhin'>ID-124</adapter:myId> " +
                "	</wsa:ReferenceParameters> " +
                "</wsnt:SubscriptionReference>";

        ReferenceParametersHelper helper = new ReferenceParametersHelper();
        ReferenceParametersElements referenceParameterElements = helper.createReferenceParameterElementsFromSubscriptionReference(subscriptionReference);
        assertNotNull(referenceParameterElements);
        assertEquals(2, referenceParameterElements.getElements().size());
    }
    @Test
    public void createReferenceParameterElementsFromSubscriptionReference_TwoParameters() throws Exception {
        String subscriptionReference = "" +
                "<wsnt:SubscriptionReference xmlns:wsnt='http://docs.oasis-open.org/wsn/b-2'> " +
                "	<Address xmlns='http://www.w3.org/2005/08/addressing'>http://localhost:18080/mockAdapterSubscriptionManager</Address> " +
                "	<wsa:ReferenceParameters xmlns:wsa='http://www.w3.org/2005/08/addressing'> " +
                "		<adapter:myId xmlns:adapter='http://www.hhs.gov/healthit/nhin'>ID-123</adapter:myId> " +
                "		<adapter:myId2 xmlns:adapter='http://www.hhs.gov/healthit/nhin'>ID-124</adapter:myId2> " +
                "	</wsa:ReferenceParameters> " +
                "</wsnt:SubscriptionReference>";

        ReferenceParametersHelper helper = new ReferenceParametersHelper();
        ReferenceParametersElements referenceParameterElements = helper.createReferenceParameterElementsFromSubscriptionReference(subscriptionReference);
        assertNotNull(referenceParameterElements);
        assertEquals(2, referenceParameterElements.getElements().size());
    }
    @Test
    public void createReferenceParameterElementsFromSubscriptionReference_NoParameters() throws Exception {
        String subscriptionReference = "" +
                "<wsnt:SubscriptionReference xmlns:wsnt='http://docs.oasis-open.org/wsn/b-2'> " +
                "	<Address xmlns='http://www.w3.org/2005/08/addressing'>http://localhost:18080/mockAdapterSubscriptionManager</Address> " +
                "	<wsa:ReferenceParameters xmlns:wsa='http://www.w3.org/2005/08/addressing'> " +
                "	</wsa:ReferenceParameters> " +
                "</wsnt:SubscriptionReference>";

        ReferenceParametersHelper helper = new ReferenceParametersHelper();
        ReferenceParametersElements referenceParameterElements = helper.createReferenceParameterElementsFromSubscriptionReference(subscriptionReference);
        assertNotNull(referenceParameterElements);
        assertEquals(0, referenceParameterElements.getElements().size());
    }
    @Test
    public void createReferenceParameterElementsFromSubscriptionReference_NoReferenceParameters() throws Exception {
        String subscriptionReference = "" +
                "<wsnt:SubscriptionReference xmlns:wsnt='http://docs.oasis-open.org/wsn/b-2'> " +
                "	<Address xmlns='http://www.w3.org/2005/08/addressing'>http://localhost:18080/mockAdapterSubscriptionManager</Address> " +
                "</wsnt:SubscriptionReference>";

        ReferenceParametersHelper helper = new ReferenceParametersHelper();
        ReferenceParametersElements referenceParameterElements = helper.createReferenceParameterElementsFromSubscriptionReference(subscriptionReference);
        assertNotNull(referenceParameterElements);
        assertEquals(0, referenceParameterElements.getElements().size());
    }
}