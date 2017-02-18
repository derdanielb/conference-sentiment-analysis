package net.csa.conference.tweets.integration;

import org.apache.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author philipp.amkreutz
 */
@Service
public class ConferenceIntegration {

	private static Logger log = Logger.getLogger(ConferenceIntegration.class);

	public List<HashMap> getConferences(String hashtag) {
		RestTemplate restTemplate = new RestTemplate();
//		Deactivate for local test / Activate for Docker ->
		String getConferencesByTwitterHashtagURL = "http://csa_conference_01:8080/conference/getByTwitterHashtag/" + hashtag;
//		Activate for local Test / Deactivate for Docker ->
//		String getConferencesByTwitterHashtagURL = "http://localhost:8080/conference/getByTwitterHashtag/" + hashtag;
		try {
			ResponseEntity responseEntity = restTemplate.getForEntity(getConferencesByTwitterHashtagURL, List.class);
			return (List<HashMap>) responseEntity.getBody();
		} catch (HttpClientErrorException e) {
			return new ArrayList<>();
		}
	}

}
