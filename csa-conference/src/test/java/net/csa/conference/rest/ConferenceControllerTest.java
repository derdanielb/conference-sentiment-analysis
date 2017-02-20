package net.csa.conference.rest;


import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
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

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.when;
import static net.csa.conference.TestDataGenerator.createConference;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

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

    @Test
    public void testGet() {
        try {
            List<Conference> list = new ArrayList<>();
            for (int i = 0; i < 10; i++)
                list.add(repository.save(createConference()).get());

            when()
                    .get("/csa/v1/conferences/" + list.get(0).getUuid().toString())
                    .then()
                    .statusCode(HttpStatus.SC_OK)
                    .body("uuid", equalTo(list.get(0).getUuid().toString()));
        } catch (InterruptedException | ExecutionException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testGetByName() {
        try {
            Conference c1 = createConference();
            c1.setName("Baum42");
            repository.save(c1).get();
            Conference c2 = createConference();
            c2.setName("Baum666");
            repository.save(c2).get();

            when()
                .get("/csa/v1/conferences?name=" + c1.getName())
            .then()
                .statusCode(HttpStatus.SC_OK)
                .body("uuid", contains(c1.getUuid().toString()));
        } catch (InterruptedException | ExecutionException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testPost() {
        try {
            given()
                    .body("{\"name\":\"SuperBaum\"}")
                    .contentType(ContentType.JSON)
                    .when()
                    .post("/csa/v1/conferences")
                    .then()
                    .statusCode(HttpStatus.SC_CREATED)
                    .body("name", equalTo("SuperBaum"))
                    .body("uuid", equalTo(findUUID("SuperBaum")));

        } catch (InterruptedException | ExecutionException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testPut() {
        try {
            Conference c = repository.save(createConference()).get();

            given()
                .body("{\"name\":\"SuperBaum\"}")
                .contentType(ContentType.JSON)
            .when()
                .put("/csa/v1/conferences/" + c.getUuid().toString())
            .then()
                .statusCode(HttpStatus.SC_OK)
                .body("name", equalTo("SuperBaum"))
                .body("uuid", equalTo(c.getUuid().toString()));

            assertEquals("SuperBaum", repository.findOne(c.getUuid()).get().getName());

        } catch (InterruptedException | ExecutionException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testDelete() {
        try {
            Conference c1 = repository.save(createConference()).get();
            Conference c2 = repository.save(createConference()).get();
            Conference c3 = repository.save(createConference()).get();

            when()
                .delete("/csa/v1/conferences/" + c2.getUuid().toString())
            .then()
                .statusCode(HttpStatus.SC_NO_CONTENT);

            assertNull(repository.findOne(c2.getUuid()).get());
            assertEquals(new Long(2), repository.count().get());

        } catch (InterruptedException | ExecutionException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testDeleteAll() {
        try {
            List<Conference> list = new ArrayList<>();
            for (int i = 0; i < 10; i++)
                list.add(repository.save(createConference()).get());

            when()
                .delete("/csa/v1/conferences")
            .then()
                .statusCode(HttpStatus.SC_NO_CONTENT);

            assertEquals(new Long(0), repository.count().get());
        } catch (InterruptedException | ExecutionException e) {
            fail(e.getMessage());
        }
    }

    @SuppressWarnings("LoopStatementThatDoesntLoop")
    private String findUUID(String name) throws InterruptedException, ExecutionException{
        for (Conference c : repository.findByNameContaining(name).get())
            return c.getUuid().toString();
        return null;
    }
}
