package gov.hhs.fha.nhinc.connectmgr.util;

import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.connectmgr.data.CMInternalConnInfoService;
import gov.hhs.fha.nhinc.connectmgr.data.CMInternalConnectionInfo;
import gov.hhs.fha.nhinc.connectmgr.data.CMInternalConnectionInfoState;
import gov.hhs.fha.nhinc.connectmgr.data.CMInternalConnectionInfos;
import gov.hhs.fha.nhinc.connectmgr.data.CMInternalConnectionInfosXML;
import gov.hhs.fha.nhinc.connectmgr.persistance.dao.InternalConnectionInfoDAOFileImpl;
import gov.hhs.fha.nhinc.util.StringUtil;
import gov.hhs.fha.nhinc.util.UtilException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

public class InternalConnectionInfoFileConverter {
	
	private static Map<String, String> states = null;
	
	static {
		states = new HashMap<String, String>();
		states.put("CA-AB", "Alberta");
		states.put("CA-BC", "British Columbia");
		states.put("CA-MB", "Manitoba");
		states.put("CA-NB", "New Brunswick");
		states.put("CA-NL", "Newfoundland");
		states.put("CA-NS", "Nova Scotia");
		states.put("CA-NU", "Nunavut");
		states.put("CA-ON", "Ontario");
		states.put("CA-PE", "Prince Edward Island");
		states.put("CA-QC", "Quebec");
		states.put("CA-SK", "Saskatchewan");
		states.put("CA-NT", "Northwest Territories");
		states.put("CA-YT", "Yukon Territory");
		states.put("US-AA", "Armed Forces Americas");
		states.put("US-AE", "Armed Forces Europe- Middle East- & Canada");
		states.put("US-AK", "Alaska");
		states.put("US-AL", "Alabama");
		states.put("US-AP", "Armed Forces Pacific");
		states.put("US-AR", "Arkansas");
		states.put("US-AS", "American Samoa");
		states.put("US-AZ", "Arizona");
		states.put("US-CA", "California");
		states.put("US-CO", "Colorado");
		states.put("US-CT", "Connecticut");
		states.put("US-DC", "District of Columbia");
		states.put("US-DE", "Delaware");
		states.put("US-FL", "Florida");
		states.put("US-FM", "Federated States of Micronesia");
		states.put("US-GA", "Georgia");
		states.put("US-GU", "Guam");
		states.put("US-HI", "Hawaii");
		states.put("US-IA", "Iowa");
		states.put("US-ID", "Idaho");
		states.put("US-IL", "Illinois");
		states.put("US-IN", "Indiana");
		states.put("US-KS", "Kansas");
		states.put("US-KY", "Kentucky");
		states.put("US-LA", "Louisiana");
		states.put("US-MA", "Massachusetts");
		states.put("US-MD", "Maryland");
		states.put("US-ME", "Maine");
		states.put("US-MH", "Marshall Islands");
		states.put("US-MI", "Michigan");
		states.put("US-MN", "Minnesota");
		states.put("US-MO", "Missouri");
		states.put("US-MP", "Northern Mariana Islands");
		states.put("US-MS", "Mississippi");
		states.put("US-MT", "Montana");
		states.put("US-NC", "North Carolina");
		states.put("US-ND", "North Dakota");
		states.put("US-NE", "Nebraska");
		states.put("US-NH", "New Hampshire");
		states.put("US-NJ", "New Jersey");
		states.put("US-NM", "New Mexico");
		states.put("US-NV", "Nevada");
		states.put("US-NY", "New York");
		states.put("US-OH", "Ohio");
		states.put("US-OK", "Oklahoma");
		states.put("US-OR", "Oregon");
		states.put("US-PA", "Pennsylvania");
		states.put("US-PR", "Puerto Rico");
		states.put("US-PW", "Palau");
		states.put("US-RI", "Rhode Island");
		states.put("US-SC", "South Carolina");
		states.put("US-SD", "South Dakota");
		states.put("US-TN", "Tennessee");
		states.put("US-TX", "Texas");
		states.put("US-UT", "Utah");
		states.put("US-VA", "Virginia");
		states.put("US-VI", "Virgin Islands");
		states.put("US-VT", "Vermont");
		states.put("US-WA", "Washington");
		states.put("US-WV", "West Virginia");
		states.put("US-WI", "Wisconsin");
		states.put("US-WY", "Wyoming");
	}
	
	private static String stateCodeToName(String code) {
		return states.get(code);
	}

	private final static String CONNECTION_FORMAT = 
  "	<businessEntity businessKey=\"uddi:testnhincnode:%1$s\">"+
"\n		<name xml:lang=\"en\">%2$s</name>"+
"\n		<businessServices>"+
"\n%3$s"+
"\n		</businessServices>"+
"\n		<identifierBag>"+
"\n			<keyedReference tModelKey=\"uddi:nhin:nhie:homecommunityid\" keyName=\"\" keyValue=\"%1$s\"/>"+
"\n		</identifierBag>"+
"\n%4$s"+
"\n	</businessEntity>";

	private final static String SERVICE_FORMAT =
  "			<businessService serviceKey=\"uddi:testnhincnode:%1$s\" businessKey=\"uddi:testnhieonenode:%2$s\">"+
"\n				<name xml:lang=\"en\">%3$s</name>"+
"\n				<bindingTemplates>"+
"\n					<bindingTemplate bindingKey=\"uddi:testnhincnode:%5$s\" serviceKey=\"uddi:testnhincnode:%1$s\">"+
"\n						<accessPoint useType=\"endPoint\">%4$s</accessPoint>"+
"\n						<categoryBag>"+
"\n							<keyedReference tModelKey=\"uddi:nhin:versionofservice\" keyName=\"\" keyValue=\"%6$s\"/>"+
"\n						</categoryBag>"+
"\n					</bindingTemplate>"+
"\n				</bindingTemplates>"+
"\n			</businessService>";

	private final static String STATE_FORMAT =       
"			<keyedReference tModelKey=\"uddi:uddi.org:ubr:categorization:iso3166\" keyName=\"%1$s\" keyValue=\"%2$s\"/>";


	private String generateConnectionInfo(CMInternalConnectionInfo connectionInfo) {
		String servicesOutput = "";
		for (CMInternalConnInfoService service : connectionInfo.getServices().getService()) {
			if (servicesOutput.length() != 0) {
				servicesOutput += '\n';
			}
			servicesOutput += generateService(service, connectionInfo.getHomeCommunityId());
		}
		String statesOutput = "";
		if (connectionInfo.getStates() != null){
			statesOutput += "		<categoryBag>";
			for (CMInternalConnectionInfoState state : connectionInfo.getStates().getState()) {
				if (statesOutput.length() != 0) {
					statesOutput += "\n";
				}
				statesOutput += generateState(state);
			}
			statesOutput += "\n		</categoryBag>";
		}
		return String.format(CONNECTION_FORMAT, connectionInfo.getHomeCommunityId(), connectionInfo.getName(), servicesOutput, statesOutput);
	}
	
	
	private String generateService(CMInternalConnInfoService service, String hcid) {
		String result = String.format(SERVICE_FORMAT, service.getName(), hcid, service.getName(), service.getEndpointURL(), service.getName(), "1.0");
		return result;		
	}

	private String generateState(CMInternalConnectionInfoState state) {
		String result = String.format(STATE_FORMAT, stateCodeToName(state.getName()), state.getName());
		return result;
	}

	public String convert(String inputXML) {
		ByteArrayOutputStream s = new ByteArrayOutputStream();
		PrintStream out = new PrintStream(s);
		convert(inputXML, out);
		String res = new String(s.toByteArray());
		return res;
	}

	public void convert(File file, PrintStream out) throws ConnectionManagerException, UtilException {
		String inputXmlContent = StringUtil.readTextFile(file.getPath());
		convert(inputXmlContent, out);
	}
		
	public void convert(String inputXmlContent, PrintStream out) {
		CMInternalConnectionInfos internalConnectionInfos = CMInternalConnectionInfosXML.deserialize(inputXmlContent);
		out.print("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n");
		out.print("<businessDetail xmlns=\"urn:uddi-org:api_v3\" xmlns:ns2=\"http://www.w3.org/2000/09/xmldsig#\">\n");
		for (CMInternalConnectionInfo internalConnectionInfo : internalConnectionInfos.getInternalConnectionInfo()) {
			String str = generateConnectionInfo(internalConnectionInfo);
			out.print(str + "\n");
			out.flush();
		}
		out.print("</businessDetail>");
	}
	
	public void convert(File inFile, File outFile) throws IOException {
		FileOutputStream fos = new FileOutputStream(outFile);
		PrintStream ps = null;
		try {
			ps = new PrintStream(fos);
			convert(inFile, ps);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} finally {
			ps.close();
		}
		
	}

	public static void main(String[] args) {
		if (args.length == 0) {
			System.out.println("Usage:");
			System.out.println("\tInternalConnectionInfoFileConverter [fromFile] [toFile]");
			return;
		}
		InternalConnectionInfoFileConverter instance = new InternalConnectionInfoFileConverter();
		String inFileName = args[0];
		String outFileName = args[1];
		System.out.println("Converting from: " + inFileName);
		System.out.println("Converting   to: " + outFileName);
		try {
			instance.convert(new File(inFileName), new File(outFileName));
			//InternalConnectionInfoDAOFileImpl.getInstance().loadBusinessDetail();
			System.out.println("Done.");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
