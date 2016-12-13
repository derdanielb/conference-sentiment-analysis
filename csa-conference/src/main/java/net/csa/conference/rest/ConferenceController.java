package net.csa.conference.rest;

import net.csa.conference.model.Conference;
import net.csa.conference.repository.ConferenceRepository;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.inject.Inject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
            return createError("Invalid conference id: " + id);
        } catch (Throwable t) {
            return createServerError(t);
        }
    }

    @RequestMapping(path = "/conferences", method = RequestMethod.POST)
    public ResponseEntity<?> postConference(@RequestBody Conference conference){
        try {
            conference.generateUUID();
            Conference c = repository.save(conference).get();
            if(c != null) {
                return ResponseEntity.created(
                        ServletUriComponentsBuilder.fromCurrentContextPath()
                                .path("/csa/v1/conferences/" + c.getUuid())
                                .build()
                                .toUri()
                ).body(c);
            } else
                return createServerError("Conference is null");
        } catch (Throwable t) {
            return createServerError(t);
        }
    }

    @RequestMapping(path = "/conferences/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> putConference(@PathVariable String id, @RequestBody Conference conference){
        try {
            conference.setUuid(UUID.fromString(id));
            if(repository.exists(conference.getUuid()).get()){
                Conference c = repository.save(conference).get();
                if(c != null)
                    return ResponseEntity.ok(c);
                else
                    return createServerError("Conference is null");
            }
            else
                return createError("Conference with id does not exists: " + conference.getUuid(), HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return createError("Invalid conference id: " + id);
        } catch (Throwable t) {
            return createServerError(t);
        }
    }
    @RequestMapping(path = "/conferences/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteConference(@PathVariable String id){
        try {
            repository.delete(UUID.fromString(id)).get();
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return createError("Invalid conference id: " + id);
        } catch (Throwable t) {
            return createServerError(t);
        }
    }

    @RequestMapping(path = "/conferences", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteAllConferences(){
        try {
            repository.deleteAll().get();
            return ResponseEntity.noContent().build();
        } catch (Throwable t) {
            return createServerError(t);
        }
    }
}
