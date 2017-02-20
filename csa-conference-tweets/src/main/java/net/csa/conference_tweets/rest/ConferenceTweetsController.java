package net.csa.conference_tweets.rest;

import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static net.csa.conference_tweets.rest.ErrorGenerator.createServerError;
import static org.slf4j.LoggerFactory.getLogger;

@RestController
@RequestMapping("/csa/v1")
public class ConferenceTweetsController {
    private static final Logger log = getLogger(ConferenceTweetsController.class);

    @RequestMapping(path = "/conferences", method = RequestMethod.GET, params = {"name"})
    public ResponseEntity<?> findByNameContaining(@RequestParam String name){
        try {
            return ResponseEntity.noContent().build();
        } catch (Throwable t) {
            return createServerError(t);
        }
    }
}
