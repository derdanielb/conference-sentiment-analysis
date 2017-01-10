package net.csa.conference.tweets.integration;

import org.apache.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @author philipp.amkreutz
 */
@Service
public class TwitterIntegration {

	private static Logger log = Logger.getLogger(TwitterIntegration.class);

	public List<String> getTweets(String hashtag) {
		RestTemplate restTemplate = new RestTemplate();
//		Deactivate for local test / Activate for Docker ->
		String getConferencesByTwitterHashtagURL = "http://csa_twitter_search_01:8080/twitter/search/" + hashtag;
//		Activate for local Test / Deactivate for Docker ->
//		String getConferencesByTwitterHashtagURL = "http://localhost:8070/twitter/search/" + hashtag;
		ResponseEntity responseEntity = restTemplate.getForEntity(getConferencesByTwitterHashtagURL, List.class);
		return (List<String>) responseEntity.getBody();
	}

}
