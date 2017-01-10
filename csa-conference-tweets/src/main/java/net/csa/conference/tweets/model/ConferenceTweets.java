package net.csa.conference.tweets.model;

import java.util.HashMap;
import java.util.List;

/**
 * @author philipp.amkreutz
 */
public class ConferenceTweets {

	private List<HashMap> conferences;
	private List<String> tweets;

	public ConferenceTweets(List<HashMap> conferences, List<String> tweets) {
		this.conferences = conferences;
		this.tweets = tweets;
	}

	public List<HashMap> getConferences() {
		return conferences;
	}

	public List<String> getTweets() {
		return tweets;
	}
}
