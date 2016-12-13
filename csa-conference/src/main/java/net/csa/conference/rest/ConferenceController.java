package net.csa.conference.rest;

import net.csa.conference.model.Conference;
import net.csa.conference.repository.ConferenceRepository;
import org.apache.catalina.connector.Response;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static net.csa.conference.rest.ErrorGenerator.createError;
import static net.csa.conference.rest.ErrorGenerator.createNotFound;
import static net.csa.conference.rest.ErrorGenerator.createServerError;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by Felix on 13.12.2016.
 */
@RestController
@RequestMapping("/csa/v1")
public class ConferenceController {
    private static final Logger log = getLogger(ConferenceController.class);

    private ConferenceRepository repository;

    @Inject
    public ConferenceController(ConferenceRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(path = "/conferences", method = RequestMethod.GET)
    public ResponseEntity<?> listConferences(){
        try {
            List<Conference> target = new ArrayList<>();
            for (Conference c : repository.findAll().get())
                target.add(c);
            return ResponseEntity.ok(target);
        } catch (Throwable t) {
            return createServerError(t);
        }
    }

    @RequestMapping(path = "/conferences/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getConference(@PathVariable String id){
        try {
            Conference c = repository.findOne(UUID.fromString(id)).get();
            if(c != null)
                return ResponseEntity.ok(c);
            else
                return createNotFound(id);
        } catch (IllegalArgumentException e) {
            return createError("Error: Invalid conference id: " + id);
        } catch (Throwable t) {
            return createServerError(t);
        }
    }
}
