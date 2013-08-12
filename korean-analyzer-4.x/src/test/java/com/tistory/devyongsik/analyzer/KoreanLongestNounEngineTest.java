package com.tistory.devyongsik.analyzer;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.tistory.devyongsik.analyzer.util.AnalyzerTestUtil;
import com.tistory.devyongsik.analyzer.util.TestToken;

public class KoreanLongestNounEngineTest extends AnalyzerTestUtil {
	private Set<TestToken> nouns = null;
	
	private List<Engine> engines = new ArrayList<Engine>();

	@Before
	public void initDictionary() {
		nouns = new HashSet<TestToken>();
		
		engines.add(new KoreanLongestNounEngine());
	}

	@Test
	public void testCase1() throws Exception {
		StringReader reader = new StringReader("서울지방경찰청");
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
	public void testCase2() throws Exception {
		StringReader reader = new StringReader("서울지방경찰청을");
		nouns.add(getToken("을", 7, 8));
		nouns.add(getToken("서울지방경찰청", 0, 7));
		nouns.add(getToken("서울지방경찰청을", 0, 8));
		
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
		StringReader reader = new StringReader("서울지방경찰청읔");
		nouns.add(getToken("서울지방경찰청", 0, 7));
		nouns.add(getToken("서울지방경찰청읔", 0, 8));
		
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
		StringReader reader = new StringReader("읔서울지방경찰청");
		nouns.add(getToken("서울지방경찰청", 1, 8));
		nouns.add(getToken("읔서울지방경찰청", 0, 8));
		
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
	public void testCase6() throws Exception {
		StringReader reader = new StringReader("검색엔진개발자");
		nouns.add(getToken("개발자", 4, 7));
		nouns.add(getToken("검색엔진", 0, 4));
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
	public void testCase7() throws Exception {
		StringReader reader = new StringReader("여러가지 방법을 사용해서 색인을 실시합니다.");
		nouns.add(getToken("합니다", 20, 23));
		nouns.add(getToken("실시", 18, 20));
		nouns.add(getToken("실시합니다", 18, 23));
		nouns.add(getToken("을", 16, 17));
		nouns.add(getToken("색인", 14, 16));
		nouns.add(getToken("색인을", 14, 17));
		nouns.add(getToken("사용", 9, 11));
		nouns.add(getToken("해서", 11, 13));
		nouns.add(getToken("사용해서", 9, 13));
		nouns.add(getToken("방법", 5, 7));
		nouns.add(getToken("을", 7, 8));
		nouns.add(getToken("방법을", 5, 8));
		nouns.add(getToken("여러", 0, 2));
		nouns.add(getToken("가지", 2, 4));
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
	public void testCase8() throws Exception {
		StringReader reader = new StringReader("상품판매읔출장소");
		nouns.add(getToken("출장소", 5, 8));
		nouns.add(getToken("상품", 0, 2));
		nouns.add(getToken("판매", 2, 4));
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
}
