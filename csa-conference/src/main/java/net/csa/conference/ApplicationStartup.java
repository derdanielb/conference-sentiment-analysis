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
    }
}