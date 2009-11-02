/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/util/ISO3166Importer.java,v 1.7 2005/02/23 23:15:42 farrukh_najmi Exp $
 * ====================================================================
 */
/*
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/util/ISO3166Importer.java,v 1.7 2005/02/23 23:15:42 farrukh_najmi Exp $
 */
package org.freebxml.omar.server.util;

import org.freebxml.omar.common.BindingUtility;
import javax.xml.registry.JAXRException;
import org.freebxml.omar.common.UUIDFactory;

import org.oasis.ebxml.registry.bindings.rim.ClassificationNode;
import org.oasis.ebxml.registry.bindings.rim.ClassificationScheme;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StreamTokenizer;

import java.util.HashMap;
import java.util.List;

import javax.xml.bind.JAXBException;


/**
 * It is the importer for ISO3166 taxonomy. The MiddleEast node/tree will be a
 * sub-node/tree of Asia node.        The Australia node will be moved from Other node
 * to Australia node next to New Zealand. The remaining nodes remain the same
 * relationship as the original input ISO3166 taxonomy.
 * @author Adrian Chong
 */
public class ISO3166Importer {
    private StreamTokenizer tokenizer;
    private int maxNumOfCountries;
    private UUIDFactory uUIDFactory;

    /**
     *         Constructor.
     *         @param fileName The file path and name of the ISO3166 taxonomy file.
     *         @param uUIDFactory The UUIDFactory
     */
    public ISO3166Importer(String fileName, UUIDFactory uUIDFactory)
        throws FileNotFoundException {
        tokenizer = new StreamTokenizer(new FileReader(fileName));
        tokenizer.eolIsSignificant(false);
        tokenizer.slashSlashComments(true);
        tokenizer.slashStarComments(true);
        tokenizer.whitespaceChars(',', ',');
        this.uUIDFactory = uUIDFactory;
    }

    /**
     *         Set the file path of the ISO3166 taxonomy file.
     *         @param fileName The file path of the ISO3166 taxonomy file
     */
    public void setFile(String fileName) throws FileNotFoundException {
        tokenizer = new StreamTokenizer(new FileReader(fileName));
    }

    /**
     *         Set the maximum number of entries should be handled. Setting it to 0 means
     *         that the importer will handle unlimited number of entries.
     *         @param maxNum The maximum number of entries the importer will handle
     */
    public void setMaxNumOfCountries(int maxNum) {
        maxNumOfCountries = maxNum;
    }

    /**
     *         Get the ClassificationScheme for ISO3166 taxonomy
     *         @return the ClassificationScheme
     */
    public ClassificationScheme getClassificationScheme()
        throws IOException, JAXBException, JAXRException {
        int count = 0;

        ClassificationScheme classScheme = BindingUtility.getInstance().rimFac.createClassificationScheme();
        classScheme.setId("urn:uuid:" + uUIDFactory.newUUID().toString());
        classScheme.setIsInternal(true);
        classScheme.setNodeType(BindingUtility.CANONICAL_NODE_TYPE_ID_UniqueCode);
        classScheme.setDescription(BindingUtility.getInstance().getDescription("This is the classification scheme for ISO3166"));
        classScheme.setName(BindingUtility.getInstance().getName("ISO 3166"));

        HashMap continentToNodeMap = new HashMap();

        while (true) {
            if (((maxNumOfCountries != 0) && (count > maxNumOfCountries)) ||
                    (tokenizer.nextToken() == StreamTokenizer.TT_EOF)) {
                break;
            }

            //System.out.println(tokenizer.sval);
            String code = tokenizer.sval;
            tokenizer.nextToken();

            //System.out.println(tokenizer.sval);
            String name = tokenizer.sval;
            tokenizer.nextToken();

            //System.out.println(tokenizer.sval);
            String continent = tokenizer.sval;

            ClassificationNode parentClassificationNode = null;

            if ((parentClassificationNode = (ClassificationNode) continentToNodeMap.get(
                            continent)) == null) {
                // Create root concept for a continent
                String continentId = uUIDFactory.newUUID().toString();

                // id attribute
                parentClassificationNode = BindingUtility.getInstance().rimFac.createClassificationNode();
                parentClassificationNode.setId("urn:uuid:" + continentId);

                /*
                Add the continent concept to the Classification Scheme,
                and set parent attribite
                 */
                classScheme.getClassificationNode().add(parentClassificationNode);

                // code attribute
                parentClassificationNode.setCode(continent);

                // name
                parentClassificationNode.setName(BindingUtility.getInstance()
                                                               .getName(continent));
                continentToNodeMap.put(continent, parentClassificationNode);
            }

            //Generate new ClassificationNode
            String countryId = uUIDFactory.newUUID().toString();

            // id attribute
            ClassificationNode node = BindingUtility.getInstance().rimFac.createClassificationNode();
            node.setId("urn:uuid:" + countryId);

            /*
            Add the concept to the continent parent and set the parent attribute
             */
            parentClassificationNode.getClassificationNode().add(node);

            // code attribute
            node.setCode(code);

            // name attribute
            node.setName(BindingUtility.getInstance().getName(name));
            count++;
        }

        return fixNodes(classScheme);
    }

    /**
     *         Make the MiddleEast node/tree be a sub-node/tree of Asia node.
     *         Move Australia from Other node to Australia node next to New Zealand.
     */
    private ClassificationScheme fixNodes(ClassificationScheme classScheme) {
        List nodes = classScheme.getClassificationNode();
        ClassificationNode asiaNode = null;
        ClassificationNode middleEastNode = null;
        ClassificationNode otherNode = null;
        ClassificationNode australiaNode = null;

        for (int i = 0; i < nodes.size(); i++) {
            ClassificationNode _node = (ClassificationNode) nodes.get(i);

            if (_node.getCode().equalsIgnoreCase("Asia")) {
                asiaNode = _node;
            } else if (_node.getCode().equalsIgnoreCase("Other")) {
                otherNode = _node;
            } else if (_node.getCode().equalsIgnoreCase("Middle East")) {
                middleEastNode = _node;
            } else if (_node.getCode().equalsIgnoreCase("Australia")) {
                australiaNode = _node;
            }
        }

        ClassificationNode australiaInOtherNode = null;
        List otherNodeChildren = otherNode.getClassificationNode();

        for (int i = 0; i < otherNodeChildren.size(); i++) {
            ClassificationNode _node = (ClassificationNode) otherNodeChildren.get(i);

            if (_node.getCode().equalsIgnoreCase("AU")) {
                australiaInOtherNode = _node;
            }
        }

        if ((middleEastNode != null) && (asiaNode != null)) {
            classScheme.getClassificationNode().remove(middleEastNode);
            asiaNode.getClassificationNode().add(middleEastNode);
        }

        if ((otherNode != null) && (australiaNode != null) &&
                (australiaInOtherNode != null)) {
            otherNode.getClassificationNode().remove(australiaInOtherNode);
            australiaNode.getClassificationNode().add(australiaInOtherNode);
        }

        return classScheme;
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.out.println("usage: [input file path] [output file path]");

            return;
        }

        UUIDFactory uUIDFactory = UUIDFactory.getInstance();
        ISO3166Importer importer = new ISO3166Importer(args[0], uUIDFactory);
        ClassificationScheme scheme = importer.getClassificationScheme();
        FileWriter outputWriter = new FileWriter(args[1]);
        BindingUtility.getInstance().getJAXBContext().createMarshaller()
                      .marshal(scheme, outputWriter);
    }
}
