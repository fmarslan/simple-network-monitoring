package com.fmarslan.properties;

public class OSBuilder {

	private static final String PING_WINDOWS = "ping -n 1 -l %s -w %s %s";

	private PingResultParser parser;
	private String commandFormat;

	public static OSBuilder build() {
		OSBuilder osb = new OSBuilder();
		if (System.getProperty("os.name").startsWith("Windows")) {
			osb.parser = new PingResultParser(PingResultParser.WINDOWS);
			osb.commandFormat = PING_WINDOWS;
		} else {
			// For Linux and OSX
//		cmd = "ping -c 1 " + host;
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

}
