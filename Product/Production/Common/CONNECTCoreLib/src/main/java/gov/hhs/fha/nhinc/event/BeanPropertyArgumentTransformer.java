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
package gov.hhs.fha.nhinc.event;

import com.google.common.base.Optional;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

/**
 * Argument Transformer that transforms based upon bean properties of the argument list passed in.
 * <code>transformArguments</code> will call every bean getter on every argument in the list and assemble the
 * transformed arguments from those calls. <code>transformReturnValue</code> does a pass through.
 */
public abstract class BeanPropertyArgumentTransformer extends ArgTransformerEventDescriptionBuilder {

    private static final Logger LOG = LoggerFactory.getLogger(BeanPropertyArgumentTransformer.class);

    /**
     * Transforms the input argument list into the result by taking every argument, introspecting the bean properties,
     * and constructing an object array of all the resultant (non-null) gets.
     */
    @Override
    public final Object[] transformArguments(Object[] arguments) {
        if (arguments == null) {
            return new Object[] {};
        }
        List<Object> resultList = new ArrayList<>();
        for (Object argument : arguments) {
            resultList.addAll(transformSingleArgument(argument));
        }
        return resultList.toArray();
    }

    private List<Object> transformSingleArgument(Object argument) {
        if (argument == null) {
            return Collections.emptyList();
        }
        List<Object> result = new ArrayList<>();
        PropertyDescriptor[] propertyDescriptors = BeanUtils.getPropertyDescriptors(argument.getClass());
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            Method readMethod = propertyDescriptor.getReadMethod();
            if (readMethod == null) {
                continue;
            }
            Optional<Object> readValue = readValue(argument, readMethod);
            if (readValue.isPresent()) {
                result.add(readValue.get());
            }
        }
        return result;
    }

    private Optional<Object> readValue(Object argument, Method readMethod) {
        try {
            Object readValue = readMethod.invoke(argument);
            if (readValue != null) {
                return Optional.of(readValue);
            }
        } catch (IllegalAccessException e) {
            LOG.warn(String.format("Unable to invoke bean read method '%s' on '%s'", readMethod, argument), e);
        } catch (InvocationTargetException e) {
            LOG.warn(String.format("Unable to invoke bean read method '%s' on '%s'", readMethod, argument), e);
        }
        return Optional.absent();
    }

    /**
     * Default behavior is to return the unmodified argument.
     */
    @Override
    public Object transformReturnValue(Object returnValue) {
        return returnValue;
    }
}
