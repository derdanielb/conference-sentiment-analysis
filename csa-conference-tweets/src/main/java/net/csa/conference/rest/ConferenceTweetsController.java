package net.csa.conference.rest;

import net.csa.conference.model.Conference;
import net.csa.conference.model.ConferenceTweets;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static net.csa.conference.rest.ErrorGenerator.createServerError;
import static org.slf4j.LoggerFactory.getLogger;

@RestController
@RequestMapping("/csa/v1")
public class ConferenceTweetsController {
    private static final Logger log = getLogger(ConferenceTweetsController.class);

    @Value("${conference.host}")
    private String ConferenceHost;
    @Value("${search.host}")
    private String SearchHost;

    @RequestMapping(path = "/conferences", method = RequestMethod.GET, params = {"name"})
    public ResponseEntity<?> findByNameContaining(@RequestParam String name){
        try {
            List<ConferenceTweets> confTweets = new ArrayList<>();

            RestTemplate t = new RestTemplate();
            Conference[] conferences = t.getForObject("http://" + ConferenceHost + "/csa/v1/conferences?name=" + name, Conference[].class);//TODO url escape
            for (Conference conference : conferences) {
                String[] tw = t.getForObject("http://" + SearchHost + "/twitter/search/" + conference.getHashTag(), String[].class);
                confTweets.add(new ConferenceTweets(conference, tw));
            }

            return ResponseEntity.ok(confTweets);
        } catch (Throwable t) {
            return createServerError(t);
        }
    }
}
