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

public class KoreanBaseNounEngine implements Engine {
	
	private Logger logger = LoggerFactory.getLogger(KoreanBaseNounEngine.class);
	
	private Map<String, String> customNounsDic = new HashMap<String, String>();
		
	public KoreanBaseNounEngine() {
		if(logger.isInfoEnabled()) {
			logger.info("init KoreanBaseNounEngine");
		}
		
		customNounsDic = DictionaryFactory.getFactory().getCustomNounDictionaryMap();
	}

	@Override
	public void collectNounState(AttributeSource attributeSource, List<ComparableState> comparableStateList, Map<String, String> returnedTokens) throws Exception {
		
		CharTermAttribute termAttr = attributeSource.getAttribute(CharTermAttribute.class);
		TypeAttribute typeAttr = attributeSource.getAttribute(TypeAttribute.class);
		OffsetAttribute offSetAttr = attributeSource.getAttribute(OffsetAttribute.class);
		PositionIncrementAttribute positionAttr = attributeSource.getAttribute(PositionIncrementAttribute.class);

		//Stack<State> nounsStack = new Stack<State>();

		if(!typeAttr.type().equals("word")) {
			
			if(logger.isDebugEnabled()) {
				logger.debug("명사 분석 대상이 아닙니다.");
			}
			
			return;
		}
		
		String term = termAttr.toString();
		//단어 자체에 대한 명사인지 평가
		if(customNounsDic.containsKey(term)) {
			typeAttr.setType("base_noun");
		}
		
		returnedTokens.put(term+"_"+offSetAttr.startOffset()+"_"+offSetAttr.endOffset(), "");
		
		String comparedWord = null;
		//1. 매칭이 되는대로 추출한다.
		int startIndex = 0;
		int endIndex = startIndex + 1;
		
		int orgStartOffset = offSetAttr.startOffset();
		
		boolean isPrevMatch = false;
		
		while(true) {
			
			if(endIndex > term.length()) {
				startIndex ++;
				endIndex = startIndex + 1;
			}
			
			if(startIndex >= term.length()) {
				break;
			}
			
			comparedWord = term.substring(startIndex, endIndex);
			
			//매칭될 때 State 저장
			if(customNounsDic.containsKey(comparedWord) && !term.equals(comparedWord)) {

				//offset도 계산해주어야 합니다. 그래야 하이라이팅이 잘 됩니다.
				int startOffSet = orgStartOffset + startIndex;
				int endOffSet = orgStartOffset + endIndex;
				
				String makeKeyForCheck = comparedWord + "_" + startOffSet + "_" + endOffSet;
				
				if(returnedTokens.containsKey(makeKeyForCheck)) {
					
					if(logger.isDebugEnabled()) {
						logger.debug("["+makeKeyForCheck+"] 는 이미 추출된 Token입니다. Skip");
					}
					
					endIndex++;
					isPrevMatch = true;
					
					continue;
					
				} else {
					returnedTokens.put(makeKeyForCheck, "");
				}
				
				termAttr.setEmpty();
				termAttr.append(comparedWord);

				positionAttr.setPositionIncrement(1);  //추출된 명사이기 때문에 위치정보를 1로 셋팅
				//타입을 noun으로 설정한다.
				typeAttr.setType("base_noun"); 
				
				offSetAttr.setOffset(startOffSet , endOffSet);
				
				ComparableState comparableState = new ComparableState();
				comparableState.setState(attributeSource.captureState());
				comparableState.setStartOffset(offSetAttr.startOffset());
				
				comparableStateList.add(comparableState);
				
				endIndex++;
				isPrevMatch = true;
				
			} else {
				if(isPrevMatch) {
					startIndex = endIndex - 1;
					endIndex = startIndex + 1;
				} else {
					endIndex++;
				}
				
				isPrevMatch = false;
			}
		}

		return;
	}
}
