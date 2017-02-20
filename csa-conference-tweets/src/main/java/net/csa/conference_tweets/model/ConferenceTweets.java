package net.csa.conference_tweets.model;

import java.util.List;
import java.util.Objects;

public class ConferenceTweets extends Conference {
    private List<String> tweets;

    public ConferenceTweets() {}

    public ConferenceTweets(Conference c, List<String> tweets) {
        setUuid(c.getUuid());
        setName(c.getName());
        setTimeSpan(c.getTimeSpan());
        setLocation(c.getLocation());
        setHashTag(c.getHashTag());
        setOrganisers(c.getOrganisers());
        setSponsors(c.getSponsors());
        this.tweets = tweets;
    }

    public List<String> getTweets() {
        return tweets;
    }

    public void setTweets(List<String> tweets) {
        this.tweets = tweets;
    }

    @Override
    public boolean equals(Object obj) {
        if(!super.equals(obj))
            return false;
        if(!ConferenceTweets.class.isAssignableFrom(obj.getClass()))
            return false;
        ConferenceTweets o = (ConferenceTweets)obj;
        return Objects.equals(tweets, o.tweets);
    }
}
