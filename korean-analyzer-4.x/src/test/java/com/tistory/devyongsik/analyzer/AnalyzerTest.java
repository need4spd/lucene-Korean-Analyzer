package com.tistory.devyongsik.analyzer;

import java.io.StringReader;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.tistory.devyongsik.analyzer.util.AnalyzerTestUtil;
import com.tistory.devyongsik.analyzer.util.TestToken;

public class AnalyzerTest extends AnalyzerTestUtil {
	private List<TestToken> nouns = null;

	@Before
	public void initDictionary() {
		nouns = Lists.newArrayList();
	}

	@Test
	public void testCase1() throws Exception {
		StringReader reader = new StringReader("고속도로");

		nouns.add(getToken("고속도로", 0, 4));
		nouns.add(getToken("고속도", 0, 3));
		nouns.add(getToken("고속", 0, 2));
		nouns.add(getToken("속도", 1, 3));
		nouns.add(getToken("고", 0, 1));
		
		Analyzer analyzer = new KoreanAnalyzer(true);
		TokenStream stream = analyzer.tokenStream("dummy", reader);
		stream.reset();
		
		List<TestToken> extractedTokens = collectExtractedNouns(stream);

		analyzer.close();

		verify(nouns, extractedTokens);
	}
	
	@Test
	public void testCase2() throws Exception {
		StringReader reader = new StringReader("고속도로");

		nouns.add(getToken("고속도로", 0, 4));
		nouns.add(getToken("고속도", 0, 3));
		nouns.add(getToken("고속", 0, 2));
		nouns.add(getToken("속도", 1, 3));
		nouns.add(getToken("고", 0, 1));
		
		Analyzer analyzer = new KoreanAnalyzer(true);
		TokenStream stream = analyzer.tokenStream("dummy", reader);
		stream.reset();
		
		List<TestToken> extractedTokens = collectExtractedNouns(stream);

		analyzer.close();

		verify(nouns, extractedTokens);
	}
	
	@Test
	public void testCase3() throws Exception {
		StringReader reader = new StringReader("the big 입니다. dog");

		nouns.add(getToken("고속도로", 0, 4));
		nouns.add(getToken("고속도", 0, 3));
		nouns.add(getToken("고속", 0, 2));
		nouns.add(getToken("속도", 1, 3));
		nouns.add(getToken("고", 0, 1));
		
		Analyzer analyzer = new KoreanAnalyzer(true);
		TokenStream stream = analyzer.tokenStream("dummy", reader);
		stream.reset();

		List<TestToken> extractedTokens = collectExtractedNouns(stream);

		analyzer.close();

		verify(nouns, extractedTokens);
	}
}
