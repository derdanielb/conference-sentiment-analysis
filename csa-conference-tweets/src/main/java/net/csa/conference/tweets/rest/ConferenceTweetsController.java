package net.csa.conference.tweets.rest;

import net.csa.conference.tweets.integration.ConferenceIntegration;
import net.csa.conference.tweets.integration.TwitterIntegration;
import net.csa.conference.tweets.model.ConferenceTweets;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author philipp.amkreutz
 */

@RestController
@RequestMapping("/combinedSearch")
public class ConferenceTweetsController {

	private static Logger log = Logger.getLogger(ConferenceTweetsController.class);

	@Autowired
	private ConferenceIntegration conferenceIntegration;

	@Autowired
	private TwitterIntegration twitterIntegration;

	@CrossOrigin(origins = "*")
	@RequestMapping("/{hashtag}")
	public ResponseEntity<List<ConferenceTweets>> search(@PathVariable String hashtag) {
		List<HashMap> conferences = conferenceIntegration.getConferences(hashtag);
		List<String> tweets = twitterIntegration.getTweets(hashtag);
		ConferenceTweets conferenceTweets = new ConferenceTweets(conferences, tweets);
		List<ConferenceTweets> conferenceTweetsList = new ArrayList<>();
		conferenceTweetsList.add(conferenceTweets);
		return new ResponseEntity<List<ConferenceTweets>>(conferenceTweetsList, HttpStatus.OK);
	}

}
