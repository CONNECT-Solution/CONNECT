package gov.hhs.fha.nhinc.lift.common.util.cleanup;

import gov.hhs.fha.nhinc.gateway.lift.FailedLiftTransactionRequestType;
import gov.hhs.fha.nhinc.gateway.lift.StartLiftTransactionRequestType;
import gov.hhs.fha.nhinc.lift.dao.GatewayLiftMessageDao;
import gov.hhs.fha.nhinc.lift.model.GatewayLiftMsgRecord;
import gov.hhs.fha.nhinc.lift.proxy.GatewayLiftManagerProxy;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import org.apache.commons.logging.Log;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

/**
 *
 * @author Neil Webb
 */
@RunWith(JMock.class)
public class CleanupUtilTest
{
    Mockery context = new JUnit4Mockery()
    {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };
    final Log mockLog = context.mock(Log.class);
    final GatewayLiftMessageDao mockDao = context.mock(GatewayLiftMessageDao.class);
    final GatewayLiftManagerProxy mockLiftManagerProxy = context.mock(GatewayLiftManagerProxy.class);

    @Test
    public void testCreateLogger()
    {
        try
        {
            CleanupUtil cleanupUtil = new CleanupUtil()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
            };
            Log log = cleanupUtil.createLogger();
            assertNotNull("Log was null", log);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testCreateLogger test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testCreateLogger test: " + t.getMessage());
        }
    }

    @Test
    public void testGetGatewayLiftMessageDao()
    {
        try
        {
            CleanupUtil cleanupUtil = new CleanupUtil()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected GatewayLiftMessageDao getGatewayLiftMessageDao()
                {
                    return mockDao;
                }
            };
            GatewayLiftMessageDao dao = cleanupUtil.getGatewayLiftMessageDao();
            assertNotNull("GatewayLiftMessageDao was null", dao);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testGetGatewayLiftMessageDao test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetGatewayLiftMessageDao test: " + t.getMessage());
        }
    }

    @Test
    public void testGetGatewayLiftManagerProxy()
    {
        try
        {
            CleanupUtil cleanupUtil = new CleanupUtil()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected GatewayLiftManagerProxy getGatewayLiftManagerProxy()
                {
                    return mockLiftManagerProxy;
                }
            };
            GatewayLiftManagerProxy proxy = cleanupUtil.getGatewayLiftManagerProxy();
            assertNotNull("GatewayLiftManagerProxy was null", proxy);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testGetGatewayLiftManagerProxy test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetGatewayLiftManagerProxy test: " + t.getMessage());
        }
    }

    @Test
    public void testGetCleanupIntervalPropertyHappy()
    {
        try
        {
            CleanupUtil cleanupUtil = new CleanupUtil()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected long getCleanupIntervalProperty() throws PropertyAccessException
                {
                    return 5L;
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                }
            });
            long cleanupIntervalProperty = cleanupUtil.getCleanupIntervalProperty();
            assertEquals("Cleanup interval property", 5L, cleanupIntervalProperty);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testGetCleanupIntervalPropertyHappy test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetCleanupIntervalPropertyHappy test: " + t.getMessage());
        }
    }

    @Test  (expected= gov.hhs.fha.nhinc.properties.PropertyAccessException.class)
    public void testGetCleanupIntervalPropertyException() throws PropertyAccessException
    {
        CleanupUtil cleanupUtil = new CleanupUtil()
        {
            @Override
            protected Log createLogger()
            {
                return mockLog;
            }
            @Override
            protected long getCleanupIntervalProperty() throws PropertyAccessException
            {
                throw new PropertyAccessException();
            }
        };
        context.checking(new Expectations()
        {
            {
                allowing(mockLog).debug(with(any(String.class)));
            }
        });
        cleanupUtil.getCleanupIntervalProperty();
        fail("Should have had exception");
    }

    @Test
    public void testGetCleanupIntervalMinutesHappy()
    {
        try
        {
            CleanupUtil cleanupUtil = new CleanupUtil()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected long getCleanupIntervalProperty() throws PropertyAccessException
                {
                    return 5L;
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                }
            });
            int cleanupIntervalMinutes = cleanupUtil.getCleanupIntervalMinutes();
            assertEquals("Cleanup interval minutes", 5, cleanupIntervalMinutes);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testGetCleanupIntervalMinutesHappy test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetCleanupIntervalMinutesHappy test: " + t.getMessage());
        }
    }

    @Test
    public void testGetCleanupIntervalMinutesException()
    {
        try
        {
            CleanupUtil cleanupUtil = new CleanupUtil()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected long getCleanupIntervalProperty() throws PropertyAccessException
                {
                    throw new PropertyAccessException();
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                    oneOf(mockLog).error(with(aNonNull(String.class)), with(aNonNull(PropertyAccessException.class)));
                }
            });
            int cleanupIntervalMinutes = cleanupUtil.getCleanupIntervalMinutes();
            assertEquals("Cleanup interval minutes", 0, cleanupIntervalMinutes);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testGetCleanupIntervalMinutesException test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetCleanupIntervalMinutesException test: " + t.getMessage());
        }
    }

    @Test
    public void testGetStaleMessageEnteredPropertyHappy()
    {
        try
        {
            CleanupUtil cleanupUtil = new CleanupUtil()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected long getStaleMessageEnteredProperty() throws PropertyAccessException
                {
                    return 6L;
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                }
            });
            long staleMessageEnteredProperty = cleanupUtil.getStaleMessageEnteredProperty();
            assertEquals("Stale message entered property", 6L, staleMessageEnteredProperty);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testGetStaleMessageEnteredPropertyHappy test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetStaleMessageEnteredPropertyHappy test: " + t.getMessage());
        }
    }

    @Test  (expected= gov.hhs.fha.nhinc.properties.PropertyAccessException.class)
    public void testGetStaleMessageEnteredPropertyException() throws PropertyAccessException
    {
        CleanupUtil cleanupUtil = new CleanupUtil()
        {
            @Override
            protected Log createLogger()
            {
                return mockLog;
            }
            @Override
            protected long getStaleMessageEnteredProperty() throws PropertyAccessException
            {
                throw new PropertyAccessException();
            }
        };
        context.checking(new Expectations()
        {
            {
                allowing(mockLog).debug(with(any(String.class)));
            }
        });
        cleanupUtil.getStaleMessageEnteredProperty();
        fail("Should have had exception");
    }

    @Test
    public void testGetStaleMessageEnteredMinutesHappy()
    {
        try
        {
            CleanupUtil cleanupUtil = new CleanupUtil()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected long getStaleMessageEnteredProperty() throws PropertyAccessException
                {
                    return 6L;
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                }
            });
            int staleMessageEnteredMinutes = cleanupUtil.getStaleMessageEnteredMinutes();
            assertEquals("Stale message entered minutes", 6, staleMessageEnteredMinutes);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testGetStaleMessageEnteredMinutesHappy test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetStaleMessageEnteredMinutesHappy test: " + t.getMessage());
        }
    }

    @Test
    public void testGetStaleMessageEnteredMinutesException()
    {
        try
        {
            CleanupUtil cleanupUtil = new CleanupUtil()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected long getStaleMessageEnteredProperty() throws PropertyAccessException
                {
                    throw new PropertyAccessException();
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                    oneOf(mockLog).error(with(aNonNull(String.class)), with(aNonNull(PropertyAccessException.class)));
                }
            });
            int staleMessageEnteredMinutes = cleanupUtil.getStaleMessageEnteredMinutes();
            assertEquals("Stale message entered minutes", 0, staleMessageEnteredMinutes);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testGetStaleMessageEnteredMinutesException test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetStaleMessageEnteredMinutesException test: " + t.getMessage());
        }
    }

    @Test
    public void testGetStaleMessageProcessingPropertyHappy()
    {
        try
        {
            CleanupUtil cleanupUtil = new CleanupUtil()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected long getStaleMessageProcessingProperty() throws PropertyAccessException
                {
                    return 7L;
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                }
            });
            long staleMessageProcessingProperty = cleanupUtil.getStaleMessageProcessingProperty();
            assertEquals("Stale message processing property", 7L, staleMessageProcessingProperty);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testGetStaleMessageProcessingPropertyHappy test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetStaleMessageProcessingPropertyHappy test: " + t.getMessage());
        }
    }

    @Test  (expected= gov.hhs.fha.nhinc.properties.PropertyAccessException.class)
    public void testGetStaleMessageProcessingPropertyException() throws PropertyAccessException
    {
        CleanupUtil cleanupUtil = new CleanupUtil()
        {
            @Override
            protected Log createLogger()
            {
                return mockLog;
            }
            @Override
            protected long getStaleMessageProcessingProperty() throws PropertyAccessException
            {
                throw new PropertyAccessException();
            }
        };
        context.checking(new Expectations()
        {
            {
                allowing(mockLog).debug(with(any(String.class)));
            }
        });
        cleanupUtil.getStaleMessageProcessingProperty();
        fail("Should have had exception");
    }

    @Test
    public void testGetStaleMessageProcessingMinutesHappy()
    {
        try
        {
            CleanupUtil cleanupUtil = new CleanupUtil()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected long getStaleMessageProcessingProperty() throws PropertyAccessException
                {
                    return 7L;
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                }
            });
            int staleMessageProcessingMinutes = cleanupUtil.getStaleMessageProcessingMinutes();
            assertEquals("Stale message processing minutes", 7, staleMessageProcessingMinutes);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testGetStaleMessageProcessingMinutesHappy test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetStaleMessageProcessingMinutesHappy test: " + t.getMessage());
        }
    }

    @Test
    public void testGetStaleMessageProcessingMinutesException()
    {
        try
        {
            CleanupUtil cleanupUtil = new CleanupUtil()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected long getStaleMessageProcessingProperty() throws PropertyAccessException
                {
                    throw new PropertyAccessException();
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                    oneOf(mockLog).error(with(aNonNull(String.class)), with(aNonNull(PropertyAccessException.class)));
                }
            });
            int staleMessageProcessingMinutes = cleanupUtil.getStaleMessageProcessingMinutes();
            assertEquals("Stale message processing minutes", 0, staleMessageProcessingMinutes);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testGetStaleMessageProcessingMinutesException test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetStaleMessageProcessingMinutesException test: " + t.getMessage());
        }
    }

    @Test
    public void testGetLastCleanupHappy()
    {
        try
        {
            final Date lastCleanupDate = new Date();
            CleanupUtil cleanupUtil = new CleanupUtil()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected Date getLastCleanup()
                {
                    return lastCleanupDate;
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                }
            });
            Date lastCleanup = cleanupUtil.getLastCleanup();
            assertNotNull("Last cleanup date was null", lastCleanup);
            assertEquals("Last cleanup date", lastCleanupDate, lastCleanup);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testGetLastCleanupHappy test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetLastCleanupHappy test: " + t.getMessage());
        }
    }

    @Test
    public void testIsRunningTrue()
    {
        try
        {
            CleanupUtil cleanupUtil = new CleanupUtil()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected boolean getIsRunningFlag()
                {
                    return true;
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                }
            });
            assertTrue("Is running flag", cleanupUtil.getIsRunningFlag());
        }
        catch(Throwable t)
        {
            System.out.println("Error running testIsRunningTrue test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testIsRunningTrue test: " + t.getMessage());
        }
    }

    @Test
    public void testGetCurrentDateHappy()
    {
        try
        {
            final Date currentDate = new Date();
            CleanupUtil cleanupUtil = new CleanupUtil()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected Date getCurrentDate()
                {
                    return currentDate;
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                }
            });
            Date current = cleanupUtil.getCurrentDate();
            assertNotNull("Current date was null", current);
            assertEquals("Current date", currentDate, current);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testGetCurrentDateHappy test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetCurrentDateHappy test: " + t.getMessage());
        }
    }

    @Test
    public void testGetLastCleanupNull()
    {
        try
        {
            CleanupUtil cleanupUtil = new CleanupUtil()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected Date getLastCleanup()
                {
                    return null;
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                }
            });
            Date lastCleanup = cleanupUtil.getLastCleanup();
            assertNull("Last cleanup date was not null", lastCleanup);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testGetLastCleanupNull test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetLastCleanupNull test: " + t.getMessage());
        }
    }

    @Test
    public void testIsCleanupRequiredNullLastCleanup()
    {
        try
        {
            CleanupUtil cleanupUtil = new CleanupUtil()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected Date getLastCleanup()
                {
                    return null;
                }
                @Override
                protected boolean getIsRunningFlag()
                {
                    return false;
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                }
            });
            assertTrue("Cleanup required", cleanupUtil.isCleanupRequired());
        }
        catch(Throwable t)
        {
            System.out.println("Error running testGetLastCleanupNull test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetLastCleanupNull test: " + t.getMessage());
        }
    }

    @Test
    public void testIsCleanupRequiredAlreadyRunning()
    {
        try
        {
            final Date currentDate = new Date();
            CleanupUtil cleanupUtil = new CleanupUtil()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected Date getLastCleanup()
                {
                    return currentDate;
                }
                @Override
                protected Date getCurrentDate()
                {
                    return currentDate;
                }
                @Override
                protected long getCleanupIntervalProperty() throws PropertyAccessException
                {
                    return 5L;
                }
                @Override
                protected boolean getIsRunningFlag()
                {
                    return true;
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                }
            });
            assertFalse("Cleanup required", cleanupUtil.isCleanupRequired());
        }
        catch(Throwable t)
        {
            System.out.println("Error running testIsCleanupRequiredAlreadyRunning test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testIsCleanupRequiredAlreadyRunning test: " + t.getMessage());
        }
    }

    @Test
    public void testIsCleanupRequiredSameTime()
    {
        try
        {
            final Date currentDate = new Date();
            CleanupUtil cleanupUtil = new CleanupUtil()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected Date getLastCleanup()
                {
                    return currentDate;
                }
                @Override
                protected Date getCurrentDate()
                {
                    return currentDate;
                }
                @Override
                protected long getCleanupIntervalProperty() throws PropertyAccessException
                {
                    return 5L;
                }
                @Override
                protected boolean getIsRunningFlag()
                {
                    return false;
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                }
            });
            assertTrue("Cleanup required", cleanupUtil.isCleanupRequired());
        }
        catch(Throwable t)
        {
            System.out.println("Error running testIsCleanupRequiredSameTime test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testIsCleanupRequiredSameTime test: " + t.getMessage());
        }
    }

    @Test
    public void testIsCleanupRequiredTriggerSame()
    {
        try
        {
            final Date lastCleanupDate = new Date();
            Calendar currentDateCal = GregorianCalendar.getInstance();
            currentDateCal.add(Calendar.MINUTE, 5);
            final Date currentDate = currentDateCal.getTime();
            CleanupUtil cleanupUtil = new CleanupUtil()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected Date getLastCleanup()
                {
                    return lastCleanupDate;
                }
                @Override
                protected Date getCurrentDate()
                {
                    return currentDate;
                }
                @Override
                protected long getCleanupIntervalProperty() throws PropertyAccessException
                {
                    return 5L;
                }
                @Override
                protected boolean getIsRunningFlag()
                {
                    return false;
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                }
            });
            assertFalse("Cleanup required", cleanupUtil.isCleanupRequired());
        }
        catch(Throwable t)
        {
            System.out.println("Error running testIsCleanupRequiredTriggerSame test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testIsCleanupRequiredTriggerSame test: " + t.getMessage());
        }
    }

    @Test
    public void testIsCleanupRequiredTriggerPast()
    {
        try
        {
            final Date lastCleanupDate = new Date();
            Calendar currentDateCal = GregorianCalendar.getInstance();
            currentDateCal.add(Calendar.MINUTE, 4);
            final Date currentDate = currentDateCal.getTime();
            CleanupUtil cleanupUtil = new CleanupUtil()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected Date getLastCleanup()
                {
                    return lastCleanupDate;
                }
                @Override
                protected Date getCurrentDate()
                {
                    return currentDate;
                }
                @Override
                protected long getCleanupIntervalProperty() throws PropertyAccessException
                {
                    return 5L;
                }
                @Override
                protected boolean getIsRunningFlag()
                {
                    return false;
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                }
            });
            assertTrue("Cleanup required", cleanupUtil.isCleanupRequired());
        }
        catch(Throwable t)
        {
            System.out.println("Error running testIsCleanupRequiredTriggerPast test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testIsCleanupRequiredTriggerPast test: " + t.getMessage());
        }
    }

    @Test
    public void testCleanupRecordsNotRequired()
    {
        try
        {
            CleanupUtil cleanupUtil = new CleanupUtil()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected boolean isCleanupRequired()
                {
                    return false;
                }
            };
            context.checking(new Expectations()
            {
                {
                    oneOf(mockLog).info("cleanupRecords - no cleanup required.");
                }
            });
            cleanupUtil.cleanupRecords();
        }
        catch(Throwable t)
        {
            System.out.println("Error running testCleanupRecordsNotRequired test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testCleanupRecordsNotRequired test: " + t.getMessage());
        }
    }

    @Test
    public void testCleanupRecordsRequired()
    {
        try
        {
            CleanupUtil cleanupUtil = new CleanupUtil()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected boolean isCleanupRequired()
                {
                    return true;
                }
                @Override
                protected void restartStaleEnteredRecords()
                {
                }
                @Override
                protected void restartStaleProcessingRecords()
                {
                }
            };
            context.checking(new Expectations()
            {
                {
                    oneOf(mockLog).info("cleanupRecords - cleanup required, restarting processing of stale records.");
                    allowing(mockLog).debug(with(any(String.class)));
                }
            });
            cleanupUtil.cleanupRecords();
        }
        catch(Throwable t)
        {
            System.out.println("Error running testCleanupRecordsRequired test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testCleanupRecordsRequired test: " + t.getMessage());
        }
    }

    @Test
    public void testCreateStaleDate()
    {
        try
        {
            int staleOffset = 30;
            final Date currentDate = new Date();
            Calendar staleDateCal = GregorianCalendar.getInstance();
            staleDateCal.setTime(currentDate);
            staleDateCal.add(Calendar.MINUTE, staleOffset);
            final Date currentDateToReturn = staleDateCal.getTime();
            CleanupUtil cleanupUtil = new CleanupUtil()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected Date getCurrentDate()
                {
                    return currentDateToReturn;
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                }
            });
            Date staleDate = cleanupUtil.createStaleDate(staleOffset);
            assertNotNull("Stale date was null", staleDate);
            assertEquals("Stale date", currentDate, staleDate);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testCreateStaleDate test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testCreateStaleDate test: " + t.getMessage());
        }
    }

    @Test
    public void testGetStaleRecordsHappy()
    {
        try
        {
            final String messageStatus = "TestSatus";
            final Date staleDate = new Date();
            CleanupUtil cleanupUtil = new CleanupUtil()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected GatewayLiftMessageDao getGatewayLiftMessageDao()
                {
                    return mockDao;
                }
           };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                    allowing(mockDao).queryByMessageTypeOrderByProcessingTime(messageStatus, staleDate);
                }
            });
            
            List<GatewayLiftMsgRecord> records = cleanupUtil.getStaleRecords(messageStatus, staleDate);
            assertNotNull("Stale records list was null", records);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testGetStaleRecordsHappy test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetStaleRecordsHappy test: " + t.getMessage());
        }
    }

    @Test
    public void testGetStaleRecordsNullStatus()
    {
        try
        {
            final String messageStatus = null;
            final Date staleDate = new Date();
            CleanupUtil cleanupUtil = new CleanupUtil()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected GatewayLiftMessageDao getGatewayLiftMessageDao()
                {
                    return mockDao;
                }
           };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                    oneOf(mockLog).error("Message status was null or empty.");
                }
            });

            List<GatewayLiftMsgRecord> records = cleanupUtil.getStaleRecords(messageStatus, staleDate);
            assertNull("Stale records list was not null", records);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testGetStaleRecordsNullStatus test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetStaleRecordsNullStatus test: " + t.getMessage());
        }
    }

    @Test
    public void testGetStaleRecordsEmptyStatus()
    {
        try
        {
            final String messageStatus = "";
            final Date staleDate = new Date();
            CleanupUtil cleanupUtil = new CleanupUtil()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected GatewayLiftMessageDao getGatewayLiftMessageDao()
                {
                    return mockDao;
                }
           };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                    oneOf(mockLog).error("Message status was null or empty.");
                }
            });

            List<GatewayLiftMsgRecord> records = cleanupUtil.getStaleRecords(messageStatus, staleDate);
            assertNull("Stale records list was not null", records);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testGetStaleRecordsEmptyStatus test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetStaleRecordsEmptyStatus test: " + t.getMessage());
        }
    }

    @Test
    public void testGetStaleRecordsNullStaleMessageDate()
    {
        try
        {
            final String messageStatus = "TestSatus";
            final Date staleDate = null;
            CleanupUtil cleanupUtil = new CleanupUtil()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected GatewayLiftMessageDao getGatewayLiftMessageDao()
                {
                    return mockDao;
                }
           };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                    oneOf(mockLog).error("Stale message date was null.");
                }
            });

            List<GatewayLiftMsgRecord> records = cleanupUtil.getStaleRecords(messageStatus, staleDate);
            assertNull("Stale records list was not null", records);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testGetStaleRecordsNullStaleMessageDate test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetStaleRecordsNullStaleMessageDate test: " + t.getMessage());
        }
    }

    @Test
    public void testGetStaleRecordsNullDao()
    {
        try
        {
            final String messageStatus = "TestSatus";
            final Date staleDate = new Date();
            CleanupUtil cleanupUtil = new CleanupUtil()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected GatewayLiftMessageDao getGatewayLiftMessageDao()
                {
                    return null;
                }
           };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                    oneOf(mockLog).error("GatewayLiftMessageDao was null.");
                }
            });

            List<GatewayLiftMsgRecord> records = cleanupUtil.getStaleRecords(messageStatus, staleDate);
            assertNull("Stale records list was not null", records);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testGetStaleRecordsNullDao test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetStaleRecordsNullDao test: " + t.getMessage());
        }
    }

    @Test
    public void testRestartStaleEnteredRecordsHappyWithOneMsg()
    {
        try
        {
            final Date staleDate = new Date();
            CleanupUtil cleanupUtil = new CleanupUtil()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected int getStaleMessageEnteredMinutes()
                {
                    return 1;
                }
                @Override
                protected Date createStaleDate(int minuteOffset)
                {
                    return staleDate;
                }
                @Override
                protected List<GatewayLiftMsgRecord> getStaleRecords(String messageStatus, Date staleMessageDate)
                {
                    List<GatewayLiftMsgRecord> staleRecords = new ArrayList<GatewayLiftMsgRecord>();
                    GatewayLiftMsgRecord record1 = new GatewayLiftMsgRecord();
                    staleRecords.add(record1);
                    record1.setRequestKeyGuid("1234");
                    return staleRecords;
                }
                @Override
                protected GatewayLiftManagerProxy getGatewayLiftManagerProxy()
                {
                    return mockLiftManagerProxy;
                }
           };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                    oneOf(mockLog).info("Stale message list for entered status has 1 record(s).");
                    oneOf(mockLog).info("Restarting processing for message in " + NhincConstants.LIFT_TRANSFER_DB_STATE_ENTERED + " state with request key guid: 1234");
                    oneOf(mockLiftManagerProxy).startLiftTransaction(with(aNonNull(StartLiftTransactionRequestType.class)));
                }
            });

            cleanupUtil.restartStaleEnteredRecords();
        }
        catch(Throwable t)
        {
            System.out.println("Error running testRestartStaleEnteredRecordsHappyWithOneMsg test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testRestartStaleEnteredRecordsHappyWithOneMsg test: " + t.getMessage());
        }
    }

    @Test
    public void testRestartStaleEnteredRecordsHappyWithTwoMsgs()
    {
        try
        {
            final Date staleDate = new Date();
            CleanupUtil cleanupUtil = new CleanupUtil()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected int getStaleMessageEnteredMinutes()
                {
                    return 1;
                }
                @Override
                protected Date createStaleDate(int minuteOffset)
                {
                    return staleDate;
                }
                @Override
                protected List<GatewayLiftMsgRecord> getStaleRecords(String messageStatus, Date staleMessageDate)
                {
                    List<GatewayLiftMsgRecord> staleRecords = new ArrayList<GatewayLiftMsgRecord>();
                    GatewayLiftMsgRecord record1 = new GatewayLiftMsgRecord();
                    staleRecords.add(record1);
                    record1.setRequestKeyGuid("1234");
                    GatewayLiftMsgRecord record2 = new GatewayLiftMsgRecord();
                    staleRecords.add(record2);
                    record2.setRequestKeyGuid("5678");
                    return staleRecords;
                }
                @Override
                protected GatewayLiftManagerProxy getGatewayLiftManagerProxy()
                {
                    return mockLiftManagerProxy;
                }
           };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                    oneOf(mockLog).info("Stale message list for entered status has 2 record(s).");
                    oneOf(mockLog).info("Restarting processing for message in " + NhincConstants.LIFT_TRANSFER_DB_STATE_ENTERED + " state with request key guid: 1234");
                    oneOf(mockLog).info("Restarting processing for message in " + NhincConstants.LIFT_TRANSFER_DB_STATE_ENTERED + " state with request key guid: 5678");
                    exactly(2).of(mockLiftManagerProxy).startLiftTransaction(with(aNonNull(StartLiftTransactionRequestType.class)));
                }
            });

            cleanupUtil.restartStaleEnteredRecords();
        }
        catch(Throwable t)
        {
            System.out.println("Error running testRestartStaleEnteredRecordsHappyWithTwoMsgs test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testRestartStaleEnteredRecordsHappyWithTwoMsgs test: " + t.getMessage());
        }
    }

    @Test
    public void testRestartStaleEnteredRecordsBadOffset()
    {
        try
        {
            CleanupUtil cleanupUtil = new CleanupUtil()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected int getStaleMessageEnteredMinutes()
                {
                    return 0;
                }
           };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                    oneOf(mockLog).warn("Invalid stale message entered minutes value (0) - not restarting stale messages in entered status");
                }
            });

            cleanupUtil.restartStaleEnteredRecords();
        }
        catch(Throwable t)
        {
            System.out.println("Error running testRestartStaleEnteredRecordsBadOffset test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testRestartStaleEnteredRecordsBadOffset test: " + t.getMessage());
        }
    }

    @Test
    public void testRestartStaleEnteredRecordsNullStaleRecords()
    {
        try
        {
            final Date staleDate = new Date();
            CleanupUtil cleanupUtil = new CleanupUtil()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected int getStaleMessageEnteredMinutes()
                {
                    return 1;
                }
                @Override
                protected Date createStaleDate(int minuteOffset)
                {
                    return staleDate;
                }
                @Override
                protected List<GatewayLiftMsgRecord> getStaleRecords(String messageStatus, Date staleMessageDate)
                {
                    List<GatewayLiftMsgRecord> staleRecords = null;
                    return staleRecords;
                }
           };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                    oneOf(mockLog).info("Stale entered message list was null.");
                }
            });

            cleanupUtil.restartStaleEnteredRecords();
        }
        catch(Throwable t)
        {
            System.out.println("Error running testRestartStaleEnteredRecordsNullStaleRecords test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testRestartStaleEnteredRecordsNullStaleRecords test: " + t.getMessage());
        }
    }

    @Test
    public void testRestartStaleEnteredRecordsEmptyStaleRecords()
    {
        try
        {
            final Date staleDate = new Date();
            CleanupUtil cleanupUtil = new CleanupUtil()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected int getStaleMessageEnteredMinutes()
                {
                    return 1;
                }
                @Override
                protected Date createStaleDate(int minuteOffset)
                {
                    return staleDate;
                }
                @Override
                protected List<GatewayLiftMsgRecord> getStaleRecords(String messageStatus, Date staleMessageDate)
                {
                    List<GatewayLiftMsgRecord> staleRecords = new ArrayList<GatewayLiftMsgRecord>();
                    return staleRecords;
                }
           };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                    oneOf(mockLog).info("Stale message list for entered status has 0 record(s).");
                }
            });

            cleanupUtil.restartStaleEnteredRecords();
        }
        catch(Throwable t)
        {
            System.out.println("Error running testRestartStaleEnteredRecordsEmptyStaleRecords test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testRestartStaleEnteredRecordsEmptyStaleRecords test: " + t.getMessage());
        }
    }

    @Test
    public void testRestartStaleProcessingRecordsHappyWithOneMsg()
    {
        try
        {
            final Date staleDate = new Date();
            CleanupUtil cleanupUtil = new CleanupUtil()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected int getStaleMessageProcessingMinutes()
                {
                    return 1;
                }
                @Override
                protected Date createStaleDate(int minuteOffset)
                {
                    return staleDate;
                }
                @Override
                protected List<GatewayLiftMsgRecord> getStaleRecords(String messageStatus, Date staleMessageDate)
                {
                    List<GatewayLiftMsgRecord> staleRecords = new ArrayList<GatewayLiftMsgRecord>();
                    GatewayLiftMsgRecord record1 = new GatewayLiftMsgRecord();
                    staleRecords.add(record1);
                    record1.setRequestKeyGuid("4321");
                    return staleRecords;
                }
                @Override
                protected GatewayLiftManagerProxy getGatewayLiftManagerProxy()
                {
                    return mockLiftManagerProxy;
                }
           };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                    oneOf(mockLog).info("Stale message list for processing status has 1 record(s).");
                    oneOf(mockLog).info("Failing processing for message in " + NhincConstants.LIFT_TRANSFER_DB_STATE_PROCESSING + " state with request key guid: 4321");
                    oneOf(mockLiftManagerProxy).failedLiftTransaction(with(aNonNull(FailedLiftTransactionRequestType.class)));
                }
            });

            cleanupUtil.restartStaleProcessingRecords();
        }
        catch(Throwable t)
        {
            System.out.println("Error running testRestartStaleProcessingRecordsHappyWithOneMsg test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testRestartStaleProcessingRecordsHappyWithOneMsg test: " + t.getMessage());
        }
    }

    @Test
    public void testRestartStaleProcessingRecordsHappyWithTwoMsgs()
    {
        try
        {
            final Date staleDate = new Date();
            CleanupUtil cleanupUtil = new CleanupUtil()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected int getStaleMessageProcessingMinutes()
                {
                    return 1;
                }
                @Override
                protected Date createStaleDate(int minuteOffset)
                {
                    return staleDate;
                }
                @Override
                protected List<GatewayLiftMsgRecord> getStaleRecords(String messageStatus, Date staleMessageDate)
                {
                    List<GatewayLiftMsgRecord> staleRecords = new ArrayList<GatewayLiftMsgRecord>();
                    GatewayLiftMsgRecord record1 = new GatewayLiftMsgRecord();
                    staleRecords.add(record1);
                    record1.setRequestKeyGuid("4321");
                    GatewayLiftMsgRecord record2 = new GatewayLiftMsgRecord();
                    staleRecords.add(record2);
                    record2.setRequestKeyGuid("8765");
                    return staleRecords;
                }
                @Override
                protected GatewayLiftManagerProxy getGatewayLiftManagerProxy()
                {
                    return mockLiftManagerProxy;
                }
           };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                    oneOf(mockLog).info("Stale message list for processing status has 2 record(s).");
                    oneOf(mockLog).info("Failing processing for message in " + NhincConstants.LIFT_TRANSFER_DB_STATE_PROCESSING + " state with request key guid: 4321");
                    oneOf(mockLog).info("Failing processing for message in " + NhincConstants.LIFT_TRANSFER_DB_STATE_PROCESSING + " state with request key guid: 8765");
                    exactly(2).of(mockLiftManagerProxy).failedLiftTransaction(with(aNonNull(FailedLiftTransactionRequestType.class)));
                }
            });

            cleanupUtil.restartStaleProcessingRecords();
        }
        catch(Throwable t)
        {
            System.out.println("Error running testRestartStaleProcessingRecordsHappyWithTwoMsgs test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testRestartStaleProcessingRecordsHappyWithTwoMsgs test: " + t.getMessage());
        }
    }

    @Test
    public void testRestartStaleProcessingRecordsBadOffset()
    {
        try
        {
            CleanupUtil cleanupUtil = new CleanupUtil()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected int getStaleMessageProcessingMinutes()
                {
                    return 0;
                }
           };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                    oneOf(mockLog).warn("Invalid stale message processing minutes value (0) - not restarting stale messages in processing status.");
                }
            });

            cleanupUtil.restartStaleProcessingRecords();
        }
        catch(Throwable t)
        {
            System.out.println("Error running testRestartStaleProcessingRecordsBadOffset test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testRestartStaleProcessingRecordsBadOffset test: " + t.getMessage());
        }
    }

    @Test
    public void testRestartStaleProcessingRecordsNullStaleRecords()
    {
        try
        {
            final Date staleDate = new Date();
            CleanupUtil cleanupUtil = new CleanupUtil()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected int getStaleMessageProcessingMinutes()
                {
                    return 1;
                }
                @Override
                protected Date createStaleDate(int minuteOffset)
                {
                    return staleDate;
                }
                @Override
                protected List<GatewayLiftMsgRecord> getStaleRecords(String messageStatus, Date staleMessageDate)
                {
                    List<GatewayLiftMsgRecord> staleRecords = null;
                    return staleRecords;
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                    oneOf(mockLog).info("Stale processing message list was null.");
                }
            });

            cleanupUtil.restartStaleProcessingRecords();
        }
        catch(Throwable t)
        {
            System.out.println("Error running testRestartStaleProcessingRecordsNullStaleRecords test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testRestartStaleProcessingRecordsNullStaleRecords test: " + t.getMessage());
        }
    }

    @Test
    public void testRestartStaleProcessingRecordsEmptyStaleRecords()
    {
        try
        {
            final Date staleDate = new Date();
            CleanupUtil cleanupUtil = new CleanupUtil()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected int getStaleMessageProcessingMinutes()
                {
                    return 1;
                }
                @Override
                protected Date createStaleDate(int minuteOffset)
                {
                    return staleDate;
                }
                @Override
                protected List<GatewayLiftMsgRecord> getStaleRecords(String messageStatus, Date staleMessageDate)
                {
                    List<GatewayLiftMsgRecord> staleRecords = new ArrayList<GatewayLiftMsgRecord>();
                    return staleRecords;
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                    oneOf(mockLog).info("Stale message list for processing status has 0 record(s).");
                }
            });

            cleanupUtil.restartStaleProcessingRecords();
        }
        catch(Throwable t)
        {
            System.out.println("Error running testRestartStaleProcessingRecordsEmptyStaleRecords test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testRestartStaleProcessingRecordsEmptyStaleRecords test: " + t.getMessage());
        }
    }

}
