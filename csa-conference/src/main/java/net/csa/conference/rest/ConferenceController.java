package net.csa.conference.rest;

import net.csa.conference.model.Conference;
import net.csa.conference.repository.ConferenceRepository;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by Felix on 13.12.2016.
 */
@RestController
@RequestMapping("/confApi")
public class ConferenceController {
    private static final Logger log = getLogger(ConferenceController.class);

    private ConferenceRepository repository;

    @Inject
    public ConferenceController(ConferenceRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(path = "/conference", method = RequestMethod.GET)
    public List<Conference> listConferences(){
        try {
            List<Conference> target = new ArrayList<>();
            for (Conference c : repository.findAll().get())
                target.add(c);
            return target;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
