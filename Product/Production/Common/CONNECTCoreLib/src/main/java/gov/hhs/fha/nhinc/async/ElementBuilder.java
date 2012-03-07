/**
 * 
 */
package gov.hhs.fha.nhinc.async;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author bhumphrey
 * 
 */
public class ElementBuilder {

	private Log log = LogFactory.getLog(ElementBuilder.class);

	private Document document;

	
	private ElementBuilder() {
		try {
			document = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder().newDocument();
		} catch (ParserConfigurationException e) {
			log.error("unable to create document " + e.getMessage());
		}
	}

	public Element buildElement(final String ns, final String name) {

		return buildElement(ns, name, null, null);
	}
	
	public Element buildElement(final String ns, final String name, final String content) {

		return buildElement(ns, name, content,  null);
	}
	
	public Element buildElement(final String ns, final String name, final String content, Boolean mustUnderstand) {

		Element theElement = null;
		theElement = document.createElementNS(ns, name);
		
		if (content != null) {
			theElement.setTextContent(content);
		}
		
		if (mustUnderstand != null) {
			theElement.setAttribute("mustUnderstand", mustUnderstand.toString());
		}
		return theElement;
	}

	public static ElementBuilder newInstance() {
		return new ElementBuilder();
	}

}
