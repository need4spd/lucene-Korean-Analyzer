package com.tistory.devyongsik.analyzer;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.tistory.devyongsik.analyzer.util.AnalyzerTestUtil;
import com.tistory.devyongsik.analyzer.util.TestToken;

public class KoreanStopFilterTest extends AnalyzerTestUtil {
	private static Set<TestToken> tokens = new HashSet<TestToken>();
	//불용어는 the와 .
	StringReader reader = new StringReader("the 개발하고 꼭 이것을 잘 해야합니다. 공백입니다.");

	@Before
	public void setUp() {
		tokens.add(getToken("공백입니다", 24, 29));
		tokens.add(getToken("해야합니다", 17, 22));
		tokens.add(getToken("이것을", 11, 14));
		tokens.add(getToken("개발하고", 4, 8));
	}
	
	
	@Test
	public void stopFilter() throws IOException {
		TokenStream stream = new KoreanStopFilter(new KoreanCharacterTokenizer(reader));
		stream.reset();
		
		CharTermAttribute charTermAtt = stream.getAttribute(CharTermAttribute.class);
		OffsetAttribute offSetAtt = stream.getAttribute(OffsetAttribute.class);
		
		while(stream.incrementToken()) {
			TestToken t = getToken(charTermAtt.toString(), offSetAtt.startOffset(), offSetAtt.endOffset());
			System.out.println("termAtt.term() : " + charTermAtt.toString());
			System.out.println("offSetAtt : " + offSetAtt.startOffset());
			System.out.println("offSetAtt : " + offSetAtt.endOffset());

			Assert.assertTrue(tokens.contains(t));
		}
		
		stream.close();
	}
}
