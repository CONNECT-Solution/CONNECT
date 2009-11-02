/*
 * This code is subject to the HIEOS License, Version 1.0
 *
 * Copyright(c) 2008-2009 Vangent, Inc.  All rights reserved.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * SoapRequestBean.java
 *
 * Created on October 31, 2005, 12:40 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package com.vangent.hieos.xutil.soap;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ConnectException;
import java.util.Iterator;
import java.util.Properties;

import javax.activation.DataContentHandlerFactory;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.AttachmentPart;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeader;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;

import org.w3c.dom.Document;

/**
 *
 * @author gunn
 */
public class SoapRequestBean {
    
    /** Creates a new instance of SoapRequestBean */
    
    String[] attachfileArray = null;
    String[] mimeTypeArray = null;
    String[] uuidArray = null ;
    int NumOfAttachFiles = 0;
//        String xdsPropertiesFile = "test.properties";
    String url = null;
    String patientid = null;
    String Metadata = null;
    //File Metadata = null;
    String metadatafilename = null;
    
    String metadataInfo = "metadata info";
    String msgrequeststring = "message request string";
    String msgresponse = "message response string";
    String logstring = "logstring";
    
    public void SoapRequestBean() {      
    }
    
    public void setURL(String url) {
        this.url = url;
    }
    
    public String getURL() {
        return url;
    }
    
    public void setMetadata(String Metadata) {
        this.Metadata = Metadata;
    }
    
    public String getMetadata() {
        return Metadata;
    }
    
    public void setattachfileArray(String[] attachfileArray) {
        this.attachfileArray = attachfileArray;
    }
    
    public String[] getattachfileArray() {
        return attachfileArray;
    }
    
    public void setmimeTypeArray(String[] mimeTypeArray) {
        this.mimeTypeArray = mimeTypeArray;
    }
    
    public String[] getmimeTypeArray() {
        return mimeTypeArray;
    }
    
    public void setuuidArray(String[] uuidArray) {
        this.uuidArray = uuidArray;
    }
    
    public String[] getuuidArray() {
        return uuidArray;
    }
    
    public void setpatientid(String patientid) {
        this.patientid = patientid;
    }
    
    public String getpatientid() {
        return patientid;
    }
    
    public void setNumofAttachfile(int NumOfAttachFiles) {
        this.NumOfAttachFiles = NumOfAttachFiles;
    }
    
    public int getNumofAttachfile() {
        return NumOfAttachFiles;
    }
        
    public void setlogmsg(String logstring) {     
        this.logstring = logstring;
    }
    
    public String getlogmsg() {
        return logstring;
    }
    
    public void setRequestmsg(String message) {
        this.msgrequeststring = msgrequeststring;
    }
    
    public String getRequestmsg() {
        return msgrequeststring;
    }
    
    public void setmetadataInfo(String metadataInfo) {
        //directs SOAP messages to a string buffer;
        this.metadataInfo = metadataInfo;
    }
    
    public String getmetadataInfo() {
        return metadataInfo;
    }
    
    
    public void setResponsemsg(String msgresponse) {
        //directs SOAP messages to a string buffer;
        this.msgresponse = msgresponse;
    }
    
    public String getResponsemsg() {
        return msgresponse;
    }
    public void SoapRequest(String metadata, String[]attachfileArray, String[]mimeTypeArray, String[]uuidArray, int NumOfAttachFiles, String url, String patientid)
   throws Exception    
    {
        DataContentHandlerFactory dchf = null;
        String headervalue = " ";
        PrintStream orig = System.out;
        File msgfile = null;
        String msgfilename = "log.msg";
        
        try {
            MessageFactory messageFactory = MessageFactory.newInstance();
            SOAPMessage message = messageFactory.createMessage();
            SOAPEnvelope envelope = message.getSOAPPart().getEnvelope();
            SOAPHeader soapHeader = envelope.getHeader();
            
            //adds an empty string to the SOAPheader
            soapHeader.addTextNode(headervalue);
            SOAPBody soapBody = envelope.getBody();
            SOAPElement bodyElement = soapBody.addDocument(buildDoc(metadata));
            
            
            String metadataInfo = ("\nURL = " + url + "\npatientid = " + patientid);
            setmetadataInfo(metadataInfo);
            
            //print the message itself
            //System.out.println("\n\nMessage: " + headerType);
            ByteArrayOutputStream bye = new ByteArrayOutputStream();
            ByteArrayOutputStream requestArray = printWebMessage(message, "REQUEST: ", bye);
            msgrequeststring = requestArray.toString();
            setRequestmsg(msgrequeststring);
            
            //process the attached files
            if (NumOfAttachFiles > 0 ) {
                for (int i = 0; i < NumOfAttachFiles; i++) {
                    
                    // Set the mime type and uuid for the  attachment file. The uuid is used as the file name of
                    // stored in the repository
                    String ExtrinsicObjMimeType = mimeTypeArray[i];
                    String ExtrinsicObjUUID =  uuidArray[i];
                    
                    // Gets the attached file.
                    DataSource Attachfile = new FileDataSource(attachfileArray[i]);
                    DataHandler Attachmentdh = new DataHandler(Attachfile);
                    
                    AttachmentPart Attachmentpart = message.createAttachmentPart(Attachmentdh);
                    Attachmentpart.setMimeHeader("Content-Type", ExtrinsicObjMimeType);
                    Attachmentpart.setContentId(ExtrinsicObjUUID);                   
                    message.addAttachmentPart(Attachmentpart);
                }
            }
            
            SOAPConnectionFactory connFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection conn = connFactory.createConnection();
            SOAPMessage response = conn.call(message, url);
            conn.close();
            
            //prints out the response back from the registry
            SOAPMessage responsemessage = onMessage(response);
            
            
            ByteArrayOutputStream respbye = new ByteArrayOutputStream();
            ByteArrayOutputStream respArray = printWebMessage(responsemessage, "\n\n\n MYTestRESPONSE", bye);
            msgresponse = respArray.toString();
            setResponsemsg(msgresponse);
                        
            // send exceptions to the error.log file
        } catch (ConnectException ce) {          
            ByteArrayOutputStream logarray = new ByteArrayOutputStream();
            ce.printStackTrace(writeErrorlog(logarray));
            logstring = logarray.toString();
            setlogmsg(logstring);
	    throw new ConnectException(ce.getMessage() + "\n" + logstring);
        } catch (SOAPException ex) {        
            ByteArrayOutputStream logarray = new ByteArrayOutputStream();
            ex.printStackTrace(writeErrorlog(logarray));
            logstring = logarray.toString();
            setlogmsg(logstring);
	    throw new SOAPException(ex.getMessage() + "\n" + logstring);            
        } catch (Exception e) {
            ByteArrayOutputStream logarray = new ByteArrayOutputStream();
            e.printStackTrace(writeErrorlog(logarray));
            logstring = logarray.toString();
            setlogmsg(logstring);            
	    throw new Exception(e.getMessage() + "\n" + logstring);            
        }
    }
        
    public PrintStream writeErrorlog(ByteArrayOutputStream logarray) {
        
        PrintStream printStream = new PrintStream(logarray);
        logarray.reset();
        printStream.println("\n\nERROR OCCURRED: See");
        
        return printStream;
    }
        
    public Document buildDoc(String metadata){
        
        Document document = null;
        PrintStream orig = System.out;
        
        //uses the JAXP API to build a DOM Document. This parses the SubmitObjectsRequest containing
        //registry metadata for addition to the soap body
        
        byte stringBytes[] = metadata.getBytes();
        ByteArrayInputStream InStrm = new ByteArrayInputStream(stringBytes);
        
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        try {
            dbFactory.setNamespaceAware(true);
            DocumentBuilder builder = dbFactory.newDocumentBuilder();
            document = builder.parse(InStrm);
        } catch (ParserConfigurationException pce) {           
            ByteArrayOutputStream logarray = new ByteArrayOutputStream();
            pce.printStackTrace(writeErrorlog(logarray));
            logstring = logarray.toString();
            setlogmsg(logstring);  
                                  
        } catch (org.xml.sax.SAXException se) {
            ByteArrayOutputStream logarray = new ByteArrayOutputStream();
            se.printStackTrace(writeErrorlog(logarray));
            logstring = logarray.toString();
            setlogmsg(logstring);  
 
        } catch (IOException ex) {
            ByteArrayOutputStream logarray = new ByteArrayOutputStream();
            ex.printStackTrace(writeErrorlog(logarray));
            logstring = logarray.toString();
            setlogmsg(logstring);  
        }
        
        return document;
    }
    
    
    public SOAPMessage onMessage(SOAPMessage msg){
        //handles the response back from the registry
        PrintStream orig = System.out;
        
        try {
            SOAPEnvelope env = msg.getSOAPPart().getEnvelope();
            Name response = env.createName("response");
            env.getBody().getChildElements(response);
            
        } catch (SOAPException ex) {
            ByteArrayOutputStream logarray = new ByteArrayOutputStream();
            ex.printStackTrace(writeErrorlog(logarray));
            logstring = logarray.toString();
            setlogmsg(logstring);  
        }
        
        return msg;
    }
    
    private ByteArrayOutputStream printWebMessage(SOAPMessage message, String headerType, ByteArrayOutputStream bye) throws IOException, SOAPException {
        
        PrintStream printStream = new PrintStream(bye);
        bye.reset();
        
        printStream.println("\n************************************************");
        if (message != null) {
            //get the mime headers and print them
            printStream.println("\n\nHeader: " + headerType);
            if (message.saveRequired()) {
                message.saveChanges();
            }
            MimeHeaders headers = message.getMimeHeaders();
            Iterator iter = headers.getAllHeaders();
            while (iter.hasNext()) {
                MimeHeader header = (MimeHeader)iter.next();
                System.out.println("the header is " + header.getValue());
                printStream.println("\t" + header.getName() + " : " + header.getValue());
            }
            
            //print the message itself
            
            printStream.println("\n\nMessage: " + headerType);
            message.writeTo(bye);
        }
        return bye;
    }
    
    private static void printMessage(SOAPMessage message, String headerType) throws IOException, SOAPException {
        if (message != null) {
            //get the mime headers and print them
            System.out.println("\n\nHeader: " + headerType);
            if (message.saveRequired()) {
                message.saveChanges();
            }
            MimeHeaders headers = message.getMimeHeaders();
            printHeaders(headers);
            
            //print the message itself
            System.out.println("\n\nMessage: " + headerType);
            message.writeTo(System.out);
            System.out.println();
        }
    }
    
    private static void printHeaders(MimeHeaders headers) {
        // used to print all http headers
        printHeaders(headers.getAllHeaders());
    }
    
    private static void printHeaders(Iterator iter){
        while (iter.hasNext()) {
            MimeHeader header = (MimeHeader)iter.next();
            System.out.println("\t" + header.getName() + " : " + header.getValue());
        }
    }
       
    public static void setSecurityfiles(String securitypropfile) {
        
        //String SecurityPropertyFile = "/home/gunn/xdsTestClient/security.properties";
        String keystore = null;
        String keystorepass = null;
        String certsfile = null;
        String certsfilepass = null;
        
        Properties securityprops = new Properties();
        try {
            securityprops.load(new FileInputStream(securitypropfile));
        } catch(FileNotFoundException e) {
            System.out.print(" No Such file ");
            System.exit(0);
            // e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        }
        keystore = securityprops.getProperty("keystore");
        keystorepass = securityprops.getProperty("keystorepass");
        certsfile = securityprops.getProperty("certsfile");
        certsfilepass = securityprops.getProperty("certsfilepass");
        //set the system security properties
        System.setProperty("javax.net.ssl.keyStore", keystore);
        System.setProperty("javax.net.ssl.keyStorePassword", keystorepass);
        System.setProperty("javax.net.ssl.trustStore",certsfile);
        System.setProperty("javax.net.ssl.trustStorePassword", certsfilepass);
    }   
}



