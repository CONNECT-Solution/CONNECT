/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.common.ftaconfigmanager;
import java.util.List;
/**
 *
 * @author dunnek
 */
public class FTAChannel
{
    private String topic = "";
    private String type = "";
    private String location = "";
    private List<FTAElement> elements;

    public String getTopic()
    {
        return topic;
    }
    public String getType()
    {
        return type;
    }
    public String getLocation()
    {
            return location;
    }
    public List<FTAElement> getAdditionalElements()
    {
        return elements;
    }
    public void setTopic(String value)
    {
        topic = value;
    }
    public void setType(String value)
    {
        type = value;
    }
    public void setLocation(String value)
    {
        location = value;
    }
    public void setAdditionalElements(List<FTAElement> value)
    {
        elements = value;
    }
}
