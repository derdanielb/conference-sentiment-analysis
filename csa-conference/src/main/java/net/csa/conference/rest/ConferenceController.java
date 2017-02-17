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

	@CrossOrigin(origins = "*")
	@RequestMapping(path = "/add", method = RequestMethod.POST)
	public ResponseEntity<Void> add(@RequestBody Conference conference) {
		log.info("POST Request /conference/add");
		int result = conferenceRepositoryService.add(conference);
		if(result == 1) {
			return new ResponseEntity<Void>(HttpStatus.OK);
		} else if(result == 0) {
			return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@CrossOrigin(origins = "*")
	@RequestMapping(path = "/getByConferenceName/{conferenceName}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<Conference>> getConferenceByConferenceName(@PathVariable String conferenceName) {
		log.info("GET Request /conference/getByConferenceName");
		List<Conference> conference = conferenceRepositoryService.findByConferenceName(conferenceName);
		if(conference.isEmpty()) {
			return new ResponseEntity<List<Conference>>(HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<List<Conference>>(conference, HttpStatus.OK);
		}
	}

	@CrossOrigin(origins = "*")
	@RequestMapping(path = "/getByTwitterHashtag/{hashtag}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<Conference>> getConferenceByTwitterHashtag(@PathVariable String hashtag) {
		log.info("GET Request /conference/getByTwitterHashtag");
		List<Conference> conferences = conferenceRepositoryService.findByTwitterHashTag(hashtag);
		if(conferences.isEmpty()) {
			return new ResponseEntity<List<Conference>>(HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<List<Conference>>(conferences, HttpStatus.OK);
		}
	}

	@CrossOrigin(origins = "*")
	@RequestMapping(path = "/update", method = RequestMethod.POST)
	public ResponseEntity<Void> updateConference(@RequestBody Conference conference) {
		log.info("POST Request /conference/update");
		int result = conferenceRepositoryService.update(conference);
		if(result == 1) {
			return new ResponseEntity<Void>(HttpStatus.OK);
		} else if(result == 0) {
			return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
		} else if(result == -1) {
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@CrossOrigin(origins = "*")
	@RequestMapping(path = "/delete", method = RequestMethod.POST)
	public ResponseEntity<Void> delete(@RequestBody Conference conference) {
		log.info("DELETE Request /conference/delete");
		int result = conferenceRepositoryService.delete(conference);
		if(result == 1) {
			return new ResponseEntity<Void>(HttpStatus.OK);
		} else if(result == 0) {
			return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
		} else if(result == -1) {
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@CrossOrigin(origins = "*")
	@RequestMapping(path = "/deleteAll", method = RequestMethod.POST)
	public ResponseEntity<Void> deleteAll() {
		log.info("DELETE Request /conference/deleteAll");
		int result = conferenceRepositoryService.deleteAll();
		if(result == 1) {
			return new ResponseEntity<Void>(HttpStatus.OK);
		} else if(result == 0) {
			return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@CrossOrigin(origins = "*")
	@RequestMapping(path = "/createTestdata/{count}", method = RequestMethod.POST)
	public ResponseEntity<Void> createTestdata(@PathVariable String count) {
		log.info("POST Request /conference/createTestdata");
		int result = conferenceRepositoryService.createTestdata(Integer.parseInt(count));
		if(result == 1) {
			return new ResponseEntity<Void>(HttpStatus.OK);
		} else if(result == 0) {
			return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
