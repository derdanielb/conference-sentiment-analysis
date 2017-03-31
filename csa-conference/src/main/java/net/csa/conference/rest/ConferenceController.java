package net.csa.conference.rest;

import net.csa.conference.model.Conference;
import net.csa.conference.repository.ConferenceRepository;
import net.csa.conference.service.ConferenceService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Stream;

import static org.slf4j.LoggerFactory.getLogger;

@RestController
@RequestMapping("/conference")
public class ConferenceController {

    private static final Logger log = getLogger(ConferenceController.class);

    @Autowired
    ConferenceService service;

    //CREATE
    @RequestMapping(path = "/create", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @CrossOrigin(origins="*")
    Conference create(@RequestBody @Valid Conference conference) {
        log.debug("Added Conference with id + " + conference.getUuid());
        return service.save(conference);
    }

    //READ per uuid
    @RequestMapping(path = "/uuid/{uuid}", method = RequestMethod.GET)
    @CrossOrigin(origins="*")
    Conference findById(@PathVariable String uuid) {
        log.debug("Search for Conference with id " + uuid);
        return service.findOne(uuid);
    }

    //READ all
    @RequestMapping(method = RequestMethod.GET)
    @CrossOrigin(origins="*")
    List<Conference> findAll() {
        log.debug("Search for all Conferences");
        return service.findAll();
    }

    //READ by name
    @RequestMapping(path = "/name/{name}", method = RequestMethod.GET)
    @CrossOrigin(origins="*")
    List<Conference> findByNameContaining(@PathVariable String name) {
        log.debug("Search for Conferences with name " + name);
        return service.findByNameContaining(name);
    }

    //READ by hashtag
    @RequestMapping(path = "/hashtag/{hashtag}", method = RequestMethod.GET)
    @CrossOrigin(origins="*")
    List<Conference> findByHashtag(@PathVariable String hashtag) {
        log.debug("Search for Conferences with hashtag " + hashtag);
        return service.findByHashtag(hashtag);
    }

    //UPDATE
    @RequestMapping(path = "/update/{uuid}", method = RequestMethod.PUT)
    @CrossOrigin(origins="*")
    Conference update(@RequestBody @Valid Conference conference) {
        validateConferenceByUuid(conference.getUuid());
        log.debug("Update Conference with id " + conference.getUuid());
        return service.save(conference);
    }

    //DELETE
    @RequestMapping(path = "/delete/{uuid}", method = RequestMethod.DELETE)
    @CrossOrigin(origins="*")
    void delete(@PathVariable String uuid) {
        validateConferenceByUuid(uuid);
        log.debug("Delete Conference with id " + uuid);
        service.delete(uuid);
    }

    //DELETE Database
    @RequestMapping(path = "/clearDB", method = RequestMethod.GET)
    @CrossOrigin(origins="*")
    String clearDatabase() {
        log.debug("Clear Database");
        service.deleteAll();
        return "Database cleared.";
    }

    //CREATE Test Database
    @RequestMapping(path = "/createTestDB", method = RequestMethod.GET)
    @CrossOrigin(origins="*")
    String createTestDB() {
        log.debug("Create Test Database");
        service.createTestDB();
        return "Test database created.";
    }

    private void validateConferenceByUuid(String uuid) {
        Conference conference = service.findOne(uuid);
        if(conference == null) throw new ConferenceNotFoundException(uuid);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    private class ConferenceNotFoundException extends RuntimeException {
        public  ConferenceNotFoundException(String uuid) {
            super("Conference with uuid " + uuid + " not found.");
        }
    }
}
