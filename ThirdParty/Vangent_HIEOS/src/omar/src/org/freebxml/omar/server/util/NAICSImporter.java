/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/util/NAICSImporter.java,v 1.7 2005/02/23 23:15:42 farrukh_najmi Exp $
 * ====================================================================
 */
package org.freebxml.omar.server.util;

import org.freebxml.omar.common.BindingUtility;
import javax.xml.registry.JAXRException;
import org.freebxml.omar.common.UUIDFactory;

import org.oasis.ebxml.registry.bindings.rim.ClassificationNode;
import org.oasis.ebxml.registry.bindings.rim.ClassificationScheme;

import java.io.FileReader;
import java.io.IOException;
import java.io.StreamTokenizer;

import java.util.StringTokenizer;

import javax.xml.bind.JAXBException;


/**
 * It is the importer for NAICS taxonomy. It expects the input file is of
 * version 2002. It will automatically skip the first four lines in the input file.
 *
 * @author Adrian Chong
*/
public class NAICSImporter {
    private static final int SECTOR = 2;
    private static final int SUBSECTOR = 3;
    private static final int INDUSTRY_GROUP = 4;
    private static final int INDUSTRY = 5;
    private static final int NATIONAL = 6;
    private StreamTokenizer tokenizer;
    private int maxNumOfEntries;
    private UUIDFactory uUIDFactory;

    /**
    *         Constructor.
    *         @param fileName The file path and name of the NAICS taxonomy file.
    *         @param uUIDFactory The UUIDFactory
    */
    public NAICSImporter(String fileName, UUIDFactory uUIDFactory)
        throws IOException {
        setFile(fileName);
        tokenizer.eolIsSignificant(false);
        tokenizer.slashSlashComments(true);
        tokenizer.slashStarComments(true);
        tokenizer.wordChars(' ', ' ');
        tokenizer.wordChars(',', ',');
        this.uUIDFactory = uUIDFactory;
    }

    /**
    *         Set the file path of the ISO3166 taxonomy file.
    *         @param fileName The file path of the NAICS taxonomy file
        */
    public void setFile(String fileName) throws IOException {
        tokenizer = new StreamTokenizer(new FileReader(fileName));
        tokenizer.eolIsSignificant(true);

        int lineCount = 0;

        while ((tokenizer.nextToken() != StreamTokenizer.TT_EOF) &&
                (lineCount < 3)) {
            if (tokenizer.ttype == StreamTokenizer.TT_EOL) {
                lineCount++;
            }
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
        classScheme.setDescription(BindingUtility.getInstance().getDescription("This is the classification scheme for NAICS version 2002"));
        classScheme.setName(BindingUtility.getInstance().getName("ntis-gov:naics"));

        ClassificationNode parentSector = null;
        ClassificationNode parentSubSector = null;
        ClassificationNode parentIndustryGroup = null;
        ClassificationNode parentIndustry = null;
        ClassificationNode national = null;

        int count = 0;

        while (true) {
            if (((maxNumOfEntries != 0) && (count > maxNumOfEntries)) ||
                    (tokenizer.nextToken() == StreamTokenizer.TT_EOF)) {
                break;
            }

            // code
            String code = "";

            if (tokenizer.ttype == StreamTokenizer.TT_NUMBER) {
                code = ((int) tokenizer.nval) + "";
            }

            // It is to check whether the code is a range  
            tokenizer.nextToken();

            if (tokenizer.ttype == StreamTokenizer.TT_NUMBER) {
                code += (int) tokenizer.nval;
                tokenizer.nextToken();
            }

            //System.out.println(code);
            //System.out.println(getType(code));
            String name = tokenizer.sval;

            //System.out.println(name);
            String id = "urn:uuid:" + uUIDFactory.newUUID().toString();

            if (getType(code) == SECTOR) {
                parentSector = BindingUtility.getInstance().rimFac.createClassificationNode();
                parentSector.setId(id);
                parentSector.setName(BindingUtility.getInstance().getName(name));
                parentSector.setCode(code);
                classScheme.getClassificationNode().add(parentSector);
            } else if (getType(code) == SUBSECTOR) {
                parentSubSector = BindingUtility.getInstance().rimFac.createClassificationNode();
                parentSubSector.setId(id);
                parentSubSector.setName(BindingUtility.getInstance().getName(name));
                parentSubSector.setCode(code);
                parentSector.getClassificationNode().add(parentSubSector);
            } else if (getType(code) == INDUSTRY_GROUP) {
                parentIndustryGroup = BindingUtility.getInstance().rimFac.createClassificationNode();
                parentIndustryGroup.setId(id);
                parentIndustryGroup.setName(BindingUtility.getInstance()
                                                          .getName(name));
                parentIndustryGroup.setCode(code);
                parentSubSector.getClassificationNode().add(parentIndustryGroup);
            } else if (getType(code) == INDUSTRY) {
                parentIndustry = BindingUtility.getInstance().rimFac.createClassificationNode();
                parentIndustry.setId(id);
                parentIndustry.setName(BindingUtility.getInstance().getName(name));
                parentIndustry.setCode(code);
                parentIndustryGroup.getClassificationNode().add(parentIndustry);
            } else if (getType(code) == NATIONAL) {
                national = BindingUtility.getInstance().rimFac.createClassificationNode();
                national.setId(id);
                national.setName(BindingUtility.getInstance().getName(name));
                national.setCode(code);
                parentIndustry.getClassificationNode().add(national);
            }

            count++;
        }

        return classScheme;
    }

    /**
    *         Get the type (i.e. Sector, SubSector, etc.) by inspecting the code length.
    *         A code range 's type is specified by the number of digits.
    */
    private int getType(String code) {
        if (code.indexOf('-') == -1) {
            return code.length();
        } else {
            StringTokenizer sTokenizer = new StringTokenizer(code);

            return sTokenizer.nextToken("-").length();
        }
    }
}
