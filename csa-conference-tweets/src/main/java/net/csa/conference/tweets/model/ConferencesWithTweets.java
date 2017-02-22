package net.csa.conference.tweets.model;


import java.util.HashMap;
import java.util.List;

public class ConferencesWithTweets {

    private List<HashMap> conferences;
    private List<String> tweets;

    public ConferencesWithTweets(List conferences, List tweets) {
        this.conferences = conferences;
        this.tweets = tweets;
    }

    public List getConferences() {
        return conferences;
    }

    public void setConferences(List conferences) {
        this.conferences = conferences;
    }

    public List getTweets() {
        return tweets;
    }

    public void setTweets(List tweets) {
        this.tweets = tweets;
    }
}
