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
		for (TweetAnalysis tweetAnalysis : analysisList) {
			tweetAnalysis.printList();
		}

		Collections.sort(analysisList, new Comparator<TweetAnalysis>() {
			@Override
			public int compare(TweetAnalysis o1, TweetAnalysis o2) {
				return o2.getTweetCount() - o1.getTweetCount();
			}
		});
		System.out.println("---------------------------------------------------");
		System.out.println("RESULTS");
		System.out.println("---------------------------------------------------");
		System.out.println("RANKING FOR TWEET-COUNT:");
		int i = 1;
		for (TweetAnalysis tweetAnalysis : analysisList) {
			System.out.println(i + ". Place");
			tweetAnalysis.printAnalysis();
			System.out.println("------------------------");
			i++;
		}

		Collections.sort(analysisList, new Comparator<TweetAnalysis>() {
			@Override
			public int compare(TweetAnalysis o1, TweetAnalysis o2) {
				return o2.getPositiveTweetCount() - o1.getPositiveTweetCount();
			}
		});
		System.out.println("---------------------------------------------------");
		System.out.println("RANKING FOR POSITIVE-TWEET-COUNT:");
		i = 1;
		for (TweetAnalysis tweetAnalysis : analysisList) {
			System.out.println(i + ". Place");
			tweetAnalysis.printAnalysis();
			System.out.println("------------------------");
			i++;
		}

	}

}
