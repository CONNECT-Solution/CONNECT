package gov.hhs.fha.nhinc.repository.util;

import gov.hhs.fha.nhinc.repository.model.EventCode;
import gov.hhs.fha.nhinc.repository.service.DocumentService;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.PropertyResourceBundle;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

/**
 * Utility to load documents from an XML file into the database. 
 * 
 * @author Neil Webb
 */
public class DocumentLoadUtil
{
    private static final String DATE_FORMAT_STRING = "yyyyMMddhhmmssZ";
    private static Log log = LogFactory.getLog(DocumentLoadUtil.class);

    private static void loadData(String absoluteFilePath) throws Exception
    {
        // Load file into XML
        Document doc = XmlUtil.getDocumentFromFile(absoluteFilePath);
        if (doc == null)
        {
            throw new Exception("Could not load document from file for: " + absoluteFilePath);
        }

        // Traverse the DOM and load documents into database
        NodeList docNodes = doc.getElementsByTagName("Document");
        if ((docNodes != null) && (docNodes.getLength() > 0))
        {
            for (int i = 0; i < docNodes.getLength(); i++)
            {
                Node node = docNodes.item(i);
                if (node instanceof Element)
                {
                    storeDocument((Element)node);
                }
            }
        }
    }

    private static void storeDocument(Element documentElement) throws Exception
    {
        if (documentElement == null)
        {
            throw new Exception("Document element was null.");
        }
        gov.hhs.fha.nhinc.repository.model.Document doc = new gov.hhs.fha.nhinc.repository.model.Document();
        doc.setDocumentid(getChildElementLongValue(documentElement, "documentId"));
        doc.setDocumentUniqueId(getChildElementStringValue(documentElement, "documentUniqueId"));
        doc.setRepositoryId("1");
        doc.setDocumentTitle(getChildElementStringValue(documentElement, "documentTitle"));
        doc.setAuthorPerson(getChildElementStringValue(documentElement, "authorPerson"));
        doc.setAuthorInstitution(getChildElementStringValue(documentElement, "authorInstitution"));
        doc.setAuthorRole(getChildElementStringValue(documentElement, "authorRole"));
        doc.setAuthorSpecialty(getChildElementStringValue(documentElement, "authorSpecialty"));
        doc.setAvailablityStatus(getChildElementStringValue(documentElement, "availablityStatus"));
        doc.setClassCode(getChildElementStringValue(documentElement, "classCode"));
        doc.setClassCodeScheme(getChildElementStringValue(documentElement, "classCodeScheme"));
        doc.setClassCodeDisplayName(getChildElementStringValue(documentElement, "classCodeDisplayName"));
        doc.setConfidentialityCode(getChildElementStringValue(documentElement, "confidentialityCode"));
        doc.setConfidentialityCodeScheme(getChildElementStringValue(documentElement, "confidentialityCodeScheme"));
        doc.setConfidentialityCodeDisplayName(getChildElementStringValue(documentElement, "confidentialityCodeDisplayName"));
        doc.setFormatCode(getChildElementStringValue(documentElement, "formatCode"));
        doc.setFormatCodeScheme(getChildElementStringValue(documentElement, "formatCodeScheme"));
        doc.setFormatCodeDisplayName(getChildElementStringValue(documentElement, "formatCodeDisplayName"));
        doc.setPatientId(getChildElementStringValue(documentElement, "patientId"));
        doc.setStatus(getChildElementStringValue(documentElement, "status"));
        doc.setComments(getChildElementStringValue(documentElement, "comments"));
        doc.setFacilityCode(getChildElementStringValue(documentElement, "facilityCode"));
        doc.setFacilityCodeScheme(getChildElementStringValue(documentElement, "facilityCodeScheme"));
        doc.setFacilityCodeDisplayName(getChildElementStringValue(documentElement, "facilityCodeDisplayName"));
        doc.setIntendedRecipientPerson(getChildElementStringValue(documentElement, "intendedRecipientPerson"));
        doc.setIntendedRecipientOrganization(getChildElementStringValue(documentElement, "intendedRecipientOrganization"));
        doc.setLanguageCode(getChildElementStringValue(documentElement, "languageCode"));
        doc.setLegalAuthenticator(getChildElementStringValue(documentElement, "legalAuthenticator"));
        doc.setMimeType(getChildElementStringValue(documentElement, "mimeType"));
        doc.setParentDocumentId(getChildElementStringValue(documentElement, "parentDocumentId"));
        doc.setParentDocumentRelationship(getChildElementStringValue(documentElement, "parentDocumentRelationship"));
        doc.setPracticeSetting(getChildElementStringValue(documentElement, "practiceSetting"));
        doc.setPracticeSettingScheme(getChildElementStringValue(documentElement, "practiceSettingScheme"));
        doc.setPracticeSettingDisplayName(getChildElementStringValue(documentElement, "practiceSettingDisplayName"));
        doc.setSize(getChildElementIntegerValue(documentElement, "size"));
        doc.setSourcePatientId(getChildElementStringValue(documentElement, "sourcePatientId"));
        doc.setPid3(getChildElementStringValue(documentElement, "pid3"));
        doc.setPid5(getChildElementStringValue(documentElement, "pid5"));
        doc.setPid8(getChildElementStringValue(documentElement, "pid8"));
        doc.setPid7(getChildElementStringValue(documentElement, "pid7"));
        doc.setPid11(getChildElementStringValue(documentElement, "pid11"));
        doc.setTypeCode(getChildElementStringValue(documentElement, "typeCode"));
        doc.setTypeCodeScheme(getChildElementStringValue(documentElement, "typeCodeScheme"));
        doc.setTypeCodeDisplayName(getChildElementStringValue(documentElement, "typeCodeDisplayName"));
        doc.setDocumentUri(getChildElementStringValue(documentElement, "documentUri"));
        doc.setRawData(getChildElementByteArray(documentElement, "rawData"));

        // Date fields
        doc.setCreationTime(parseDate(getChildElementStringValue(documentElement, "creationTime"),DATE_FORMAT_STRING));
        doc.setServiceStartTime(parseDate(getChildElementStringValue(documentElement, "serviceStartTime"),DATE_FORMAT_STRING));
        doc.setServiceStopTime(parseDate(getChildElementStringValue(documentElement, "serviceStopTime"),DATE_FORMAT_STRING));
//        doc.setServiceStartTime(parseDate(getChildElementStringValue(documentElement, "serviceStartTime"), "yyyyMMddhhmmss"));
//        doc.setServiceStopTime(parseDate(getChildElementStringValue(documentElement, "serviceStopTime"), "yyyyMMddhhmmss"));

        loadEventCodes(documentElement, doc);

        DocumentService docService = new DocumentService();
        docService.saveDocument(doc);
    }

    private static Date parseDate(String value, String dateFormat)
    {
        Date parsedDate = null;
        if ((value != null) && (dateFormat != null))
        {
            try
            {
                String formatString = prepareDateFormatString(dateFormat, value);
                SimpleDateFormat dateFormatter = new SimpleDateFormat(formatString);
                parsedDate = dateFormatter.parse(value);
            }
            catch (Throwable t)
            {
                log.error("Error parsing date '" + value + "' using format: '" +
                    dateFormat + "': " + t.getMessage(), t);
            }
        }
        return parsedDate;
    }

    /**
     * Prepare a date format string based on the length of the date string
     * to be parsed.
     * @param dateFormat Date format string (ex. yyyyMMddhhmmssZ)
     * @param dateString Date string to be parsed (ex. 19990205)
     * @return Modified format string based on the date string length (ex. yyyyMMdd)
     */
    public static String prepareDateFormatString(String dateFormat, String dateString)
    {
        String formatString = dateFormat;
        if ((dateString != null) && (dateFormat != null) && (dateString.length() > 0) && (dateString.length() < dateFormat.length()))
        {
            formatString = dateFormat.substring(0, dateString.length());
            if(log.isDebugEnabled())
            {
                log.debug("New dateFormat: " + dateFormat);
            }
        }
        return formatString;
    }

    private static void loadEventCodes(Element documentElement, gov.hhs.fha.nhinc.repository.model.Document doc)
    {
        if (documentElement != null)
        {
            NodeList nodes = documentElement.getElementsByTagName("eventCode");
            if ((nodes != null) && (nodes.getLength() > 0))
            {
                Set<EventCode> eventCodes = new HashSet<EventCode>();
                for (int i = 0; i < nodes.getLength(); i++)
                {
                    // eventCode element - load data
                    Node node = nodes.item(i);
                    if (node instanceof Element)
                    {
                        Element eventCodeElement = (Element)node;
                        EventCode eventCode = new EventCode();
                        eventCode.setDocument(doc);
                        eventCode.setEventCodeId(getChildElementLongValue(eventCodeElement, "codeId"));
                        eventCode.setEventCode(getChildElementStringValue(eventCodeElement, "code"));
                        eventCode.setEventCodeScheme(getChildElementStringValue(eventCodeElement, "codeSchema"));
                        eventCode.setEventCodeDisplayName(getChildElementStringValue(eventCodeElement, "codeDisplayName"));
                        eventCodes.add(eventCode);
                    }
                }
                doc.setEventCodes(eventCodes);
            }
        }
    }

    private static String getChildElementStringValue(Element element, String childElementName)
    {
        String value = null;
        if ((element != null) && (childElementName != null))
        {
            if(log.isDebugEnabled())
            {
                log.debug("Extracting child element '" + childElementName + "' from '" + element.getTagName() + "'");
            }
            NodeList nodes = element.getElementsByTagName(childElementName);
            if ((nodes != null) && (nodes.getLength() > 0))
            {
                Node node = nodes.item(0);
                if (node instanceof Element)
                {
                    Element childElement = (Element)node;
                    value = childElement.getTextContent();
                }
            }
        }
        return value;
    }

    private static Long getChildElementLongValue(Element element, String childElementName)
    {
        Long value = null;
        if ((element != null) && (childElementName != null))
        {
            if(log.isDebugEnabled())
            {
                log.debug("Extracting child element '" + childElementName + "' from '" + element.getTagName() + "'");
            }
            NodeList nodes = element.getElementsByTagName(childElementName);
            if ((nodes != null) && (nodes.getLength() > 0))
            {
                Node node = nodes.item(0);
                if (node instanceof Element)
                {
                    Element childElement = (Element)node;
                    String strVal = childElement.getTextContent();
                    if (strVal != null)
                    {
                        try
                        {
                            value = Long.parseLong(strVal);
                        }
                        catch (Throwable t)
                        {
                            log.error("Failed to parse long from '" + strVal + "'", t);
                        }
                    }
                }
            }
        }
        return value;
    }

    private static Integer getChildElementIntegerValue(Element element, String childElementName)
    {
        Integer value = null;
        if ((element != null) && (childElementName != null))
        {
            if(log.isDebugEnabled())
            {
                log.debug("Extracting child element '" + childElementName + "' from '" + element.getTagName() + "'");
            }
            NodeList nodes = element.getElementsByTagName(childElementName);
            if ((nodes != null) && (nodes.getLength() > 0))
            {
                Node node = nodes.item(0);
                if (node instanceof Element)
                {
                    Element childElement = (Element)node;
                    String strVal = childElement.getTextContent();
                    if (strVal != null)
                    {
                        try
                        {
                            value = new Integer(strVal);
                        }
                        catch (Throwable t)
                        {
                            log.error("Failed to parse integer from '" + strVal + "'",t);
                        }
                    }
                }
            }
        }
        return value;
    }

    private static byte[] getChildElementByteArray(Element element, String childElementName)
    {
        byte[] value = null;
        if ((element != null) && (childElementName != null))
        {
            if(log.isDebugEnabled())
            {
                log.debug("Extracting child element '" + childElementName + "' from '" + element.getTagName() + "'");
            }
            NodeList nodes = element.getElementsByTagName(childElementName);
            if ((nodes != null) && (nodes.getLength() > 0))
            {
                Node node = nodes.item(0);
                if (node instanceof Element)
                {
                    Element childElement = (Element)node;
                    String encStrVal = childElement.getTextContent();
                    if (encStrVal != null)
                    {
                        value = encStrVal.getBytes();
                    }
                }
            }
        }
        return value;
    }

    /**
     * This method copies files from input folder to output folder...
     * @param source
     * @param dest
     * @throws java.io.IOException
     */
    private static void copyFile(File source, File dest) throws IOException {
        if (!dest.exists()) {
            dest.createNewFile();
        }
        InputStream in = null;
        OutputStream out = null;
        try {
            in = new FileInputStream(source);
            out = new FileOutputStream(dest);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
        } finally {
            in.close();
            out.close();
            source.deleteOnExit();
        }
    }
    
    /**
     * Method reads bunch of files kept under a folder and move them to different folder after processing..
     */
    public static void readFiles() {
        String propFileName = "filespath";
        PropertyResourceBundle prop = (PropertyResourceBundle) PropertyResourceBundle.getBundle(propFileName);
        String inFilePath = prop.getString("inFilePath");
        String outFilePath = prop.getString("outFilePath");
        log.info("In Put File Path -> " + inFilePath);
        log.info("Out Put File Path -> " + outFilePath);
        File inputfolder = new File(inFilePath);
        File files[] = inputfolder.listFiles();
        if (files.length != 0) {
            String inFileName = "";
            File destFile = null;
            String absolutePath = "";
            for (File inFile : files) {
                if (inFile.isFile()) {
                    try {
                        absolutePath = inFile.getAbsolutePath();
                        try {
                            log.info("absolutePath" + absolutePath);
                            loadData(absolutePath);
                        } catch (Throwable t) {
                            log.debug("Failed to load documents: " + t.getMessage());
                            t.printStackTrace();
                        }
                        Thread.sleep(500);
                    } catch (InterruptedException ex) {
                        log.error(ex.getMessage());
                    }
                    inFileName = inFile.getName();
                    destFile = new File(outFilePath, inFileName);
                    try {
                        copyFile(inFile, destFile);
                    //inFile.delete();
                    } catch (IOException ex) {
                        log.error(ex.getMessage());
                    }
                }
            }
        } else {
            log.info("Input files Not found under the specific directory, Please verify filespath.properties and run again");
        }
    }
    
    /**
     * Entry point for the document load utility. The absolute path to the
     * source file is expected as the only parameter.
     * 
     * @param args Absolute path to source file as only parameter.
     */
    public static void main(String[] args)
    {
        log.debug("Begin DocumentLoad");
        try {
            if (args.length == 0) {
                readFiles();
            }
            if (args.length == 1) {
                loadData(args[0]);
            }
            if (args.length > 1) {
                throw new Exception("Only one argument of the absolute path to a " +
                        "source file is expected or bunch files expected using a propery file, review ReadMe.tx before uploading");
            }

        } catch (Throwable t) {
            log.debug("Failed to load documents: " + t.getMessage());
            t.printStackTrace();
        }
        log.debug("End DocumentLoad");
        System.exit(0);
    }
}
