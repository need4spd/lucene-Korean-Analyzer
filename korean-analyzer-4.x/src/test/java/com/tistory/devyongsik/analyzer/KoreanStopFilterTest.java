package com.tistory.devyongsik.analyzer;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.TokenStream;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tistory.devyongsik.analyzer.dictionary.DictionaryFactory;
import com.tistory.devyongsik.analyzer.util.AnalyzerTestUtil;
import com.tistory.devyongsik.analyzer.util.TestToken;

public class KoreanStopFilterTest extends AnalyzerTestUtil {
	private List<TestToken> tokens = null;
	//불용어는 the와 .
	private StringReader reader = new StringReader("the 개발하고 꼭 이것을 잘 해야합니다. 공백입니다.");
	private DictionaryFactory dictionaryFactory = null;
	
	@Before
	public void setUp() {
		tokens = Lists.newArrayList();
		dictionaryFactory = DictionaryFactory.getFactory();
		
		tokens.add(getToken("공백입니다", 24, 29));
		tokens.add(getToken("해야합니다", 17, 22));
		tokens.add(getToken("이것을", 11, 14));
		tokens.add(getToken("개발하고", 4, 8));
		tokens.add(getToken("꼭", 9, 10));
		tokens.add(getToken("잘", 15, 16));
	}
	
	
	@Test
	public void stopFilter() throws IOException {
		
		Map<String, String> stopWordDictionaryMap = Maps.newHashMap();
		stopWordDictionaryMap.put("the", null);
		stopWordDictionaryMap.put(".", null);
		
		dictionaryFactory.setStopWordDictionaryMap(stopWordDictionaryMap);

		TokenStream stream = new KoreanStopFilter(new KoreanCharacterTokenizer(reader));
		stream.reset();
		
		List<TestToken> extractedTokens = collectExtractedNouns(stream);

		stream.close();

		verify(tokens, extractedTokens);
	}
}
