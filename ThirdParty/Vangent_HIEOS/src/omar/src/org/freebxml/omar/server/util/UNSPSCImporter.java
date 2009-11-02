/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/util/UNSPSCImporter.java,v 1.8 2005/04/21 19:24:23 joehw Exp $
 * ====================================================================
 */
/*
$Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/util/UNSPSCImporter.java,v 1.8 2005/04/21 19:24:23 joehw Exp $
*/
package org.freebxml.omar.server.util;

import org.freebxml.omar.common.BindingUtility;
import javax.xml.registry.JAXRException;
import org.freebxml.omar.common.UUIDFactory;
import org.freebxml.omar.server.util.ServerResourceBundle;

import org.oasis.ebxml.registry.bindings.rim.ClassificationNode;
import org.oasis.ebxml.registry.bindings.rim.ClassificationScheme;

import java.io.FileReader;
import java.io.IOException;
import java.io.StreamTokenizer;

import javax.xml.bind.JAXBException;


/**
 * It is the importer for UNSPSC. It will automatically skip the first line in the
 * input file.
 *
 * @author Adrian Chong
*/
public class UNSPSCImporter {
    private static final int SEGMENT = 1;
    private static final int FAMILY = 2;
    private static final int CLASS = 3;
    private static final int COMMODITY = 4;
    private static final int BUSINESS_FUNCTION = 5;
    private StreamTokenizer tokenizer;
    private int maxNumOfEntries;
    private UUIDFactory uUIDFactory;

    /**
    *         Constructor.
    *         @param fileName The file path and name of the UNSPSC taxonomy file.
    *         @param uUIDFactory The UUIDFactory
    */
    public UNSPSCImporter(String fileName, UUIDFactory uUIDFactory)
        throws IOException {
        setFile(fileName);
        tokenizer.eolIsSignificant(false);
        tokenizer.slashSlashComments(true);
        tokenizer.slashStarComments(true);
        tokenizer.wordChars(' ', '/');
        tokenizer.whitespaceChars(',', ',');
        tokenizer.parseNumbers();
        this.uUIDFactory = uUIDFactory;
    }

    /**
    *         Set the file path of the UNSPSC taxonomy file.
    *         @param fileName The file path of the UNSPSC taxonomy file
        */
    public void setFile(String fileName) throws IOException {
        tokenizer = new StreamTokenizer(new FileReader(fileName));
        tokenizer.eolIsSignificant(true);

        while ((tokenizer.nextToken() != StreamTokenizer.TT_EOF) &&
                (tokenizer.lineno() < 2)) {
        }

        tokenizer.eolIsSignificant(false);
    }

    /**
    *         Set the maximum number of entries should be handled. Setting it to 0 means
    *         that the importer will handle unlimited number of entries.
    *         @param maxNum The maximum number of entries the importer will handle
        */
    public void setMaxNumOfEntries(int maxNum) {
        maxNumOfEntries = maxNum;
    }

    /**
    *         Get the ClassificationScheme for NAICS taxonomy
    *         @return the ClassificationScheme
        */
    public ClassificationScheme getClassificationScheme()
        throws IOException, JAXBException, JAXRException {
        // Create the classification scheme
        ClassificationScheme classScheme = BindingUtility.getInstance().rimFac.createClassificationScheme();
        classScheme.setId("urn:uuid:" + uUIDFactory.newUUID().toString());
        classScheme.setIsInternal(true);
        classScheme.setNodeType(BindingUtility.CANONICAL_NODE_TYPE_ID_UniqueCode);
        classScheme.setDescription(BindingUtility.getInstance().getDescription("This is the classification scheme for UNSPSC"));
        classScheme.setName(BindingUtility.getInstance().getName("UNSPSC"));

        ClassificationNode parentSegment = null;
        ClassificationNode parentFamily = null;
        ClassificationNode parentClass = null;
        ClassificationNode parentCommodity = null;
        ClassificationNode businessFunction = null;

        int count = 0;

        while (true) {
            if (((maxNumOfEntries != 0) && (count > maxNumOfEntries)) ||
                    (tokenizer.nextToken() == StreamTokenizer.TT_EOF)) {
                break;
            }

            int code = 0;
            String name = null;
            code = (int) tokenizer.nval;

            //System.out.println("a number:" + (int)tokenizer.nval);
            tokenizer.nextToken();
            name = tokenizer.sval;

            //System.out.println(tokenizer.sval);
            // Create the classification node by the type
            String id = uUIDFactory.newUUID().toString();

            if (getType(code) == SEGMENT) {
                parentSegment = BindingUtility.getInstance().rimFac.createClassificationNode();
                parentSegment.setId("urn:uuid:" + id);
                classScheme.getClassificationNode().add(parentSegment);

                // code attribute 
                parentSegment.setCode(code + "");

                // name 
                parentSegment.setName(BindingUtility.getInstance().getName(name));
            } else if (getType(code) == FAMILY) {
                parentFamily = BindingUtility.getInstance().rimFac.createClassificationNode();
                parentFamily.setId("urn:uuid:" + id);
                parentSegment.getClassificationNode().add(parentFamily);

                // code attribute 
                parentFamily.setCode(code + "");

                // name 
                parentFamily.setName(BindingUtility.getInstance().getName(name));
            } else if (getType(code) == CLASS) {
                parentClass = BindingUtility.getInstance().rimFac.createClassificationNode();
                parentClass.setId("urn:uuid:" + id);
                parentFamily.getClassificationNode().add(parentClass);

                // code attribute 
                parentClass.setCode(code + "");

                // name 
                parentClass.setName(BindingUtility.getInstance().getName(name));
            } else if (getType(code) == COMMODITY) {
                parentCommodity = BindingUtility.getInstance().rimFac.createClassificationNode();
                parentCommodity.setId("urn:uuid:" + id);
                parentClass.getClassificationNode().add(parentCommodity);

                // code attribute 
                parentCommodity.setCode(code + "");

                // name 
                parentCommodity.setName(BindingUtility.getInstance().getName(name));
            } else if (getType(code) == BUSINESS_FUNCTION) {
                businessFunction = BindingUtility.getInstance().rimFac.createClassificationNode();
                businessFunction.setId("urn:uuid:" + id);
                parentCommodity.getClassificationNode().add(businessFunction);

                // code attribute 
                businessFunction.setCode(code + "");

                // name 
                businessFunction.setName(BindingUtility.getInstance().getName(name));
            }

            count++;
        }

        return classScheme;
    }

    /**
    *         Get the type (i.e. Segment, Family, etc.)
    */
    private int getType(int code) {
        String codeStr = code + "";

        if (!codeStr.substring(0, 2).equals("00") &&
                codeStr.substring(2, 4).equals("00")) {
            return SEGMENT;
        } else if (!codeStr.substring(2, 4).equals("00") &&
                codeStr.substring(4, 6).equals("00")) {
            return FAMILY;
        } else if (!codeStr.substring(4, 6).equals("00") &&
                codeStr.substring(6, 8).equals("00")) {
            return CLASS;
        } else if (!codeStr.substring(6, 8).equals("00") &&
                ((codeStr.length() <= 8) ||
                codeStr.substring(8, 10).equals("00"))) {
            return COMMODITY;
        } else if ((codeStr.length() > 8) &&
                !codeStr.substring(8, 10).equals("00")) {
            return BUSINESS_FUNCTION;
        } else {
            throw new RuntimeException(ServerResourceBundle.getInstance().getString("message.unknownTypeFound"));
        }
    }
}
