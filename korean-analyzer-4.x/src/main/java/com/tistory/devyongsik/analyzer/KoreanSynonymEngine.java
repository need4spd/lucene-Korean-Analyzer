package com.tistory.devyongsik.analyzer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.SearcherManager;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.util.AttributeSource;
import org.apache.lucene.util.AttributeSource.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tistory.devyongsik.analyzer.dictionary.DictionaryFactory;
import com.tistory.devyongsik.analyzer.dictionary.DictionaryType;
import com.tistory.devyongsik.analyzer.dictionaryindex.SynonymDictionaryIndex;

public class KoreanSynonymEngine implements Engine {

	private Logger logger = LoggerFactory.getLogger(KoreanSynonymEngine.class);

	static {
		DictionaryFactory dictionaryFactory = DictionaryFactory.getFactory();
		createSynonymIndex(dictionaryFactory.get(DictionaryType.SYNONYM));
	}
	
	private static void createSynonymIndex(List<String> synonyms) {

		SynonymDictionaryIndex indexingModule = SynonymDictionaryIndex.getIndexingModule();
		indexingModule.indexingDictionary(synonyms);
	}

	private List<String> getWords(String word) throws Exception {
		List<String> synWordList = new ArrayList<String>();
		if(logger.isDebugEnabled()) {
			logger.debug("동의어 탐색 : " + word);
		}

		Query query = new TermQuery(new Term("syn",word));

		if(logger.isDebugEnabled()) {
			logger.debug("query : " + query);
		}
		
		SynonymDictionaryIndex indexingModule = SynonymDictionaryIndex.getIndexingModule();
		SearcherManager searcherManager = indexingModule.getSearcherManager();
		searcherManager.maybeRefresh();
		IndexSearcher indexSearcher = searcherManager.acquire();
		
		TopScoreDocCollector collector = TopScoreDocCollector.create(5 * 5, false);
		indexSearcher.search(query, collector);
		ScoreDoc[] hits = collector.topDocs().scoreDocs;

		if(logger.isDebugEnabled()) {
			logger.debug("대상 word : " + word);
			//검색된 document는 하나이므로..
			logger.debug("동의어 갯수 : " + hits.length);
		}

		for(int i = 0; i < hits.length; i++) {
			Document doc = indexSearcher.doc(hits[i].doc);

			String[] values = doc.getValues("syn");

			for(int j = 0; j < values.length; j++) {
				if(logger.isDebugEnabled())
					logger.debug("대상 word : " + "["+word+"]" + " 추출된 동의어 : " + values[j]);

				if(!word.equals(values[j])) {
					synWordList.add(values[j]);
				}
			}
		}
		
		searcherManager.release(indexSearcher);
		indexSearcher = null;
		
		return synWordList;
	}

	@Override
	public void collectNounState(AttributeSource attributeSource, Stack<State> nounsStack, Map<String, String> returnedTokens) throws Exception {
		CharTermAttribute charTermAttr = attributeSource.getAttribute(CharTermAttribute.class);
		OffsetAttribute offSetAttr = attributeSource.getAttribute(OffsetAttribute.class);

		returnedTokens.put(charTermAttr.toString()+"_"+offSetAttr.startOffset()+"_"+offSetAttr.endOffset(), "");

		if(logger.isDebugEnabled())
			logger.debug("넘어온 Term : " + charTermAttr.toString());

		List<String> synonyms = getWords(charTermAttr.toString());

		if (synonyms.size() == 0) new Stack<State>(); //동의어 없음

		for (int i = 0; i < synonyms.size(); i++) {

			String synonymWord = synonyms.get(i);
			String makeKeyForCheck = synonymWord + "_" + offSetAttr.startOffset() + "_" + offSetAttr.endOffset();

			if(returnedTokens.containsKey(makeKeyForCheck)) {

				if(logger.isDebugEnabled()) {
					logger.debug("["+makeKeyForCheck+"] 는 이미 추출된 Token입니다. Skip");
				}

				continue;

			} else {
				returnedTokens.put(makeKeyForCheck, "");
			}

			//#1. 동의어는 키워드 정보와 Type정보, 위치증가정보만 변경되고 나머지 속성들은 원본과 동일하기 때문에
			//attributeSource로부터 변경이 필요한 정보만 가져와서 필요한 정보를 변경한다.
			//offset은 원본과 동일하기 때문에 건드리지 않는다.
			CharTermAttribute attr = attributeSource.getAttribute(CharTermAttribute.class); //원본을 복사한 AttributeSource의 Attribute를 받아옴
			attr.setEmpty();
			attr.append(synonyms.get(i));
			PositionIncrementAttribute positionAttr = attributeSource.getAttribute(PositionIncrementAttribute.class); //원본 AttributeSource의 Attribute를 받아옴
			positionAttr.setPositionIncrement(0);  //동의어이기 때문에 위치정보 변하지 않음
			TypeAttribute typeAtt = attributeSource.getAttribute(TypeAttribute.class); //원본 AttributeSource의 Attribute를 받아옴
			//타입을 synonym으로 설정한다. 나중에 명사추출 시 동의어 타입은 건너뛰기 위함
			typeAtt.setType("synonym"); 

			nounsStack.push(attributeSource.captureState()); //추출된 동의어에 대한 AttributeSource를 Stack에 저장
		}
		return;
	}
}
