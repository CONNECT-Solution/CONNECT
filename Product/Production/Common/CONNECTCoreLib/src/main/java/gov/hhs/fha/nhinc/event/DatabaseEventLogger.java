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

import gov.hhs.fha.nhinc.event.dao.DatabaseEventLoggerDao;
import gov.hhs.fha.nhinc.event.model.DatabaseEvent;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Use the database to log events.
 */
public class DatabaseEventLogger extends EventLogger {

    private final DatabaseEventLoggerDao databaseEventLoggerDao;

    private static final Logger LOG = LoggerFactory.getLogger(DatabaseEventLogger.class);

    /**
     * Constructor.
     *
     * @param databaseEventLoggerDao database event logger dao
     */
    public DatabaseEventLogger(DatabaseEventLoggerDao databaseEventLoggerDao) {
        super();
        this.databaseEventLoggerDao = databaseEventLoggerDao;
    }

    /**
     * Constructor.
     */
    public DatabaseEventLogger() {
        super();
        databaseEventLoggerDao = DatabaseEventLoggerDao.getInstance();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void recordEvent(EventManager manager, Event event) {
        if (event != null) {
            DatabaseEvent dbEvent = new DatabaseEvent();
            dbEvent.setEventName(event.getEventName());
            dbEvent.setDescription(event.getDescription());
            dbEvent.setMessageID(event.getMessageID());
            dbEvent.setTransactionID(event.getTransactionID());
            dbEvent.setServiceType(event.getServiceType());
            dbEvent.setInitiatorHcid(event.getInitiatorHcid());
            dbEvent.setRespondingHcid(event.getRespondingHcid());
            dbEvent.setEventTime(getFormattedEventTime());
            databaseEventLoggerDao.insertEvent(dbEvent);
        }
    }

    public Date getFormattedEventTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();

        try {
            date = dateFormat.parse(dateFormat.format(date));
        } catch (ParseException ex) {
            LOG.error("EventTime could not be parsed: " + ex.getMessage());
        }

        return date;
    }
}
