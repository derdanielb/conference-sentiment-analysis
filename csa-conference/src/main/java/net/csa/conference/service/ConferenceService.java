package net.csa.conference.service;

import net.csa.conference.model.*;
import net.csa.conference.repository.ConferenceRepository;
import net.csa.conference.testdata.Testdata;
import org.mockito.internal.matchers.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Future;

@Service
public class ConferenceService {

    @Autowired
    private ConferenceRepository repository;

    //CREATE and UPDATE
    public Conference save(Conference conference) {
        return repository.save(conference);
    }

    //READ
    public Conference findOne(String uuid) {
        return repository.findOne(uuid);
    }

    public List<Conference> findAll() {
        return repository.findAll();
    }

    public List<Conference> findByName(String name) {
        return repository.findByName(name);
    }

    @Async
    public Future<List<Conference>> findByLocation(String location) {
        return repository.findByLocation(location);
    }

    public List<Conference> findByStartdatetime(LocalDateTime startdatetime) {
        return repository.findByStartdatetime(startdatetime);
    }

    public List<Conference> findByEnddatetime(LocalDateTime enddatetime) {
        return repository.findByEnddatetime(enddatetime);
    }

    public List<Conference> findByAddress(Address address) {
        return repository.findByAddress(address);
    }

    public List<Conference> findByGeolocation(GeoLocation geolocation) {
        return repository.findByGeolocation(geolocation);
    }

    public List<Conference> findByHashtag(String hashtag) {
        return repository.findByHashtag(hashtag);
    }

    public List<Conference> findByOrganisers(AbstractOrganiserSponsor aos) {
        return repository.findByOrganisers(aos);
    }

    public List<Conference> findBySponsors(AbstractOrganiserSponsor aos) {
        return repository.findBySponsors(aos);
    }

    public List<Conference> findByNameContaining(String name) {
        return repository.findByNameContaining(name);
    }

    public List<Conference> findByHashtagContaining(String hashtag) {
        return repository.findByHashtagContaining(hashtag);
    }

    //DELETE
    public List<Conference> deleteByName(String name) {
        return repository.deleteByName(name);
    }

    public List<Conference> deleteByHashtag(String hashtag) {
        return repository.deleteByHashtag(hashtag);
    }

    public Conference delete(String id) {
        Conference conference = repository.findOne(id);
        repository.delete(id);
        return conference;
    }

    public List<Conference> deleteAll() {
        List<Conference> conferences = repository.findAll();
        repository.deleteAll();
        return conferences;
    }

    public void createTestDB() {
        //delete all data
        repository.deleteAll();

        //add test data
        ArrayList<AbstractOrganiserSponsor> organisersSponsors = new ArrayList<>(Arrays.asList(Testdata.aos));

        for(int i=0; i< Testdata.names.length; i++) {
            Conference conference = new Conference(Testdata.names[i]);
            conference.setStartDateTime(Testdata.startdatetimes[i]);
            conference.setEnddatetime(Testdata.enddatetimes[i]);
            conference.setLocation(Testdata.locations[i]);
            conference.setAddress(Testdata.addresses[i]);
            conference.setGeolocation(Testdata.geolocations[i]);
            conference.setHashtag(Testdata.hashtags[i]);

            int numberOrganisers = (int) ((Math.random()*4)+1);
            int numberSponsors = (int) ((Math.random()*8)+1);

            Collections.shuffle(organisersSponsors);

            for(int j = 0; j<numberOrganisers; j++) {
                conference.addOrganiser(organisersSponsors.get(j));
            }

            Collections.shuffle(organisersSponsors);

            for(int n = 0; n<numberSponsors; n++) {
                conference.addSponsor(organisersSponsors.get(n));
            }

            if(conference.getName().equals("Javaland")) {
                conference.addOrganiser(new Organisation("Corp Z"));
                conference.addSponsor(new NaturalPerson("Snowden", "Edward"));
            }

            repository.save(conference);
        }
    }
}
