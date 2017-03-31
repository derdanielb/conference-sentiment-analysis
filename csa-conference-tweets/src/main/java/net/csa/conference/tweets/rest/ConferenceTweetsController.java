package net.csa.conference.tweets.rest;

import net.csa.conference.tweets.model.ConferencesWithTweets;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;


import java.util.HashMap;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@RestController
@RequestMapping("/conferencetweets")
public class ConferenceTweetsController {

    @Value("${host_conference}")
    private String host_conference;

    @Value("${host_twitter_search}")
    private String host_twitter_search;

    private static final Logger log = getLogger(ConferenceTweetsController.class);

    //READ per hashtag
    @RequestMapping(path = "/hashtag/{hashtag}", method = RequestMethod.GET)
    @CrossOrigin(origins="*")
    ConferencesWithTweets findByHashtag(@PathVariable String hashtag) {

        log.info("Search for Conference and tweets with hashtag: " + hashtag);

        //use rest api from csa-conference
        RestTemplate restTemplateConference = new RestTemplate();
        String conferenceURL = "http://" + host_conference + ":8080/conference/hashtag/" + hashtag;
        ResponseEntity responseConference = restTemplateConference.getForEntity(conferenceURL , List.class);
        List<HashMap> conferences = (List<HashMap>) responseConference.getBody();
        log.info("Conferences found: " + conferences.size());

        //use rest api from csa-twitter-search
        RestTemplate restTemplateTwitterSearch = new RestTemplate();
        String twitterSearchURL = "http://" + host_twitter_search + ":8090/twitter/search/" + hashtag;
        ResponseEntity responseTwitterSearch = restTemplateTwitterSearch.getForEntity(twitterSearchURL , List.class);
        List<String> tweets = (List<String>) responseTwitterSearch.getBody();
        log.info("Tweets found: " + tweets.size());

        //combine the results of both rest apis
        ConferencesWithTweets conferencesWithTweets = new ConferencesWithTweets(conferences, tweets);


        return conferencesWithTweets;
    }

}
