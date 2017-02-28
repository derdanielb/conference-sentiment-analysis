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
		System.out.println("Hashtag-ID: " + id);
		System.out.println("TweetCount: " + getTweetCount());
		System.out.println("PositiveTweetCount: " + getPositiveTweetCount());
		System.out.println("NegativeTweetCount: " + getNegativeTweetCount());
		System.out.println("NeutralTweetCount: " + getNeutralTweetCount());
		System.out.println("WordCount: " + getWordCount());
		System.out.println("------------------------");
		int i = 1;
		for(Tweet tweet : tweetList) {
			System.out.println("Tweet " + i);
			System.out.println("Score: " + tweet.getScore() + ((tweet.getScore() == 1) ? " (positive)" : (tweet.getScore() == -1) ? " (negative)" : " (neutral)"));
			System.out.println("Words: " + tweet.getWordCount());
			System.out.println("Text: " + tweet.getText());
			System.out.println("------------------------");
			i++;
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

	public int getNegativeTweetCount() {
		int negativeCount = 0;
		for(Tweet tweet : tweetList) {
			if(tweet.getScore() == -1) {
				negativeCount++;
			}
		}
		return negativeCount;
	}

	public int getNeutralTweetCount() {
		int neutralCount = 0;
		for(Tweet tweet : tweetList) {
			if(tweet.getScore() == 0) {
				neutralCount++;
			}
		}
		return neutralCount;
	}

	public int getWordCount() {
		int wordCount = 0;
		for(Tweet tweet : tweetList) {
			wordCount = wordCount + tweet.getWordCount();
		}
		return wordCount;
	}

}
