package com.tistory.devyongsik.analyzer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.apache.lucene.analysis.kr.morph.AnalysisOutput;
import org.apache.lucene.analysis.kr.morph.CompoundEntry;
import org.apache.lucene.analysis.kr.morph.MorphAnalyzer;
import org.apache.lucene.analysis.kr.morph.MorphException;
import org.apache.lucene.analysis.kr.morph.PatternConstants;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.lucene.util.AttributeSource;
import org.apache.lucene.util.AttributeSource.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author need4spd, need4spd@cplanet.co.kr, 2011. 10. 14.
 *
 */
public class KoreanMorphEngine implements Engine {

	private MorphAnalyzer morph = null;
	private Logger logger = LoggerFactory.getLogger(KoreanMorphEngine.class);
		
	public KoreanMorphEngine() {
		
		if(logger.isInfoEnabled()) {
			logger.info("init KoreanMorphEngine");
		}
		
		morph = new MorphAnalyzer();
	}

	
	@Override
	public void collectNounState(AttributeSource attributeSource, Stack<State> nounsStack, Map<String, String> returnedTokens)
			throws Exception {
		
		CharTermAttribute termAttr = attributeSource.getAttribute(CharTermAttribute.class);
		TypeAttribute typeAttr = attributeSource.getAttribute(TypeAttribute.class);
		OffsetAttribute offSetAttr = attributeSource.getAttribute(OffsetAttribute.class);
		
		if(!typeAttr.type().equals("word")) {
			if(logger.isDebugEnabled()) {
				logger.debug("명사 분석 대상이 아닙니다.");
			}
			return;
		}
		
		String term = termAttr.toString();
		returnedTokens.put(term+"_"+offSetAttr.startOffset()+"_"+offSetAttr.endOffset(), "");
		
		try {
	    	analysisKorean(attributeSource, nounsStack, returnedTokens);	
	 
	    } catch (MorphException e) {
	    	logger.error(e.getMessage());
	    }
	}
	
	@SuppressWarnings("unchecked")
	private void analysisKorean(AttributeSource attrSource, Stack<State> nounStateStack, Map<String, String> returnedTokens) throws MorphException {
		
		if(logger.isDebugEnabled())
			logger.debug("analysisKorean");

		CharTermAttribute termAttr = attrSource.getAttribute(CharTermAttribute.class);
		OffsetAttribute offsetAttr = attrSource.getAttribute(OffsetAttribute.class);

		String input = termAttr.toString();

		logger.info("morph engine input : " + input);
		
		List<AnalysisOutput> outputs = morph.analyze(input);

		//AnalysisOutput에는 각각의 단어에 대해 형태소 정보가 다 들어가 있고 getStem은 명사만 가져옴
		//여러개로 추측 하는 경우도 있음
		//ex> (에서사랑하고,0,6,type=<KOREAN>) -> [에서사랑하(N),고(j)],[에서사랑(N),하고(j)],[에서사랑하고(N)]
		if(logger.isDebugEnabled()) {
			for(AnalysisOutput output : outputs) {
				logger.debug("outputs : " + "["+ output.getStem() + "] : " + output.getScore());
				logger.debug("outputs all info : " + "["+ output + "] : " + output.getPos());
			}
		}

		//map에 추출된 명사/혹은 n-gram 추출 색인어를 넣어두고 나중에
		//하나하나를 token으로 koreanQueue에 집어 넣는다.
		Map<String,Integer> map = new HashMap<String,Integer>();
		for(AnalysisOutput output : outputs) {
			//명사(N) 만 처리
			if(output.getPos()==PatternConstants.POS_NOUN) {
				map.put(output.getStem(), new Integer(1));
				//점수가 100점이 아니면 n-gram 처리
				if(output.getScore()==AnalysisOutput.SCORE_CORRECT) {
					List<CompoundEntry> cnouns = output.getCNounList();

					for(CompoundEntry cnoun : cnouns) {
						if(cnoun.getWord().length()>1) {
							map.put(cnoun.getWord(),  new Integer(0));
						}
					}
				}
			}
		}

		Iterator<String> iter = map.keySet().iterator();
		
		State current = attrSource.captureState();

		while(iter.hasNext()) {
			String text = iter.next();

			//원본이 두번 추출되는 것을 막기 위해
			if(!input.equals(text)) {
				int index = input.indexOf(text);

				attrSource.restoreState(current); //attrSource를 다시 이전 상태로 restore

				CharTermAttribute termAttrResult = attrSource.addAttribute(CharTermAttribute.class);
				termAttrResult.setEmpty();
				termAttrResult.append(text);

			    PositionIncrementAttribute positionAttrResult = attrSource.addAttribute(PositionIncrementAttribute.class);
			    positionAttrResult.setPositionIncrement(0);

			    OffsetAttribute offsetAttrResult = attrSource.addAttribute(OffsetAttribute.class);
			    offsetAttrResult.setOffset(offsetAttr.startOffset() + (index!=-1?index:0), index!=-1?offsetAttr.startOffset()+index+text.length():offsetAttr.endOffset());

			    TypeAttribute typeAttrResult = attrSource.addAttribute(TypeAttribute.class);
			    typeAttrResult.setType("morph_noun");

			    String makeKeyForCheck = text + "_" + offsetAttrResult.startOffset() + "_" + offsetAttrResult.endOffset();
				
				if(returnedTokens.containsKey(makeKeyForCheck)) {
					if(logger.isDebugEnabled()) {
						logger.debug("["+makeKeyForCheck+"] 는 이미 추출된 Token입니다. Skip");
					}
				} else {
					nounStateStack.add(attrSource.captureState());
					
					if(logger.isDebugEnabled())
						logger.debug("추출 된 명사 : [" + termAttrResult.toString() + "]");
				}
			}
		}
	}
}
