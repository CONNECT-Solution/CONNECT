package gov.hhs.fha.nhinc.connectmgr.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class InternalConnectionInfoFileConverterTest {

	private final static String TEST_INPUT =
  "	<InternalConnectionInfos>" +
"\n		<internalConnectionInfo>" +
"\n			<homeCommunityId>1.1</homeCommunityId>" +
"\n			<name>CONNECTION</name>" +
"\n			<description>CONNECTION Description</description>" +
"\n			<states>" +
"\n				<state>" +
"\n					<name>US-NY</name>" +
"\n				</state>" +
"\n				<state>" +
"\n					<name>US-FL</name>" +
"\n				</state>" +
"\n			</states>" +
"\n			<services>" +
"\n				<service>" +
"\n					<name>QueryForDocument</name>" +
"\n					<description>documentquery</description>" +
"\n					<endpointURL>https://localhost:8181/GatewayDocumentQuery/1_0/NhinService/RespondingGateway_Query_Service/DocQuery</endpointURL>" +
"\n				</service>" +
"\n		            <service>" +
"\n	                <name>QueryForDocumentsDeferredRequest</name>" +
"\n	                <description>NHIN Doc Query Deferred Request</description>" +
"\n	                <endpointURL>https://localhost:8181/GatewayDocumentQuery/1_0/NhinService/DocQueryDeferredRequestService</endpointURL>" +
"\n	            </service>" +
"\n			</services>" +
"\n		</internalConnectionInfo>" +
"\n	</InternalConnectionInfos>";
	
	private final static String EXPECTED_TEST_OUTPUT = 
  "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
"\n<businessDetail xmlns=\"urn:uddi-org:api_v3\" xmlns:ns2=\"http://www.w3.org/2000/09/xmldsig#\">" +
"\n	<businessEntity businessKey=\"uddi:testnhincnode:1.1\">" +
"\n		<name xml:lang=\"en\">CONNECTION</name>" +
"\n		<businessServices>" +
"\n			<businessService serviceKey=\"uddi:testnhincnode:QueryForDocument\" businessKey=\"uddi:testnhieonenode:1.1\">" +
"\n				<name xml:lang=\"en\">QueryForDocument</name>" +
"\n				<bindingTemplates>" +
"\n					<bindingTemplate bindingKey=\"uddi:testnhincnode:QueryForDocument\" serviceKey=\"uddi:testnhincnode:QueryForDocument\">" +
"\n						<accessPoint useType=\"endPoint\">https://localhost:8181/GatewayDocumentQuery/1_0/NhinService/RespondingGateway_Query_Service/DocQuery</accessPoint>" +
"\n						<categoryBag>" +
"\n							<keyedReference tModelKey=\"uddi:nhin:versionofservice\" keyName=\"\" keyValue=\"1.0\"/>" +
"\n						</categoryBag>" +
"\n					</bindingTemplate>" +
"\n				</bindingTemplates>" +
"\n			</businessService>" +
"\n			<businessService serviceKey=\"uddi:testnhincnode:QueryForDocumentsDeferredRequest\" businessKey=\"uddi:testnhieonenode:1.1\">" +
"\n				<name xml:lang=\"en\">QueryForDocumentsDeferredRequest</name>" +
"\n				<bindingTemplates>" +
"\n					<bindingTemplate bindingKey=\"uddi:testnhincnode:QueryForDocumentsDeferredRequest\" serviceKey=\"uddi:testnhincnode:QueryForDocumentsDeferredRequest\">" +
"\n						<accessPoint useType=\"endPoint\">https://localhost:8181/GatewayDocumentQuery/1_0/NhinService/DocQueryDeferredRequestService</accessPoint>" +
"\n						<categoryBag>" +
"\n							<keyedReference tModelKey=\"uddi:nhin:versionofservice\" keyName=\"\" keyValue=\"1.0\"/>" +
"\n						</categoryBag>" +
"\n					</bindingTemplate>" +
"\n				</bindingTemplates>" +
"\n			</businessService>" +
"\n		</businessServices>" +
"\n		<identifierBag>" +
"\n			<keyedReference tModelKey=\"uddi:nhin:nhie:homecommunityid\" keyName=\"\" keyValue=\"1.1\"/>" +
"\n		</identifierBag>" +
"\n		<categoryBag>" +
"\n			<keyedReference tModelKey=\"uddi:uddi.org:ubr:categorization:iso3166\" keyName=\"New York\" keyValue=\"US-NY\"/>" +
"\n			<keyedReference tModelKey=\"uddi:uddi.org:ubr:categorization:iso3166\" keyName=\"Florida\" keyValue=\"US-FL\"/>" +
"\n		</categoryBag>" +
"\n	</businessEntity>" +
"\n</businessDetail>";			

	@Test
	public void testConvert() {
		InternalConnectionInfoFileConverter converter = new InternalConnectionInfoFileConverter();
		String result = converter.convert(TEST_INPUT);
		assertEquals(EXPECTED_TEST_OUTPUT, result);
	}
}
