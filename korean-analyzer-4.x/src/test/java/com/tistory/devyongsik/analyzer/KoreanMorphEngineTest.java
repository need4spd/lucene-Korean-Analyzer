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

/**
 * @author need4spd, need4spd@cplanet.co.kr, 2011. 10. 14.
 *
 */
public class KoreanMorphEngineTest extends AnalyzerTestUtil {
	private Set<TestToken> nouns = null;
	
	private List<Engine> engines = new ArrayList<Engine>();

	@Before
	public void initDictionary() {
		nouns = new HashSet<TestToken>();
		
		engines.add(new KoreanMorphEngine());
	}

	@Test
	public void testCase1() throws Exception {
		StringReader reader = new StringReader("기본사전이변경되었습니다");
		nouns.add(getToken("기본사전이변경", 0, 7));
		nouns.add(getToken("기본", 0, 2));
		nouns.add(getToken("전이", 3, 5));
		nouns.add(getToken("변경", 5, 7));
		nouns.add(getToken("기본사전이변경되었습니다", 0, 12));
		
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
		StringReader reader = new StringReader("worldcup경기장");
		nouns.add(getToken("worldcup", 0, 8));
		nouns.add(getToken("경기장", 8, 11));
		
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
