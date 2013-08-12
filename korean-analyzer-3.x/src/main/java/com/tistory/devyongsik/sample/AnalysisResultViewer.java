package com.tistory.devyongsik.sample;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class AnalysisResultViewer extends JFrame {

	private static final long serialVersionUID = 92433123423362153L;
	
	public static String TITLE = "KeywordAnalysis";
	
	
	public static AnalysisResultViewer keywordAnalysis;
	public static JTextArea textTextArea = new JTextArea();
	public static JTextArea urlTextArea = new JTextArea();
	public static JTextArea resultTextArea = new JTextArea();

	public AnalysisResultViewer(){
		// close 버튼 클릭시 action
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// 창 사이즈
		setSize(800,500);
		
		// 프로그램 아이콘 설정(16pixel)
		//URL src = AnalysisResultViewer.class.getResource("/img/icon/analysis-icon.png");
		//setIconImage(new ImageIcon(src).getImage());
		
		// 타이틀설정
		setTitle(TITLE);
		
		// 레이아웃 설정
		JPanel backgroundPanel = new JPanel(new GridLayout(2, 1));	
		
		JPanel topMainPanel = new JPanel(new GridLayout(1, 1));
		
		// text 입력 패널
		JPanel textInputPanel = new JPanel(new BorderLayout());	
		JLabel textLabel = new JLabel("Text    : ");
		textInputPanel.add(textLabel,BorderLayout.WEST);
		JScrollPane textScrollPane = new JScrollPane();
		textScrollPane.setViewportView(textTextArea);
		textScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		textScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		//textScrollPane.setBounds(new Rectangle(0, 0, 100, 200));
		textInputPanel.add(textScrollPane,BorderLayout.CENTER);
		
		// url 입력 패널
//		JPanel urlInputPanel = new JPanel(new BorderLayout());
//		urlInputPanel.add(new JLabel("URL     : "),BorderLayout.WEST);
//		JScrollPane urlScrollPane = new JScrollPane();
//		urlScrollPane.setViewportView(urlTextArea);
//		urlScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
//		urlScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
//		urlInputPanel.add(urlScrollPane,BorderLayout.CENTER);

		topMainPanel.add(textInputPanel);
//		topMainPanel.add(urlInputPanel);
		
		// 분석 결과 패널
		JPanel resultPanel = new JPanel(new BorderLayout());
		resultPanel.add(new JLabel("Result : "),BorderLayout.WEST);
		JScrollPane resultScrollPane = new JScrollPane();
		resultTextArea.setEditable(false);
		resultScrollPane.setViewportView(resultTextArea);
		resultScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		resultScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		resultPanel.add(resultScrollPane,BorderLayout.CENTER);		

		// 버튼 패널
		JPanel btnPanel = new JPanel(new GridLayout(1, 2));
		JButton analysisBtn = new JButton("분석");	
		analysisBtn.addActionListener(new Listener());
		JButton cancelBtn = new JButton("취소");
		cancelBtn.addActionListener(new Listener());
		btnPanel.add(analysisBtn);
		btnPanel.add(cancelBtn);	
		
		backgroundPanel.add(topMainPanel);
		backgroundPanel.add(resultPanel);
		
		add(backgroundPanel,BorderLayout.CENTER);
		add(btnPanel,BorderLayout.SOUTH);
	}

	public static void main(String[] args) {
		keywordAnalysis = new AnalysisResultViewer();
		keywordAnalysis.setVisible(true);
	}
}
