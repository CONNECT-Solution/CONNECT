/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.subscription.repository.data;

/**
 * Data object for a subscription filter topic expression
 * 
 * @author Neil Webb
 */
public class TopicExpression
{
    private String dialect;
    private String topicExpressionValue;

    public String getTopicExpressionValue()
    {
        return topicExpressionValue;
    }

    public void setTopicExpressionValue(String topicExpressionValue)
    {
        this.topicExpressionValue = topicExpressionValue;
    }

    public String getDialect()
    {
        return dialect;
    }

    public void setDialect(String dialect)
    {
        this.dialect = dialect;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final TopicExpression other = (TopicExpression) obj;
        if (this.dialect != other.dialect && (this.dialect == null || !this.dialect.equals(other.dialect)))
        {
            return false;
        }
        if (this.topicExpressionValue != other.topicExpressionValue && (this.topicExpressionValue == null || !this.topicExpressionValue.equals(other.topicExpressionValue)))
        {
            return false;
        }
        return true;
    }

}
