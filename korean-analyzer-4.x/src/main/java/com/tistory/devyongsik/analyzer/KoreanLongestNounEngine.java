package com.tistory.devyongsik.analyzer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.lucene.util.AttributeSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tistory.devyongsik.analyzer.dictionary.DictionaryFactory;

public class KoreanLongestNounEngine implements Engine {

	private Logger logger = LoggerFactory.getLogger(KoreanLongestNounEngine.class);
	
	private static Map<String, String> customNounsDic = new HashMap<String, String>();
	
	
	public KoreanLongestNounEngine() {
		if(logger.isInfoEnabled()) {
			logger.info("init KoreanLongestNounEngine");
		}
		
		customNounsDic = DictionaryFactory.getFactory().getCustomNounDictionary();
	}

	@Override
	public void collectNounState(AttributeSource attributeSource, List<ComparableState> comparableStateList, Map<String, String> returnedTokens) throws Exception {
		
		
		CharTermAttribute termAttr = attributeSource.getAttribute(CharTermAttribute.class);
		TypeAttribute typeAttr = attributeSource.getAttribute(TypeAttribute.class);
		OffsetAttribute offSetAttr = attributeSource.getAttribute(OffsetAttribute.class);
		PositionIncrementAttribute positionAttr = attributeSource.getAttribute(PositionIncrementAttribute.class);

		if(!typeAttr.type().equals("word")) {
			
			if(logger.isDebugEnabled()) {
				logger.debug("명사 분석 대상이 아닙니다.");
			}
			
			return;
		}
		
		String term = termAttr.toString();
		//단어 자체에 대한 명사인지 평가
		if(customNounsDic.containsKey(term)) {
			typeAttr.setType("long_noun");
		}
		
		returnedTokens.put(term+"_"+offSetAttr.startOffset()+"_"+offSetAttr.endOffset(), "");
		
		String comparedWord = null;
		
		//1. 사전과 매칭되는 가장 긴 단어를 추출한다.
		int startIndex = 0;
		int endIndex = startIndex + 1;
		
		int orgStartOffSet = offSetAttr.startOffset();
		
		int prevMatchedStartIndex = 0;
		int prevMatchedEndIndex = 0;
		
		String matchedTerm = "";
		
		while(true) {
			
			if(endIndex > term.length()) {
				
				if(matchedTerm.length() > 0 && !term.equals(matchedTerm)) { //endIndex가 끝까지 갔고, 매칭된 키워드가 있음

					int startOffSet = orgStartOffSet + prevMatchedStartIndex;
					int endOffSet = orgStartOffSet + prevMatchedEndIndex;
					
					String makeKeyForCheck = matchedTerm + "_" + startOffSet + "_" + endOffSet;
					
					if(returnedTokens.containsKey(makeKeyForCheck)) {
						
						if(logger.isDebugEnabled()) {
							logger.debug("["+makeKeyForCheck+"] 는 이미 추출된 Token입니다. Skip");
						}
						
						matchedTerm = "";
						
						startIndex = prevMatchedEndIndex;
						endIndex = startIndex + 1;
						
						continue;
						
					} else {
						returnedTokens.put(makeKeyForCheck, "");
					}
					
					termAttr.setEmpty();
					termAttr.append(matchedTerm);

					positionAttr.setPositionIncrement(1);  //추출된 명사이기 때문에 위치정보를 1로 셋팅
					//타입을 noun으로 설정한다.
					typeAttr.setType("long_noun"); 
					
					offSetAttr.setOffset(startOffSet , endOffSet);
					
					ComparableState comparableState = new ComparableState();
					comparableState.setState(attributeSource.captureState());
					comparableState.setStartOffset(offSetAttr.startOffset());
					
					comparableStateList.add(comparableState);
					
					matchedTerm = "";
					
					startIndex = prevMatchedEndIndex;
					endIndex = startIndex + 1;
				} else {
					
					if(startIndex == prevMatchedEndIndex) {
						startIndex++;
						endIndex = startIndex + 1;
					} else {
						startIndex = endIndex;
						endIndex = startIndex + 1;
					}
				}
				
				
			}
			
			if(startIndex >= term.length()) {
				break;
			}
			
			comparedWord = term.substring(startIndex, endIndex);
			
			//매칭될 때 우선 matchedTerm에 저장
			if(customNounsDic.containsKey(comparedWord)) {
				matchedTerm = comparedWord;
				prevMatchedStartIndex = startIndex;
				prevMatchedEndIndex = endIndex;
			}
			
			endIndex++;
			
		}//end while

		return;
	}

}
