package com.tistory.devyongsik.analyzer;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DictionaryProperties {
	private Logger logger = LoggerFactory.getLogger(DictionaryProperties.class);
	
	private static DictionaryProperties instance = new DictionaryProperties();

	private Properties defaultProp = new Properties();
	private Properties customProp = new Properties();
	
	private String resourceName = "dictionary.properties";
	private final String defaultResourceName = "com/tistory/devyongsik/analyzer/dictionary.properties";

	private DictionaryProperties() {
		loadDefaultProperties();
		loadCustomProperties();
	}

	private void loadDefaultProperties() {
		if(logger.isDebugEnabled())
			logger.debug("load analyzer default properties..... : " + defaultResourceName);

		Class<DictionaryProperties> clazz = DictionaryProperties.class;
		
		InputStream in = clazz.getClassLoader().getResourceAsStream(defaultResourceName);
		
		if(in == null) {
			logger.error(defaultResourceName + " was not found!!!");
			throw new IllegalStateException(defaultResourceName + " was not found!!!");
		}

		try {
			defaultProp.load(in);
			in.close();
		} catch (IOException e) {
			logger.error(e.toString());
		}
		
		if(logger.isInfoEnabled()) {
			logger.info("default dictionary.properties : " + defaultProp);
		}
	}

	private void loadCustomProperties() {
		if(logger.isDebugEnabled())
			logger.debug("load analyzer custom properties..... : " + resourceName);

		Class<DictionaryProperties> clazz = DictionaryProperties.class;
		
		InputStream in = clazz.getClassLoader().getResourceAsStream(resourceName);
		
		if(in == null) {
			logger.warn(customProp + " was not found!!! skip load custom properties");
			return;
		}

		try {
			customProp.load(in);
			in.close();
		} catch (IOException e) {
			logger.error(e.toString());
		}
		
		if(logger.isInfoEnabled()) {
			logger.info("custom dictionary.properties : " + customProp);
		}
	}
	
	public static DictionaryProperties getInstance() {
		return instance;
	}

	public String getProperty(String key) {
		//read property value from custom properties first
		String value = customProp.getProperty(key);
		
		if(value == null) {
			value = defaultProp.getProperty(key);
		}
		
		return value.trim();
	}
}
