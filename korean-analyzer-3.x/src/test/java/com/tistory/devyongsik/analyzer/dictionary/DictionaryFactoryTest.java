package com.tistory.devyongsik.analyzer.dictionary;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

public class DictionaryFactoryTest {
	
	@Test
	public void loadDictionary() {
		DictionaryType[] dics = DictionaryType.values();
		for(DictionaryType dic : dics) {
			DictionaryFactory factory = DictionaryFactory.getFactory();
			List<String> readWords = factory.get(dic);
			
			Assert.assertTrue(readWords.size() > 0);
		}
	}
}
