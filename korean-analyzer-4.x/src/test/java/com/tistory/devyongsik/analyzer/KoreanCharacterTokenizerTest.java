package com.tistory.devyongsik.analyzer;



import java.io.IOException;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;

import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.tistory.devyongsik.analyzer.util.AnalyzerTestUtil;
import com.tistory.devyongsik.analyzer.util.TestToken;

/**
 *
 * @author 장용석, 2011.07.16 need4spd@naver.com
 */

public class KoreanCharacterTokenizerTest extends AnalyzerTestUtil {
	
	private Set<TestToken> tokenizedToken = new HashSet<TestToken>();
	private StringReader content = new StringReader("삼성전자absc1234엠피3mp3버전1.2  띄어쓰기");
	private KoreanCharacterTokenizer tokenizer = new KoreanCharacterTokenizer(content);

	@Before
	public void setUp() throws IOException {
		tokenizedToken.add(getToken("띄어쓰기", 25, 29));
		tokenizedToken.add(getToken("2", 22, 23));
		tokenizedToken.add(getToken("1", 20, 21));
		tokenizedToken.add(getToken("버전", 18, 20));
		tokenizedToken.add(getToken("3",17, 18));
		tokenizedToken.add(getToken("mp", 15, 17));
		tokenizedToken.add(getToken("3", 14, 15));
		tokenizedToken.add(getToken("엠피", 12, 14));
		tokenizedToken.add(getToken("1234", 8, 12));
		tokenizedToken.add(getToken("absc", 4, 8));
		tokenizedToken.add(getToken("삼성전자", 0, 4));
		
		tokenizer.reset();
	}

	@Test
	public void testIncrementToken() throws IOException {
		CharTermAttribute charTermAtt = tokenizer.getAttribute(CharTermAttribute.class);
		OffsetAttribute offSetAtt = tokenizer.getAttribute(OffsetAttribute.class);
		
		while(tokenizer.incrementToken()) {
			TestToken t = getToken(charTermAtt.toString(), offSetAtt.startOffset(), offSetAtt.endOffset());
			System.out.println("termAtt.term() : " + charTermAtt.toString());
			System.out.println("offSetAtt : " + offSetAtt.startOffset());
			System.out.println("offSetAtt : " + offSetAtt.endOffset());

			Assert.assertTrue(tokenizedToken.contains(t));
		}
	}
}
