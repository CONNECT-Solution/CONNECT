/**
 * 
 */
package gov.hhs.fha.nhinc.callback.openSAML;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;

import org.junit.Test;
import org.opensaml.Configuration;
import org.opensaml.saml2.core.Attribute;
import org.opensaml.saml2.core.NameID;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.Marshaller;
import org.opensaml.xml.io.MarshallerFactory;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.schema.XSAny;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * @author bhumphrey
 * 
 */
public class OpenSAMLComponentBuilderTest {

	@Test
	public void testBootstraping() {
		OpenSAML2ComponentBuilder builder = OpenSAML2ComponentBuilder
				.getInstance();
		assertNotNull(builder);
	}

	private Element marshall(XMLObject object) throws MarshallingException {
		// Get the marshaller factory
		MarshallerFactory marshallerFactory = Configuration
				.getMarshallerFactory();

		// Get the Subject marshaller
		Marshaller marshaller = marshallerFactory.getMarshaller(object);

		// Marshall the Subject
		Element element = marshaller.marshall(object);
		return element;

	}

	@Test
	public void testCreateNameID() throws MarshallingException {
		OpenSAML2ComponentBuilder builder = OpenSAML2ComponentBuilder
				.getInstance();
		NameID nameid = builder.createNameID(null,
				"urn:oasis:names:tc:SAML:2.0:nameid-format:transient",
				"3f7b3dcf-1674-4ecd-92c8-1544f346baf8");

		Element nameIdElement = marshall(nameid);
		assertEquals("urn:oasis:names:tc:SAML:2.0:nameid-format:transient",
				nameIdElement.getAttribute("Format"));
		assertEquals("3f7b3dcf-1674-4ecd-92c8-1544f346baf8", nameIdElement
				.getFirstChild().getNodeValue());
		assertEquals("saml2:NameID", nameIdElement.getNodeName());
		assertTrue(nameIdElement.getChildNodes().getLength() == 1);

	}

	@Test 
	public void testSimpleAttribute() throws MarshallingException {
		OpenSAML2ComponentBuilder builder = OpenSAML2ComponentBuilder
				.getInstance();
		Attribute attribute = builder.createAttribute("friendlyName", "name", "nameFormat");
		Element attributeElement = marshall(attribute);
		assertEquals("saml2:Attribute", attributeElement.getNodeName());
		assertEquals("friendlyName", attributeElement.getAttribute("FriendlyName"));
		assertEquals("nameFormat", attributeElement.getAttribute("NameFormat"));

		assertTrue(attributeElement.getChildNodes().getLength() == 0);
	}
	@Test
	public void testAttributeWithValues() throws MarshallingException {
		OpenSAML2ComponentBuilder builder = OpenSAML2ComponentBuilder
				.getInstance();
		Attribute attribute = builder.createAttribute("eduPersonAffiliation",
				"urn:oid:1.3.6.1.4.1.5923.1.1.1.1",
				"urn:oasis:names:tc:SAML:2.0:attrname-format:uri",
				Arrays.asList("member", "staff"));
		Element attributeElement = marshall(attribute);

		assertTrue(attributeElement.getChildNodes().getLength() == 2);

		NodeList childNodes = attributeElement.getChildNodes();
		assertEquals("saml2:AttributeValue", childNodes.item(0).getNodeName());
		assertEquals("xs:string",
				((Element) childNodes.item(0)).getAttribute("xsi:type"));
		assertEquals("saml2:AttributeValue", childNodes.item(1).getNodeName());
		assertEquals("xs:string",
				((Element) childNodes.item(1)).getAttribute("xsi:type"));

	}

	@Test
	public void testComplex() throws MarshallingException {
		Map<QName, String> attributeMap = new HashMap<QName, String>();

		attributeMap.put(new QName("code"), "defaultUserRoleCode");
		attributeMap.put(new QName("codeSystem"), "defaultUserRoleCodeSystem");
		attributeMap.put(new QName("codeSystemName"),
				"defaultUserRoleCodeSystemName");
		attributeMap.put(new QName("displayName"),
				"defaultUserRoleCodeDisplayName");
		attributeMap.put(new QName("http://www.w3.org/2001/XMLSchema-instance",
				"type"), "hl7:CE");

		

		XSAny any = OpenSAML2ComponentBuilder.getInstance()
				.createAttributeValue("urn:hl7-org:v3", "Role", "hl7",
						attributeMap);
		any.getUnknownAttributes().put(
				new QName("http://www.w3.org/2001/XMLSchema-instance",
						"xsi:type"), "hl7:CE");

		assertNotNull(any);

		Attribute attribute = OpenSAML2ComponentBuilder.getInstance()
				.createAttribute(null,
						"urn:oasis:names:tc:xacml:2.0:subject:role", null,
						Arrays.asList(any));

		Element attributeElement = marshall(attribute);

		assertEquals("urn:oasis:names:tc:xacml:2.0:subject:role", attributeElement.getAttribute("Name")
				);
		
		
		Element anyElement = (Element) ((Element) attributeElement
				.getFirstChild()).getFirstChild();
		assertEquals("hl7:Role", anyElement.getNodeName());
		assertEquals("defaultUserRoleCode", anyElement.getAttribute("code"));

	}


}
