package gov.hhs.fha.nhinc.direct.addressparsing;

public class FromAddressParserFactory {
	
	public FromAddressParser getFromParser() {
		return new DefaultFromAddresParser();
	}
}
