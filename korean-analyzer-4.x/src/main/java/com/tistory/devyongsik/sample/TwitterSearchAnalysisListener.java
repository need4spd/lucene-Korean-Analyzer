package com.tistory.devyongsik.sample;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

import javax.swing.JOptionPane;

import twitter4j.TwitterException;

import com.tistory.devyongsik.sample.TwittTrendKeywordViewer.TrendKeyword;

public class TwitterSearchAnalysisListener implements ActionListener {

	public void actionPerformed(ActionEvent e) {
		System.out.println("Command: " + e.getActionCommand());
		System.out.println("Command: " + e.paramString());
		System.out.println("Command: " + e.toString());
		if ("분석".equals(e.getActionCommand())) {

			String keyword = TwitterSearchAnalysisViewer.textTextArea.getText();

			TwittTrendKeywordViewer keywordViewer = new TwittTrendKeywordViewer();
			List<TrendKeyword> resultList = null;
			
			try {
				resultList = keywordViewer.execute(keyword);
			} catch (TwitterException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			StringBuffer textAreaResultString = new StringBuffer();
			for(TrendKeyword result : resultList) {
				textAreaResultString.append("[").append(result).append("],");
				textAreaResultString.append("\n");
			}

			TwitterSearchAnalysisViewer.resultTextArea.setText(textAreaResultString.toString());			

			JOptionPane.showMessageDialog(TwitterSearchAnalysisViewer.keywordAnalysis, "분석이 완료 되었습니다.", "분석", JOptionPane.INFORMATION_MESSAGE);
		} else if ("취소".equals(e.getActionCommand())) {
			TwitterSearchAnalysisViewer.textTextArea.setText("");
			TwitterSearchAnalysisViewer.resultTextArea.setText("");
		}		
	}
}
