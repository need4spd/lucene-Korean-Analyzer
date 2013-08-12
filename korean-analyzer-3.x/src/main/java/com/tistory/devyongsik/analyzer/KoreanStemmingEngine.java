package com.tistory.devyongsik.analyzer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.lucene.util.AttributeSource;
import org.apache.lucene.util.AttributeSource.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tistory.devyongsik.analyzer.dictionary.DictionaryFactory;
import com.tistory.devyongsik.analyzer.dictionary.DictionaryType;

public class KoreanStemmingEngine implements Engine {	
	private static List<String> eomisJosaList = new ArrayList<String>();
	private Logger logger = LoggerFactory.getLogger(KoreanStemmingEngine.class);
	
	public KoreanStemmingEngine() {
		
		
		if(logger.isInfoEnabled()) {
			logger.info("init KoreanStemmingEngine..");
		}
		
		eomisJosaList = DictionaryFactory.getFactory().get(DictionaryType.EOMI);
	}
	
	@Override
	public void collectNounState(AttributeSource attributeSource, Stack<State> nounsStack, Map<String, String> returnedTokens) throws Exception {
		
		CharTermAttribute characterAttr = attributeSource.getAttribute(CharTermAttribute.class);
		OffsetAttribute offSetAttr = attributeSource.getAttribute(OffsetAttribute.class);
		
		String tokenTerm = characterAttr.toString();
		returnedTokens.put(tokenTerm+"_"+offSetAttr.startOffset()+"_"+offSetAttr.endOffset(), "");
		
		int removedWordLength = 0;
		
		for(String eomiJosa : eomisJosaList) {
			
			if(tokenTerm.endsWith(eomiJosa)) {
				//뒤에서부터 매칭되는 가장 긴 어미를 찾아 substring
				String stemmedWord = tokenTerm.substring(0, tokenTerm.length() - eomiJosa.length());
				removedWordLength = eomiJosa.length();
				
				if("".equals(stemmedWord)) {
					if(logger.isDebugEnabled())
						logger.debug("..in Stem : " + tokenTerm);
					
					return; //스테밍 할 것이 없음 Token=어미/조사인 경우임
				}
				
				CharTermAttribute termAttrResult = attributeSource.getAttribute(CharTermAttribute.class);
				//offset 재조정
				OffsetAttribute offSetAttrResult = attributeSource.getAttribute(OffsetAttribute.class);
				
				int startOffSet = offSetAttrResult.startOffset();
				int endOffSet = offSetAttrResult.endOffset() - removedWordLength;
				
				String makeKeyForCheck = stemmedWord + "_" + startOffSet + "_" + endOffSet;
				
				if(returnedTokens.containsKey(makeKeyForCheck)) {
					
					if(logger.isDebugEnabled()) {
						logger.debug("["+makeKeyForCheck+"] 는 이미 추출된 Token입니다. Skip");
					}
					
					continue;
					
				} else {
					returnedTokens.put(makeKeyForCheck, "");
				}
				
				termAttrResult.setEmpty();
				termAttrResult.append(stemmedWord);
			    
			    offSetAttrResult.setOffset(startOffSet, endOffSet);
			    
			    TypeAttribute typeAttrResult = attributeSource.getAttribute(TypeAttribute.class);
			    typeAttrResult.setType("stemmedword");

			    nounsStack.push(attributeSource.captureState());
			    
				if(logger.isDebugEnabled()) {
					logger.debug("tokenTerm : " + tokenTerm);
					logger.debug("stemming된 term : " + tokenTerm.substring(0, tokenTerm.length() - eomiJosa.length()));
				}
				
				return;
			}
		}

		return;
	}
}
