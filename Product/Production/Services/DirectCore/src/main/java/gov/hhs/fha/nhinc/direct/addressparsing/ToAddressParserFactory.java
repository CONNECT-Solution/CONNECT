package gov.hhs.fha.nhinc.direct.addressparsing;

public class ToAddressParserFactory {

	public ToAddressParser getToParser() {
		return new SmtpOnlyToAddresParser();
	}
}
