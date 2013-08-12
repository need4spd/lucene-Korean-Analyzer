package com.tistory.devyongsik.sample;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Tweet;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

import com.tistory.devyongsik.analyzer.KoreanAnalyzer;

/**
 * @author need4spd, need4spd@cplanet.co.kr, 2011. 8. 31.
 *
 */
public class TwittTrendKeywordViewer {

	private Map<String,Integer> results = new HashMap<String,Integer>();

	private List<TrendKeyword> convertData() {

		List<TrendKeyword> tkList = new ArrayList<TrendKeyword>();

		Set<String> keywordSet = results.keySet();
		for(String keyword : keywordSet) {
			TrendKeyword tk = new TrendKeyword();
			tk.setKeyword(keyword);
			tk.setCount(results.get(keyword));

			tkList.add(tk);
		}

		Collections.sort(tkList);

		return tkList.subList(0, 100);
	}

	public List<TrendKeyword> execute(String keyword) throws TwitterException, IOException {
		int targetPage = 30;

		Twitter twitter = new TwitterFactory().getInstance();
		// The factory instance is re-useable and thread safe.
		for(int i = 1; i <= targetPage; i++) {
			Query query = new Query(keyword);
			query.setPage(i);
			QueryResult result = twitter.search(query);

			for (Tweet tweet : result.getTweets()) {
				System.out.println(tweet.getFromUser() + ":" + tweet.getText());

				Analyzer analyzer = new KoreanAnalyzer(false);
				TokenStream stream = analyzer.reusableTokenStream("dummy", new StringReader(tweet.getText()));

				CharTermAttribute charTermAtt = stream.getAttribute(CharTermAttribute.class);

				while(stream.incrementToken()) {
					//System.out.println(charTermAtt.toString());
					analysis(charTermAtt.toString());
				}
				
				analyzer.close();
			}
		}
		
		return convertData();
	}

	private void analysis(String keyword) {

		if(keyword.length() == 1) {
			return;
		}

		if(results.containsKey(keyword)) {
			int cnt = results.get(keyword);
			cnt++;
			results.put(keyword, cnt);
		} else {
			results.put(keyword, 1);
		}
	}

	public class TrendKeyword implements Comparable<TrendKeyword>{
		private String keyword;
		private int count;
		/**
		 * @return the keyword
		 */
		public String getKeyword() {
			return keyword;
		}
		/**
		 * @param keyword the keyword to set
		 */
		public void setKeyword(String keyword) {
			this.keyword = keyword;
		}
		/**
		 * @return the count
		 */
		public int getCount() {
			return count;
		}
		/**
		 * @param count the count to set
		 */
		public void setCount(int count) {
			this.count = count;
		}
		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + count;
			result = prime * result
					+ ((keyword == null) ? 0 : keyword.hashCode());
			return result;
		}
		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			TrendKeyword other = (TrendKeyword) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (count != other.count)
				return false;
			if (keyword == null) {
				if (other.keyword != null)
					return false;
			} else if (!keyword.equals(other.keyword))
				return false;
			return true;
		}
		private TwittTrendKeywordViewer getOuterType() {
			return TwittTrendKeywordViewer.this;
		}
		/* (non-Javadoc)
		 * @see java.lang.Comparable#compareTo(java.lang.Object)
		 */
		@Override
		public int compareTo(TrendKeyword o) {
			return o.getCount() - this.count;
		}
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "TrendKeyword [keyword=" + keyword + ", count=" + count
					+ "]";
		}
	}
}
