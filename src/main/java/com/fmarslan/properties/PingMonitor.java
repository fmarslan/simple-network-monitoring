
/**
 * 
 * Copyright 2019 FMARSLAN
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 * 
 */

package com.fmarslan.properties;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author fmarslan
 *
 */
public class PingMonitor {

	private static final Logger LOGGER = LogManager.getLogger(PingMonitor.class);
	private static OSBuilder osBuilder;
	private static int timeout;
	private static int packageSize;

	static SimpleDateFormat sdf;

	public static void sendPingRequest(String ipAddress) {

		try {
			String cmd = String.format(osBuilder.getCommandFormat(), packageSize, timeout, ipAddress);
			StringBuilder outputLines = new StringBuilder();
			Process myProcess = Runtime.getRuntime().exec(cmd);
			myProcess.waitFor();
			InputStreamReader isr = new InputStreamReader(myProcess.getInputStream());
			BufferedReader br = new BufferedReader(isr);
			String line;
			while ((line = br.readLine()) != null) {
				outputLines.append(line);
			}
			PingResult pingResult = osBuilder.getParser().parse(outputLines.toString());
			LOGGER.info("time={}, host={}, responseIp={}, size={}, miliseconds={}, ttl={}, message={}, originalText={}", getTime(), pingResult.getHost(), pingResult.getIp(), pingResult.getSize(),
					pingResult.getTime(), pingResult.getTtl(), pingResult.getMessage(), pingResult.getOriginalText());
		} catch (Exception ex) {
			ex.printStackTrace();
		}

//		InetAddress geek = InetAddress.getByName(ipAddress);
//
//		long time = System.currentTimeMillis();
//		System.out.println("Sending Ping Request to " + ipAddress);
//		
//		if (geek.isReachable(Integer.valueOf(EnvironmentReader.getEnvironment("app.timeout")))) {
//			time = System.currentTimeMillis() - time;
//			LOGGER.info("", getTime(), REACHABLE, time, ipAddress);
//			if (Long.valueOf(EnvironmentReader.getEnvironment("app.threshold")) <= time) {
//
//			}
//		} else {
////			LOGGER.error(String.format("%s,%s,[unreachable]", "-", ipAddress));
//			LOGGER.error("", getTime(), UNREACHABLE, "-", ipAddress);
//		}
	}

	private static String getTime() {
		String timezone = EnvironmentReader.getEnvironment("app.timezone");
		Calendar c = Calendar
				.getInstance(TimeZone.getTimeZone(timezone));
		return sdf.format(c.getTime());
	}

	public static void main(String[] ars) {
		EnvironmentReader.loadFromResource(PingMonitor.class, "app.properties");
		osBuilder = OSBuilder.build();
		timeout = Integer.valueOf(EnvironmentReader.getEnvironment("app.timeout"));
		packageSize = Integer.valueOf(EnvironmentReader.getEnvironment("app.packageSize"));
		sdf = new SimpleDateFormat(EnvironmentReader.getEnvironment("app.dateformat"));
		String ips = EnvironmentReader.getEnvironment("app.ips");
		String[] ipadress = ips.split(",");
		while (true) {
			for (String ip : ipadress) {
				ip = ip.replaceAll("\"", "");
				sendPingRequest(ip);
			}
			try {
				Thread.sleep(Long.valueOf(EnvironmentReader.getEnvironment("app.tick")));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
