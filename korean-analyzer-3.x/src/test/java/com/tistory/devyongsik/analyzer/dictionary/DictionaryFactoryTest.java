package com.tistory.devyongsik.analyzer.dictionary;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

public class DictionaryFactoryTest {

	@Test
	public void loadDictionary() {
		DictionaryFactory factory = DictionaryFactory.getFactory();
		List<String> readWords = factory.getSynonymList();

		Assert.assertTrue(readWords.size() > 0);

	}
}
