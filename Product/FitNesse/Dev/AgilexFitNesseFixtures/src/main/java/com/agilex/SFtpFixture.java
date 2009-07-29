package com.agilex;

import java.io.File;
import java.util.Vector;

import com.jcraft.jsch.*;

import fit.Fixture;
import fit.Parse;
import fitlibrary.SequenceFixture;
import fitlibrary.table.Row;
import fitlibrary.traverse.workflow.SequenceTraverse;
import fitlibrary.utility.TestResults;

public class SFtpFixture extends SequenceFixture implements UserInfo {

	public Row currentRow;
	public TestResults currentTestResults;
	private SequenceTraverse sequenceTraverse;

	public SFtpFixture() {
		sequenceTraverse = new SequenceTraverse(this) {
			@Override
			public Object interpretRow(Row row, TestResults testResults,
					Fixture fixtureByName) {
				((SFtpFixture) this.getSystemUnderTest()).currentRow = row;
				((SFtpFixture) this.getSystemUnderTest()).currentTestResults = testResults;
				return super.interpretRow(row, testResults, fixtureByName);
			}
		};
		setTraverse(sequenceTraverse);
	}

	public SFtpFixture(Object sut) {
		this();
		setSystemUnderTest(sut);
	}
	
	private Session _Session;
	private Session getSession(){
		return this._Session;
	}
	private void setSession(Session value) {
		this._Session = value;
	}
	
	private ChannelSftp _SFtpClient;
	private ChannelSftp getSFtpClient() {
		return this._SFtpClient;
	}
	private void setSFtpClient(ChannelSftp value) {
		this._SFtpClient = value;
	}
	
	public void connect(String host, String userName, String password, String passphrase) throws Exception{
		this.setHost(host);
		this.setUser(userName);
		this.setPassword(password);
		this.setPassphrase(passphrase);
		this.connect();
	}
	
	public void connect() throws Exception{
		JSch jsch=new JSch();
		
		this.setSession(jsch.getSession(this.getUser(), this.getHost(), this.getPort()));
		this.getSession().setUserInfo(this);
		this.getSession().connect();

		this.setSFtpClient((ChannelSftp)this.getSession().openChannel("sftp"));
		this.getSFtpClient().connect();
	}
	
	public void disconnect() {
		this.getSFtpClient().disconnect();
		this.getSession().disconnect();
	}
	
	public boolean directoryExists(String remoteDirectoryPath) {
		try{
			this.getSFtpClient().ls(remoteDirectoryPath);
			return true;
		}catch (SftpException ex){
			return false;
		}
	}
	
	public boolean fileExists(String remoteFilePath) throws SftpException{
		File file = new File(remoteFilePath);
		if (!directoryExists(file.getParent())) {
			return false;
		}
		Vector names = this.getSFtpClient().ls(remoteFilePath);
		if (names != null && names.size() > 0)
			return true;
		return false;
	}
	
	public void deleteDirectory(String remoteDirectoryPath) throws SftpException{
		this.getSFtpClient().rmdir(remoteDirectoryPath);
	}
	
	public void deleteFile(String remoteFilePath) throws SftpException{
		this.getSFtpClient().rm(remoteFilePath);
	}
	
	public void get(String localPath, String remotePath) throws SftpException{
		this.getSFtpClient().get(localPath, remotePath);
	}
	
	public void put(String localPath, String remotePath) throws SftpException{
		this.getSFtpClient().put(localPath, remotePath);
	}
	
	public void createDirectory(String remoteDirectoryPath) throws SftpException{
		this.getSFtpClient().mkdir(remoteDirectoryPath);
	}

	private String _User;
	
	public String getUser() {
		return this._User;
	}
	
	public void setUser(String value) {
		this._User = value;
	}
	
	private String _Host;
	
	public String getHost() {
		return this._Host;
	}
	
	public void setHost(String value) {
		this._Host = value;
	}
	
	private int _Port;
	
	public int getPort() {
		return this._Port;
	}
	
	public void setPort(int value) {
		this._Port = value;
	}
	
	private String _Passphrase;

	@Override
	public String getPassphrase() {
		return this._Passphrase;
	}
	
	public void setPassphrase(String value) {
		this._Passphrase = value;
	}

	private String _Password;
	
	@Override
	public String getPassword() {
		return this._Password;
	}
	
	public void setPassword(String value) {
		this._Password = value;
	}

	@Override
	public boolean promptPassphrase(String arg0) {
		return false;
	}

	@Override
	public boolean promptPassword(String arg0) {
		return false;
	}

	@Override
	public boolean promptYesNo(String arg0) {
		return false;
	}

	@Override
	public void showMessage(String message) {
		//I don't think this is needed...
	}
	
}
