package net.csa.conference.rest;


import com.jayway.restassured.RestAssured;
import net.csa.conference.ConferenceMicroservice;
import net.csa.conference.model.Conference;
import net.csa.conference.repository.ConferenceRepository;
import org.apache.http.HttpStatus;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static com.jayway.restassured.RestAssured.when;
import static net.csa.conference.TestDataGenerator.createConference;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ConferenceMicroservice.class)
@WebAppConfiguration
@IntegrationTest()
@SpringBootTest
public class ConferenceControllerTest {
    @Autowired
    private ConferenceRepository repository;
    @SuppressWarnings("unused")
    @Autowired
    private ConferenceController controller;

    @Before
    public void clearDatabase() throws ExecutionException, InterruptedException {
        repository.deleteAll().get();
        RestAssured.port = 8080;
    }

    @Test
    public void testGetAll() {
        try {
            List<Conference> list = new ArrayList<>();
            List<String> uuids = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                Conference c = repository.save(createConference()).get();
                list.add(c);
                uuids.add(c.getUuid().toString());
            }

            when()
                .get("/csa/v1/conferences")
            .then()
                .statusCode(HttpStatus.SC_OK)
                .body("uuid", hasSize(10))
                .body("uuid", containsInAnyOrder(uuids.toArray()));
        } catch (InterruptedException | ExecutionException e) {
            fail(e.getMessage());
        }
    }
}
