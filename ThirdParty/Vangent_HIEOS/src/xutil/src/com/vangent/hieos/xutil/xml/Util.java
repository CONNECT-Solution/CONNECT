/*
 * This code is subject to the HIEOS License, Version 1.0
 *
 * Copyright(c) 2008-2009 Vangent, Inc.  All rights reserved.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.vangent.hieos.xutil.xml;

import com.vangent.hieos.xutil.exception.XdsInternalException;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;

public class Util {
	
	
	public static OMElement parse_xml(Object o) throws FactoryConfigurationError, XdsInternalException {
		if (o instanceof String) return parse_xml((String) o);
		if (o instanceof InputStream) return parse_xml((InputStream) o);
		if (o instanceof File) return parse_xml((File) o);
		throw new XdsInternalException("Util.parse_xml(): do not understand input format " + o.getClass().getName());
	}

	public static OMElement parse_xml(InputStream is) throws FactoryConfigurationError, XdsInternalException {

//		create the parser
		XMLStreamReader parser=null;

		try {
			parser = XMLInputFactory.newInstance().createXMLStreamReader(is);
		} catch (XMLStreamException e) {
			throw new XdsInternalException("Could not create XMLStreamReader from InputStream");
		} 

//		create the builder
		StAXOMBuilder builder = new StAXOMBuilder(parser);

//		get the root element (in this case the envelope)
		OMElement documentElement =  builder.getDocumentElement();	
		if (documentElement == null)
			throw new XdsInternalException("No document element");
		return documentElement;
	}

	public static OMElement parse_xml(File infile) throws FactoryConfigurationError, XdsInternalException {

//		create the parser
		XMLStreamReader parser=null;

		try {
			parser = XMLInputFactory.newInstance().createXMLStreamReader(new FileInputStream(infile));
		} catch (XMLStreamException e) {
			throw new XdsInternalException("Could not create XMLStreamReader from " + infile.getName());
		} catch (FileNotFoundException e) {
			throw new XdsInternalException("Could not find input file " + infile.getAbsolutePath());
		}

//		create the builder
		StAXOMBuilder builder = new StAXOMBuilder(parser);

//		get the root element (in this case the envelope)
		OMElement documentElement =  builder.getDocumentElement();	
		if (documentElement == null)
			throw new XdsInternalException("No document element");
		return documentElement;
	}

	public static OMElement parse_xml(String input) throws FactoryConfigurationError, XdsInternalException {

//		create the parser
		XMLStreamReader parser=null;

		try {
			parser = XMLInputFactory.newInstance().createXMLStreamReader(new ByteArrayInputStream(input.getBytes()));
		} catch (Exception e) {
			throw new XdsInternalException("Could not create XMLStreamReader from string: " + input.substring(0, 100) + "...");
		} 

//		create the builder
		StAXOMBuilder builder = null;
		try {
			 builder = new StAXOMBuilder(parser);
		} catch (Exception e) {
			throw new XdsInternalException("Util.parse_xml(): Could not create StAXOMBuilder from parser");
		} 

		OMElement documentElement = null; 
		try {
//			get the root element (in this case the envelope)
			documentElement =  builder.getDocumentElement();	
			if (documentElement == null)
				throw new XdsInternalException("No document element");
		} catch (Exception e) {
			throw new XdsInternalException("Could not create XMLStreamReader (in Util.parse_xml()) from string: " + 
					input.substring(0, (input.length() < 100) ? input.length() : 100) + 
					"...");
		}
		return documentElement;
	}

	public static OMElement deep_copy(OMElement in) throws XdsInternalException {
		if (in == null)
			return null;
		return parse_xml(in.toString());
	}


}
