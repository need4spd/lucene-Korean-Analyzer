package com.tistory.devyongsik.analyzer;

import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.TokenStream;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.tistory.devyongsik.analyzer.dictionary.DictionaryFactory;
import com.tistory.devyongsik.analyzer.util.AnalyzerTestUtil;
import com.tistory.devyongsik.analyzer.util.TestToken;

public class KoreanLongestNounEngineTest extends AnalyzerTestUtil {
	private List<TestToken> nouns = null;
	private DictionaryFactory dictionaryFactory = null;
	private List<Engine> engines = null;

	@Before
	public void initDictionary() {
		nouns = Lists.newArrayList();
		engines = Lists.newArrayList();
		dictionaryFactory = DictionaryFactory.getFactory();
	}

	@Test
	public void testCase1() throws Exception {
		Map<String, String> customNounDictionaryMap = new HashMap<String, String>();
		customNounDictionaryMap.put("서울지방", null);
		customNounDictionaryMap.put("경찰청", null);
		customNounDictionaryMap.put("서울지방경찰청", null);

		dictionaryFactory.setCustomNounDictionaryMap(customNounDictionaryMap);

		createEngines();

		StringReader reader = new StringReader("서울지방경찰청");
		nouns.add(getToken("서울지방경찰청", 0, 7));

		TokenStream stream = new KoreanNounFilter(new KoreanCharacterTokenizer(reader), engines);
		stream.reset();

		List<TestToken> extractedTokens = collectExtractedNouns(stream);

		stream.close();

		verify(nouns, extractedTokens);
	}

	@Test
	public void testCase2() throws Exception {
		
		Map<String, String> customNounDictionaryMap = new HashMap<String, String>();
		customNounDictionaryMap.put("서울지방", null);
		customNounDictionaryMap.put("경찰청", null);
		customNounDictionaryMap.put("서울지방경찰청", null);

		dictionaryFactory.setCustomNounDictionaryMap(customNounDictionaryMap);

		createEngines();
		
		StringReader reader = new StringReader("서울지방경찰청을");
		nouns.add(getToken("서울지방경찰청", 0, 7));
		nouns.add(getToken("서울지방경찰청을", 0, 8));

		TokenStream stream = new KoreanNounFilter(new KoreanCharacterTokenizer(reader), engines);
		stream.reset();

		List<TestToken> extractedTokens = collectExtractedNouns(stream);

		stream.close();

		verify(nouns, extractedTokens);
	}

	@Test
	public void testCase3() throws Exception {
		Map<String, String> customNounDictionaryMap = new HashMap<String, String>();
		customNounDictionaryMap.put("서울지방", null);
		customNounDictionaryMap.put("경찰청", null);
		customNounDictionaryMap.put("서울지방경찰청", null);

		dictionaryFactory.setCustomNounDictionaryMap(customNounDictionaryMap);

		createEngines();
		
		StringReader reader = new StringReader("서울지방경찰청읔");
		nouns.add(getToken("서울지방경찰청", 0, 7));
		nouns.add(getToken("서울지방경찰청읔", 0, 8));

		TokenStream stream = new KoreanNounFilter(new KoreanCharacterTokenizer(reader), engines);
		stream.reset();

		List<TestToken> extractedTokens = collectExtractedNouns(stream);

		stream.close();

		verify(nouns, extractedTokens);
	}

	@Test
	public void testCase4() throws Exception {
		Map<String, String> customNounDictionaryMap = new HashMap<String, String>();
		customNounDictionaryMap.put("서울지방", null);
		customNounDictionaryMap.put("경찰청", null);
		customNounDictionaryMap.put("서울지방경찰청", null);

		dictionaryFactory.setCustomNounDictionaryMap(customNounDictionaryMap);

		createEngines();
		
		StringReader reader = new StringReader("읔서울지방경찰청");
		nouns.add(getToken("서울지방경찰청", 1, 8));
		nouns.add(getToken("읔서울지방경찰청", 0, 8));

		TokenStream stream = new KoreanNounFilter(new KoreanCharacterTokenizer(reader), engines);
		stream.reset();

		List<TestToken> extractedTokens = collectExtractedNouns(stream);

		stream.close();

		verify(nouns, extractedTokens);
	}

	@Test
	public void testCase5() throws Exception {
		Map<String, String> customNounDictionaryMap = new HashMap<String, String>();
		customNounDictionaryMap.put("삼성전자", null);
		customNounDictionaryMap.put("연수원", null);
		
		dictionaryFactory.setCustomNounDictionaryMap(customNounDictionaryMap);

		createEngines();
		
		StringReader reader = new StringReader("삼성전자연수원");
		nouns.add(getToken("연수원", 4, 7));
		nouns.add(getToken("삼성전자", 0, 4));
		nouns.add(getToken("삼성전자연수원", 0, 7));

		TokenStream stream = new KoreanNounFilter(new KoreanCharacterTokenizer(reader), engines);
		stream.reset();

		List<TestToken> extractedTokens = collectExtractedNouns(stream);

		stream.close();

		verify(nouns, extractedTokens);
	}

	@Test
	public void testCase6() throws Exception {
		Map<String, String> customNounDictionaryMap = new HashMap<String, String>();
		customNounDictionaryMap.put("검색", null);
		customNounDictionaryMap.put("엔진", null);
		customNounDictionaryMap.put("검색엔진", null);
		customNounDictionaryMap.put("개발", null);
		customNounDictionaryMap.put("개발자", null);
		
		
		dictionaryFactory.setCustomNounDictionaryMap(customNounDictionaryMap);

		createEngines();

		StringReader reader = new StringReader("검색엔진개발자");
		nouns.add(getToken("개발자", 4, 7));
		nouns.add(getToken("검색엔진", 0, 4));
		nouns.add(getToken("검색엔진개발자", 0, 7));

		TokenStream stream = new KoreanNounFilter(new KoreanCharacterTokenizer(reader), engines);
		stream.reset();

		List<TestToken> extractedTokens = collectExtractedNouns(stream);

		stream.close();

		verify(nouns, extractedTokens);
	}

	@Test
	public void testCase8() throws Exception {
		Map<String, String> customNounDictionaryMap = new HashMap<String, String>();
		customNounDictionaryMap.put("출장소", null);
		customNounDictionaryMap.put("상품", null);
		customNounDictionaryMap.put("판매", null);
		customNounDictionaryMap.put("상품판매출장소", null);
		
		dictionaryFactory.setCustomNounDictionaryMap(customNounDictionaryMap);

		createEngines();
		
		StringReader reader = new StringReader("상품판매읔출장소");
		nouns.add(getToken("출장소", 5, 8));
		nouns.add(getToken("상품", 0, 2));
		nouns.add(getToken("판매", 2, 4));
		nouns.add(getToken("상품판매읔출장소", 0, 8));

		TokenStream stream = new KoreanNounFilter(new KoreanCharacterTokenizer(reader), engines);
		stream.reset();

		List<TestToken> extractedTokens = collectExtractedNouns(stream);

		stream.close();

		verify(nouns, extractedTokens);
	}

	private void createEngines() {
		engines.add(new KoreanLongestNounEngine());
	}
}
