package net.csa.tweet.analysis;

import java.util.ArrayList;
import java.util.List;

/**
 * @author philipp.amkreutz
 */
public class TweetAnalysis {

	private int id;

	private List<Tweet> tweetList = new ArrayList<>();

	public TweetAnalysis(int id) {
		this.id = id;
	}

	public void addTweet(Tweet tweet) {
		tweetList.add(tweetList.size(), tweet);
	}

	public void printList() {
		System.out.println("ID: " + id);
		System.out.println("TweetCount: " + getTweetCount());
		System.out.println("PositiveTweetCount: " + getPositiveTweetCount());
		System.out.println("WordCount: " + getWordCount());
		for(Tweet tweet : tweetList) {
			System.out.println("TWEET");
			System.out.println("Score: " + tweet.getScore());
			System.out.println("Words: " + tweet.getWordCount());
			System.out.println("TEXT: " + tweet.getText());
		}
	}

	public int getTweetCount() {
		return tweetList.size();
	}

	public int getPositiveTweetCount() {
		int positiveCount = 0;
		for(Tweet tweet : tweetList) {
			if(tweet.getScore() == 1) {
				positiveCount++;
			}
		}
		return positiveCount;
	}

	public int getWordCount() {
		int wordCount = 0;
		for(Tweet tweet : tweetList) {
			wordCount = wordCount + tweet.getWordCount();
		}
		return wordCount;
	}

}
