package com.tistory.devyongsik.analyzer;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import org.apache.lucene.analysis.TokenStream;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.tistory.devyongsik.analyzer.dictionary.DictionaryFactory;
import com.tistory.devyongsik.analyzer.util.AnalyzerTestUtil;
import com.tistory.devyongsik.analyzer.util.TestToken;

public class KoreanSynonymEngineTest extends AnalyzerTestUtil {
	private List<String> synonymWordList = null;
	private List<Engine> engines = null;
	private DictionaryFactory dictionaryFactory = null;
	private List<TestToken> nouns = null;
	
	@Before
	public void setUp() throws Exception {
		
		synonymWordList = Lists.newArrayList();
		engines = Lists.newArrayList();
		dictionaryFactory = DictionaryFactory.getFactory();
		nouns = Lists.newArrayList();
		
		synonymWordList.add("노트북");
		synonymWordList.add("노트북pc");
		synonymWordList.add("노트북컴퓨터");
		synonymWordList.add("노트북피씨");
		synonymWordList.add("notebook");
		
		engines.add(new KoreanSynonymEngine());
		
		dictionaryFactory.setSynonymList(synonymWordList);
	}

	@Test
	public void testSynonym() throws IOException {
		StringReader reader = new StringReader("노트북");
		nouns.add(getToken("노트북", 0, 3));
		nouns.add(getToken("노트북pc", 0, 3));
		nouns.add(getToken("노트북컴퓨터", 0, 3));
		nouns.add(getToken("노트북피씨", 0, 3));
		nouns.add(getToken("notebook", 0, 3));
		
		TokenStream stream = new KoreanNounFilter(new KoreanCharacterTokenizer(reader), engines);
		stream.reset();
		
		List<TestToken> extractedTokens = collectExtractedNouns(stream);

		stream.close();

		verify(nouns, extractedTokens);
	}
}
