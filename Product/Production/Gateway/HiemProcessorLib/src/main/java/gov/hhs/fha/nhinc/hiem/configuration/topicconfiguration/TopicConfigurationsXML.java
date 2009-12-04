/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.hiem.configuration.topicconfiguration;

import com.thoughtworks.xstream.XStream;

/**
 * This class is used to serialize/deserialize to/from XML using XStream.
 *
 * @author Jon Hoppesch
 */
public class TopicConfigurationsXML {

        /**
     * This method serializes an InternalConnectionInfos object to an
     * XML string.
     *
     * @param oInternalConnectionInfos The object to be serialized.
     * @return The XML string representation of the object.
     */
    public static String serialize(TopicConfigurations topicConfigurations)
    {
        String sXML = "";

        XStream oXStream = new XStream();
        oXStream.alias("TopicConfigurations", TopicConfigurations.class);
        oXStream.addImplicitCollection(TopicConfigurations.class, "topicConfigurations");
        oXStream.alias("TopicConfiguration", TopicConfigurationEntry.class);
        oXStream.processAnnotations(TopicConfigurations.class);
        sXML = oXStream.toXML(topicConfigurations);

        return sXML;
    }

    /**
     * This method takes an XML representation of CMInternalConnectionInfos and
     * produces an instance of the object.
     *
     * @param sXML The serialized representation of the TopicConfigurations object.
     * @return The object instance of the XML.
     */
    public static TopicConfigurations deserialize(String sXML)
    {
        TopicConfigurations topicConfigurations = new TopicConfigurations();

        XStream oXStream = new XStream();
        oXStream.alias("topicConfigurations", TopicConfigurations.class);
        oXStream.addImplicitCollection(TopicConfigurations.class, "topicConfigurations");
        oXStream.alias("topicConfiguration", TopicConfigurationEntry.class);
        oXStream.processAnnotations(TopicConfigurations.class);
        Object oObject = oXStream.fromXML(sXML);
        if (oObject instanceof TopicConfigurations)
        {
            topicConfigurations = (TopicConfigurations) oObject;
        }

        return topicConfigurations;
    }

}
