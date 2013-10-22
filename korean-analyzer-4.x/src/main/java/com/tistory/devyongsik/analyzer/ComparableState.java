package com.tistory.devyongsik.analyzer;

import org.apache.lucene.util.AttributeSource.State;

public class ComparableState implements Comparable<ComparableState> {

	private State state;
	private int startOffset;
	
	public State getState() {
		return state;
	}
	public void setState(State state) {
		this.state = state;
	}
	public int getStartOffset() {
		return startOffset;
	}
	public void setStartOffset(int startOffset) {
		this.startOffset = startOffset;
	}
	
	@Override
	public int compareTo(ComparableState comparableState) {
		return getStartOffset() - comparableState.getStartOffset();
	}
}
