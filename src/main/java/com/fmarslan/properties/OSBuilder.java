package com.fmarslan.properties;

public class OSBuilder {

	private static final String PING_WINDOWS = "ping -n 1 -l %s -w %s %s";
	private static final String PING_LINUX = "ping -c 1 -s %s -W %s %s";
	
	private PingResultParser parser;
	private String commandFormat;
	private int timeoutFactor;

	public static OSBuilder build() {
		OSBuilder osb = new OSBuilder();
		if (System.getProperty("os.name").startsWith("Windows")) {
			osb.parser = new PingResultParser(PingResultParser.WINDOWS);
			osb.commandFormat = PING_WINDOWS;
			osb.timeoutFactor=1000;
		} else {
			osb.parser = new PingResultParser(PingResultParser.LINUX);
			osb.commandFormat = PING_LINUX;
			osb.timeoutFactor=1;
		}
		return osb;
	}

	private OSBuilder() {
	}

	public PingResultParser getParser() {
		return parser;
	}

	public String getCommandFormat() {
		return commandFormat;
	}
	
	public int getTimeoutFactor() {
		return timeoutFactor;
	}

}
