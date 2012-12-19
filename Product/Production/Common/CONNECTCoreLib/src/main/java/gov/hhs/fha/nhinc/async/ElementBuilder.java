/**
 *
 */
package gov.hhs.fha.nhinc.async;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author bhumphrey
 *
 */
public final class ElementBuilder {

	private static final Logger LOG = Logger.getLogger(ElementBuilder.class);

	private Document document;


	private ElementBuilder() {
		try {
			document = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder().newDocument();
		} catch (ParserConfigurationException e) {
			LOG.error("unable to create document " + e.getMessage());
		}
	}

	/**
	 * @param ns - The Namespace
	 * @param name - The Name
	 * @return built element
	 */
	public Element buildElement(final String ns, final String name) {

		return buildElement(ns, name, null, null);
	}

	/**
	 * @param ns Namespace
	 * @param name Name
	 * @param content Content
	 * @return built element
	 */
	public Element buildElement(final String ns, final String name, final String content) {

		return buildElement(ns, name, content,  null);
	}

	/**
	 * @param ns Namespace
	 * @param name Name
	 * @param content Content
	 * @param mustUnderstand mustUnderstand, true or false
	 * @return built element
	 */
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

	/**
	 * @return A new instance of ElementBuilder.
	 */
	public static ElementBuilder newInstance() {
		return new ElementBuilder();
	}

}
