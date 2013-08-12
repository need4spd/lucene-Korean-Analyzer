package com.tistory.devyongsik.analyzer;

import junit.framework.Assert;

import org.junit.Test;

public class DictionaryPropertiesTest {

	@Test
	public void propertiesLoad() {
		DictionaryProperties dp = DictionaryProperties.getInstance();
		Assert.assertNotNull(dp);
	}
}
