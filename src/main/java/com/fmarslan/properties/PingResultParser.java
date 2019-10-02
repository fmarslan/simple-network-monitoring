package com.fmarslan.properties;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PingResultParser {

	public static final String WINDOWS = "Pinging (?<host>[a-zA-Z0-9\\.]*)(.*)data:((Reply from (?<ip>[0-9\\.]*): bytes=(?<size>[0-9]*) time(=|<|>)(?<time>[0-9]*)ms TTL(=|>|<)(?<ttl>[0-9]*))|(?<message>[a-zA-Z ]*)\\.)";

	private String regex;

	public PingResultParser(String regex) {
		this.regex = regex;
	}

	public PingResult parse(String text) {
		PingResult pr = new PingResult();
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(text);
		if (m.find()) {
			String ip = m.group("ip");
			String size = m.group("size");
			String ttl = m.group("ttl");
			String time = m.group("time");
			String host = m.group("host");
			String message = m.group("message");
			pr.setMessage(message);
			pr.setIp(ip);
			pr.setHost(host);
			if (size != null)
				pr.setSize(Integer.valueOf(size));
			if (time != null)
				pr.setTime(Integer.valueOf(time));
			if (ttl != null)
				pr.setTtl(Integer.valueOf(ttl));
			pr.setOriginalText(text);
		}
		return pr;
	}
}
