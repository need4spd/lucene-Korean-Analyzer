package com.tistory.devyongsik.analyzer;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.TokenStream;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tistory.devyongsik.analyzer.dictionary.DictionaryFactory;
import com.tistory.devyongsik.analyzer.util.AnalyzerTestUtil;
import com.tistory.devyongsik.analyzer.util.TestToken;

public class KoreanCompoundNounEngineTest extends AnalyzerTestUtil {
	private List<TestToken> compondNouns = Lists.newArrayList();
	private StringReader reader = new StringReader("월드컵조직위원회분과위");
	private List<Engine> engines = new ArrayList<Engine>();
	private DictionaryFactory dictionaryFactory;

	@Before
	public void initDictionary() {
		compondNouns.add(getToken("분과위", 8, 11));
		compondNouns.add(getToken("위원회", 5, 8));
		compondNouns.add(getToken("조직", 3, 5));
		compondNouns.add(getToken("월드컵", 0, 3));
		compondNouns.add(getToken("월드컵조직위원회분과위", 0, 11));
		
		dictionaryFactory = DictionaryFactory.getFactory();
	}

	@Test
	public void testCompoundNounExtract() throws Exception {
		Map<String, List<String>> compoundNounDictionaryMap = Maps.newHashMap();
		List<String> compoundList = Lists.newArrayList();
		compoundList.add("분과위");
		compoundList.add("위원회");
		compoundList.add("조직");
		compoundList.add("월드컵");
		
		compoundNounDictionaryMap.put("월드컵조직위원회분과위", compoundList);
		
		dictionaryFactory.setCompoundDictionaryMap(compoundNounDictionaryMap);
		
		createEngines();
		
		TokenStream stream = new KoreanNounFilter(new KoreanCharacterTokenizer(reader), engines);
		
		stream.reset();
		
		List<TestToken> extractedTokens = collectExtractedNouns(stream);
		
		stream.close();
		
		verify(compondNouns, extractedTokens);
	}
	
	private void createEngines() {
		engines.add(new KoreanCompoundNounEngine());
	}
	
}
