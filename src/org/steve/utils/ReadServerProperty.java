package org.steve.utils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.steve.base.Configs;

public class ReadServerProperty {

	private static Properties properties = new Properties();
	private static String propertiesPath = "conf/server.properties";
	
	private static Logger logger = Logger.getLogger(ReadServerProperty.class);
	
	public static Configs read(){
		try {
			InputStream in = new BufferedInputStream(new FileInputStream(propertiesPath));  
			properties.load(in);
			String port,stopPort;
			port = properties.getProperty("server.port","80");
			stopPort = properties.getProperty("server.stopport", "81");
			Configs conf = new Configs(port, stopPort);
			return conf;
		} catch (IOException e) {
			logger.error("Exception:", e);
		}
		return null;
	}
	
	public static Properties getProperties(){
		try {
			InputStream in = new BufferedInputStream(new FileInputStream(propertiesPath));  
			properties.load(in);
			return properties;
		} catch (IOException e) {
			logger.error("Exception:", e);
		}
		return null;
	}
}
