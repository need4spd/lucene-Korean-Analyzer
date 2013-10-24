package com.tistory.devyongsik.analyzer;

import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.tistory.devyongsik.analyzer.dictionary.DictionaryFactory;
import com.tistory.devyongsik.analyzer.util.AnalyzerTestUtil;
import com.tistory.devyongsik.analyzer.util.TestToken;

public class AnalyzerTest extends AnalyzerTestUtil {
	private List<TestToken> nouns = null;
	private DictionaryFactory dictionaryFactory;

	@Before
	public void initDictionary() {
		nouns = Lists.newArrayList();
		dictionaryFactory = DictionaryFactory.getFactory();
	}

	@Test
	public void testCase1() throws Exception {
		
		Map<String, String> customNounDictionaryMap = new HashMap<String, String>();
		customNounDictionaryMap.put("고속도로", null);
		customNounDictionaryMap.put("고속", null);
		customNounDictionaryMap.put("도로", null);
		
		dictionaryFactory.setCustomNounDictionaryMap(customNounDictionaryMap);

		StringReader reader = new StringReader("고속도로");

		nouns.add(getToken("고속도로", 0, 4));
		nouns.add(getToken("고속도", 0, 3));
		nouns.add(getToken("고속", 0, 2));
		nouns.add(getToken("속도", 1, 3));
		
		Analyzer analyzer = new KoreanAnalyzer(true);
		TokenStream stream = analyzer.tokenStream("dummy", reader);
		stream.reset();
		
		List<TestToken> extractedTokens = collectExtractedNouns(stream);

		analyzer.close();

		verify(nouns, extractedTokens);
	}
}
