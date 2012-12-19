/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
 * All rights reserved. 
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met: 
 *     * Redistributions of source code must retain the above 
 *       copyright notice, this list of conditions and the following disclaimer. 
 *     * Redistributions in binary form must reproduce the above copyright 
 *       notice, this list of conditions and the following disclaimer in the documentation 
 *       and/or other materials provided with the distribution. 
 *     * Neither the name of the United States Government nor the 
 *       names of its contributors may be used to endorse or promote products 
 *       derived from this software without specific prior written permission. 
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY 
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS 
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */
package gov.hhs.fha.nhinc.util.config.app;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * 
 * @author Neil Webb
 */
class ConfigurationUtil {
    private static final String ROOT_ELEMENT_BEANS = "beans";
    private static final String BEANS_ELEMENT_BEAN = "bean";
    private static final String BEANS_ELEMENT_DESCRIPTION = "description";
    private static final String BEAN_ATTRIBUTE_ID = "id";
    private static final String BEAN_ELEMENT_META = "meta";
    private static final String META_ATTRIBUTE_KEY = "key";
    private static final String META_ATTRIBUTE_VALUE = "value";
    private static final String META_ATTRIBUTE_KEY_IMPL_TYPE = "impltype";
    private static final Logger LOG = Logger.getLogger(ConfigurationUtil.class);

    public void switchToType(File sourceFile, BeanImplementationType implType) throws ParserConfigurationException,
            SAXException, IOException, TransformerConfigurationException, TransformerException {
        LOG.debug("Begin switchToType(...)");
        Document sourceDoc = loadDocument(sourceFile);
        LOG.debug("Document loaded");
        updateDocument(sourceDoc, implType);
        LOG.debug("document updated - saving");
        saveConfigFile(sourceFile, sourceDoc);
        LOG.debug("End switchToType(...)");
    }

    /**
     * Load an XML document from file
     * 
     * @param sourceFile Source file
     * @return XML Document from file contents
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    private Document loadDocument(File sourceFile) throws ParserConfigurationException, SAXException, IOException {
        LOG.debug("Begin loadDocument(...)");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(sourceFile);
        LOG.debug("End loadDocument(...)");
        return document;
    }

    /**
     * Update the beans in an XML document with the implementation type provided.
     * 
     * @param sourceDocument Source XML document
     * @param implType Target implementation type
     */
    private boolean updateDocument(Document sourceDocument, BeanImplementationType implType) {
        boolean updated = false;
        LOG.debug("Begin updateDocument(...)");
        if (sourceDocument == null) {
            throw new ConfigurationUtilException("Source document was null.");
        }
        if (implType == null) {
            throw new ConfigurationUtilException("Bean implementation type was null.");
        }

        Element beansElement = sourceDocument.getDocumentElement();
        if ((beansElement == null) || (!ROOT_ELEMENT_BEANS.equals(beansElement.getNodeName()))) {
            throw new ConfigurationUtilException("Invalid configuration file");
        }
        List<String> beanNames = getBeanNames(beansElement);
        if ((beanNames == null) || beanNames.isEmpty()) {
            LOG.info("No beans enabled for processing - exiting");
        } else {
            LOG.debug("Bean count: " + beanNames.size());
            for (String beanName : beanNames) {
                processBeanType(beanName, beansElement, implType);
            }
            updated = true;
        }
        LOG.debug("end updateDocument(...)");
        return updated;
    }

    private boolean saveConfigFile(File sourceFile, Document updatedDocument) throws IOException,
            TransformerConfigurationException, TransformerException {
        LOG.debug("Begin saveConfigFile");
        boolean success = false;
        DOMSource domSource = new DOMSource(updatedDocument);
        StreamResult streamResult = new StreamResult(sourceFile);
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer serializer = tf.newTransformer();
        serializer.transform(domSource, streamResult);
        LOG.debug("End saveConfigFile");
        return success;
    }

    private void processBeanType(String beanName, Element beansElement, BeanImplementationType implType) {
        LOG.debug("Processing bean: " + beanName);
        List<Element> beanElements = getElementsOfBeanType(beanName, beansElement);
        if ((beanElements == null) || beanElements.isEmpty()) {
            throw new ConfigurationUtilException("No beans found for bean name: " + beanName
                    + ". Please update bean names in the config file.");
        }
        LOG.debug("Found " + beanElements.size() + " matching bean entries");
        setBeanId(beanName, beanElements, implType);
        LOG.debug("End processing bean: " + beanName);
    }

    private boolean setTargetType(String beanName, List<Element> beanElements, BeanImplementationType implType) {
        LOG.debug("Begin setTargetType");
        boolean modifiedEntry = false;

        for (Element beanElement : beanElements) {
            String beanIdAttr = beanElement.getAttribute(BEAN_ATTRIBUTE_ID);
            if ((beanIdAttr == null) || (beanIdAttr.length() < 1)) {
                throw new ConfigurationUtilException("Invalid bean configuration for type (" + beanName + ") with id: "
                        + beanIdAttr);
            }
            String metaType = getMetadataValue(beanElement);
            boolean currentBeanMatch = implType.implementationType().equals(metaType);

            boolean currentBeanEnabled = beanName.equals(beanIdAttr);

            if (currentBeanMatch) {
                if (currentBeanEnabled) {
                    // Already enabled - no change required
                    LOG.debug("Already enabled - no change required");
                } else {
                    // Set the id attribute of the bean element to the bean name.
                    LOG.debug("Set the id attribute of the bean element to the bean name.");
                    beanElement.setAttribute(BEAN_ATTRIBUTE_ID, beanName);
                }
                modifiedEntry = true;
            }
        }

        LOG.debug("End setTargetType");
        return modifiedEntry;
    }

    private void resetOtherBeans(String beanName, List<Element> beanElements, BeanImplementationType implType) {
        LOG.debug("Begin resetOtherBeans");
        for (Element beanElement : beanElements) {
            String beanIdAttr = beanElement.getAttribute(BEAN_ATTRIBUTE_ID);
            if ((beanIdAttr == null) || (beanIdAttr.length() < 1)) {
                throw new ConfigurationUtilException("Bean did not contain an id attribute value");
            }
            String metaType = getMetadataValue(beanElement);
            boolean currentBeanMatch = implType.implementationType().equals(metaType);

            boolean currentBeanEnabled = beanName.equals(beanIdAttr);

            if ((!currentBeanMatch) && currentBeanEnabled) {
                // Reset the bean id to the bean name with the implementation type appended.
                LOG.debug("Reset the bean id to the value provided in the name attribute of the bean.");
                beanElement.setAttribute(BEAN_ATTRIBUTE_ID, (beanName + metaType));
            }
        }

        LOG.debug("End resetOtherBeans");
    }

    private void setBeanId(String beanName, List<Element> beanElements, BeanImplementationType implType) {
        LOG.debug("Begin setBeanId");
        if (setTargetType(beanName, beanElements, implType)) {
            resetOtherBeans(beanName, beanElements, implType);
        }
        LOG.debug("End setBeanId");
    }

    private String getMetadataValue(Element beanElement) {
        String metaValue = null;
        NodeList metaNodes = beanElement.getElementsByTagName(BEAN_ELEMENT_META);
        if ((metaNodes != null) && (metaNodes.getLength() > 0)) {
            LOG.debug("Bean had meta node(s)");
            for (int i = 0; i < metaNodes.getLength(); i++) {
                LOG.debug("Processing meta node");
                Node metaNode = metaNodes.item(i);
                if ((metaNode != null) && (metaNode instanceof Element)) {
                    LOG.debug("Meta node was an element");
                    Element metaElement = (Element) metaNode;
                    String metaKey = metaElement.getAttribute(META_ATTRIBUTE_KEY);
                    if (META_ATTRIBUTE_KEY_IMPL_TYPE.equalsIgnoreCase(metaKey)) {
                        LOG.debug("Meta element was 'impltype'");
                        metaValue = metaElement.getAttribute(META_ATTRIBUTE_VALUE);
                        LOG.debug("Meta value: " + metaValue);
                    }
                    break;
                }
            }
        }

        return metaValue;
    }

    private List<Element> getElementsOfBeanType(String beanName, Element beansElement) {
        List<Element> beans = new ArrayList<Element>();
        NodeList childNodes = beansElement.getChildNodes();
        if (childNodes != null) {
            LOG.debug("child nodes list was not null.");
            for (int i = 0; i < childNodes.getLength(); i++) {
                LOG.debug("Processing a node.");
                Node workingNode = childNodes.item(i);
                if ((BEANS_ELEMENT_BEAN.equals(workingNode.getNodeName())) && (workingNode instanceof Element)) {
                    LOG.debug("Found bean element.");
                    Element beanElement = (Element) workingNode;
                    String beanId = beanElement.getAttribute(BEAN_ATTRIBUTE_ID);
                    String implType = getMetadataValue(beanElement);
                    String currentBeanName = beanId;
                    if ((beanId != null) && (implType != null) && beanId.endsWith(implType)) {
                        currentBeanName = beanId.substring(0, beanId.indexOf(implType));
                    }
                    if (beanName.equals(currentBeanName)) {
                        LOG.debug("Found matching bean with id: " + beanId);
                        beans.add(beanElement);
                    }
                }
            }
        }

        return beans;
    }

    private List<String> getBeanNames(Element beansElement) {
        List<String> beanNames = new ArrayList<String>();
        if (beansElement != null) {
            LOG.debug("beans element was not null.");
            NodeList childNodes = beansElement.getChildNodes();
            if (childNodes != null) {
                LOG.debug("child nodes list was not null.");
                for (int i = 0; i < childNodes.getLength(); i++) {
                    LOG.debug("Processing a node.");
                    Node workingNode = childNodes.item(i);
                    if ((BEANS_ELEMENT_DESCRIPTION.equals(workingNode.getNodeName()))
                            && (workingNode instanceof Element)) {
                        LOG.debug("Found description element.");
                        Element descriptionElement = (Element) workingNode;
                        String description = descriptionElement.getTextContent();
                        LOG.debug("description: " + description);
                        if (description != null) {
                            int beginIndex = description.indexOf("{");
                            int endIndex = description.indexOf("}");
                            if ((beginIndex != -1) && (endIndex != -1)) {
                                String workingBeanNames = description.substring((beginIndex + 1), endIndex);
                                LOG.debug("Full bean name list: " + workingBeanNames);
                                if (workingBeanNames != null) {
                                    workingBeanNames = workingBeanNames.trim();
                                    if (workingBeanNames.length() > 0) {
                                        String[] beanNamesArray = workingBeanNames.split(",");
                                        if (beanNamesArray != null) {
                                            for (String beanName : beanNamesArray) {
                                                if (beanName != null) {
                                                    beanNames.add(beanName.trim());
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        break;
                    }
                }
            }
        }
        return beanNames;
    }
}
