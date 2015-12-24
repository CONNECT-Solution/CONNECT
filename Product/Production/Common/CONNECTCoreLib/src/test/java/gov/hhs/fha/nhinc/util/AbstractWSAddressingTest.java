/*
 * Copyright (c) 2009-2015, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.util;

import static org.junit.Assert.assertEquals;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.transform.stream.StreamSource;

import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XQueryCompiler;
import net.sf.saxon.s9api.XQueryEvaluator;
import net.sf.saxon.s9api.XQueryExecutable;
import net.sf.saxon.s9api.XdmItem;
import net.sf.saxon.s9api.XdmNode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

public abstract class AbstractWSAddressingTest {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractWSAddressingTest.class);

    private static Processor proc;
    private static XQueryExecutable BINDING_LEVEL_ACTION;
    private static XQueryExecutable BINDING_LEVEL_OPERATION;
    private static XQueryExecutable PORT_LEVEL_ACTION;
    private static XQueryExecutable PORT_LEVEL_OPERATION;

    @BeforeClass
    public static final void beforeClass() throws Exception {
        proc = new Processor(false);
        XQueryCompiler comp = proc.newXQueryCompiler();

        String bindingLevelBaseXPath = "//*[local-name()='operation']/*[@soapAction]/";
        BINDING_LEVEL_ACTION = comp.compile(bindingLevelBaseXPath + "@soapAction");
        BINDING_LEVEL_OPERATION = comp.compile(bindingLevelBaseXPath + "../@name");

        String portLevelBaseXPath = "//*[local-name()='portType']/*[local-name()='operation']/*[local-name()='input']/";
        PORT_LEVEL_ACTION = comp.compile(portLevelBaseXPath
                + "@*[namespace-uri()='http://www.w3.org/2006/05/addressing/wsdl']");
        PORT_LEVEL_OPERATION = comp.compile(portLevelBaseXPath + "../@name");
    }

    @Test
    public void classpathWSDLs() throws Exception {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources("classpath*:/wsdl/**/*.wsdl");
        LOG.info("Testing " + resources.length + " resources.");
        for (Resource r : resources) {
            LOG.info("Testing " + r);
            assertInputStream(r.getInputStream());
        }
    }

    private static void assertInputStream(InputStream input) throws SaxonApiException {
        XdmNode source = proc.newDocumentBuilder().build(new StreamSource(input));

        Map<String, String> bindingMap = createMap(source, BINDING_LEVEL_ACTION.load(), BINDING_LEVEL_OPERATION.load());
        Map<String, String> portMap = createMap(source, PORT_LEVEL_ACTION.load(), PORT_LEVEL_OPERATION.load());

        for (String bindingKey : bindingMap.keySet()) {
            if (portMap.containsKey(bindingKey)) {
                assertEquals("Input: " + input + " for key=" + bindingKey, bindingMap.get(bindingKey),
                        portMap.get(bindingKey));
            }
        }
    }

    private static Map<String, String> createMap(XdmNode source, XQueryEvaluator actionEvaluator,
            XQueryEvaluator operationEvaluator) {
        actionEvaluator.setContextItem(source);
        operationEvaluator.setContextItem(source);
        Iterator<XdmItem> operationEvaluatorIterator = operationEvaluator.iterator();
        Map<String, String> result = new HashMap<>();
        for (XdmItem item : actionEvaluator) {
            String actionValue = item.getStringValue();
            String operationValue = operationEvaluatorIterator.next().getStringValue();
            result.put(operationValue, actionValue);
        }
        return result;
    }
}
