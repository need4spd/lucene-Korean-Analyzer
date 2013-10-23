package com.tistory.devyongsik.analyzer;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.tistory.devyongsik.analyzer.dictionary.DictionaryFactory;
import com.tistory.devyongsik.analyzer.util.AnalyzerTestUtil;
import com.tistory.devyongsik.analyzer.util.TestToken;

public class KoreanBaseNounEngineTest extends AnalyzerTestUtil {
	private Set<TestToken> nouns = null;	
	private List<Engine> engines = new ArrayList<Engine>();
	private DictionaryFactory dictionaryFactory;
	
	@Before
	public void initDictionary() {
		nouns = new HashSet<TestToken>();
		dictionaryFactory = DictionaryFactory.getFactory();
		engines = new ArrayList<Engine>();
	}

	@Test
	public void testCase1() throws Exception {
		
		Map<String, String> customNounDictionaryMap = new HashMap<String, String>();
		customNounDictionaryMap.put("사랑", null);
		customNounDictionaryMap.put("회사", null);
		customNounDictionaryMap.put("동료", null);
		customNounDictionaryMap.put("동산", null);
		
		dictionaryFactory.setCustomNounDictionaryMap(customNounDictionaryMap);
		
		engines.add(new KoreanBaseNounEngine());
		
		StringReader reader = new StringReader("사랑하고회사동료동산");
		
		nouns.add(getToken("동산", 8, 10));
		nouns.add(getToken("동료", 6, 8));
		nouns.add(getToken("회사", 4, 6));
		nouns.add(getToken("사랑", 0, 2));
		nouns.add(getToken("사랑하고회사동료동산", 0, 10));
				
		TokenStream stream = new KoreanNounFilter(new KoreanCharacterTokenizer(reader), engines);
		stream.reset();
		
		CharTermAttribute charTermAtt = stream.getAttribute(CharTermAttribute.class);
		OffsetAttribute offSetAtt = stream.getAttribute(OffsetAttribute.class);

		while(stream.incrementToken()) {
			TestToken t = getToken(charTermAtt.toString(), offSetAtt.startOffset(), offSetAtt.endOffset());
			System.out.println("termAtt.term() : " + charTermAtt.toString());
			System.out.println("offSetAtt : " + offSetAtt.startOffset());
			System.out.println("offSetAtt : " + offSetAtt.endOffset());

			Assert.assertTrue(nouns.contains(t));
		}
		
		stream.close();
	}
	
	@Test
	public void testCase2() throws Exception {
		Map<String, String> customNounDictionaryMap = new HashMap<String, String>();
		customNounDictionaryMap.put("서울", null);
		customNounDictionaryMap.put("지방", null);
		customNounDictionaryMap.put("경찰", null);
		customNounDictionaryMap.put("경찰청", null);
		customNounDictionaryMap.put("경", null);
		customNounDictionaryMap.put("방", null);
		
		dictionaryFactory.setCustomNounDictionaryMap(customNounDictionaryMap);
		
		engines.add(new KoreanBaseNounEngine());
		
		StringReader reader = new StringReader("서울지방경찰청");
		
		nouns.add(getToken("경찰청", 4, 7));
		nouns.add(getToken("경찰", 4, 6));
		nouns.add(getToken("경", 4, 5));
		nouns.add(getToken("지방", 2, 4));
		nouns.add(getToken("서울", 0, 2));
		nouns.add(getToken("서울지방경찰청", 0, 7));
		
		TokenStream stream = new KoreanNounFilter(new KoreanCharacterTokenizer(reader), engines);
		stream.reset();
		
		CharTermAttribute charTermAtt = stream.getAttribute(CharTermAttribute.class);
		OffsetAttribute offSetAtt = stream.getAttribute(OffsetAttribute.class);

		while(stream.incrementToken()) {
			TestToken t = getToken(charTermAtt.toString(), offSetAtt.startOffset(), offSetAtt.endOffset());
			System.out.println("termAtt.term() : " + charTermAtt.toString());
			System.out.println("offSetAtt : " + offSetAtt.startOffset());
			System.out.println("offSetAtt : " + offSetAtt.endOffset());

			Assert.assertTrue(nouns.contains(t));
		}
		
		stream.close();
	}
	
	@Test
	public void testCase3() throws Exception {
		Map<String, String> customNounDictionaryMap = new HashMap<String, String>();
		customNounDictionaryMap.put("삼성전자", null);
		customNounDictionaryMap.put("연수원", null);
		
		dictionaryFactory.setCustomNounDictionaryMap(customNounDictionaryMap);
		
		engines.add(new KoreanBaseNounEngine());
		
		StringReader reader = new StringReader("삼성전자연수원");
		
		nouns.add(getToken("연수원", 4, 7));
		nouns.add(getToken("삼성전자", 0, 4));
		nouns.add(getToken("삼성전자연수원", 0, 7));
		
		TokenStream stream = new KoreanNounFilter(new KoreanCharacterTokenizer(reader), engines);
		stream.reset();
		
		CharTermAttribute charTermAtt = stream.getAttribute(CharTermAttribute.class);
		OffsetAttribute offSetAtt = stream.getAttribute(OffsetAttribute.class);

		while(stream.incrementToken()) {
			TestToken t = getToken(charTermAtt.toString(), offSetAtt.startOffset(), offSetAtt.endOffset());
			System.out.println("termAtt.term() : " + charTermAtt.toString());
			System.out.println("offSetAtt : " + offSetAtt.startOffset());
			System.out.println("offSetAtt : " + offSetAtt.endOffset());

			Assert.assertTrue(nouns.contains(t));
		}
		
		stream.close();
	}
	
	@Test
	public void testCase4() throws Exception {
		Map<String, String> customNounDictionaryMap = new HashMap<String, String>();
		customNounDictionaryMap.put("검", null);
		customNounDictionaryMap.put("검색", null);
		customNounDictionaryMap.put("엔진", null);
		customNounDictionaryMap.put("개발자", null);
		
		dictionaryFactory.setCustomNounDictionaryMap(customNounDictionaryMap);
		
		engines.add(new KoreanBaseNounEngine());
		
		StringReader reader = new StringReader("검색엔진개발자");
		
		nouns.add(getToken("개발자", 4, 7));
		nouns.add(getToken("엔진", 2, 4));
		nouns.add(getToken("검색", 0, 2));
		nouns.add(getToken("검", 0, 1));
		nouns.add(getToken("검색엔진개발자", 0, 7));
			
		TokenStream stream = new KoreanNounFilter(new KoreanCharacterTokenizer(reader), engines);
		stream.reset();
		
		CharTermAttribute charTermAtt = stream.getAttribute(CharTermAttribute.class);
		OffsetAttribute offSetAtt = stream.getAttribute(OffsetAttribute.class);

		while(stream.incrementToken()) {
			TestToken t = getToken(charTermAtt.toString(), offSetAtt.startOffset(), offSetAtt.endOffset());
			System.out.println("termAtt.term() : " + charTermAtt.toString());
			System.out.println("offSetAtt : " + offSetAtt.startOffset());
			System.out.println("offSetAtt : " + offSetAtt.endOffset());

			Assert.assertTrue(nouns.contains(t));
		}
		
		stream.close();
	}
	
	@Test
	public void testCase5() throws Exception {
		Map<String, String> customNounDictionaryMap = new HashMap<String, String>();
		customNounDictionaryMap.put("색인", null);
		customNounDictionaryMap.put("방법", null);
		customNounDictionaryMap.put("실시", null);;
		
		dictionaryFactory.setCustomNounDictionaryMap(customNounDictionaryMap);
		
		engines.add(new KoreanBaseNounEngine());
		
		StringReader reader = new StringReader("여러가지 방법을 사용해서 색인을 실시합니다.");
		
		nouns.add(getToken("실시", 18, 20));
		nouns.add(getToken("실시합니다", 18, 23));
		nouns.add(getToken("색인", 14, 16));
		nouns.add(getToken("색인을", 14, 17));
		nouns.add(getToken("사용", 9, 11));
		nouns.add(getToken("사용해서", 9, 13));
		nouns.add(getToken("방법", 5, 7));
		nouns.add(getToken("방법을", 5, 8));
		nouns.add(getToken("여러가지", 0, 4));
		
		TokenStream stream = new KoreanNounFilter(new KoreanCharacterTokenizer(reader), engines);
		stream.reset();
		
		CharTermAttribute charTermAtt = stream.getAttribute(CharTermAttribute.class);
		OffsetAttribute offSetAtt = stream.getAttribute(OffsetAttribute.class);

		while(stream.incrementToken()) {
			TestToken t = getToken(charTermAtt.toString(), offSetAtt.startOffset(), offSetAtt.endOffset());
			System.out.println("termAtt.term() : " + charTermAtt.toString());
			System.out.println("offSetAtt : " + offSetAtt.startOffset());
			System.out.println("offSetAtt : " + offSetAtt.endOffset());

			Assert.assertTrue(nouns.contains(t));
		}
		
		stream.close();
	}
	
	@Test
	public void testCase6() throws Exception {
		Map<String, String> customNounDictionaryMap = new HashMap<String, String>();
		customNounDictionaryMap.put("상품", null);
		customNounDictionaryMap.put("판매", null);
		customNounDictionaryMap.put("출장소", null);;
		
		dictionaryFactory.setCustomNounDictionaryMap(customNounDictionaryMap);
		
		engines.add(new KoreanBaseNounEngine());
		
		StringReader reader = new StringReader("상품판매읔출장소");
		
		nouns.add(getToken("출장소", 5, 8));
		nouns.add(getToken("판매", 2, 4));
		nouns.add(getToken("상품", 0, 2));
		nouns.add(getToken("상품판매읔출장소", 0, 8));
		
		TokenStream stream = new KoreanNounFilter(new KoreanCharacterTokenizer(reader), engines);
		stream.reset();
		
		CharTermAttribute charTermAtt = stream.getAttribute(CharTermAttribute.class);
		OffsetAttribute offSetAtt = stream.getAttribute(OffsetAttribute.class);

		while(stream.incrementToken()) {
			TestToken t = getToken(charTermAtt.toString(), offSetAtt.startOffset(), offSetAtt.endOffset());
			System.out.println("termAtt.term() : " + charTermAtt.toString());
			System.out.println("offSetAtt : " + offSetAtt.startOffset());
			System.out.println("offSetAtt : " + offSetAtt.endOffset());

			Assert.assertTrue(nouns.contains(t));
		}
		
		stream.close();
	}
	
	@Test
	public void testCase7() throws Exception {
		Map<String, String> customNounDictionaryMap = new HashMap<String, String>();
		customNounDictionaryMap.put("검색", null);
		customNounDictionaryMap.put("엔진", null);
		customNounDictionaryMap.put("개발자", null);;
		
		dictionaryFactory.setCustomNounDictionaryMap(customNounDictionaryMap);
		
		engines.add(new KoreanBaseNounEngine());
		
		StringReader reader = new StringReader("검색엔진개발자읔");
		
		nouns.add(getToken("개발자", 4, 7));
		nouns.add(getToken("엔진", 2, 4));
		nouns.add(getToken("검색", 0, 2));
		nouns.add(getToken("검색엔진개발자읔", 0, 8));
		
		TokenStream stream = new KoreanNounFilter(new KoreanCharacterTokenizer(reader), engines);
		stream.reset();
		
		CharTermAttribute charTermAtt = stream.getAttribute(CharTermAttribute.class);
		OffsetAttribute offSetAtt = stream.getAttribute(OffsetAttribute.class);
		TypeAttribute typeAttr = stream.getAttribute(TypeAttribute.class);

		while(stream.incrementToken()) {
			TestToken t = getToken(charTermAtt.toString(), offSetAtt.startOffset(), offSetAtt.endOffset());
			System.out.println("termAtt.term() : " + charTermAtt.toString());
			System.out.println("offSetAtt : " + offSetAtt.startOffset());
			System.out.println("offSetAtt : " + offSetAtt.endOffset());
			System.out.println("typeAtt : " + typeAttr.type());
			
			Assert.assertTrue(nouns.contains(t));
		}
		
		stream.close();
	}
	
	@Test
	public void testCase8() throws Exception {
		Map<String, String> customNounDictionaryMap = new HashMap<String, String>();
		customNounDictionaryMap.put("랑콤", null);
		customNounDictionaryMap.put("엔진", null);
		
		dictionaryFactory.setCustomNounDictionaryMap(customNounDictionaryMap);
		
		engines.add(new KoreanBaseNounEngine());
		
		StringReader reader = new StringReader("랑콤");
		
		nouns.add(getToken("랑콤", 0, 2));
		
		TokenStream stream = new KoreanNounFilter(new KoreanCharacterTokenizer(reader), engines);
		stream.reset();
		
		CharTermAttribute charTermAtt = stream.getAttribute(CharTermAttribute.class);
		OffsetAttribute offSetAtt = stream.getAttribute(OffsetAttribute.class);
		TypeAttribute typeAttr = stream.getAttribute(TypeAttribute.class);

		while(stream.incrementToken()) {
			TestToken t = getToken(charTermAtt.toString(), offSetAtt.startOffset(), offSetAtt.endOffset());
			System.out.println("termAtt.term() : " + charTermAtt.toString());
			System.out.println("offSetAtt : " + offSetAtt.startOffset());
			System.out.println("offSetAtt : " + offSetAtt.endOffset());
			System.out.println("typeAtt : " + typeAttr.type());

			Assert.assertTrue(nouns.contains(t));
		}
		
		stream.close();
	}
}
