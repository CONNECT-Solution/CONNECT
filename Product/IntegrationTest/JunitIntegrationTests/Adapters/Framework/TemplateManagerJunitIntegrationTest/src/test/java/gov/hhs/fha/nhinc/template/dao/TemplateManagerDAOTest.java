/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.template.dao;

import gov.hhs.fha.nhinc.template.TemplateManager;
import gov.hhs.fha.nhinc.template.model.CdaTemplate;
import gov.hhs.fha.nhinc.template.model.DocSection;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author kim
 */
public class TemplateManagerDAOTest {

    public TemplateManagerDAOTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getTemplates method, of class TemplateManagerDAO.
     */
    @Test
    public void testGetTemplates() {
        System.out.println("getTemplates");
        //String docType = "34133-9";
        TemplateManager instance = TemplateManagerDAO.getInstance();
        if (instance != null) {
            List<CdaTemplate> results = instance.getTemplates();
            System.out.println(results.size() + " template found=" + results.size());
            CdaTemplate docTemplate = null;
            for (CdaTemplate t : results) {
                System.out.println("\n" + t);
            }
        }
    }

    /**
     * Test of getSectionTemplatesForSection method, of class TemplateManagerDAO.
     */
    @Test
    public void testGetSectionTemplatesForSection() {
        System.out.println("getSectionTemplatesForSection");
        String docType = "34133-9";
        TemplateManager instance = TemplateManagerDAO.getInstance();
        List<CdaTemplate> results = instance.getSectionTemplatesForDocument(docType, true);
        System.out.println(results.size() + " section(s) found=" + results.size());
        for (CdaTemplate t : results) {
            System.out.println("\n" + t);
        }
    }

    /**
     * Test of getModuleTemplatesForSection method, of class TemplateManagerDAO.
     */
    @Test
    public void testGetModuleTemplatesForSection() {
        System.out.println("getModuleTemplatesForSection");
        int sectionId = 28; // medications
        TemplateManager instance = TemplateManagerDAO.getInstance();
        List<CdaTemplate> results = instance.getModuleTemplatesForSection(sectionId, true);
        System.out.println(results.size() + " modules(s) found=" + results.size());
        for (CdaTemplate t : results) {
            System.out.println("\n" + t);
        }
    }

    /**
     * Test of getTemplateForDocument method, of class TemplateManagerDAO.
     */
    @Test
    public void testGetTemplateForDocument() {
        System.out.println("getTemplateForDocument");
        String docType = "34133-9";
        TemplateManager instance = TemplateManagerDAO.getInstance();
        CdaTemplate result = instance.getTemplateForDocument(docType);
        System.out.println("\n" + result);
    }
}
