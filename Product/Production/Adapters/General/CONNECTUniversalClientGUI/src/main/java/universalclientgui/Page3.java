/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
/*
 * Copyright (c) 2011, Conemaugh Valley Memorial Hospital
 *
 * This source is subject to the Conemaugh public license.  Please see the
 * license.txt file for more information.
 *
 * All other rights reserved.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND
 * CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT
 * SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package universalclientgui;

import java.io.IOException;

//faces
import com.sun.rave.web.ui.appbase.AbstractPageBean;
import com.sun.webui.jsf.component.Page;
import javax.faces.FacesException;

//logging
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

//JAXB libs
import javax.xml.parsers.*;
import org.xml.sax.InputSource;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

//utils
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

//connect
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import javax.faces.context.FacesContext;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;

/**
 * <p>Page bean that corresponds to a similarly named JSP page.  This
 * class contains component definitions (and initialization code) for
 * all components that you have defined on this page, as well as
 * lifecycle methods and event handlers where you may add behavior
 * to respond to incoming events.</p>
 *
 * @version Page3.java
 * @version Created on May 27, 2009, 5:38:42 PM
 * @author abarger
 */
public class Page3 extends AbstractPageBean {

    //logging properties
    private static Log log = LogFactory.getLog(Page3.class);
    //dynamic stylesheet properties
    private static final String UC_PROPERTY_FILE = "universalClient";
    private static final String C32_STYLE_SHEET_PROPERTY = "C32StyleSheet";
    private static String m_sPropertyFileDir = "";
    private static String m_sFileSeparator =
            System.getProperty("file.separator");
    private static final String m_sFailedEnvVarMessage =
            "Unable to access environment variable: NHINC_PROPERTIES_DIR.";
    private static boolean m_bFailedToLoadEnvVar = false;

    static {
        String sValue = PropertyAccessor.getInstance().getPropertyFileLocation();

        if ((sValue != null) && (sValue.length() > 0)) {
            // Set it up so that we always have a "/" at the end - in case
            //------------------------------------------------------------
            if ((sValue.endsWith("/")) || (sValue.endsWith("\\"))) {
                m_sPropertyFileDir = sValue;
            } else {
                m_sPropertyFileDir = sValue + m_sFileSeparator;
            }
        } else {
            log.error(m_sFailedEnvVarMessage);
            m_bFailedToLoadEnvVar = true;
        }
    }

    public PatientSearchData getPatientSearchData() {
        return (PatientSearchData) getSessionBean1().getPatientSearchDataList().get(0);
    }

    /**
     * <p>Automatically managed component initialization.  <strong>WARNING:</strong>
     * This method is automatically generated, so any user-specified code inserted
     * here is subject to being replaced.</p>
     */
    private void _init() throws Exception {
    }
    //Page binding components
    private Page page1 = new Page();

    public Page getPage1() {
        return page1;
    }

    public void setPage1(Page p) {
        this.page1 = p;
    }

    // </editor-fold>
    /**
     * <p>Construct a new Page bean instance.</p>
     */
    public Page3() {
    }

    /**
     * <p>Callback method that is called whenever a page is navigated to,
     * either directly via a URL, or indirectly via page navigation.
     * Customize this method to acquire resources that will be needed
     * for event handlers and lifecycle methods, whether or not this
     * page is performing post back processing.</p>
     *
     * <p>Note that, if the current request is a postback, the property
     * values of the components do <strong>not</strong> represent any
     * values submitted with this request.  Instead, they represent the
     * property values that were saved for this view when it was rendered.</p>
     */
    @Override
    public void init() {
        // Perform initializations inherited from our superclass
        super.init();
        // Perform application initialization that must complete
        // *before* managed components are initialized
        // TODO - add your own initialiation code here

        // <editor-fold defaultstate="collapsed" desc="Managed Component Initialization">
        // Initialize automatically managed components
        // *Note* - this logic should NOT be modified
        try {
            _init();
        } catch (Exception e) {
            log("Page1 Initialization Failure", e);
            throw e instanceof FacesException ? (FacesException) e : new FacesException(e);
        }



        String token = getSessionBean1().getAuthToken();
        if (token == null || token.isEmpty()) {

            try {
                getExternalContext().redirect("Page1.jsp");
            } catch (IOException ex) {
                log.error("Universal Client can not prerender Page3: " + ex.getMessage());
            }

        }
        // </editor-fold>
        // Perform application initialization that must complete
        // *after* managed components are initialized
        // TODO - add your own initialization code here
    }

    /**
     * <p>Callback method that is called after the component tree has been
     * restored, but before any event processing takes place.  This method
     * will <strong>only</strong> be called on a postback request that
     * is processing a form submit.  Customize this method to allocate
     * resources that will be required in your event handlers.</p>
     */
    @Override
    public void preprocess() {
    }

    /**
     * <p>Callback method that is called just before rendering takes place.
     * This method will <strong>only</strong> be called for the page that
     * will actually be rendered (and not, for example, on a page that
     * handled a postback and then navigated to a different page).  Customize
     * this method to allocate resources that will be required for rendering
     * this page.</p>
     */
    @Override
    public void prerender() {



        //check for appropriate request parameter
        if (getExternalContext().getRequestParameterMap().containsKey("docid")) {

            //retrieve docid value
            if (getExternalContext().getRequestParameterMap().get("docid") != null && !getExternalContext().getRequestParameterMap().get("docid").isEmpty()) {
                //docid parameter contains a value
                log.debug("Selected document ID: " + getExternalContext().getRequestParameterMap().get("docid"));

                //assign id to variable
                String documentID = getExternalContext().getRequestParameterMap().get("docid");

                //create DocumentInformation instance
                DocumentInformation currentDocument = null;
                //doc query & retrieve client instance

                String localPatientId = (String) getPatientSearchData().getPatientId() + "^^^&" +
                       getPatientSearchData().getAssigningAuthorityID() + "&ISO";

                DocumentRetrieveClient docRetrieveClient = new DocumentRetrieveClient();
                docRetrieveClient.setLocalPatientId(localPatientId);
                DocumentQueryResults docQueryResults = (DocumentQueryResults) getBean("DocumentQueryResults");

                //loop through returned documents and locate the requested document information
                for (DocumentInformation documentInformation : docQueryResults.getDocuments()) {
                    if (documentID.equals(documentInformation.getDocumentID())) {
                        currentDocument = documentInformation;
                        break;
                    }
                }

                String document = null;

                if (currentDocument != null) {
                    document = docRetrieveClient.retriveDocument(currentDocument);

                    if (document != null && !document.isEmpty()) {
                        try {
                            //declare output stream
                            HttpServletResponse response = (HttpServletResponse) getExternalContext().getResponse();
                            ServletOutputStream out = response.getOutputStream();

                            log.debug("Document " + documentID + " successfully retrieved from DocRetrieveClient.");
                            log.debug("Document Type = " + currentDocument.getDocumentType());
                            log.debug("Document LOINC = " + currentDocument.getDocumentLOINC());

                            //traverse the doc and return all C62 <nonXMLBody> tags
                            NodeList nonXMLBodyNodes = checkRetievedDocType(document);

                            //check to see if C62 nonXMLBody tag exists
                            if (nonXMLBodyNodes != null && nonXMLBodyNodes.getLength() > 0) {
                                log.debug("Retrieved Doc Type is a C64. Determining document payload...");


                                List<String> C64Payload = retrieveC64PayloadType(nonXMLBodyNodes);

                                if (C64Payload.get(0).equals("text")) {
                                    //set response content type
                                    response.setContentType("text/plain;charset=UTF-8");
                                    
                                } else if (C64Payload.get(0).equals("pdf")) {
                                    //set response content type
                                    response.setContentType("application/pdf");
                                }

                                log.debug("C62FileHandler : base64 string = " + C64Payload.get(1));

                                byte decoded[] = new sun.misc.BASE64Decoder().decodeBuffer(C64Payload.get(1));

                                // save it to a binary stream
                                out.write(decoded);

                            } else {
                                log.debug("Retrieved Doc Type is probably a C32.");

                                if (currentDocument.getDocumentLOINC() != null && currentDocument.getDocumentLOINC().equals("34133-9"))
                                {
                                        // Look for the C32 stylesheet specific to this provider
                                        log.debug("Lookup stylesheet for: " + currentDocument.getHomeCommunityID());
                                        String styleSheet = null;

                                        if (currentDocument.getHomeCommunityID() != null) {
                                            String[] saArgs = currentDocument.getHomeCommunityID().split(":");

                                            log.debug("Lookup property value for: C32StyleSheet_" + saArgs[saArgs.length - 1]);

                                            styleSheet = PropertyAccessor.getInstance().getProperty(UC_PROPERTY_FILE, "C32StyleSheet_" + saArgs[saArgs.length - 1]);
                                        }
                                        if ((styleSheet == null) || (styleSheet.isEmpty())) {
                                            // Provider-specific stylesheet not defined... use default
                                            String c32 = PropertyAccessor.getInstance().getProperty(UC_PROPERTY_FILE, C32_STYLE_SHEET_PROPERTY);
                                            styleSheet = c32;
                                        }

                                        log.debug("Converting raw XML using stylesheet: " + styleSheet);

                                        //read stylesheet file and prepare to render with document
                                        FileReader reader = new FileReader(m_sPropertyFileDir + styleSheet);

                                        String html = convertXMLToHTML(new ByteArrayInputStream(document.getBytes()), reader);

                                        log.debug("HTML PAGE: " + html);

                                        if (html == null || html.isEmpty()) {
                                            sendToErrorPage();
                                        }

                                        out.write(html.getBytes());
                                }
                                else
                                {
                                    //output blob text to the screen because it is not a c32 and we don't have a stylesheet for it
                                     out.write(document.getBytes());
                                }
                            }

                            //flush and close the output stream
                            out.flush();
                            out.close();

                            //complete faces response
                            FacesContext.getCurrentInstance().responseComplete();

                        } catch (Exception ex) {
                            log.error("Exception: " + ex);
                            sendToErrorPage();
                        }
                    } else {
                        log.error("Display Document Error - retrieved document is null or empty");

                        sendToErrorPage();
                    }
                } else {
                    log.error("Display Document Error - Document Information cannot be retrieved.");

                    sendToErrorPage();
                }
            } else {
                //the docid query parameter is null or empty
                log.debug("The docid value is null or empty. Cannot process doc retrieve request.");

                sendToErrorPage();
            }
        } else {
            //docid query parameter is missing from the url
            log.debug("No document id has been passed to the FileRetrieveHandler for processing.");

            sendToErrorPage();

        }
    }

    /**
     *
     */
    private void sendToErrorPage() {
        try {
            //forward to document.jsp error page
            FacesContext.getCurrentInstance().getExternalContext().dispatch("document.jsp");
        } catch (Exception ex) {
            log.error("Forward Exception: " + ex);
        }

    }

    /**
     *
     * this method checks the payload type of the C64 document - text or pdf
     *
     * valid return values are "pdf" or "text"
     */
    private List<String> retrieveC64PayloadType(NodeList nList) {
        log.debug("Retrieving text tag values...");

        //retrieve text tag from nonXMLBody
        Node node;
        String mediaType = "";
        String payloadType = null;
        String representationType = null;
        String base64 = null;
        List<String> payloadList = new ArrayList<String>();

        //retrieve the "mediaType" attribute from nonXMLBody Tag
        if (nList != null) {
            log.debug("Passed NodeList is not null");

            for (int i = 0; i < nList.getLength(); i++) {
                node = nList.item(i);

                if (node != null && node.getNodeName().equals("text")) {
                    log.debug("Located text tag within nonXMLBody Tag. Retrieving attributes...");

                    //get text node attributes
                    NamedNodeMap nnm = node.getAttributes();

                    if (nnm != null) {
                        log.debug("# of <text> attributes = " + String.valueOf(nnm.getLength()));

                        if (nnm.getNamedItem("representation") != null) {
                            representationType = nnm.getNamedItem("representation").getTextContent();
                        }

                        if (nnm.getNamedItem("mediaType") != null) {
                            mediaType = nnm.getNamedItem("mediaType").getTextContent();
                        }

                        //check if string should be decoded.
                        if (representationType != null && representationType.equals("B64")) {
                            //B64 - decode string
                            log.debug("representationType = B64 -  Decode Text");

                            //retrive Base64 encoded string
                            base64 = node.getTextContent();

                            //display content baed on media type
                            if (mediaType.contains("pdf")) {
                                //display pdf in browser
                                payloadList.add("pdf");
                            } else {
                                //display text document in browser
                                payloadList.add("text");
                            }

                            //add binary to List
                            payloadList.add(base64);
                        }
                    }

                }
            }
        }

        return payloadList;

    }

    /**
     *  this method checks to see if the returned document is a C32 or C62.
     *
     *  returns C64 <nonXML> Body nodes. If none exist, assume C32
     */
    private NodeList checkRetievedDocType(String pDocument) {
        NodeList nodes = null;
        NodeList nList = null;

        try {
            //need to check for C62 by parsing xml and looking for a "nonXMLbody" tag
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();

            log.debug("Successfuly created DocumentBuilder instances.");

            InputSource is = new InputSource();

            log.debug("Successfuly created InputSource instance.");

            is.setCharacterStream(new StringReader(pDocument));

            log.debug("Sucessfully set CharacterStream");

            //parse XML Document and search for C62 nonXMLBody tag
            Document doc = db.parse(is);
           

            if (doc.getElementsByTagName("nonXMLBody") != null)
            {
              nodes = doc.getElementsByTagName("nonXMLBody");
              nList = nodes.item(0).getChildNodes();
            }

        } catch (Exception ex) {
            log.error("checkRetrievedDocTypeError: " + ex);
        }

        return nList;
    }

    /**
     *
     * @param xml
     * @param xsl
     * @return
     */
    private String convertXMLToHTML(ByteArrayInputStream xml, FileReader xsl) {

        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try {

            TransformerFactory tFactory = TransformerFactory.newInstance();

            Transformer transformer =
                    tFactory.newTransformer(new javax.xml.transform.stream.StreamSource(xsl));

            transformer.transform(new javax.xml.transform.stream.StreamSource(xml),
                    new javax.xml.transform.stream.StreamResult(output));

        } catch (Exception e) {
            log.error("Exception in transforming xml to html", e);
        }

        return output.toString();
    }

    /**
     * <p>Callback method that is called after rendering is completed for
     * this request, if <code>init()</code> was called (regardless of whether
     * or not this was the page that was actually rendered).  Customize this
     * method to release resources acquired in the <code>init()</code>,
     * <code>preprocess()</code>, or <code>prerender()</code> methods (or
     * acquired during execution of an event handler).</p>
     */
    @Override
    public void destroy() {
    }

    /**
     * <p>Return a reference to the scoped data bean.</p>
     *
     * @return reference to the scoped data bean
     */
    protected SessionBean1 getSessionBean1() {
        return (SessionBean1) getBean("SessionBean1");
    }

    /**
     * <p>Return a reference to the scoped data bean.</p>
     *
     * @return reference to the scoped data bean
     */
    protected RequestBean1 getRequestBean1() {
        return (RequestBean1) getBean("RequestBean1");
    }

    /**
     * <p>Return a reference to the scoped data bean.</p>
     *
     * @return reference to the scoped data bean
     */
    protected ApplicationBean1 getApplicationBean1() {
        return (ApplicationBean1) getBean("ApplicationBean1");
    }
}

