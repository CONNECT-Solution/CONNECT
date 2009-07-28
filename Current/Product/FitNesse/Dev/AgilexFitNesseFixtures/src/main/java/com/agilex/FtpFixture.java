package com.agilex;

import java.util.*;
import java.io.*;
import java.net.*;
import java.util.Vector;

import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
 
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
 
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.*;

import fit.Fixture;
import fit.Parse;
import fitlibrary.SequenceFixture;
import fitlibrary.table.Row;
import fitlibrary.traverse.workflow.SequenceTraverse;
import fitlibrary.utility.TestResults;

public class FtpFixture extends SequenceFixture {

	public Row currentRow;
	public TestResults currentTestResults;
	private SequenceTraverse sequenceTraverse;
	private FTPClient ftpClient;

	public FtpFixture() {
		sequenceTraverse = new SequenceTraverse(this) {
			@Override
			public Object interpretRow(Row row, TestResults testResults,
					Fixture fixtureByName) {
				((FtpFixture) this.getSystemUnderTest()).currentRow = row;
				((FtpFixture) this.getSystemUnderTest()).currentTestResults = testResults;
				return super.interpretRow(row, testResults, fixtureByName);
			}
		};
		setTraverse(sequenceTraverse);
	}

	public FtpFixture(Object sut) {
		this();
		setSystemUnderTest(sut);
	}

	public void connect(String server, int port, String userName, String password, String protocol, String keyStorePassword, String keyStoreFilePath) throws Exception{
		FTPSClient ftps = new FTPSClient(protocol, false);
		ftps.setRemoteVerificationEnabled(false);
		KeyManager[] keyManagers = getKeyManagers(keyStorePassword, keyStoreFilePath);
		TrustManager[] trustManagers = getTrustManagers(keyStorePassword, keyStoreFilePath);
		SSLContext sslContext = getSSLContext(trustManagers);
		FTPSSocketFactory sf = new FTPSSocketFactory(sslContext);
		ftps.setSocketFactory(sf);
		ftps.setBufferSize(1000);
		ftps.setControlEncoding("UTF-8");

		ftps.setKeyManager(keyManagers[0]);
		ftps.setTrustManager(trustManagers[0]);
					
		ftps.addProtocolCommandListener(new PrintCommandListener(
				new PrintWriter(System.out)));

		ftpClient = ftps;
		
		InetAddress address = InetAddress.getByName(server);
		ftpClient.connect(address, port);
		ftpClient.login(userName, password);
	}
	
	private static SSLContext getSSLContext(TrustManager[] trustManagers) throws KeyManagementException, KeyStoreException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, UnrecoverableKeyException, IOException {
        System.out.println("Init SSL Context");
        SSLContext sslContext = SSLContext.getInstance("SSLv3");
		sslContext.init(null, trustManagers, null);
 
		return sslContext;
	}
	private static KeyManager[] getKeyManagers(String keyStorePassword, String keyStoreFilePath) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException, UnrecoverableKeyException {
		KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream(keyStoreFilePath), keyStorePassword.toCharArray());
 
		KeyManagerFactory tmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        tmf.init(ks, keyStorePassword.toCharArray());
 
		return tmf.getKeyManagers();
	}
	private static TrustManager[] getTrustManagers(String keyStorePassword, String keyStoreFilePath) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException, UnrecoverableKeyException {
		KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream(keyStoreFilePath), keyStorePassword.toCharArray());
 
		TrustManagerFactory tmf = TrustManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        tmf.init(ks);
 
		return tmf.getTrustManagers();
	}
	
	public void connect(String server, String userName, String password)
			throws SocketException, IOException {
		ftpClient = new FTPClient();
		InetAddress address = InetAddress.getByName(server);
		ftpClient.connect(address, FTP.DEFAULT_PORT);
		ftpClient.login(userName, password);
	}

	public void setFileType(String type) throws IOException {
		if (type.toLowerCase().equals("ascii")) {
			ftpClient.setFileType(FTP.ASCII_FILE_TYPE);
		} else if (type.toLowerCase().equals("binary")) {
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
		} else {
			throw new RuntimeException(
					String
							.format(
									"The type: '%1$s' is not supported.  Please choose ascii or binary.",
									type));
		}
	}

	public void setPassive(boolean value) {
		if (value)
			ftpClient.enterLocalPassiveMode();
		else
			ftpClient.enterLocalActiveMode();
	}
	
	public boolean directoryExists(String remoteDirectoryPath) throws IOException{
		if (!ftpClient.changeWorkingDirectory(remoteDirectoryPath)) {
			return false;
		}
		return true;
	}
	
	public boolean fileExists(String remoteFilePath) throws IOException{
		File file = new File(remoteFilePath);
		if (!directoryExists(file.getParent())) {
			return false;
		}
		String[] names = ftpClient.listNames(remoteFilePath);
		if (names != null && names.length > 0)
			return true;
		return false;
	}
	
	public void deleteDirectory(String remoteDirectoryPath) throws IOException{
		ftpClient.changeWorkingDirectory("/");
		ftpClient.removeDirectory(remoteDirectoryPath);
		boolean success = FTPReply.isPositiveCompletion(ftpClient
				.getReplyCode());

		if (!success) {
			throw new RuntimeException("Could not delete directory: "
					+ ftpClient.getReplyString());
		}
	}
	
	public void deleteFile(String remoteFilePath) throws IOException{
		ftpClient.deleteFile(remoteFilePath);
		boolean success = FTPReply.isPositiveCompletion(ftpClient
				.getReplyCode());

		if (!success) {
			throw new RuntimeException("Could not delete file: "
					+ ftpClient.getReplyString());
		}
	}
	
	public void get(String localFilePath, String remoteFilePath) throws IOException{
		OutputStream outstream = null;
		try{
			File file = new File(localFilePath);
			(new File(file.getParent())).mkdirs();
			outstream = new BufferedOutputStream(new FileOutputStream(file));

			ftpClient.retrieveFile(remoteFilePath, outstream);
			
			boolean success = FTPReply.isPositiveCompletion(ftpClient
					.getReplyCode());

			if (!success) {
				throw new RuntimeException("Could not get file: "
						+ ftpClient.getReplyString());
			}
		}finally {
			if (outstream != null){
				try{
					outstream.close();
				}catch (IOException ex) {
					// ignore it
				}
			}
		}
	}

	public void put(String localFilePath, String remoteFilePath)
			throws IOException {
		InputStream instream = null;
		try {
			File file = new File(localFilePath);
			instream = new BufferedInputStream(new FileInputStream(file));

			createDirectory((new File(remoteFilePath)).getParent());

			ftpClient.storeFile(remoteFilePath, instream);

			boolean success = FTPReply.isPositiveCompletion(ftpClient
					.getReplyCode());

			if (!success) {
				throw new RuntimeException("Could not put file: "
						+ ftpClient.getReplyString());
			}
		} finally {
			if (instream != null) {
				try {
					instream.close();
				} catch (IOException ex) {
					// ignore it
				}
			}
		}
	}

	private Vector dirCache = new Vector();

	public void createDirectory(String remoteDirectoryPath)
			throws IOException {

		File dir = new File(remoteDirectoryPath);
		if (dirCache.contains(dir)) {
			return;
		}

		Vector directories = new Vector();
		directories.addElement(dir);
		
		String dirname;
		while ((dirname = dir.getParent()) != null) {
			File checkDir = new File(dirname);
			if (dirCache.contains(checkDir)) {
				break;
			}
			dir = checkDir;
			directories.addElement(dir);
		}

		// find first non cached dir
		int i = directories.size() - 1;

		if (i >= 0) {
			String cwd = ftpClient.printWorkingDirectory();
			String parent = dir.getParent();
			if (parent != null) {
				if (!ftpClient.changeWorkingDirectory(resolveFile(parent))) {
					throw new RuntimeException("could not change to "
							+ "directory: " + ftpClient.getReplyString());
				}
			}

			while (i >= 0) {
				dir = (File) directories.elementAt(i--);
				// check if dir exists by trying to change into it.
				if (!ftpClient.changeWorkingDirectory(dir.getName())) {
					// could not change to it - try to create it
					if (!ftpClient.makeDirectory(dir.getName())) {
						handleMkDirFailure();
					}
					if (!ftpClient.changeWorkingDirectory(dir.getName())) {
						throw new RuntimeException("could not change to "
								+ "directory: " + ftpClient.getReplyString());
					}
				}
				dirCache.addElement(dir);
			}
			ftpClient.changeWorkingDirectory(cwd);
		}
	}

	private static final int CODE_521 = 521;
    private boolean ignoreNoncriticalErrors = true;
	public void setIgnoreNoncriticalErrors(boolean ignoreNoncriticalErrors) {
        this.ignoreNoncriticalErrors = ignoreNoncriticalErrors;
    }
	private void handleMkDirFailure() {
		int rc = ftpClient.getReplyCode();
		if (!(ignoreNoncriticalErrors && (rc == FTPReply.CODE_550
				|| rc == FTPReply.CODE_553 || rc == CODE_521))) {
			throw new RuntimeException("could not create directory: "
					+ ftpClient.getReplyString());
		}
	}

	private String remoteFileSep = "/";

	public void setSeparator(String separator) {
		remoteFileSep = separator;
	}

	protected String resolveFile(String file) {
		return file.replace(System.getProperty("file.separator").charAt(0),
				remoteFileSep.charAt(0));
	}
}
