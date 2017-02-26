package net.csa.tweet.analysis;

import java.util.ArrayList;
import java.util.List;

/**
 * @author philipp.amkreutz
 */
public class TweetAnalyzerResult {

	private List<TweetAnalysis> analysisList = new ArrayList<>();

	public TweetAnalyzerResult() {

	}

	public void addTweetAnalysis(TweetAnalysis tweetAnalysis) {
		this.analysisList.add(tweetAnalysis);
	}

	public void printResult() {
		for(TweetAnalysis tweetAnalysis : analysisList) {
			tweetAnalysis.printList();
		}
	}

}
