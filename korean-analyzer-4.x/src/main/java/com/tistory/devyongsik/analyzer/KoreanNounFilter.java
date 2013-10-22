package com.tistory.devyongsik.analyzer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KoreanNounFilter extends TokenFilter {
	private Logger logger = LoggerFactory.getLogger(KoreanNounFilter.class);
	
	private List<ComparableState> comparableStateList = new ArrayList<ComparableState>();
	private List<Engine> engines;
	private Map<String, String> returnedTokens = new HashMap<String, String>();
	
	protected KoreanNounFilter(TokenStream input, List<Engine> engines) {
		super(input);
		this.engines = engines;
	}

	@Override
	public final boolean incrementToken() throws IOException {
		
		
		if(logger.isDebugEnabled())
			logger.debug("incrementToken KoreanNounFilter");
		
		if(engines == null) {
			throw new IllegalStateException("KoreanNounFilter의 engines가 Null입니다.");
		}
		

		if (comparableStateList.size() > 0) {
			if(logger.isDebugEnabled())
				logger.debug("명사 Stack에서 토큰 리턴함");

			ComparableState comparableState = comparableStateList.get(0);
			comparableStateList.remove(0);
			State synState = comparableState.getState();
			restoreState(synState);

			return true;
		}

		if (!input.incrementToken())
			return false;
		
		try {
			
			for(Engine engine : engines) {
				engine.collectNounState(input.cloneAttributes(), comparableStateList , returnedTokens);
			}
			
			returnedTokens.clear();
			Collections.sort(comparableStateList); //startoffset이 순서대로 나오도록...
			
		} catch (Exception e) {
			logger.error("명사필터에서 목록 조회 오류");
			e.printStackTrace();
		}
		
		return true;
	}

}
