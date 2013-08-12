package com.tistory.devyongsik.analyzer;

import java.io.IOException;
import java.util.Map;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tistory.devyongsik.analyzer.dictionary.DictionaryFactory;

public class KoreanStopFilter extends TokenFilter {

	private boolean enablePositionIncrements = false;

	private CharTermAttribute charTermAtt;
	private PositionIncrementAttribute posIncrAtt;
	private Logger logger = LoggerFactory.getLogger(KoreanStopFilter.class);
	private static Map<String, String> stopWordsMap = null;
	
	protected KoreanStopFilter(TokenStream input) {
		super(input);
		
		if(logger.isInfoEnabled()) {
			logger.info("init KoreanStopFilter");
		}
		charTermAtt = getAttribute(CharTermAttribute.class);
		posIncrAtt = getAttribute(PositionIncrementAttribute.class);
		
		DictionaryFactory dictionaryFactory = DictionaryFactory.getFactory();	
		stopWordsMap = dictionaryFactory.getStopWordsDictionary();
	}

	public void setEnablePositionIncrements(boolean enable) {
		this.enablePositionIncrements = enable;
	}

	public boolean getEnablePositionIncrements() {
		return enablePositionIncrements;
	}
	
	@Override
	public final boolean incrementToken() throws IOException {
		
		if(logger.isDebugEnabled())
			logger.debug("incrementToken KoreanStopFilter");


		// return the first non-stop word found
		int skippedPositions = 0;
		
		while(input.incrementToken()) {

			if(logger.isDebugEnabled())
				logger.debug("원래 리턴 될 TermAtt : " + charTermAtt.toString() + " , stopWordDic.isExist : " + stopWordsMap.containsKey(charTermAtt.toString()));

			if(!stopWordsMap.containsKey(charTermAtt.toString())) {
				if(enablePositionIncrements) {
					posIncrAtt.setPositionIncrement(posIncrAtt.getPositionIncrement() + skippedPositions);
				}

				return true;
			}

			skippedPositions += posIncrAtt.getPositionIncrement();
		}

		return false;
	}

}
