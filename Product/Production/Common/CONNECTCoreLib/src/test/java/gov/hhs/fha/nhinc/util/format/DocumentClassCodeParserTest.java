/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package gov.hhs.fha.nhinc.util.format;

import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import java.util.List;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author rayj
 */
public class DocumentClassCodeParserTest {

    public DocumentClassCodeParserTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void SingleItemFormattedTest1() {
        String input = "('1')";
        int expectedResultSize = 1;
        String expectedOutputList = "1";
        PerformTest(input, expectedResultSize, expectedOutputList);
    }

    @Test
    public void SingleItemNotFormattedTest() {
        String input = "1";
        int expectedResultSize = 1;
        String expectedOutputList = "1";
        PerformTest(input, expectedResultSize, expectedOutputList);
    }

    @Test
    public void MultipleItemsTest() {
        String input = "('1,2,3')";
        int expectedResultSize = 3;
        String expectedOutputList = "1,2,3";
        PerformTest(input, expectedResultSize, expectedOutputList);
    }

    @Test
    public void MultipleItemsTest2() {
        String input = "('aa,bb,cc')";
        int expectedResultSize = 3;
        String expectedOutputList = "aa,bb,cc";
        PerformTest(input, expectedResultSize, expectedOutputList);
    }

    @Test
    public void NullInput() {
        String input = null;
        int expectedResultSize = 0;
        String expectedOutputList = null;
        PerformTest(input, expectedResultSize, expectedOutputList);
    }

    @Test
    public void EmptyInput() {
        String input = "";
        int expectedResultSize = 0;
        String expectedOutputList = null;
        PerformTest(input, expectedResultSize, expectedOutputList);
    }

    private void PerformTest(String input, int expectedResultSize, String expectedResultList) {
        List<String> result = DocumentClassCodeParser.parseFormattedParameter(input);
        assertNotNull(result);
        assertEquals(expectedResultSize, result.size());
        String resultList = null;
        for (String item : result) {
            if (NullChecker.isNotNullish(item)) {
                if (resultList == null) {
                    resultList = item;
                } else {
                    resultList = resultList + "," + item;
                }
            }
        }

        assertEquals(expectedResultList, resultList);
    }
}