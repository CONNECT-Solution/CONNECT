/*
 * Copyright (c) 2009-2019, United States Government, as represented by the Secretary of Health and Human Services.
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

import gov.hhs.fha.nhinc.messaging.service.port.SOAP12ServicePortDescriptor;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.transform.stream.StreamSource;
import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XQueryCompiler;
import net.sf.saxon.s9api.XQueryEvaluator;
import net.sf.saxon.s9api.XQueryExecutable;
import net.sf.saxon.s9api.XdmItem;
import net.sf.saxon.s9api.XdmNode;
import org.apache.commons.collections.CollectionUtils;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

public abstract class AbstractWSAddressingTest {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractWSAddressingTest.class);

    private static Processor proc;
    private static XQueryExecutable BINDING_LEVEL_ACTION;
    private static XQueryExecutable BINDING_LEVEL_OPERATION;
    private static XQueryExecutable PORT_LEVEL_ACTION;
    private static XQueryExecutable PORT_LEVEL_OPERATION;
    private static XQueryExecutable PORT_TYPE_NAME;

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
        PORT_TYPE_NAME = comp.compile(portLevelBaseXPath + "../../@name");
    }

    protected abstract String getPackageName();

    @Test
    public void classpathWSDLs() throws Exception {
        Map<String, String> portTypeActionMap = buildPortTypeAndActionMapFromPortDescriptor(findPortDescriptorClasses());
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources("classpath*:/wsdl/**/*.wsdl");
        LOG.info("Testing " + resources.length + " resources.");
        for (Resource r : resources) {
            LOG.info("Testing " + r);
            assertInputStream(r, portTypeActionMap);
        }
    }

    private static void assertInputStream(Resource wsdl, Map<String, String> portTypeActionMap) throws
        IOException, SaxonApiException {
        XdmNode source = proc.newDocumentBuilder().build(new StreamSource(wsdl.getInputStream()));
        XQueryEvaluator bindingLevelAction = BINDING_LEVEL_ACTION.load();
        Map<String, String> bindingMap = createMap(source, bindingLevelAction, BINDING_LEVEL_OPERATION.load());
        Map<String, String> portMap = createMap(source, PORT_LEVEL_ACTION.load(), PORT_LEVEL_OPERATION.load());
        Map<String, List<String>> portTypeMapFromWSDL = createPortTypeAndActionMap(source, PORT_TYPE_NAME.load(),
            bindingLevelAction);
        for (String bindingKey : bindingMap.keySet()) {
            if (portMap.containsKey(bindingKey)) {
                assertEquals("Input: "
                    + wsdl + " for key=" + bindingKey, bindingMap.get(bindingKey), portMap.get(bindingKey));
            }
        }
        assertSoapAction(wsdl, portTypeMapFromWSDL, portTypeActionMap);
    }

    /*
     * This method takes in a wsdl, a map of portType and a list of soapOperations defined in the wsdl and a
     * ServicePortDescriptor class defined for that wsdl. The method loops through the portTypes defined in the wsdl and
     * check if the portType is present in the ServicePortDescriptor map. If a match is found,
     * retrieveActionFromWsdlMap() is called to loop through all the soapOperations defined for that portType in wsdl to
     * find an exact match.
     */
    private static void assertSoapAction(Resource wsdl, Map<String, List<String>> portTypeFromWsdlMap,
        Map<String, String> portTypeActionMap) {
        assertNotNull("No PortType/soapAction defined in WSDL: " + wsdl, portTypeFromWsdlMap);
        assertFalse("No soapAction defined in WSDL: " + wsdl, portTypeFromWsdlMap.isEmpty());
        for (String key : portTypeFromWsdlMap.keySet()) {
            if (portTypeActionMap.containsKey(key)) {
                retrieveActionFromWsdlMap(wsdl, portTypeActionMap.get(key), portTypeFromWsdlMap.get(key));
            }
        }
    }

    private static void retrieveActionFromWsdlMap(Resource wsdl, String wsActionInPortDesc,
        List<String> portTypeFromWsdlMap) {
        if (CollectionUtils.isNotEmpty(portTypeFromWsdlMap)) {
            for (String key : portTypeFromWsdlMap) {
                if (wsActionInPortDesc.equals(key)) {
                    return;
                }
            }
        }
        assertEquals("ServicePortDescriptor Action and WSDL soapAction do not match for WSDL: " + wsdl,
            wsActionInPortDesc, null);
    }

    private static Map<String, String> createMap(XdmNode source, XQueryEvaluator actionEvaluator,
        XQueryEvaluator operationEvaluator) {
        actionEvaluator.setContextItem(source);
        operationEvaluator.setContextItem(source);
        Iterator<XdmItem> operationEvaluatorIterator = operationEvaluator.iterator();
        Map<String, String> result = new HashMap<>();
        for (XdmItem item : actionEvaluator) {
            String actionValue = item.getStringValue();
            XdmItem obj = operationEvaluatorIterator.next();
            String operationValue = obj.getStringValue();
            result.put(operationValue, actionValue);
        }
        return result;
    }

    private static Map<String, List<String>> createPortTypeAndActionMap(XdmNode source, XQueryEvaluator portEvaluator,
        XQueryEvaluator soapActionEvaluator) {
        portEvaluator.setContextItem(source);
        soapActionEvaluator.setContextItem(source);
        String portType = portEvaluator.iterator().next().getStringValue();
        Map<String, List<String>> result = new HashMap<>();
        List<String> actionList;
        for (XdmItem item : soapActionEvaluator) {
            String operationValue = item.getStringValue();
            if (result.containsKey(portType)) {
                actionList = result.get(portType);
            } else {
                actionList = new ArrayList<>();
            }
            actionList.add(operationValue);
            result.put(portType, actionList);
        }
        return result;
    }

    private static Method getWSAddressingActionMethod(Method[] methods) {
        for (Method m : methods) {
            if (m.getName().equals("getWSAddressingAction")) {
                return m;
            }
        }
        return null;
    }

    private static Method getPortClassMethod(Method[] methods) {
        for (Method m : methods) {
            if (m.getName().equals("getPortClass")) {
                return m;
            }
        }
        return null;
    }

    private List<Class<?>> findPortDescriptorClasses() {
        return ReflectionHelper.findClassesImpmenenting(SOAP12ServicePortDescriptor.class,
            "gov.hhs.fha.nhinc", getPackageName());
    }

    private Map<String, String> buildPortTypeAndActionMapFromPortDescriptor(List<Class<?>> portDecriptors) throws
        IllegalArgumentException, InstantiationException, IllegalAccessException,
        InvocationTargetException {
        Map<String, String> portTypeActionMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(portDecriptors)) {
            for (Class<?> clazz : portDecriptors) {
                Method[] methods = clazz.getMethods();
                Method m = getPortClassMethod(methods);
                Object obj = clazz.newInstance();
                if (null != m) {
                    Class<?> portTypeInClass = (Class<?>) m.invoke(obj);
                    Method addrMethod = getWSAddressingActionMethod(methods);
                    portTypeActionMap.put(portTypeInClass.getSimpleName(), (String) addrMethod.invoke(obj));
                }
            }
        }
        return portTypeActionMap;
    }
}
