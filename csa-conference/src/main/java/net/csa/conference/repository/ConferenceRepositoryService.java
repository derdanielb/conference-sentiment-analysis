package net.csa.conference.repository;

import net.csa.conference.model.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

/**
 * @author philipp.amkreutz
 */

@Service
@EnableAsync
public class ConferenceRepositoryService {

	private static final Logger log = Logger.getLogger(ConferenceRepositoryService.class);

	@Autowired
	private ConferenceRepository repository;

	@Async
	public Future<List<Conference>> findByLocationName(String locationName) {
		return repository.findByLocationName(locationName);
	}

	public List<Conference> findByConferenceName(String conferenceName) {
		return repository.findByConferenceName(conferenceName);
	}

	public List<Conference> findByTwitterHashTag(String twitterHashTag) {
		return repository.findByTwitterHashTag(twitterHashTag);
	}

	public List<Conference> findAll() {
		return repository.findAll();
	}

	public int createTestdata(int count) {
		repository.deleteAll();
		List<Conference> conferenceList = new ArrayList<>();
		Person person = new Person("Philipp", "Amkreutz");
		Group group = new Group("TolleGruppe");
		Organisation organisation = new Organisation("TolleOrganisation");
		List<Organizer> organizerList = new ArrayList<>();
		organizerList.add(person);
		organizerList.add(group);
		organizerList.add(organisation);
		List<Sponsor> sponsorList = new ArrayList<>();
		sponsorList.add(person);
		sponsorList.add(group);
		sponsorList.add(organisation);
		for (int i = 1; i <= count; i++) {
			Conference c = new Conference("TestKonferenz" + i, "from", "to", "TestLocationName", "Auf der Höhe", "28", "51429",
					"Bergisch Gladbach", "DE", "test", organizerList, sponsorList);
			conferenceList.add(c);
		}
		try {
			repository.save(conferenceList);
		} catch (DataAccessException e) {
			return 0;
		}
		log.info(count + " Testdaten persistiert");
		return 1;
	}

	public int add(Conference conference) {
		conference.initGeoLocation();
		try {
			repository.save(conference);
		} catch (DataAccessException e) {
			return 0;
		}
		log.info("Konferenz " + conference.getConferenceName() + " erfolgreich erstellt und persistiert");
		return 1;
	}

	public int update(Conference conference) {
		if (conference.getUuid() == null) {
			log.info("Konferenz " + conference.getConferenceName() + " hat keine UUID");
			return -1;
		}
		Conference old = repository.findByUuid(conference.getUuid());
		if (!(conference.getStreet().equals(old.getStreet()) && conference.getHouseNumber().equals(old.getHouseNumber()) &&
				conference.getPostcode().equals(old.getPostcode()) && conference.getCity().equals(old.getCity()) &&
				conference.getCountry().equals(old.getCountry()))) {
			conference.initGeoLocation();
		}
		try {
			repository.save(conference);
		} catch (DataAccessException e) {
			return 0;
		}
		log.info("Konferenz: " + conference.getConferenceName() + " erfolgreich geupdatet!");
		return 1;
	}

	public int delete(Conference conference) {
		Conference toDelete = repository.findByUuid(conference.getUuid());
		if (toDelete == null) {
			log.info("Konferenz mit der UUID: " + conference.getUuid() + " wurde nicht gefunden");
			return -1;
		}
		try {
			repository.delete(conference);
		} catch (DataAccessException e) {
			return 0;
		}
		log.info("Konferenz " + conference.getConferenceName() + " erfolgreich gelöscht");
		return 1;
	}

	public int deleteAll() {
		try {
			repository.deleteAll();
		} catch (DataAccessException e) {
			return 0;
		}
		log.info("Alle Konferenzen gelöscht");
		return 1;
	}

}
