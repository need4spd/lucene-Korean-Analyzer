package com.tistory.devyongsik.analyzer;

import java.util.Map;
import java.util.Stack;

import org.apache.lucene.util.AttributeSource;
import org.apache.lucene.util.AttributeSource.State;

public interface Engine {
	void collectNounState(AttributeSource attributeSource, Stack<State> nounsStack, Map<String, String> returnedTokens) throws Exception;
}
