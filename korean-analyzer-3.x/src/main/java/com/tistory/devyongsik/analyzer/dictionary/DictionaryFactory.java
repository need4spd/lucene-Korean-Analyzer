package com.tistory.devyongsik.analyzer.dictionary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tistory.devyongsik.analyzer.DictionaryProperties;
import com.tistory.devyongsik.analyzer.dictionaryindex.SynonymDictionaryIndex;

public class DictionaryFactory {
	private Logger logger = LoggerFactory.getLogger(DictionaryFactory.class);

	private static DictionaryFactory factory = new DictionaryFactory();
	private Map<DictionaryType, List<String>> dictionaryMap = new HashMap<DictionaryType, List<String>>();
	private Map<String, List<String>> compoundDictionaryMap = new HashMap<String, List<String>>();
	private Map<String, String> baseNounDictionaryMap = new HashMap<String, String>();
	private Map<String, String> customNounDictionaryMap = new HashMap<String, String>();
	private Map<String, String> stopDictionaryMap = new HashMap<String, String>();
	
		
	public static DictionaryFactory getFactory() {
		return factory;
	}

	private DictionaryFactory() {
		initDictionary();
	}
	
	private void initDictionary() {
		DictionaryType[] dictionaryTypes = DictionaryType.values();
		for(DictionaryType dictionaryType : dictionaryTypes) {
			if(logger.isInfoEnabled()) {
				logger.info("["+dictionaryType.getDescription()+"] "+"create wordset from file");
			}
			
			List<String> dictionary = loadDictionary(dictionaryType);
			dictionaryMap.put(dictionaryType, dictionary);
		}
		
		List<String> dictionaryData = dictionaryMap.get(DictionaryType.COMPOUND);
		String[] extractKey = null;
		String key = null;
		String[] nouns = null;
		
		for(String data : dictionaryData) {
			extractKey = data.split(":");
			key = extractKey[0];
			nouns = extractKey[1].split(",");
			
			compoundDictionaryMap.put(key, Arrays.asList(nouns));
		}
		
		List<String> baseNouns = dictionaryMap.get(DictionaryType.NOUN);
		for(String noun : baseNouns) {
			baseNounDictionaryMap.put(noun, null);
		}
		
		List<String> customNouns = dictionaryMap.get(DictionaryType.CUSTOM);
		for(String noun : customNouns) {
			customNounDictionaryMap.put(noun, null);
		}
		
		List<String> stopWords = dictionaryMap.get(DictionaryType.STOP);
		for(String stopWord : stopWords) {
			stopDictionaryMap.put(stopWord, null);
		}
	}
	
	public List<String> get(DictionaryType name) {
		return dictionaryMap.get(name);
	}
	
	public Map<String, List<String>> getCompoundDictionary() {
		return compoundDictionaryMap;
	}

	public Map<String, String> getBaseNounDictionary() {
		return baseNounDictionaryMap;
	}
	
	public Map<String, String> getCustomNounDictionary() {
		return customNounDictionaryMap;
	}
	
	public Map<String, String> getStopWordsDictionary() {
		return stopDictionaryMap;
	}
	
	private List<String> loadDictionary(DictionaryType name) {

		BufferedReader in = null;
		String dictionaryFile = DictionaryProperties.getInstance().getProperty(name.getPropertiesKey());
		InputStream inputStream = DictionaryFactory.class.getClassLoader().getResourceAsStream(dictionaryFile);

		if(inputStream == null) {
			logger.info("couldn't find dictionary : " + dictionaryFile);
			
			inputStream = DictionaryFactory.class.getResourceAsStream(dictionaryFile);
			
			logger.info(dictionaryFile + " file loaded.. from classloader.");
		}

		List<String> words = new ArrayList<String>();

		try {
			String readWord = "";
			in = new BufferedReader( new InputStreamReader(inputStream ,"utf-8"));
			
			
			while( (readWord = in.readLine()) != null ) {
				words.add(readWord.trim());
			}

			if(logger.isInfoEnabled()) {
				logger.info(name.getDescription() + " : " + words.size());
			}

			if(logger.isInfoEnabled()) {
				logger.info("create wordset from file complete");
			}

		}catch(IOException e){
			logger.error(e.toString());
		}finally{
			try {
				in.close();
			} catch (IOException e) {
				logger.error(e.toString());
			}
		}
		
		return words;
	}
	
	public void rebuildDictionary(DictionaryType dictionaryType) {
		
		if(DictionaryType.CUSTOM == dictionaryType) {
			List<String> customNouns = dictionaryMap.get(DictionaryType.CUSTOM);
			customNounDictionaryMap.clear();
			for(String noun : customNouns) {
				customNounDictionaryMap.put(noun, null);
			}
			
			return;
		}
		
		if(DictionaryType.COMPOUND == dictionaryType) {
			List<String> customNouns = dictionaryMap.get(DictionaryType.CUSTOM);
			customNounDictionaryMap.clear();
			for(String noun : customNouns) {
				customNounDictionaryMap.put(noun, null);
			}
		}
		
		if(DictionaryType.STOP == dictionaryType) {
			List<String> stopWords = dictionaryMap.get(DictionaryType.STOP);
			stopDictionaryMap.clear();
			for(String stopWord : stopWords) {
				stopDictionaryMap.put(stopWord, null);
			}
		}
		
		if(DictionaryType.SYNONYM == dictionaryType) {
			List<String> synonymWords = dictionaryMap.get(DictionaryType.SYNONYM);
			SynonymDictionaryIndex indexModule = SynonymDictionaryIndex.getIndexingModule();
			indexModule.indexingDictionary(synonymWords);
		}
	}
}
