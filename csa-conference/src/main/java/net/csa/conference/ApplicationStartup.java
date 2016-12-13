package net.csa.conference;

import net.csa.conference.model.*;
import net.csa.conference.repository.ConferenceRepository;
import net.csa.conference.rest.ConferenceController;
import org.slf4j.Logger;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by zelle on 13.12.2016.
 */
@Component
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {

    private static final Logger log = getLogger(ConferenceController.class);

    private ConferenceRepository repository;

    @Inject
    public ApplicationStartup(ConferenceRepository repository){
        this.repository = repository;
    }

    /**
     * This event is executed as late as conceivably possible to indicate that
     * the application is ready to service requests.
     */
    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        repository.deleteAll();
        repository.save(createConference());
    }

    private static Conference createConference() {
        Conference c = new Conference();
        c.setHashTag("baum");
        c.setName("Baum");
        List<Persona> orcs = new ArrayList<>();
        orcs.add(new Organisation("o1"));
        orcs.add(new Group("g1"));
        orcs.add(new Person("Ralf", "Baum"));
        c.setOrganisers(orcs);
        List<Persona> spon = new ArrayList<>();
        spon.add(new Organisation("o2"));
        spon.add(new Group("g2"));
        spon.add(new Person("Rolf", "B42"));
        c.setSponsors(spon);
        c.setTimeSpan(new TimeSpan(new Date(2016, 1, 1),
                new Date(2016, 1, 2)));
        EventLocation el = new EventLocation();
        el.setName("Loc");
        el.setGeoLocation(new Location(42, 42));
        Address a = new Address();
        a.setCountry("DE");
        a.setNumber(42);
        a.setStreet("BaumStreet");
        a.setTown("BaumCity");
        a.setZipCode(424242);
        el.setAddress(a);
        return c;
    }
}