package com.tistory.devyongsik.sample;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;

import com.tistory.devyongsik.analyzer.KoreanAnalyzer;

public class Listener implements ActionListener {

	public void actionPerformed(ActionEvent e) {
		System.out.println("Command: " + e.getActionCommand());
		System.out.println("Command: " + e.paramString());
		System.out.println("Command: " + e.toString());
		if ("분석".equals(e.getActionCommand())) {

			String targetSentence = AnalysisResultViewer.textTextArea.getText();

			Analyzer analyzer = new KoreanAnalyzer(false);
			TokenStream stream = null;
			try {
				stream = analyzer.reusableTokenStream("dummy", new StringReader(targetSentence));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			CharTermAttribute charTermAtt = stream.getAttribute(CharTermAttribute.class);
			OffsetAttribute offsetAtt = stream.getAttribute(OffsetAttribute.class);
			
			List<KeywordToken> tokens = new ArrayList<KeywordToken>();
			try {
				while(stream.incrementToken()) {
					KeywordToken token = new KeywordToken();
					token.setKeyword(charTermAtt.toString());
					token.setStartOffset(offsetAtt.startOffset());
					token.setEndOffset(offsetAtt.endOffset());
					
					tokens.add(token);
				}
				
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			StringBuffer textAreaResultString = new StringBuffer();
			for(KeywordToken token : tokens) {
				textAreaResultString.append("[").append(token).append("],");
				textAreaResultString.append("\n");
			}

//			StringBuffer resultString = new StringBuffer();
//			resultString.append("Text =");
//			resultString.append(AnalysisResultViewer.textTextArea.getText());
//			resultString.append("\n");
//			resultString.append("URL =");
//			resultString.append(AnalysisResultViewer.urlTextArea.getText());
			AnalysisResultViewer.resultTextArea.setText(textAreaResultString.toString());			

			JOptionPane.showMessageDialog(AnalysisResultViewer.keywordAnalysis, "분석이 완료 되었습니다.", "분석", JOptionPane.INFORMATION_MESSAGE);
		} else if ("취소".equals(e.getActionCommand())) {
			AnalysisResultViewer.textTextArea.setText("");
			//AnalysisResultViewer.urlTextArea.setText("");
			AnalysisResultViewer.resultTextArea.setText("");
		}		
	}
	
	private class KeywordToken {
		private String keyword;
		private int startOffset;
		private int endOffset;
		public void setKeyword(String keyword) {
			this.keyword = keyword;
		}
		public void setStartOffset(int startOffset) {
			this.startOffset = startOffset;
		}
		public void setEndOffset(int endOffset) {
			this.endOffset = endOffset;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + endOffset;
			result = prime * result
					+ ((keyword == null) ? 0 : keyword.hashCode());
			result = prime * result + startOffset;
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			KeywordToken other = (KeywordToken) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (endOffset != other.endOffset)
				return false;
			if (keyword == null) {
				if (other.keyword != null)
					return false;
			} else if (!keyword.equals(other.keyword))
				return false;
			if (startOffset != other.startOffset)
				return false;
			return true;
		}
		private Listener getOuterType() {
			return Listener.this;
		}
		@Override
		public String toString() {
			return "KeywordToken [keyword=" + keyword + ", startOffset="
					+ startOffset + ", endOffset=" + endOffset + "]";
		}
	}
}
