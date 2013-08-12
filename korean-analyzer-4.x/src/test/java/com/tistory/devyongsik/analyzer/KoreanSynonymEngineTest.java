package com.tistory.devyongsik.analyzer;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class KoreanSynonymEngineTest {
	private StringReader reader = new StringReader("노트북");

	private List<String> synonymWordList = new ArrayList<String>();
	private List<Engine> engines = new ArrayList<Engine>();
	
	@Before
	public void setUp() throws Exception {
		synonymWordList.add("노트북");
		synonymWordList.add("노트북pc");
		synonymWordList.add("노트북컴퓨터");
		synonymWordList.add("노트북피씨");
		synonymWordList.add("notebook");
		
		engines.add(new KoreanSynonymEngine());
	}

	@Test
	public void testSynonym() throws IOException {
		TokenStream stream = new KoreanNounFilter(new KoreanCharacterTokenizer(reader), engines);
		stream.reset();
		
		CharTermAttribute charTermAtt = stream.getAttribute(CharTermAttribute.class);
		OffsetAttribute offsetAtt = stream.getAttribute(OffsetAttribute.class);
		TypeAttribute typeAtt = stream.getAttribute(TypeAttribute.class);
		PositionIncrementAttribute positionAtt = stream.getAttribute(PositionIncrementAttribute.class);
		
		List<String> extractedSynonyms = new ArrayList<String>();
		
		while(stream.incrementToken()) {

			System.out.println("charTermAtt : " + charTermAtt.toString());
			System.out.println("offsetAtt start offset : " + offsetAtt.startOffset());
			System.out.println("offsetAtt end offset : " + offsetAtt.endOffset());
			System.out.println("typeAtt : " + typeAtt.type());
			System.out.println("positionAtt : " + positionAtt.getPositionIncrement());

			Assert.assertTrue(synonymWordList.contains(charTermAtt.toString()));
			
			extractedSynonyms.add(charTermAtt.toString());
		}
		
		for(String syn : synonymWordList) {
			Assert.assertTrue(extractedSynonyms.contains(syn));
		}
		
		stream.close();
	}
}
