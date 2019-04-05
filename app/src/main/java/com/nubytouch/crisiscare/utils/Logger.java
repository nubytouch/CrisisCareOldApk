package com.nubytouch.crisiscare.utils;

import timber.log.Timber;

public class Logger {

	private static Logger logger = null;

	public static Logger getInstance() {
		if (logger == null)
			logger = new Logger();
		return logger;
	}

	public void LogException(Throwable e) {
		Timber.wtf(e);

	}

	public void LogException(Throwable e, String msg) {
		Timber.wtf(msg, e);
	}

	public void LogException(String message) {
		Timber.wtf(message,"");
	}

}
