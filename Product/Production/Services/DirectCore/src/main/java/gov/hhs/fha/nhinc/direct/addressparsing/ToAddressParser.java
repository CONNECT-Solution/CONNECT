package gov.hhs.fha.nhinc.direct.addressparsing;

import javax.mail.Address;

import org.nhindirect.xd.common.DirectDocuments;

public interface ToAddressParser {

	Address[] parse(String addresses, DirectDocuments documents);

}
