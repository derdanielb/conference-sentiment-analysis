package net.csa.conference.repository;

import net.csa.conference.model.Conference;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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

	public void createTestdata(int count) {
		repository.deleteAll();
		List<Conference> conferenceList = new ArrayList<>();
		String[] organizerStringArray = {"Person;Philipp;Amkreutz", "Group;TolleGruppe", "Organisation;TolleOrganisation"};
		String[] sponsorStringArray = {"Person;Philipp;Amkreutz", "Group;TolleGruppe", "Organisation;TolleOrganisation"};
		for (int i = 1; i <= count; i++) {
			Conference c = new Conference("TestKonferenz" + i, "from", "to", "TestLocationName", "Auf der Höhe", "28", "51429",
					"Bergisch Gladbach", "DE", "test", organizerStringArray, sponsorStringArray);
			conferenceList.add(c);
		}
		repository.save(conferenceList);
		log.info(count + " Testdaten persistiert");
	}

	public Conference add(Conference conference) {
		conference.initGeoLocation();
		repository.save(conference);
		log.info("Konferenz " + conference.getConferenceName() + " erfolgreich erstellt und persistiert");
		return conference;
	}

	public Conference update(Conference conference) {
		if (conference.getUuid() == null) {
			log.info("Konferenz mit der UUID: " + conference.getConferenceName() + " wurde nicht gefunden");
			return new Conference();
		}
		Conference old = repository.findByUuid(conference.getUuid());
		if (!(conference.getStreet().equals(old.getStreet()) && conference.getHouseNumber().equals(old.getHouseNumber()) &&
				conference.getPostcode().equals(old.getPostcode()) && conference.getCity().equals(old.getCity()) &&
				conference.getCountry().equals(old.getCountry()))) {
			conference.initGeoLocation();
		}
		repository.save(conference);
		log.info("Konferenz: " + conference.getConferenceName() + " erfolgreich geupdatet!");
		return conference;
	}

	public String delete(Conference conference) {
		Conference toDelete = repository.findByUuid(conference.getUuid());
		if(toDelete == null) {
			log.info("Konferenz mit der UUID: " + conference.getConferenceName() + " wurde nicht gefunden");
			return "0";
		}
		repository.delete(conference);
		log.info("Konferenz " + conference.getConferenceName() + " erfolgreich gelöscht");
		return "1";
	}

	public String deleteAll() {
		repository.deleteAll();
		log.info("Alle Konferenzen gelöscht");
		return "1";
	}

}
