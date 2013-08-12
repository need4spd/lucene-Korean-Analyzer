package com.tistory.devyongsik.analyzer.util;


public class AnalyzerTestUtil {
	protected TestToken getToken(String term, int start, int end) {
		TestToken t = new TestToken();
		t.setTerm(term);
		t.setStartOffset(start);
		t.setEndOffset(end);
		
		return t;
	}
}
