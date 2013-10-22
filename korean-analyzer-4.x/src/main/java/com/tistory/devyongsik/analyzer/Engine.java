package com.tistory.devyongsik.analyzer;

import java.util.List;
import java.util.Map;

import org.apache.lucene.util.AttributeSource;

public interface Engine {
	void collectNounState(AttributeSource attributeSource, List<ComparableState> comparableStateList, Map<String, String> returnedTokens) throws Exception;
}
