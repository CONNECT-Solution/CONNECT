package com.agilex;

public abstract class Wait {
	public static final long DEFAULT_TIMEOUT = 30000L;
	public static final long DEFAULT_INTERVAL = 500L;

	public Wait() {
	}

	public Wait(String messageToShowIfTimeout) {
		wait(messageToShowIfTimeout, 30000L, 500L);
	}

	public abstract boolean until();

	public void wait(String message) {
		wait(message, 30000L, 500L);
	}

	public void wait(String message, long timeoutInMilliseconds) {
		wait(message, timeoutInMilliseconds, 500L);
	}

	public void wait(String message, long timeoutInMilliseconds,
			long intervalInMilliseconds) {
		if (!trywait(timeoutInMilliseconds, intervalInMilliseconds))
			throw new WaitTimedOutException(message);
	}

	public boolean trywait() {
		return trywait(30000L, 500L);
	}

	public boolean trywait(long timeoutInMilliseconds) {
		return trywait(timeoutInMilliseconds, 500L);
	}

	public boolean trywait(long timeoutInMilliseconds, long intervalInMilliseconds) {
		long start = System.currentTimeMillis();
		long end = start + timeoutInMilliseconds;
		while (System.currentTimeMillis() < end) {
			if (until())
				return true;
			try {
				Thread.sleep(intervalInMilliseconds);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
		return false;
	}

	public class WaitTimedOutException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public WaitTimedOutException() {
		}

		public WaitTimedOutException(String paramString,
				Throwable paramThrowable) {
			super(paramString, paramThrowable);
		}

		public WaitTimedOutException(String paramString) {
			super(paramString);
		}

		public WaitTimedOutException(Throwable paramThrowable) {
			super(paramThrowable);
		}
	}
}
