package gov.hhs.fha.nhinc.subscription.repository.data.test;

import org.junit.Test;
import static org.junit.Assert.*;
import gov.hhs.fha.nhinc.subscription.repository.data.ReferenceParameter;
import gov.hhs.fha.nhinc.subscription.repository.data.SubscriptionReference;
import org.junit.Ignore;

/**
 * Unit test for the SubscriptionReference class
 * 
 * @author webbn
 */
@Ignore
public class SubscriptionReferenceTest
{
    @Test
    public void testGettersAndSetters()
    {
        System.out.println("Begin testGettersAndSetters");
        try
        {
            String subMgrEndptAddr = "Submgredptaddr";
            String namespace = "namespace";
            String namespacePrefix = "prefix";
            String elementName = "elementName";
            String value = "elementValue";

            ReferenceParameter refParam = new ReferenceParameter();
            refParam.setNamespace(namespace);
            refParam.setNamespacePrefix(namespacePrefix);
            refParam.setElementName(elementName);
            refParam.setValue(value);

            SubscriptionReference ref = new SubscriptionReference();
            ref.setSubscriptionManagerEndpointAddress(subMgrEndptAddr);
            ref.addReferenceParameter(refParam);

            assertEquals("Endpoint address", subMgrEndptAddr, ref.getSubscriptionManagerEndpointAddress());
            assertNotNull("ReferenceParameters null", ref.getReferenceParameters());
            assertEquals("ReferenceParameters empty", 1, ref.getReferenceParameters().size());
            ReferenceParameter retRefParam = ref.getReferenceParameters().get(0);
            assertNotNull("Retrieved reference param was null", retRefParam);
            assertEquals("Ref param - namespace", namespace, retRefParam.getNamespace());
            assertEquals("Ref param - namespace prefix", namespacePrefix, retRefParam.getNamespacePrefix());
            assertEquals("Ref param - element name", elementName, retRefParam.getElementName());
            assertEquals("Ref param - value", value, retRefParam.getValue());
        }
        catch (Throwable t)
        {
            t.printStackTrace();
            fail(t.getMessage());
        }
        System.out.println("End testGettersAndSetters");
    }

    @Test
    public void testEquals()
    {
        System.out.println("Begin testEquals");
        try
        {
            // Equals - full
            ReferenceParameter refParam = new ReferenceParameter();
            refParam.setNamespace("namespace1");
            refParam.setNamespacePrefix("namespacePrefix1");
            refParam.setElementName("elementName1");
            refParam.setValue("value1");
            SubscriptionReference ref1 = new SubscriptionReference();
            ref1.setSubscriptionManagerEndpointAddress("subMgrEndptAddr1");
            ref1.addReferenceParameter(refParam);
            refParam = new ReferenceParameter();
            refParam.setNamespace("namespace1");
            refParam.setNamespacePrefix("namespacePrefix1");
            refParam.setElementName("elementName1");
            refParam.setValue("value1");
            SubscriptionReference ref2 = new SubscriptionReference();
            ref2.setSubscriptionManagerEndpointAddress("subMgrEndptAddr1");
            ref2.addReferenceParameter(refParam);
            assertTrue("Equals - full", ref1.equals(ref2));

            // Equals - endpoint address only
            ref1 = new SubscriptionReference();
            ref1.setSubscriptionManagerEndpointAddress("subMgrEndptAddr1");
            ref2 = new SubscriptionReference();
            ref2.setSubscriptionManagerEndpointAddress("subMgrEndptAddr1");
            assertTrue("Equals - endpoint address only", ref1.equals(ref2));

            // Equals - ref param only
            refParam = new ReferenceParameter();
            refParam.setNamespace("namespace1");
            refParam.setNamespacePrefix("namespacePrefix1");
            refParam.setElementName("elementName1");
            refParam.setValue("value1");
            ref1 = new SubscriptionReference();
            ref1.addReferenceParameter(refParam);
            refParam = new ReferenceParameter();
            refParam.setNamespace("namespace1");
            refParam.setNamespacePrefix("namespacePrefix1");
            refParam.setElementName("elementName1");
            refParam.setValue("value1");
            ref2 = new SubscriptionReference();
            ref2.addReferenceParameter(refParam);
            assertTrue("Equals - ref param only", ref1.equals(ref2));

            // Not equal - full, endpoint address different
            refParam = new ReferenceParameter();
            refParam.setNamespace("namespace1");
            refParam.setNamespacePrefix("namespacePrefix1");
            refParam.setElementName("elementName1");
            refParam.setValue("value1");
            ref1 = new SubscriptionReference();
            ref1.setSubscriptionManagerEndpointAddress("subMgrEndptAddr1");
            ref1.addReferenceParameter(refParam);
            refParam = new ReferenceParameter();
            refParam.setNamespace("namespace1");
            refParam.setNamespacePrefix("namespacePrefix1");
            refParam.setElementName("elementName1");
            refParam.setValue("value1");
            ref2 = new SubscriptionReference();
            ref2.setSubscriptionManagerEndpointAddress("subMgrEndptAddr2");
            ref2.addReferenceParameter(refParam);
            assertFalse("Not equal - full, endpoint address different", ref1.equals(ref2));

            // Not equal - full, ref param different
            refParam = new ReferenceParameter();
            refParam.setNamespace("namespace1");
            refParam.setNamespacePrefix("namespacePrefix1");
            refParam.setElementName("elementName1");
            refParam.setValue("value1");
            ref1 = new SubscriptionReference();
            ref1.setSubscriptionManagerEndpointAddress("subMgrEndptAddr1");
            ref1.addReferenceParameter(refParam);
            refParam = new ReferenceParameter();
            refParam.setNamespace("namespace1");
            refParam.setNamespacePrefix("namespacePrefix1");
            refParam.setElementName("elementName1");
            refParam.setValue("value2");
            ref2 = new SubscriptionReference();
            ref2.setSubscriptionManagerEndpointAddress("subMgrEndptAddr1");
            ref2.addReferenceParameter(refParam);
            assertFalse("Not equal - full, ref param different", ref1.equals(ref2));

            // Not equal - 1 full, 2 missing endpoint address
            refParam = new ReferenceParameter();
            refParam.setNamespace("namespace1");
            refParam.setNamespacePrefix("namespacePrefix1");
            refParam.setElementName("elementName1");
            refParam.setValue("value1");
            ref1 = new SubscriptionReference();
            ref1.setSubscriptionManagerEndpointAddress("subMgrEndptAddr1");
            ref1.addReferenceParameter(refParam);
            refParam = new ReferenceParameter();
            refParam.setNamespace("namespace1");
            refParam.setNamespacePrefix("namespacePrefix1");
            refParam.setElementName("elementName1");
            refParam.setValue("value1");
            ref2 = new SubscriptionReference();
            ref2.addReferenceParameter(refParam);
            assertFalse("Not equal - 1 full, 2 missing endpoint address", ref1.equals(ref2));

            // Not equal - 1 full, 2 missing ref param
            refParam = new ReferenceParameter();
            refParam.setNamespace("namespace1");
            refParam.setNamespacePrefix("namespacePrefix1");
            refParam.setElementName("elementName1");
            refParam.setValue("value1");
            ref1 = new SubscriptionReference();
            ref1.setSubscriptionManagerEndpointAddress("subMgrEndptAddr1");
            ref1.addReferenceParameter(refParam);
            ref2 = new SubscriptionReference();
            ref2.setSubscriptionManagerEndpointAddress("subMgrEndptAddr1");
            assertFalse("Not equal - 1 full, 2 missing ref param", ref1.equals(ref2));

            // Not equal - 2 full, 1 missing endpoint address
            refParam = new ReferenceParameter();
            refParam.setNamespace("namespace1");
            refParam.setNamespacePrefix("namespacePrefix1");
            refParam.setElementName("elementName1");
            refParam.setValue("value1");
            ref1 = new SubscriptionReference();
            ref1.addReferenceParameter(refParam);
            refParam = new ReferenceParameter();
            refParam.setNamespace("namespace1");
            refParam.setNamespacePrefix("namespacePrefix1");
            refParam.setElementName("elementName1");
            refParam.setValue("value1");
            ref2 = new SubscriptionReference();
            ref2.setSubscriptionManagerEndpointAddress("subMgrEndptAddr1");
            ref2.addReferenceParameter(refParam);
            assertFalse("Not equal - 2 full, 1 missing endpoint address", ref1.equals(ref2));

            // Not equal - 2 full, 1 missing ref param
            ref1 = new SubscriptionReference();
            ref1.setSubscriptionManagerEndpointAddress("subMgrEndptAddr1");
            refParam = new ReferenceParameter();
            refParam.setNamespace("namespace1");
            refParam.setNamespacePrefix("namespacePrefix1");
            refParam.setElementName("elementName1");
            refParam.setValue("value1");
            ref2 = new SubscriptionReference();
            ref2.setSubscriptionManagerEndpointAddress("subMgrEndptAddr1");
            ref2.addReferenceParameter(refParam);
            assertFalse("Not equal - 2 full, 1 missing ref param", ref1.equals(ref2));

        }
        catch (Throwable t)
        {
            t.printStackTrace();
            fail(t.getMessage());
        }
        System.out.println("End testEquals");
    }
}