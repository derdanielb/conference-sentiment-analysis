package net.csa.tweet.analysis;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ConferenceAnalysis {

    private String hashtag = "";
    private List<String> tweets = new ArrayList<>();

    private int positiveCount = 0;
    private int neutralCount = 0;
    private int negativCount = 0;
    private int score = 0;
    private int wordCount = 0;

    public ConferenceAnalysis(String hashtag, List<String> tweets) {
        this.hashtag = hashtag;
        this.tweets = tweets;
    }

    public int getScore() {
        return score;
    }

    public void changeScore(int i) {
        score += (i-2);
    }

    public void incrementNegativeCount() {
        negativCount++;
    }

    public void incrementNeutralCount() {
        neutralCount++;
    }

    public void incrementPositiveCount() {
        positiveCount++;
    }

    public int getWordCount() {
        return wordCount;
    }

    public void setWordCount(int i) {
        wordCount = i;
    }

    public int getTweetCount() {
        return tweets.size();
    }

    public void setPositiveCount(int i) {
        positiveCount = i;
    }

    public void setNeutralCount(int i) {
        neutralCount = i;
    }

    public void setNegativeCount(int i) {
        negativCount = i;
    }

    public String getHashtag() {
        return hashtag;
    }

    public int getPositiveCount() {
        return positiveCount;
    }

    public int getNeutralCount() {
        return neutralCount;
    }

    public int getNegativCount() {
        return negativCount;
    }

    public List<String> getTweets() {
        return tweets;
    }

    public String toString() {
        return "Conference with hashtag #" + hashtag + ": tweetCount = " + getTweetCount() + ", wordCount = " + wordCount +
                ", positiveCount = " + positiveCount + ", neutralCount = " + neutralCount +
                ", negativeCount = " + negativCount;
    }

}
