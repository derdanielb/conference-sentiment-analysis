package net.csa.conference.rest;

import net.csa.conference.model.Conference;
import net.csa.conference.repository.ConferenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by leoneck on 15.12.16.
 */
@RestController
@RequestMapping("/conference")
public class ConferenceController {

    @Autowired
    private ConferenceRepository repository;

    @RequestMapping(path = "/{UUID}", method = RequestMethod.GET, produces = "application/json")
    public Conference getConferenceById(@PathVariable String UUID) {
        return repository.findOne(UUID);
    }

    @RequestMapping(path = "/name/{conferenceName}", method = RequestMethod.GET, produces = "application/json")
    public List<Conference> getConferenceByName(@PathVariable String conferenceName) {
        List<Conference> returnConference = null;
        try {
            returnConference = repository.findByName(conferenceName).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return returnConference;
    }

    @RequestMapping(path = "/twitterhashtag/{twitterHashtag}", method = RequestMethod.GET, produces = "application/json")
    public List<Conference> getConferenceByTwitterHashtag(@PathVariable String twitterHashtag) {
        List<Conference> returnConference = null;
        try {
            returnConference = repository.findByTwitterHashtag(twitterHashtag).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return returnConference;
    }

    @RequestMapping(path = "/veranstaltungsortadressestadt/{veranstaltungsortAdresseStadt}", method = RequestMethod.GET, produces = "application/json")
    public List<Conference> getConferenceByVeranstaltungsort(@PathVariable String veranstaltungsortAdresseStadt) {
        List<Conference> returnConference = null;
        try {
            returnConference = repository.findByVeranstaltungsortAdresseStadt(veranstaltungsortAdresseStadt).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return returnConference;
    }

    @RequestMapping(path = "/organisatorname/{organisatorName}", method = RequestMethod.GET, produces = "application/json")
    public List<Conference> getConferenceByOrganisatorName(@PathVariable String organisatorName) {
        List<Conference> returnConference = null;
        try (Stream<Conference> stream = repository.findAllByOrganisatorenName("Mustermann")) {
            returnConference = stream.collect(Collectors.toList());
        }

        return returnConference;
    }

    @RequestMapping(method=RequestMethod.POST)
    public Conference create(@RequestBody Conference conference) {
        return repository.save(conference);
    }

    @RequestMapping(method=RequestMethod.PUT)
    public Conference put(@RequestBody Conference conference) {
        return repository.save(conference);
    }

    @RequestMapping(path = "/{UUID}", method=RequestMethod.DELETE)
    public void delete(@PathVariable String UUID) {
        repository.delete(UUID);
    }
}
