/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/common/I18nXSLTTask.java,v 1.2 2005/05/11 06:44:43 doballve Exp $
 * ====================================================================
 */
package org.freebxml.omar.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * A helper class to localize the XML files containing rim:LocalizedString
 * templates. This class can be used to call the XSLT to localize a single
 * file or an entire directory. Furthermore, the static getLocalizedString
 * method is used by the XSLT to obtain Strings from a ResourceBundle.
 *
 * //TODO: make it an Ant task ?
 * //TODO: take target locales as parameter (now hardcoded to XSL)
 * //TODO: support suffix for output files
 *
 * @author Diego Ballve / Digital Artefacts
 */
public class I18nXSLTTask {
    
    /**
     * Static method to be called from XSLT. Get the ResourceBundle for
     * 'bundleBaseName', then calls getString with 'key' and 'locale'.
     * In case of missing resource / bundle, return String "'locale'('key')".
     *
     * @param bundleBaseName The ResourceBundle base name.
     * @param key The key for the desired String.
     * @param locale The locale for which a resource bundle is desired.
     * @return Localized String for 'key' in 'locale', from 'bundleBaseName'
     */
    public static String getLocalizedString(String bundleBaseName, String locale, String key) {
        try {
            //System.out.println("Getting " + locale + "(" + key + ") from bundle " + bundleBaseName);
            // get bundle
            ResourceBundle bundle = ResourceBundle.getBundle(bundleBaseName,
                    AbstractResourceBundle.parseLocale(locale));
            // get string
            return bundle.getString(key);
        } catch (MissingResourceException e) {
            // swallow error and return locale(key)
            System.err.println(e);
            return locale + "(" + key + ")";
        }
    }
    
    public static String toXMLLangString(String localeString) {
        Locale locale = AbstractResourceBundle.parseLocale(localeString);
        String xmlLang = locale.getLanguage();
        if (locale.getCountry() != null && !"".equals(locale.getCountry())) {
            xmlLang += "-" + locale.getCountry();
        }
        // warning: this will produce invalid XML
        if (locale.getVariant() != null && !"".equals(locale.getVariant())) {
            xmlLang += "-" + locale.getVariant();
        }
        return xmlLang;
    }
    
    private static void printUsage() {
        System.out.println("\nI18nXSLTTask Usage: \njava I18nXSLTTask (-sourceFile <file> -destFile <file> | -sourceDir <dir> -destDir <dir>) -xsltFile <file> -baseBundleName <baseName> -localeList <locales>");
        System.out.println("Use ':' as saparator for localeList, no spaces.\n");
        System.exit(0);        
    }
    
    /**
     * Perform localization on a single file or an entire directory (XML files)
     *
     * For usage, try: java I18nXSLTTask -help
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        File sourceFile = null;
        File destFile = null;
        File sourceDir = null;
        File destDir = null;
        File xsltFile = null;
        String bundleBaseName = null;
        String localeList = null;
        
        for (int i = 0; i < args.length; i++) {
            if (args[i].equalsIgnoreCase("-help")) {
                printUsage();
            } else if (args[i].equalsIgnoreCase("-sourceFile")) {
                sourceFile = new File(args[++i]);
            } else if (args[i].startsWith("-destFile")) {
                destFile = new File(args[++i]);
            } else if (args[i].startsWith("-sourceDir")) {
                sourceDir = new File(args[++i]);
            } else if (args[i].startsWith("-destDir")) {
                destDir = new File(args[++i]);
            } else if (args[i].startsWith("-xsltFile")) {
                xsltFile = new File(args[++i]);
            } else if (args[i].startsWith("-bundleBaseName")) {
                bundleBaseName = args[++i];
            } else if (args[i].startsWith("-localeList")) {
                localeList = args[++i];
            } else {
                System.err.println("Unknown parameter: '" + args[i] +
                        "' at position " + i);
                if (i > 0) {
                    System.err.println("Last valid parameter was '" +
                            args[i - 1] + "'");
                }
                printUsage();
            }
        }
        
        if (!((sourceFile != null && destFile != null)
        || (sourceDir != null && destDir != null))) {
            System.err.println("Error: Missing sourceFile + destFile or sourceDir + destDir.");
            printUsage();
        }
        if (xsltFile == null) {
            System.err.println("Error: Missing xsltFile.");
            printUsage();
        }
        if (bundleBaseName == null) {
            System.err.println("Error: Missing bundleBaseName.");
            printUsage();
        }
        
        FileInputStream stylesheetFis = null;
        try {
            File inputs[];
            File outputs[];
            
            if ((sourceFile != null && destFile != null)) {
                inputs = new File[1];
                outputs = new File[1];
                inputs[0] = sourceFile;
                outputs[0] = destFile;
            } else {
                final File finalSrcDir = sourceDir;
                inputs = sourceDir.listFiles(new FilenameFilter() {
                    public boolean accept(File dir, String name) {
                        return dir.equals(finalSrcDir) && name.endsWith(".xml");
                    }
                });

                outputs = new File[inputs.length];
                for (int i = 0; i < inputs.length; i++) {
                    outputs[i] = new File(destDir, inputs[i].getName());
                }
            }

            stylesheetFis = new FileInputStream(xsltFile);
            StreamSource stylesheet = new StreamSource(stylesheetFis);
            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer xFormer = tFactory.newTransformer(stylesheet);
            
            for (int i = 0; i < inputs.length; i++) {
                FileInputStream fis = null;
                FileOutputStream fos = null;
                try {
                    xFormer.setParameter("bundleBaseName", bundleBaseName);
                    if (localeList != null) {
                        xFormer.setParameter("localeList", localeList);
                    }
                    fis = new FileInputStream(inputs[i]);
                    StreamSource source = new StreamSource(fis);
                    fos = new FileOutputStream(outputs[i]);
                    StreamResult result = new StreamResult(fos);
                    xFormer.transform(source, result);
                    xFormer.clearParameters();
                } finally {
                    if (fis != null) {
                        fis.close();
                    }
                    if (fos != null) {
                        fos.close();
                    }
                }
            }            
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        } finally {
            if (stylesheetFis != null) {
                stylesheetFis.close();
            }
        }
    }    
}
