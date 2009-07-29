/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nhinc.wsn.entities.filters;

import org.oasis_open.docs.wsn.b_2.Notify;
import org.oasis_open.docs.wsn.b_2.Subscribe;
import org.oasis_open.docs.wsn.b_2.TopicExpressionType;

/**
 *
 * @author rayj
 */
public class SimpleTopicFilter implements IFilterCheck {

    private TopicExpressionType _topicExpression;
    private String _topic = null;
    private String _dialect = null;

    public SimpleTopicFilter(TopicExpressionType topicExpression) {
        this._topicExpression = topicExpression;
        this._dialect = topicExpression.getDialect();
        this._topic = extractTopicValue(topicExpression);
    //todo: nullcheck on both _topic and _dialect
    }

    public boolean MeetsCriteria(Notify notify) {
        boolean result = false;
        TopicExpressionType notifyTopic = extractTopic(notify);
        String notifyTopicValue = extractTopicValue(notifyTopic);

        if (_topic != null) {
            result = _topic.contentEquals(notifyTopicValue);
        }
        return result;
    }

    private TopicExpressionType extractTopic(Notify notify) {
        return notify.getNotificationMessage().get(0).getTopic();
    }

    private String extractTopicValue(TopicExpressionType topic) {
        String topicValue = null;

        if (topic != null) {
            for (Object content : topic.getContent()) {
                if (content instanceof String) {
                    topicValue = (String) content;
                }
            }
        }
        return topicValue;
    }
}
