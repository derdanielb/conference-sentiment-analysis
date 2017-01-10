package net.csa.conference.rest;

import net.csa.conference.model.Conference;
import net.csa.conference.repository.ConferenceRepositoryService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author philipp.amkreutz
 */

@RestController
@RequestMapping(path = "/conference")
public class ConferenceController {

	private static final Logger log = Logger.getLogger(ConferenceController.class);

	@Autowired
	private ConferenceRepositoryService conferenceRepositoryService;

	@RequestMapping(value = "/")
	public List<Conference> home() {
		log.info("GET Request /conference");
		List<Conference> conferences = conferenceRepositoryService.findAll();
		return conferences;
	}

	@CrossOrigin(origins = "*")
	@RequestMapping(path = "/add", method = RequestMethod.POST)
	public Conference add(@RequestBody Conference conference) {
		log.info("POST Request /conference/add");
		Conference returnConference = conferenceRepositoryService.add(conference);
		return returnConference;
	}

	@CrossOrigin(origins = "*")
	@RequestMapping(path = "/getByConferenceName/{conferenceName}", method = RequestMethod.GET, produces = "application/json")
	public List<Conference> getConferenceByConferenceName(@PathVariable String conferenceName) {
		log.info("GET Request /conference/getByConferenceName");
		List<Conference> conference = conferenceRepositoryService.findByConferenceName(conferenceName);
		return conference;
	}

	@CrossOrigin(origins = "*")
	@RequestMapping(path = "/getByTwitterHashtag/{hashtag}", method = RequestMethod.GET, produces = "application/json")
	public List<Conference> getConferenceByTwitterHashtag(@PathVariable String hashtag) {
		log.info("GET Request /conference/getByTwitterHashtag");
		List<Conference> conferences = conferenceRepositoryService.findByTwitterHashTag(hashtag);
		return conferences;
	}

	@CrossOrigin(origins = "*")
	@RequestMapping(path = "/update", method = RequestMethod.POST)
	public Conference updateConference(@RequestBody Conference conference) {
		log.info("POST Request /conference/update");
		Conference returnConference = conferenceRepositoryService.update(conference);
		return returnConference;
	}

	@CrossOrigin(origins = "*")
	@RequestMapping(path = "/delete", method = RequestMethod.POST)
	public String delete(@RequestBody Conference conference) {
		log.info("DELETE Request /conference/delete");
		String returnString = conferenceRepositoryService.delete(conference);
		return returnString;
	}

	@CrossOrigin(origins = "*")
	@RequestMapping(path = "/deleteAll", method = RequestMethod.POST)
	public String deleteAll() {
		log.info("DELETE Request /conference/deleteAll");
		String returnString = conferenceRepositoryService.deleteAll();
		return returnString;
	}

	@CrossOrigin(origins = "*")
	@RequestMapping(path = "/createTestdata/{count}", method = RequestMethod.POST)
	public String createTestdata(@PathVariable String count) {
		log.info("POST Request /conference/createTestdata");
		conferenceRepositoryService.createTestdata(Integer.parseInt(count));
		return count;
	}

}
