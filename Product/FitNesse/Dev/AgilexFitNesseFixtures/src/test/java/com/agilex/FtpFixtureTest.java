package com.agilex;

import org.junit.* ;
import static org.junit.Assert.* ;

public class FtpFixtureTest {

	@Test
	public void shouldConnect() throws Exception {
		FtpFixture testSubject = new FtpFixture();
		
		String server = "sftp.agilexhealth.com";
		int port = 990;
		String userName = "agilexsftp";
		String password = "41H4Jonu";
		String protocol = "TSL";
		String keyStorePassword = "password";
		String keyStoreFilePath = "C:\\Projects\\triserv-esb\\trunk\\product\\FitNesse\\Run\\resources\\keystore.jks";
		
		testSubject.connect(server, port, userName, password, protocol, keyStorePassword, keyStoreFilePath);
	}
}
