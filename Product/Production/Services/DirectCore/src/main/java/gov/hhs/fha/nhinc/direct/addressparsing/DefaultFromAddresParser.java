package gov.hhs.fha.nhinc.direct.addressparsing;

import gov.hhs.fha.nhinc.direct.DirectException;

import java.net.URI;
import java.net.URISyntaxException;

import javax.mail.Address;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.nhindirect.xd.common.DirectDocuments;
import org.nhindirect.xd.transform.parse.ParserHL7;

public class DefaultFromAddresParser implements FromAddressParser {
	private static final Logger LOG = Logger
			.getLogger(DefaultFromAddresParser.class);
	
	@Override
	public Address parse(String addresses, DirectDocuments documents) {
		Address address = null;
		String replyEmail = null;
		
		// Get a reply address (first check direct:from header, then
		// go to authorPerson)
		if (StringUtils.isNotBlank(addresses))
			try {
				replyEmail = (new URI(addresses)).getSchemeSpecificPart();
			} catch (URISyntaxException e) {
				LOG.error(
						"Unable to parse Direct From header, attempting to parse XDR author telecom.",
						e);
			}
		else {
			replyEmail = documents.getSubmissionSet()
					.getAuthorTelecommunication();
			replyEmail = ParserHL7.parseXTN(replyEmail);
		}
		
		try {
			address = new InternetAddress(replyEmail);
		} catch (AddressException e) {
			String errorMesssage = "Unable to create InternetAddress from direct from address.";
			LOG.error(errorMesssage, e);
			throw new DirectException(errorMesssage, e);
		}
		
		return address;
	}

}
