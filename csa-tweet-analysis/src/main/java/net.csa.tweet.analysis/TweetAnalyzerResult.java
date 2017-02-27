package net.csa.tweet.analysis;

import java.util.*;

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
		Collections.sort(analysisList, new Comparator<TweetAnalysis>() {
			@Override
			public int compare(TweetAnalysis o1, TweetAnalysis o2) {
				return o2.getTweetCount() - o1.getTweetCount();
			}
		});
		System.out.println("TweetCount Ranking:");
		int i = 1;
		for (TweetAnalysis tweetAnalysis : analysisList) {
			System.out.println(i + ". Hashtag");
			tweetAnalysis.printList();
			i++;
		}

		Collections.sort(analysisList, new Comparator<TweetAnalysis>() {
			@Override
			public int compare(TweetAnalysis o1, TweetAnalysis o2) {
				return o2.getPositiveTweetCount() - o1.getPositiveTweetCount();
			}
		});
		System.out.println("---------------------------------------------------");
		System.out.println("PositiveTweetCount Ranking:");
		i = 1;
		for (TweetAnalysis tweetAnalysis : analysisList) {
			System.out.println(i + ". Hashtag");
			tweetAnalysis.printList();
			i++;
		}

	}

}
