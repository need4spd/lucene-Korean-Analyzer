package com.tistory.devyongsik.analyzer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.lucene.util.AttributeSource;
import org.apache.lucene.util.AttributeSource.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tistory.devyongsik.analyzer.dictionary.DictionaryFactory;

public class KoreanCompoundNounEngine implements Engine {
	
	private Logger logger = LoggerFactory.getLogger(KoreanCompoundNounEngine.class);
	private Map<String, List<String>> compoundNouns = new HashMap<String, List<String>>();
	
	public KoreanCompoundNounEngine() {
		if(logger.isInfoEnabled()) {
			logger.info("init KoreanCompoundNounEngine");
		}
		
		compoundNouns = DictionaryFactory.getFactory().getCompoundDictionary();
	}
	
	@Override
	public void collectNounState(AttributeSource attributeSource, Stack<State> nounsStack, Map<String, String> returnedTokens) throws Exception {
		
		
		CharTermAttribute termAttr = attributeSource.getAttribute(CharTermAttribute.class);
		TypeAttribute typeAttr = attributeSource.getAttribute(TypeAttribute.class);
		OffsetAttribute offSetAttr = attributeSource.getAttribute(OffsetAttribute.class);
		PositionIncrementAttribute positionAttr = attributeSource.getAttribute(PositionIncrementAttribute.class);

		String termString = termAttr.toString();
		returnedTokens.put(termString+"_"+offSetAttr.startOffset()+"_"+offSetAttr.endOffset(), "");
		
		//복합명사 사전에 있는 단어면
		List<String> matchedData = compoundNouns.get(termString);
		if(matchedData != null) {
			typeAttr.setType("compounds");

			for(String noun : matchedData) {
				
				if(logger.isDebugEnabled()) {
					logger.debug("복합명사추출 : " + noun);
				}
				
			    int startOffSet = termString.indexOf(noun);
			    int endOffSet = startOffSet + noun.length();
			    
			    String makeKeyForCheck = noun + "_" + startOffSet + "_" + endOffSet;
				
				if(returnedTokens.containsKey(makeKeyForCheck)) {
					if(logger.isDebugEnabled()) {
						logger.debug("["+makeKeyForCheck+"] 는 이미 추출된 Token입니다. Skip");
					}
					
					continue;
					
				} else {
					returnedTokens.put(makeKeyForCheck, "");
				}
				
				termAttr.setEmpty();
				termAttr.append(noun);
			    
				positionAttr.setPositionIncrement(1);
			    
			    offSetAttr.setOffset(startOffSet , endOffSet);
			    
			    typeAttr.setType("compound");
			    nounsStack.add(attributeSource.captureState());
			}
		}
		
		return;
	}
}
