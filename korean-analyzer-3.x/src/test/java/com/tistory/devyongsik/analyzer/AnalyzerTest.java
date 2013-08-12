package com.tistory.devyongsik.analyzer;

import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.tistory.devyongsik.analyzer.util.AnalyzerTestUtil;
import com.tistory.devyongsik.analyzer.util.TestToken;

public class AnalyzerTest extends AnalyzerTestUtil {
	private Set<TestToken> nouns = null;

	@Before
	public void initDictionary() {
		nouns = new HashSet<TestToken>();
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
		TokenStream stream = analyzer.reusableTokenStream("dummy", reader);
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
		
		analyzer.close();
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
		TokenStream stream = analyzer.reusableTokenStream("dummy", reader);
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
		
		analyzer.close();
	}
}
