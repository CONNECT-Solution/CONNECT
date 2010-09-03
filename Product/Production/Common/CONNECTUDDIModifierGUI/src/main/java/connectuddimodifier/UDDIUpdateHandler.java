/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
//********************************************************************
// FILE: UDDIUpdateHandler.java
//
// 2009 Harris Corporation. All rights reserved.
//
// CLASSIFICATION: Unclassified
//
// DESCRIPTION: UDDIUpdateHandler
//
// LIMITATIONS: None
//
// SOFTWARE HISTORY: //
//> 22OCT09 D. Cannon
// Initial Coding.
//<
//********************************************************************
package connectuddimodifier;

import java.io.File;


import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 *
 * @author dcannon
 */

public class UDDIUpdateHandler
{
    //TODO Add enum for update/success status

    private static final String newFileName = "uddiConnectionInfo.xml";

    //parent of where we are inserting.
    private static final    String rootNodeName = "businessEntities";

    //name of child node we are adding.
    private static final     String xmlStringNodeName = "businessEntity";

    //busEntStrParts 1 2 and 3 make up the businessEntity xml entry we want to
    //add seperated by the values we are taking in.
    private final static String busEntStrPart1 = "<businessEntity businessKey=\"\">" +
            "<names><businessName></businessName></names>" +
            "<descriptions><businessDescription></businessDescription></descriptions>" +
            "<homeCommunityId>";
    private final static String busEntStrPart2 = "</homeCommunityId>" +
            "<federalHIE>false</federalHIE><publicKeyURI/>" +
            "<businessServices><businessService serviceKey=\"\">" +
            "<names><name>Subject Discovery Service</name></names><descriptions>" +
            "<description>Service to perform subject discovery over NHIN</description>" +
            "</descriptions><uniformServiceName>subjectdiscovery</uniformServiceName>" +
            "<serviceVersion>2.0</serviceVersion><internalWebService>false</internalWebService>" +
            "<bindingTemplates><bindingTemplate bindingKey=\"\"><endpointURL>";
    private final static String busEntStrPart3 = "</endpointURL>" +
            "<wsdlURL/></bindingTemplate></bindingTemplates></businessService>" +
            "</businessServices></businessEntity>";

    public String updateUDDI(String homeCommunityID, String endPoint)
    {
        //Keep track of progress and errors so we know what to display
        // on return.
        String retval = "success";

        //xml to write in
        String xmlString = busEntStrPart1 + homeCommunityID +
                busEntStrPart2 + endPoint + busEntStrPart3;

        //Get the NHIN properties directory to find the uddiConnectionInfo.xml
        String path = PropertyAccessor.getPropertyFileLocation();

        //path and file name we are writing too.
        String xmlFileName = path+ File.separator + newFileName;



        try
        {
            //open up the original file and find the node to add under
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            DocumentBuilder db4XMLFile = factory.newDocumentBuilder();
            Document doc4XMLFile = db4XMLFile.parse(xmlFileName);
            Node root4XMLFile = doc4XMLFile.getElementsByTagName(rootNodeName).item(0);

            //Parse out all the endpoints
            NodeList endpoints = doc4XMLFile.getElementsByTagName("endpointURL");

            //compare all the endpoints vs the given one to check
            // if its already in the file
            String tmpEPVal = "";
            for(int i=0; i<endpoints.getLength(); i++)
            {
               tmpEPVal = (endpoints.item(i)).getTextContent();
               if(tmpEPVal.equals(endPoint))
               {
                  retval = "duplicate Endpoint";
               }
            }

            String tmpHCIDVal = "";
            //Parse out all the home community ids
            NodeList hcids = doc4XMLFile.getElementsByTagName("homeCommunityId");
            //compare all the home communtiy ids vs the given one to check
            // if its already in the file
            for(int j=0; j<hcids.getLength(); j++)
            {
                  tmpHCIDVal = (hcids.item(j)).getTextContent();
               if(tmpHCIDVal.equals(homeCommunityID))
               {
                  retval = "duplicate HCID";
               }
            }

            if(retval.equals("success"))
            {
                //build the child node off our constructed xml string.
                DocumentBuilder db4XMLString = factory.newDocumentBuilder();
                InputSource inStream = new InputSource();
                inStream.setCharacterStream(new StringReader(xmlString));
                Document doc4XMLString = db4XMLString.parse(inStream);
                Node root4XMLString = doc4XMLString.getElementsByTagName(xmlStringNodeName).item(0);

                root4XMLFile.appendChild(doc4XMLFile.importNode(root4XMLString, true));

                File file = new File(xmlFileName);
                StreamResult result = new StreamResult(file.toURI().getPath());

                DOMSource source = new DOMSource(doc4XMLFile);
                Transformer transformer = TransformerFactory.newInstance().newTransformer();
                transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
                transformer.setOutputProperty(OutputKeys.METHOD, "xml");
                transformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

                transformer.transform(source, result);
            }
        } catch (Exception e)
        {
            retval = e.toString();
        }
        finally
        {
            return retval;
        }

    }
}
