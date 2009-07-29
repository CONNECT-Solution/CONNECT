/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.hiem.configuration.topicconfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jon Hoppesch
 */
public class TopicConfigurations {
    private List<TopicConfigurationEntry> topicConfigurations = new ArrayList<TopicConfigurationEntry>();

    /**
     * Default Constructor.
     */
    public TopicConfigurations () {
        clear();
    }

    /**
     * Clear the contents of this and set it to a default state.
     */
    public void clear() {
        topicConfigurations = new ArrayList<TopicConfigurationEntry>();
    }

    /**
     * This returns a list of TopicConfiguration objects.
     *
     * @return The list of topic configuration objects.
     */
    public List<TopicConfigurationEntry> getTopicConfigurations()
    {
        return topicConfigurations;
    }
}
