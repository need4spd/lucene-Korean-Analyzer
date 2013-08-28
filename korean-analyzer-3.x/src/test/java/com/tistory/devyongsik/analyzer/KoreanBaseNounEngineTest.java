package com.tistory.devyongsik.analyzer;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.tistory.devyongsik.analyzer.util.AnalyzerTestUtil;
import com.tistory.devyongsik.analyzer.util.TestToken;

public class KoreanBaseNounEngineTest extends AnalyzerTestUtil {
	private Set<TestToken> nouns = null;
	private List<Engine> engines = new ArrayList<Engine>();

	@Before
	public void initDictionary() {
		nouns = new HashSet<TestToken>();
		engines.add(new KoreanBaseNounEngine());
	}

	@Test
	public void testCase1() throws Exception {
		StringReader reader = new StringReader("사랑하고회사동료동산");

		nouns.add(getToken("산", 9, 10));
		nouns.add(getToken("동산", 8, 10));
		nouns.add(getToken("동", 8, 9));
		nouns.add(getToken("동료", 6, 8));
		nouns.add(getToken("동", 6, 7));
		nouns.add(getToken("사", 5, 6));
		nouns.add(getToken("회사", 4, 6));
		nouns.add(getToken("회", 4, 5));
		nouns.add(getToken("하고", 2, 4));
		nouns.add(getToken("하", 2, 3));
		nouns.add(getToken("사랑", 0, 2));
		nouns.add(getToken("사", 0, 1));
		nouns.add(getToken("사랑하고회사동료동산", 0, 10));

		TokenStream stream = new KoreanNounFilter(new KoreanCharacterTokenizer(
				reader), engines);
		stream.reset();

		CharTermAttribute charTermAtt = stream
				.getAttribute(CharTermAttribute.class);
		OffsetAttribute offSetAtt = stream.getAttribute(OffsetAttribute.class);

		while (stream.incrementToken()) {
			TestToken t = getToken(charTermAtt.toString(),
					offSetAtt.startOffset(), offSetAtt.endOffset());
			System.out.println("termAtt.term() : " + charTermAtt.toString());
			System.out.println("offSetAtt : " + offSetAtt.startOffset());
			System.out.println("offSetAtt : " + offSetAtt.endOffset());

			Assert.assertTrue(nouns.contains(t));
		}

		stream.close();
	}

	@Test
	public void testCase2() throws Exception {
		StringReader reader = new StringReader("서울지방경찰청");

		nouns.add(getToken("청", 6, 7));
		nouns.add(getToken("찰", 5, 6));
		nouns.add(getToken("경찰청", 4, 7));
		nouns.add(getToken("경찰", 4, 6));
		nouns.add(getToken("경", 4, 5));
		nouns.add(getToken("지방", 2, 4));
		nouns.add(getToken("지", 2, 3));
		nouns.add(getToken("서울", 0, 2));
		nouns.add(getToken("서", 0, 1));
		nouns.add(getToken("서울지방경찰청", 0, 7));

		TokenStream stream = new KoreanNounFilter(new KoreanCharacterTokenizer(
				reader), engines);
		stream.reset();

		CharTermAttribute charTermAtt = stream
				.getAttribute(CharTermAttribute.class);
		OffsetAttribute offSetAtt = stream.getAttribute(OffsetAttribute.class);

		while (stream.incrementToken()) {
			TestToken t = getToken(charTermAtt.toString(),
					offSetAtt.startOffset(), offSetAtt.endOffset());
			System.out.println("termAtt.term() : " + charTermAtt.toString());
			System.out.println("offSetAtt : " + offSetAtt.startOffset());
			System.out.println("offSetAtt : " + offSetAtt.endOffset());

			Assert.assertTrue(nouns.contains(t));
		}

		stream.close();
	}

	@Test
	public void testCase3() throws Exception {
		StringReader reader = new StringReader("삼성전자연수원");

		nouns.add(getToken("원", 6, 7));
		nouns.add(getToken("수원", 5, 7));
		nouns.add(getToken("수", 5, 6));
		nouns.add(getToken("연수원", 4, 7));
		nouns.add(getToken("연수", 4, 6));
		nouns.add(getToken("연", 4, 5));
		nouns.add(getToken("전자", 2, 4));
		nouns.add(getToken("전", 2, 3));
		nouns.add(getToken("삼성", 0, 2));
		nouns.add(getToken("삼", 0, 1));
		nouns.add(getToken("삼성전자연수원", 0, 7));

		TokenStream stream = new KoreanNounFilter(new KoreanCharacterTokenizer(
				reader), engines);
		stream.reset();

		CharTermAttribute charTermAtt = stream
				.getAttribute(CharTermAttribute.class);
		OffsetAttribute offSetAtt = stream.getAttribute(OffsetAttribute.class);

		while (stream.incrementToken()) {
			TestToken t = getToken(charTermAtt.toString(),
					offSetAtt.startOffset(), offSetAtt.endOffset());
			System.out.println("termAtt.term() : " + charTermAtt.toString());
			System.out.println("offSetAtt : " + offSetAtt.startOffset());
			System.out.println("offSetAtt : " + offSetAtt.endOffset());

			Assert.assertTrue(nouns.contains(t));
		}

		stream.close();
	}

	@Test
	public void testCase4() throws Exception {
		StringReader reader = new StringReader("검색엔진개발자");

		nouns.add(getToken("자", 6, 7));
		nouns.add(getToken("발자", 5, 7));
		nouns.add(getToken("발", 5, 6));
		nouns.add(getToken("개발자", 4, 7));
		nouns.add(getToken("개발", 4, 6));
		nouns.add(getToken("개", 4, 5));
		nouns.add(getToken("엔진", 2, 4));
		nouns.add(getToken("엔", 2, 3));
		nouns.add(getToken("검색", 0, 2));
		nouns.add(getToken("검", 0, 1));
		nouns.add(getToken("검색엔진개발자", 0, 7));

		TokenStream stream = new KoreanNounFilter(new KoreanCharacterTokenizer(
				reader), engines);
		stream.reset();

		CharTermAttribute charTermAtt = stream
				.getAttribute(CharTermAttribute.class);
		OffsetAttribute offSetAtt = stream.getAttribute(OffsetAttribute.class);

		while (stream.incrementToken()) {
			TestToken t = getToken(charTermAtt.toString(),
					offSetAtt.startOffset(), offSetAtt.endOffset());
			System.out.println("termAtt.term() : " + charTermAtt.toString());
			System.out.println("offSetAtt : " + offSetAtt.startOffset());
			System.out.println("offSetAtt : " + offSetAtt.endOffset());

			Assert.assertTrue(nouns.contains(t));
		}

		stream.close();
	}

	@Test
	public void testCase5() throws Exception {
		StringReader reader = new StringReader("여러가지 방법을 사용해서 색인을 실시합니다.");

		nouns.add(getToken("합", 20, 21));
		nouns.add(getToken("니", 21, 22));
		nouns.add(getToken("실", 18, 19));
		nouns.add(getToken("실시", 18, 20));
		nouns.add(getToken("니", 21, 22));
		nouns.add(getToken("니다", 21, 23));
		nouns.add(getToken("다", 22, 23));
		nouns.add(getToken("실시합니다", 18, 23));
		nouns.add(getToken("색", 14, 15));
		nouns.add(getToken("색인", 14, 16));
		nouns.add(getToken("을", 16, 17));
		nouns.add(getToken("색인을", 14, 17));
		nouns.add(getToken("사", 9, 10));
		nouns.add(getToken("사용", 9, 11));
		nouns.add(getToken("해", 11, 12));
		nouns.add(getToken("해서", 11, 13));
		nouns.add(getToken("서", 12, 13));
		nouns.add(getToken("사용해서", 9, 13));
		nouns.add(getToken("방", 5, 6));
		nouns.add(getToken("방법", 5, 7));
		nouns.add(getToken("을", 7, 8));
		nouns.add(getToken("방법을", 5, 8));
		nouns.add(getToken("여", 0, 1));
		nouns.add(getToken("여러", 0, 2));
		nouns.add(getToken("가", 2, 3));
		nouns.add(getToken("가지", 2, 4));
		nouns.add(getToken("지", 3, 4));
		nouns.add(getToken("여러가지", 0, 4));

		TokenStream stream = new KoreanNounFilter(new KoreanCharacterTokenizer(
				reader), engines);
		stream.reset();

		CharTermAttribute charTermAtt = stream
				.getAttribute(CharTermAttribute.class);
		OffsetAttribute offSetAtt = stream.getAttribute(OffsetAttribute.class);

		while (stream.incrementToken()) {
			TestToken t = getToken(charTermAtt.toString(),
					offSetAtt.startOffset(), offSetAtt.endOffset());
			System.out.println("termAtt.term() : " + charTermAtt.toString());
			System.out.println("offSetAtt : " + offSetAtt.startOffset());
			System.out.println("offSetAtt : " + offSetAtt.endOffset());

			Assert.assertTrue(nouns.contains(t));
		}

		stream.close();
	}

	@Test
	public void testCase6() throws Exception {
		StringReader reader = new StringReader("상품판매읔출장소");

		nouns.add(getToken("소", 7, 8));
		nouns.add(getToken("장소", 6, 8));
		nouns.add(getToken("장", 6, 7));
		nouns.add(getToken("출장소", 5, 8));
		nouns.add(getToken("출장", 5, 7));
		nouns.add(getToken("출", 5, 6));
		nouns.add(getToken("판매", 2, 4));
		nouns.add(getToken("판", 2, 3));
		nouns.add(getToken("상", 0, 1));
		nouns.add(getToken("상품", 0, 2));
		nouns.add(getToken("상품판매읔출장소", 0, 8));

		TokenStream stream = new KoreanNounFilter(new KoreanCharacterTokenizer(
				reader), engines);
		stream.reset();

		CharTermAttribute charTermAtt = stream
				.getAttribute(CharTermAttribute.class);
		OffsetAttribute offSetAtt = stream.getAttribute(OffsetAttribute.class);

		while (stream.incrementToken()) {
			TestToken t = getToken(charTermAtt.toString(),
					offSetAtt.startOffset(), offSetAtt.endOffset());
			System.out.println("termAtt.term() : " + charTermAtt.toString());
			System.out.println("offSetAtt : " + offSetAtt.startOffset());
			System.out.println("offSetAtt : " + offSetAtt.endOffset());

			Assert.assertTrue(nouns.contains(t));
		}

		stream.close();
	}

	@Test
	public void testCase7() throws Exception {
		StringReader reader = new StringReader("검색엔진개발자읔");

		nouns.add(getToken("자", 6, 7));
		nouns.add(getToken("발자", 5, 7));
		nouns.add(getToken("발", 5, 6));
		nouns.add(getToken("개발자", 4, 7));
		nouns.add(getToken("개발", 4, 6));
		nouns.add(getToken("개", 4, 5));
		nouns.add(getToken("엔진", 2, 4));
		nouns.add(getToken("엔", 2, 3));
		nouns.add(getToken("검색", 0, 2));
		nouns.add(getToken("검", 0, 1));
		nouns.add(getToken("검색엔진개발자읔", 0, 8));

		TokenStream stream = new KoreanNounFilter(new KoreanCharacterTokenizer(
				reader), engines);
		stream.reset();

		CharTermAttribute charTermAtt = stream
				.getAttribute(CharTermAttribute.class);
		OffsetAttribute offSetAtt = stream.getAttribute(OffsetAttribute.class);
		TypeAttribute typeAttr = stream.getAttribute(TypeAttribute.class);

		while (stream.incrementToken()) {
			TestToken t = getToken(charTermAtt.toString(),
					offSetAtt.startOffset(), offSetAtt.endOffset());
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
		StringReader reader = new StringReader("랑콤");

		nouns.add(getToken("랑콤", 0, 2));

		TokenStream stream = new KoreanNounFilter(new KoreanCharacterTokenizer(
				reader), engines);
		stream.reset();

		CharTermAttribute charTermAtt = stream
				.getAttribute(CharTermAttribute.class);
		OffsetAttribute offSetAtt = stream.getAttribute(OffsetAttribute.class);
		TypeAttribute typeAttr = stream.getAttribute(TypeAttribute.class);

		while (stream.incrementToken()) {
			TestToken t = getToken(charTermAtt.toString(),
					offSetAtt.startOffset(), offSetAtt.endOffset());
			System.out.println("termAtt.term() : " + charTermAtt.toString());
			System.out.println("offSetAtt : " + offSetAtt.startOffset());
			System.out.println("offSetAtt : " + offSetAtt.endOffset());
			System.out.println("typeAtt : " + typeAttr.type());

			Assert.assertTrue(nouns.contains(t));
		}

		stream.close();
	}
}
