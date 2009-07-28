package com.agilex;

import java.io.*;

import fit.Fixture;
import fitlibrary.SequenceFixture;
import fitlibrary.table.Row;
import fitlibrary.traverse.workflow.SequenceTraverse;
import fitlibrary.utility.TestResults;
import fitnesse.components.CommandRunner;

public class IOFixture extends SequenceFixture {

	public Row currentRow;
	public TestResults currentTestResults;
	private SequenceTraverse sequenceTraverse;

	public IOFixture() {
		sequenceTraverse = new SequenceTraverse(this) {
			@Override
			public Object interpretRow(Row row, TestResults testResults,
					Fixture fixtureByName) {
				((IOFixture) this.getSystemUnderTest()).currentRow = row;
				((IOFixture) this.getSystemUnderTest()).currentTestResults = testResults;
				return super.interpretRow(row, testResults, fixtureByName);
			}
		};
		setTraverse(sequenceTraverse);
	}

	public IOFixture(Object sut) {
		this();
		setSystemUnderTest(sut);
	}

	public void create(String path) throws IOException {
		File ioThing = new File(path);

		if (ioThing.isDirectory()) {
			this.creteDirectory(path);
		} else {
			this.createFile(path);
		}

	}

	public void creteDirectory(String path) {
		File ioThing = new File(path);
		if (!ioThing.exists()) {
			ioThing.mkdirs();
		}
	}

	public void createFile(String path) throws IOException {
		File ioThing = new File(path);
		if (!ioThing.exists()) {
			ioThing.createNewFile();
		}
	}

	public boolean exists(String path) {
		File ioThing = new File(path);
		return ioThing.exists();
	}

	public void delete(String path) {
		File ioThing = new File(path);
		ioThing.delete();
	}

	public void move(String sourcePath, String destinationPath) throws Exception {
		this.copy(sourcePath, destinationPath);
		this.delete(sourcePath);
	}

	public void copy(String sourcePath, String destinationPath) throws Exception {
		InputStream inStream = null;
		OutputStream outStream = null;
		try {
			File sourceFile = new File(sourcePath);
			File destinationFile = new File(destinationPath);
			inStream = new FileInputStream(sourceFile);
			outStream = new FileOutputStream(destinationFile);

			byte[] buffer = new byte[1024];
			int len;
			while ((len = inStream.read(buffer)) > 0) {
				outStream.write(buffer, 0, len);
			}
		} finally {
			if (inStream != null)
				inStream.close();
			if (outStream != null)
				outStream.close();
		}
	}

	public void write(String path, String contents) throws IOException {
		File file = new File(path);
		FileWriter writer = new FileWriter(file);
		writer.write(contents);
		writer.close();
	}
	
	private class WaitForFileExists extends Wait {
		private File file;

		public WaitForFileExists(String filePath) {
			this.file = new File(filePath);
		}

		public boolean until() {
			return this.file.exists();
		}
	}
	
	public boolean waitForFileExists(String filePath) {
		Wait wait = new WaitForFileExists(filePath);
		return wait.trywait();
	}
	
	public boolean waitForFileExists(String filePath, long timeoutInMilliseconds) {
		Wait wait = new WaitForFileExists(filePath);
		return wait.trywait(timeoutInMilliseconds);
	}
	
	public boolean waitForFileExists(String filePath, long timeoutInMilliseconds, long intervalInMilliseconds) {
		Wait wait = new WaitForFileExists(filePath);
		return wait.trywait(timeoutInMilliseconds, intervalInMilliseconds);
	}

	public void append(String path, String contents) throws IOException {
		File file = new File(path);
		FileWriter writer = new FileWriter(file, true);
		writer.write(contents);
		writer.close();
	}
	
	public void mapDrive(String uri, String userName, String password) throws Exception {
		mapDrive("", uri, userName, password);
	}
	
	public void mapDrive(String driveLetter, String uri, String userName, String password) throws Exception {
		String command = String.format("net use %1$s %2$s %4$s /user:%3$s", driveLetter, uri, password, userName);
		CommandRunner runner = new CommandRunner(command , "");
		runner.run();
		if (runner.getExitCode() != 0){
			colorFail(command + "<hr/>" + runner.getOutput() + runner.getError(), 0);
		}
	}
	
	public void disconnectDrive(String uri) throws Exception {
		String command = String.format("net use %1$s /DELETE", uri);
		CommandRunner runner = new CommandRunner(command , "");
		runner.run();
		if (runner.getExitCode() != 0){
			colorFail(command + "<hr/>" + runner.getOutput() + runner.getError(), 0);
		}
	}

	public boolean filesAreSame(String pathA, String pathB) throws IOException {
		String contentsA = this.readFileAsString(pathA);
		String contentsB = this.readFileAsString(pathB);
		if (!contentsA.equals(contentsB)) {
			currentRow.cell(2).parse.body = contentsB + label(pathB) + "<hr>"
					+ Fixture.escape(contentsA) + label(pathA);
			currentRow.cell(2).parse.addToTag(" class=\"fail\"");
			return false;
		}
		return true;
	}

	public boolean fileContentsEquals(String path, String expectedContents)
			throws IOException {
		String actualContents = this.readFileAsString(path);
		return assertEquals(expectedContents, actualContents, 2);
	}

	public boolean fileContains(String path, String expectedContents)
			throws IOException {
		String actualContents = this.readFileAsString(path);
		return assertContains(expectedContents, actualContents, 2);
	}

	private String readFileAsString(String filePath) throws java.io.IOException {
		StringBuffer fileData = new StringBuffer(1000);
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		char[] buf = new char[1024];
		int numRead = 0;
		while ((numRead = reader.read(buf)) != -1) {
			fileData.append(buf, 0, numRead);
		}
		reader.close();
		return fileData.toString();
	}

	private boolean assertContains(String expected, String actual, int cellIndex) {
		if (!actual.contains(expected)) {
			currentRow.cell(cellIndex).parse.addToTag(" class=\"fail\"");
			currentRow.cell(cellIndex).parse.addToBody(label("expected")
					+ "<hr>" + Fixture.escape(actual) + label("actual"));
			return false;
		}
		return true;
	}

	private boolean assertEquals(String expected, String actual, int cellIndex) {
		if (!expected.equals(actual)) {
			currentRow.cell(cellIndex).parse.addToTag(" class=\"fail\"");
			currentRow.cell(cellIndex).parse.addToBody(label("expected")
					+ "<hr>" + Fixture.escape(actual) + label("actual"));
			return false;
		}
		return true;
	}

	private void colorFail(String message, int cellIndex){
		currentRow.cell(cellIndex).parse.addToTag(" class=\"fail\"");
		currentRow.cell(cellIndex).parse.addToBody("<hr>" + message);
	}
}
