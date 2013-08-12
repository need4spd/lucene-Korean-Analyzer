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

public class KoreanStemEngineTest extends AnalyzerTestUtil {
	private Set<TestToken> stemmingToken = new HashSet<TestToken>();
	private StringReader reader = new StringReader("사랑하고 사랑치고는 사랑커녕");
	private List<Engine> engines = new ArrayList<Engine>();
	
	@Before
	public void initDictionary() {
		stemmingToken.add(getToken("사랑", 11, 13));
		stemmingToken.add(getToken("사랑커녕", 11, 15));
		stemmingToken.add(getToken("사랑치고는", 5, 10));
		stemmingToken.add(getToken("사랑하고", 0, 4));
		stemmingToken.add(getToken("사랑하", 0, 3));
		stemmingToken.add(getToken("사랑치", 5, 8));
		
		engines.add(new KoreanStemmingEngine());
	}

	@Test
	public void testStemCase() throws Exception {
		TokenStream stream = new KoreanNounFilter(new KoreanCharacterTokenizer(reader), engines);
		stream.reset();
		
		CharTermAttribute charTermAtt = stream.getAttribute(CharTermAttribute.class);
		OffsetAttribute offSetAtt = stream.getAttribute(OffsetAttribute.class);

		while(stream.incrementToken()) {
			TestToken t = getToken(charTermAtt.toString(), offSetAtt.startOffset(), offSetAtt.endOffset());
			System.out.println("termAtt.term() : " + charTermAtt.toString());
			System.out.println("offSetAtt : " + offSetAtt.startOffset());
			System.out.println("offSetAtt : " + offSetAtt.endOffset());

			Assert.assertTrue(stemmingToken.contains(t));
		}
		
		stream.close();
	}
}
