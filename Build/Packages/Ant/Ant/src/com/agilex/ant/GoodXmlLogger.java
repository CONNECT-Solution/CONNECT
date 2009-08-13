package com.agilex.ant;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.Stack;
import java.util.Enumeration;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;

import org.apache.tools.ant.util.DOMElementWriter;
import org.apache.tools.ant.util.StringUtils;
import org.apache.tools.ant.BuildEvent;
import org.apache.tools.ant.BuildLogger;
import org.apache.tools.ant.Project;


public class GoodXmlLogger implements BuildLogger {
    
	private class StopWatchStack
	{
		private final Stack stack = new Stack();

		public StopWatchStack()
		{
			
		}

		public long PopStop()
		{
			return ((StopWatch) this.stack.pop()).Elapsed();
		}

		public void PushStart()
		{
			this.stack.push(new StopWatch());
		}

		private class StopWatch
		{
			private final long start;

			public StopWatch()
			{
				this.start = System.currentTimeMillis();
			}

			public long Elapsed()
			{
				return System.currentTimeMillis() - this.start;
			}
		}
	}

    public GoodXmlLogger() {
    }

    OutputStreamWriter out;
	PrintStream err;
	int outputLevel = Project.MSG_INFO;
    final StopWatchStack stopWatchStack = new StopWatchStack();

	private void writeDuration()
    {
        this.writeOutput("<duration>" + this.stopWatchStack.PopStop() + "</duration>\n");
    }
	
	private void writeOutput(String s) {
		try {
			out.write(s);
			out.flush();
		}
		catch (IOException ioe) {
			err.println("Exception encountered: "+ioe);
			ioe.printStackTrace(err);
		}
	}

	private void writeThrowable(Throwable t) {
		if (t!=null) {
			writeOutput("<failure>\n");
			writeOutput("<builderror>\n");
			writeOutput("<type>"+t.getClass().getName()+"</type>\n");
			writeOutput("<message><![CDATA["+t.getMessage()+"]]></message>\n");
			writeOutput("<stacktrace><![CDATA["+StringUtils.getStackTrace(t)+"]]></stacktrace>\n");
			writeOutput("</builderror>\n");
			writeOutput("</failure>\n");
		}
	}
	
	public void setMessageOutputLevel(int level) {
		outputLevel = level;
	}

	public void setOutputPrintStream(PrintStream outStream) {
		out = new OutputStreamWriter(outStream,Charset.forName("UTF-8"));
	}

	public void setErrorPrintStream(PrintStream errorStream) {
		err = errorStream;
	}

	public void setEmacsMode(boolean value) {}

	public void buildStarted(BuildEvent event) {
		this.stopWatchStack.PushStart();
		writeOutput("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
	}
	
	boolean initialized = false;
	private void logBuildInfo(Project project){
		writeOutput("<antbuildresults project=\"" + project.getName() + "\">\n");
		writeOutput("<message level=\"Info\"><![CDATA[Buildfile: file:///" + project.getProperty("ant.file") + "]]></message>\n");
		writeOutput("<message level=\"Info\"><![CDATA[Target jvm: " + project.getProperty("java.specification.vendor") + " " + project.getProperty("java.runtime.version") + "]]></message>\n");
		//writeOutput("<message level=\"Info\"><![CDATA[Target(s) specified: " + I don't know how to get the target specified at the command line!!! Ant Sucks!!! + "]]></message>\n");
		initialized = true;
	}

	public void buildFinished(BuildEvent event) {
		if (event.getException() != null){
			this.writeThrowable(event.getException());
		}
		this.writeDuration();
		writeOutput("</antbuildresults>\n");
	}

	public void targetStarted(BuildEvent event) {
		if (!initialized)
			this.logBuildInfo(event.getProject());
		this.stopWatchStack.PushStart();
		writeOutput("<target name=\"" + event.getTarget().getName() + "\">");
	}

	public void targetFinished(BuildEvent event) {	
		this.writeDuration();
		writeOutput("</target>\n");
	}

	public void taskStarted(BuildEvent event) {
		if (!initialized)
			this.logBuildInfo(event.getProject());
		this.stopWatchStack.PushStart();
		writeOutput("<task name=\"" + event.getTask().getTaskName() + "\">\n");
	}

	public void taskFinished(BuildEvent event) {
		this.writeDuration();
		writeOutput("</task>\n");
	}

	public void messageLogged(BuildEvent event) {
		int priority = event.getPriority();
		if (priority <= outputLevel) {
			String logLevel = "Debug";
			switch (priority) {
				case Project.MSG_ERR:
					logLevel = "Error";
					break;
				case Project.MSG_WARN:
					logLevel = "Warning";
					break;
				case Project.MSG_INFO:
					logLevel = "Info";
					break;
				default:
					logLevel = "Debug";
					break;
			}
			String rawMessage = this.stripFormatting(event.getMessage().trim());
			if (!rawMessage.matches("^[\\p{Cntrl}]*$"))
            {
				writeOutput("<message level=\"" + logLevel + "\">");
				if (this.isValidXml(rawMessage))
				{
					writeOutput(rawMessage.replaceAll("<\\?.*\\?>", ""));
				}
				else
				{
					writeOutput("<![CDATA[" + this.stripCData(rawMessage) + "]]>");
				}
				writeOutput("</message>\n");
			}
		}
	}
	
	public String stripFormatting(String message)
    {
		return message;
        //return message.replaceAll("\\0", "");
    }
	
	private boolean isValidXml(String message)
    {
        if (message.matches("^<.*>"))
        {
        	StringReader reader = new StringReader(message);
			
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(false);
			factory.setNamespaceAware(true);

			DocumentBuilder builder;
			try {
				builder = factory.newDocumentBuilder();
			} catch (Throwable  ex) {
				throw new RuntimeException(ex);
			}
			builder.setErrorHandler(null);

            try
            {
                builder.parse(new InputSource(reader));
			}
			catch(Throwable t)
			{
				return false;
			}
            finally
            {
                reader.close();
            }
            return true;
        }
        return false;
    }
	
	private String stripCData(String message)
    {
        return message.replaceAll("<!\\[CDATA\\[", "").replaceAll("\\]\\]>", "");
    }

}
