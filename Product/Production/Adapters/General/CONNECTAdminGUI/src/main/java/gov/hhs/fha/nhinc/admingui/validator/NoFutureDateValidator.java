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
package gov.hhs.fha.nhinc.admingui.validator;

import gov.hhs.fha.nhinc.admingui.util.HelperUtil;
import java.text.MessageFormat;
import java.util.Date;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Tran Tang
 *
 */
@FacesValidator("NoFutureDateValidator")
public class NoFutureDateValidator implements Validator {
    private static final Logger LOG = LoggerFactory.getLogger(NoFutureDateValidator.class);

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) {
        Date nowDate = new Date(HelperUtil.getDateNow());
        Date inputDate = (Date) value;
        boolean isRequired = (boolean) component.getAttributes().get("required");

        if (isRequired && HelperUtil.diffByDays(inputDate, nowDate) > 0) {
            LOG.trace("NoFutureDateValidator fail-validation");

            String errorMessage = (String)component.getAttributes().get("errorMessage");
            if(null == errorMessage){
                errorMessage = MessageFormat.format("{0} cannot be after this date: {1}.", component.getId(),
                    nowDate);
            }
            throw new ValidatorException(HelperUtil.getMsgError(errorMessage));
        }

    }
}
