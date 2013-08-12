package com.tistory.devyongsik.analyzer;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KoreanNounFilter extends TokenFilter {
	private Logger logger = LoggerFactory.getLogger(KoreanNounFilter.class);
	
	private Stack<State> nounsStack = new Stack<State>();
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
		

		if (nounsStack.size() > 0) {
			if(logger.isDebugEnabled())
				logger.debug("명사 Stack에서 토큰 리턴함");

			State synState = nounsStack.pop();
			restoreState(synState);

			return true;
		}

		if (!input.incrementToken())
			return false;
		
		try {
			
			for(Engine engine : engines) {
				engine.collectNounState(input.cloneAttributes(), nounsStack , returnedTokens);
			}
			
			returnedTokens.clear();
			
		} catch (Exception e) {
			logger.error("명사필터에서 목록 조회 오류");
			e.printStackTrace();
		}
		
		return true;
	}

}
